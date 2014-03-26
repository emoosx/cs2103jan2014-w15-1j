package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.MutableDateTime;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import common.PandaLogger;

public class RegExp {

	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	
	private static final int INDEX_FIRST_CASE = 0;
	private static final int INDEX_SECOND_CASE = 1;
	private static final int INDEX_THIRD_CASE = 2;
	private static final int INDEX_DAY = 0;
	private static final int INDEX_MONTH = 1;
	private static final int INDEX_YEAR = 2;
	
	private static final int NUM_CURRENT_CENTURY = 2000;
	private static final int NUM_TWO_DIGIT_YEAR = 100;
	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	private static final int NUM_START_TIME_GROUP = 1;
	private static final int NUM_END_TIME_GROUP = 4;
	private static final int NUM_MONTH_JANUARY = 1;
	private static final int NUM_MONTH_FEBRUARY = 2;
	private static final int NUM_MONTH_MARCH = 3;
	private static final int NUM_MONTH_APRIL = 4;
	private static final int NUM_MONTH_MAY = 5;
	private static final int NUM_MONTH_JUNE = 6;
	private static final int NUM_MONTH_JULY = 7;
	private static final int NUM_MONTH_AUGUST = 8;
	private static final int NUM_MONTH_SEPTEMBER = 9;
	private static final int NUM_MONTH_OCTOBER = 10;
	private static final int NUM_MONTH_NOVEMBER = 11;
	private static final int NUM_MONTH_DECEMBER = 12;
	private static final int NUM_CURRENT_YEAR = 2014;
	
	
	private static final String STRING_TIMEZONE_AM = "am";
	private static final String STRING_TIMEZONE_PM = "pm";
	
    /*
     *  Date Input Expressions
     *  These patterns are used to compare with user's raw input to extract date data 
     */
    public static String[] regexDateArray = {
    	// Case 1: DD-MM-YY(YY) or DD/MM/YY(YY) 
    	"\\bon\\s((([1-9]|[12]\\d|3[01])-([13578]|1[02])-(\\d{4}|\\d{2})|([1-9]|[12]\\d|30)-([1-9]|1[02])-(\\d{4}|\\d{2}))|(([1-9]|[12]\\d|3[01])/([13578]|1[02])/(\\d{4}|\\d{2})|([1-9]|[12]\\d|30)/([1-9]|1[02])/(\\d{4}|\\d{2})))\\b",
    	// Case 2: partial text based dates (e.g. 15 march 2014, 2 feb)
    	"\\b(?i)on\\s((([1-9]|[12]\\\\d|3[01])\\s(jan|january|mar|march|may|jul|july|aug|august|oct|october|dec|december)(\\s(\\d{4}|\\d{2}))?|([1-9]|[12]\\d|30)\\s(jan|january|feb|february|mar|march|apr|april|may|jun|june|jul|july|aug|august|sep|september|oct|october|nov|november|dec|december)(\\s(\\d{4}|\\d{2}))?))\\b",
    	// Case 3: pure text based relative dates (e.g. next Monday)
    	"\\b(?i)((on\\s)?next\\s((mon(day)?|tues(day)?|wed(nesday)?|thurs(day)?|fri(day)?|sat(urday)?|sun(day)?)))\\b"
    	};
    
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
    // Case 2: Partial text based dates (e.g. 15 march 2014, 2 feb)
    public static String REGEX_DATESTRING_PATTERN_2 = "\\b(?i)((([1-9]|[12]\\\\d|3[01])\\s(jan|january|mar|march|may|jul|july|aug|august|oct|october|dec|december)(\\s(\\d{4}|\\d{2}))?|([1-9]|[12]\\d|30)\\s(jan|january|feb|february|mar|march|apr|april|may|jun|june|jul|july|aug|august|sep|september|oct|october|nov|november|dec|december)(\\s(\\d{4}|\\d{2}))?))\\b";
    // Case 3: pure text based relative dates (e.g. next Monday)
    public static String REGEX_DATESTRING_PATTERN_3 = "\\b(?i)((on\\s)?next\\s((mon(day)?|tues(day)?|wed(nesday)?|thurs(day)?|fri(day)?|sat(urday)?|sun(day)?)))\\b";
    
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
    	if(dateString.matches(REGEX_DATESTRING_PATTERN_1)) {
    		String[] dateStringArray = dateString.split("[-/]");
        	date[INDEX_DAY] = Integer.parseInt(dateStringArray[INDEX_DAY]);
        	date[INDEX_MONTH] = Integer.parseInt(dateStringArray[INDEX_MONTH]);
        	date[INDEX_YEAR] = Integer.parseInt(dateStringArray[INDEX_YEAR]);
        	if(date[INDEX_YEAR] < NUM_TWO_DIGIT_YEAR) {
        		date[INDEX_YEAR] += NUM_CURRENT_CENTURY;
        	}
        	return date;
    	}
    	
