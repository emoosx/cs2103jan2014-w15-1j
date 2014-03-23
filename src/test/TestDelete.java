package test;

import logic.CommandFactory;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestDelete {

	CommandFactory cFact = CommandFactory.getInstance();

	@Test
	public void testDelete() {
		
        testDeleteCommand("test empty", "Usage: delete <number>", "");
       testDeleteCommand("test number out of arraylist range", "Please choose another value", "4");
        testDeleteCommand("test for positive number", "Please key in a positive number", "-2");
        testDeleteCommand("test for invalid number format", "Please key in an integer", "two");
	 	testDeleteCommand("test delete", "task1notdeletedtask2deletedtask3notdeleted", "2");
	 	testDeleteCommand("test delete", "task1deletedtask2notdeletedtask3notdeleted", "1");
	}

	private void testDeleteCommand(String description, String expected,
			String input) {
		assertEquals(description, expected, cFact.testDelete(input));
	}
}

