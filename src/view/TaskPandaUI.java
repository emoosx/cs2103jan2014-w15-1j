package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import logic.Command;
import logic.Command.COMMAND_TYPE;
import logic.CommandFactory;

//@author A0105860L-unused
//now longer used from V0.3 onwards since we switched to javafx instead of Swing
public class TaskPandaUI extends JFrame {

	protected PlaceholderTextField inputField;
	protected CommandFactory commandFactory;
	protected JTable table;
	protected TaskTableModel tableModel;
	protected JPanel basic, bottomPanel, topPanel;
	
	private Border cache;
	
	private static final String PLACEHOLDER_DEFAULT = "Enter command";
	private static final int PADDING = 5;
	private static final String COLOR_BG = "#FCFFFF";

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
		basic = new JPanel();
		basic.setOpaque(true);
		basic.setBackground(new Color(255, 255, 255, 1));
		basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
		add(basic);

		topPanel = new JPanel(new BorderLayout(0, 0));
		inputField = new PlaceholderTextField(40);
		inputField.setOpaque(false);
		inputField.setBackground(new Color(236, 240, 241, 1));
		inputField.setPlaceholder("Hello there!");
		inputField.setPreferredSize(new Dimension(450, 45));
		inputField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.decode(COLOR_BG)),
				BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)));
		inputField.setFont(new Font("Sans-Serif", Font.BOLD, 18));
		
		inputField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					processCommand(inputField.getText());
					inputField.setText("");
				}
			}

			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
			}
		});
		
		inputField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				System.out.println("insertUpdate");
				System.out.println(inputField.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println("removeUPdate");
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("changedUpdate");
			}
			
		});
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		topPanel.add(inputField, BorderLayout.CENTER);
		// topPanel.add(separator, BorderLayout.SOUTH);

		basic.add(topPanel);

		bottomPanel = new JPanel(new BorderLayout());
		tableModel = new TaskTableModel();
		table = new JTable(tableModel);
		table.setMaximumSize(getMaximumSize());

		JScrollPane scrollPane = new JScrollPane(table);
		bottomPanel.add(scrollPane);

		add(basic);
		setUndecorated(true);	// Remove title bar
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void processCommand(String inputText) {
		assert (inputText != null);
		Command command = new Command(inputText);
		if (command.command != COMMAND_TYPE.INVALID) {
			inputField.setBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createLineBorder(Color.decode(COLOR_BG)),
							BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
                    )
            );
			inputField.setPlaceholder(PLACEHOLDER_DEFAULT);
			commandFactory.process(command);
			basic.add(bottomPanel);
			basic.revalidate();
			basic.repaint();
			pack();
			tableModel.fireTableDataChanged();
		} else {
			inputField.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.decode("#c0392b")), 
					BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)));
			inputField.setPlaceholder("Invalid Command!");
		}
	}
}
