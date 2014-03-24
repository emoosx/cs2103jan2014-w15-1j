package test;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import storage.UndoStorage;

public class UndoStorageTest {
	
	private static final String FILENAME = "UndoStorageTest.json";
	private static final String ERROR_FILE_CREATION = "Error in creating json file for testing";;
	private static File file;
	private UndoStorage undoStorage = UndoStorage.INSTANCE;
	
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
		file = createOrGetFile(FILENAME);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
//		file.delete();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testCommandWrite() {
	}

}
