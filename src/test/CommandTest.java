package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import logic.Command;

import org.junit.Before;
import org.junit.Test;

public class CommandTest {
	
	private String c0Txt = "";
	private String c1Txt = "add";
	private String c2Txt = "add ";
	private String c3Txt = "list  ";
	
	private Command command0;
	private Command command1;
	private Command command2;
	private Command command3;
	private Command command4;
	
	@Before
	public void setUp() {
		command0 = new Command(c0Txt);
		command1 = new Command(c1Txt);
		command2 = new Command(c2Txt);
		command3 = new Command(c3Txt);
		command4 = new Command("add hackathon from 12:00 to 13:00");
	}
	
	public void testAttemptAutoComplete() {
	}
	
	@Test
	public void testStripCommand() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> params[] = new Class[1];
		params[0] = String.class;

		Method m;
		m = command1.getClass().getDeclaredMethod("stripCommand", params);
		m.setAccessible(true);

		String[] result = (String[])m.invoke(command0, c0Txt);
		String[] expected = {"", null};

		String[] result1 = (String[])m.invoke(command1, c1Txt);
		String[] expected1 = {"add", null};
		
		String[] result2 = (String[])m.invoke(command2, c2Txt);
		String[] expected2 = {"add", null};
		
		String[] result3 = (String[])m.invoke(command3, c3Txt);
		String[] expected3 = {"list", null};

		assertArrayEquals("command 0", expected, result);
		assertArrayEquals("command 1", expected1, result1);
		assertArrayEquals("command 2", expected2, result2);
		assertArrayEquals("command 3", expected3, result3);
	}
	

	public void testCreateCommand() {
		
	}

}
