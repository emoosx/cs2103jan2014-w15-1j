package view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import logic.CommandFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import common.PandaLogger;

import core.Task;

@SuppressWarnings("serial")
public class TaskTableModel extends AbstractTableModel {

	// TODO delete marked column .. enabled only during development phase
	private String[] columnNames = { "Task ID", "Description",
			"Start Time", "End Time", "Tags" , "Marked"};
	
	private List<Task> l;
	
	public TaskTableModel() {
		l = CommandFactory.INSTANCE.getTasks();
		PandaLogger.getLogger().info("TaskTableModel: " + l.size());
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return l.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex == 0) {
			return rowIndex + 1;
		} else if(columnIndex == 1) {
			return l.get(rowIndex).getTaskDescription();
		} else if(columnIndex == 2) {
			DateTime startTime = l.get(rowIndex).getTaskStartTime();
			if(startTime == null) {
				return null;
			}
			DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM d, yyyy HH:mm");
			return fmt.print(startTime);
		} else if(columnIndex == 3) {
			DateTime endTime = l.get(rowIndex).getTaskEndTime();
			if (endTime == null) {
				return null;
			}
			DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM d, yyyy HH:mm");
			return fmt.print(endTime);
		} else if(columnIndex == 4) {
			return l.get(rowIndex).getTaskTags();
		} else if (columnIndex == 5) {
			return l.get(rowIndex).getMarkAsDelete();
		}
		return null;
	}

}
