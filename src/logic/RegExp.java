package logic;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.PandaLogger;

public class RegExp {

	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	private static final int INDEX_FIRST_CASE = 0;
	private static final int INDEX_SECOND_CASE = 1;
	private static final int INDEX_THIRD_CASE = 2;
	private static final int NUM_CENTURY = 2000;
	private static final int NUM_TWO_DIGIT_YEAR = 100;
	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	private static final int NUM_START_TIME_GROUP = 1;
	private static final int NUM_END_TIME_GROUP = 4;
	private static final String STRING_TIMEZONE_AM = "am";
	private static final String STRING_TIMEZONE_PM = "pm";
	
    /*
     *  Date Input Expressions
     *  These patterns are used to compare with user's raw input to extract date data 
     */
    public static String[] regexDateArray = {
    	// Case 1: DD-MM-YY(YY) or DD/MM/YY(YY) 
    	"\\bon\\s((([1-9]|[12]\\d|3[01])-([13578]|1[02])-(\\d{4}|\\d{2})|([1-9]|[12]\\d|30)-([1-9]|1[02])-(\\d{4}|\\d{2}))|(([1-9]|[12]\\d|3[01])/([13578]|1[02])/(\\d{4}|\\d{2})|([1-9]|[12]\\d|30)/([1-9]|1[02])/(\\d{4}|\\d{2})))\\b"};
    
    /*
     *  Time Input Expressions
     *  These patterns are used to compare with user's raw input to extract time data
     */
    public static String[] regexTimeArray = {
    	// Case 1: "from TIME to TIME", where TIME is HH:MM(am/pm) or HH(am/pm), all cases are insensitive and HH is between 1-12 and MM is between 0 to 59
    	"\\bfrom\\s(([1-9]|1[0-2])(:[0-5][0-9])?[aApP][mM])\\sto\\s(([1-9]|1[0-2])(:[0-5][0-9])?[aApP][mM])\\b",
    	// Case 2: "from TIME to TIME", where TIME is HH:MM or HHMM, H is between 0-23 and MM is between 0 to 59									
    	"\\bfrom\\s(([0-1][0-9]|2[0-3]):?([0-5][0-9]))\\sto\\s(([0-1][0-9]|2[0-3]):?[0-5][0-9])\\b",											
    	// Case 3: "at/by TIME", where TIME can be HH:MM(am/pm) or HH(am/pm), all cases are insensitive
    	"\\b(at|by)\\s((([1-9]|1[0-2])(:[0-5][0-9])?[aApP][mM])|(([0-1][0-9]|2[0-3]):?[0-5][0-9]))\\b",
    	// Case 4: "at/by TIME", where TIME can be HH:MM or HHMM, H is between 0-23 and MM is between 0-59
    	"\\b(at|by)\\s(([0-1][0-9]|2[0-3]):?[0-5][0-9])\\b"
    	};
    
    /*
     * Date Format Expression for parsing raw date data
     * These formats are used mainly in the dateFromDateString method that convert date parameters into an int[] which contains 3 elements: year, month and day 
     */
    // Case 1: DD/MM/YY(YY) or DD-MM-YY(YY) or  
    public static String REGEX_DATESTRING_PATTERN_1 = "((([1-9]|[12]\\d|3[01])-([13578]|1[02])-(\\d{4}|\\d{2})|([1-9]|[12]\\d|30)-([1-9]|1[02])-(\\d{4}|\\d{2}))|(([1-9]|[12]\\d|3[01])/([13578]|1[02])/(\\d{4}|\\d{2})|([1-9]|[12]\\d|30)/([1-9]|1[02])/(\\d{4}|\\d{2})))\\b";
    
    /*
     *  Time Format Expressions for parsing raw time data
     *  These formats are used mainly in the timeFromTimeString method that convert time parameters into an int[] which contains 2 elements: hour and minute 
     */
    // Case 1: HH:MM (am/pm)
    public static String REGEX_TIMESTRING_PATTERN_1 = "([1-9]|1[0-2]):[0-5][0-9][AaPp][Mm]";
    // Case 2: HH (am/pm)
    public static String REGEX_TIMESTRING_PATTERN_2 = "([1-9]|1[0-2])[AaPp][Mm]"; 
    // Case 3: HH:MM
    public static String REGEX_TIMESTRING_PATTERN_3 = "([0-1][0-9]|2[0-3]):[0-5][0-9]";
    // Case 4: HHMM
    public static String REGEX_TIMESTRING_PATTERN_4 = "([0-1][0-9]|2[0-3])[0-5][0-9]";
    
