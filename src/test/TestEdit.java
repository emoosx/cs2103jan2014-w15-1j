package test;

import static org.junit.Assert.*;
import logic.CommandFactory;

import org.junit.Test;

public class TestEdit {

	CommandFactory cFact = CommandFactory.INSTANCE;
	@Test
	
		public void testEdit() {
			
	        testEditCommand("test empty", "Usage: edit <index> <description> on <date> from <start time> to <end time>", "");
	        testEditCommand("test number out of arraylist range", "Please choose another value", "4 meetingchanged on 28-2-2014 from 3pm to 4pm");
	        testEditCommand("test for positive number", "Please key in a positive number", "-2 meetingchanged on 28-2-2014 from 3pm to 4pm");
	        testEditCommand("test for invalid number format", "Please key in an integer", "one meetingchanged on 28-2-2014 from 3pm to 4pm");
			testEditCommand("test edit", "meetingchanged1516", "1 meetingchanged on 28-2-2014 from 3pm to 4pm");
	        testUndoEditCommand("test undo edit", "meeting2");
		}

		private void testEditCommand(String description, String expected,
				String input) {
			assertEquals(description, expected, cFact.testEdit(input));
		}
		
		private void testUndoEditCommand(String description, String expected){
			assertEquals(description, expected, cFact.testUndoEdit());
		}
	}

