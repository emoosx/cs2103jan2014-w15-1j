package view;

import java.io.File;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Command;
import logic.Command.COMMAND_TYPE;
import logic.CommandFactory;
import core.Task;

public class PandaUI extends Application {

	// app
	public static final int APP_WIDTH = 450;
	public static final int APP_HEIGHT = 600;
	private static final String CSS_PATH = "resources/css/style.css";

	// input field
	private static final String IF_PLACEHOLDER = "Get Busy!";
	private static final int IF_HEIGHT = 70;
	private static final int IF_WIDTH = APP_WIDTH;
	private static final String IF_ID = "inputField";

	private static final int PADDING = 20;

	private static final int OFFSET = 1;
	private static final int COMMAND_INDEX = 0;

	// listview
	private static final String LIST_ID = "tasklist";

	private static final String OVERDUE_TXT = "You have %d overdue task(s).";
	private static final String OVERDUE_ID = "overdue-label";
	private static final String OVERDUE_VBOX_ID = "overdue-vbox";
	private static final int OVERDUE_HEIGHT = 40;
	
	private static final String INVALID_COMMAND = "Invalid Command! Type \"Help\" for Manual";

	CommandFactory commandFactory = CommandFactory.INSTANCE;
	ObservableList<Task> tasks = commandFactory.getDisplayTasks();
	ObservableList<Task> overduetasks = commandFactory.getOverdueTasks();

	ListView<Task> list = new ListView<Task>();
	Label overdueLabel = new Label();
//	Label errorLabel = new Label(INVALID_COMMAND);
	Text overdueText = new Text();
	Tooltip tooltip = new Tooltip(INVALID_COMMAND);

	TextField inputField;
	
	VBox bottomBox;
	VBox overBox = addOverStatus();
	VBox taskBox = addTaskList();
	VBox helpBox = addHelpText();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		BorderPane border = new BorderPane();
		border.setTop(addInputField(primaryStage));
		border.setCenter(addBottomComponents());

		Scene scene = new Scene(border, APP_WIDTH, APP_HEIGHT);
		File file = new File(CSS_PATH);
		scene.getStylesheets().add("file:///" + file.getAbsolutePath());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private HBox addInputField(final Stage stage) {
		HBox hbox = new HBox();
		
		inputField = new TextField();
		inputField.setPromptText(IF_PLACEHOLDER);
		inputField.setPrefColumnCount(50);
		inputField.setPrefSize(IF_WIDTH, IF_HEIGHT);
		inputField.setId(IF_ID);
		inputField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				handleSearch(oldValue, newValue);
			}
		});

		inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					if (inputField.getText().length() > 0) {
						Command command = new Command(inputField.getText());
						if (command.command == COMMAND_TYPE.INVALID) {
							// invalid command
							inputField.setTooltip(tooltip);
							overdueLabel.textProperty().unbind();
							overdueLabel.setText(INVALID_COMMAND);
						} else {
                            inputField.setTooltip(null);
							commandFactory.process(command);
							updateTasksList();
                            overdueLabel.textProperty().bind(Bindings.format(OVERDUE_TXT, Bindings.size(overduetasks)));
							inputField.clear();
							if(command.command == COMMAND_TYPE.HELP) {
								// show help text
								bottomBox.getChildren().remove(taskBox);
								bottomBox.getChildren().add(helpBox);
							} else {
								// remove help text
								bottomBox.getChildren().remove(helpBox);
								bottomBox.getChildren().add(taskBox);
							    if (command.command == COMMAND_TYPE.ADD) {
								    list.scrollTo(tasks.size() - OFFSET);
							    }
								
							}
						}
					}
				} else if(e.getCode() == KeyCode.DOWN) {
					// select the first cell in the task list
				}
			}
		});

		hbox.getChildren().addAll(inputField);
		return hbox;
	}

	private VBox addBottomComponents() {
		bottomBox = new VBox();
		bottomBox.getChildren().addAll(overBox, taskBox);
		return bottomBox;
	}
	
	private VBox addHelpText() {
		helpBox = new VBox();
		Label title = new Label("TaskPanda Help Manual");
		Label helpText = new Label(
			"add <description> \n" + 
		    "add <description> <timestamp> \n" +
		    "add <description> from <timestamp> to <timestamp> \n\n" +
		    
		    "list \n" +
		    "list floating \n" +
		    "list timed \n" +
		    "list deadline \n\n" +

		    "list today \n" +
		    "list tomorrow \n" +
		    "list this week \n" +
		    "list <date> \n\n" +
		    
		    "edit <id> <description> <timestamp> \n\n" +
		    
		    "done <id> \n" +
		    "undone <id> \n\n" +
		    
		    "undo \n\n" + 
		    "redo \n\n" + 

		    "clear \n\n" + 
		    
		    "search <keyword> \n"
		);
		title.setPadding(new Insets(PADDING, 0, 0, PADDING));
		helpText.setPadding(new Insets(PADDING));
		helpBox.getChildren().addAll(title, helpText);
		return helpBox;
	}

	private VBox addOverStatus() {
		VBox overBox = new VBox();
		overBox.setId(OVERDUE_VBOX_ID);
		overdueLabel.setId(OVERDUE_ID);
		overdueLabel.setAlignment(Pos.CENTER);
		overdueLabel.setPrefWidth(APP_WIDTH);
		overdueLabel.setPrefHeight(OVERDUE_HEIGHT);
		overdueLabel.textProperty().bind(Bindings.format(OVERDUE_TXT, Bindings.size(overduetasks)));
		overBox.getChildren().addAll(overdueLabel);
		return overBox;
	}

	private VBox addTaskList() {
		VBox taskBox = new VBox();
		list.setItems(tasks);
		list.setId(LIST_ID);
		list.setPrefHeight(APP_HEIGHT-(IF_HEIGHT + OVERDUE_HEIGHT));
		list.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
			@Override
			public ListCell<Task> call(ListView<Task> param) {
				TaskCell taskcell = new TaskCell();
				return taskcell;
			}
		});
		taskBox.getChildren().addAll(list);
		return taskBox;
	}

	private void handleSearch(String oldValue, String newValue) {
		// user pressed delete and reverse back to the old list
		if (oldValue != null && (newValue.length() < oldValue.length())) {
			updateTasksList();
		}

		String[] pieces = newValue.split("\\s+");
		if ((!pieces[COMMAND_INDEX]
				.equalsIgnoreCase(COMMAND_TYPE.SEARCH.name()))
				|| pieces.length <= 1) {
			updateTasksList();
		} else {
			newValue = pieces[1];
			String[] parts = newValue.toLowerCase().split(" ");
			// create a temporary subentries list matching list and replace it
			ObservableList<Task> subentries = FXCollections.observableArrayList();
			for (Task task : list.getItems()) {
				boolean match = true;
				for (String part : parts) {
					if (!task.getTaskDescription().toLowerCase().contains(part)) {
						match = false;
						break;
					}
				}
				if (match) {
					subentries.add(task);
				}
			}

			list.setItems(subentries);
		}

	}

	private void updateTasksList() {
		tasks = commandFactory.getDisplayTasks();
		overduetasks = commandFactory.getOverdueTasks();
		list.setItems(tasks);
	}
}
