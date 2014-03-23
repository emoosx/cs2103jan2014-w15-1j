package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import core.Task;

// Class to help with the filtering of tasks
// Task list is passed from the CommandFactory
public class TaskLister {
	
	private List<Task> tasks;

	public TaskLister(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	// get all tasks that are not marked as deleted
	public ArrayList<Task> getAllTasks() {

		Predicate<Task> undeletedTaskPredicate = new Predicate<Task>() {
			public boolean apply(Task t) {
				return t.getMarkAsDelete() == false;
			}
		};
		Collection<Task> filteredTasks = Collections2.filter(tasks, undeletedTaskPredicate);
		ArrayList<Task> result = new ArrayList<Task>(filteredTasks);
		return result;
	}
}
