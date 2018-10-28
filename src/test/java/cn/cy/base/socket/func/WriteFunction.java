package cn.cy.base.socket.func;

import java.nio.channels.SocketChannel;

@FunctionalInterface
public interface WriteFunction {
    void apply(SocketChannel remoteSocketChannel);
}
