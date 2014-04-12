package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import logic.RegExp;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

//@author A0101810A
/*
 * This class does unit testing on the various methods in static class RegExp.java
 * It mainly tests whether the regular expression patterns are matched and extracted out correctly 
 * Function coverage - 100% 
 * Written by A0101810A - Tan Zheng Jie (Matthew)
 */

public class RegExpTest {
	
	@Test
	public void testParseTimeFloating() {
		// Partition: inputs with no matched time input
		ArrayList<String> list = new ArrayList<String>();
		assertEquals(list, RegExp.parseTime("add haha by tomorrow"));
		assertEquals(list, RegExp.parseTime("add meeting"));
		assertEquals(list, RegExp.parseTime("add deadline by 5/4/2014"));
		assertEquals(list, RegExp.parseTime("add deadline 5pm"));
		assertEquals(list, RegExp.parseTime("add watch 2pm concert"));
		assertEquals(list, RegExp.parseTime("add buy present for april and may"));
	}
	
	@Test
	public void testParseTimeDeadline() {
		// Partition: cases which results in 1 matched time pattern
		assertEquals("2:15pm", RegExp.parseTime("add haha on 30 november by 2:15pm").get(0));
		assertEquals("5:15pm", RegExp.parseTime("add haha on 14/2/2014 by 5:15pm").get(0));
		assertEquals("5pm", RegExp.parseTime("add haha by 14 mar 5pm").get(0));
		assertEquals("12am", RegExp.parseTime("add haha on 30 may 12am").get(0));
		assertEquals("23:59", RegExp.parseTime("add haha by 23:59").get(0));
		assertEquals("01:59", RegExp.parseTime("add haha by 01:59").get(0));
		assertEquals("2359", RegExp.parseTime("add haha by 2359").get(0));
		
		// Boundary case for invalid input
		ArrayList<String> list = new ArrayList<String>();
		assertEquals(list, RegExp.parseTime("add haha by 1:59"));
		assertEquals(list, RegExp.parseTime("add haha by 12:60pm"));
		assertEquals(list, RegExp.parseTime("add haha by 11:60am"));
		assertEquals(list, RegExp.parseTime("add haha at 13:00am"));
	}
	
	@Test
	public void testParseTimeTimed() {
		// Partition: cases which results in 2 matched time patterns
		assertEquals("5pm", RegExp.parseTime("add haha on 14/2/2014 from 5pm to 6pm").get(0));
		assertEquals("6pm", RegExp.parseTime("add haha on 14/2/2014 from 5pm to 6pm").get(1));
		
		assertEquals("5:15pm", RegExp.parseTime("add haha on 14/2/2014 from 5:15pm to 6:45pm").get(0));
		assertEquals("6:45pm", RegExp.parseTime("add haha on 14/2/2014 from 5:15pm to 6:45pm").get(1));
		
		assertEquals("12:15pm", RegExp.parseTime("add haha on 14 feb from 12:15pm to 6:45pm").get(0));
		assertEquals("6pm", RegExp.parseTime("add haha on 14 feb from 12:15pm to 6pm").get(1));
		
		assertEquals("15:15", RegExp.parseTime("add haha on 14/2/2014 from 15:15 to 16:45").get(0));
		assertEquals("16:45", RegExp.parseTime("add haha on 14/2/2014 from 15:15 to 16:45").get(1));
		
		assertEquals("15:15", RegExp.parseTime("add haha from 1/2/2014 15:15 to 1/2/2014 16:45").get(0));
		assertEquals("16:45", RegExp.parseTime("add haha from 1/2/2014 15:15 to 1/2/2014 16:45").get(1));
		
		assertEquals("15:15", RegExp.parseTime("add haha from 1 jan 15:15 to 3 jan 16:45").get(0));
		assertEquals("16:45", RegExp.parseTime("add haha from 1 jan 15:15 to 3 jan 16:45").get(1));
		
		// Boundary case for invalid inputs
		assertEquals(0, RegExp.parseTime("add haha from 11:60am to 12pm").size());
		assertEquals(0, RegExp.parseTime("add haha from 23:59 to 24:00").size());
	}
	
