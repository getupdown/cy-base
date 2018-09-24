package cn.cy.base.handler;

import cn.cy.base.context.SocketContext;

/**
 * 事件处理器
 */
public interface EventHandler {

    void onRead(SocketContext context);

    void send(SocketContext context);

    void onCancel(SocketContext context);
}
