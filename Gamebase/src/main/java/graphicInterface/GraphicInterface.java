package graphicInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JLabel;
import javax.swing.JTextField;

import logic.*; 

import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Insets;

public class GraphicInterface {

	private JFrame frame;
	private JPanel panel;
	
	//Login Page
	private JPanel loginPanel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameTextfield;
	private JPasswordField passwordField;
	private JButton signUpButton;
	private JButton loginButton;
	private JLabel errorMessageLabel;
	private JLabel myGamesLabel;

	//Home page
	private JPanel homePagePanel;
	private JLabel gamesNumberHPLabel;
	private JLabel followerNumberHPLabel;
	private JLabel usertypeHPLabel;
	private JLabel usernameHPLabel;
	private JButton logoutHPButton;
	private JLabel userTypeIconHPLabel;
	private JButton adminHPButton;
	private JButton becomeAnalystButton;
	private JButton analystHPButton;
	
	//Logic and support info
	private logicBridge logicHandler = new logicBridge();
	private String currentUser = null;
	
	//support functions
	
	private void initializeHomePage( userType user, String username ) {
		
		gamesNumberHPLabel.setText(logicHandler.getFollowersNumber(username)!=-1?Integer.toString(logicHandler.getFollowersNumber(username)):"N/A");
		followerNumberHPLabel.setText(logicHandler.getLikedGamesNumber(username)!=-1?Integer.toString(logicHandler.getLikedGamesNumber(username)):"N/A");
		
		usertypeHPLabel.setText(user.toString());
		usernameHPLabel.setText(username);
		
		switch(user) {
			case ADMINISTRATOR:
				userTypeIconHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/admin.png")).getImage().getScaledInstance(83, 83, Image.SCALE_SMOOTH)));
				adminHPButton.setVisible(true);
				becomeAnalystButton.setVisible(false);
				analystHPButton.setVisible(true);
				//fai vedere alcune cose, nascondine altre
				break;
			case ANALYST:
				userTypeIconHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/analyst.png")).getImage().getScaledInstance(83, 83, Image.SCALE_SMOOTH)));
				adminHPButton.setVisible(false);
				becomeAnalystButton.setVisible(false);
				analystHPButton.setVisible(true);
				break;
			case USER:
				userTypeIconHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/user.png")).getImage().getScaledInstance(83, 83, Image.SCALE_SMOOTH)));
				adminHPButton.setVisible(false);
				becomeAnalystButton.setVisible(true);
				analystHPButton.setVisible(false);
				//fai vedere alcune cose, nascondine altre
				break;
			default:
				return;
		}
	}
	