    	// Case 2: Partial text based dates (e.g. 15 march 2014, 2 feb)
    	if(dateString.matches(REGEX_DATESTRING_PATTERN_2)) {
    		String[] dateStringArray = dateString.split(" ");
    		date[INDEX_DAY] = Integer.parseInt(dateStringArray[INDEX_DAY]); 
    		date[INDEX_MONTH] = getMonthIndex(dateStringArray[INDEX_MONTH]);
    		if(dateStringArray.length == 3) {
    			date[INDEX_YEAR] = Integer.parseInt(dateStringArray[INDEX_YEAR]);
    			if(date[INDEX_YEAR] < NUM_TWO_DIGIT_YEAR) {
    				date[INDEX_YEAR] += NUM_CURRENT_CENTURY;
    			}
    		} else {
    			date[INDEX_YEAR] = NUM_CURRENT_YEAR;
    		}
    		return date;
    	}
    	
    	// Case 3: pure text based relative dates (e.g. next Monday)
    	if(dateString.matches(REGEX_DATESTRING_PATTERN_3)) {
    		Parser parser = new Parser();
    		List<DateGroup> groups = parser.parse(dateString);
    		for(DateGroup group: groups) {
    			List<Date> dates = group.getDates();
    			MutableDateTime tempDate = new MutableDateTime(dates.get(0));
    			date[0] = tempDate.getDayOfMonth();
    			date[1] = tempDate.getMonthOfYear();
    			date[2] = tempDate.getYear();
    		}
    		System.out.println("next monday: " + date);
    		return date;
    	}
    	
