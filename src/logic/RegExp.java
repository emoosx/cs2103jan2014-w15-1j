package logic;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegExp {

	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	
	// Date Regular Expressions
	// 1. DDMM[YY[YY]]
    public static String DATE1 = "\\b\\d{1,2}\\/\\d{1,2}(?:\\/\\d{2,4})?\\b";    
    // 2. DD string_rep_of_month [YY[YY]]
    public static String DATE2 = "\\b\\d{1,2}\\s(?:january|febuary|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(?:\\s\\d{2,4})?\\b";
    
    // Date Input Expressions
    public static String[] regexDateArray = {"\\b(?:(on||at)\\s)(\\d{1,2}[-/]\\d{1,2}[-/]\\d{1,4}\\b)"};
    
    // Time Input Expressions
    public static String[] regexTimeArray = {"\\b(?:(at|by|due|on|from|to)\\s)(([1-9]|1[0-2]):[0-5][0-9][AaPp][Mm])\\b", //HH:MM am|pm (eg. 12:45pm, 1:25am)
									    	"\\b(?:(at|by|due|on|from|to)\\s)(([1-9]|1[0-2])[AaPp][Mm])\\b",				//HH am|pm (eg. 5pm, 12am)										
									    	"\\b(?:(at|by|due|on|from|to)\\s)(([0-1]?[0-9]|2[0-3]):[0-5][0-9])\\b"};  		  	//HH:MM (eg. 23:59, 13:15)
    											
    
    // Date Format Expressions
    public static String REGEX_DATE_FORMAT_1 = "\\d{1,2}[-/]\\d{1,2}[-/]\\d{1,4}";
    
    // Time Format Expressions 
    public static String REGEX_TIME_FORMAT_1 = "([1-9]|1[0-2]):[0-5][0-9][AaPp][Mm]";
    public static String REGEX_TIME_FORMAT_2 = "([1-9]|1[0-2])[AaPp][Mm]";
    
    // Hash Tag Regular Expressions
    public static String HASHTAG = "#(.+?)\\b";
    
    // Method returns integer array of 3 elements given time string
    public static int[] dateFromDateString(String dateString) {
    	int[] date = new int[TOTAL_DATE_FIELDS];
    	
    	// Case 1: DD-MM-YYYY
    	Pattern pattern = Pattern.compile(REGEX_DATE_FORMAT_1);
    	Matcher matcher = pattern.matcher(dateString);
    	if(matcher.find()) {
    		String[] dateStringArray = dateString.split("[-|/]");
        	date[0] = Integer.parseInt(dateStringArray[0]);
        	date[1] = Integer.parseInt(dateStringArray[1]);
        	date[2] = Integer.parseInt(dateStringArray[2]);
        	return date;
    	}
    	
    	// Case 2: DDMMYYYY
		return null;
    }
    
    // Method returns integer array of 2 elements given time string
    public static int[] timeFromTimeString(String timeString) {
    	int[] time = new int[TOTAL_TIME_FIELDS];
    	
    	// Case 1: HH:MM am|pm
    	Pattern pattern = Pattern.compile(REGEX_TIME_FORMAT_1);
    	Matcher matcher = pattern.matcher(timeString);
    	if(matcher.find()) {
    		String[] timeStringArray = timeString.split(":");
    		time[NUM_HOUR_INDEX] = Integer.parseInt(timeStringArray[NUM_HOUR_INDEX]);
    		if(timeStringArray[NUM_MIN_INDEX].contains("pm")) {
    			timeStringArray[NUM_MIN_INDEX] = timeStringArray[NUM_MIN_INDEX].replace("pm", "");
    			time[NUM_HOUR_INDEX] = (time[0] % 12) + 12;
    			time[NUM_MIN_INDEX] = Integer.parseInt(timeStringArray[1]);
    		} else {
    			timeStringArray[NUM_MIN_INDEX] = timeStringArray[NUM_MIN_INDEX].replace("am", "");
    			time[NUM_MIN_INDEX] = Integer.parseInt(timeStringArray[NUM_MIN_INDEX]);
    		}
    		return time;
    	}
    	
    	// Case 2: HH am|pm
    	pattern = Pattern.compile(REGEX_TIME_FORMAT_2);
    	matcher = pattern.matcher(timeString);
    	if(matcher.find()) {
    		if(timeString.contains("pm")) {
    			timeString = timeString.replace("pm", "");
    			time[NUM_HOUR_INDEX] = Integer.parseInt(timeString) % 12 + 12;
    		} else {
    			timeString = timeString.replace("am", "");
    			time[NUM_HOUR_INDEX] = Integer.parseInt(timeString);
    		}
    		return time;
    	}
    	
    	// Case 3: HH:MM
    	
    	return null;
    }
    
    // Method parses raw input into an array list of date strings 
    public static ArrayList<String> parseDate(String userInput) {
    	ArrayList<String> dateArray = new ArrayList<String>();
    	for(int i=0; i<regexDateArray.length; i++) {
    		Pattern pattern = Pattern.compile(regexDateArray[i]);
    		Matcher matcher = pattern.matcher(userInput);
    		while(matcher.find()) {
    			dateArray.add(matcher.group(2));
    		}
    		// size 1 = deadline tasks, size 2 = timed task
    		if(dateArray.size() != 0) {
    			break;
    		}
    	}
    	return dateArray;
    	
    }
    
    // Method parses raw user input into an array list of time strings 
    public static ArrayList<String> parseTime(String userInput) {
    	ArrayList<String> timeArray = new ArrayList<String>();
    	for(int i=0; i<regexTimeArray.length; i++) {
    		Pattern pattern = Pattern.compile(regexTimeArray[i]);
    		Matcher matcher = pattern.matcher(userInput);
    		while(matcher.find()) {
    			timeArray.add(matcher.group(2));
    		}
    		// size 1 = deadline tasks, size 2 = timed task
    		if(timeArray.size() != 0) {
    			break;
    		}
    	}
    	return timeArray; 
    }

    // Method will replace all time and date regular expressions in user input with ""
	public static String parseDescription(String taskDescription) {
		// Replacing all matched time regex with ""
		for(int i=0; i<regexTimeArray.length; i++) {
    		Pattern pattern = Pattern.compile(regexTimeArray[i]);
    		Matcher matcher = pattern.matcher(taskDescription);
    		while(matcher.find()) {
    			taskDescription = taskDescription.replaceAll(regexTimeArray[i], "");
    		}
		}
    	// Replacing all matched date regex with ""
		for(int i=0; i<regexDateArray.length; i++) {
    		Pattern pattern = Pattern.compile(regexDateArray[i]);
    		Matcher matcher = pattern.matcher(taskDescription);
    		while(matcher.find()) {
    			taskDescription = taskDescription.replaceAll(regexDateArray[i], "");
    		}
    	}
		return taskDescription;
	}
}
