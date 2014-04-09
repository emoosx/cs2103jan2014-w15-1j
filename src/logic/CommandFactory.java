package logic;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.logging.Logger;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.Command.COMMAND_TYPE;
import storage.RedoStorage;
import storage.StorageHelper;
import storage.UndoStorage;
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
	private static Integer OFFSET = 1;

	String userInputDesc;
	String commandType;
	String[] inputArray;
	public static String FEEDBACK = "";

	// DELETE//
	private static final String MESSAGE_INVALID_DELETE = "Usage: delete <number>";
	private static final String MESSAGE_INVALID_NUMBER = "Please choose another value";

	private static Integer DELETE_PARA = 1;
	
	//UNDO / REDO//
	private static final String MESSAGE_UNDO_FAIL = "There is nothing to undo";
	private static final String MESSAGE_REDO_FAIL = "There is nothing to redo";


	public static CommandFactory INSTANCE = new CommandFactory();
	public final String UNDO_ADD = "add";
	public final String UNDO_EDIT = "edit";
	public final String UNDO_DONE = "done";
	public final String UNDO_DONEALL = "doneall";
	
	// DateTime printing
  	private static final DateTimeFormatter dateDisplay = DateTimeFormat.forPattern("dd/MM/YY");
	private static final DateTimeFormatter timeDisplay = DateTimeFormat.forPattern("HH:mm");
	private static final DateTimeFormatter dateTimeDisplay = DateTimeFormat.forPattern("dd/MM/YY HH:mm");

