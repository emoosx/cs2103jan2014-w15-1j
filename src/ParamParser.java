/*
 * ParamParser utilizes the JCommander Framework to parse user inputs according to specific tags
 * 
 */

import java.util.ArrayList;

import com.beust.jcommander.*;

public class ParamParser {

	@Parameter(description = "Words that are not needed")
	private ArrayList<String> files = new ArrayList<String>();

	// Stub
	@Parameter(names = "-priority", description = "priority of tasks")
	private String priority = null;

	/**************************** Add Parameters ****************************/
	@Parameter(names = "-from", description = "getting start time")
	private String startTime = null;

	@Parameter(names = "-to", description = "getting end time")
	private String endTime = null;

	@Parameter(names = { "-on", "-by" }, description = "getting deadline")
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

	/****** Edit Parameters ****/
	@Parameter(names = { "-editdesc", "-ed" }, description = "Editing of task")
	private String editDesc;

	public String getDesc() {
		return editDesc;
	}

	@Parameter(names = { "-editname", "-en" }, description = "Editing of task")
	private String editName;

	public String getName() {
		return editName;
	}

	@Parameter(names = { "-editdate", "-date" }, description = "Editing of date")
	private String editDate;

	public String getDate() {
		return editDate;
	}
}
