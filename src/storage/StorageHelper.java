package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import common.DateTimeTypeConverter;

import core.Task;

public class StorageHelper {

	protected static final String FILENAME = "data.json";
	private static final String JSON_INDENT = "  ";
	private static final String CHAR_ENCODING = "UTF-8";

	public static final String ERROR_FILE_CREATION = "Error in file creation";
	public static final String ERROR_TASK_ADDITION = "Error in task addition";
	public static final String ERROR_JSON_FILE_CORRUPTED = "Error in json file: file seems to be corrupted";

	private Gson gson;
	private File file;
	private InputStream inputStream;
	private OutputStream outputStream;
	private JsonWriter writer;
	private JsonReader reader;
	private Map<String, Task> taskList;
	
	public StorageHelper() {
		this.taskList = new HashMap<String, Task>();
		this.file = createOrGetFile(FILENAME);
		this.gson = new GsonBuilder()
			.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
			.enableComplexMapKeySerialization()
			.create();
	}

//	public void addNewTask(Task t) {
//		try(Writer writer = new OutputStreamWriter(new FileOutputStream(this.file), "UTF-8")) {
//			gson.toJson(t, writer);
//			System.out.println(this.gson.toJson(t));
//		} catch(IOException e) {
//			throw new Error(ERROR_TASK_ADDITION);
//		}
//	}
	
	public void addNewTask(Task t) {
		Map<String, Task> taskList = getAllTasks();
		taskList.put(t.getTaskID().toString(), t);
		writeToFile(taskList);
	}
	
	private void writeToFile(Map<String, Task> taskList) {
		openJsonOutputStream();
		writer.setIndent(JSON_INDENT);
		try {
		writer.beginArray();
		for(Map.Entry<String, Task> entry : taskList.entrySet()) {
			Task task = entry.getValue();
			gson.toJson(task, task.getClass(), writer);
		}
		writer.endArray();
		} catch(IOException e) {
			throw new Error("Error in I/O");
		}
		closeJsonOutputStream();
	}
	
	public Map<String, Task>  getAllTasks() {
		openJsonInputStream();
		taskList = populateTaskList();
		closeJsonInputStream();
		return taskList;
	}
	
	private JsonArray populateJsonArray() {
		JsonParser parser = new JsonParser();
		JsonArray jArray = null;
		try {
			jArray = parser.parse(reader).getAsJsonArray();
            return jArray;
		} catch(JsonSyntaxException e) {
			throw new Error(ERROR_JSON_FILE_CORRUPTED);
		} catch(IllegalStateException e) {
			throw new Error("Illegal State");
		}
	}
	
	private Map<String, Task> populateTaskList() {
		JsonArray jArray = populateJsonArray();
		for(JsonElement obj: jArray) {
			if(obj != null) {
				Task newTask = gson.fromJson(obj, Task.class);
				taskList.put(newTask.getTaskID().toString(), newTask);
			}
		}
		return taskList;
		
	}
	
	private void openJsonInputStream()  {
		try {
		inputStream = new FileInputStream(FILENAME);
		reader = new JsonReader(new InputStreamReader(inputStream, CHAR_ENCODING));
		} catch(Exception e) {
			throw new Error("Error opening input stream");
		}
	}
	
	private void closeJsonInputStream() {
		try {
		reader.close();
		inputStream.close();
		} catch(IOException e) {
			throw new Error("Error in I/O");
		}
	}
	
	private void openJsonOutputStream() {
		try{
		outputStream = new FileOutputStream(FILENAME);
		writer = new JsonWriter(new OutputStreamWriter(outputStream, CHAR_ENCODING));
		} catch(Exception e) {
			throw new Error("Error opening output stream");
		}
	}
	
	private void closeJsonOutputStream() {
		try {
		writer.close();
		outputStream.close();
		} catch(IOException e) {
			throw new Error("Error in I/O");
		}
	}
	
	private File createOrGetFile(String filename) {
		file = new File(filename);
		if(!file.isFile()) {
			try {
				file.createNewFile(); 
				openJsonOutputStream();
				writer.setIndent(JSON_INDENT);
				writer.beginArray();
				writer.endArray();
				closeJsonOutputStream();
			} catch(IOException e) {
				throw new Error(ERROR_FILE_CREATION);
			}
		}
		return file;
	}
}


