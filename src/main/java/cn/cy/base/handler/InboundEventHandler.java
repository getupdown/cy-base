package cn.cy.base.handler;

import cn.cy.base.core.context.ConnectionContext;

/**
 * 事件处理器
 */
public interface InboundEventHandler<T> {

    /**
     * 接收到读事件的处理
     *
     * @param context
     */
    void onRead(ConnectionContext<T> context);

    /**
     * 由于异常退出事件的处理
     *
     * @param context
     */
    void onCancel(ConnectionContext<T> context);

    /**
     * 触发OutBoundHandler的接口
     */
    void triggerOutBoundHandler(ConnectionContext<T> socketContext, OutboundEventHandler<T> outboundEventHandler);
}
