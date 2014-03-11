package logic;

import java.util.*;
import java.io.*;

import logic.*;
import core.Task;


public class TaskDelete {
	private static final String MESSAGE_INVALID_DELETE = "Usage: delete <number>";
	private static final String MESSAGE_INVALID_NUMBERFORMAT = "Please key in an integer";
	private static final String MESSAGE_INVALID_NUMBERSIGN = "Please key in a positive number";
	private static final String MESSAGE_INVALID_NUMBER = "Please choose a lower value";
	private static final String MESSAGE_DELETED = "deleted : \"%s\"";
	private static final String MESSAGE_EMPTY = "file is empty";
	

	private static Integer DELETE_PARA = 1;
	private static Integer DELETE_OFFSET = 1;
	//String FEEDBACK used for testing purposes;
	private static String FEEDBACK;
	
    private List<Task> taskList;
    private CommandFactory cFactory = new CommandFactory();
	// To be coded by Clement
	/*
	 * How it works: - search for task id - no task found > return error -delete
	 * task object - delete task successful update gson
	 */
	public void execute(String inputNumber) {
		getTaskList();
		if (checkDeleteInput(inputNumber)) {
			int lineToRemove = Integer.parseInt(inputNumber) - DELETE_OFFSET;
			 String deletedString = taskList.get(lineToRemove).getTaskDescription();
			 taskList.remove(lineToRemove);
			//Task task = taskList.remove(lineToRemove);
			 //addToHistory(task);
			 showToUser(String.format(MESSAGE_DELETED, deletedString));
			 cFactory.updateTasksList(taskList);
			 cFactory.writeToJson();
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

	// Need to edit arraylist name
	private boolean checkIfNumberBelowArraySize(String n) {
		if (checkIfFileIsEmpty()) {
			showToUser(MESSAGE_EMPTY);
			FEEDBACK = MESSAGE_EMPTY;
			return false;
		}
		try {
			int num = Integer.parseInt(n) - 1;
			 if (num >= taskList.size()) {
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
	private boolean isPositiveNonZeroInt(String lineNumber) {
		try {
			int i = Integer.parseInt(lineNumber);
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

	// Method will print out given argument
	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

	// Method checks if data list is empty
	// Arraylist name to be edited accordingly
	private boolean checkIfFileIsEmpty() {
		 if (taskList.isEmpty()) {
		return true;
		 }
		 return false;
	}

	// Method checks if inputString is not empty
	private boolean isValidString(String inputString) {
		if (inputString.trim().length() == 0) {
			return false;
		}
		return true;
	}
	
	private void getTaskList(){
		taskList= cFactory.getTasksList();
	}
	
	public String testDelete(String inputNumber){
		taskList = new ArrayList<Task>();
		Task task1 = new Task("meeting1 on 27-2-2014 from 1pm to 2pm");
		//task1.setTaskDescription("1");
		Task task2 = new Task("meeting2 on 27-2-2014 from 2pm to 3pm");
		//task2.setTaskDescription("2");
		Task task3 = new Task("meeting3 on 27-2-2014 from 3pm to 4pm");
		//task3.setTaskDescription("3");
		taskList.add(task1);
		taskList.add(task2);
		taskList.add(task3);
		if (checkDeleteInput(inputNumber)) {
			int lineToRemove = Integer.parseInt(inputNumber) - DELETE_OFFSET;
			 String deletedString = taskList.get(lineToRemove).getTaskDescription();
			 taskList.remove(lineToRemove);
			 //Task task = taskList.remove(lineToRemove);
			 //addToHistory(task);
			 showToUser(String.format(MESSAGE_DELETED, deletedString));
			 cFactory.updateTasksList(taskList);
			 cFactory.writeToJson();
			 StringBuilder sb = new StringBuilder();
				for(int i=0; i<taskList.size();i++){
				sb.append(taskList.get(i).getTaskDescription());
				}
				return sb.toString();
		}else{
			return FEEDBACK;
		}
		
	}
	
	/* Method adds recent deletion to the history for undo purposes
	private void addToHistory(Task task) {
		TaskUndo history = new TaskUndo();
		history.addDeleteHistory(task);
	}
	*/
}
