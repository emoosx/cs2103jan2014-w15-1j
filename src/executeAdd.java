/*
 * executeAdd class will perform its run method as a drive method
 * It will determine what kind of tasks to add and call on storage to store the data thereafter
 */

public class executeAdd {
	
	private final String TASK_TYPE_FLOATING = "float"; 
	private final String TASK_TYPE_TIMED = "timed";
	private final String TASK_TYPE_DEADLINE = "deadline";
	
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
		String taskType = parseTask(inputString);
		switch(taskType) {
			case(TASK_TYPE_FLOATING):
				addFloatingTask(inputString);
				break;
			case(TASK_TYPE_TIMED):
				addTimedTask(inputString);
				break;
			case(TASK_TYPE_DEADLINE):
				addDeadlineTask(inputString);
				break;
			default:
				break;
		}
	}
	
	private String parseTask(String inputString) {
		return inputString;
	}
	
	private void addFloatingTask(String inputString) {
		
	}
	
	private void addTimedTask(String inputString) {
		
	}
	
	private void addDeadlineTask(String inputString) {
		
	}
}