	@Test
	public void testParseDateFloating() {
		// Partition: inputs with no matched date input
		ArrayList<String> list = new ArrayList<String>();
		assertEquals(list, RegExp.parseDate("add something at 5pm"));
		assertEquals(list, RegExp.parseDate("add meeting"));
		assertEquals(list, RegExp.parseDate("add deadline 11/2/2014 5pm"));
		assertEquals(list, RegExp.parseDate("add renew phone number to 91122014"));
		assertEquals(list, RegExp.parseDate("add buy present for april and may"));
		assertEquals(list, RegExp.parseDate("add 11/1/2014"));
	}
	
	@Test
	public void testDateRegexDeadline() {
		// Partitioned format: on DD-/MM-/YYYY
		assertEquals("14-2-14", RegExp.parseDate("add haha on 14-2-14").get(0));
		assertEquals("14-2-2014", RegExp.parseDate("add haha on 14-2-2014").get(0));
		
		assertEquals("14/11/14", RegExp.parseDate("add haha on 14/11/14").get(0));
		assertEquals("14/11/2014", RegExp.parseDate("add haha on 14/11/2014").get(0));
		
		// Partitioned format: by DD-/MM-/YYYY
		assertEquals("14-2-14", RegExp.parseDate("add haha by 14-2-14").get(0));
		assertEquals("14-2-2014", RegExp.parseDate("add haha by 14-2-2014").get(0));
		
		assertEquals("14/2/14", RegExp.parseDate("add haha by 14/2/14").get(0));
		assertEquals("14/2/2014", RegExp.parseDate("add haha by 14/2/2014").get(0));
		
		// Boundary case for invalid inputs
		assertEquals(0, RegExp.parseDate("add haha on 31/2/2014").size());
		assertEquals(0, RegExp.parseDate("add haha on 55/2/2014").size());
		
		assertEquals(0, RegExp.parseDate("add haha on 31/4/2014").size());
		assertEquals(0, RegExp.parseDate("add haha on 60/4/2014").size());
		
		assertEquals(0, RegExp.parseDate("add haha on 31/6/2014").size());
		assertEquals(0, RegExp.parseDate("add haha on 45/6/2014").size());
		
		assertEquals(0, RegExp.parseDate("add haha on 31/9/2014").size());
		assertEquals(0, RegExp.parseDate("add haha on 77/9/2014").size());
		
		assertEquals(0, RegExp.parseDate("add haha on 31/11/2014").size());
		assertEquals(0, RegExp.parseDate("add haha on 35/11/2014").size());
		
		assertEquals(0, RegExp.parseDate("add haha on 20/13/2014").size());
		
		// Partitioned format: DD mmm YYYY
		assertEquals("14 march 2014", RegExp.parseDate("add haha on 14 march 2014").get(0));
		assertEquals("14 mar 2014", RegExp.parseDate("add haha on 14 mar 2014").get(0));
		assertEquals("14 march", RegExp.parseDate("add haha on 14 march").get(0));
		
	}
	
