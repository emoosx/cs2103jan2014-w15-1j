package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import logic.Command.COMMAND_TYPE;
import storage.StorageHelper;
import core.Task;



public class CommandFactory {
	
	//EDIT//
	private static final String MESSAGE_INVALID_EDIT = "Usage: edit <index> <description> on <date> from <start time> to <end time>";
	private static final String MESSAGE_INVALID_NUMBERFORMAT = "Please key in an integer";
	private static final String MESSAGE_INVALID_NUMBERSIGN = "Please key in a positive number";
	private static final String MESSAGE_EMPTY = "file is empty";

	private static Integer NUMBER_TASK_INDEX = 0;
	private static Integer EDIT_PARA = 8;
	private static Integer EDIT_OFFSET = 1;

	private static String FEEDBACK;
	String userInputDesc;
	String commandType;
	String[] inputArray;
	
	//DELETE//
	private static final String MESSAGE_INVALID_DELETE = "Usage: delete <number>";
	private static final String MESSAGE_INVALID_NUMBER = "Please choose another value";
	private static final String MESSAGE_DELETED = "deleted : \"%s\"";
	
	private static Integer DELETE_PARA = 1;
	private static Integer DELETE_OFFSET = 1;
	
	public static CommandFactory INSTANCE = new CommandFactory();
	public final String UNDO_ADD = "add";
	public final String UNDO_EDIT = "edit";
	public final String UNDO_DONE = "done";
	public final String UNDO_ARCHIVE = "archive";
	public final String UNDO_DONEALL = "doneall";
	public final String UNDO_ARCHIVEALL = "archiveall";

	private List<Task> tasks;
	private StorageHelper storage;
	
	private Stack<Map<Integer, List<String>>> undoStack;
	
	private CommandFactory() {
		tasks = new ArrayList<Task>();
		this.storage = StorageHelper.INSTANCE;
		this.fetch();
	}
	
	// Method will fill task list and undo history from storage
	private void fetch() {
		
	}
	
	public List<Task> getTasks() {
		return this.tasks;
	}
	
	
	public void undo() {
		if(undoStack.size() == 0) {
			return;
		}
		
		Map<Integer, List<String>> undoItem = undoStack.pop();
		
		// get the command string from undoItem
		String commandString = null;
		this.doUndoAction(commandString);
		
		// remove that entry record from the storage, pass the integerID
		this.removeRecord(1);
	}
	
	

	private void doUndoAction(String cmd) {
		switch(cmd) {
		case UNDO_ADD: break;
		case UNDO_EDIT: break;
		case UNDO_DONE: break;
		case UNDO_ARCHIVE: break;
		case UNDO_DONEALL: break;
		case UNDO_ARCHIVEALL: break;
		}
		
		// remove that entry record from the storage
	}
	
	private void removeRecord(int id) {
		
	}
	
	private void pushUndo(String cmd, Task task, List<Task> tasks) {
		
		List<String> item = new ArrayList<String>();
		item.add(cmd);
		
		switch(cmd) {
		case UNDO_ADD: break;
		case UNDO_EDIT: break;
		case UNDO_DONE: break;
		case UNDO_ARCHIVE: break;
		case UNDO_DONEALL: break;
		case UNDO_ARCHIVEALL: break;
		}
		
		this.saveUndo(item);
	}
	

	public void writeToJson() {
		StorageHelper.INSTANCE.clearFile();
		storage.clearFile();
		for (int i = 0; i < tasks.size(); i++) {
			storage.addNewTask(tasks.get(i));
		}
	}

	
	/*
	 * insert a new row into the undo stack of task collection
	 */
	private void saveUndo(List<String> item) {
		
	}
	
	public void process(Command command) {
		executeCommand(command.command, command.rawText);
		
		// call undo here
	}
	
	public void executeCommand(COMMAND_TYPE command, String rawText) {
		assert(rawText != null);
		switch(command) {
		case ADD:
			doAdd(rawText);
			break;
		case LIST:
			doList(rawText);
			break;
		case EDIT:
			doEdit(rawText);
			break;
		case UNDO:
			break;
		case ARCHIVE:
			break;
		case CLEAR:
			break;
		case DONEALL:
			break;
		case ARCHIVEALL:
			break;
		case DELETE:
			doDelete(rawText);
			break;
		case HELP:
			break;
		default:
			break;
		}
	}
	
	private void doAdd(String rawText) {
		Task newTask = new Task(rawText);
		tasks.add(newTask);
		writeToJson();
		showToUser(newTask.getTaskDescription() + " added.");
	}
	
	private void doList(String rawText) {
		assert(rawText != null);
		List<Task> tasks = this.storage.getAllTasks();
		for(Task t : tasks) {
			System.out.println(t);
		}	
	}
	
	private void doEdit(String userInput){
		if (checkEditIndexInput(userInput)) {
			int taskInt = (Integer.parseInt(getFirstWord(userInput)) - EDIT_OFFSET);
			Task editTask = new Task(obtainUserEditInput(userInput));
			tasks.set(taskInt, editTask);
			writeToJson();
		}
	}
	
 private void doDelete(String inputNumber){
	 if (checkDeleteInput(inputNumber)) {
			int lineToRemove = Integer.parseInt(inputNumber) - DELETE_OFFSET;
			 String deletedString = tasks.get(lineToRemove).getTaskDescription();
			 tasks.remove(lineToRemove);
			 showToUser(String.format(MESSAGE_DELETED, deletedString));
		     writeToJson();
		}
 }
		
