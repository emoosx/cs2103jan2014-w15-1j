package storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.Gson;

import core.Task;

public class StorageHelper {

	protected static Gson gson;
	protected static Writer writer;
	
	protected static final String FILENAME = "data.json";
	
	public static final String ERROR_FILE_CREATION = "Error in file creation";
	public static final String ERROR_TASK_ADDITION = "Error in task addition";
	

	private File file;
	
	public StorageHelper() {
		this.file = createOrGetFile(FILENAME);
	}

	public void addNewTaskTest(Task t) {
		gson = new Gson();
		String json = gson.toJson(t);
		System.out.println(json);
	}
	public void addNewTask(Task t) {
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(this.file), "UTF-8")) {
			gson = new Gson();
			gson.toJson(t, writer);
		} catch(IOException e) {
			throw new Error(ERROR_TASK_ADDITION);
		}
	}
	
	private File createOrGetFile(String filename) {
		file = new File(filename);
		if(!file.isFile()) {
			try {
				file.createNewFile(); 
			} catch(IOException e) {
				throw new Error(ERROR_FILE_CREATION);
			}
		}
		return file;
	}
	
	private void writeToFile() {
	}
}


