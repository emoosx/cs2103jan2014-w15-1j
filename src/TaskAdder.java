import java.util.StringTokenizer;
import storage.StorageHelper;

/*
 *TaskAdder class will perform its run method as a drive method
 *It will determine what kind of tasks to add and call on storage to store the data thereafter
 *Passes a task object to storage.java
 *V0.1 - Supports specific keywords and date only
 * 
 *Floating tasks:
 *add repair watch
 *add wash car
 *add go to gardens by the bay
 *add watch the day after tomorrow movie
 *
 *Timed tasks:
 *add meeting on 27-2-2014 from 2pm to 4pm
 *add hackathon from 27-2-2014 2pm to 28-2-2014 3pm
 *
 *Deadline tasks:
 *add submit minutes on 28-2-2014 by 8pm
 */

public class TaskAdder {
	
	
	private final String KEYWORD_BY = "by";
	private final String KEYWORD_TO = "to";
	private final String KEYWORD_FROM = "from";
	private final String KEYWORD_ON = "on";
	private final String KEYWORD_AM = "am";
	private final String KEYWORD_PM = "pm";
	
	private final int NUM_INTEGER_IN_DATE = 3;
	private final int NUM_DAY_INDEX = 0;
	private final int NUM_MONTH_INDEX = 1;
	private final int NUM_YEAR_INDEX = 2;
	private final int NUM_LONGEST_TIME_STRING = 7;
	private final int NUM_SHORTEST_TIME_STRING = 3;
	private final int NUM_TIME_FORMAT_DIGITS_ONLY = 4;
	private final int NUM_TIME_FORMAT_SINGLE_DIGITS = 3;
	private final int NUM_TIME_FORMAT_DOUBLE_DIGITS = 4;
	private final int LENGTH_AM_PM = 2;
	
	private String taskDesc = "";
	private String startHour = null;
	private String startMin = null;
	private String startTimeZone = null;
	private String endHour = null;
	private String endMin = null;
	private String endTimeZone = null;
	private Integer day = null;
	private Integer month = null;
	private Integer year = null;
	
	/*
	 *Public method that will be called by Parser.java
	 *This is just a driver method that will pass the input String into a parser method, determineTask
	 */
	public void run(String inputString) {
		parseTask(inputString);
	}
	
