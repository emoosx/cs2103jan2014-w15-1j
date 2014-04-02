package test;

import static org.junit.Assert.assertEquals;
import logic.TaskParser;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Test;

public class TaskParserTest {
	
	// By the the time the input reaches the Task creation,
	// the command verb has already been striped
    String[] insaneFloating = {
        "just a floating task",
        "",
        " ",
        "null"
    };
    
    String[] insaneTimed = {
        "attend a meeting next week from 2pm to 4pm",
        "go to the school from 2pm to 4pm",
        "remind boss of meeting next week by the day after tomorrow 5pm"
    };
    
    String[] insaneDeadline = {
        "watch the day after tomorrow the day after tomorrow at 5pm",
        "renew phone number to 91832015 tomorrow morning at 7:54am",
        "buy birthday presents for may and april by 4 march 10am",
        "download eath wind fire by september by 20th march 5pm",
        "2pm concert on 2/2/2014 6pm",
        "2pm concert on 2014/2/22 at 6pm"
    };
    
//	@Test
//	public void testInsaneTimed() {
//		testTimedTask(insaneTimed[0], "attend a meeting", null, null);
//		testTimedTask(insaneTimed[1], "go to the school", null, null);
//	}
	
//	@Test
//	public void testInsaneFloating() {
//		testFloatingTask(insaneFloating[0], "just a floating task");
//	}
	
	@Test
	public void testInsaneDeadline() {
		// "watch the day after tomorrow the day after tomorrow at 5pm",
		MutableDateTime  endDateTime = new MutableDateTime();  
		endDateTime.addDays(2);
		endDateTime.setTime(17, 0, 0, 0);
		testDeadlineTask(insaneDeadline[0], "watch the day after tomorrow", endDateTime);

		endDateTime = new MutableDateTime();
		endDateTime.addDays(1);
		endDateTime.setTime(7, 54, 0, 0);
		testDeadlineTask(insaneDeadline[1], "renew phone number to 91832014", endDateTime);
		
		endDateTime = new MutableDateTime();
		endDateTime.setDate(endDateTime.getYear(), 3, 4);
		endDateTime.setTime(10, 0, 0, 0);
		testDeadlineTask(insaneDeadline[2], "buy birthday presents for may and april", endDateTime);
		
		endDateTime = new MutableDateTime();
		endDateTime.setDate(endDateTime.getYear(), 3, 20);
		endDateTime.setTime(17, 0, 0, 0);
		testDeadlineTask(insaneDeadline[3], "download eath wind fire", endDateTime);
		
		endDateTime = new MutableDateTime();
		endDateTime.setDate(2014, 2, 2);
		endDateTime.setTime(18, 0, 0, 0);
		testDeadlineTask(insaneDeadline[4], "2pm concert", endDateTime);
		
		//"2pm concert on 22/2/2014 at 6pm"
		endDateTime = new MutableDateTime();
		endDateTime.setDate(2014, 2, 22);
		endDateTime.setTime(18, 0, 0, 0);
		testDeadlineTask(insaneDeadline[5], "2pm concert", endDateTime);
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
		assertEquals(null, parser.getMutableStartDateTime());
		assertEquals(null, parser.getMutableEndDateTime());
	}
	
	public void testDeadlineTask(String input, String expectedDesc, MutableDateTime expectedEndDateTime) {
		TaskParser parser = new TaskParser(input);
		parser.parseTask2();
//		assertEquals(expectedDesc, parser.getTaskDescription());
		assertEquals(null, parser.getMutableStartDateTime());
		assertEquals(expectedEndDateTime, parser.getMutableEndDateTime());
	}
}
