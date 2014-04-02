package view;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class cliMain extends Application {
	
	private final int PADDING = 10;

  ObservableList<String> entries = FXCollections.observableArrayList();
  ListView list = new ListView();

  public static void main(String[] args) {
	  launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
	  primaryStage.setTitle("Simple Search");
	  Button btn = new Button();
	  btn.setText("Choose");
	  btn.setOnAction(new EventHandler<ActionEvent>() {
		  @Override
		  public void handle(ActionEvent event) {
			  System.exit(0);
		  }
	  });
	  TextField txt = new TextField();
	  txt.setPromptText("Search");
	  txt.textProperty().addListener(new ChangeListener() {
		  public void changed(ObservableValue observable, Object oldVal, Object newVal) {
			  handleSearchByKey2((String)oldVal, (String)newVal);
		  }
	  });
	  
	  list.setMaxHeight(180);
	  
	  for(int i = 0; i < 100; i++) {
		  entries.add("Item " + i);
	  }
	  entries.add("Arkar Aung");
	  entries.add("Darli Aung");
	  entries.add("Selina Khine");
	  entries.add("Swan Htet Aung");
	  list.setItems(entries);
	  
	  VBox root = new VBox();
	  root.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
	  root.setSpacing(2);
	  root.getChildren().addAll(txt, list, btn);
	  primaryStage.setScene(new Scene(root, 300, 250));
	  primaryStage.show();
  }
  
  public void handleSearchByKey(String oldVal, String newVal) {
	  
	  if(oldVal != null && (newVal.length() < oldVal.length())) {
		  list.setItems(entries);
	  }
	  
	  newVal = newVal.toUpperCase();
	  
	  ObservableList<String> subentries = FXCollections.observableArrayList();
	  for(Object entry: list.getItems()) {
		  String entryText = (String)entry;
		  if(entryText.toUpperCase().contains(newVal)) {
			  subentries.add(entryText);
		  }
	  }
	  list.setItems(subentries);
  }
  
  public void handleSearchByKey2(String oldVal, String newVal) {
	  if(oldVal != null && (newVal.length() < oldVal.length())) {
		  list.setItems(entries);
	  }
	  
	  String[] parts = newVal.toUpperCase().split(" ");
	  
	  ObservableList<String> subentries = FXCollections.observableArrayList();
	  for(Object entry: list.getItems()) {
		  boolean match = true;
		  String entryText = (String)entry;
		  for(String part: parts) {
			  if(!entryText.toUpperCase().contains(part)) {
				  match = false;
				  break;
			  }
		  }
		  
		  if(match) {
			  subentries.add(entryText);
		  }
	  }
	  list.setItems(subentries);
  }
}
