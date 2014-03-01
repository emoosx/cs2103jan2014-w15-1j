/* Task Panda v0.1
 * Created on 24/2/2014
 * Contributors: 
 * Clement Chong - A0097784H
 * Kuang Htet - A0105860L
 * Tan Zheng Jie (Matthew) - A0101810A
 */

import java.util.*;

public class PandaMain {
	
	private static final String MESSAGE_WELCOME = "Welcome to Task Panda v0.1!";
	
	public static void main(String[] args) {
		showToUser(MESSAGE_WELCOME);
		while(true) {
			try {
				String userInput = readUserInput();
				Parser parser = new Parser();
				parser.parseUserInput(userInput);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// This method print out the given argument
	private static void showToUser(String outputString) {
		System.out.println(outputString);
	}
	
	// This method will read in 1 line of user input
	private static String readUserInput() {
		Scanner sc = new Scanner(System.in);
		String userInput = sc.nextLine();
		sc.close();
		return userInput;
	}
}
