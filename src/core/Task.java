package core;

import java.util.ArrayList;

import logic.TaskParser;

import org.joda.time.DateTime;

/*
 * Task class that will be used for creation of task objects.
 * Task objects will be converted into GSON objects which will be handled by storage
 */

public class Task {

	private static int START_INDEX = 0;
	private static String COMMA = ", ";
	
	private int taskID;
	private String rawText;
	private String taskDescription;
	private ArrayList<String> taskTags;			// tags are in lowercase
	private DateTime startDateTime;
	private DateTime endDateTime;
	private boolean taskDone;
	private DateTime taskCreatedTimestamp; 
	private boolean markAsDelete;

	public Task(String rawText) {
		this.rawText = rawText;
		this.taskDescription = null;
		this.startDateTime = null;
		this.endDateTime = null;
		this.taskCreatedTimestamp = new DateTime();
		this.taskDone = false;
		this.markAsDelete = false;
		this.taskTags = new ArrayList<String>();
		this.parse(this.rawText);
	}
	
	/* Update the necessary attributes of the task */
	private void parse(String rawText) {
		assert(!rawText.equals(null));
		TaskParser parser = new TaskParser(rawText);
		parser.parseTask();
		taskDescription = parser.getTaskDescription();
		startDateTime = parser.getStartDateTime();
		endDateTime = parser.getEndDateTime();
		taskTags = parser.getHashTag();
	}
	
	public void setID(int id) {
		this.taskID = id;
	}

	public void setTaskDescription(String tDesc){
		this.taskDescription = tDesc;
	}
	
	public String getTaskDescription(){
		return this.taskDescription;
	}
	
	public DateTime getTaskStartTime() {
		return this.startDateTime;
	}

	public DateTime getTaskEndTime() {
		return this.endDateTime;
	}
	
	public void setTaskStartTime(DateTime newStartDateTime) {
		this.startDateTime = newStartDateTime;
	}

	public void setTaskEndTime(DateTime newEndDateTime) {
		this.endDateTime = newEndDateTime;
	}
	
	public void setTaskTags(ArrayList<String> tags) {
		this.taskTags = tags;
	}
	
	public void addNewTag(String newTag) {
		this.taskTags.add(newTag.trim().toLowerCase());
	}
	
	public void setTaskDone() {
		this.taskDone = true;
	}
	
	public void setTaskUndone() {
		this.taskDone = false;
	}
	
	public boolean getTaskDone() {
		return this.taskDone;
	}
	
	public void setMarkAsDelete(){
		this.markAsDelete = true;
	}
	
	public void setMarkAsUndelete() {
		this.markAsDelete = false;
	}
	
	public boolean getMarkAsDelete(){
	   return this.markAsDelete;	
	}
	
	public ArrayList<String> getTaskTags() {
		return taskTags;
	}
	
	public String getTags() {
		StringBuilder sb = new StringBuilder();
		if(taskTags.isEmpty()) {
			return sb.toString();
		} else {
			for(String tag: taskTags) {
				sb.append(tag);
				sb.append(COMMA);
			}
			String result = sb.toString();
			return result.substring(START_INDEX, result.length() - COMMA.length());
		}

	}
	
	public String getLabel() {
		if(this.startDateTime == null && this.endDateTime == null) {
			return "F";
		} else if(this.startDateTime == null) {
			return "D";
		} else {
			return "T";
		}
	}
	
	// Override method for toString
	@Override
	public String toString() {
		return this.taskDescription + " " + String.valueOf(this.taskDone);
	}
}
