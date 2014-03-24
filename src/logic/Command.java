package logic;

public class Command {
	
	private final int INDEX_COMMAND = 0;
	private final int INDEX_RAWTEXT = 1;

	public String rawText;
	public COMMAND_TYPE command;

	public enum COMMAND_TYPE {
		ADD, LIST, EDIT, UNDO, ARCHIVE, CLEAR, DONEALL, ARCHIVEALL, DELETE, HELP, INVALID
	}

	// Constructor Method
	public Command(String commandText) {
		this.rawText = commandText;
		this.command = COMMAND_TYPE.INVALID;
		this.parse();
	}
	
	/*
	 * Attempts to auto complete a command based on supplied string
	 * TODO
	 */
	public static String attemptAutoComplete(String s) {
		return s;
	}
	
	private void parse() {
		String[] rawText = stripCommand(this.rawText);
		this.rawText = rawText[INDEX_RAWTEXT];
		this.command = determineCommandType(rawText[INDEX_COMMAND]);
	}

	// Method splits raw user input into command and user input
	private String[] stripCommand(String commandText) {
		assert(commandText != null);
		String cmd, rawText;
		int spaceIndex = commandText.indexOf(' ');
		if(spaceIndex > 0) {
			cmd = commandText.substring(0, spaceIndex);
			rawText = commandText.substring(spaceIndex+1).trim();
			if (rawText.equals("")) {
				rawText = null;
			}
		} else {
			cmd = commandText.trim(); 
			rawText = null;
		}
		String[] ans = {cmd, rawText};
		return ans;
	}

	// Method will determine command type given user command
	private COMMAND_TYPE determineCommandType(String command) {
		assert(command != null);
		if(command.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (command.equalsIgnoreCase("list")) {
			return COMMAND_TYPE.LIST;
		} else if (command.equalsIgnoreCase("edit")) {
			return COMMAND_TYPE.EDIT;
		} else if (command.equalsIgnoreCase("undo")) {
			return COMMAND_TYPE.UNDO;
		} else if (command.equalsIgnoreCase("archive")) {
			return COMMAND_TYPE.ARCHIVE;
		} else if (command.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (command.equalsIgnoreCase("doneall")) {
			return COMMAND_TYPE.DONEALL;
		} else if (command.equalsIgnoreCase("archiveall")) {
			return COMMAND_TYPE.ARCHIVEALL;
		} else if (command.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		}
		return COMMAND_TYPE.INVALID; 
	}
	
	public String toString() {
		return this.rawText;
	}
}
