package logic;

public class Command {
	
	private final int INDEX_COMMAND = 0;
	private final int INDEX_RAWTEXT = 1;

	public String rawText;
	public COMMAND_TYPE command;

	public enum COMMAND_TYPE {
		ADD ("add"), 
		LIST ("list"), 
		EDIT ("edit"), 
		UNDO ("undo"), 
		ARCHIVE ("archive"), 
		CLEAR ("clear"), 
		DONEALL ("doneall"), 
		ARCHIVEALL ("archiveall"), 
		DELETE ("delete"), 
		HELP ("help"), 
		INVALID ("invalid");
		
		private String commandName;

		private COMMAND_TYPE(String commandName) {
			this.commandName = commandName;
		}
		
		private String getCommandName() {
			return commandName;
		}
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

	/* splits raw user input into (Command + user input) */
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
		if(command.equalsIgnoreCase(COMMAND_TYPE.ADD.getCommandName())) {
			return COMMAND_TYPE.ADD;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.LIST.getCommandName())) {
			return COMMAND_TYPE.LIST;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.EDIT.getCommandName())) {
			return COMMAND_TYPE.EDIT;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.UNDO.getCommandName())) {
			return COMMAND_TYPE.UNDO;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.ARCHIVE.getCommandName())) {
			return COMMAND_TYPE.ARCHIVE;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.CLEAR.getCommandName())) {
			return COMMAND_TYPE.CLEAR;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.DONEALL.getCommandName())) {
			return COMMAND_TYPE.DONEALL;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.ARCHIVEALL.getCommandName())) {
			return COMMAND_TYPE.ARCHIVEALL;
		} else if (command.equalsIgnoreCase(COMMAND_TYPE.DELETE.getCommandName())) {
			return COMMAND_TYPE.DELETE;
		}
		return COMMAND_TYPE.INVALID; 
	}

	public String toString() {
		return command.getCommandName() + " " + this.rawText;
	}
}
