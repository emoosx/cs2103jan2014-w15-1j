package logic;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

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
			if(t.getMarkAsDelete() == false && !t.getTaskDone()) {
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
	
	public static ArrayList<Integer> getAllUndeletedTimedTasks(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
			if(t.getMarkAsDelete() == false) {
				if(t.getTaskStartTime() != null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllTimedTasks(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
				if(t.getTaskStartTime() != null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));		
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllUndeletedDeadlineTasks(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
			if(t.getMarkAsDelete() == false) {
				if(t.getTaskStartTime() == null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllDeadlineTasks(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
				if(t.getTaskStartTime() == null && t.getTaskEndTime() != null)
				result.add(tasks.indexOf(t));
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllTasksforToday(List<Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		DateTime today = new DateTime();
		for(Task t: tasks) {
			if(t.getMarkAsDelete()==false && t.getTaskEndTime() != null && 
			   t.getTaskEndTime().withTimeAtStartOfDay().isEqual(today.withTimeAtStartOfDay())) {
				result.add(tasks.indexOf(t));
			}
		}
		System.out.println(result.size());
		return result;
	}
	
	public static ArrayList<Integer> getAllTasksforTomorrow(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		DateTime today = new DateTime();
		DateTime tomorrow = today.plusDays(1);
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskEndTime() != null &&
               t.getTaskEndTime().withTimeAtStartOfDay().isEqual(tomorrow.withTimeAtStartOfDay())) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllTasksforThisWeek(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		DateTime today = new DateTime();
		DateTime nextWeek = today.plusDays(1);
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskEndTime() != null &&
			   t.getTaskEndTime().withTimeAtStartOfDay().isEqual(nextWeek.withTimeAtStartOfDay())) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllUndeletedTasksWithHashTag(List<Task> tasks, String rawText) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks) {
			if(t.getTaskTags().contains(rawText)) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
}
