package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import logic.RegExp;

import org.junit.Test;

public class RegExpTest {
	
	@Test
	public void testTimeRegexDeadline() {
		assertEquals("2:15pm", RegExp.parseTime("add haha by 2:15pm 14/2/2014").get(0));
		assertEquals("5:15pm", RegExp.parseTime("add haha by 5:15pm 14/2/2014").get(0));
		assertEquals("5pm", RegExp.parseTime("add haha by 5pm 14/2/2014").get(0));
		assertEquals("12am", RegExp.parseTime("add haha by 12am 14/2/2014").get(0));
		assertEquals("23:59", RegExp.parseTime("add haha by 23:59 14/2/2014").get(0));
		assertEquals("01:59", RegExp.parseTime("add haha by 01:59 14/2/2014").get(0));
		assertEquals("2359", RegExp.parseTime("add haha by 2359 on 14-2-2014").get(0));
		
		ArrayList<String> list = new ArrayList<String>();
		assertEquals(list, RegExp.parseTime("add haha by 1:59 on 14-2-2014"));
	}
	
	@Test
	public void testTimeRegexTimed() {
		assertEquals("5pm", RegExp.parseTime("add haha on 14/2/2014 from 5pm to 6pm").get(0));
		assertEquals("6pm", RegExp.parseTime("add haha on 14/2/2014 from 5pm to 6pm").get(1));
		
		assertEquals("5:15pm", RegExp.parseTime("add haha on 14/2/2014 from 5:15pm to 6:45pm").get(0));
		assertEquals("6:45pm", RegExp.parseTime("add haha on 14/2/2014 from 5:15pm to 6:45pm").get(1));
		
		assertEquals("15:15", RegExp.parseTime("add haha on 14/2/2014 from 15:15 to 16:45").get(0));
		assertEquals("16:45", RegExp.parseTime("add haha on 14/2/2014 from 15:15 to 16:45").get(1));
	}
	
	@Test
	public void testDateRegexDeadline() {
		assertEquals("14-2-2014", RegExp.parseDate("add haha on 14-2-2014").get(0));
		assertEquals("14-2-2014", RegExp.parseDate("add haha on 14-2-2014").get(0));
		assertEquals("14 march 2014", RegExp.parseDate("add haha on 14 march 2014").get(0));
		assertEquals("14 mar 2014", RegExp.parseDate("add haha on 14 mar 2014").get(0));
		assertEquals("14 march 14", RegExp.parseDate("add haha on 14 march 14").get(0));
		assertEquals("14 march", RegExp.parseDate("add haha on 14 march").get(0));
	}
	
	@Test
	public void testGetStringFromDateString() {
		int[] date = new int[999];
		date = RegExp.dateFromDateString("12 march 2014");
		assertEquals(12,date[0]);
		assertEquals(3,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("12 MAR 14");
		assertEquals(12,date[0]);
		assertEquals(3,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("31 JaN 14");
		assertEquals(31,date[0]);
		assertEquals(1,date[1]);
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
		
		time = RegExp.timeFromTimeString("haha");
		assertEquals(null, time);
		
//		time = RegExp.timeFromTimeString("45:45pm");
//		assertEquals(null, time);
//
//		time = RegExp.timeFromTimeString("06:99am");
//		assertEquals(null, time);
	}
	
	@Test
	public void testGetDateFromDateString() {
		int[] date = new int[999];
		date = RegExp.dateFromDateString("12-2-2014");
		assertEquals(12,date[0]);
		assertEquals(2,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("12/2/2014");
		assertEquals(12,date[0]);
		assertEquals(2,date[1]);
		assertEquals(2014,date[2]);
		
		date = RegExp.dateFromDateString("a/2/2014");
		assertEquals(null, date);
		
		date = RegExp.dateFromDateString("31/14/9999");
		assertEquals(null, date);
	}
	
	@Test
	public void testParseDescription() {
		assertEquals("add meeting", RegExp.parseDescription("add meeting from 5pm to 6pm"));
		assertEquals("add from to from to", RegExp.parseDescription("add from to from to from 5:15pm to 6:15pm"));
		assertEquals("add meeting", RegExp.parseDescription("add meeting on 14-2-2014 from 5pm to 6pm"));
	}
	
	@Test
	public void testParseHashtag() {
		ArrayList<String> hashtags = new ArrayList<String>();
		hashtags.add("#work");
		assertEquals(hashtags, RegExp.parseHashtag("add ##### on 2/2/2014 from 2pm to 3pm #work"));
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
		assertEquals(3, RegExp.getMonthIndex("march"));
	}
}