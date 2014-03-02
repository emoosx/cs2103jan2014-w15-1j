package storage;

import core.Task;

public class Blah {
	public static void main(String[] args) {
		StorageHelper storage = new StorageHelper();
		Task newTask = new Task("test Task", "woowhooo");
		storage.addNewTask(newTask);
	}
}
