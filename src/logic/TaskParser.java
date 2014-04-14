package logic;

import java.util.ArrayList;
import java.util.Calendar;
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

	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	
	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	private static final int NUM_YEAR_INDEX = 2;
	private static final int NUM_MONTH_INDEX = 1;
	private static final int NUM_DAY_INDEX = 0;
	private static final int NUM_CALENDAR_MONTH_OFFSET = 1;
	private static final int NUM_TIME_MIDNIGHT = 0;
	private static final int NUM_FLOATING_DATE_SIZE = 0;
	private static final int NUM_FLOATING_TIME_SIZE = 0;
	private static final int NUM_TIMED_DATE_SIZE = 2;
	private static final int NUM_TIMED_TIME_SIZE = 2;
	
	private static final int ARRAY_FIRST_INDEX = 0;
	private static final int ARRAY_SECOND_INDEX = 1;
	private static final int ARRAY_SIZE_1 = 1;
	private static final int ARRAY_SIZE_2 = 2;

	// Task object variables
	private DateTime startDateTime;
	private DateTime endDateTime;
	private String taskDescription;
	private ArrayList<String> hashtags;

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

	// Task booleans
	private Boolean isFloatingTask;
	private Boolean isDeadlineTask;
	private Boolean isTimedTask;
	
	/*
	 *  Constructor method for TaskParser
	 */
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
		isFloatingTask = false;
		isDeadlineTask = false;
		isTimedTask = false;
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
			initializeTaskType(timeArray.size(), dateArray.size());
			finalizeDateTime();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Given a string array of time input, method will parse and initialize the primitive time variables. 
	 */
	private void initializeTime(ArrayList<String> timeArray) {
		PandaLogger.getLogger().info("TaskParser - Initializing time variables");
		// Case 1: User input contains one time string
		if(timeArray.size() == ARRAY_SIZE_1) {
			int[] endTimeArray = timeFromTimeString(timeArray.get(ARRAY_FIRST_INDEX));
			initializeEndTime(endTimeArray);
		}
		// Case 2: User input contains two time strings
		else if(timeArray.size() == ARRAY_SIZE_2) {
			int[] startTimeArray = timeFromTimeString(timeArray.get(ARRAY_FIRST_INDEX));
			initializeStartTime(startTimeArray);
			int[] endTimeArray = timeFromTimeString(timeArray.get(ARRAY_SECOND_INDEX));
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
		} else if (dateArray.size() == ARRAY_SIZE_2) {
			int[] startDateArray = dateFromDateString(dateArray.get(ARRAY_FIRST_INDEX));
			initializeStartDate(startDateArray);
			int[] endDateArray = dateFromDateString(dateArray.get(ARRAY_SECOND_INDEX));
			initializeEndDate(endDateArray);
		// Case 2: user input having only end deadline
		} else {
			int[] endDateArray = dateFromDateString(dateArray.get(ARRAY_FIRST_INDEX));
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
	 * Method will deduce what kind of task was specified by user
	 * The boolean variables will be used to set default date/time settings later on
	 */
	private void initializeTaskType(int timeSize, int dateSize) {
		if(timeSize == NUM_FLOATING_TIME_SIZE && dateSize == NUM_FLOATING_DATE_SIZE) {
			isFloatingTask = true;
		} else if(timeSize == NUM_TIMED_TIME_SIZE || dateSize == NUM_TIMED_DATE_SIZE) {
			isTimedTask = true;
		} else {
			isDeadlineTask = true;
		}
	}
	
	/* 
	 * Method will finalize all DateTime objects accordingly
	 * Main function of this method is to set unspecified date or time to default settings
	 * and to create DateTime objects for Task.java
	 * Default time: 00:00, Default date: today
	 */
	private void finalizeDateTime() {
		if(isFloatingTask) {
			finalizeFloatingTask();
		} else if(isTimedTask) {
			finalizeTimedTask();
		} else if(isDeadlineTask) {
			finalizeDeadlineTask();
		} else {
			assert(false);
		}
	}

	/* 
	 * Method creates 2 dateTime objects which are null based on logical interpretations earlier
	 */
	private void finalizeFloatingTask() {
		startDateTime = null;
		endDateTime = null;
	}

	/*
	 *  Method will finalize date and time if variables fits that of a timed task
	 */
	private void finalizeTimedTask() {
		if(startYear == null) {
			// Initialize both dates to today if no dates are specified
			if(endYear == null) {
				initializeDateToToday();
			}
			// Initialize both dates to the same day if only 1 date is specified
			else {
				startYear = endYear;
				startMonth = endMonth;
				startDay = endDay;
			}
		}
		if(startHour == null) {
			initializeStartTimeToMidnight();
		}
		if(endHour == null) {
			initializeEndTimeToMidnight();
		}
		startDateTime = new DateTime(startYear, startMonth, startDay, startHour, startMin);
		endDateTime = new DateTime(endYear, endMonth, endDay, endHour, endMin);
	}

	/*
	 *  Method will finalize date and time if variables fits that of a deadline task
	 */
	private void finalizeDeadlineTask() {
		if(endYear == null) {
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
		startMonth = endMonth = Calendar.getInstance().get(Calendar.MONTH) + NUM_CALENDAR_MONTH_OFFSET;
		startDay = endDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	private void initializeTimeToMidnight() {
		initializeStartTimeToMidnight();
		initializeEndTimeToMidnight();
	}
	
	private void initializeStartTimeToMidnight() {
		startHour = NUM_TIME_MIDNIGHT;
		startMin = NUM_TIME_MIDNIGHT; 
	}
	
	private void initializeEndTimeToMidnight() {
		endHour = NUM_TIME_MIDNIGHT;
		endMin = NUM_TIME_MIDNIGHT; 
	}
	
	/*
     *  Given a time string such as 5:15pm,
     *  Method parses the string accordingly using NattyTime library 
     *  @return integer array of 3 elements: year, month and day
     */
    public static int[] dateFromDateString(String dateString) {
    	int[] date = new int[TOTAL_DATE_FIELDS];
    	
    	// Overcoming NattyTime limitation (Natty parses dates as MM/DD/YYYY) 
		dateString = RegExp.changeDateFormat(dateString);
		
		// Calling NattyTime parser
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(dateString);
		for(DateGroup group: groups) {
			List<Date> dates = group.getDates();
			MutableDateTime tempDate = new MutableDateTime(dates.get(0));
			date[NUM_DAY_INDEX] = tempDate.getDayOfMonth();
			date[NUM_MONTH_INDEX] = tempDate.getMonthOfYear();
			date[NUM_YEAR_INDEX] = tempDate.getYear();
		}
		return date;
    }
    
    /* Given a string of time format (eg. "5pm"),
     * Method will parse the string accordingly using NattyTime
     * @return integer array with 2 elements: hour and minute 
     */
    public static int[] timeFromTimeString(String timeString) {
    	int[] time = new int[TOTAL_TIME_FIELDS];
    	
    	// Calling NattyTime parser
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(timeString);
		for(DateGroup group: groups) {
			List<Date> dates = group.getDates();
			MutableDateTime tempTime = new MutableDateTime(dates.get(0));
			time[NUM_HOUR_INDEX] = tempTime.getHourOfDay();
			time[NUM_MIN_INDEX] = tempTime.getMinuteOfHour();
		}
		return time;
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