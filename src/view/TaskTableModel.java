package view;

import java.util.LinkedHashMap;
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
			"Start Time", "End Time", "Tags" };//, "Marked", "Done"};
	
	private List<Task> l;
	private LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
	
	private final int OFFSET = 1;
	
	public TaskTableModel() {
		l = CommandFactory.INSTANCE.getTasks();
		map = CommandFactory.INSTANCE.getTasksMap();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public int getRowCount() {
		return map.size();
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
		Task t = l.get(map.get(rowIndex));
		if(columnIndex == 0) {
			Integer idToDisplay = (Integer)map.keySet().toArray()[rowIndex];
			return idToDisplay + OFFSET;
		} else if(columnIndex == 1) {
			return t.getTaskDescription();
		} else if(columnIndex == 2) {
			DateTime startTime = t.getTaskStartTime();
			if(startTime == null) {
				return null;
			}
			DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM d, yyyy HH:mm");
			return fmt.print(startTime);
		} else if(columnIndex == 3) {
			DateTime endTime = t.getTaskEndTime();
			if (endTime == null) {
				return null;
			}
			DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM d, yyyy HH:mm");
			return fmt.print(endTime);
		} else if(columnIndex == 4) {
			return t.getTaskTags();
		}
//		} else if (columnIndex == 5) {
//			return t.getMarkAsDelete();
//		} else if (columnIndex == 6) {
//			return t.getTaskDone();
//		}
		return null;
	}

}
