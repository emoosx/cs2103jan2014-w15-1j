package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import logic.Criteria;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.Task;

public class CriteriaTest {
	
	ArrayList<Task> tasks;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
	public void test() {
		tasks = new ArrayList<Task>();
		tasks.add(new Task("go do something today"));
		tasks.add(new Task("go to school from 2pm to 4pm"));
		tasks.add(new Task("random floating task"));
		tasks.add(new Task("go to school tomorrow from 3pm to 6pm"));
		
		ArrayList<Integer> filtered = Criteria.getAllTasksforToday(tasks);
		for(Integer i: filtered) {
			System.out.print(i + "\t");
		}
		assertEquals(filtered, "xx");
	}
	
	@Test
	public void testDummy() {
		Task task = new Task("go to school from 2pm to 4pm");
		DateTime endDateTime = task.getTaskEndTime();
		DateTime today = new DateTime();
		assertEquals(today.withTimeAtStartOfDay(), endDateTime.withTimeAtStartOfDay());
	}

}
