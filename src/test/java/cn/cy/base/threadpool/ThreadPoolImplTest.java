package cn.cy.base.threadpool;

import static org.junit.Assert.*;

import org.junit.Test;

public class ThreadPoolImplTest {

	@Test
	public void testGetInstance() {
		assertNotNull(ThreadPoolImpl.getInstance());
	}

	@Test
	public void testInitialize() {
		assertTrue(ThreadPoolImpl.getInstance().initialize(0, 100, 60));
	}

	@Test
	public void testSubmit() {
		ThreadPool testPool = ThreadPoolImpl.getInstance();
		assertNotNull(testPool);
		assertTrue(testPool.initialize(0, 100, 60));
		testPool.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println(1);
			}
		});
	}

}
