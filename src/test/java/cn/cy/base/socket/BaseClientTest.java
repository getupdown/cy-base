package cn.cy.base.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import cn.cy.base.socket.func.ConnectFunction;
import cn.cy.base.socket.func.ReadFunction;
import cn.cy.base.socket.func.WriteFunction;

public class BaseClientTest {

    protected static Logger logger = LoggerContext.getContext().getLogger("test");

    public void startSingleClient(WriteFunction writeFunction, ReadFunction readFunction,
                                  ConnectFunction connectFunction) throws IOException, InterruptedException {

        SocketAddress remote = new InetSocketAddress(InetAddress.getLocalHost(), 8100);

        SocketChannel clientChannel = SocketChannel.open(remote);
        clientChannel.configureBlocking(false);
        Selector selector = Selector.open();
        // 关心读写以及连接事件
        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);

        while (true) {
            if (selector.select() == 0) {
                continue;
            }

            for (SelectionKey selectionKey : selector.selectedKeys()) {
                SocketChannel remoteChannel = (SocketChannel) selectionKey.channel();

                if (writeFunction != null && selectionKey.isWritable()) {

                    writeFunction.apply(remoteChannel);

                } else if (readFunction != null && selectionKey.isConnectable()) {

                    readFunction.apply(remoteChannel);

                } else if (connectFunction != null && selectionKey.isReadable()) {

                    connectFunction.apply(remoteChannel);

                }
            }
        }
    }
}
