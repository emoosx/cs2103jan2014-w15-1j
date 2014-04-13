package common;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/* PandaLogger 
 * 
 * For logging purposes. Set it up for file and console logging.
 * 
 * LVL_LOG_CONSOLE_LVL = Level of logging to output to console
 * LVL_LOG_LVL = Level of logging to external log file
 */

public class PandaLogger {
	
	private static final String PANDA_LOG_FILE = "panda.log";
	private static final String LOGGER_NAME = "log_file";
	
	private static final Level LVL_LOG_CONSOLE_LVL = Level.INFO;
	private static final Level LVL_LOG_LVL = Level.ALL;
	
	private static PandaLogger instance = null;
	private static FileHandler fileHandler;
	private static ConsoleHandler consoleHandler;
	private static Logger logger;

	public static Logger getLogger() {
		if(instance == null) {
			instance = new PandaLogger();
		}
		return logger;
	}
	private PandaLogger() {
		try {
			setUpLogger();
			setUpFileHandler();
			setUpConsoleHandler();
		} catch(SecurityException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setUpLogger() {
		logger = Logger.getLogger(LOGGER_NAME);
		logger.setLevel(LVL_LOG_LVL);
		logger.setUseParentHandlers(false);
	}
	
	private void setUpFileHandler() throws IOException {
		fileHandler = new FileHandler(PANDA_LOG_FILE);
		logger.addHandler(fileHandler);
	}
	
	private void setUpConsoleHandler() {
		consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(LVL_LOG_CONSOLE_LVL);
		logger.addHandler(consoleHandler);
	}
}
