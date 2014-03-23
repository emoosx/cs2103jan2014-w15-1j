package logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import core.Task;

// Class to help with the filtering of tasks
// Task list is passed from the CommandFactory
public class TaskLister {
	
	// get all tasks that are not marked as deleted
	public static ArrayList<Task> getAllUndeletedTasks(Map<Task, Integer> map) {

		Predicate<Task> undeletedTaskPredicate = new Predicate<Task>() {
			public boolean apply(Task t) {
				return t.getMarkAsDelete() == false;
			}
		};
		
		List<Task> originalTasks = new ArrayList<Task>(map.keySet());
		Collection<Task> collection = Collections2.filter(originalTasks, undeletedTaskPredicate);
		ArrayList<Task> result = new ArrayList<Task>(collection);
		return result;
	}
}
