public class TaskDelete {
	private static final String MESSAGE_INVALID_DELETE = "Usage: delete <number>";
	private static final String MESSAGE_INVALID_NUMBERFORMAT = "Please key in an integer";
	private static final String MESSAGE_INVALID_NUMBERSIGN = "Please key in a positive number";
	private static final String MESSAGE_INVALID_NUMBER = "Please choose a lower value";
	private static final String MESSAGE_DELETED = "deleted : \"%s\"";
	private static final String MESSAGE_EMPTY = "file is empty";

	private static Integer DELETE_PARA = 0;
	private static Integer DELETE_OFFSET = 1;
	

	// To be coded by Clement
	/*
	 * How it works: - search for task id - no task found > return error -delete
	 * task object - delete task successful update gson
	 */
	public void execute(String inputNumber) {
		if (checkDeleteInput(inputNumber)) {
			int lineToRemove = Integer.parseInt(inputNumber) - DELETE_OFFSET;
			 String deletedString = dataList.get(lineToRemove);
			 dataList.remove(lineToRemove);
			 showToUser(String.format(MESSAGE_DELETED, deletedString));
		}
	}

	// Method to check delete parameter
	private boolean checkDeleteInput(String inputNumber) {
		// No argument input
		if (isValidString(inputNumber)) {
			showToUser(MESSAGE_INVALID_DELETE);
			return false;
		}
		/*
		 * Checks if argument fulfill the delete parameters, is a positive non
		 * zero integer and whether the number specified is within the array
		 * size
		 */
		String[] stringArray = inputNumber.split(" ");
		if (stringArray.length != DELETE_PARA) {
			showToUser(MESSAGE_INVALID_DELETE);
			return false;
		} else if (!isPositiveNonZeroInt(inputNumber)) {
			return false;
		} else if (!checkIfNumberBelowArraySize(inputNumber)) {
			return false;
		}
		return true;
	}

	// Need to edit arraylist name
	private boolean checkIfNumberBelowArraySize(String n) {
		if (checkIfFileIsEmpty()) {
			showToUser(MESSAGE_EMPTY);
			return false;
		}
		try {
			int num = Integer.parseInt(n) - 1;
			 if (num >= dataList.size()) {
			showToUser(MESSAGE_INVALID_NUMBER);
			return false;
			 }
			 return true;
		} catch (NumberFormatException nfe) {
			showToUser(MESSAGE_INVALID_NUMBERFORMAT);
			return false;
		}
	}

	// Method to check for number >0
	private boolean isPositiveNonZeroInt(String lineNumber) {
		try {
			int i = Integer.parseInt(lineNumber);
			if (i > 0) {
				return true;
			} else {
				showToUser(MESSAGE_INVALID_NUMBERSIGN);
				return false;
			}
		} catch (NumberFormatException nfe) {
			showToUser(MESSAGE_INVALID_NUMBERFORMAT);
			return false;
		}
	}

	// Method will print out given argument
	private void showToUser(String outputString) {
		System.out.println(outputString);
	}

	// Method checks if data list is empty
	// Arraylist name to be edited accordingly
	private boolean checkIfFileIsEmpty() {
		 if (dataList.isEmpty()) {
		return true;
		 }
		 return false;
	}

	// Method checks if inputString is not empty
	private boolean isValidString(String inputString) {
		if (inputString.trim().length() == 0) {
			return false;
		}
		return true;
	}
}
