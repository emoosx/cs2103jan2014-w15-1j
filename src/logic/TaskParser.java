package logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.ParseLocation;
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
	private static final int NUM_CENTURY_YEAR = 2000;
	private static final int NUM_2_DIGIT_FORMAT = 100;

	// Task object variables
	private DateTime startDateTime;
	private DateTime endDateTime;
	private String taskDescription;

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
	}

	// Method will call RegExp class to get all date and time of user's input
	// Method will then call other methods to initialize all date and time
	// variables
	public void parseTask() {
		PandaLogger.getLogger().info("TaskParser.taskParse");
		PandaLogger.getLogger().info("taskDescription:" + taskDescription);
		ArrayList<String> timeArray = RegExp.parseTime(taskDescription);
		ArrayList<String> dateArray = RegExp.parseDate(taskDescription);
		taskDescription = RegExp.parseDescription(taskDescription);
		initializeTime(timeArray);
		initializeDate(dateArray);
		try {
			finalizeDateTime();
		} catch (Exception e) {
			showToUser(MESSAGE_INVALID_TIME_DATE);
		}
	}

	public void parseTask2() {
		Parser parser = new Parser();
		System.out.println("Input :" + taskDescription);
		ArrayList<String> timeArray = RegExp.parseTime(taskDescription);
		System.out.println("Timestamps :" + timeArray);
		List<DateGroup> groups = parser.parse(taskDescription);
		List<Date> dates = null;
		for (DateGroup group : groups) {
			dates = group.getDates();
			String matchingValue = group.getText();
			taskDescription = removeMatchingValue(taskDescription, matchingValue);
//			System.out.println(matchingValue);
		}
		System.out.println("Dates :" + dates);
		taskDescription = RegExp.parseDescription(taskDescription);
		System.out.println("Description: " + taskDescription);
	}
	
	private String removeMatchingValue(String desc, String match) {
		String result = desc.replaceFirst("the " + match,  "");
		return result.replaceAll("  ", " ");
	}

	// Given an array of user time inputs, method will parse start and end time
	// accordingly
	private void initializeTime(ArrayList<String> timeArray) {
		// Case 0: user inputs a floating task
		if (timeArray.isEmpty()) {
			return;
		}
		// Case 1: user input having start time and end time
		if (timeArray.size() == 2) {
			int[] startTimeArray = RegExp.timeFromTimeString(timeArray.get(0));
			initializeStartTime(startTimeArray);
			int[] endTimeArray = RegExp.timeFromTimeString(timeArray.get(1));
			initializeEndTime(endTimeArray);
			// Case 2: user input having only end time
		} else {
			int[] endTimeArray = RegExp.timeFromTimeString(timeArray.get(0));
			initializeEndTime(endTimeArray);
		}
	}

	// Given an integer array of 2 time field values (hour and minute), it will
	// initialize start time
	private void initializeStartTime(int[] startTimeArray) {
		startHour = startTimeArray[NUM_HOUR_INDEX];
		startMin = startTimeArray[NUM_MIN_INDEX];
	}

	// Given an integer array of 2 time field values (hour and minute), it will
	// initialize end time
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
		}
		// Case 1: user input having start date and end date
		if (dateArray.size() == 2) {
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

	// Given an integer array of 3 date field values (year, month and day), it
	// will initialize start date
	private void initializeStartDate(int[] startDateArray) {
		startYear = startDateArray[NUM_YEAR_INDEX];
		startMonth = startDateArray[NUM_MONTH_INDEX];
		startDay = startDateArray[NUM_DAY_INDEX];
	}

	// Given an integer array of 3 date field values (year, month and day), it
	// will initialize end date
	private void initializeEndDate(int[] endDateArray) {
		endYear = endDateArray[NUM_YEAR_INDEX];
		endMonth = endDateArray[NUM_MONTH_INDEX];
		endDay = endDateArray[NUM_DAY_INDEX];
	}

	public String getTaskDescription() {
		// System.out.println("Task Description: " + taskDescription);
		return taskDescription.trim();
	}

	public DateTime getStartDateTime() {
		// System.out.println("Start Date: " + startDay + "/" + startMonth + "/"
		// + startYear + ". Start Time: " + startHour + ":" + startMin);
		return startDateTime;
	}

	public DateTime getEndDateTime() {
		// System.out.println("End Date: " + endDay + "/" + endMonth + "/" +
		// endYear + ". Start Time: " + endHour + ":" + endMin);
		return endDateTime;
	}

	// Method will finalize all date and time dates accordingly
	private void finalizeDateTime() {
		if (startYear != null && startYear < NUM_2_DIGIT_FORMAT) {
			startYear += NUM_CENTURY_YEAR;
		}
		if (endYear != null && endYear < NUM_2_DIGIT_FORMAT) {
			endYear += NUM_CENTURY_YEAR;
		}
		finalizeFloatingTask();
		finalizeTimedTask();
		finalizeDeadlineTask();
	}

	// Method will finalize date and time if variables fits that of a floating
	// task
	private void finalizeFloatingTask() {
		if (startHour == null && endHour == null && startYear == null
				&& endYear == null) {
			startDateTime = null;
			endDateTime = null;
		}
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

	// Method will finalize date and time if variables fits that of a deadline
	// task
	private void finalizeDeadlineTask() {
		if (startHour == null && endHour != null) {
			if (endYear == null) {
				// Initialize date to local date if end time is stated by user
				// but not the date
				initializeDateToToday();
			}
			startDateTime = null;
			endDateTime = new DateTime(endYear, endMonth, endDay, endHour,
					endMin);
		}
	}

	// Method will initialize date to local date if it is not stated by user
	private void initializeDateToToday() {
		startYear = endYear = Calendar.getInstance().get(Calendar.YEAR);
		startMonth = endMonth = Calendar.getInstance().get(Calendar.MONTH)
				+ NUM_CALENDAR_MONTH_OFFSET;
		startDay = endDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	// Method will print statement given string arguments
	private void showToUser(String outputString) {
		System.out.println(outputString);
	}
}
