package cn.cy.base.constraint;

import java.nio.channels.SelectionKey;

/**
 * accept到请求之后，以什么样的方式分发这些请求
 */
public interface ConnectionDistributor {
    void distributeAccept(SelectionKey selectionKey);

    void distributeRead(SelectionKey selectionKey);
}
