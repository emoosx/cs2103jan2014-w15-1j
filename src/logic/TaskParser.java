package logic;

import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;

public class TaskParser {
	
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
	private Integer	startMonth;
	private Integer	startDay;
	private Integer	endYear;
	private Integer	endMonth;
	private Integer	endDay;
	
	
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
	// Method will then initialize all date and time variables
	public void parseTask() {
		ArrayList<String> timeArray = RegExp.parseTime(taskDescription);
		ArrayList<String> dateArray = RegExp.parseDate(taskDescription);
		taskDescription = RegExp.parseDescription(taskDescription);
		initializeTime(timeArray);
		initializeDate(dateArray);
		try {
			finalizeDateTime();
		} catch (Exception e) {
			System.out.println("Invalid time and date format");
			e.printStackTrace();
		}
	}
	
	// Given an array of user time, method will parse start and end time accordingly
	private void initializeTime(ArrayList<String> timeArray) {
		// Case 0: user inputs a floating task
		if(timeArray.isEmpty()) {
			return;
		}
		// Case 1: user input having start time and end time
		if(timeArray.size()==2) {
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
	
	private void initializeStartTime(int[] startTimeArray) {
		startHour = startTimeArray[NUM_HOUR_INDEX];
		startMin = startTimeArray[NUM_MIN_INDEX];
	}
	
	private void initializeEndTime(int[] endTimeArray) {
		endHour = endTimeArray[NUM_HOUR_INDEX];
		endMin = endTimeArray[NUM_MIN_INDEX];
	}
	
	// Given an array of user dates, method will parse start and end date accordingly
	private void initializeDate(ArrayList<String> dateArray) {
		// Case 0: user inputs a floating task
		if(dateArray.isEmpty()) {
			return;
		}
		// Case 1: user input having start date and end date
		if(dateArray.size() == 2) {
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
		// User specified start time but not start date, assumed to be on same day as end date
		if(startHour != null && startYear == null) {
			initializeStartDate(endDateArray);
		}
	}
	
	public String getTaskDescription() {
		//System.out.println("Task Description: " + taskDescription);
		return taskDescription.trim();
	}
	
	public DateTime getStartDateTime() {
		//System.out.println("Start Date: " + startDay + "/" + startMonth + "/" + startYear + ". Start Time: " + startHour + ":" + startMin);
		return startDateTime;
	}
	
	public DateTime getEndDateTime() {
		//System.out.println("End Date: " + endDay + "/" + endMonth + "/" + endYear + ". Start Time: " + endHour + ":" + endMin);
		return endDateTime;
	}
	
	// Method will finalize all date and time dates accordingly
	private void finalizeDateTime() {
		if(startYear != null && startYear < NUM_2_DIGIT_FORMAT) {
			startYear += NUM_CENTURY_YEAR;
		}
		if(endYear != null && endYear < NUM_2_DIGIT_FORMAT) {
			endYear += NUM_CENTURY_YEAR;
		}
		finalizeFloatingTask();
		finalizeTimedTask();
		finalizeDeadlineTask();
	}
	
	// Method will finalize date and time if variables fits that of a floating task
	private void finalizeFloatingTask() {
		if(startHour == null && endHour == null &&
				startYear == null && endYear == null) {
			startDateTime = null;
			endDateTime = null;
		}		
	}
	
	// Method will finalize date and time if variables fits that of a timed task
	private void finalizeTimedTask() {
		if(startHour != null && endHour != null) {
			// if 1 date is given, task starts and end on the same day
			if(endYear != null && startYear == null) {
				startYear = endYear;
				startMonth = endMonth;
				startDay = endDay;
			}
			if(endYear == null && startYear == null) {
				initializeDateToToday();
			}
			startDateTime = new DateTime(startYear, startMonth, startDay, startHour, startMin);
			endDateTime = new DateTime(endYear, endMonth, endDay, endHour, endMin);
		}
	}
	
	// Method will finalize date and time if variables fits that of a deadline task
	private void finalizeDeadlineTask() {
		if(startHour == null && endHour != null) {
			if(endYear == null) {
				initializeDateToToday();
			}
			startDateTime = null;
			endDateTime = new DateTime(endYear, endMonth, endDay, endHour, endMin);	
		}
	}
	
	// Method will initialize date to local time if it is not stated by user
	private void initializeDateToToday() {
		startYear = endYear = Calendar.getInstance().get(Calendar.YEAR);
		startMonth = endMonth = Calendar.getInstance().get(Calendar.MONTH) + NUM_CALENDAR_MONTH_OFFSET;
		startDay = endDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
}
