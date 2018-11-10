package cn.cy.base.core.context;

import java.nio.Buffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 定义了一个连接的上下文
 */
public interface ConnectionContext<T> {

    /**
     * 获取上下文中定义的数据
     *
     * @return
     */
    T getData();

    /**
     * 设置
     */
    void setData(T data);

    /**
     * 获取key
     */
    SelectionKey getKey();

    /**
     * 获取channel
     */
    SocketChannel getChannel();

    /**
     * 获取selector
     */
    Selector getSelector();

    /**
     * 获取Buffer
     */
    Buffer getBuffer();
}
