package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Stack;

import logic.Command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import common.PandaLogger;

/* A singleton class to handle the persistence of Command objects
 * to support Undo/Redo feature
 */
public class UndoStorage {

	public static UndoStorage INSTANCE = new UndoStorage();
	public static final String FILENAME = "undoData.json";

	private static final String ERROR_FILE_CREATION = "Error in file creation of " + FILENAME;
	private static final String ERROR_COMMAND_WRITE = "Error in writing commands to " + FILENAME;
	private static final String ERROR_FILE_IO = "Error in File IO";

	private Gson gson;
	private File file;

	private UndoStorage() {
		this.file = createOrGetFile(FILENAME);
		this.gson = new GsonBuilder()
				.enableComplexMapKeySerialization().create();
	}
	
	public void writeCommands(Stack<Command> c) {
		PandaLogger.getLogger().info("writeCommands");
		PandaLogger.getLogger().info("Total Commands to DB:" + c.size());
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(
				this.file), "UTF-8")) {
			gson.toJson(c, writer);
		} catch(IOException e) {
			throw new Error(ERROR_COMMAND_WRITE);
		}
	}
	
	public void writeCommands(Stack<Command> c, File f) {
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(
				f), "UTF-8")) {
			gson.toJson(c, writer);
		} catch(IOException e) {
			throw new Error(ERROR_COMMAND_WRITE);
		}
	}

	public Stack<Command> getAllCommands() {
		PandaLogger.getLogger().info("getAllCommands");
		Stack<Command> commands = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.file));
			commands = this.gson.fromJson(br, new TypeToken<Stack<Command>>(){
			}.getType());
			if(commands == null) {
				return new Stack<Command>();
			}
			PandaLogger.getLogger().info("Total Commands From DB:" + commands.size());
		} catch(Exception e) {
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