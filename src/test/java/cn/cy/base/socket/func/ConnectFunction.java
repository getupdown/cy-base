package cn.cy.base.socket.func;

import java.nio.channels.SocketChannel;

@FunctionalInterface
public interface ConnectFunction {
    void apply(SocketChannel socketChannel);
}
