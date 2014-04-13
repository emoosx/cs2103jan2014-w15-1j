package view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import core.Task;

public class TaskCell extends ListCell<Task> {

	private static final int OFFSET = 1;

	// grid
	private static final int VGAP = 5;
	private static final int INDEX_WIDTH = 40;
	private static final int INDEX_HEIGHT = 40;
	
	private static final int CELL_WIDTH = 482;
	
	private static final int HASHTAG_WIDTH = 100;
	
	private static final double PADDING = 2.0;

	// css id for UI elements
	private static final String HBOX_ID = "hbox";
	private static final String GRID_ID = "grid";
	private static final String DESC_ID = "description";
	private static final String HASHTAG_ID = "hashtag";
	private static final String INDEX_ID = "index";
	private static final String START_ID = "start";
	private static final String END_ID = "end";
	private static final String TIME_CLASS = "timestamp";
	private static final String OVERDUE_CLASS = "overdue-task";
	private static final String DONE_ID = "done";
	private static final String SEPARATOR_ID = "separator";
	private static final String TASK_CELL_ID = "task-cell";
	
	private static final String DATE_FORMAT_PATTERN = "d MMM yy  h:mm a";

	private GridPane grid = new GridPane();
	private HBox hbox = new HBox();
	private Label index = new Label();
	private Label desc = new Label();
	private Label start = new Label();
	private Label end = new Label();
	private Label hashtag = new Label();
	private Separator separator = new Separator(Orientation.VERTICAL);

	public TaskCell() {
		configureCell();
		configureGrid();
		configureHBox();
		configureIndex(); // id label
		configureHashtag();
		configureDesc(); // description label
		configureTimestamp();
		configureSeparator();
		addControls();

	}
	
	private void configureCell() {
		super.setId(TASK_CELL_ID);
		super.setPrefWidth(CELL_WIDTH);
		super.widthProperty().addListener(new ChangeListener() {
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Double width = (Double) newValue;
				if(width < CELL_WIDTH) {
					desc.setPrefWidth(PandaUI.APP_WIDTH - PADDING*HASHTAG_WIDTH);
				} else {
					desc.setPrefWidth(width - PADDING*HASHTAG_WIDTH);
				}
			}
		});
	}
	
	private void configureGrid() {
		grid.setHgap(VGAP);
		grid.setVgap(VGAP);
		grid.setId(GRID_ID);
		grid.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(120));
	}

	private void configureHBox() {
		hbox.setId(HBOX_ID);
	}

	private void configureIndex() {
		index.setPrefSize(INDEX_WIDTH, INDEX_HEIGHT);
		index.setId(INDEX_ID);
		index.setAlignment(Pos.CENTER);
	}

	private void configureHashtag() {
		hashtag.setId(HASHTAG_ID);
		hashtag.setPrefWidth(HASHTAG_WIDTH);
	}

	private void configureDesc() {
		desc.setId(DESC_ID);
		desc.setPrefWidth(PandaUI.APP_WIDTH - PADDING*HASHTAG_WIDTH);
	}

	private void configureTimestamp() {
		start.getStyleClass().add(TIME_CLASS);
		start.setId(START_ID);
		end.setId(END_ID);
		end.getStyleClass().add(TIME_CLASS);
	}

	
	private void configureSeparator() {
		separator.setId(SEPARATOR_ID);
	}

	private void addControls() {
		grid.add(index, 0, 0, 1, 3);
		grid.add(desc, 1, 0, 3, 1);
		grid.add(start, 1, 1, 1, 1);
		grid.add(end, 3, 1, 1, 1);
//		grid.gridLinesVisibleProperty().set(true);
		hbox.getChildren().addAll(grid, separator, hashtag);
		HBox.setHgrow(grid, Priority.ALWAYS);
	}

	private void addContent(Task task) {
		setText(null);
		index.setText(String.valueOf(super.indexProperty().get() + OFFSET));
		desc.setText(task.getTaskDescription());
		if(task.getTaskTags().isEmpty()) {
			separator.setVisible(false);
		}
		hashtag.setText(task.getTags());

		if (task.getTaskDone() == false) {
			desc.getStyleClass().remove(DONE_ID);
			start.getStyleClass().remove(DONE_ID);
			end.getStyleClass().remove(DONE_ID);
			hashtag.getStyleClass().remove(DONE_ID);
		} else {
			desc.getStyleClass().add(DONE_ID);
			start.getStyleClass().add(DONE_ID);
			end.getStyleClass().add(DONE_ID);
			hashtag.getStyleClass().add(DONE_ID);
		}

		// overdue status
		hbox.getStyleClass().clear();
		if (task.isOverdue()) {
			hbox.getStyleClass().add(OVERDUE_CLASS);
		} else {
			hbox.getStyleClass().remove(OVERDUE_CLASS);
		}

		DateTimeFormatter fmt = DateTimeFormat.forPattern(DATE_FORMAT_PATTERN);
		DateTime startTimestamp = task.getTaskStartTime();
		if(startTimestamp == null) {
			start.setText("");
		} else {
			start.setText(fmt.print(startTimestamp));
		}

		DateTime endTimestamp = task.getTaskEndTime();
		if (endTimestamp == null) {
			end.setText("");
		} else {
			end.setText(fmt.print(endTimestamp));
		}

		setGraphic(hbox);
	}

	private void clearContent() {
		setText(null);
		setGraphic(null);
	}

	@Override
	public void updateItem(Task task, boolean empty) {
		super.updateItem(task, empty);
		if (empty) {
			clearContent();
		} else {
			addContent(task);
		}
	}
}
