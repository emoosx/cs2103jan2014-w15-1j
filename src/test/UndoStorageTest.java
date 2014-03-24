package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
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
	}

	@After
	public void tearDown() throws Exception {
		commands = new Stack<Command>();
	}

	
	@Test
	public void testCommandWrite() {
		undoStorage.writeCommands(commands, file);
		assertTrue(true);
	}
	
	@Test
	public void testStack() {
		Stack<String> testStack = new Stack<String>();
		testStack.push("aaa");
		testStack.push("bbb");
		testStack.push("ccc");
		assertEquals("[aaa, bbb, ccc]", testStack.toString());
	}

}
