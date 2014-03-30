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

/*
 * TaskParser class will aid the adding of tasks through parsing of user's raw data
 * It is called by any task object to correctly parse user's raw data input
 * It will take in a raw user input and create 2 DateTime objects and 1 task description string using RegExp class
 * The task object can then get these data through getter methods available. 
 */

public class TaskParser {

	private static final String MESSAGE_INVALID_TIME_DATE = "Invalid date and time format";
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
	public TaskParser(String userInput) {
		taskDescription = userInput;
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

	// Method will call RegExp class to get all date and time of user's input
	// Method will then call other methods to initialize all date and time
	// variables
	public void parseTask() {
		PandaLogger.getLogger().info("TaskParser.parseTask1");
		PandaLogger.getLogger().info("taskDescription:" + taskDescription);
		ArrayList<String> timeArray = RegExp.parseTime(taskDescription);
		ArrayList<String> dateArray = RegExp.parseDate(taskDescription);
		hashtags = RegExp.parseHashtag(taskDescription);
		taskDescription = RegExp.parseDescription(taskDescription);
		try {
			initializeTimeAndDate(timeArray, dateArray);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
/*
	// Method combines the use of third party library (natty) to parse date, and regular expressions to parse time
	public void parseTask2() {
		PandaLogger.getLogger().info("TaskParser.parseTask2");
		PandaLogger.getLogger().info("taskDescription:" + taskDescription);
		Parser parser = new Parser();
		ArrayList<String> timeArray = RegExp.parseTime(taskDescription);
		List<DateGroup> groups = parser.parse(taskDescription);
		List<Date> dates = new ArrayList<Date>();
		String matchingValue = null;

		for (DateGroup group : groups) {
			dates = group.getDates();
			matchingValue = group.getText();
			System.out.print("Matching Value::\t" + matchingValue+ ". ");
			for(Date date : dates) {
				System.out.println(date);
			}
		}
//		taskDescription = RegExp.parseDescription(taskDescription);
		System.out.println(taskDescription);
		initStartAndEndDateTime(timeArray, dates);
		prepareDescription(taskDescription, matchingValue);

	}
	
	private String prepareDescription(String original, String matchingValue) {
		String result = "";

		PandaLogger.getLogger().info("original desc:" + original);
		PandaLogger.getLogger().info("match:" + matchingValue);
		result = RegExp.parseDescription(original);
		PandaLogger.getLogger().info("after clement: " + result);
		result = result.replaceFirst("the " + matchingValue,  "");
		result = result.replaceAll("  ", " ");
		PandaLogger.getLogger().info("after substraction: " + result);
		return result;
	}
	
	private void initStartAndEndDateTime(ArrayList<String> timeArray, List<Date> dates) {
		PandaLogger.getLogger().info("timeArray: " + timeArray);
		PandaLogger.getLogger().info("dates: " + dates);
		if(dates.size() == 1) {
            PandaLogger.getLogger().info("Dates.size() = 1");
			mutableEndDateTime = new MutableDateTime(dates.get(0));
			if(timeArray.size() == 1) {
				PandaLogger.getLogger().info("timeArray.size() = 1");
                int[] time = RegExp.timeFromTimeString(timeArray.get(0));
                mutableEndDateTime.setTime(time[0], time[1], 0, 0);
                PandaLogger.getLogger().info("MutableEndDateTime:" + mutableEndDateTime);
			}
			else if(timeArray.size() == 2) {
				mutableStartDateTime = new MutableDateTime(dates.get(1));
			}
		} else if(dates.size() == 2) {
			//PandaLogger.getLogger().info("Date.size() = 2");
			Collections.sort(dates);
			mutableStartDateTime = new MutableDateTime(dates.get(0));
			mutableEndDateTime = new MutableDateTime(dates.get(1));
		}
	}
	
	public MutableDateTime getMutableStartDateTime() {
		return mutableStartDateTime;
	}
	
	public MutableDateTime getMutableEndDateTime() {
		return mutableEndDateTime;
	}
*/
	
	private void initializeTimeAndDate(ArrayList<String> timeArray, ArrayList<String> dateArray) {
		PandaLogger.getLogger().info("Initializing time and date variables...");
		initializeTime(timeArray);
		initializeDate(dateArray);
		finalizeDateTime();
	}
	
	// Given an array of user time inputs, method will parse start and end time
	// accordingly
	private void initializeTime(ArrayList<String> timeArray) {
		// Case 0: user inputs a floating task
		if (timeArray.isEmpty()) {
			return;
		// Case 1: Timed task
		} else if(timeArray.size() == 2) {
			int[] startTimeArray = RegExp.timeFromTimeString(timeArray.get(0));
			initializeStartTime(startTimeArray);
			int[] endTimeArray = RegExp.timeFromTimeString(timeArray.get(1));
			initializeEndTime(endTimeArray);
		// Case 2: Deadline task initialized as end time variable
		} else {
			int[] endTimeArray = RegExp.timeFromTimeString(timeArray.get(0));
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

	// Given an array of user date inputs, method will parse start and end date
	// accordingly
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

	public String getTaskDescription() {
		return taskDescription.trim();
	}

	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public DateTime getEndDateTime() {
		return endDateTime;
	}

	/* 
	 * Method will finalize all DateTime objects accordingly
	 * Main function of this method is to set unspecified date or time to default settings
	 * Default time: 00:00, Default date: today
	 */
	private void finalizeDateTime() {
		// Case 1: Finalized floating task
		if(startHour == null && endHour == null
				&& startYear == null && endYear == null) {
			finalizeFloatingTask();
		// Case 2: Finalized as deadline task as long as 1 date or 1 time input is specified
		} else if((startHour == null && endHour != null)
				|| (startYear == null && endYear !=null && startHour == null)) {
			finalizeDeadlineTask();
		// Case 3: Finalized timed task	
		} else if(startHour != null && endHour != null) {
			finalizeTimedTask();
		} else {
			assert(false);
		}
	}

	// Method will finalize the 2 DateTime objects to null
	private void finalizeFloatingTask() {
		startDateTime = null;
		endDateTime = null;
	}

	// Method will finalize date and time if variables fits that of a timed task
	private void finalizeTimedTask() {
		if (startHour != null && endHour != null) {
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
			startDateTime = new DateTime(startYear, startMonth, startDay,
					startHour, startMin);
			endDateTime = new DateTime(endYear, endMonth, endDay, endHour,
					endMin);
		}
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

	public ArrayList<String> getHashTag() {
		return hashtags;
	}
}
