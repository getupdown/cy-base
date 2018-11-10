package cn.cy.base.handler;

import cn.cy.base.core.context.ConnectionContext;

public interface OutboundEventHandler<T> {
    /**
     * OutboundEventHandler由Inbound触发
     */
    void trigger(ConnectionContext<T> socketContext);

    /**
     * 写入队列
     */
    void write(T data);

    /**
     * 写入并且直接发送出去
     */
    void writeAndFlush(T data);

    /**
     * 发送暂存区数据
     */
    void flush(T data);
}
