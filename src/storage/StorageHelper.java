package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import logic.CommandFactory;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.DateTimeTypeConverter;
import common.PandaLogger;
import core.Task;

public class StorageHelper {

	// A singleton class
	public static StorageHelper INSTANCE = new StorageHelper();

	protected static final String FILENAME = "data.json";
	public static final String ERROR_FILE_CREATION = "Error in file creation";
	public static final String ERROR_TASK_ADDITION = "Error in task addition";
	public static final String ERROR_FILE_IO = "Error in File IO";

	private Gson gson;
	private File file;
	
	private StorageHelper() {
		this.file = createOrGetFile(FILENAME);
		this.gson = new GsonBuilder()
			.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
			.enableComplexMapKeySerialization()
			.create();
	}

	public void writeTasks(List<Task> t) {
		System.out.println(t.size());
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(this.file), "UTF-8")) {
			gson.toJson(t, writer);
		} catch(IOException e) {
			throw new Error(ERROR_TASK_ADDITION);
		}
	}
	
	
//	public ArrayList<Task> getAll() {
//		try(Reader reader = new InputStreamReader(JsonToJava.class.getResourceAsStream(this.file), "UTF-8")) {
//			Gson gson = new GsonBuilder().create();
//		} catch(IOException e) {
//			throw new Error(ERROR_FILE_IO);
//		}
//	}

	public ArrayList<Task> getAllTasks() {
        PandaLogger.getLogger().info("getAllTasks");
		ArrayList<Task> tasks = null;
		try{
            BufferedReader br = new BufferedReader(new FileReader(this.file));
            tasks = this.gson.fromJson(br, new TypeToken<List<Task>>(){}.getType());
            PandaLogger.getLogger().info(String.valueOf(tasks.size()));
        } catch(Exception e) {
        	throw new Error(ERROR_FILE_IO);
        }
		return tasks;
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
	
	public void clearFile(){
		file.delete();
		this.file = createOrGetFile(FILENAME);
	}
}