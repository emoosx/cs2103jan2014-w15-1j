package test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import logic.CommandFactory;
import logic.TaskLister;

import org.junit.Test;

import core.Task;

public class TaskListerTest {
	
	private List<Task> tasks = CommandFactory.INSTANCE.getTasks();

	@Test
	public void testGetAllUndeletedTasks() {
//		List<Task> filtered = TaskLister.getAllUndeletedTasks(tasks);
//		System.out.println(filtered.size());
//		System.out.println(tasks.size());
		fail("to be implemented");
	}
}