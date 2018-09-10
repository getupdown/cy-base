package cn.cy.base.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
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

    /**
     * 初始化selector
     */
    public void init() throws UnknownHostException {
        try {
            serverSocketChannel = selectorProvider.openServerSocketChannel();
            // 这一步帮忙把bind和listen都做了
            serverSocketChannel = serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8100));

            selector = selectorProvider.openSelector();

            // 对于serverSocketChannel, 感兴趣的是accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
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

                        distributeAccept((ServerSocketChannel) key.channel());

                    } else if (key.isReadable()) {
                        /**
                         * unix网络编程, IO模型中讲到的一下几种"套接字准备好写的状态"
                         * 1. 套接字的 接受缓冲区 的 字节数 > 套接字接收缓冲区低水位标记的当前大小, 返回的字节数 > 0
                         * 2. 读半部关闭, 字节数直接返回0
                         * 3. 套接字错误待处理, 返回 -1
                         */
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        distributeRead(clientChannel);

                    } else if (key.isWritable()) {
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

        } catch (ClosedChannelException cce) {

        } catch (Exception e) {

        }
    }

    @Override
    public void distributeAccept(ServerSocketChannel serverSocketChannel) {

    }

    @Override
    public void distributeRead(SocketChannel channel) {

    }
}
