
public class Task {

	private String taskName;
	private String taskDescription;
	//private Date time;
	//private String priority;
	
	public Task(String tName, String tDesc){
		this.taskName = tName;
		this.taskDescription = tDesc;
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
	public String toString() {
		
		        return new StringBuffer(" Task Name : ").append(this.taskName)
		
		                .append(" Task Description : ").append(this.taskDescription).
		
		               toString();
		
		    }

	
}