	private void cleanHomePage() {
		
		gamesNumberHPLabel.setText("");
		followerNumberHPLabel.setText("");
		usertypeHPLabel.setText("");
		usernameHPLabel.setText("");
		userTypeIconHPLabel.setIcon(null);
	}
	
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
		
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new CardLayout(0, 0));
		
		loginPanel = new JPanel();
		loginPanel.setName("loginPanel");
		loginPanel.setBackground(new Color(87, 86, 82));
		panel.add(loginPanel, "loginPanel");
		loginPanel.setLayout(null);
		
		usernameLabel = new JLabel("Username");
		usernameLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		usernameLabel.setName("usernameLabel");
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setBounds(269, 254, 166, 25);
		loginPanel.add(usernameLabel);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setName("passwordLabel");
		passwordLabel.setBounds(269, 342, 107, 25);
		loginPanel.add(passwordLabel);
		
		usernameTextfield = new JTextField();
		usernameTextfield.setBounds(269, 275, 387, 43);
		loginPanel.add(usernameTextfield);
		usernameTextfield.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(269, 362, 387, 43);
		loginPanel.add(passwordField);
		
		signUpButton = new JButton("Sign Up");
		signUpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		signUpButton.setBorderPainted(false);
		signUpButton.setBackground(new Color(0, 128, 128));
		signUpButton.setOpaque(false);
		signUpButton.setContentAreaFilled(false);
		signUpButton.setBorder(null);
		signUpButton.setForeground(new Color(0, 0, 128));
		signUpButton.setFont(new Font("Corbel", Font.BOLD, 21));
		signUpButton.setToolTipText("Click Here To Sign Up");
		signUpButton.setName("signUpButton");
		signUpButton.setBounds(328, 470, 107, 38);
		signUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				String username = usernameTextfield.getText();
				String password = new String(passwordField.getPassword());
				
				System.out.println("GRAPHICINTERFACE.JAVA/SIGNUPACTIONPERFORMED-->trying to sign up " + username);
				
				if( username.equals("") || password.equals("") ) {
					System.out.println("GRAPHICINTERFACE.JAVA/SIGNUPACTIONPERFORMED-->sign up failed: empty username and(or) password");
					errorMessageLabel.setText("Please Insert Username and Password");
					errorMessageLabel.setVisible(true);
					return;
				}
				
				if( !logicHandler.signUp(username,password) ) {
					System.out.println("GRAPHICINTERFACE.JAVA/SIGNUPACTIONPERFORMED-->sign up failed: username " + username + " already exists");
					errorMessageLabel.setText("Username already used");
					errorMessageLabel.setVisible(true);
					return;
				} else {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->sign up completed: username " + username + " registered");
					cl.show(panel, "homePagePanel");
					initializeHomePage(userType.USER,username);
					currentUser = username;
				}
			}
		});
		loginPanel.add(signUpButton);
		
		loginButton = new JButton("Login");
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginButton.setBorderPainted(false);
		loginButton.setBackground(new Color(0, 128, 128));
		loginButton.setOpaque(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setBorder(null);
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font("Corbel", Font.BOLD, 21));
		loginButton.setName("loginButton");
		loginButton.setBounds(489, 470, 103, 38);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				String username = usernameTextfield.getText();
				String password = new String(passwordField.getPassword());
				
				System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->user " + username + " is trying to login");
				
				if( username.equals("") || password.equals("") ) {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->empty username and(or) password");
					errorMessageLabel.setText("Please Insert Username and Password");
					errorMessageLabel.setVisible(true);
					return;
				}
				
				userType usertype = logicHandler.login(username,password);
				
				if( usertype == userType.NO_USER ) {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->login failed: no user " + username + " found");
					errorMessageLabel.setText("No User " + username + " found");
					errorMessageLabel.setVisible(true);
				} else if( usertype == userType.WRONG_PASSWORD ) {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->login failed: wrong password for username " + username);
					errorMessageLabel.setText("Uncorrect Password for User " + username );
					errorMessageLabel.setVisible(true);
				} else {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->login completed:user " + username + " logged in");
					cl.show(panel, "homePagePanel");
					initializeHomePage( usertype, username );
					currentUser = username;
				}				
			}
		});
		loginPanel.add(loginButton);
		
		errorMessageLabel = new JLabel("");
		errorMessageLabel.setForeground(new Color(255, 0, 0));
		errorMessageLabel.setFont(new Font("Corbel", Font.PLAIN, 17));
		errorMessageLabel.setVisible(false);
		errorMessageLabel.setName("errorMessageLabel");
		errorMessageLabel.setBounds(269, 418, 387, 31);
		loginPanel.add(errorMessageLabel);
		
		myGamesLabel = new JLabel("");
		myGamesLabel.setIcon(new ImageIcon(GraphicInterface.class.getResource("/resources/mygames.png")));
		myGamesLabel.setName("myGamesLabel");
		myGamesLabel.setBounds(304, 51, 318, 170);
		loginPanel.add(myGamesLabel);
		
		homePagePanel = new JPanel();
		homePagePanel.setBackground(new Color(87, 86, 82));
		panel.add(homePagePanel, "homePagePanel");
		homePagePanel.setLayout(null);
		
		usertypeHPLabel = new JLabel("userType");
		usertypeHPLabel.setForeground(Color.WHITE);
		usertypeHPLabel.setFont(new Font("Corbel", Font.PLAIN, 16));
		usertypeHPLabel.setName("usertypeHPLabel");
		usertypeHPLabel.setBounds(152, 13, 106, 16);
		homePagePanel.add(usertypeHPLabel);
		
		usernameHPLabel = new JLabel("username");
		usernameHPLabel.setFont(new Font("Corbel", Font.PLAIN, 16));
		usernameHPLabel.setForeground(Color.WHITE);
		usernameHPLabel.setName("usernameHPLabel");
		usernameHPLabel.setBounds(152, 42, 106, 16);
		homePagePanel.add(usernameHPLabel);
		
		logoutHPButton = new JButton("Logout");
		logoutHPButton.setMargin(new Insets(2, 2, 2, 2));
		logoutHPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				currentUser = null;
				cl.show(panel, "loginPanel");
			}
		});
		logoutHPButton.setToolTipText("Click Here To Logout");
		logoutHPButton.setName("logoutHPButton");
		logoutHPButton.setBounds(152, 75, 74, 21);
		logoutHPButton.setBorderPainted(false);
		logoutHPButton.setBackground(new Color(0, 128, 128));
		logoutHPButton.setOpaque(false);
		logoutHPButton.setContentAreaFilled(false);
		logoutHPButton.setBorder(null);
		logoutHPButton.setForeground(new Color(128, 0, 0));
		logoutHPButton.setFont(new Font("Corbel", Font.BOLD, 21));
		homePagePanel.add(logoutHPButton);
		
		gamesNumberHPLabel = new JLabel("11");
		gamesNumberHPLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		gamesNumberHPLabel.setForeground(Color.WHITE);
		gamesNumberHPLabel.setFont(new Font("Corbel", Font.BOLD, 41));
		gamesNumberHPLabel.setToolTipText("Number of Games You Like");
		gamesNumberHPLabel.setName("gamesNumberHPLabel");
		gamesNumberHPLabel.setBounds(508, 10, 128, 69);
		gamesNumberHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/controller.png")).getImage().getScaledInstance(69, 69, Image.SCALE_SMOOTH)));
		homePagePanel.add(gamesNumberHPLabel);
		
		followerNumberHPLabel = new JLabel("0");
		followerNumberHPLabel.setForeground(Color.WHITE);
		followerNumberHPLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		followerNumberHPLabel.setFont(new Font("Corbel", Font.BOLD, 41));
		followerNumberHPLabel.setToolTipText("Number of  People Who Follow You");
		followerNumberHPLabel.setName("followerNumberHPLabel");
		followerNumberHPLabel.setBounds(648, 10, 116, 69);
		followerNumberHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/followers.png")).getImage().getScaledInstance(69, 69, Image.SCALE_SMOOTH)));
		homePagePanel.add(followerNumberHPLabel);
		
		userTypeIconHPLabel = new JLabel("");
		userTypeIconHPLabel.setName("userTypeIconHPLabel");
		userTypeIconHPLabel.setBounds(41, 13, 83, 83);
		homePagePanel.add(userTypeIconHPLabel);
		
		adminHPButton = new JButton("Admin Section");
		adminHPButton.setBounds(326, 137, 97, 30);
		homePagePanel.add(adminHPButton);
		
		becomeAnalystButton = new JButton("Become Analyst");
		becomeAnalystButton.setBounds(326, 242, 97, 25);
		homePagePanel.add(becomeAnalystButton);
		
		analystHPButton = new JButton("Analyst Section");
		analystHPButton.setBounds(161, 358, 97, 25);
		homePagePanel.add(analystHPButton);
	}
}
