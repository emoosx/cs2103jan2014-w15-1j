package test;

import static org.junit.Assert.assertTrue;
import logic.Command;
import logic.CommandFactory;

import org.junit.Before;
import org.junit.Test;

import common.PandaLogger;

public class TaskTest {
	
	@Before
	public void setUp() {

	}
	
//	@Test
//	public void testParse() throws NoSuchMethodException, SecurityException, IllegalAccessException {
//		Class<?> params[] = new Class[1];
//		params[0] = String.class;
//		
//		Method m;
//		m = Task.class.getDeclaredMethod("parse", params);
//		m.setAccessible(true);
//		
//	}
	
	@Test
	public void testAddNewTaskJson() {
		PandaLogger.getLogger().info("testAddNewTaskJson");
		Command newAddCommand = new Command("add haha by 16-2-2014");
		CommandFactory.getInstance().executeCommand(newAddCommand);
		assertTrue(true);
	}


}
