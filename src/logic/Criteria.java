package logic;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

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
	
	public static ArrayList<Task> getAllOverdueTasks(List<Task> tasks) {
		ArrayList<Task> result = new ArrayList<Task>();
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false && t.getTaskEndTime() != null && 
			   t.getTaskEndTime().isBeforeNow())
				result.add(t);
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllOverdueTaskIDs(List<Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false && t.getTaskEndTime() != null && 
			   t.getTaskEndTime().isBeforeNow())
				result.add(tasks.indexOf(t));
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllDoneTasks(List<Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskDone() == true) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	/* default criteria */
	public static ArrayList<Integer> getAllUndeletedTasks(List<Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	

	public static ArrayList<Integer> getAllUndeletedFloatingTasks(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks){
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false) {
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
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false) {
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
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false) {
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
			if(t.getMarkAsDelete()==false && t.getTaskEndTime() != null && !t.getTaskDone() &&
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
			if(t.getMarkAsDelete() == false && t.getTaskEndTime() != null && !t.getTaskDone() && 
               t.getTaskEndTime().withTimeAtStartOfDay().isEqual(tomorrow.withTimeAtStartOfDay())) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllTasksforThisWeek(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		DateTime today = new DateTime();
		DateTime nextWeek = today.plusWeeks(1);
		Interval interval = new Interval(today, nextWeek);
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false) {
				// deadline tasks - check with end timestamp
				if(t.getTaskStartTime() == null && t.getTaskEndTime() != null) {
					if(interval.contains(t.getTaskEndTime())) {
						result.add(tasks.indexOf(t));
					}
				// timed tasks - check with start timestamp
				} else if(t.getTaskStartTime() != null && t.getTaskEndTime() != null) {
					if(interval.contains(t.getTaskStartTime()))
						result.add(tasks.indexOf(t));
				}
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllTasksforNextWeek(List <Task> tasks) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		DateTime today = new DateTime();
		Interval interval = new Interval(today.plusWeeks(1), today.plusWeeks(2));
		for(Task t: tasks) {
			if(t.getMarkAsDelete() == false && t.getTaskDone() == false) {
				// deadline tasks - check with end timestamp
				if(t.getTaskStartTime() == null && t.getTaskEndTime() != null) {
					if(interval.contains(t.getTaskEndTime())) {
						result.add(tasks.indexOf(t));
					}
				// timed tasks - check with start timestamp
				} else if(t.getTaskStartTime() != null && t.getTaskEndTime() != null) {
					if(interval.contains(t.getTaskStartTime()))
						result.add(tasks.indexOf(t));
				}
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllUndeletedTasksWithHashTag(List<Task> tasks, String rawText) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks) {
			if(t.getTaskTags().contains(rawText) && t.getTaskDone() == false && t.getMarkAsDelete() == false) {
				result.add(tasks.indexOf(t));
			}
		}
		return result;
	}
	
	public static ArrayList<Integer> getAllUndeletedTasksWithTimestamp(List<Task> tasks, DateTime inputDate) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(Task t: tasks) {
			// deadline tasks
			if(t.getTaskStartTime() == null && t.getTaskEndTime() != null && t.getMarkAsDelete() == false && t.getTaskDone() == false) {
				if(t.getTaskEndTime().withTimeAtStartOfDay().isEqual(inputDate.withTimeAtStartOfDay())) {
					result.add(tasks.indexOf(t));
				}

			// timed tasks
			} else if(t.getTaskStartTime() != null && t.getTaskEndTime() != null && t.getMarkAsDelete() == false && t.getTaskDone() == false) {
                Interval interval = new Interval(t.getTaskStartTime(), t.getTaskEndTime());
                if(t.getTaskStartTime().withTimeAtStartOfDay().isEqual(inputDate.withTimeAtStartOfDay()) ||
                   t.getTaskEndTime().withTimeAtStartOfDay().isEqual(inputDate.withTimeAtStartOfDay()) ||
                   interval.contains(inputDate)) 
                {
                	result.add(tasks.indexOf(t));
                }
			}
		}
		return result;
	}
}
