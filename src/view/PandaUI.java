package view;

import java.io.File;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Command;
import logic.Command.COMMAND_TYPE;
import logic.CommandFactory;
import core.Task;

public class PandaUI extends Application {
	
	// input field
	private static final String IF_PLACEHOLDER = "Get Busy!";
	private static final int IF_HEIGHT = 70; 
	private static final int IF_WIDTH = 500;
	private static final String IF_ID = "inputField";
	private static final int PADDING = 10;
	
	private static final int OFFSET = 1;
	private static final int COMMAND_INDEX = 0;
	
	// listview
	private static final String LIST_ID = "tasklist";
	// app
	private static final int APP_WIDTH = 500;
	private static final int APP_HEIGHT = 450;
	private static final String CSS_PATH = "resources/css/style.css";
	
	CommandFactory commandFactory = CommandFactory.INSTANCE;
//	ObservableList<Task> tasks = FXCollections.observableArrayList(commandFactory.getTasks());
	ObservableList<Task> tasks = commandFactory.getDisplayTasks();
	ListView<Task> list = new ListView<Task>();
	TextField inputField;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
        BorderPane border = new BorderPane();
        border.setTop(addInputField());
        border.setCenter(addList());
        
        Scene scene = new Scene(border, APP_WIDTH, APP_HEIGHT);
        File file = new File(CSS_PATH);
        scene.getStylesheets().add("file:///" + file.getAbsolutePath());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
	}
	
	private HBox addInputField() {
		HBox hbox = new HBox();
		inputField = new TextField();
		inputField.setPromptText(IF_PLACEHOLDER);
		inputField.setPrefColumnCount(50);
		inputField.setPrefSize(IF_WIDTH, IF_HEIGHT);
		inputField.setId(IF_ID);
		inputField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				handleSearch(oldValue, newValue);
			}
		});
		
		inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if(e.getCode() == KeyCode.ENTER) {
					Command command = new Command(inputField.getText());
					if(command.command == COMMAND_TYPE.INVALID) {
						// invalid command
						System.out.println("Invalid command");
					} else{
						commandFactory.process(command);
						updateTasksList();
						inputField.clear();
						list.scrollTo(tasks.size()-OFFSET);
					}
				}
			}
		});
		
		hbox.getChildren().addAll(inputField);
		return hbox;
	}
	
	private VBox addList() {

		VBox vbox = new VBox();
		vbox.setSpacing(8);
		
        list.setItems(tasks);
        list.setId(LIST_ID);
        list.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
        	@Override
        	public ListCell<Task> call(ListView<Task> param) {
        		return new TaskCell();
        	}
        });
        vbox.getChildren().addAll(list);
        return vbox;
	}
	
	private void handleSearch(String oldValue, String newValue) {
		// user pressed delete and reverse back to the old list
		if(oldValue != null && (newValue.length() < oldValue.length())) {
			updateTasksList();
		}
		
		String[] pieces  = newValue.split("\\s+");
		if((!pieces[COMMAND_INDEX].equalsIgnoreCase(COMMAND_TYPE.SEARCH.name())) || pieces.length <= 1) {
			updateTasksList();
		} else {
			newValue = pieces[1];
		
		

		String[] parts = newValue.toLowerCase().split(" ");
		
		
		
		// create a temporary subentries matching list and replace it
		ObservableList<Task> subentries = FXCollections.observableArrayList();
		for(Task task: list.getItems()) {
			boolean match = true;
			for(String part: parts) {
				if(!task.getTaskDescription().toLowerCase().contains(part)) {
					match = false;
					break;
				}
			}
			if(match) {
				subentries.add(task);
			}
		}
		
		list.setItems(subentries);
		}
		
	}
	private void updateTasksList() {
		tasks = commandFactory.getDisplayTasks();
//		tasks = FXCollections.observableArrayList(commandFactory.getDisplayTasks());
		list.setItems(tasks);
	}
}
