package cn.cy.base.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import cn.cy.base.config.ServerConfig;
import cn.cy.base.constraint.ConnectionDistributor;
import cn.cy.base.context.SocketContext;
import cn.cy.base.handler.EchoHandler;

/**
 * 监听请求的中心 , nio
 */
public class Listener implements ConnectionDistributor {

    private ServerConfig serverConfig;

    private Selector selector;

    private static SelectorProvider selectorProvider = SelectorProvider.provider();

    private ServerSocketChannel serverSocketChannel;

    private static Logger logger = LoggerContext.getContext().getLogger(Listener.class.getName());

    private ByteBuffer readBuffer;

    /**
     * 初始化selector
     */
    public void init() throws UnknownHostException {
        try {

            serverSocketChannel = selectorProvider.openServerSocketChannel();
            serverSocketChannel.configureBlocking(false);
            // 这一步帮忙把bind和listen都做了
            serverSocketChannel = serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8100));

            selector = selectorProvider.openSelector();

            // 对于serverSocketChannel, 感兴趣的是accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            readBuffer = ByteBuffer.allocate(2048);
        } catch (AlreadyBoundException abe) {

        } catch (ClosedChannelException cce) {

        } catch (IOException e) {

        }
    }

    /**
     * eventLoop函数, 无限循环, 接受连接
     */
    public void eventLoop() {
        try {
            // 开始进行selector的监听

            // 水平触发? 边缘触发?
            while (true) {
                if (selector.select() == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();

                // 遍历每一个selectionKey
                for (SelectionKey key : selectionKeySet) {
                    // 首先remove掉
                    selectionKeySet.remove(key);
                    // 监听到连接, 根据策略分发连接
                    if (key.isAcceptable()) {
                        logger.info(" server can accept , {}", key.channel().toString());
                        distributeAccept(key);
                    } else if (key.isReadable()) {
                        logger.info("server can read {}", key.channel().toString());
                        /**
                         * unix网络编程, IO模型中讲到的一下几种"套接字准备好写的状态"
                         * 1. 套接字的 接受缓冲区 的 字节数 > 套接字接收缓冲区低水位标记的当前大小, 返回的字节数 > 0
                         * 2. 读半部关闭, 字节数直接返回0
                         * 3. 套接字错误待处理, 返回 -1
                         */
                        distributeRead(key);
                    }
                    // 最好的做法应该是, 当且仅当准备写的时候, 才开始注册写事件
                }
            }

        } catch (ClosedByInterruptException cie) {
            cie.printStackTrace();
        } catch (ClosedChannelException cce) {
            cce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void distributeAccept(SelectionKey selectionKey) {
        logger.info("{}", selectionKey.selector().toString());
        // 这里是非阻塞的accept
        while (true) {
            SocketChannel remoteChannel = null;
            try {
                remoteChannel = serverSocketChannel.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (remoteChannel != null) {
                try {
                    remoteChannel.configureBlocking(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    // 注意, 注册和selector要在同一个线程里做
                    // echo Handler
                    SocketContext socketContext = SocketContext.buildSocketContext(selectionKey, new EchoHandler(),
                            remoteChannel);

                    /**
                     * 关于事件分发
                     * 貌似通常做法是, 利用selectionKey的attach方法，来附带一个"带外数据"
                     * 然后每次获取事件之后, 通过attach变量访问到当前连接的上下文
                     * 一个selectionKey只能attach一个东西
                     *
                     * 这个attach在调用的时候,要注意,别attach错selectionKey
                     * 之前踩了一个坑, 就是一直attach到那个专门用来accept的selectionKey上去
                     * 这里nio给我们提供了一个register方法的重载, 在这个过程中就可以把内容attach上去
                     * 可以说是非常贴心的
                     *
                     * ps: 不过里面的findKey方法的时间复杂度居然是O(n)的。。。。
                     */
                    // 在这里attach
                    remoteChannel.register(selectionKey.selector(), SelectionKey.OP_READ, socketContext);
                    logger.info(" selector key num: {}", selectionKey.selector().keys().size());
                } catch (ClosedChannelException e) {
                    logger.error(e.getMessage());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                break;
            }
        }
    }

    @Override
    public void distributeRead(SelectionKey selectionKey) {
        SocketContext socketContext = (SocketContext) selectionKey.attachment();

        socketContext.fireReadEvent();
    }

    public static void main(String[] args) throws UnknownHostException {
        Listener listener = new Listener();
        listener.init();

        listener.eventLoop();
    }
}
