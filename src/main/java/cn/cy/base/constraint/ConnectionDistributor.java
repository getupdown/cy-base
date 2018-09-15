package cn.cy.base.constraint;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * accept到请求之后，以什么样的方式分发这些请求
 */
public interface ConnectionDistributor {
    void distributeAccept(ServerSocketChannel serverSocketChannel, Selector selector);

    void distributeRead(SocketChannel channel);
}
