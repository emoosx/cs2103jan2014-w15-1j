import java.util.Stack;

/*
 * This class performs undo operations
 * It will store deleted task objects into a stack 
 * Stack will be popped when undo is executed
 * V0.1 - Supports undo for 1 operation
 */

public class TaskUndo {
	
	private static Stack<Task> undoStack = new Stack<Task>();
	
	public void undoAction() {
		
	}
	
	public void addHistory(Task task) {
		undoStack.push(task);
	}
	
}
