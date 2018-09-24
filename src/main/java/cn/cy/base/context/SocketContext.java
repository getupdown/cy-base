package cn.cy.base.context;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import cn.cy.base.handler.EventHandler;

/**
 * 维护每一个连接相关的上下文, 一个连接
 */
public class SocketContext {

    private Selector selector;

    private SocketChannel socketChannel;

    private SelectionKey selectionKey;

    // 读写公用
    private Buffer buffer;

    private EventHandler eventHandler;

    private SocketContext() {

    }

    /**
     * 通过这个来构造
     *
     * @param selectionKey
     * @param remoteChannel
     *
     * @return
     */
    public static SocketContext buildSocketContext(SelectionKey selectionKey, EventHandler eventHandler,
                                                   SocketChannel remoteChannel) {
        SocketContext socketContext = new SocketContext();

        socketContext.selector = selectionKey.selector();
        socketContext.socketChannel = remoteChannel;
        socketContext.selectionKey = selectionKey;
        socketContext.buffer = ByteBuffer.allocate(2048);
        socketContext.eventHandler = eventHandler;
        return socketContext;
    }

    /**
     * selector循环中触发读事件
     */
    public void fireReadEvent() {
        // todo 线程池
        eventHandler.onRead(this);
    }

    public Selector getSelector() {
        return selector;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public Buffer getBuffer() {
        return buffer;
    }
}
