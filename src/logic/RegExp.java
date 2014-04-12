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

/*
 * RegExp class contains regular expression patterns of supported time and date formats
 * It is called by TaskParser.java to extract time and date values from input strings
 * RegExp will match and extract matched date and time patterns to return to TaskParser in string forms (e.g. "next Thursday")
 * Written by A0101810A - Tan Zheng Jie (Matthew)
 */

public class RegExp {

	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	
	private static final int INDEX_FIRST_CASE = 0;
	private static final int INDEX_SECOND_CASE = 1;
	private static final int INDEX_THIRD_CASE = 2;
	private static final int INDEX_FOURTH_CASE = 3;

	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	private static final int NUM_DAY_INDEX = 0;
	private static final int NUM_MONTH_INDEX = 1;
	private static final int NUM_YEAR_INDEX = 2;
	
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
	private static final int NUM_MONTH_INVALID = -1;
	
	private static final String STRING_JAN = "jan";
	private static final String STRING_JANUARY = "january";
	private static final String STRING_FEB = "feb";
	private static final String STRING_FEBRUARY = "february";
	private static final String STRING_MAR = "mar";
	private static final String STRING_MARCH = "march";
	private static final String STRING_APR = "apr";
	private static final String STRING_APRIL = "april";
	private static final String STRING_MAY = "may";
	private static final String STRING_JUN = "jun";
	private static final String STRING_JUNE = "june";
	private static final String STRING_JUL = "jul";
	private static final String STRING_JULY = "july";
	private static final String STRING_AUG = "aug";
	private static final String STRING_AUGUST = "august";
	private static final String STRING_SEP = "sep";
	private static final String STRING_SEPTEMBER = "september";
	private static final String STRING_OCT = "oct";
	private static final String STRING_OCTOBER = "october";
	private static final String STRING_NOV = "nov";
	private static final String STRING_NOVEMBER = "november";
	private static final String STRING_DEC = "dec";
	private static final String STRING_DECEMBER = "december";
	
    /*
     *  Date Input Expressions
     *  These patterns contains keywords and date formats (on 12 mar 2014)
     *  These patterns are used to compare with user's raw input to extract pure date formats (eg. 12 mar 2014)
     */
    public static String[] regexDateInputArray = {
    	// Case 1: DD-MM-YY(YY) or DD/MM/YY(YY) 
    	"\\b(?i)(on\\s((0?[1-9]|[12]\\d|3[01])[-/]((0?[13578])|1[02])[-/](\\d{4}|\\d{2})|(0?[1-9]|[12]\\d|30)[-/]((0?[1-9])|1[012])[-/](\\d{4}|\\d{2})))\\b",
    	// Case 2: partial text based dates (e.g. 15 march 2014, 2 feb)
    	"\\b(?i)(on\\s)(((0?[1-9]|[12]\\d|3[01])\\s(jan(uary)?|mar(ch)?|may|jul(y)?|aug(ust)?|oct(ober)?|dec(ember)?)(\\s\\d{4})?|(0?[1-9]|[12]\\d|30)\\s(jan(uary)?|feb(ruary)?|mar(ch)?|apr(il)?|may|jun(e)?|jul(y)?|aug(ust)?|sep(tember)?|oct(ober)?|nov(ember)?|dec(ember)?)(\\s\\d{4})?))\\b",
    	// Case 3: pure text based relative dates (e.g. next Monday)
    	"\\b(?i)((((on|by)\\s)((next|this)\\s)?((mon(day)?|tues(day)?|wed(nesday)?|thurs(day)?|fri(day)?|sat(urday)?|sun(day)?)))|((on|at|by)\\s(the day after )?tomorrow))\\b"
    	};
    
