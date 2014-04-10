package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import common.PandaLogger;

//@author A0101810A
/*
 * TaskParser class will aid the adding of tasks through parsing of user's raw data
 * It will take in a raw user input given by Task.java 
 * and create 2 DateTime objects and 1 task description string using RegExp class
 * The task object then get these data through getter methods available. 
 */

public class TaskParser {

	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	private static final int NUM_YEAR_INDEX = 2;
	private static final int NUM_MONTH_INDEX = 1;
	private static final int NUM_DAY_INDEX = 0;
	private static final int NUM_CALENDAR_MONTH_OFFSET = 1;

	// Task object variables
	private DateTime startDateTime;
	private DateTime endDateTime;
	private String taskDescription;
	private ArrayList<String> hashtags;
	
	// mutable
	private MutableDateTime mutableStartDateTime;
	private MutableDateTime mutableEndDateTime;

	// Primitive time variables
	private Integer startHour;
	private Integer startMin;
	private Integer endHour;
	private Integer endMin;

	// Primitive date variables
	private Integer startYear;
	private Integer startMonth;
	private Integer startDay;
	private Integer endYear;
	private Integer endMonth;
	private Integer endDay;

	// Constructor method for TaskParser
	public TaskParser() {
		hashtags = new ArrayList<String>();
		startHour = null;
		startMin = null;
		endHour = null;
		endMin = null;
		startYear = null;
		startMonth = null;
		startDay = null;
		endYear = null;
		endMonth = null;
		endDay = null;
		
		// mutable date time
		mutableStartDateTime = null;
		mutableEndDateTime = null;
	}

	/*
	 * Method calls on static class RegExp.java to obtain date and time strings from user inputs
	 * It will then attempt to create DateTime JODA objects
	 */
	public void parseTask(String userInput) {
		PandaLogger.getLogger().info("Parsing of user input in TaskParser: " + userInput);
		taskDescription = userInput;
		
		ArrayList<String> timeArray = RegExp.parseTime(taskDescription);
		ArrayList<String> dateArray = RegExp.parseDate(taskDescription);
		hashtags = RegExp.parseHashtag(taskDescription);
		taskDescription = RegExp.parseDescription(taskDescription);
		
		PandaLogger.getLogger().info("TaskParser - Task Description: " + taskDescription);
		PandaLogger.getLogger().info("TaskParser - Time Strings: " + timeArray);
		PandaLogger.getLogger().info("TaskParser - Date Strings: " + dateArray);
		PandaLogger.getLogger().info("TaskParser - Hashtags: " + hashtags);
		
		initializeDateTimeObjects(timeArray, dateArray);
	}
	
