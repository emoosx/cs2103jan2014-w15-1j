/*
 * Parser parses the user input to determine the type of commands
 * It will then execute the determined command 
 */

public class Parser {
	

	private final static String MESSAGE_ERROR_INVALID_INPUT = "An error has occured. Please try again.";
	private final static String MESSAGE_ERROR_INVALID_KEYWORD = "Invalid Keyword. Please try again";
	private final static String COMMAND_ADD = "add";
	private final static String COMMAND_DELETE = "delete";
	private final static String COMMAND_EDIT = "edit";
	private final static String COMMAND_LIST = "list";
	private final static String COMMAND_UNDO = "undo";
	
	private static Integer NUMBER_COMMAND_INDEX = 0;

	
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
	
	// To be coded by Htet
	private void executeList(String userInput) {

	}

	// Method will print out given argument
	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

	private void executeEdit(String userInput) {

	}

	private void executeDelete(String userInput) {
     TaskDelete taskDel = new TaskDelete();
     taskDel.execute(userInput);
		
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
	

	
}