    /*
     *  Time Input Expressions
     *  These patterns contains keywords and time formats (eg. by 2:15pm)
     *  These patterns are used to compare with user's raw input to extract pure time formats (eg. 2:15pm)
     */
    public static String[] regexTimeInputArray = {
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
     *  Date Expressions
     *  These patterns contains purely date formats, without any keywords such as "from", "by", etc.
     *  These patterns are used to identify and parse date formats into an integer array of 3 elements: year, month and day
     */
    private static String[] regexDateArray = {
    	// Case 1: DD/MM/YY(YY) or DD-MM-YY(YY) or  
        "((0?[1-9]|[12]\\d|3[01])[-/](0?[13578]|1[02])[-/](\\d{4}|\\d{2})|(0?[1-9]|[12]\\d|30)[-/](0?[1-9]|1[012])[-/](\\d{4}|\\d{2}))",
        // Case 2: Partial text based dates (e.g. 15 march 2014, 2 feb)
        "\\b(?i)(((0?[1-9]|[12]\\\\d|3[01])\\s(jan|january|mar|march|may|jul|july|aug|august|oct|october|dec|december)(\\s\\d{4})?|(0?[1-9]|[12]\\d|30)\\s(jan|january|feb|february|mar|march|apr|april|may|jun|june|jul|july|aug|august|sep|september|oct|october|nov|november|dec|december)(\\s\\d{4})?))\\b",
        // Case 3: pure text based relative dates (e.g. next Monday)
        "\\b(?i)((((on|by)\\s)((next|this)\\s)?((mon(day)?|tues(day)?|wed(nesday)?|thurs(day)?|fri(day)?|sat(urday)?|sun(day)?)))|((on the day after )?tomorrow))\\b"
    };
    
    /* 
     * Hybrid patterns that captures date and time under one keyword such as "from, by, to", etc.
     * These patterns are used twice, once by parseTime and once by parseDate  
     */
    // Case 1: (by/on/from/to/at) <date> <time>
    public static String REGEX_HYBRID_PATTERN_1 = "\\b(by |on |from |to |at )(((0?[1-9]|[12]\\d|3[01])[-/](0?[13578]|1[02])[-/](\\d{4}|\\d{2})|(0?[1-9]|[12]\\d|30)[-/](0?[1-9]|1[012])[-/](\\d{4}|\\d{2}))|(((0?[1-9]|[12]\\d|3[01])\\s(jan(uary)?|mar(ch)?|may|jul(y)?|aug(ust)?|oct(ober)?|dec(ember)?)(\\s\\d{4})?|(0?[1-9]|[12]\\d|30)\\s(jan(uary)?|feb(ruary)?|mar(ch)?|apr(il)?|may|jun(e)?|jul(y)?|aug(ust)?|sep(tember)?|oct(ober)?|nov(ember)?|dec(ember)?)(\\s\\d{4})?)))(\\s(([1-9]|1[0-2])(:[0-5][0-9])?[AaPp][Mm]|([0-1][0-9]|2[0-3]):?[0-5][0-9]))?\\b";
    // Case 2: (by/on/from/to/at) <relative date> <time>
    public static String REGEX_HYBRID_PATTERN_2 = "\\b(?i)(by |on |from |to |at )(((next|this)?\\s)?((mon(day)?|tues(day)?|wed(nesday)?|thurs(day)?|fri(day)?|sat(urday)?|sun(day)?))|(the day after )?tomorrow)(\\s(([1-9]|1[0-2])(:[0-5][0-9])?[AaPp][Mm]|([0-1][0-9]|2[0-3]):?[0-5][0-9]))?\\b";
    
    /*
     *  Hash Tag Regular Expressions
     */
    public static String REGEX_HASHTAG = "(?<=^|(?<=[^a-zA-Z0-9-\\.]))#([A-Za-z]+[A-Za-z0-9]+)";
    
    /*
     *  Given a time string such as 5:15pm,
     *  Method parses the string accordingly using NattyTime library 
     *  @return integer array of 3 elements: year, month and day
     */
    /*
    public static int[] dateFromDateString(String dateString) {
    	int[] date = new int[TOTAL_DATE_FIELDS];
    	
    	// Overcoming NattyTime limitation (Natty parses dates as MM/DD/YYYY) 
		dateString = changeDateFormat(dateString);
		
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
    */
    
    /* Given a string of time format (eg. "5pm"),
     * Method will parse the string accordingly using NattyTime
     * @return integer array with 2 elements: hour and minute 
     */
    /*
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
    */
    
    /*
     *  Method parses raw input into an array list of date strings
     *  It will match user input with all supported date patterns 
     *  @return an ArrayList<String> of dates 
     */
    public static ArrayList<String> parseDate(String userInput) {
    	ArrayList<String> dateArray = new ArrayList<String>();

    	// Case 1: "on DD-MM-YY(YY) or DD/MM/YY(YY)"
    	Pattern pattern = Pattern.compile(regexDateInputArray[INDEX_FIRST_CASE]);
    	Matcher matcher = pattern.matcher(userInput);
    	if(matcher.find()) {
    		dateArray.add(matcher.group(2));
    		return dateArray;
    	}
    	
    	// Case 4: in the form of hybrid input (eg. from <date> <time> to <date> <time>) 
    	pattern = Pattern.compile(REGEX_HYBRID_PATTERN_1);
    	matcher = pattern.matcher(userInput);
    	while(matcher.find()) {
    		if(matcher.group(2) != null) {
    			dateArray.add(matcher.group(2));
    		}
    	}
    	if(dateArray.size() > 0) {
    		return dateArray;
    	}
    	
    	// Case 5: in the form of relative date hybrid input (eg. from next wed 12pm to next thurs 1pm) 
    	pattern = Pattern.compile(REGEX_HYBRID_PATTERN_2);
    	matcher = pattern.matcher(userInput);   	
    	while(matcher.find()) {
    		if(matcher.group(2) != null) {
    			dateArray.add(matcher.group(2));
    		}
    	}
    	if(dateArray.size() > 0) {
    		return dateArray;
    	}
    	
    	// Case 2: Partial text based date format (e.g. 12 march 14, 20 aug)
    	pattern = Pattern.compile(regexDateInputArray[INDEX_SECOND_CASE]);
    	matcher = pattern.matcher(userInput);
    	if(matcher.find()) {
    		dateArray.add(matcher.group(2));
    		return dateArray;
    	}
    	
    	// Case 3: pure text based relative dates (e.g. next Monday)
    	pattern = Pattern.compile(regexDateInputArray[INDEX_THIRD_CASE]);
    	matcher = pattern.matcher(userInput);
    	if(matcher.find()) {
    		dateArray.add(matcher.group(2));
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
    	Pattern pattern = Pattern.compile(regexTimeInputArray[INDEX_FIRST_CASE]);
		Matcher matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			timeArray.add(matcher.group(NUM_START_TIME_GROUP));
			timeArray.add(matcher.group(NUM_END_TIME_GROUP));
			return timeArray;
		}
    	
		// Case 2: "from TIME to TIME", where TIME can be HH:MM or HHMM
		pattern = Pattern.compile(regexTimeInputArray[INDEX_SECOND_CASE]);
		matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			timeArray.add(matcher.group(NUM_START_TIME_GROUP));
			timeArray.add(matcher.group(NUM_END_TIME_GROUP));
			return timeArray;
		}
		
		// Case 3: by/at/due "TIME", where TIME can be HH:MM(am/pm) or HH(am/pm) or HH:MM
		pattern = Pattern.compile(regexTimeInputArray[INDEX_THIRD_CASE]);
		matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			String timeString = userInput.substring(matcher.start(), matcher.end());
			String[] userInputArray = timeString.split(" ");
			timeArray.add(userInputArray[1]);
			return timeArray;
		}
		
