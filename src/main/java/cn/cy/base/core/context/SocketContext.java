package cn.cy.base.core.context;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;

import cn.cy.base.handler.InboundEventHandler;

/**
 * 维护每一个连接相关的上下文, 一个连接
 */
public class SocketContext implements ConnectionContext<String> {

    private Selector selector;

    private SocketChannel socketChannel;

    private SelectionKey selectionKey;

    // 读写公用
    private Buffer buffer;

    private InboundEventHandler inboundEventHandler;

    private String data;

    private Queue<Byte> sendQueue = new ArrayDeque<>();

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
    public static SocketContext buildSocketContext(SelectionKey selectionKey, InboundEventHandler inboundEventHandler,
                                                   SocketChannel remoteChannel) {
        SocketContext socketContext = new SocketContext();

        socketContext.selector = selectionKey.selector();
        socketContext.socketChannel = remoteChannel;
        socketContext.selectionKey = selectionKey;
        socketContext.buffer = ByteBuffer.allocate(2048);
        socketContext.inboundEventHandler = inboundEventHandler;
        socketContext.sendQueue = new ArrayDeque();
        return socketContext;
    }

    /**
     * selector循环中触发读事件
     */
    public void fireReadEvent() {
        GlobalContext.getInstance().getThreadPool().executeRead(this, inboundEventHandler);
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Queue<Byte> getSendQueue() {
        return sendQueue;
    }

    @Override
    public SelectionKey getKey() {
        return selectionKey;
    }

    @Override
    public SocketChannel getChannel() {
        return socketChannel;
    }
}
