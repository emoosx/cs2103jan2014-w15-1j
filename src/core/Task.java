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
	protected String taskName;
	protected String taskDescription;
	protected ArrayList<String> taskTags;			// tags are in lowercase
	protected DateTime taskDeadline;
	private DateTime taskCreatedTimestamp; 
	
	// Constructor method
	public Task(String tName, String tDesc){
		this.taskID = UUID.randomUUID();
		this.taskName = tName;
		this.taskDescription = tDesc;
		this.taskTags = new ArrayList<String>();
		this.taskCreatedTimestamp = new DateTime();
	}
	
	// task without any deadline
	public Task(String tName) {
		this.taskID = UUID.randomUUID();
		this.taskName = tName;
		this.taskDescription = null;
		this.taskTags = new ArrayList<String>();
		this.taskCreatedTimestamp = new DateTime();
	}
	
	public Task(String tName, DateTime taskDeadline) {
		this.taskID = UUID.randomUUID();
		this.taskName = tName;
		this.taskDeadline = taskDeadline;
		this.taskTags = new ArrayList<String>();
		this.taskCreatedTimestamp = new DateTime();
		this.taskDeadline = taskDeadline;
	}
	
	
	public UUID getTaskID() {
		return this.taskID;
	}

	public void setTaskName(String tName){
		this.taskName = tName;
	}
	
	public String getTaskName(){
		return this.taskName;
	}
	
	public void setTaskDescription(String tDesc){
		this.taskDescription = tDesc;
	}
	
	public String getTaskDescription(){
		return this.taskDescription;
	}
	
	public DateTime getTaskDeadline() {
		return this.taskDeadline;
	}
	
	public void setTaskTags(ArrayList<String> tags) {
		this.taskTags = tags;
	}
	
	public void addNewTag(String newTag) {
		this.taskTags.add(newTag.trim().toLowerCase());
	}
	
	// Override method for toString
	public String toString() {
		return new StringBuffer(" Task ID :").append(this.taskID.toString())
						.append(" Task Name : ").append(this.taskName)
		                .append(" Task Description : ").append(this.taskDescription).toString();
	}
}
