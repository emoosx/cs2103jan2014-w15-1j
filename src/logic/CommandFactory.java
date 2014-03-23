package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Logger;

import storage.StorageHelper;

import common.PandaLogger;

import core.Task;

public class CommandFactory {

	// EDIT//
	private static final String MESSAGE_INVALID_EDIT = "Usage: edit <index> <description> on <date> from <start time> to <end time>";
	private static final String MESSAGE_INVALID_NUMBERFORMAT = "Please key in an integer";
	private static final String MESSAGE_INVALID_NUMBERSIGN = "Please key in a positive number";
	private static final String MESSAGE_EMPTY = "file is empty";

	private static Integer NUMBER_TASK_INDEX = 0;
	private static Integer EDIT_OFFSET = 1;

	private static String FEEDBACK;
	String userInputDesc;
	String commandType;
	String[] inputArray;

	// DELETE//
	private static final String MESSAGE_INVALID_DELETE = "Usage: delete <number>";
	private static final String MESSAGE_INVALID_NUMBER = "Please choose another value";
	private static final String MESSAGE_DELETED = "deleted : \"%s\"";

	private static Integer DELETE_PARA = 1;
	private static Integer DELETE_OFFSET = 1;

	private static CommandFactory INSTANCE ;
	public final String UNDO_ADD = "add";
	public final String UNDO_EDIT = "edit";
	public final String UNDO_DONE = "done";
	public final String UNDO_ARCHIVE = "archive";
	public final String UNDO_DONEALL = "doneall";
	public final String UNDO_ARCHIVEALL = "archiveall";

	private List<Task> tasks;
	private Map<Task, Integer> tasksMap;
	private List<Task> filteredTasks;
	private StorageHelper storage;
	private Logger logger = PandaLogger.getLogger();
	private int lastElementID = 0;

	private Stack<Command> undoStack;

	private CommandFactory() {
		this.tasks = new ArrayList<Task>();
		this.undoStack = new Stack<Command>();
		this.storage = StorageHelper.INSTANCE;
		this.fetch(); // Make sure this comes after Storage singleton
						// initialization
	}
	
	public static CommandFactory getInstance() {
		if(INSTANCE == null) {
			return new CommandFactory();
		}
		return INSTANCE;
	}

	// Method will fill task list from storage
	private void fetch() {
		List<Task> allTasks = this.storage.getAllTasks();
		tasksMap = new LinkedHashMap<Task, Integer>(allTasks.size());
		for(int i = 0; i < allTasks.size(); i++) {
			this.tasksMap.put(allTasks.get(i), i);
		}
		this.filteredTasks = TaskLister.getAllUndeletedTasks(tasksMap);
		this.populateUndoStack();
	}

	// initialize and populate undoStack
	private void populateUndoStack() {
		this.undoStack = new Stack<Command>();
		// this.undoStack = this.undoStorage.getAllCommands();
	}

	public List<Task> getTasks() {
		return this.filteredTasks;

	}

	public void process(Command command) {
		executeCommand(command);
	}

	public void executeCommand(Command command) {
		assert (command.rawText != null);
		switch (command.command) {
		case ADD:
			doAdd(command.rawText);
			this.undoStack.push(command);
			break;
		case LIST:
			doList(command.rawText);
			break;
		case EDIT:
			doEdit(command.rawText);
			this.undoStack.push(command);
			break;
		case UNDO:
			doUndo();
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
			doDelete(command.rawText);
			this.undoStack.push(command);
			break;
		case HELP:
			break;
		default:
			break;
		}
	}

	private void doUndo() {
		logger.info("doUndo");
		Command lastCommand = this.undoStack.pop();
		logger.info(lastCommand.toString());
		executeUndo(lastCommand);
		syncTasks(tasks);
	}

	private void executeUndo(Command command) {
		assert (command.rawText == null);
		switch (command.command) {
		case ADD:
			doDelete(String.valueOf(this.tasks.size()));
			break;
		case EDIT:
			break;
		case DELETE:
			break;
		default:
			return;
		}
	}

	private void doAdd(String rawText) {
		assert (rawText != null);
		Task newTask = new Task(rawText);
		this.tasksMap.put(newTask, this.tasksMap.size());
		syncTasks();
	}

	private void doList(String rawText) {
		assert (rawText != null);
		logger.info("doList");
		this.filteredTasks = TaskLister.getAllUndeletedTasks(this.tasksMap);
		
	}

	/* remove the original task from tasksMap and replace it with new task */
	private void doEdit(String userInput) {
		assert (userInput != null);
		this.logger.info("doEdit:" + userInput);
		if (checkEditIndexInput(userInput)) {
			int taskInt = (Integer.parseInt(getFirstWord(userInput)) - EDIT_OFFSET);
			Task originalTask = getTaskById(taskInt);
			this.tasksMap.remove(originalTask);
			Task editTask = new Task(obtainUserEditInput(userInput));
			this.tasksMap.put(editTask, taskInt);
			syncTasks();
		}
	}
	
