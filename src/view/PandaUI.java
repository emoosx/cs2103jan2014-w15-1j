package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.CommandFactory;
import core.Task;

public class PandaUI extends Application {
	
	private final String TXT_PLACEHOLDER = "Search";
	private final int PADDING = 10;
	
	CommandFactory commandFactory = CommandFactory.INSTANCE;
	ObservableList<Task> tasks = FXCollections.observableArrayList(commandFactory.getTasks());
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
        
        primaryStage.setScene(new Scene(border, 300, 250));
        primaryStage.show();
	}
	
	private HBox addInputField() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));

		inputField = new TextField();
		inputField.setPromptText(TXT_PLACEHOLDER);
		
		hbox.getChildren().addAll(inputField);
		return hbox;
	}
	
	private VBox addList() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(PADDING));
		vbox.setSpacing(8);
		
        list.setItems(tasks);
        list.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
        	@Override
        	public ListCell<Task> call(ListView<Task> param) {
        		return new TaskCell();
        	}
        });
        vbox.getChildren().addAll(list);
        return vbox;
	}
}
