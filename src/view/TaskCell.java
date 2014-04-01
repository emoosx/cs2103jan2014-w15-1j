package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import core.Task;

public class TaskCell extends ListCell<Task> {

	private static final String FONT_ROBOTO = "Roboto";
	
	private GridPane grid = new GridPane();
	private Label icon = new Label();
	private Label name = new Label();
	
	public TaskCell() {
		configureGrid();
		configureIcon();
		configureName();
		addControlsToGrid();
	}
	
	private void configureGrid() {
		grid.setHgap(10);
		grid.setVgap(4);
		grid.setPadding(new Insets(0, 10, 0, 10));
	}
	
	private void configureIcon() {
		icon.setFont(Font.font(FONT_ROBOTO, FontWeight.BOLD, 24));
	}
	
	private void configureName() {

	}
	
	private void addControlsToGrid() {
		grid.add(icon, 0, 0, 1, 2);
		grid.add(name, 1, 1);
	}

	@Override
	public void updateItem(Task task, boolean empty) {
		super.updateItem(task, empty);
		if(empty) {
			clearContent();
		} else {
			addContent(task);
		}
	}
	
	private void clearContent() {
		setText(null);
		setGraphic(null);
	}
	
	private void addContent(Task task) {
		setText(null);
		icon.setText(task.getLabel());
		name.setText(task.toString());
		setGraphic(grid);
	}
	
}
