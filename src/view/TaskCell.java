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
	
	private static final int GRID_HGAP = 10;
	private static final int GRID_VGAP = 4;
	private static final String GRID_ID = "grid";
	
	private static final String ICON_CLASS = "icon";
	private static final String ICON_TIMED_ID = "timed";
	private static final String ICON_DEADLINE_ID = "deadline";
	private static final String ICON_FLOATING_ID = "floating";
	private static final int ICON_HEIGHT = 35;
	private static final int ICON_WIDTH = ICON_HEIGHT;
	
	private static final String NAME_ID = "taskDescription";
	
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
		grid.setHgap(GRID_HGAP);
		grid.setVgap(GRID_VGAP);
		grid.setId(GRID_ID);
//		grid.setPadding(new Insets(0, 10, 0, 10));
	}
	
	private void configureIcon() {
		icon.setFont(Font.font(FONT_ROBOTO, FontWeight.BOLD, 24));
		icon.setPrefSize(ICON_WIDTH, ICON_HEIGHT);
		icon.getStyleClass().add(ICON_CLASS);
	}
	
	private void configureName() {
		name.setId(NAME_ID);
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
		String label = task.getLabel();
		if(label.equals("T")) {
			icon.setId(ICON_TIMED_ID);
		}else if(label.equals("D")) {
			icon.setId(ICON_DEADLINE_ID);
		} else {
			icon.setId(ICON_FLOATING_ID);
		}
		icon.setText(label);
		name.setText(task.toString());
		setGraphic(grid);
	}
	
}
