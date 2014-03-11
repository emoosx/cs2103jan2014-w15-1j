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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import logic.Command;
import logic.CommandFactory;

public class TaskPandaUI extends JFrame {
	
	protected PlaceholderTextField inputField;
	protected CommandFactory commandFactory; 

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
		
		JPanel basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		add(basic);

		JPanel topPanel = new JPanel(new BorderLayout(0, 0));
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
		separator.setForeground(Color.GRAY);
		topPanel.add(inputField, BorderLayout.CENTER);
		topPanel.add(separator, BorderLayout.SOUTH);
		
		
		String[] columnNames = {
				"First Name",
				"Last Name",
				"Sport",
				"# of Years",
				"Vegetarian"
		};
		
		Object[][] data = {
            { "Kathy", "Smith", "Snowboarding", new Integer(5), new Boolean(false)},
            { "John", "Doe", "Rowing", new Integer(3), new Boolean(true)},
            { "Sue", "Black", "Knitting", new Integer(2), new Boolean(false)},
            { "Jane", "White", "Speed reading", new Integer(20), new Boolean(true)},
            { "Joe", "Brown", "Pool", new Integer(10), new Boolean(false)},
		};
		
		JPanel feedbackPanel = new JPanel();
		JTable table = new JTable(data, columnNames);
		
		JScrollPane scrollPane = new JScrollPane(table);


		
		
		

		add(topPanel);
//		add(scrollPane);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void processCommand(String inputText) {
		assert(inputText != null);
		Command command = new Command(inputText);
		commandFactory.process(command);
	}
}
