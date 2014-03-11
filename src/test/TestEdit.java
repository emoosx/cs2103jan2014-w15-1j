package test;

import static org.junit.Assert.*;
import logic.TaskEdit;

import org.junit.Test;

public class TestEdit {

	TaskEdit task = new TaskEdit();
	@Test
	
		public void testEdit() {
			
	        //testEditCommand("test empty", "Usage: delete <number>", "");
	       // testEditCommand("test number out of arraylist range", "Please choose a lower value", "4");
	       // testEditCommand("test for positive number", "Please key in a positive number", "-2");
	      //  testEditCommand("test for invalid number format", "Please key in an integer", "two");
			testEditCommand("test edit", "meetingchanged on 28-2-2014 from 1pm to 2pm", "1 meetingchanged on 28-2-2014 from 1pm to 2pm");
	
		}

		private void testEditCommand(String description, String expected,
				String input) {
			assertEquals(description, expected, task.testEdit(input));
		}
	}

