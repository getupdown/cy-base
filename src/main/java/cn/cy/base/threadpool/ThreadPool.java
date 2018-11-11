package cn.cy.base.threadpool;

import cn.cy.base.core.context.ConnectionContext;
import cn.cy.base.handler.InboundEventHandler;

public interface ThreadPool {

	boolean initialize(int corePoolSize, int maximumPoolSize, long keepAliveTime);

	void submit(Runnable task);

	void executeRead(ConnectionContext context, InboundEventHandler eventHandler);
}
