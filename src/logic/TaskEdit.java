package logic;


import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import core.Task;

public class TaskEdit {

	/*
	 * Format: edit <taskid/taskname> <
	 */


	private static final String MESSAGE_EDIT_FAIL = "Unable to find file to edit";
	private static final String MESSAGE_INVALID_EDIT = "Usage: edit <index> <description> on <date> from <start time> to <end time>";
	private static final String MESSAGE_INVALID_NUMBERFORMAT = "Please key in an integer";
	private static final String MESSAGE_INVALID_NUMBERSIGN = "Please key in a positive number";
	private static final String MESSAGE_INVALID_NUMBER = "Please choose a lower value";
	private static final String MESSAGE_EDIT_SUCCESS = "Edited successfully";
	private static final String MESSAGE_EMPTY = "file is empty";

	private static Integer NUMBER_TASK_INDEX = 0;
	private static Integer NUMBER_COMMAND_INDEX = 1;
	private static Integer EDIT_PARA = 8;
	private static Integer EDIT_OFFSET = 1;

	private static String FEEDBACK;
	private List<Task> taskList;
	private CommandFactory cFactory = CommandFactory.INSTANCE;
	String userInputDesc;
	String commandType;
	String[] inputArray;

	// user input should contains 8 para eg. 1 meeting1 on 27-2-2014 from 1pm to
	// 2pm
	public void execute(String userInput) {
		getTaskList();	
		if (checkEditIndexInput(userInput)) {
			int taskInt = (Integer.parseInt(getFirstWord(userInput)) - EDIT_OFFSET);
			Task taskToEdit = taskList.get(taskInt);
			parseAndEdit(taskToEdit, userInput);
			cFactory.updateTasksList(taskList);
			cFactory.writeToJson();
		}
	}

	private void parseAndEdit(Task t, String userInput) {		
		TaskParser parse = new TaskParser(obtainUserEditInput(userInput));
		parse.parseTask();
		t.setTaskDescription(parse.getTaskDescription());
		t.setTaskStartTime(parse.getStartDateTime());
		t.setTaskEndTime(parse.getEndDateTime());
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
			if (num >= taskList.size()) {
				showToUser(MESSAGE_EDIT_FAIL);
				FEEDBACK = MESSAGE_EDIT_FAIL;
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

	public String testEdit(String userInput){
		taskList = new ArrayList<Task>();
		Task task1 = new Task("meeting1 on 27-2-2014 from 1pm to 2pm");
		Task task2 = new Task("meeting2 on 27-2-2014 from 2pm to 3pm");
		
		//task1.setTaskDescription("1");
		taskList.add(task1);
		taskList.add(task2);
		if (checkEditIndexInput(userInput)){
		int taskInt = (Integer.parseInt(getFirstWord(userInput)) - EDIT_OFFSET);
				Task taskToEdit = taskList.get(taskInt);
				parseAndEdit(taskToEdit, userInput);
				cFactory.updateTasksList(taskList);
				cFactory.writeToJson();
			 StringBuilder sb = new StringBuilder();
				//for(int i=0; i<taskList.size();i++){
				//sb.append(taskList.get(0).getTaskDescription());
				//sb.append(taskList.get(0).getTaskEndTime());
				//sb.append(taskList.get(i).getTaskStartTime());
				int tasktartHour = taskList.get(0).getTaskStartTime().getHourOfDay();
				int taskendhour = taskList.get(0).getTaskEndTime().getHourOfDay();
				sb.append(taskList.get(0).getTaskDescription());
				sb.append(tasktartHour);
				sb.append(taskendhour);
				//}
				return sb.toString();
		}
		else{
			return FEEDBACK;
		}	
	}

	// Method checks if data list is empty
	private boolean checkIfFileIsEmpty() {
		if (taskList.isEmpty()) {
			return true;
		}
		return false;
	}

	private void getTaskList() {
		taskList = cFactory.getTasksList();
	}

	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

}
