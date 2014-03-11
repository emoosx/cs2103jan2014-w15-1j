package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import common.PandaLogger;

import logic.Command;
import logic.Command.COMMAND_TYPE;
import logic.CommandFactory;

public class TaskPandaUI extends JFrame {
	
	protected PlaceholderTextField inputField;
	protected CommandFactory commandFactory; 
	protected JTable table;
	protected TaskTableModel tableModel;
	protected JPanel basic, bottomPanel, topPanel;

	public TaskPandaUI() {
		this.commandFactory = CommandFactory.INSTANCE;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initUI();
				setVisible(true);
			}
		});
	}
	
	private void initUI() {
		
		PandaLogger.getLogger().info("initUI");
		basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		add(basic);

		topPanel = new JPanel(new BorderLayout(0, 0));
		inputField = new PlaceholderTextField(40);
		inputField.setPlaceholder("Hello there!");
		inputField.setPreferredSize(new Dimension(450, 45));
		inputField.setFont(new Font("Sans-Serif", Font.BOLD, 18));
		inputField.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		inputField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					processCommand(inputField.getText());
					inputField.setText("");
				}
			}
			public void keyTyped(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
			}
		});
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		topPanel.add(inputField, BorderLayout.CENTER);
//		topPanel.add(separator, BorderLayout.SOUTH);
		
		basic.add(topPanel);
		
		
		bottomPanel = new JPanel(new BorderLayout());
		tableModel = new TaskTableModel();
		table = new JTable(tableModel);
		table.setMaximumSize(getMaximumSize());
		
		JScrollPane scrollPane = new JScrollPane(table);
		bottomPanel.add(scrollPane);
//		basic.add(bottomPanel);


		add(basic);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void processCommand(String inputText) {
		assert(inputText != null);
		Command command = new Command(inputText);
		commandFactory.process(command);
		basic.add(bottomPanel);
        basic.revalidate();
        basic.repaint();
        pack();
		tableModel.fireTableDataChanged();
	}
}
