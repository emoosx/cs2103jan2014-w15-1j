package test;

import static org.junit.Assert.assertEquals;
import logic.TaskParser;

import org.joda.time.DateTime;
import org.junit.Test;

public class TaskParserTest {
	
	// By the the time the input reaches the Task creation,
	// the command verb has already been striped
    String[] insaneFloating = {
        "just a floating task",
    };
    
    String[] insaneTimed = {
        "attend a meeting next week from 2pm to 4pm",
        "go to the school from 2pm to 4pm"
    };
    
    String[] insaneDeadline = {
        "watch the day after tomorrow the day after tomorrow at 5pm",
        "renew phone number to 91832014 tomorrow morning at 7:54am",
        "buy birthday presents for may and april by 4 march 10am",
        "download eath wind fire by september by 20th march 5pm",
        "2pm concert on 2/2/2014 6pm"
    };
    
	@Test
	public void testInsaneTimed() {
		testTimedTask(insaneTimed[0], "attend a meeting", null, null);
		testTimedTask(insaneTimed[1], "go to the school", null, null);
	}
	
	@Test
	public void testInsaneFloating() {
		testFloatingTask(insaneFloating[0], "just a floating task");
	}
	
	@Test
	public void testInsaneDeadline() {
		testDeadlineTask(insaneDeadline[0], "watch the day after tomorrow", null);
		testDeadlineTask(insaneDeadline[1], "renew phone number to 91832014", null);
		testDeadlineTask(insaneDeadline[2], "buy birthday presents for may and april", null);
		testDeadlineTask(insaneDeadline[3], "download eath wind fire", null);
		testDeadlineTask(insaneDeadline[4], "2pm concert", null);

	}

	public void testTimedTask(String input, String expectedDesc, DateTime expectedStartDateTime, DateTime expectedEndDateTime) {
		TaskParser parser = new TaskParser(input);
		parser.parseTask2();
		assertEquals(expectedDesc, parser.getTaskDescription());
		assertEquals(expectedStartDateTime, parser.getStartDateTime());
		assertEquals(expectedEndDateTime, parser.getEndDateTime());
	}
	
	public void testFloatingTask(String input, String expectedDesc) {
		TaskParser parser = new TaskParser(input);
		parser.parseTask2();
		assertEquals(expectedDesc, parser.getTaskDescription());
		assertEquals(null, parser.getStartDateTime());
		assertEquals(null, parser.getEndDateTime());
	}
	
	public void testDeadlineTask(String input, String expectedDesc, DateTime expectedEndDateTime) {
		TaskParser parser = new TaskParser(input);
		parser.parseTask2();
		assertEquals(expectedDesc, parser.getTaskDescription());
		assertEquals(null, parser.getStartDateTime());
		assertEquals(expectedEndDateTime, parser.getEndDateTime());
	}
}
