package core;

import java.util.ArrayList;

import logic.TaskParser;

import org.joda.time.DateTime;

/*
 * Task class that will be used for creation of task objects.
 * Task objects will be converted into GSON objects which will be handled by storage
 */

public class Task {

//	protected int taskID;
	private String rawText;
	protected String taskDescription;
	protected ArrayList<String> taskTags;			// tags are in lowercase
	protected DateTime startDateTime;
	protected DateTime endDateTime;
	protected boolean taskDone;
	private DateTime taskCreatedTimestamp; 
	private boolean hasAlias;

	// Constructor method
	public Task(String rawText) {
		this.rawText = rawText;
		this.taskDescription = null;
		this.startDateTime = null;
		this.endDateTime = null;
		this.taskCreatedTimestamp = new DateTime();
		this.taskDone = false;
		this.hasAlias = false;
		this.parse(this.rawText);
	}
	
	// This method will update the necessary attributes of the task
	private void parse(String rawText) {
		TaskParser parser = new TaskParser(rawText);
		parser.parseTask();
		taskDescription = parser.getTaskDescription();
		startDateTime = parser.getStartDateTime();
		endDateTime = parser.getEndDateTime();
	}
	
//	public int getID() {
//		return this.taskID;
//	}
//
//	public void setID(int id) {
//		this.taskID = id;
//	}
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
	
	// Override method for toString
	public String toString() {
		return this.taskDescription;
	}
}
