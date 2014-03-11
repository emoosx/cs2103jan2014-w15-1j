package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class TaskPandaUI extends JFrame {
	
	protected JTextField inputField;

	public TaskPandaUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initUI();
				setVisible(true);
			}
		});
	}
	
	private void initUI() {
		JPanel panel = new JPanel(new BorderLayout());
		
		JPanel inputPanel = new JPanel();
		inputField = new JTextField(50);
		inputField.setPreferredSize(new Dimension(5, 50));
		inputField.setFont(new Font("SansSerif", Font.BOLD, 20));
		inputPanel.add(inputField);
		
		panel.add(inputPanel);
		panel.setBorder(new EmptyBorder(new Insets(1, 1, 1, 1)));
		

		add(panel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
}