//	private List<Task> tasks;
	private List<Task> tasks;
	private ObservableList<Task> display;
	private LinkedHashMap<Integer, Integer> tasksMap; // <displayId, realId>


	private StorageHelper storage;
	private UndoStorage undoStorage;
	private RedoStorage redoStorage;
	private Logger logger = PandaLogger.getLogger();

	private Stack<SimpleEntry<Integer, Command>> undoStack;
	private Stack<SimpleEntry<Integer, Command>> redoStack;

	private CommandFactory() {
		this.tasks = new ArrayList<Task>();
		this.display = FXCollections.observableArrayList();
		this.undoStack = new Stack<SimpleEntry<Integer, Command>>();
		this.redoStack = new Stack<SimpleEntry<Integer, Command>>();
		this.tasksMap = new LinkedHashMap<Integer, Integer>(); // <ID to display, real ID in tasks>
		this.storage = StorageHelper.INSTANCE;
		this.undoStorage = UndoStorage.INSTANCE;
		this.redoStorage = RedoStorage.INSTANCE;
		this.fetch();
	}
	
	/* populate tasks buffer and undo command stack */ 
	private void fetch() {
		this.tasks = this.storage.getAllTasks();
		this.display = FXCollections.observableArrayList(tasks);
		this.populateTasksMapWithDefaultCriteria();
		this.populateUndoStack();
		this.populateRedoStack();
	}

	/* initialize and populate undoStack */
	private void populateUndoStack() {
		this.undoStack = this.undoStorage.getAllCommands();
	}
	
	/* initialize and populate redoStack */
	private void populateRedoStack() {
		this.redoStack = this.redoStorage.getAllCommands();
	}
	
	/* by default, display tasks which are not marked as deleted */
	private void populateTasksMapWithDefaultCriteria() {
		ArrayList<Integer> undeletedTasksIDs = Criteria.getAllUndeletedTasks(tasks);
		for(int i = 0; i < undeletedTasksIDs.size(); i++) {
			this.tasksMap.put(i, undeletedTasksIDs.get(i));
		}
	}

	public List<Task> getTasks() {
		return this.tasks;
	}
	
	public ObservableList<Task> getDisplayTasks() {
		this.display.clear();
		for(Map.Entry<Integer, Integer> entry : tasksMap.entrySet()) {
			this.display.add(tasks.get(entry.getValue()));
		}
		return FXCollections.observableArrayList(this.display);
	}
	
	public LinkedHashMap<Integer, Integer> getTasksMap() {
		return this.tasksMap;
	}

	public void process(Command command) {
		executeCommand(command);
	}

	public void executeCommand(Command command) {
		assert (command.rawText != null);
		switch (command.command) {
		case ADD:
			doAdd(command);
			this.redoStack.clear();
			break;
		case LIST:
			doList(command);
			break;
		case EDIT:
			doEdit(command);
			this.redoStack.clear();
			break;
		case UNDO:
			doUndo();
			break;
		case DONE:
			doDone(command);
			this.redoStack.clear();
			break;
		case REDO:
			doRedo();
			break;
		case CLEAR:
			break;
		case DONEALL:
			break;
		case DELETE:
			doDelete(command);
			this.redoStack.clear();
			break;
		case HELP:
			break;
		default:
			break;
		}
	}

	private void doUndo() {
		logger.info("doUndo");
		if(!undoStack.isEmpty()){
		SimpleEntry<Integer, Command> lastEntry = this.undoStack.pop();
		int taskid = lastEntry.getKey();
		Command lastCommand = lastEntry.getValue();
		logger.info("Last Command:" + lastCommand.toString());
		executeUndo(taskid, lastCommand);
		syncTasks();
		System.out.println("After undo:" + tasksMap);
		}else{
			  showToUser(MESSAGE_UNDO_FAIL);
		}
	}

	private void executeUndo(int taskid, Command command) {
		assert (command.rawText == null);
		switch (command.command) {
		case ADD:
			doUndoAdd(taskid, command);
			break;
		case EDIT:
			doUndoEdit(taskid, command);
			break;
		case DELETE:
			doUndoDelete(taskid, command);
			break;
		case DONE:
			doUndoDone(taskid, command);
		default:
			return;
		}
	}
	
	private void doRedo() {
		logger.info("doRedo");
		if(!redoStack.isEmpty()){
		SimpleEntry<Integer, Command> lastEntry = this.redoStack.pop();
		int taskid = lastEntry.getKey();
		Command lastCommand = lastEntry.getValue();
		logger.info("Last Command:" + lastCommand.toString());
		executeRedo(taskid, lastCommand);
		syncTasks();
		System.out.println("After redo:" + tasksMap);
		}else{
			  showToUser(MESSAGE_REDO_FAIL);
		}
	}
	
	private void executeRedo(int taskid, Command command) {
		assert (command.rawText == null);
		switch (command.command) {
		case ADD:
			doRedoAdd(taskid, command);
			break;
		case EDIT:
			doRedoEdit(taskid, command);
			break;
		case DELETE:
			doRedoDelete(taskid, command);
			break;
		case DONE:
			doRedoDone(taskid, command);
		default:
			return;
		}
	}
	

	private void doRedoDone(int taskid, Command command) {
		doDone(command);	
	}

	private void doRedoDelete(int taskid, Command command) {
	doDelete(command);
	}

	private void doRedoEdit(int taskid, Command command) {
	doEdit(command);
	}

	private void doRedoAdd(int taskid, Command command) {
	doAdd(command);
	}
	
	private Command convertAddTaskToCommand(int taskid){
		Task taskToAdd = tasks.get(taskid);
		ArrayList<String> tags = taskToAdd.getTaskTags();
	    //desc time date 
		StringBuilder sb = new StringBuilder();

		sb.append(COMMAND_TYPE.ADD.name().toLowerCase() + " " + taskToAdd.getTaskDescription());
		//deadline
	    if(taskToAdd.getTaskStartTime() == null && taskToAdd.getTaskEndTime() != null){
	    	sb.append(" on " + dateDisplay.print(taskToAdd.getTaskEndTime()) + " by "+
					timeDisplay.print(taskToAdd.getTaskEndTime()));
		//timed
		}else if(taskToAdd.getTaskStartTime() != null && taskToAdd.getTaskEndTime() != null){
			sb.append(" from " +dateTimeDisplay.print(taskToAdd.getTaskStartTime()));
			sb.append(" to " +dateTimeDisplay.print(taskToAdd.getTaskEndTime()));
		}
	    if(tags.size() != 0){
	    	for(int i =0; i<tags.size(); i++){
	    	sb.append(" " + tags.get(i));
	    	}
	    }
	    String rawText = sb.toString();
		this.logger.info("string is :" + rawText);
	    Command oldCommand = new Command(rawText);
	    return oldCommand;
	}

	private void doAdd(Command command) {
		assert (command.rawText!=null);
		Task newTask = new Task(command.rawText);
		this.tasks.add(newTask);
		System.out.println("Before Add:" + tasksMap);
		this.tasksMap.put(tasksMap.size(), tasks.size() - OFFSET);
		System.out.println("After Add:" + tasksMap);
		this.undoStack.push(new SimpleEntry<Integer, Command>(this.tasks.size() - OFFSET, command));
		syncTasks();
	}
	
	private void doUndoAdd(int taskid, Command command) {
		/* remove it from the buffer
		 * remove the entry from the map
         */
		this.redoStack.push(new SimpleEntry<Integer, Command>(taskid,convertAddTaskToCommand(taskid)));
		this.tasks.remove(taskid);
		Integer fakeID = getFakeIDbyRealId(taskid);
		assert(fakeID != null);
		this.tasksMap.remove(fakeID);
		syncTasks();
	}

	private void doList(Command command) {
		logger.info("doList");
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		// some hard-coded cases for (tmw|today|this week|floating|timed|deadline)
		if(command.rawText == null || command.rawText.equals("")) {
			result = Criteria.getAllUndeletedTasks(tasks);
		}
		else if(command.rawText.equalsIgnoreCase("tmw") || command.rawText.equalsIgnoreCase("tomorrow")) {
			result = Criteria.getAllTasksforTomorrow(tasks);
		} else if(command.rawText.equalsIgnoreCase("today")) {
			logger.info(tasks.toString());
			result = Criteria.getAllTasksforToday(tasks);
		} else if(command.rawText.equalsIgnoreCase("this week")) {
			result = Criteria.getAllTasksforThisWeek(tasks);
		} else if(command.rawText.equalsIgnoreCase("floating")) {
			result = Criteria.getAllUndeletedFloatingTasks(tasks);
		} else if(command.rawText.equalsIgnoreCase("deadline")) {
			result = Criteria.getAllUndeletedDeadlineTasks(tasks);
		} else if(command.rawText.equalsIgnoreCase("timed")) {
			result = Criteria.getAllUndeletedTimedTasks(tasks);
		} else if(command.rawText.startsWith("#")) {
			result = Criteria.getAllUndeletedTasksWithHashTag(tasks, command.rawText);
		} else {
			// assume it as a timestamp
			result = Criteria.getAllUndeletedTasks(tasks);
		}

		this.tasksMap.clear();
		for(int i = 0; i < result.size(); i++) {
			this.tasksMap.put(i, result.get(i));
		}
	}
	
	

	/* remove the original task from tasksMap and replace it with new task */
	private void doEdit(Command command) {
		String userInput = command.rawText;
		assert (userInput != null);
		this.logger.info("doEdit:" + userInput);
		if (checkEditIndexInput(userInput)) {
			int taskInt = (Integer.parseInt(getFirstWord(userInput)) - EDIT_OFFSET);
			Task editTask = new Task(obtainUserEditInput(userInput));
			this.undoStack
					.push(new SimpleEntry<Integer, Command>(tasksMap
							.get(taskInt), convertTaskToCommand(tasksMap
							.get(taskInt))));
			this.tasks.set(tasksMap.get(taskInt), editTask);
	
			syncTasks();
		}
	}
	
	/* cater for single undo edit */
	private void doUndoEdit(int taskid, Command command) {
		System.out.println("undo edit:"+taskid);
		System.out.println(command.rawText);
		System.out.println("tasksmap is"+ this.tasksMap.toString());
		Task oldTask = new Task(command.rawText);
		int displayID = this.getDisplayId(taskid);
		System.out.println("displayID is"+ displayID);
		this.redoStack.push(new SimpleEntry<Integer, Command>(taskid,convertEditedTaskToCommand(displayID)));
		this.tasks.set(taskid, oldTask);
		syncTasks();
	}
	
	private Command convertEditedTaskToCommand(int taskid){
		System.out.println("task id is"+taskid);
		Task editedTask = tasks.get(tasksMap.get(taskid-OFFSET));
		ArrayList<String> tags = editedTask.getTaskTags();
		StringBuilder sb = new StringBuilder();
		sb.append(COMMAND_TYPE.EDIT.name().toLowerCase() + " " + taskid+ " "+ editedTask.getTaskDescription());
	    if(editedTask.getTaskStartTime() == null && editedTask.getTaskEndTime() != null){
	    	sb.append(" on " + dateDisplay.print(editedTask.getTaskEndTime()) + " by "+
					timeDisplay.print(editedTask.getTaskEndTime()));
		}else if(editedTask.getTaskStartTime() != null && editedTask.getTaskEndTime() != null){
			sb.append(" from " +dateTimeDisplay.print(editedTask.getTaskStartTime()));
			sb.append(" to " +dateTimeDisplay.print(editedTask.getTaskEndTime()));
		}
	    if(tags.size() != 0){
	    	for(int i =0; i<tags.size(); i++){
	    	sb.append(" " + tags.get(i));
	    	}
	    }
	    String rawText = sb.toString();
		System.out.println("string is :" + rawText);
	    Command oldCommand = new Command(rawText);
	    return oldCommand;	
	}
	
	private Command convertTaskToCommand(int taskid){
		Task oldTask = tasks.get(taskid);
		ArrayList<String> tags = oldTask.getTaskTags();
		StringBuilder sb = new StringBuilder();
		sb.append(COMMAND_TYPE.EDIT.name().toLowerCase() + " " + oldTask.getTaskDescription());
	    if(oldTask.getTaskStartTime() == null && oldTask.getTaskEndTime() != null){
			sb.append(" on " + dateDisplay.print(oldTask.getTaskEndTime()) + " by "+
					timeDisplay.print(oldTask.getTaskEndTime()));
		}else if(oldTask.getTaskStartTime() != null && oldTask.getTaskEndTime() != null){
		sb.append(" from " +dateTimeDisplay.print(oldTask.getTaskStartTime()));
		sb.append(" to " +dateTimeDisplay.print(oldTask.getTaskEndTime()));
		}
	    if(tags.size() != 0){
	    	for(int i =0; i<tags.size(); i++){
	    	sb.append(" " + tags.get(i));
	    	}
	    }
	    String rawText = sb.toString();
	    Command oldCommand = new Command(rawText);
	    return oldCommand;
	}
	
	
	private void doDelete(Command command) {
		String rawText = command.rawText;
		assert(rawText != null);
		if(checkDeleteInput(rawText)) {
			int displayId = Integer.parseInt(rawText) - OFFSET;
			int realId = tasksMap.get(displayId);
  
			Task task = tasks.get(realId);
			task.setMarkAsDelete();
			updateHashMapAfterDelete(displayId);
			
			Command delCommand = commandWithPreviousIndex(displayId);
			this.undoStack.push(new SimpleEntry<Integer, Command>(realId, delCommand));
			syncTasks();
			System.out.println("After Delete:" + tasksMap);
		}
	}
	
	private void updateHashMapAfterUndoDelete(int realId, int prevId) {
		System.out.println("realId:" + realId);
		System.out.println("prevId:" + prevId);
		LinkedHashMap<Integer, Integer> beforeID = new LinkedHashMap<Integer, Integer>();
		LinkedHashMap<Integer, Integer> afterID = new LinkedHashMap<Integer, Integer>();
		
		if(prevId == -1){
			for(int i = 0; i < tasksMap.size(); i++){
				afterID.put(i, tasksMap.get(i));
			}
			int sizeAfterAdding = tasksMap.size() +1;
			int indexOfAfterId =0;
			tasksMap.clear();
			tasksMap.put(0, realId);
			for(int i =1; i<sizeAfterAdding;i++){
				tasksMap.put(i, afterID.get(indexOfAfterId));
				indexOfAfterId ++;
			}
		}else{
		for(int i = 0;i<=prevId;i++){
			beforeID.put(i, tasksMap.get(i));
		}
		int afterIndex= prevId+1;
		int index = 0;
		int sizeAfterAdding = tasksMap.size() +1;
		for(int k=afterIndex;k<tasksMap.size(); k++){
		afterID.put(index,tasksMap.get(k));	
		index++;
		}
		tasksMap.clear();
		int afterAddingIndex=0;
		for(int l=0; l<beforeID.size();l++ ){
		tasksMap.put(l, beforeID.get(l));
		}
		tasksMap.put(afterIndex,realId);
		for(int k=afterIndex+1; k<sizeAfterAdding;k++){
			tasksMap.put(k,afterID.get(afterAddingIndex));
			afterAddingIndex++;
		}
		}
	}
	
	private void doUndoDelete(int taskid, Command command) {
		Task t = tasks.get(taskid);	
		t.setMarkAsUndelete();
		int prevID = Integer.parseInt(command.rawText);
		updateHashMapAfterUndoDelete(taskid, prevID);
		System.out.println("after restoring:" + this.tasksMap);
		int displayID = this.getDisplayId(taskid);
		System.out.println("displayID is" + displayID);
		this.redoStack.push(new SimpleEntry<Integer, Command>(taskid,convertDeletedTaskToCommand(displayID)));
		syncTasks();
	}
	
	private int getDisplayId(int realID) {
		int displayID =0;
		for (int i=0; i<tasksMap.size();i++) {
		    if(tasksMap.get(i) == realID){
		    displayID = i+ OFFSET;
		    break;
		    }
		}
		 return displayID;
		}

	private Command commandWithPreviousIndex(int displayId){
		StringBuilder sb = new StringBuilder();
		int prevIndex;
		if(displayId != 0){
			prevIndex = displayId -1;
		}else{
			prevIndex = -1;
		}
		sb.append("delete "+ prevIndex);
		
		Command delCommand = new Command(sb.toString());
		return delCommand;
	}
	
	private Command convertDeletedTaskToCommand(int taskid){
		StringBuilder sb = new StringBuilder();
		sb.append(COMMAND_TYPE.DELETE.name().toLowerCase() + " " + taskid);
	    String rawText = sb.toString();
		this.logger.info("string is :" + rawText);
	    Command deletedCommand = new Command(rawText);
	    return deletedCommand;
	}
	
	
	
	private void doDone(Command command) {
		String rawText = command.rawText;
		assert(rawText != null);
		if(checkDeleteInput(rawText)) {
			int displayId = Integer.parseInt(rawText) - OFFSET;
			int realId = tasksMap.get(displayId);
			
			Task task = tasks.get(realId);
			task.setTaskDone();
			updateHashMapAfterDelete(displayId);
			
			Command doneCommand = commandWithPreviousIndex(displayId);
			this.undoStack.push(new SimpleEntry<Integer, Command>(realId, doneCommand));
			syncTasks();
			System.out.println("After Done:" + tasksMap);
		}
	}
	
	private void doUndoDone(int taskid, Command command) {
		Task t = tasks.get(taskid);
		t.setTaskUndone();
		int prevID = Integer.parseInt(command.rawText);
		updateHashMapAfterUndoDelete(taskid, prevID);
		int displayID = this.getDisplayId(taskid);
		this.redoStack.push(new SimpleEntry<Integer, Command>(taskid,convertDoneTaskToCommand(displayID)));
		syncTasks();
	}

	private Command convertDoneTaskToCommand(int displayID) {
		StringBuilder sb = new StringBuilder();
		sb.append(COMMAND_TYPE.DONE.name().toLowerCase() + " " + displayID);
	    String rawText = sb.toString();
		this.logger.info("string is :" + rawText);
	    Command doneCommand = new Command(rawText);
	    return doneCommand;
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

	private Integer getFakeIDbyRealId(int realid) {
		Integer removalKey = null;
		for(Entry<Integer, Integer> entry: tasksMap.entrySet()) {
			if(realid == entry.getValue()) {
				removalKey = entry.getKey();
			}
		}
		return removalKey;
	}

	private void syncTasks() {
		this.undoStorage.writeCommands(undoStack);
		this.redoStorage.writeCommands(redoStack);
		this.storage.writeTasks(tasks);
	}
	
	private void updateHashMapAfterDelete(int fakeid) {
		System.out.println("before undo:" + tasksMap);
		LinkedHashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
		for(int i = 0; i < tasksMap.size(); i++) {
			if(i < fakeid) {
				temp.put(i, tasksMap.get(i));
			} else {
				temp.put(i, tasksMap.get(i+1));
			}
		}
		temp.remove(tasksMap.size()-1);
		this.tasksMap.clear();
		this.tasksMap.putAll(temp);
		System.out.println("after undo: + tasksMap");
	}
}
