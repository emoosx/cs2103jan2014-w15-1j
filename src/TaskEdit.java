import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import core.Task;

public class TaskEdit {

	/*
	 * Format: edit <taskid/taskname> <
	 */
	private final String KEYWORD_desc1 = "desc";
	private final String KEYWORD_desc2 = "description";
	private final String KEYWORD_name = "name";
	private final String KEYWORD_date = "date";
	private final String KEYWORD_time = "time";

	private static final String MESSAGE_EDIT_FAIL = "Unable to find file to edit";
	private static final String MESSAGE_INVALID_EDIT = "Usage: edit <task name/ID> <desc/name/date/time> <output>";
	
	private static Integer NUMBER_TASK_INDEX = 0;
	private static Integer NUMBER_COMMAND_INDEX = 1;
	private static Integer EDIT_PARA = 3;
	String userInputDesc;
	String commandType;
	String[] inputArray;

// user input should contains 3 para <task name/ID> <edit_type> <output>
	void execute(String userInput) {
		if (!checkIfFileIsEmpty()) {
			if(checkEditParameters(userInput)){
			inputArray = userInput.split(" ");
			String taskId = inputArray[NUMBER_TASK_INDEX];
			commandType = inputArray[NUMBER_COMMAND_INDEX];
			userInputDesc = obtainUserEditInput(userInput);

			if (!searchTask(taskId)) {
				showToUser(MESSAGE_EDIT_FAIL);
			}
			}else{
				showToUser(MESSAGE_INVALID_EDIT);
			}
		}
	}

	private boolean checkEditParameters(String input) {
		if(isValidString(input)){
			String[] stringArray = input.split(" ");
		   if (stringArray.length != EDIT_PARA) {
			return false;
		   }
		} 
		return true;
	}

	// search for task using taskname or id
	// This is done by searching through an arraylist of task (not too sure how to search from json file)
	private boolean searchTask(String task) {
		Task t = new Task();
		for (int i = 0; i < dataList.size(); i++) {
			t = dataList.get(i);
			if (t.getTaskID == task || t.getTaskName == task) {
				performEdit(t);
				return true;
			}
		}
		return false;
	}

	// Method to edit task description/ task name / date / time
	private void performEdit(Task t) {
		
		if((commandType== KEYWORD_desc1) || commandType == KEYWORD_desc2 ){
		// Editing of task description
			t.setTaskDescription(userInputDesc);			
	        } 
		// Editing of task name
		else if (commandType == KEYWORD_name) {
	        	t.setTaskName(userInputDesc);
	        }
		// Editing of date
	        else if(commandType == KEYWORD_date){
	          // t.
	        }
		// Editing of time
	        else if(commandType == KEYWORD_time){
	        //
	        }
		// update arraylist
		updateTaskList();
	}

	// 1)Find task object in dataList and replace it
	// 2)Saves to json file
	//
	private void updateTaskList() {
	

	}

	private static String obtainUserEditInput(String userCommand) {
		userCommand.replace(getFirstWord(userCommand), "").trim();
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	// Method to check if input is a number
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}
	
	// Method checks if inputString is a valid String
		private static boolean isValidString(String inputString) {
			if (inputString.trim().length() == 0) {
				return false;
			}
			return true;
		}

	// Method checks if data list is empty
	// Arraylist name to be edited accordingly
	private boolean checkIfFileIsEmpty() {
		if (dataList.isEmpty()) {
			return true;
		}
		return false;
	}

	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

}
