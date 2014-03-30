package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.AbstractMap.SimpleEntry;
import java.util.Stack;

import logic.Command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import common.PandaLogger;

import java.util.AbstractMap;

/* A singleton class to handle the persistence of Command objects
 * to support Redo feature
 */
public class RedoStorage {

	public static RedoStorage INSTANCE = new RedoStorage();
	public static final String FILENAME = "redoData.json";

	private static final String ERROR_FILE_CREATION = "Error in file creation of " + FILENAME;
	private static final String ERROR_COMMAND_WRITE = "Error in writing commands to " + FILENAME;
	private static final String ERROR_FILE_IO = "Error in File IO";

	private Gson gson;
	private File file;

	private RedoStorage() {
		this.file = createOrGetFile(FILENAME);
		this.gson = new GsonBuilder()
				.enableComplexMapKeySerialization().create();
	}
	
	public void writeCommands(Stack<SimpleEntry<Integer, Command>> c) {
		PandaLogger.getLogger().info("writeCommands");
		PandaLogger.getLogger().info("Total Commands to DB:" + c.size());
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(
				this.file), "UTF-8")) {
			gson.toJson(c, writer);
		} catch(IOException e) {
			throw new Error(ERROR_COMMAND_WRITE);
		}
	}
	
	public void writeCommands(Stack<SimpleEntry<Integer, Command>> c, File f) {
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(
				f), "UTF-8")) {
			gson.toJson(c, writer);
		} catch(IOException e) {
			throw new Error(ERROR_COMMAND_WRITE);
		}
	}

	public Stack<SimpleEntry<Integer, Command>> getAllCommands() {
		PandaLogger.getLogger().info("getAllCommands");
		Stack<SimpleEntry<Integer, Command>> commands = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.file));
			commands = this.gson.fromJson(br,  new TypeToken<Stack<SimpleEntry<Integer, Command>>>() {
			}.getType());
			if(commands == null) {
				return new Stack<SimpleEntry<Integer, Command>>();
			}
			PandaLogger.getLogger().info("Total Commands From DB:" + commands.size());
		} catch(Exception e) {
			throw new Error(ERROR_FILE_IO);
		}
		return commands;
	}
	
	public Stack<SimpleEntry<Integer, Command>> getAllCommands(File file) {
		PandaLogger.getLogger().info("getAllCommands");
		Stack<SimpleEntry<Integer, Command>> commands = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			commands = this.gson.fromJson(br,  new TypeToken<Stack<SimpleEntry<Integer, Command>>>() {
			}.getType());
			if(commands == null) {
				return new Stack<SimpleEntry<Integer, Command>>();
			}
			PandaLogger.getLogger().info("Total Commands From DB:" + commands.size());
		} catch(Exception e) {
			e.printStackTrace();
			throw new Error(ERROR_FILE_IO);
		}
		return commands;
	}
	
	private File createOrGetFile(String filename) {
		file = new File(filename);
		if (!file.isFile()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new Error(ERROR_FILE_CREATION);
			}
		}
		return file;
	}

	public void clearFile() {
		file.delete();
		this.file = createOrGetFile(FILENAME);
	}
}