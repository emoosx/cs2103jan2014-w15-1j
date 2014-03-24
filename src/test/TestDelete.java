package test;

import logic.CommandFactory;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestDelete {

	CommandFactory cFact = CommandFactory.INSTANCE;

	@Test
	public void testDelete() {
		
        testDeleteCommand("test empty", "Usage: delete <number>", "");
       testDeleteCommand("test number out of arraylist range", "Please choose another value", "4");
        testDeleteCommand("test for positive number", "Please key in a positive number", "-2");
        testDeleteCommand("test for invalid number format", "Please key in an integer", "two");
	 	testDeleteCommand("test delete", "meeting1meeting3", "2");
	}

	private void testDeleteCommand(String description, String expected,
			String input) {
		assertEquals(description, expected, cFact.testDelete(input));
	}
}

