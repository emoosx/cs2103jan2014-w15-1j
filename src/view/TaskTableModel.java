package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import core.Task;

public class TaskTableModel extends AbstractTableModel {

	private String[] columnNames = { "Task ID", "Description",
			"Start Time", "End Time", "Tags" };
	
	private List<Task> l;
	
	public TaskTableModel() {
		l = new ArrayList<Task>();
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
		return l.get(rowIndex); 
	}

}
