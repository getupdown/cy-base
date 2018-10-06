package cn.cy.base.threadpool;

public interface ThreadPool {

	public boolean initialize(int corePoolSize, int maximumPoolSize, long keepAliveTime);
	
	public void submit(Runnable task);
}
