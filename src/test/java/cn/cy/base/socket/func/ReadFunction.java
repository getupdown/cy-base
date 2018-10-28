package cn.cy.base.socket.func;

import java.nio.channels.SocketChannel;

@FunctionalInterface
public interface ReadFunction {
    void apply(SocketChannel remoteSocketChannel);
}
