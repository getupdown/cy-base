package cn.cy.base.handler;

import cn.cy.base.context.SocketContext;

/**
 * 事件处理器
 */
public interface InboundEventHandler {

    /**
     * 接收到读事件的处理
     *
     * @param context
     */
    void onRead(SocketContext context);

    /**
     * 由于异常退出事件的处理
     *
     * @param context
     */
    void onCancel(SocketContext context);
}
