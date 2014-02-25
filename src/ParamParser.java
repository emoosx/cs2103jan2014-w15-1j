/*
 * ParamParser utilizes the JCommander Framework to parse user inputs according to specific tags
 * 
 */

import com.beust.jcommander.*;

public class ParamParser {
	
	// Stub
	@Parameter (names = "-priority", description = "priority of tasks")
	private String priority = null;
	
	@Parameter (names = "by", description = "getting deadline date")
	private String endDate = null;
	
	public String getEndDate() {
		return endDate;
	}
}