		// Method to check delete parameter
		private boolean checkDeleteInput(String inputNumber) {
			// No argument input
			if (!isValidString(inputNumber)) {
				showToUser(MESSAGE_INVALID_DELETE);
				FEEDBACK = MESSAGE_INVALID_DELETE;
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
				FEEDBACK = MESSAGE_INVALID_DELETE;
				return false;
			} else if (!isPositiveNonZeroInt(inputNumber)) {
				return false;
			} else if (!checkIfNumberBelowArraySize(inputNumber)) {
				return false;
			}
			return true;
		}
		
	
	private boolean checkEditIndexInput(String inputNumber) {
		// No argument input
		if (!isValidString(inputNumber)) {
			showToUser("here valid string");
			showToUser(MESSAGE_INVALID_EDIT);
			FEEDBACK = MESSAGE_INVALID_EDIT;
			return false;
		}
		/*
		 * Checks if argument fulfill the delete parameters, is a positive non
		 * zero integer and whether the number specified is within the array
		 * size
		 */
		String[] stringArray = inputNumber.split(" ");
		String taskIndex = stringArray[NUMBER_TASK_INDEX];
		if (stringArray.length != EDIT_PARA) {
			System.out.println((stringArray.length));
			showToUser(MESSAGE_INVALID_EDIT);
			FEEDBACK = MESSAGE_INVALID_EDIT;
			return false;
		} else if (!isPositiveNonZeroInt(taskIndex)) {
			return false;
		} else if (!checkIfNumberBelowArraySize(taskIndex)) {
			return false;
		}
		return true;
	}

	private boolean checkIfNumberBelowArraySize(String n) {
		if (checkIfFileIsEmpty()) {
			showToUser(MESSAGE_EMPTY);
			FEEDBACK = MESSAGE_EMPTY;
			return false;
		}
		try {
			int num = Integer.parseInt(n) - 1;
			if (num >= tasks.size()) {
				showToUser(MESSAGE_INVALID_NUMBER);
				FEEDBACK = MESSAGE_INVALID_NUMBER;
				return false;
			}
			return true;
		} catch (NumberFormatException nfe) {
			showToUser(MESSAGE_INVALID_NUMBERFORMAT);
			FEEDBACK = MESSAGE_INVALID_NUMBERFORMAT;
			return false;
		}
	}

	// Method to check for number >0
	private boolean isPositiveNonZeroInt(String indexNumber) {
		try {
			int i = Integer.parseInt(indexNumber);
			if (i > 0) {
				return true;
			} else {
				showToUser(MESSAGE_INVALID_NUMBERSIGN);
				FEEDBACK = MESSAGE_INVALID_NUMBERSIGN;
				return false;
			}
		} catch (NumberFormatException nfe) {
			showToUser(MESSAGE_INVALID_NUMBERFORMAT);
			FEEDBACK = MESSAGE_INVALID_NUMBERFORMAT;
			return false;
		}
	}

	//remove task index from usercommand and return edit input
	private String obtainUserEditInput(String userCommand) {
		StringBuilder sb = new StringBuilder();
		String[] stringArray = userCommand.split(" ");
		for(int i=1;i<(stringArray.length); i++){
			sb.append(stringArray[i]);
			sb.append(" ");
		}
		showToUser(sb.toString());
		return sb.toString();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	// Method checks if inputString is a valid String
	private static boolean isValidString(String inputString) {
		if (inputString.trim().length() == 0) {
			return false;
		}
		return true;
	}

	
	// Method checks if data list is empty
	private boolean checkIfFileIsEmpty() {
		if (tasks.isEmpty()) {
			return true;
		}
		return false;
	}


	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

	public String testEdit(String userInput){
		tasks.clear();
		Task task1 = new Task("meeting1 on 27-2-2014 from 1pm to 2pm");
		tasks.add(task1);
		if (checkEditIndexInput(userInput)) {
			int taskInt = (Integer.parseInt(getFirstWord(userInput)) - EDIT_OFFSET);
			Task editTask = new Task(obtainUserEditInput(userInput));
			tasks.set(taskInt, editTask);
			writeToJson();
		StringBuilder sb = new StringBuilder();
		sb.append(tasks.get(0).getTaskDescription());
		sb.append(tasks.get(0).getTaskStartTime().getHourOfDay());
		sb.append(tasks.get(0).getTaskEndTime().getHourOfDay());
		return sb.toString();
		}
		else{
			return FEEDBACK;
		}	
	}
	
	public String testDelete(String inputNumber){
		tasks.clear();
		Task task1 = new Task("meeting1 on 27-2-2014 from 1pm to 2pm");
		Task task2 = new Task("meeting2 on 27-2-2014 from 2pm to 3pm");
		Task task3 = new Task("meeting3 on 27-2-2014 from 3pm to 4pm");
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		 if (checkDeleteInput(inputNumber)) {
				int lineToRemove = Integer.parseInt(inputNumber) - DELETE_OFFSET;
				 String deletedString = tasks.get(lineToRemove).getTaskDescription();
				 tasks.remove(lineToRemove);
				 showToUser(String.format(MESSAGE_DELETED, deletedString));
			     writeToJson();
			 StringBuilder sb = new StringBuilder();
				for(int i=0; i<tasks.size();i++){
				sb.append(tasks.get(i).getTaskDescription());
				}
				return sb.toString();
		}else{
			return FEEDBACK;
		}
		
	}
}
