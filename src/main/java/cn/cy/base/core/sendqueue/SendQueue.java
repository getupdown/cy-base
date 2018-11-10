package cn.cy.base.core.sendqueue;

import java.util.Queue;

public interface SendQueue<T> extends Queue<T> {
    /**
     * 队列最大容量, 单位是字节
     *
     * @return
     */
    int maxSize();
}
