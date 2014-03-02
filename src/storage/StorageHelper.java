package storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.DateTimeTypeConverter;

import core.Task;

public class StorageHelper {

	protected static final String FILENAME = "data.json";
	public static final String ERROR_FILE_CREATION = "Error in file creation";
	public static final String ERROR_TASK_ADDITION = "Error in task addition";

	private Gson gson;
	private File file;
	
	public StorageHelper() {
		this.file = createOrGetFile(FILENAME);
		this.gson = new GsonBuilder()
			.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
			.enableComplexMapKeySerialization()
			.create();
	}

	public void addNewTask(Task t) {
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(this.file), "UTF-8")) {
//			gson.toJson(t, writer);
			System.out.println(this.gson.toJson(t));
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
}


