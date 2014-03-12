package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import logic.Command.COMMAND_TYPE;
import storage.StorageHelper;
import core.Task;

public class CommandFactory {
	
	
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
	
	/*
	 * populate command stack and task list from storage
	 */
	private void fetch() {
		
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
	
	
	public List<Task> getTasksList() {
		return tasks;
	}

	public void updateTasksList(List<Task> newTask) {
		tasks = newTask;
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
			break;
		case LIST:
			doList(rawText);
			break;
		case EDIT:
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
			break;
		case HELP:
			break;
		default:
			break;
		}
	}
	
	private void doList(String rawText) {
		assert(rawText != null);
		List<Task> tasks = this.storage.getAllTasks();
		for(Task t : tasks) {
			System.out.println(t);
		}
		
	}

}
