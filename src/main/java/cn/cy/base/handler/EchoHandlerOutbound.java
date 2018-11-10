package cn.cy.base.handler;

import java.nio.channels.SelectionKey;

import cn.cy.base.core.context.ConnectionContext;

public class EchoHandlerOutbound implements OutboundEventHandler<String> {

    @Override
    public void trigger(ConnectionContext<String> socketContext) {

        // 打开读标志位
        int interestOps = socketContext.getKey().interestOps();
        socketContext.getKey().interestOps(interestOps | SelectionKey.OP_WRITE);

        String res = socketContext.getData();

        write(res);
    }

    @Override
    public void write(String data) {

    }

    @Override
    public void writeAndFlush(String data) {

    }

    @Override
    public void flush(String data) {

    }
}