		// Case 4: in the form of hybrid input (eg. from <date> <time> to <date> <time>, on <date> <time>, by <date> <time>) 
		pattern = Pattern.compile(REGEX_HYBRID_PATTERN_1);
		matcher = pattern.matcher(userInput);
		while(matcher.find()) {
			// As pattern contains both time and date, this condition does not add time if date is specified but time is not
			if(matcher.group(36)!=null) {
				timeArray.add(matcher.group(36));
			}
		}
    	if(timeArray.size() > 0) {
    		return timeArray;
    	}
    	
    	// Case 5: in the form of relative date hybrid input (eg. from next wed 12pm to next thurs 1pm) 
    	pattern = Pattern.compile(REGEX_HYBRID_PATTERN_2);
    	matcher = pattern.matcher(userInput);   	
    	while(matcher.find()) {
    		if(matcher.group(15) != null) {
    			timeArray.add(matcher.group(15));
    		}
    	}
		// Returning empty time array indicates that user input produces 0 time parameter
    	return timeArray; 
    }

    /*
     *  Method will remove all time and date patterns to obtain task description
     *  @returns task description:String
     */
	public static String parseDescription(String taskDescription) {
		// Replacing all hybrid date and time regex with ""
		taskDescription = taskDescription.replaceAll(REGEX_HYBRID_PATTERN_1, "");
		taskDescription = taskDescription.replaceAll(REGEX_HYBRID_PATTERN_2, "");
		// Replacing time regex patterns with ""
		for(int i=0; i<regexTimeInputArray.length; i++) {
			taskDescription = taskDescription.replaceAll(regexTimeInputArray[i], "");
		}
    	// Replacing date regex patterns with ""
		for(int i=0; i<regexDateInputArray.length; i++) {
			taskDescription = taskDescription.replaceAll(regexDateInputArray[i], "");
    	}
		// Replacing hashtags regex patterns with ""
		taskDescription = taskDescription.replaceAll(REGEX_HASHTAG, "");
		
		PandaLogger.getLogger().info("Task Description obtained: " + taskDescription);
		return taskDescription.trim();
	}
	
	/*
	 *  Method will attempt to parse hashtags given user input
	 *  @return a list of hashtags or empty list if no hashtag exists
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
		Pattern pattern = Pattern.compile(regexDateArray[0]);
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
	 * @return integer corresponding to month
	 */
	public static int getMonthIndex(String month) {
		month = month.toLowerCase();
		if(month.equals(STRING_JAN) || month.equals(STRING_JANUARY)) {
			return NUM_MONTH_JANUARY;
		} else if(month.equals(STRING_FEB) || month.equals(STRING_FEBRUARY)) {
			return NUM_MONTH_FEBRUARY;
		} else if(month.equals(STRING_MAR) || month.equals(STRING_MARCH)) {
			return NUM_MONTH_MARCH;
		} else if(month.equals(STRING_APR) || month.equals(STRING_APRIL)) {
			return NUM_MONTH_APRIL;
		} else if(month.equals(STRING_MAY)) {
			return NUM_MONTH_MAY;
		} else if(month.equals(STRING_JUN) || month.equals(STRING_JUNE)) {
			return NUM_MONTH_JUNE;
		} else if(month.equals(STRING_JUL) || month.equals(STRING_JULY)) {
			return NUM_MONTH_JULY;
		} else if(month.equals(STRING_AUG) || month.equals(STRING_AUGUST)) {
			return NUM_MONTH_AUGUST;
		} else if(month.equals(STRING_SEP) || month.equals(STRING_SEPTEMBER)) {
			return NUM_MONTH_SEPTEMBER;
		} else if(month.equals(STRING_OCT) || month.equals(STRING_OCTOBER)) {
			return NUM_MONTH_OCTOBER;
		} else if(month.equals(STRING_NOV) || month.equals(STRING_NOVEMBER)) {
			return NUM_MONTH_NOVEMBER;
		} else if(month.equals(STRING_DEC) || month.equals(STRING_DECEMBER)) {
			return NUM_MONTH_DECEMBER;
		}
		
		// asserting false because parameter should be a valid date string
		assert(false);
		return NUM_MONTH_INVALID;
	}
}
