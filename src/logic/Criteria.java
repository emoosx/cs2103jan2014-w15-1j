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
public class Criteria {
	
	/* get all tasks that are not marked as deleted */
	
//	public static ArrayList<Task> getAllUndeletedTasks(List<Task> tasks) {
//		Predicate<Task> undeletedTaskPredicate = new Predicate<Task>() {
//			public boolean apply(Task t) {
//				return t.getMarkAsDelete() == false;
//			}
//		};
//		Collection<Task> collection = Collections2.filter(tasks, undeletedTaskPredicate);
//		ArrayList<Task> result = new ArrayList<Task>(collection);
//		return result;
//	}
	
	/* default criteria */
	public static ArrayList<Integer> getAllUndeletedTasks(List<Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	

	public static ArrayList<Integer> getAllUndeletedFloatingTasks(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
			if(t.getMarkAsDelete() == false) {
				if(t.getTaskStartTime() == null && t.getTaskEndTime()== null)
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllFloatingTasks(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
				if(t.getTaskStartTime() == null && t.getTaskEndTime()== null)
				result.add(tasks.indexOf(t));
			}
		return result;
	}
	
	public static ArrayList<Integer> getAllUndeletedTimedTasks(List <Task> tasks, String keyword) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
			if(t.getMarkAsDelete() == false) {
				if(t.getTaskStartTime() != null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllTimedTasks(List <Task> tasks, String keyword) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
				if(t.getTaskStartTime() != null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));		
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllUndeletedDeadlineTasks(List <Task> tasks, String keyword) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
			if(t.getMarkAsDelete() == false) {
				if(t.getTaskStartTime() == null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllDeadlineTasks(List <Task> tasks, String keyword) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
				if(t.getTaskStartTime() == null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllTaskforToday(List<Task> tasks) {
		return new ArrayList<Integer>();
	}
	
	public static ArrayList<Integer> getAllTasksforTomorrow(List <Task> tasks) {
		return new ArrayList<Integer>();
	}
	
	
}