	/*
	 *This method will determine what kind of tasks that the user wants to add
	 *Floating tasks: tasks that only contains task description
	 *Time tasks: tasks that has a start and end time
	 *Deadline tasks: tasks that has an end time
	 */
	private void parseTask(String inputString) {
		StringTokenizer st = new StringTokenizer(inputString);	
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if(isKeyWord(token)) {
				st = parseParameter(token, st);
			} else {
				appendDescription(token);
			}
		}		
		debug();
		addTask();
	}
	
	/*
	 *This method will check if token passed by determineTask method
	 *contains keyword: "on", "by", "from" and "to"
	 *Post condition: Returns true if token is a key word, false otherwise
	 */
	private boolean isKeyWord(String token) {
		token = token.toLowerCase();
		if(token.equals(KEYWORD_ON) ||
			token.equals(KEYWORD_BY) ||
				token.equals(KEYWORD_FROM) ||
					token.equals(KEYWORD_TO))
			return true;
		else
			return false;
	}
	
	//Method will append give token argument into task description
	private void appendDescription(String tokenString) {
		taskDesc += tokenString + " ";
	}
	
	/*
	 *Given specific keyword, this method will check if parameter is suitable.
	 *If parameter is wrong, it is assumed to be part of task description
	 *eg. add put cup "ON" table, add go to garden "BY" the day
	 */
	private StringTokenizer parseParameter(String keyWord, StringTokenizer st) {
		if(keyWord.equals(KEYWORD_ON)) {
			st = parseDate(keyWord, st);
		}
		else {
			st = parseTime(keyWord, st);
		}
		return st;
	}
	
	/*
	 *Given keyword "on", method will try to parse parameter as a date format
	 *Post condition: appends to task description if parameter is not of date format
	 *Post condition: parses day, month and year if it is of date format. 
	 */
	private StringTokenizer parseDate(String keyWord, StringTokenizer st) {
		String parameter = st.nextToken();
		//System.out.println("Parsing:" + keyWord + ", with token: " + parameter);
		// parameter should be a date format
		String[] stringArray = parameter.split("[./-]");
		if(stringArray.length != NUM_INTEGER_IN_DATE) {
			// does not match date format 
			appendDescription(keyWord);
			appendDescription(parameter);
		} else {
			// parsing date format as day, month and time
			try {
				day = Integer.parseInt(stringArray[NUM_DAY_INDEX]);
				month = Integer.parseInt(stringArray[NUM_MONTH_INDEX]);
				year = Integer.parseInt(stringArray[NUM_YEAR_INDEX]);
			} catch (Exception e) { // cannot be parsed as integers
				// 2 things i can do here, either 1. output invalid date format
				// or 2. treat it as task description e.g. asds-asa-das input
				appendDescription(parameter);
			}
		}
		return st;
	}
	
	/*
	 * Given keyword "by", "from" or "to", this method will try to parse next token as a time format token
	 * Acceptable time formats: 2pm, 12:00pm, 1200
	 * Post condition: Next token will update endTime accordingly. 
	 */
	private StringTokenizer parseTime(String keyWord, StringTokenizer st) {
		String hourString = null;
		String minString = null;
		String time = st.nextToken();
		//System.out.println("Parsing:" + keyWord + ", with token: " + time);
		// check if parameter is out of approved range - shortest range: 2pm, longest range: 12:12am
		if(time.length() < NUM_SHORTEST_TIME_STRING || time.length() > NUM_LONGEST_TIME_STRING) {
			System.out.println(time + " appended here. Case 1: Not in approved range");
			appendDescription(time);
			return st;
		} 
		// for cases of 3-4 letter string time format: 2pm, 1am, 12pm, 12am etc.
		if(time.length() == NUM_TIME_FORMAT_DOUBLE_DIGITS || time.length() == NUM_TIME_FORMAT_SINGLE_DIGITS) {
			String lastTwoAlphabets = time.substring(time.length() - LENGTH_AM_PM).toLowerCase();
			// token does not end with "am" or "pm"
			if(!containsAmPm(lastTwoAlphabets)) {
				System.out.println(time + " appended here. Case 2: Time does not contain am or pm");
				appendDescription(time);
				return st;				
			} else {
				try {
					hourString = time.substring(0, time.length() - LENGTH_AM_PM);
					Integer.parseInt(hourString);
				} catch(Exception e) {
					System.out.println(time + " appended here. Case 3: Time ends with am or pm but format is wrong");
					appendDescription(time);
					return st;
				}
			}
		}
		// for cases of 6-7 letter string time format: 2:00pm, 1:15am, 12:00pm etc.
		if(time.length() == 6 || time.length() == 7) {
			String lastTwoAlphabets = time.substring(time.length() - LENGTH_AM_PM).toLowerCase();
			if(!containsAmPm(lastTwoAlphabets)) {
				System.out.println(time + " appended here. Case 4: Time does not contain am or pm");
				appendDescription(time);
				return st;					
			} else {
				String[] timeArray = time.split(":");
				if(timeArray.length != 2) {
					System.out.println(time + " appended here. Case 5: Time contains am or pm but wrong format");
					appendDescription(time);
					return st;
				} else { // format here would be x:xpm or x:xam
					try {
						hourString = timeArray[0];
						minString = timeArray[1].substring(0, timeArray[1].length() - LENGTH_AM_PM);
						Integer.parseInt(hourString);
						Integer.parseInt(minString);
					} catch(Exception e) {
						System.out.println(time + " appended here. Case 6: Time ends with am or pm but format is wrong");
						appendDescription(time);
						return st;						
					}
					
				}
			}
		}
		
		/* 
		 * For next time when we want to handle 4 digit time format
		// check if parameter format is of 4 digits eg. 2359
		if(time.length() == NUM_TIME_FORMAT_DIGITS_ONLY ){
			try{			
			} catch (Exception e) {
				// token is a 4 digit string but not of time format
				appendDescription(time);
				return st;
			}
		}
		*/
		assignTime(keyWord, hourString, minString);
		return st;
	}
	
	private boolean containsAmPm(String string) {
		if(string.equals(KEYWORD_AM) || string.equals(KEYWORD_PM)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void assignTime(String keyWord, String hour, String minute) {
		if(keyWord.equals(KEYWORD_BY) || keyWord.equals(KEYWORD_TO)) {
			endHour = hour; 
			endMin = minute;
			if(endHour != null && endMin == null) {
				endMin = "00";
			}
		}
		else {
			startHour = hour;
			startMin = minute;
			if(endHour != null && endMin == null) {
				startMin = "00";
			}
		}
	}
	// Method that will store task given all parsed attributes
	private void addTask() {
		//StorageHelper storeTask = new StorageHelper();
		//Task task = new Task();
		//storeTask.addNewTask(task);
	}
	
	private void debug() {
		// Debugging purposes
		System.out.println("Task Description: " + taskDesc);
		System.out.println("Start Time: " + startHour + ":" + startMin + startTimeZone);
		System.out.println("End Time: " + endHour + ":" + endMin + endTimeZone);
		System.out.println("Date deadline: " + day + "-" + month + "-" + year);
	}
}
