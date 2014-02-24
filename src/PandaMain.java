/* Task Panda v0.1
 * Created 24/2/2014
 * Contributors: Clement, Htet Kuang, Matthew
 */

import java.util.*;

public class PandaMain {
	
	private static final String MESSAGE_WELCOME = "Welcome to Task Panda v0.0!";
	
	public static void main(String[] args) {
		showToUser(MESSAGE_WELCOME);
		while(true) {
			String feedback = readUserCommand();
			showToUser(feedback);
		}
	}
	
	private static void showToUser(String outputString) {
		System.out.println(outputString);
	}
	
	private static String readUserCommand() {
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}
}