	/* 
	 * Method creates 2 DateTime objects given a list of time strings and date strings
	 * Method will first call two methods to initialize primitive date and time variables
	 * It will then finalize the DateTime objects by initializing the DateTime variables
	 */
	private void initializeDateTimeObjects(ArrayList<String> timeArray, ArrayList<String> dateArray) {
		try {
			initializeTime(timeArray);
			initializeDate(dateArray);
			finalizeDateTime();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Given a string array of time input, method will parse and initialize the primitive time variables. 
	 */
	private void initializeTime(ArrayList<String> timeArray) {
		PandaLogger.getLogger().info("TaskParser - Initializing time variables")
		// Case 1: One time string
		if(timeArray.size() == 1) {
			int[] endTimeArray = RegExp.timeFromTimeString(timeArray.get(0));
			initializeEndTime(endTimeArray);
		}
		// Case 2: Two time strings
		else if(timeArray.size() == 2) {
			int[] startTimeArray = RegExp.timeFromTimeString(timeArray.get(0));
			initializeStartTime(startTimeArray);
			int[] endTimeArray = RegExp.timeFromTimeString(timeArray.get(1));
			initializeEndTime(endTimeArray);
		}
	}

	private void initializeStartTime(int[] startTimeArray) {
		startHour = startTimeArray[NUM_HOUR_INDEX];
		startMin = startTimeArray[NUM_MIN_INDEX];
	}

	private void initializeEndTime(int[] endTimeArray) {
		endHour = endTimeArray[NUM_HOUR_INDEX];
		endMin = endTimeArray[NUM_MIN_INDEX];
	}

	/*
	 * Given an array of user date inputs, method will parse and initialize 
	 * the primitive date variables. 
	 */
	private void initializeDate(ArrayList<String> dateArray) {
		// Case 0: user inputs a floating task
		if (dateArray.isEmpty()) {
			return;
		// Case 1: user input having start date and end date
		} else if (dateArray.size() == 2) {
			int[] startDateArray = RegExp.dateFromDateString(dateArray.get(0));
			initializeStartDate(startDateArray);
			int[] endDateArray = RegExp.dateFromDateString(dateArray.get(1));
			initializeEndDate(endDateArray);
		// Case 2: user input having only end deadline
		} else {
			int[] endDateArray = RegExp.dateFromDateString(dateArray.get(0));
			initializeEndDate(endDateArray);
		}
	}

	private void initializeStartDate(int[] startDateArray) {
		startYear = startDateArray[NUM_YEAR_INDEX];
		startMonth = startDateArray[NUM_MONTH_INDEX];
		startDay = startDateArray[NUM_DAY_INDEX];
	}

	private void initializeEndDate(int[] endDateArray) {
		endYear = endDateArray[NUM_YEAR_INDEX];
		endMonth = endDateArray[NUM_MONTH_INDEX];
		endDay = endDateArray[NUM_DAY_INDEX];
	}

	/* 
	 * Method will finalize all DateTime objects accordingly
	 * Main function of this method is to set unspecified date or time to default settings
	 * and to create DateTime objects for Task.java
	 * Default time: 00:00, Default date: today
	 */
	private void finalizeDateTime() {
		// Case 1: Finalized floating task
		if(isFloatingTask()) {
			System.out.println("FINALIZING FLOATING");
			finalizeFloatingTask();
		// Case 2: Finalized as deadline task as long as 1 date or 1 time input is specified
		} else if(isDeadlineTask()) {
			System.out.println("FINALIZING DEADLINE");
			finalizeDeadlineTask();
		// Case 3: Finalized timed task	
		} else {
			System.out.println("FINALIZING TIMED");
			finalizeTimedTask();
		}
		System.out.println("Start date: " + startHour + ":" + startMin + " " + startDay + "/" + startMonth + "/" + startYear);
		System.out.println("End date: " + endHour + ":" + endMin + " " + endDay + "/" + endMonth + "/" + endYear);
	}

	// Method will finalize the 2 DateTime objects to null
	private void finalizeFloatingTask() {
		startDateTime = null;
		endDateTime = null;
	}

	// Method will finalize date and time if variables fits that of a timed task
	private void finalizeTimedTask() {
			// if one date is given, task will be assumed to start and end on
			// the only date given
			if (endYear != null && startYear == null) {
				startYear = endYear;
				startMonth = endMonth;
				startDay = endDay;
			}
			// Initialize date to local date as two time inputs are read and no
			// date inputs are read
			if (endYear == null && startYear == null) {
				initializeDateToToday();
			}
			if(startHour == null) {
				initializeStartTimeToMidnight();
			}
			if(endHour == null) {
				initializeEndTimeToMidnight();
			}
			startDateTime = new DateTime(startYear, startMonth, startDay,
					startHour, startMin);
			endDateTime = new DateTime(endYear, endMonth, endDay, endHour,
					endMin);
	}

	// Method will finalize date and time if variables fits that of a deadline task
	private void finalizeDeadlineTask() {
		if (endYear == null) {
			// Initialize date to local date if only time is stated by user
			initializeDateToToday();
		}
		if(endHour == null) {
			// Initialize time to midnight if only date is stated by user
			initializeTimeToMidnight();
		}
		startDateTime = null;
		endDateTime = new DateTime(endYear, endMonth, endDay, endHour, endMin);
	}

	private void initializeDateToToday() {
		startYear = endYear = Calendar.getInstance().get(Calendar.YEAR);
		startMonth = endMonth = Calendar.getInstance().get(Calendar.MONTH)
				+ NUM_CALENDAR_MONTH_OFFSET;
		startDay = endDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	private void initializeTimeToMidnight() {
		startHour = endHour = 0;
		startMin = endMin = 0; 
	}
	
	private void initializeStartTimeToMidnight() {
		startHour = 0;
		startMin = 0; 
	}
	
	private void initializeEndTimeToMidnight() {
		endHour = 0;
		endMin = 0; 
	}
	
	/*
	 *  Checks if user input matches that of a floating task
	 *  @returns true if all date and time variables are null
	 */
	private Boolean isFloatingTask() {
		if(startHour == null && endHour == null
				&& startYear == null && endYear == null) {
			return true;
		}
		return false;
	}
	
	/*
	 * Checks if user input matches that of a deadline task
	 * @returns true if only one time or date input is specified
	 */
	private Boolean isDeadlineTask() {
		// Only one time input specified by user
		if(startHour == null && endHour != null) {
			// If there are 2 dates, return false as user indicated timed task
			if(endYear!= null && startYear != null) {
				return false;
			}
		}
		// Only one date input specified by user
		if(startYear == null && endYear != null) {
			// If there are 2 times, return false as user indicated timed task
			if(startHour!= null && endHour!=null) {
				return false;
			}
		}
		if(startYear != null && endYear != null) {
			return false;
		}
		if(startHour != null && endHour != null) {
			return false;
		}
		return true;
		
		/*
		if((startHour == null && endHour != null)	// only one time input is specified
				|| (startYear == null && endYear !=null && startHour == null)) { // only one date input is specified
			return true;
		}
		return false;
		*/
	}

	public ArrayList<String> getHashTag() {
		return hashtags;
	}
	
	public String getTaskDescription() {
		return taskDescription;
	}

	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public DateTime getEndDateTime() {
		return endDateTime;
	}
}
