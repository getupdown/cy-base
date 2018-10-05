package cn.cy.base.context;

import static org.junit.Assert.*;

import org.junit.Test;

public class GlobalContextTest {

	@Test
	public void testGetInstance() {
		assertNotNull(GlobalContext.getInstance());
	}

	@Test
	public void testInitialize() {
		assertTrue(GlobalContext.initialize("globalContext.yml"));
		
	}

}
