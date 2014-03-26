package test;

import static org.junit.Assert.*;

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

public class UndoStorageTest {
	
	private static final String TEST_FILENAME = "UndoStorageTest.json";
	private static final String ERROR_FILE_CREATION = "Error in creating json file for testing";;
	private static File file;
	private static UndoStorage undoStorage = UndoStorage.INSTANCE;
	
	private Stack<Command> commands;
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
//		file.delete();
	}

	@Before
	public void setUp() throws Exception {
		commands = new Stack<Command>();
		commands.push(new Command("add go to school"));
		commands.push(new Command("add invoker"));
		commands.push(new Command("add spectre"));
		
		commandStack = new Stack<SimpleEntry<Integer, Command>>();
		commandStack.push(new SimpleEntry<Integer, Command>(1, new Command("add go to school")));
		commandStack.push(new SimpleEntry<Integer, Command>(2, new Command("add invoker")));
		commandStack.push(new SimpleEntry<Integer, Command>(3, new Command("add ccc")));
		commandStack.push(new SimpleEntry<Integer, Command>(3, new Command("edit 3")));
	}

	@After
	public void tearDown() throws Exception {
		commandStack = new Stack<SimpleEntry<Integer, Command>>();
	}

	
	@Test
	public void testReadandWrite() {
		undoStorage.writeCommands(commandStack, file);
		Stack<SimpleEntry<Integer, Command>> allCommands = undoStorage.getAllCommands(file);
		assertEquals(allCommands.toString(), commandStack.toString());
	}
	
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
