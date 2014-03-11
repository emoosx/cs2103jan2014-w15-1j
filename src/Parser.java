/*
 * Parser parses the user input to determine the type of commands
 * It will then execute the determined command 
 */

public class Parser {
	
	private final static String MESSAGE_WRONG_USAGE = "Invalid command. Type <help> for assistance.";
	private final static String[] MESSAGE_HELP = {"Task Panda v0.1 help manual", "blah blah", "more blah blah"};
	private final static String COMMAND_ADD = "add";
	private final static String COMMAND_DELETE = "delete";
	private final static String COMMAND_EDIT = "edit";
	private final static String COMMAND_LIST = "list";
	private final static String COMMAND_UNDO = "undo";
	private final static String COMMAND_HELP = "help";
	
	private static Integer NUMBER_COMMAND_INDEX = 0;
	private static Integer NUMBER_USER_INPUT_WITHOUT_COMMAND = 1;

	
	/*
	 * Method will parse user input to obtain command type
	 * Precondition: userInput is not empty
	 * Postcondition: passes command type and user input into executeCommand method    
	 */
	public void parseUserInput(String userInput) {
		if(isInvalidString(userInput)) {
			showToUser(MESSAGE_WRONG_USAGE);
			return;
		}
		String userCommand = getCommand(userInput);
		executeCommand(userCommand, userInput);
	}
	
	// Method will execute different methods according to the user's command
	private void executeCommand(String userCommand, String userInput) {
		switch(userCommand) {
			case(COMMAND_ADD):
				executeAdd(removeFirstWord(userInput));
				break;
			case(COMMAND_DELETE):
				executeDelete(removeFirstWord(userInput));
				break;
			case(COMMAND_EDIT):
				executeEdit(removeFirstWord(userInput));
				break;
			case(COMMAND_LIST):
				executeList(removeFirstWord(userInput));
				break;
			case(COMMAND_UNDO):
				executeUndo();
				break;
			case(COMMAND_HELP):
				displayHelp();
				break;
			default:
				showToUser(MESSAGE_WRONG_USAGE);
				break;
		}
	}
	
	// To be coded by Htet
	private void executeList(String userInput) {

	}

	// Method will print out the given argument
	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

	// This methods edits a task given by the user
	private void executeEdit(String userInput) {
	    TaskEdit taskEdit = new TaskEdit();
	    taskEdit.execute(userInput);
	}

	// This methods deletes a task given by the user
	private void executeDelete(String userInput) {
	     TaskDelete taskDel = new TaskDelete();
	     taskDel.execute(userInput);
	}

	// This methods adds a task given by the user
	private void executeAdd(String userInput) {
		TaskAdder taskAdder = new TaskAdder();
		taskAdder.run(userInput);
	}

	// This methods undo the last operation performed by the user
	private void executeUndo() {
		TaskUndo taskUndo = new TaskUndo();
		taskUndo.undoAction();
	}
	
	// This method removes the command word of user input and returns the remaining string
	private String removeFirstWord(String userInput) {
		String[] stringArray = userInput.split(" ", 2);
		return stringArray[NUMBER_USER_INPUT_WITHOUT_COMMAND];
	}
	
	
	// This method returns the command word from the user input given
	private String getCommand(String userInput) {
		String[] inputArray = userInput.split(" ");
		return inputArray[NUMBER_COMMAND_INDEX];
	}
	
	// This method displays the help manual on "help" user command
	private void displayHelp() {
		for(int i=0; i<MESSAGE_HELP.length; i++) {
			showToUser(MESSAGE_HELP[i]);
		}
	}
	
	// This method checks for empty strings
	private boolean isInvalidString(String userInput) {
		userInput = userInput.trim();
		if(userInput.length() == 0 || userInput == null) {
			return true;
		}
		return false;
	}
}