    // Hash Tag Regular Expressions
    public static String REGEX_HASHTAG = "(?<=^|(?<=[^a-zA-Z0-9-\\.]))#([A-Za-z]+[A-Za-z0-9]+)";
    
    /*
     *  Given a time string such as 5:15pm,
     *  Method will check with all date string patterns to identify patterns and parses the string accordingly 
     *  @return integer array of 3 elements: year, month and day
     */
    public static int[] dateFromDateString(String dateString) {
    	int[] date = new int[TOTAL_DATE_FIELDS];

    	// Case 1: DD-MM-YY(YY)
    	Pattern pattern = Pattern.compile(REGEX_DATESTRING_PATTERN_1);
    	Matcher matcher = pattern.matcher(dateString);
    	if(matcher.find()) {
    		
    		String[] dateStringArray = dateString.split("[-/]");
        	date[0] = Integer.parseInt(dateStringArray[0]);
        	date[1] = Integer.parseInt(dateStringArray[1]);
        	date[2] = Integer.parseInt(dateStringArray[2]);
        	if(date[2] < NUM_TWO_DIGIT_YEAR) {
        		date[2] += NUM_CENTURY;
        	}
        	return date;
    	}
		return null;
    }
    
    /* Given a string of time format (eg. "5pm"),
     * Method will check with all time string patterns to identify patterns and parses the string accordingly
     * @return integer array with 2 elements: hour and minute 
     */
    public static int[] timeFromTimeString(String timeString) {
    	int[] time = new int[TOTAL_TIME_FIELDS];
    	
    	// Case 1: HH:MM am|pm
    	Pattern pattern = Pattern.compile(REGEX_TIMESTRING_PATTERN_1);
    	Matcher matcher = pattern.matcher(timeString);
    	if(matcher.find()) {
    		String[] timeStringArray = timeString.split(":");
    		time[NUM_HOUR_INDEX] = Integer.parseInt(timeStringArray[NUM_HOUR_INDEX]);
    		if(timeStringArray[NUM_MIN_INDEX].contains(STRING_TIMEZONE_PM)) {
    			timeStringArray[NUM_MIN_INDEX] = timeStringArray[NUM_MIN_INDEX].replace(STRING_TIMEZONE_PM, "");
    			time[NUM_HOUR_INDEX] = (time[NUM_HOUR_INDEX] % 12) + 12;
    			time[NUM_MIN_INDEX] = Integer.parseInt(timeStringArray[1]);
    		} else {
    			timeStringArray[NUM_MIN_INDEX] = timeStringArray[NUM_MIN_INDEX].replace(STRING_TIMEZONE_AM, "");
    			time[NUM_MIN_INDEX] = Integer.parseInt(timeStringArray[NUM_MIN_INDEX]);
    		}
    		return time;
    	}
    	
    	// Case 2: HH am|pm
    	pattern = Pattern.compile(REGEX_TIMESTRING_PATTERN_2);
    	matcher = pattern.matcher(timeString);
    	if(matcher.find()) {
    		if(timeString.contains(STRING_TIMEZONE_PM)) {
    			timeString = timeString.replace(STRING_TIMEZONE_PM, "");
    			time[NUM_HOUR_INDEX] = Integer.parseInt(timeString) % 12 + 12;
    		} else {
    			timeString = timeString.replace(STRING_TIMEZONE_AM, "");
    			time[NUM_HOUR_INDEX] = Integer.parseInt(timeString);
    		}
    		return time;
    	}
    	
    	// Case 3: HH:MM
    	pattern = Pattern.compile(REGEX_TIMESTRING_PATTERN_3);
    	matcher = pattern.matcher(timeString);
    	if(matcher.find()) {
    		String[] timeStringArray = timeString.split(":");
    		time[NUM_HOUR_INDEX] = Integer.parseInt(timeStringArray[0]); 
    		time[NUM_MIN_INDEX] = Integer.parseInt(timeStringArray[1]);
    		return time;
    	}
    	
    	// Case 4: HHMM
    	pattern = Pattern.compile(REGEX_TIMESTRING_PATTERN_4);
    	matcher = pattern.matcher(timeString);
    	if(matcher.find()) {
    		time[NUM_HOUR_INDEX] = Integer.parseInt(timeString.substring(0,2));
    		time[NUM_MIN_INDEX] = Integer.parseInt(timeString.substring(2,4));
    		return time;
    	}
    	
    	// asserting false because code should not reach here due to initial pattern filtering of raw user input
    	assert(false);
    	return null;
    }
    
