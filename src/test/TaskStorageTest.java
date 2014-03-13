package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import storage.StorageHelper;

public class TaskStorageTest {
	
	private StorageHelper storage;

	@Before
	public void setUp() {
		this.storage = StorageHelper.INSTANCE;

	}

	@Test
	public void testGetAllTasks() {
		assertEquals(this.storage.getAllTasks().size(), 2);

	}

}