	private Task getTaskById(int id) {
		for(Entry<Task, Integer> entry: this.tasksMap.entrySet()) {
			if(entry.getValue().equals(new Integer(id))) {
				return entry.getKey();
			}
		}
		return null;
	}

	private void doDelete(String inputNumber) {
		assert (inputNumber != null);
		this.logger.info("doDelete:" + inputNumber);
		int listIndex = 0;
		if (checkDeleteInput(inputNumber)) {
			int inputIndex = Integer.parseInt(inputNumber);
			while (inputIndex > 0) {
				if (!tasks.get(listIndex).getMarkAsDelete()) {
					inputIndex--;
					if (inputIndex != 0) {
						listIndex++;
					}
				} else {
					listIndex++;
				}
			}
			tasks.get(listIndex).setMarkAsDelete();
			syncTasks(tasks);
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
		this.logger.info("checkEditIndexInput:" + inputNumber);
		String[] stringArray = inputNumber.split(" ");
		String taskIndex = stringArray[NUMBER_TASK_INDEX];
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
		else if (!isPositiveNonZeroInt(taskIndex)) {
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

	// remove task index from usercommand and return edit input
	private String obtainUserEditInput(String userCommand) {
		StringBuilder sb = new StringBuilder();
		String[] stringArray = userCommand.split(" ");
		for (int i = 1; i < (stringArray.length); i++) {
			sb.append(stringArray[i]);
			sb.append(" ");
		}
		showToUser(sb.toString());
		this.logger.info("obtainUserEditInput:" + sb.toString());
		return sb.toString();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	// Method checks if inputString is a valid String
	private boolean isValidString(String inputString) {
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

	public String testEdit(String userInput) {
		tasks.clear();
		Task task1 = new Task("meeting1 on 27-2-2014 from 1pm to 2pm");
		tasks.add(task1);
		if (checkEditIndexInput(userInput)) {
			int taskInt = (Integer.parseInt(getFirstWord(userInput)) - EDIT_OFFSET);
			Task editTask = new Task(obtainUserEditInput(userInput));
			tasks.set(taskInt, editTask);
//			writeToJson();
			StringBuilder sb = new StringBuilder();
			sb.append(tasks.get(0).getTaskDescription());
			sb.append(tasks.get(0).getTaskStartTime().getHourOfDay());
			sb.append(tasks.get(0).getTaskEndTime().getHourOfDay());
			return sb.toString();
		} else {
			return FEEDBACK;
		}
	}

	public String testDelete(String inputNumber) {
		tasks.clear();
		Task task1 = new Task("meeting1 on 27-2-2014 from 1pm to 2pm");
		Task task2 = new Task("meeting2 on 27-2-2014 from 2pm to 3pm");
		Task task3 = new Task("meeting3 on 27-2-2014 from 3pm to 4pm");
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		if (checkDeleteInput(inputNumber)) {
			int listIndex = 0;
			if (checkDeleteInput(inputNumber)) {
				int inputIndex = Integer.parseInt(inputNumber);
				while (inputIndex > 0) {
					if (!tasks.get(listIndex).getMarkAsDelete()) {
						inputIndex--;
						if (inputIndex != 0) {
							listIndex++;
						}
					} else {
						listIndex++;
					}
				}
				tasks.get(listIndex).setMarkAsDelete();
				syncTasks(tasks);
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < tasks.size(); i++) {
				if (tasks.get(i).getMarkAsDelete()) {
					int index = i + 1;
					sb.append("task" + index + "deleted");
				} else {
					int index = i + 1;
					sb.append("task" + index + "notdeleted");
				}
			}
			return sb.toString();
		} else {
			return FEEDBACK;
		}
	}

	private void syncTasks() {
		this.sortById();
		System.out.println(this.tasksMap);
		List<Task> tasks = new ArrayList<Task>(this.tasksMap.keySet());
		this.storage.writeTasks(tasks);
	}
	
	/* sort the task hashmap by its index */
	private void sortById() {
		List<Entry<Task, Integer>> entries = new ArrayList<Entry<Task, Integer>>(this.tasksMap.entrySet());
		Collections.sort(entries, new Comparator<Entry<Task, Integer>>() {
			@Override
			public int compare(Entry<Task, Integer> e1, Entry<Task, Integer> e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		});
		
		Map<Task, Integer> sortedMap = new LinkedHashMap<Task, Integer>();
		for(Entry<Task, Integer> entry: entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		this.tasksMap = sortedMap;
	}

	private void syncTasks(List<Task> tasks) {

	}
}
