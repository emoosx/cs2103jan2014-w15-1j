package test;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import logic.Command.COMMAND_TYPE;
import logic.CommandFactory;

import org.junit.Before;
import org.junit.Test;

import common.PandaLogger;

import storage.StorageHelper;
import core.Task;

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
		CommandFactory.INSTANCE.executeCommand(COMMAND_TYPE.ADD, "haha by 16-2-2014");
		assertTrue(true);
	}


}
