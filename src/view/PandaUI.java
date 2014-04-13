package view;


import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import javax.swing.KeyStroke;

import logic.Command;
import logic.Command.COMMAND_TYPE;
import logic.CommandFactory;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import core.Task;

public class PandaUI extends Application {

	// app
	public static final String APP_TITLE = "TaskPanda";
	public static final String APP_ICON = "panda.png";
	public static final int APP_WIDTH = 500;
	public static final int APP_HEIGHT = 700;
	public static final String APP_SHORTCUT = "ctrl SPACE";
	private static final String CSS_PATH = "css/style.css";
	
	// system tray
	private static final String TRAY_SHOW = "Show";
	private static final String TRAY_CLOSE =  "Close";
	private static final String TRAY_ICON_ERROR =  "Error locating tray icon";
	private static final String TRAY_ADD_ERROR =  "Error adding TaskPanda tray icon to System Icon Tray";

	// input field
	private static final String IF_PLACEHOLDER = "Get Busy!";
	private static final int IF_HEIGHT = 90;
	private static final int IF_WIDTH = APP_WIDTH;
	private static final int IF_COLUMN_COUNT = 50;
	private static final String IF_ID = "inputField";

	private static final String SPACE = " ";
	private static final String SPLIT = "\\s+";

	private static final int OFFSET = 1;
	private static final int COMMAND_INDEX = 0;
	private static final int THE_REST_INDEX = 1;

	// listview
	private static final String LIST_ID = "tasklist";

	private static final String OVERDUE_TXT = "You have %d overdue task(s).";
	private static final String OVERDUE_ID = "overdue-label";
	private static final String OVERDUE_VBOX_ID = "overdue-vbox";
	private static final int OVERDUE_HEIGHT = 40;
	
	private static final String EDIT_TXT = "edit %1d %2s";
	private static final String DELETE_TXT = "delete %1d";
	
	
	private static final String HELP_TITLE_ID = "help-title";
	private static final String HELP_TEXT_ID = "help-text";

	private static final String INVALID_COMMAND = "Invalid Command! Type \"Help\" for Manual";
	
	// help manual
	private static final String HELP_TITLE = "\nTaskPanda Help Manual";

	CommandFactory commandFactory = CommandFactory.INSTANCE;
	ObservableList<Task> tasks = commandFactory.getDisplayTasks();
	ObservableList<Task> overduetasks = commandFactory.getOverdueTasks();

	ListView<Task> list = new ListView<Task>();
	Label overdueLabel = new Label();
	Text overdueText = new Text();
//	Tooltip tooltip = new Tooltip(INVALID_COMMAND);

	TextField inputField;

	VBox bottomBox;
	VBox overBox = addOverStatus();
	VBox taskBox = addTaskList();
	VBox helpBox = addHelpText();
	
	private TrayIcon trayIcon;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		BorderPane border = new BorderPane();
		border.setTop(addInputField());
		border.setCenter(addBottomComponents());

