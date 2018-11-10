package cn.cy.base.core.context;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
