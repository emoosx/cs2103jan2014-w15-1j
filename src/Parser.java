/*
 * Parser parses the user input to determine the type of commands
 * It will then execute the determined command 
 */

public class Parser {
	
	private static final String MESSAGE_INVALID_DELETE = "Usage: delete <number>";
	private static final String MESSAGE_INVALID_NUMBERFORMAT = "Please key in an integer";
	private static final String MESSAGE_INVALID_NUMBERSIGN = "Please key in a positive number";
	private static final String MESSAGE_INVALID_NUMBER = "Please choose a lower value";
	private static final String MESSAGE_DELETED = "deleted : \"%s\"";
	private static final String MESSAGE_EMPTY = "file is empty";
	private final static String MESSAGE_ERROR_INVALID_INPUT = "An error has occured. Please try again.";
	private final static String MESSAGE_ERROR_INVALID_KEYWORD = "Invalid Keyword. Please try again";
	private final static String COMMAND_ADD = "add";
	private final static String COMMAND_DELETE = "delete";
	private final static String COMMAND_EDIT = "edit";
	private final static String COMMAND_LIST = "list";
	private final static String COMMAND_UNDO = "undo";
	
	private static Integer NUMBER_COMMAND_INDEX = 0;
	private static Integer DELETE_PARA = 0;
	private static Integer DELETE_OFFSET = 1;
	
	/*
	 * Method will parse user input to obtain command type
	 * Precondition: userInput is not empty
	 * Postcondition: passes command type and user input into executeCommand method    
	 */
	public void parseUserInput(String userInput) {
		try {
		String[] inputArray = userInput.split(" ");
		String userCommand = inputArray[NUMBER_COMMAND_INDEX];
		// Still not sure what to pass here, array or string
		executeCommand(userCommand, userInput);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Method will determine which execution method to call depending on the userCommand passed in
	 * Post condition: executes method respectively based on user command
	 */
	private void executeCommand(String userCommand, String userInput) {
		switch(userCommand) {
			case(COMMAND_ADD):
				executeAdd(userInput);
				break;
			case(COMMAND_DELETE):
				executeDelete(userInput);
				break;
			case(COMMAND_EDIT):
				executeEdit(userInput);
				break;
			case(COMMAND_LIST):
				executeList(userInput);
				break;
			case(COMMAND_UNDO):
				executeUndo(userInput);
				break;
			default:
				showToUser(MESSAGE_ERROR_INVALID_KEYWORD);
				break;
		}
	}
	
	 /*  
	 * Process of coding.
	 * Note to Htet: This is roughly how I envision it to be. Feel free to change if this practice/method is bad 
	 */
	private void executeAdd(String userInput) {
		TaskAdder taskAdder = new TaskAdder();
		taskAdder.run(userInput);
	}

	/*
	 *  Process of coding.
	 */
	private void executeUndo(String userInput) {
		TaskUndo taskUndo = new TaskUndo();
		taskUndo.run();
	}
	
	// To be coded by Clement
	/* How it works: - search for task id
	  - no task found > return error
	  -delete task object
	  - delete task successful update gson
			  */
		private void executeDelete(String inputNumber) {
			if (checkDeleteInput(inputNumber)) {
				int lineToRemove = Integer.parseInt(inputNumber) - DELETE_OFFSET;
			//	String deletedString = dataList.get(lineToRemove);
			//	dataList.remove(lineToRemove);
			//	showToUser(String.format(MESSAGE_DELETED, deletedString));
			}
		}

		// Method to check delete parameter
		private  boolean checkDeleteInput(String inputNumber) {
			// No argument input
			if (isValidString(inputNumber)) {
				showToUser(MESSAGE_INVALID_DELETE);
				return false;
			}
			/*
			 * Checks if argument fulfill the delete parameters, is a positive non
			 * zero integer and whether the number specified is within the array
			 * size
			 */
			String[] stringArray = inputNumber.split(" ");
			if (stringArray.length != DELETE_PARA) {
				showToUser(MESSAGE_INVALID_DELETE);
				return false;
			} else if (!isPositiveNonZeroInt(inputNumber)) {
				return false;
			} else if (!checkIfNumberBelowArraySize(inputNumber)) {
				return false;
			}
			return true;
		}

	// Need to edit arraylist name
	  private  boolean checkIfNumberBelowArraySize(String n) {
			if (checkIfFileIsEmpty()) {
				showToUser(MESSAGE_EMPTY);
				return false;
			}
			try {
				int num = Integer.parseInt(n) - 1;
			//	if (num >= dataList.size()) {
					showToUser(MESSAGE_INVALID_NUMBER);
					return false;
			//	}
			//	return true;
			} catch (NumberFormatException nfe) {
				showToUser(MESSAGE_INVALID_NUMBERFORMAT);
				return false;
			}
		}
	
	// Method to check for number >0
	private  boolean isPositiveNonZeroInt(String lineNumber) {
		try {
			int i = Integer.parseInt(lineNumber);
			if (i > 0) {
				return true;
			} else {
				showToUser(MESSAGE_INVALID_NUMBERSIGN);
				return false;
			}
		} catch (NumberFormatException nfe) {
			showToUser(MESSAGE_INVALID_NUMBERFORMAT);
			return false;
		}
	}

	// To be coded by Clement
	/* How it works: format: edit task id <function>
	-search for task id
	-id exist perform edit
	-edit details of task object. set to description or set priority to then update to gson
	different functions:
	1) edit description
	2) edit time/priority (sub)
	*/
	private void executeEdit(String userInput) {
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
    
	// Method to check if input is a number
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	// To be coded by Htet
	private void executeList(String userInput) {
		
	}
	
	// Method will print out given argument
	private void showToUser(String outputString) {
		System.out.println(outputString);
	}
	
	// Method checks if data list is empty
	// Arraylist name to be edited accordingly
	private  boolean checkIfFileIsEmpty() {
	//	 if (dataList.isEmpty()) {
				return true;
		//	}
		//	return false;
		}
	
	// Method checks if inputString is not empty
		private boolean isValidString(String inputString) {
			if (inputString.trim().length() == 0) {
				return false;
			}
			return true;
		}
}
