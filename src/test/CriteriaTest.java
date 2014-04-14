package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import logic.Criteria;

import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import core.Task;

public class CriteriaTest {
	
	private static List<Task> tasks;
	
	// by the time the input become tasks, the command verb has been stripped out
	private static String[] rawTexts = {
		"cs2101 critical reflection by 11 apr 12pm", 				// already overdue
		"cs2103 demo video by tuesday #project",					
		"cs2103 V0.5 demo on next Wednesday from 3:30pm to 4pm",
		"ask a girl out on Friday",
		"freshmen orientation camp from 10 june 2014 12pm to 15 june 2014 6pm",
		"regular #project",
		"next week stuffs by 22 apr 2014",
		"go to school tomorrow from 2pm to 6pm"						// already overdue
	};

	@Before
	public void setUp() throws Exception {
		tasks = new ArrayList<Task>();
		for(String text: rawTexts) {
			tasks.add(new Task(text));
		}

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		tasks.clear();
	}

	@Test
	public void testGetAllOverdueTaskIDs() {
		ArrayList<Integer> result = Criteria.getAllOverdueTaskIDs(tasks);
		assertEquals("[0, 7]", result.toString());

		// done tasks don't count
		tasks.get(0).setTaskDone();
		result = Criteria.getAllOverdueTaskIDs(tasks);
		assertEquals("[7]", result.toString());
		
		// floating tasks don't count
		tasks.get(7).setTaskStartTime(null);
		tasks.get(7).setTaskEndTime(null);
		result = Criteria.getAllOverdueTaskIDs(tasks);
		assertEquals(new ArrayList<Integer>(), result);

	}
	

	@Test
	public void testGetAllDoneTasks() {
		tasks.get(0).setTaskDone();
		tasks.get(2).setTaskDone();
		ArrayList<Integer> result = Criteria.getAllDoneTasks(tasks);
		assertEquals("[0, 2]", result.toString());
		
		// deleted tasks don't count
		tasks.get(0).setMarkAsDelete();
		result = Criteria.getAllDoneTasks(tasks);
		assertEquals("[2]", result.toString());
		
	}

	@Test
	public void testGetAllUndeletedTasks() {
		ArrayList<Integer> result = Criteria.getAllUndeletedTasks(tasks);
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7]", result.toString());
		
		// deleted tasks don't count
		tasks.get(0).setMarkAsDelete();
		tasks.get(2).setMarkAsDelete();
		tasks.get(5).setMarkAsDelete();
		result = Criteria.getAllUndeletedTasks(tasks);
		assertEquals("[1, 3, 4, 6, 7]", result.toString());
		
		// by default, we don't show done tasks
		tasks.get(6).setTaskDone();
		result = Criteria.getAllUndeletedTasks(tasks);
		assertEquals("[1, 3, 4, 7]", result.toString());
	}

	@Test
	public void testGetAllUndeletedFloatingTasks() {
		ArrayList<Integer> result = Criteria.getAllUndeletedFloatingTasks(tasks);
		assertEquals("[5]", result.toString());
		
		tasks.get(5).setTaskEndTime(new DateTime());
		result = Criteria.getAllUndeletedFloatingTasks(tasks);
		assertEquals("[]", result.toString());
		
		// deleted tasks don't count
		tasks.get(5).setTaskEndTime(null);
		tasks.get(5).setMarkAsDelete();
		result = Criteria.getAllUndeletedFloatingTasks(tasks);
		assertEquals("[]", result.toString());
	}


	@Test
	public void testGetAllUndeletedTimedTasks() {
		ArrayList<Integer> result = Criteria.getAllUndeletedFloatingTasks(tasks);
		assertEquals("[5]", result.toString());
		
		tasks.get(5).setTaskEndTime(new DateTime());
		result = Criteria.getAllUndeletedFloatingTasks(tasks);
		assertEquals("[]", result.toString());
		
		// deleted tasks don't count
		tasks.get(5).setTaskEndTime(null);
		tasks.get(5).setMarkAsDelete();
		result = Criteria.getAllUndeletedFloatingTasks(tasks);
		assertEquals("[]", result.toString());
	}


	@Test
	public void testGetAllUndeletedDeadlineTasks() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllDeadlineTasks() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllTasksforToday() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllTasksforTomorrow() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllTasksforThisWeek() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllTasksforNextWeek() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllUndeletedTasksWithHashTag() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllUndeletedTasksWithTimestamp() {
		fail("Not yet implemented");
	}

}
