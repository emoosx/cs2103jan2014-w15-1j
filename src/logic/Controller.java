package logic;

public class Controller {

	private CommandFactory cf;
	
	public Controller() {
		this.cf = CommandFactory.INSTANCE;
	}
}
