package test;

import static org.junit.Assert.assertEquals;
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
		assertEquals("14-2-2014", RegExp.parseDate("add haha by 14-2-2014").get(0));
		assertEquals("14-2-2014", RegExp.parseDate("add haha by 14-2-2014").get(0));
	}
	
	@Test
	public void testGetStringFromDateString() {
		
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
	}
}