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
import java.nio.charset.Charset;
import java.util.Set;
import java.util.logging.Logger;

import cn.cy.base.config.ServerConfig;
import cn.cy.base.constraint.ConnectionDistributor;

/**
 * 监听请求的中心 , nio
 */
public class Listener implements ConnectionDistributor {

    private ServerConfig serverConfig;

    private Selector selector;

    private static SelectorProvider selectorProvider = SelectorProvider.provider();

    private ServerSocketChannel serverSocketChannel;

    private static Logger logger = Logger.getLogger(Listener.class.getName());

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
     * accept函数, 无限循环, 接受连接
     */
    public void accept() {
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
                    // 监听到连接, 根据策略分发连接
                    if (key.isAcceptable()) {
                        logger.info("server can accept");
                        // 分发事件
                        distributeAccept((ServerSocketChannel) key.channel(), selector);

                    } else if (key.isReadable()) {
                        logger.info("server can read");
                        /**
                         * unix网络编程, IO模型中讲到的一下几种"套接字准备好写的状态"
                         * 1. 套接字的 接受缓冲区 的 字节数 > 套接字接收缓冲区低水位标记的当前大小, 返回的字节数 > 0
                         * 2. 读半部关闭, 字节数直接返回0
                         * 3. 套接字错误待处理, 返回 -1
                         */
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        distributeRead(clientChannel);

                    } else if (key.isWritable()) {
                        logger.info("server can write on {}");
                        /**
                         * 准备好写的情况
                         * 1. 发送缓冲区的可用空间字节数 >= 发送缓冲区低水位标记大小
                         * 2. 写半部关闭
                         * 3. 有错误, 返回-1
                         */
                    }
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
    public void distributeAccept(ServerSocketChannel serverSocketChannel, Selector selector) {
        try {
            logger.info("conntected");
            // 这里是非阻塞的accept
            while (true) {
                SocketChannel remoteChannel = serverSocketChannel.accept();
                if (remoteChannel != null) {
                    remoteChannel.configureBlocking(false);
                    remoteChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void distributeRead(SocketChannel channel) {
        try {
            //之前因为是readOnly，所以无法将channel数据写入，报非法参数异常
            int cnt = channel.read(readBuffer);
            // 客户端关闭连接
            if(cnt < 0) {
                channel.keyFor(this.selector).cancel();
                channel.close();
                return;
            }
            //之前会报UnsupportOperation异常
            readBuffer.flip();
            System.out.println(Charset.forName("UTF-8").decode(readBuffer).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        Listener listener = new Listener();
        listener.init();

        listener.accept();
    }
}
