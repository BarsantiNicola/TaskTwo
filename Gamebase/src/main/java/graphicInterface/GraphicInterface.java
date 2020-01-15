package graphicInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;

public class GraphicInterface {

	private JFrame frame;
	private JTextField usernameTextfield;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicInterface window = new GraphicInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GraphicInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 952, 615);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new CardLayout(0, 0));
		
		JPanel loginPanel = new JPanel();
		loginPanel.setName("loginPanel");
		loginPanel.setBackground(new Color(87, 86, 82));
		panel.add(loginPanel, "name_2983883636122500");
		loginPanel.setLayout(null);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		usernameLabel.setName("usernameLabel");
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setBounds(175, 267, 166, 25);
		loginPanel.add(usernameLabel);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setName("passwordLabel");
		passwordLabel.setBounds(172, 367, 56, 16);
		loginPanel.add(passwordLabel);
		
		usernameTextfield = new JTextField();
		usernameTextfield.setBounds(163, 295, 394, 37);
		loginPanel.add(usernameTextfield);
		usernameTextfield.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(163, 402, 394, 37);
		loginPanel.add(passwordField);
		
		JButton signUpButton = new JButton("Sign Up");
		signUpButton.setToolTipText("Click Here To Sign Up");
		signUpButton.setName("signUpButton");
		signUpButton.setBounds(290, 483, 97, 25);
		loginPanel.add(signUpButton);
		
		JButton loginButton = new JButton("Login");
		loginButton.setName("loginButton");
		loginButton.setBounds(481, 483, 97, 25);
		loginPanel.add(loginButton);
		
		JLabel errorMessageLabel = new JLabel("");
		errorMessageLabel.setVisible(false);
		errorMessageLabel.setName("errorMessageLabel");
		errorMessageLabel.setBounds(720, 370, 56, 16);
		loginPanel.add(errorMessageLabel);
		
		JPanel homePagePanel = new JPanel();
		panel.add(homePagePanel, "name_2983995321266200");
	}
}
