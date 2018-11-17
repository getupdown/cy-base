package cn.cy.base.threadpool;

import cn.cy.base.core.context.ConnectionContext;
import cn.cy.base.handler.InboundEventHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolImpl implements ThreadPool {

	private ExecutorService pool;

	private static class SingleTon {
		private static final ThreadPoolImpl INSTANCE = new ThreadPoolImpl();
	}

	private ThreadPoolImpl() {
	}

	public static ThreadPoolImpl getInstance() {
		return SingleTon.INSTANCE;
	}

	@Override
	public boolean initialize(int corePoolSize, int maximumPoolSize,
			long keepAliveTime) {
		this.pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
                new SynchronousQueue<>());
		return true;
	}

	@Override
	public void submit(Runnable task) {
		pool.submit(task);
	}

	/**
	 * 由线程池分配线程执行读操作
	 */
	@Override
	public void executeRead(ConnectionContext context, InboundEventHandler eventHandler) {
		pool.submit(() -> eventHandler.onRead(context));
	}
}
