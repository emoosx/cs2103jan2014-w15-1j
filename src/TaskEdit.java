import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class TaskEdit {

	// Format : edit <task> <-ed / -tn>
	// To be coded by Clement
	/*
	 * How it works: format: edit task id <function> -search for task id -id
	 * exist perform edit -edit details of task object. set to description or
	 * set priority to then update to gson different functions: 1) edit
	 * description 2) edit time/priority (sub)
	 */
	ParamParser para = new ParamParser();
	private static final String MESSAGE_EDIT_FAIL = "Unable to find file to edit";
	private static Integer NUMBER_COMMAND_INDEX = 0;
	String userInput;

	void execute(String userInput) {
		if (!checkIfFileIsEmpty()) {
			String[] inputArray = userInput.split(" ");
			String taskId = inputArray[NUMBER_COMMAND_INDEX];
			userInput = removeFirstWord(userInput);
			if (!searchTask(taskId)) {
				showToUser(MESSAGE_EDIT_FAIL);
			}
		}
	}

	// search for task using taskname or id
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

	private void performEdit(Task t) {

		JCommander cmd = new JCommander();
		// jcommander determines edit type and pass description/name edit to
		// respective method
		// to add if/else once I figured how to use jcom to determine type
		cmd.addCommand("editD", para, "-ed","editdesc");
		cmd.addCommand("editN",para, "-en","editname");
		try {
	        cmd.parse(userInput);
	        //
	        if ("editD".equals(cmd.getParsedCommand())) {
	        	t.setTaskDescription(para.getDesc());
	        } else if ("editN".equals(cmd.getParsedCommand())) {
	        	t.setTaskName(para.getName());
	        } else {
	           cmd.usage();
	        }
	        //
	    } catch (ParameterException ex) {
	        System.out.println(ex.getMessage());
	        cmd.usage();
	    }
		// update arraylist
		updateTaskList();
	}
    
	// 1)Find task object in dataList and replace it
	// 2)Saves to json file
	//
	private void updateTaskList() {
		// TODO Auto-generated method stub

	}

	private static String removeFirstWord(String userCommand) {
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

	// Method checks if data list is empty
	// Arraylist name to be edited accordingly
	private boolean checkIfFileIsEmpty() {
		// if (dataList.isEmpty()) {
		return true;
		// }
		// return false;
	}

	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

}
