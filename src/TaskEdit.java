
public class TaskEdit {
	
	void execute(String userInput) {
		if(!checkIfFileIsEmpty()){
			String[] inputArray = userInput.split(" ");
			String taskId = inputArray[NUMBER_COMMAND_INDEX];
			searchForTaskId(taskId);	
		}
	}
	
	// To be coded by Clement
	//search for task using taskname or id
	private boolean searchForTaskId(String task) {
		
		return false;
	}
    
}