    	// asserting false because code should not reach here due to initial pattern filtering for date inputs
    	assert(false);
		return null;
    }
    
    /* Given a string of time format (eg. "5pm"),
     * Method will check with all time string patterns to identify time patterns and parses the string accordingly
     * Method uses string.matches() API instead of pattern and matcher in the regex API 
     * @return integer array with 2 elements: hour and minute 
     */
    public static int[] timeFromTimeString(String timeString) {
    	int[] time = new int[TOTAL_TIME_FIELDS];
    	
    	// Case 1: HH:MM am|pm
    	if(timeString.matches(REGEX_TIMESTRING_PATTERN_1)) {
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
    	if(timeString.matches(REGEX_TIMESTRING_PATTERN_2)) {
    		if(timeString.contains(STRING_TIMEZONE_PM)) {
    			timeString = timeString.replace(STRING_TIMEZONE_PM, "");
    			time[NUM_HOUR_INDEX] = Integer.parseInt(timeString) % 12 + 12;
    			time[NUM_MIN_INDEX] = 0;
    		} else {
    			timeString = timeString.replace(STRING_TIMEZONE_AM, "");
    			time[NUM_HOUR_INDEX] = Integer.parseInt(timeString);
    			time[NUM_MIN_INDEX] = 0;
    		}
    		return time;
    	}
    	
    	// Case 3: HH:MM
    	if(timeString.matches(REGEX_TIMESTRING_PATTERN_3)) {
    		String[] timeStringArray = timeString.split(":");
    		time[NUM_HOUR_INDEX] = Integer.parseInt(timeStringArray[0]); 
    		time[NUM_MIN_INDEX] = Integer.parseInt(timeStringArray[1]);
    		return time;
    	}
    	
    	// Case 4: HHMM
    	if(timeString.matches(REGEX_TIMESTRING_PATTERN_4)) {
    		time[NUM_HOUR_INDEX] = Integer.parseInt(timeString.substring(0,2));
    		time[NUM_MIN_INDEX] = Integer.parseInt(timeString.substring(2,4));
    		return time;
    	}
    	
    	// asserting false because code should not reach here due to initial pattern filtering for time inputs
    	assert(false);
    	return null;
    }
    
    /*
     *  Method parses raw input into an array list of date strings
     *  It will match user input with all supported date patterns 
     *  @return an ArrayList<String> of dates 
     */
    public static ArrayList<String> parseDate(String userInput) {
    	ArrayList<String> dateArray = new ArrayList<String>();
    	
    	// Case 1: "on DD-MM-YY(YY) or DD/MM/YY(YY)"
    	Pattern pattern = Pattern.compile(regexDateArray[INDEX_FIRST_CASE]);
    	Matcher matcher = pattern.matcher(userInput);
    	if(matcher.find()) {
    		dateArray.add(matcher.group(1));
    		return dateArray;
    	}
    	
    	// Case 2: Partial text based date format (e.g. 12 march 14, 20 aug)
    	pattern = Pattern.compile(regexDateArray[INDEX_SECOND_CASE]);
    	matcher = pattern.matcher(userInput);
    	if(matcher.find()) {
    		dateArray.add(matcher.group(1));
    		return dateArray;
    	}
    	
    	// Case 3: pure text based relative dates (e.g. next Monday)
    	pattern = Pattern.compile(regexDateArray[INDEX_THIRD_CASE]);
    	matcher = pattern.matcher(userInput);
    	if(matcher.find()) {
    		dateArray.add(matcher.group(3));
    		return dateArray;
    	}
    	
    	// Returning empty date array indicates that user input produces 0 date parameter
    	return dateArray;
    }
    
    /*
     *  Method parses raw input into an array list of time strings
     *  It will match user input with all supported time patterns 
     *  @return an ArrayList<String> of time
     */
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
		
		// Returning empty time array indicates that user input produces 0 time parameter
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
		
		PandaLogger.getLogger().info("Task Description obtained: " + taskDescription);
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
	
	/*
	 *  Method changes DD-MM-YYYY format to MM-DD-YYYY to ensure correct parsing by NattyTime
	 *  @return a modified userInput with switched date format
	 */
	public static String changeDateFormat(String userInput) {
		Pattern pattern = Pattern.compile(REGEX_DATESTRING_PATTERN_1);
		Matcher matcher = pattern.matcher(userInput);
		while(matcher.find()) {
			String tempDate = userInput.substring(matcher.start(), matcher.end());
			String[] dateStringArray = tempDate.split("[-/]");
			String newDate = dateStringArray[1] + "/" + dateStringArray[0] + "/" + dateStringArray[2];
			PandaLogger.getLogger().info("Dates switched: " + tempDate + " to " + newDate);
			userInput = userInput.replace(tempDate, newDate);
		}
		return userInput;
	}
	
	/*
	 * Method will return index of month given string format
	 * TODO change magic numbers and strings 
	 * @return integer corresponding to month
	 */
	public static int getMonthIndex(String month) {
		month = month.toLowerCase();
		if(month.equals("jan") || month.equals("january")) {
			return NUM_MONTH_JANUARY;
		} else if(month.equals("feb") || month.equals("february")) {
			return NUM_MONTH_FEBRUARY;
		} else if(month.equals("mar") || month.equals("march")) {
			return NUM_MONTH_MARCH;
		} else if(month.equals("apr") || month.equals("april")) {
			return NUM_MONTH_APRIL;
		} else if(month.equals("may")) {
			return NUM_MONTH_MAY;
		} else if(month.equals("jun") || month.equals("june")) {
			return NUM_MONTH_JUNE;
		} else if(month.equals("jul") || month.equals("july")) {
			return NUM_MONTH_JULY;
		} else if(month.equals("aug") || month.equals("august")) {
			return NUM_MONTH_AUGUST;
		} else if(month.equals("sep") || month.equals("september")) {
			return NUM_MONTH_SEPTEMBER;
		} else if(month.equals("oct") || month.equals("october")) {
			return NUM_MONTH_OCTOBER;
		} else if(month.equals("nov") || month.equals("november")) {
			return NUM_MONTH_NOVEMBER;
		} else if(month.equals("dec") || month.equals("december")) {
			return NUM_MONTH_DECEMBER;
		}
		
		// asserting false because parameter should be a valid date string
		assert(false);
		return -1;
	}
}
