package cn.cy.base.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

public class ListenerTest {

    private static Logger logger = Logger.getLogger(Listener.class.getName());

    @Before
    public void startClient() throws IOException {

        SocketAddress remote = new InetSocketAddress(InetAddress.getLocalHost(), 8100);

        SocketChannel clientChannel = SocketChannel.open(remote);
        clientChannel.configureBlocking(false);
        Selector selector = Selector.open();
        // 关心读写事件
        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        while (true) {
            if (selector.select() == 0) {
                continue;
            }

            // 做一个简单的echo服务
            for (SelectionKey selectionKey : selector.selectedKeys()) {

                if (selectionKey.isWritable()) {
                    logger.info("client can write");
                    String input = "motherFucker";

                    SocketChannel remoteChannel = (SocketChannel) selectionKey.channel();

                    remoteChannel.write(ByteBuffer.wrap(input.getBytes()));

                } else if (selectionKey.isConnectable()) {
                    logger.info("asdf");
                } else if (selectionKey.isReadable()) {
                    logger.info("client can read");
                }
            }
        }
    }

    @Test
    public void work() {

    }

}