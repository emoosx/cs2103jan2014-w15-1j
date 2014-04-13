package test;

import static org.junit.Assert.*;
import logic.TaskParser;

import org.joda.time.DateTime;
import org.junit.Test;

//@author A0101810A
/*
 * NattyParserTest is a unit test to ensure that the strings extracted from RegExp.java are parsed correctly in NattyTime
 * Written by A0101810A - Tan Zheng Jie (Matthew)
 */

public class NattyParserTest {

	@Test
	public void testDateFromDateString() {
		int[] date = new int[3];
		// Partitioned test cases: DD mmm format
		date = TaskParser.dateFromDateString("on 12 march 2014");
		assertEquals(12,date[0]);
		assertEquals(3,date[1]);
		assertEquals(2014,date[2]);
		
		date = TaskParser.dateFromDateString("on 12 MAR 2014");
		assertEquals(12,date[0]);
		assertEquals(3,date[1]);
		assertEquals(2014,date[2]);
		
		date = TaskParser.dateFromDateString("on 31 JaN 2014");
		assertEquals(31,date[0]);
		assertEquals(1,date[1]);
		assertEquals(2014,date[2]);
		
		// Partitioned test cases: DD/MM/YY format
		date = TaskParser.dateFromDateString("12-2-2014");
		assertEquals(12,date[0]);
		assertEquals(2,date[1]);
		assertEquals(2014,date[2]);
		
		date = TaskParser.dateFromDateString("12/2/2014");
		assertEquals(12,date[0]);
		assertEquals(2,date[1]);
		assertEquals(2014,date[2]);
		
		// Partitioned test cases: relative date format
		DateTime datetime = new DateTime();
		datetime = datetime.plusDays(1);
		date = TaskParser.dateFromDateString("tomorrow 5pm");
		assertEquals(datetime.getDayOfMonth(),date[0]);
		assertEquals(datetime.getMonthOfYear(),date[1]);
		assertEquals(datetime.getYear(),date[2]);
		
		datetime = new DateTime();
		datetime = datetime.plusDays(2);
		date = TaskParser.dateFromDateString("the day after tomorrow 5pm");
		assertEquals(datetime.getDayOfMonth(),date[0]);
		assertEquals(datetime.getMonthOfYear(),date[1]);
		assertEquals(datetime.getYear(),date[2]);
	}

	@Test
	public void testGetTimeFromTimeString() {
		int[] time = new int[2];
		
		time = TaskParser.timeFromTimeString("12:12pm");
		assertEquals(12,time[0]);
		assertEquals(12,time[1]);
		
		time = TaskParser.timeFromTimeString("1:12pm");
		assertEquals(13,time[0]);
		assertEquals(12,time[1]);
		
		time = TaskParser.timeFromTimeString("1:12am");
		assertEquals(1,time[0]);
		assertEquals(12,time[1]);
		
		time = TaskParser.timeFromTimeString("1pm");
		assertEquals(13,time[0]);
		assertEquals(0, time[1]);
		
		time = TaskParser.timeFromTimeString("1pm");
		assertEquals(13,time[0]);
		assertEquals(0, time[1]);
		
		time = TaskParser.timeFromTimeString("1pm");
		assertEquals(13,time[0]);
		assertEquals(0, time[1]);
		
		time = TaskParser.timeFromTimeString("2359");
		assertEquals(23,time[0]);
		assertEquals(59, time[1]);
		
		time = TaskParser.timeFromTimeString("23:59");
		assertEquals(23,time[0]);
		assertEquals(59, time[1]);
		
	}
}