	@Test
	public void testDateRegexTimed() {
		assertEquals("14 mar", RegExp.parseDate("add camp from 14 mar 5pm to 16 mar 6pm").get(0));
		assertEquals("16 mar", RegExp.parseDate("add camp from 14 mar 5pm to 16 mar 6pm").get(1));
		
		assertEquals("14 mar", RegExp.parseDate("add camp from 14 mar to 16 mar").get(0));
		assertEquals("16 mar", RegExp.parseDate("add camp from 14 mar to 16 mar").get(1));
	}
	/*
	@Test
	public void testGetDateFromDateString() {
		int[] date = new int[999];
		
		// Partitioned test cases: DD mmm format
		date = RegExp.dateFromDateString("12 march 2014");
		assertEquals(12,date[0]);
		assertEquals(3,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("12 MAR 2014");
		assertEquals(12,date[0]);
		assertEquals(3,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("31 JaN 2014");
		assertEquals(31,date[0]);
		assertEquals(1,date[1]);
		assertEquals(2014,date[2]);
		
		// Partitioned test cases: DD/MM/YY format
		date = RegExp.dateFromDateString("12-2-2014");
		assertEquals(12,date[0]);
		assertEquals(2,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("12/2/2014");
		assertEquals(12,date[0]);
		assertEquals(2,date[1]);
		assertEquals(2014,date[2]);
		
		DateTime dtOrg = new DateTime();
		DateTime dtPlusOne = dtOrg.plusDays(1);
		date = RegExp.dateFromDateString("by tomorrow 5pm");
		assertEquals(dtPlusOne.getDayOfMonth(),date[0]);
		assertEquals(dtPlusOne.getMonthOfYear(),date[1]);
		assertEquals(dtPlusOne.getYear(),date[2]);
		
		
		date = RegExp.dateFromDateString("on next monday");
		assertEquals(7,date[0]);
		assertEquals(4,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("by next friday");
		assertEquals(11,date[0]);
		assertEquals(4,date[1]);
		assertEquals(2014,date[2]);
		
	}
	
	@Test
	public void testGetTimeFromTimeString() {
		int[] time = new int[999];
		time = RegExp.timeFromTimeString("12:12pm");
		assertEquals(12,time[0]);
		assertEquals(12,time[1]);
		
		time = RegExp.timeFromTimeString("1:12pm");
		assertEquals(13,time[0]);
		assertEquals(12,time[1]);
		
		time = RegExp.timeFromTimeString("1:12am");
		assertEquals(1,time[0]);
		assertEquals(12,time[1]);
		
		time = RegExp.timeFromTimeString("1pm");
		assertEquals(13,time[0]);
	}
	*/
	@Test
	public void testParseDescription() {
		
		// Partitioned test cases: floating tasks
		assertEquals("meeting", RegExp.parseDescription("meeting"));
		assertEquals("wash car", RegExp.parseDescription("wash car"));
		// Edge test cases:
		assertEquals("meeting 14/2/2014", RegExp.parseDescription("meeting 14/2/2014"));
		assertEquals("meeting 14-2-2014", RegExp.parseDescription("meeting 14-2-2014"));
		assertEquals("meeting 14 jan", RegExp.parseDescription("meeting 14 jan"));
		
		// Partitioned test cases: deadline tasks
		assertEquals("add meeting", RegExp.parseDescription("add meeting by 10pm"));
		assertEquals("meeting", RegExp.parseDescription("meeting by 2359"));
		assertEquals("2pm concert", RegExp.parseDescription("2pm concert by 10:45pm"));
		
		// Partitioned test cases: timed tasks
		assertEquals("add meeting", RegExp.parseDescription("add meeting from 5pm to 6pm"));
		assertEquals("add from to from to", RegExp.parseDescription("add from to from to from 5:15pm to 6:15pm"));
		assertEquals("add meeting", RegExp.parseDescription("add meeting on 14-2-2014 from 5pm to 6pm"));
	}
	
	@Test
	public void testParseHashtag() {
		ArrayList<String> hashtags = new ArrayList<String>();
		hashtags.add("#work");
		assertEquals(hashtags, RegExp.parseHashtag("add ##### on 2/2/2014 from 2pm to 3pm #work"));
		
		hashtags = new ArrayList<String>();
		assertEquals(hashtags, RegExp.parseHashtag("add ##### on 2/2/2014 from 2pm to 3pm"));
	}
	
	@Test
	public void testChangeDateFormat() {
		assertEquals("2/22/2014", RegExp.changeDateFormat("22/2/2014"));
		assertEquals("2/10/2014", RegExp.changeDateFormat("10/2/2014"));
		assertEquals("10/10/2014", RegExp.changeDateFormat("10/10/2014"));
		assertEquals("meeting on 2/22/2014", RegExp.changeDateFormat("meeting on 22/2/2014"));
		assertEquals("from 1/11/2014 to 1/12/2014", RegExp.changeDateFormat("from 11/1/2014 to 12/1/2014"));
		assertEquals("3/2/2014", RegExp.changeDateFormat("2/3/2014"));
	}
	
	@Test
	public void testMonthIndex() {
		assertEquals(1, RegExp.getMonthIndex("jan"));
		assertEquals(1, RegExp.getMonthIndex("JAN"));
		assertEquals(1, RegExp.getMonthIndex("january"));
		assertEquals(1, RegExp.getMonthIndex("JANUARY"));
		assertEquals(3, RegExp.getMonthIndex("march"));
	}
}