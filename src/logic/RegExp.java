package logic;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegExp {

	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	private static final int NUM_HOUR_INDEX = 0;
	private static final int NUM_MIN_INDEX = 1;
	private static final int NUM_START_TIME_GROUP = 1;
	private static final int NUM_END_TIME_GROUP = 4;
	
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

    /*
     *  Time Format Expressions for parsing raw text data
     */
    // Case 1: "from TIME to TIME", where TIME is HH:MM(am/pm) or HH(am/pm), all cases are insensitive and HH is between 1-12 and MM is between 0 to 59
    public static String REGEX_TIME_FORMAT_1 = "\\bfrom\\s(([1-9]|1[0-2])(:[0-5][0-9])?[aApP][mM])\\sto\\s(([1-9]|1[0-2])(:[0-5][0-9])?[aApP][mM])\\b";
    // Case 2: "from TIME to TIME", where TIME is HH:MM or HHMM, H is between 0-23 and MM is between 0 to 59
    public static String REGEX_TIME_FORMAT_2 = "\\b";
    // Case 3: "at/due/by TIME", where TIME can be HH:MM(am/pm) or HH(am/pm), all cases are insensitive
    public static String REGEX_TIME_FORMAT_3 = "\\b(at|by)\\s((([1-9]|1[0-2])(:[0-5][0-9])?[aApP][mM])|(([0-1][0-9]|2[0-3]):?[0-5][0-9]))\\b";
    
    /*
     *  Time Format Expressions for parsing raw time data
     */
    // Case 1: HH:MM (am/pm)
    public static String REGEX_TIMESTRING_FORMAT_1 = "([1-9]|1[0-2]):[0-5][0-9][AaPp][Mm]";
    // Case 2: HH (am/pm)
    public static String REGEX_TIMESTRING_FORMAT_2 = "([1-9]|1[0-2])[AaPp][Mm]"; 
    // Case 3: HH:MM
    public static String REGEX_TIMESTRING_FORMAT_3 = "([0-1][0-9]|2[0-3]):?[0-5][0-9]";
    
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
    	Pattern pattern = Pattern.compile(REGEX_TIMESTRING_FORMAT_1);
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
    	pattern = Pattern.compile(REGEX_TIMESTRING_FORMAT_2);
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
    
    // Method will try to match all time regular expressions
    // and parse the raw user input into an array list of time strings and return it
    public static ArrayList<String> parseTime(String userInput) {
    	ArrayList<String> timeArray = new ArrayList<String>();
    	
    	// Case 1: "from TIME to TIME", where TIME can be HH:MM(am/pm) or HH(am/pm)
    	Pattern pattern = Pattern.compile(REGEX_TIME_FORMAT_1);
		Matcher matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			timeArray.add(matcher.group(NUM_START_TIME_GROUP));
			timeArray.add(matcher.group(NUM_END_TIME_GROUP));
			return timeArray;
		}
    	
		// Case 2: "from TIME to TIME", where TIME can be HH:MM
		pattern = Pattern.compile(REGEX_TIME_FORMAT_2);
		matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			//return timeArray;
		}
		
		// Case 3: by/at/due "TIME", where TIME can be HH:MM(am/pm) or HH(am/pm) or HH:MM
		pattern = Pattern.compile(REGEX_TIME_FORMAT_3);
		matcher = pattern.matcher(userInput);
		if(matcher.find()) {
			String timeString = userInput.substring(matcher.start(), matcher.end());
			String[] userInputArray = timeString.split(" ");
			timeArray.add(userInputArray[1]);
			return timeArray;
		}
    	return null; 
    }

    // Method will replace all time and date regular expressions in user input with "" to obtain task description
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
