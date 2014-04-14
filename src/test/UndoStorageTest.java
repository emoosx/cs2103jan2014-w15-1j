package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Stack;

import logic.Command;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import storage.UndoStorage;

//@author A0105860L
/*
 * testing of stack behavior and reading and writing of command stack in file
 */
public class UndoStorageTest {
	
	private static final String TEST_FILENAME = "UndoStorageTest.json";
	private static final String ERROR_FILE_CREATION = "Error in creating json file for testing";;
	private static File file;
	private static UndoStorage undoStorage = UndoStorage.INSTANCE;
	
	private Stack<SimpleEntry<Integer, Command>> commandStack;
	
	private static File createOrGetFile(String filename) {
		File file = new File(filename);
		if(!file.isFile()) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				throw new Error(ERROR_FILE_CREATION);
			}
		}
		return file;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		file = createOrGetFile(TEST_FILENAME);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		file.delete();
	}

	@Before
	public void setUp() throws Exception {
		commandStack = new Stack<SimpleEntry<Integer, Command>>();
		commandStack.push(new SimpleEntry<Integer, Command>(1, new Command("add submit video #cs2103 by 2359")));
		commandStack.push(new SimpleEntry<Integer, Command>(2, new Command("add ask a girl out")));
		commandStack.push(new SimpleEntry<Integer, Command>(3, new Command("delete 1")));
		commandStack.push(new SimpleEntry<Integer, Command>(4, new Command("edit 1 nice to meet you #dream")));
		commandStack.push(new SimpleEntry<Integer, Command>(5, new Command("done 1")));
	}

	@After
	public void tearDown() throws Exception {
		commandStack = new Stack<SimpleEntry<Integer, Command>>();
	}
	
	// test both read and write methods behavior
	@Test
	public void testReadandWrite() {
		undoStorage.writeCommands(commandStack, file);
		Stack<SimpleEntry<Integer, Command>> allCommands = undoStorage.getAllCommands(file);
		assertEquals(allCommands.toString(), commandStack.toString());
	}
	
	// just want to see the string representation of a stack
	@Test
	public void testStack() {
		Stack<String> testStack = new Stack<String>();
		testStack.push("aaa");
		testStack.push("bbb");
		testStack.push("ccc");
		assertEquals("[aaa, bbb, ccc]", testStack.toString());
	}
	
	@Test
	public void testNullMap() {
		Stack<AbstractMap.Entry<Integer, String>> testStack = new Stack<AbstractMap.Entry<Integer, String>>();
		testStack.push(new AbstractMap.SimpleEntry<Integer, String>(1, "aaa"));
		testStack.push(new AbstractMap.SimpleEntry<Integer, String>(2, "bbb"));
		testStack.push(new AbstractMap.SimpleEntry<Integer, String>(3, "ccc"));
		testStack.push(new AbstractMap.SimpleEntry<Integer, String>(4, "ddd"));
		testStack.push(new AbstractMap.SimpleEntry<Integer, String>(4, "edit woohoo"));
		testStack.push(new AbstractMap.SimpleEntry<Integer, String>(4, "ccc"));
		assertEquals("[1=aaa, 2=bbb, 3=ccc, 4=ddd, 4=edit woohoo, 4=ccc]", testStack.toString());
	}
}
