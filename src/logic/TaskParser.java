package logic;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class TaskParser {
	
	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	private static final int NUM_YEAR_INDEX = 2;
	private static final int NUM_MONTH_INDEX = 1;
	private static final int NUM_DAY_INDEX = 0;
	
	
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
	// Method will store results in a time array and date array
	public void parseTask() {
		ArrayList<String> timeArray = RegExp.parseTime(taskDescription);
		ArrayList<String> dateArray = RegExp.parseDate(taskDescription);
		taskDescription = RegExp.parseDescription(taskDescription);
		initializeTime(timeArray);
		initializeDate(dateArray);
		finalizeDateTime();
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
		System.out.println("Task Description: " + taskDescription);
		return taskDescription.trim();
	}
	
	public DateTime getStartDateTime() {
		System.out.println("Start Date: " + startDay + "/" + startMonth + "/" + startYear + ". Start Time: " + startHour + ":" + startMin);
		//return new DateTime(startYear, startMonth, startDay, startHour, startMin);
		return null;
	}
	
	public DateTime getEndDateTime() {
		System.out.println("End Date: " + endDay + "/" + endMonth + "/" + endYear + ". Start Time: " + endHour + ":" + endMin);
		//return new DateTime(endYear, endMonth, endDay, endHour, endMin);
		return null;
	}
	
	// Method will finalize all date and time dates accordingly
	private void finalizeDateTime() {
		
	}
}
