package core;

import java.util.ArrayList;

import logic.TaskParser;

import org.joda.time.DateTime;

//@author A0101810A
/*
 * Task class stores all required attributes of a task specified by the user input
 * Given a user input, Task class calls the TaskParser class which will parse the input
 * This class will then obtain all necessary fields from TaskParser
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
		setTaskDescription(null);
		setTaskStartTime(null);
		setTaskEndTime(null);
		setTaskCreatedTimestamp(new DateTime());
		setTaskUndone();
		setMarkAsUndelete();
		parse(rawText);
	}
	
	private void parse(String rawText) {
		assert(!rawText.equals(null));
		TaskParser parser = new TaskParser();
		parser.parseTask(rawText);
		taskDescription = parser.getTaskDescription();
		startDateTime = parser.getStartDateTime();
		endDateTime = parser.getEndDateTime();
		taskTags = parser.getHashTag();
	}
	
	public void setID(int id) {
		this.taskID = id;
	}
	public int getID() {
		return taskID;
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
	
	public String getRawText() {
		return this.rawText;
	}

	public DateTime getTaskCreatedTimestamp() {
		return taskCreatedTimestamp;
	}

	public void setTaskCreatedTimestamp(DateTime taskCreatedTimestamp) {
		this.taskCreatedTimestamp = taskCreatedTimestamp;
	}
	
	public boolean isOverdue() {
		if(taskDone == false && markAsDelete == false && endDateTime != null && endDateTime.isBeforeNow()) {
			return true;
		} else {
			return false;
		}
	}
}
