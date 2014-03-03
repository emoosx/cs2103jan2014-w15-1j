import java.util.Stack;
import core.Task;

/*
 * This class performs undo operations
 * It will store deleted task objects into a stack 
 * Stack will be popped when undo is executed
 * V0.1 - Supports undo for at least 1 operation
 */

public class TaskUndo {
	
	private static final String MESSAGE_EMPTY_HISTORY = "There is nothing to undo.";
	
	private static Stack<Task> taskStack = new Stack<Task>();
	private static Stack<String> commandStack = new Stack<String>();
	
	public void undoAction() {
		if(commandStack.isEmpty()) {
			showToUser(MESSAGE_EMPTY_HISTORY);
			return;
		}
		String undoCommand = commandStack.pop();
		switch(undoCommand) {
			case("add"):
				// Delete
				break;
			case("delete"):
				// Add
				break;
			case("edit"):
				// Un-edit
				break;
			default:
				break;
		}
	}
	
	public void addDeleteHistory(Task task) {
		commandStack.push("delete");
		taskStack.push(task);
	}
	
	public void addEditHistory(Task oldTask, Task newTask) {
		commandStack.push("add");
		taskStack.push(oldTask);
		
		commandStack.push("delete");
		taskStack.push(newTask);
	}
	
	private void showToUser(String string) {
		System.out.println(string);
	}
}
