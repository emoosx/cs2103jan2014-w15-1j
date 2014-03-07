package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import storage.StorageHelper;

public class TaskFactory {
	
	public final String UNDO_ADD = "add";
	public final String UNDO_EDIT = "edit";
	public final String UNDO_DONE = "done";
	public final String UNDO_ARCHIVE = "archive";
	public final String UNDO_DONEALL = "doneall";
	public final String UNDO_ARCHIVEALL = "archiveall";

	private List<Task> tasks;
	private StorageHelper storage;
	
	private Stack<Map<Integer, List<String>>> undoStack;
	
	public TaskFactory() {
		tasks = new ArrayList<Task>();
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
	
	/*
	 * insert a new row into the undo stack of task collection
	 */
	private void saveUndo(List<String> item) {
		
	}
}
