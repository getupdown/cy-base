package cn.cy.base.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolImpl implements ThreadPool {
	private static ExecutorService pool;

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
		pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>());
		return true;
	}

	@Override
	public void submit(Runnable task) {
		pool.submit(task);
	}

}
