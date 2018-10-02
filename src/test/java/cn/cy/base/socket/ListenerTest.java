package cn.cy.base.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Before;
import org.junit.Test;

public class ListenerTest {

    private Logger logger = LoggerContext.getContext().getLogger("test");

    public void startClient() throws IOException, InterruptedException {

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
                    String input = "motherFucker";

                    SocketChannel remoteChannel = (SocketChannel) selectionKey.channel();
                    try {
                        remoteChannel.write(ByteBuffer.wrap(input.getBytes()));
                        logger.info("client has been written, local port is : {}", remoteChannel.socket().getLocalPort());
                    } catch (Exception e) {
                        logger.warn("port {} send failed! ", remoteChannel.socket().getLocalPort());
                    }
                } else if (selectionKey.isConnectable()) {
                    logger.info("asdf");
                } else if (selectionKey.isReadable()) {
                    logger.info("client can read");
                }
            }
        }
    }

    @Test
    public void work() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        for (int i = 0;i < 1; i ++) {
            new Thread(() -> {
                try {
                    startClient();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDownLatch.await();
    }

}