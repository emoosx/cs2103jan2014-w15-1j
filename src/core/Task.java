package core;

import java.util.ArrayList;
import java.util.UUID;

import org.joda.time.DateTime;

/*
 * Task class that will be used for creation of task objects.
 * Task objects will be converted into GSON objects which will be handled by storage
 */

public class Task {

	protected UUID taskID;
	private String rawText;
	protected String taskDescription;
	protected ArrayList<String> taskTags;			// tags are in lowercase
	protected DateTime startTime;
	protected DateTime endTime;
	protected boolean taskDone;
	private DateTime taskCreatedTimestamp; 
	private boolean hasAlias;

	// Constructor method
	public Task(String rawText) {
		this.rawText = rawText;
		this.taskID = UUID.randomUUID();
		this.taskDescription = null;
		this.startTime = null;
		this.endTime = null;
		this.taskCreatedTimestamp = new DateTime();
		this.taskDone = false;
		this.hasAlias = false;
		this.parse(this.rawText);
	}
	
/*	parsing will update the necessary attributes of the task*/
	private void parse(String rawText) {

	}
	
	public UUID getTaskID() {
		return this.taskID;
	}
	
	public void setTaskDescription(String tDesc){
		this.taskDescription = tDesc;
	}
	
	public String getTaskDescription(){
		return this.taskDescription;
	}
	
	public DateTime getTaskEndTime() {
		return this.endTime;
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
		return new StringBuffer(" Task ID :").append(this.taskID.toString())
						.append(" Task Name : ").append(this.taskDescription)
		                .append(" Task Description : ").append(this.taskDescription).toString();
	}
}