    // Method parses raw input into an array list of date strings 
    public static ArrayList<String> parseDate(String userInput) {
    	ArrayList<String> dateArray = new ArrayList<String>();
    	
    	// Case 1: "on DD-MM-YY(YY) or DD/MM/YY(YY)"
    	Pattern pattern = Pattern.compile(regexDateArray[INDEX_FIRST_CASE]);
    	Matcher matcher = pattern.matcher(userInput);
    	if(matcher.find()) {
    		dateArray.add(matcher.group(1));
    		return dateArray;
    	}
    	
    	for(int i=0; i<regexDateArray.length; i++) {
    		pattern = Pattern.compile(regexDateArray[i]);
    		matcher = pattern.matcher(userInput);
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
    
    // Method will try to match all time regular expressions
    // and parse the raw user input into an array list of time strings and return it
    public static ArrayList<String> parseTime(String userInput) {
    	ArrayList<String> timeArray = new ArrayList<String>();
    	
    	// Case 1: "from TIME to TIME", where TIME can be HH:MM(am/pm) or HH(am/pm)
    	Pattern pattern = Pattern.compile(regexTimeArray[INDEX_FIRST_CASE]);
		Matcher matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			timeArray.add(matcher.group(NUM_START_TIME_GROUP));
			timeArray.add(matcher.group(NUM_END_TIME_GROUP));
			return timeArray;
		}
    	
		// Case 2: "from TIME to TIME", where TIME can be HH:MM or HHMM
		pattern = Pattern.compile(regexTimeArray[INDEX_SECOND_CASE]);
		matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			timeArray.add(matcher.group(NUM_START_TIME_GROUP));
			timeArray.add(matcher.group(NUM_END_TIME_GROUP));
			return timeArray;
		}
		
		// Case 3: by/at/due "TIME", where TIME can be HH:MM(am/pm) or HH(am/pm) or HH:MM
		pattern = Pattern.compile(regexTimeArray[INDEX_THIRD_CASE]);
		matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			String timeString = userInput.substring(matcher.start(), matcher.end());
			String[] userInputArray = timeString.split(" ");
			timeArray.add(userInputArray[1]);
			return timeArray;
		}
		
    	return timeArray; 
    }

    /*
     *  Method will remove all time and date patterns to obtain task description
     *  @returns task description:String
     */
	public static String parseDescription(String taskDescription) {
		
		// Replacing all matched time regex with ""
		for(int i=0; i<regexTimeArray.length; i++) {
			taskDescription = taskDescription.replaceAll(regexTimeArray[i], "");
		}
		
    	// Replacing all matched date regex with ""
		for(int i=0; i<regexDateArray.length; i++) {
			taskDescription = taskDescription.replaceAll(regexDateArray[i], "");
    	}
		
		// Replacing hashtags with ""
		taskDescription = taskDescription.replaceAll(REGEX_HASHTAG, "");
		
		return taskDescription.trim();
	}
	
	/*
	 *  Method will attempt to parse a hashtag if possible and return the string
	 *  @return a list of hashtags or null if hashtag does not exist/invalid
	 */
	public static ArrayList<String> parseHashtag(String userInput) {
		ArrayList<String> hashtag = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile(REGEX_HASHTAG);
		Matcher matcher = pattern.matcher(userInput);
		while(matcher.find()) {
			hashtag.add(userInput.substring(matcher.start(), matcher.end()));
		}
		PandaLogger.getLogger().info("Hashtag obtained: " + hashtag);
		return hashtag;
	}
}
