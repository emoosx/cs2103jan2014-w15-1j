/*
 * ParamParser utilizes the JCommander Framework to parse user inputs according to specific tags
 * 
 */

import com.beust.jcommander.*;

public class ParamParser {
	
	// Stub
	@Parameter (names = "-priority", description = "priority of tasks")
	private String priority = null;
	
	/**************************** Add Parameters ****************************/
	
	@Parameter (names = "from", description = "getting start time")
	private String startTime = null;
	
	@Parameter (names = "to", description = "getting end time")
	private String endTime = null;
	
	@Parameter (names = { "on", "by" }, description = "getting deadline")
	private String endDate = null;

	public String getStartTime() {
		return startTime;
	}
	
	public String getEndTime() {
		return endTime;
	}	
	public String getEndDate() {
		return endDate;
	}	
}
