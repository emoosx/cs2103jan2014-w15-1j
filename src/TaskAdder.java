import java.util.StringTokenizer;

/*
 * executeAdd class will perform its run method as a drive method
 * It will determine what kind of tasks to add and call on storage to store the data thereafter
 * Passes a task object to storage.java
 * V0.1 - Supports specific keywords and date only
 * 
 * Floating tasks:
 * add repair watch
 * add wash car
 * add go to gardens by the bay
 * 
 * Timed tasks:
 * add meeting on 27-2-2014 from 2pm to 4pm
 * 
 * Deadline tasks:
 * add submit minutes on 28-2-2014 by 8pm
 */

public class TaskAdder {
	
	private final String KEYWORD_BY = " by ";
	private final String KEYWORD_TO = "to";
	private final String KEYWORD_FROM = "from";
	private final String KEYWARD_ON = "on";
	
	private String taskDescription;
	private int startTime;
	private int endTime;
	//private Date date;
	
	/*
	 * Public method that will be called by Parser.java
	 * This is just a driver method that will pass the input String into a parser method
	 */
	public void run(String inputString) {
		determineTask(inputString);
	}
	
	/*
	 * This method will determine what kind of tasks that the user wants to add
	 * Floating tasks: tasks that only contains task description
	 * Time tasks: tasks that has a start and end time
	 * Deadline tasks: tasks that has an end time
	 */
	private void determineTask(String inputString) {
		StringTokenizer st = new StringTokenizer(inputString);
		String taskDesc = "";
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if(hasKeyword(token)) {
				
			} else {
				taskDesc += token;
			}
		}	
	}
	
	/*
	 * This method will check if token passed by determineTask method
	 * contains keyword: by, to, from, on
	 */
	private boolean hasKeyword(String token) {
		return false;
	}
}