		createTrayIcon(primaryStage);
		setupGlobalHotKey(primaryStage);
		Platform.setImplicitExit(false);
		primaryStage.setScene(setUpScene(border));
		primaryStage.getIcons().add(new Image(APP_ICON));
		primaryStage.setTitle(APP_TITLE);
		primaryStage.show();
		primaryStage.toFront();
	}
	
	private void createTrayIcon(final Stage stage) {
		if(SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					hide(stage);
				}
			});
		
            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
				    Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
						    stage.show();
						    stage.toFront();
					    }
				    });
			    }
		    };
		    
		    ActionListener exitListener = new ActionListener() {
		    	@Override
		    	public void actionPerformed(ActionEvent e) {
		    		System.exit(0);
		    	}
		    };
		
		    // Popup menu
		    PopupMenu menu = new PopupMenu();
		    MenuItem showItem = new MenuItem(TRAY_SHOW);
		    showItem.addActionListener(showListener);
		    menu.add(showItem);
		    
		    MenuItem exitItem = new MenuItem(TRAY_CLOSE);
		    exitItem.addActionListener(exitListener);
		    menu.add(exitItem);
		
		    java.awt.Image trayImage = null;
		    try {
		        trayImage = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(APP_ICON));
		    } catch(IOException e) {
		    	System.err.println(TRAY_ICON_ERROR);
		    }
		    
		    // setup tray icon
		    trayIcon = new TrayIcon(trayImage, APP_TITLE , menu);
		    trayIcon.addActionListener(showListener);
		    try {
			    tray.add(trayIcon);
		    } catch(AWTException e) {
		    	System.err.println(TRAY_ADD_ERROR);
		    }
		}
	}
	
	private void setupGlobalHotKey(final Stage stage) {
		HotKeyListener hotkeylistener = new HotKeyListener() {
	    	@Override
	    	public void onHotKey(HotKey hotkey) {
	    		Platform.runLater(new Runnable() {
	    			@Override
	    			public void run() {
	    				stage.show();
	    				stage.toFront();
	    			}
	    		});
	    	}
	    };
	    Provider provider = Provider.getCurrentProvider(true);
	    provider.reset();
	    provider.register(KeyStroke.getKeyStroke(APP_SHORTCUT), hotkeylistener);
	}
	
	private void hide(final Stage stage) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(SystemTray.isSupported()) {
					stage.hide();
				} else {
					System.exit(0);
				}
			}
		});
	}
	
	private Scene setUpScene(BorderPane border) {
		Scene scene = new Scene(border, APP_WIDTH, APP_HEIGHT);
		scene.getStylesheets().add(this.getClass().getClassLoader().getResource(CSS_PATH).toExternalForm());

		// for resizing of app
		scene.widthProperty().addListener(new ChangeListener() {
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Double width = (Double) newValue;
				inputField.setPrefWidth(width);
				overdueLabel.setPrefWidth(width);
				list.setPrefWidth(width);
				
			}
		});
		scene.heightProperty().addListener(new ChangeListener() {
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Double height = (Double) newValue;
				list.setPrefHeight((height - (IF_HEIGHT + OVERDUE_HEIGHT)));
			}
		});
		return scene;
	}

	private HBox addInputField() {
		HBox hbox = new HBox();

		inputField = new TextField();
		inputField.setPromptText(IF_PLACEHOLDER);
		inputField.setPrefColumnCount(IF_COLUMN_COUNT);
		inputField.setPrefSize(IF_WIDTH, IF_HEIGHT);
		inputField.setId(IF_ID);
		inputField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				handleSearch(oldValue, newValue);
			}
		});
		handleInputKeyListener();
		hbox.getChildren().addAll(inputField);
		return hbox;
	}
	
	private void handleInputKeyListener() {
		inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					if (inputField.getText().length() > 0) {
						Command command = new Command(inputField.getText());
						if (command.command == COMMAND_TYPE.INVALID) {
							// invalid command
							overdueLabel.textProperty().unbind();
							overdueLabel.setText(INVALID_COMMAND);
						} else {
							commandFactory.process(command);
							if(command.command != COMMAND_TYPE.SEARCH) {
							    updateTasksList();
							}
                            if (command.command == COMMAND_TYPE.ADD) {
                                list.scrollTo(tasks.size() - OFFSET);
                                list.getSelectionModel().select(tasks.size() - OFFSET);
                            }
							overdueLabel.textProperty().bind(Bindings.format(OVERDUE_TXT, Bindings.size(overduetasks)));
							if (command.command == COMMAND_TYPE.HELP) {
								// show help text
								bottomBox.getChildren().remove(taskBox);
								bottomBox.getChildren().add(helpBox);
							} else {
								// remove help text
								if(bottomBox.getChildren().contains(helpBox)) {
									bottomBox.getChildren().remove(helpBox);
								}
								if(!bottomBox.getChildren().contains(taskBox)) {
									bottomBox.getChildren().add(taskBox);
								}

							}
							inputField.clear();
						}
					}
				} else if (e.getCode() == KeyCode.DOWN) {
					list.requestFocus();
				}
			}
		});
	}

	private VBox addBottomComponents() {
		bottomBox = new VBox();
		bottomBox.getChildren().addAll(overBox);
		if(list.getItems().size() > 0) {
			bottomBox.getChildren().add(taskBox);
		}
		return bottomBox;
	}

	private VBox addHelpText() {
		helpBox = new VBox();
		Label title = new Label(HELP_TITLE);
		Label helpText = new Label(

		"add <description> \n" + 
		"add <description> <timestamp> \n" + 
		"add <description> from <timestamp> to <timestamp> \n\n" +

		"list \n" + 
		"list misc \n" + 
		"list timed \n" + 
		"list deadline \n\n" +
		"list today \n" + 
		"list tomorrow \n" + 
		"list this week \n" + 
		"list <date> \n\n" +

		"edit <id> <description> <timestamp> \n\n" +

		"done <id> \n" + "undone <id> \n\n" +

		"undo \n\n" + "redo \n\n" +

		"search <keyword> \n");

		title.setId(HELP_TITLE_ID);
		helpText.setId(HELP_TEXT_ID);
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
		list.setPrefHeight(APP_HEIGHT - (IF_HEIGHT + OVERDUE_HEIGHT));
		list.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
			@Override
			public ListCell<Task> call(ListView<Task> param) {
				TaskCell taskcell = new TaskCell();
				return taskcell;
			}
		});
		handleListKeyListener();
		taskBox.getChildren().addAll(list);
		return taskBox;
	}
	
	private void handleListKeyListener() {

		// for bigger scrolling across page
		list.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					handleEnterKey();
				} else if (e.getCode() == KeyCode.ESCAPE) {
					handleEscKey();
				} else if (e.getCode() == KeyCode.DELETE) {
					handleDeleteKey();
				}
			}
		});
	}
	
	private void handleEnterKey() {
        int index = list.getSelectionModel().getSelectedIndex() + OFFSET;
		Task t = list.getSelectionModel().getSelectedItem();
		inputField.setText(String.format(EDIT_TXT, index, t.getRawText()));
		inputField.requestFocus();
	}
	
	private void handleEscKey() {
		inputField.requestFocus();
	}
	
	private void handleDeleteKey() {
		int index = list.getSelectionModel().getSelectedIndex() + OFFSET;
		inputField.setText(String.format(DELETE_TXT, index));
		inputField.requestFocus();
	}

	private void handleSearch(String oldValue, String newValue) {
		// user pressed delete and reverse back to the old list
		if (oldValue != null && (newValue.length() < oldValue.length())) {
			updateTasksList();
		}

		String[] pieces = newValue.split(SPLIT);
		if ((!pieces[COMMAND_INDEX].equalsIgnoreCase(COMMAND_TYPE.SEARCH.name())) || pieces.length <= THE_REST_INDEX) {
			updateTasksList();
		} else {
			newValue = pieces[THE_REST_INDEX];
			String[] parts = newValue.toLowerCase().split(SPACE);
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
