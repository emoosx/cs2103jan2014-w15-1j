package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import core.Task;

/* 
 * Class to help with the filtering of tasks
 */
public class TaskLister {
	
	/* get all tasks that are not marked as deleted */
	public static ArrayList<Task> getAllUndeletedTasks(List<Task> tasks) {
		Predicate<Task> undeletedTaskPredicate = new Predicate<Task>() {
			public boolean apply(Task t) {
				return t.getMarkAsDelete() == false;
			}
		};
		Collection<Task> collection = Collections2.filter(tasks, undeletedTaskPredicate);
		ArrayList<Task> result = new ArrayList<Task>(collection);
		return result;
	}
}
