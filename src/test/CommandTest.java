package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import logic.Command;

import org.junit.Before;
import org.junit.Test;

//@author A0105860L
/* test whether a command is created correctly from the user's input
 * desired result is strip out the command verb from the input and create a command object
 * the result of the string goes into attribute of the command as command.rawText
 */
public class CommandTest {
	
	private String c0Txt = "";
	private String c1Txt = "add";			// test whether it works with no space
	private String c2Txt = "add ";			// one space
	private String c3Txt = "list #project, 14 apr";		// multiple space
	private String c4Txt = "add hackathon from 12:00 to 13:00";
	private String c5Txt = "done 1";
	private String c6Txt = "delete 1";
	private String c7Txt = "delete lajalsjfdsa";
	private String c8Txt = "undone 5";
	private String c9Txt = "edit xxxx yyy ggg ";
	
	private Command command0;
	private Command command1;
	private Command command2;
	private Command command3;
	private Command command4;
	private Command command5;
	private Command command6;
	private Command command7;
	private Command command8;
	private Command command9;
	
	@Before
	public void setUp() {
		command0 = new Command(c0Txt);
		command1 = new Command(c1Txt);
		command2 = new Command(c2Txt);
		command3 = new Command(c3Txt);
		command4 = new Command(c4Txt);
		command5 = new Command(c5Txt);
		command6 = new Command(c6Txt);
		command7 = new Command(c7Txt);
		command8 = new Command(c8Txt);
		command9 = new Command(c9Txt);
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
		String[] expected3 = {"list", "#project, 14 apr"};

		String[] result4 = (String[])m.invoke(command4, c4Txt);
		String[] expected4 = {"add", "hackathon from 12:00 to 13:00"};
			
		String[] result5 = (String[])m.invoke(command5, c5Txt);
		String[] expected5 = {"done", "1"};

		String[] result6 = (String[])m.invoke(command6, c6Txt);
		String[] expected6 = {"delete", "1"};

		String[] result7 = (String[])m.invoke(command7, c7Txt);
		String[] expected7 = {"delete", "lajalsjfdsa"};				// still get the invalid string, but won't be executed

		assertArrayEquals("command 0", expected, result);
		assertArrayEquals("command 1", expected1, result1);
		assertArrayEquals("command 2", expected2, result2);
		assertArrayEquals("command 3", expected3, result3);
		assertArrayEquals("command 4", expected4, result4);
		assertArrayEquals("command 5", expected5, result5);
		assertArrayEquals("command 5", expected6, result6);
		assertArrayEquals("command 6", expected7, result7);
	}
}
