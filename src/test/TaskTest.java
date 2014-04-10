package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import logic.Command;
import logic.CommandFactory;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;

import common.PandaLogger;
import core.Task;

//@author A0101810A
/*
 * This class does an integration test on several different classes. 
 * They are namely: Task.java, TaskParser.java and RegExp.java
 * The test class spans across the logic (RegExp, TaskParser) and core components (Task). 
 * This test class will create Task objects out of user inputs and compare the DateTime objects of Task objects
 * Test cases are run through a single TestTask method as it is an integration test rather than a unit test
 * Written by A0101810A - Tan Zheng Jie (Matthew)
 */

public class TaskTest {
	
	/****************************** Partitioned floating test cases *******************************/
	String[] floatTasks = {		
		"climb mount everest",
		"ride an elephant and dinosaur",
		"watch 2pm concert",
		"go to garden by the bay"
	};
	
	
	/****************************** Partitioned deadline test cases *******************************/
	String[] deadlineTasks = {
		"reflection deadline on 14/2/2014 at 2359",
		"reflection deadline on 14/2/2014 at 11:59pm",
		"renew passport by 30/5/2014 3pm",
		"meeting on 8 march at 11am"
	};
	
	DateTime[] expectedDeadlineDateTime = {
		new DateTime(2014, 2, 14, 23, 59),
		new DateTime(2014, 2, 14, 23, 59),
		new DateTime(2014, 5, 30, 15, 0),
		new DateTime(2014, 3, 8, 11, 0)
	};
	
	String[] expectedDeadlineDescription = {
		"reflection deadline",
		"reflection deadline",
		"renew passport",
		"meeting"
	};
	
		
	/****************************** Partitioned timed test cases *******************************/
	String[] timedTasks = {
		"valentines date on 14/2/2014 from 5pm to 7pm",
		"meeting on 15 march 2014 from 1000 to 1200",
		"camp from 15 march 2014 12pm to 17 march 2014 7pm"
	};
	
	String[] expectedTimedDescription = {
		"valentines date",
		"meeting",
		"camp"
	};
	
	DateTime[] expectedStartDateTime = {
		new DateTime(2014, 2, 14, 17, 0),
		new DateTime(2014, 3, 15, 10, 0),
		new DateTime(2014, 3, 15, 12, 0)
	};
	
	DateTime[] expectedEndDateTime = {
		new DateTime(2014, 2, 14, 19, 0),
		new DateTime(2014, 3, 15, 12, 0),
		new DateTime(2014, 3, 17, 19, 0)
	};
	
	@Test
	public void testFloatingTask() {
		for(int i=0; i<floatTasks.length; i++) {
			testTask(floatTasks[i], floatTasks[i], null, null);
		}
	}
	
	@Test
	public void testDeadlineTask() {
		for(int i=0; i<deadlineTasks.length; i++) {
			testTask(deadlineTasks[i], expectedDeadlineDescription[i], null, expectedDeadlineDateTime[i]);
		}
	}
	
	@Test
	public void testTimedTask() {
		for(int i=0; i<timedTasks.length; i++) {
			testTask(timedTasks[i], expectedTimedDescription[i], expectedStartDateTime[i], expectedEndDateTime[i]);
		}
	}
	
	/*
	 * Method that takes in userInput and creates a task object out of it
	 * It will then compare the object's description, start time and end time 
	 * with expected description, start time and end time parameters
	 */
	public void testTask(String userInput, String expectedDescription, DateTime expectedStart, DateTime expectedEnd) {
		// Asserting task description
		Task task = new Task(userInput);
		assertEquals(task.getTaskDescription(), expectedDescription);
		
		// Asserting start date time
		if(expectedStart==null) {
			assertEquals(task.getTaskStartTime(), expectedStart);
		} else {
			assertEquals(task.getTaskStartTime().getYear(), expectedStart.getYear());
			assertEquals(task.getTaskStartTime().getMonthOfYear(), expectedStart.getMonthOfYear());
			assertEquals(task.getTaskStartTime().getDayOfMonth(), expectedStart.getDayOfMonth());
			assertEquals(task.getTaskStartTime().getHourOfDay(), expectedStart.getHourOfDay());
			assertEquals(task.getTaskStartTime().getMinuteOfHour(), expectedStart.getMinuteOfHour());
		}
		
		// Asserting end date time
		if(expectedEnd==null) {
			assertEquals(task.getTaskEndTime(), expectedEnd);
		} else {
			assertEquals(task.getTaskEndTime().getYear(), expectedEnd.getYear());
			assertEquals(task.getTaskEndTime().getMonthOfYear(), expectedEnd.getMonthOfYear());
			assertEquals(task.getTaskEndTime().getDayOfMonth(), expectedEnd.getDayOfMonth());
			assertEquals(task.getTaskEndTime().getHourOfDay(), expectedEnd.getHourOfDay());
			assertEquals(task.getTaskEndTime().getMinuteOfHour(), expectedEnd.getMinuteOfHour());
		}
	}
}
