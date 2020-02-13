package graphicInterface;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.util.List;

import logic.*;
import logic.data.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.net.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.table.*;


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
	private JLabel welcomeHPLabel;
	private JLabel usernameHPLabel;
	private JButton logoutHPButton;
	private JLabel userTypeIconHPLabel;
	private JButton adminHPButton;
	private JButton becomeAnalystButton;
	private JButton analystHPButton;
	private JScrollPane myGamesScrollPane;
	private JLabel mostPopularGamesLabel;
	private JLabel mostViewedGamesLabel;
	private JLabel searchGameLabel;
	private JScrollPane followedTableScrollPane;
	private JTable followedTable;
	private JTableHeader followedTableHeader;
	private JList<PreviewGame> myGamesList;
	private DefaultListModel<PreviewGame> gamesListModel = new DefaultListModel();
	private DefaultTableModel followedTableModel = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Username", "Name", "Last Activity"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
	
	//admin panel
	private JPanel adminPanel;	
	private JButton homeADButton;
	private JButton deleteGameButton;
	private JButton deleteUserButton;
	private JTextField deleteUserTextField;
	private JTextField deleteGameTextField;
	private JPanel adminActionContainer;
	private JLabel adminSectionLabel;
	private JButton updateDatabaseButton;
	private JLabel userCountLabel;
	private JLabel gameCountLabel;
	private JLabel deleteUserResultLabel;
	private JLabel deleteGameResultLabel;
	private JLabel updateDatabaseResultLabel;
	
	//analyst panel
	private JPanel analystPanel;
		
	//search game panel
	private JPanel searchGamePanel;	
	private JButton homeSEButton;
	
	//game panel
	private JPanel gamePanel;	
	
	//Logic and support info
	private logicBridge logicHandler = new logicBridge();
	private String currentUser = null;
	Font titleFont = new Font("Corbel", Font.BOLD, 20);
	private JTextField searchTextField;
	private JButton searchButton;
	
	
	
	//support functions
	
	private boolean fillGamesList(List<PreviewGame> gamesList) {
		
		gamesListModel.removeAllElements();
		
		for( int i = 0; i < gamesList.size(); i++ ) {
			gamesListModel.addElement(gamesList.get(i));
		}
		
		return true;
	}
	
	private boolean fillFollowedTable(List<Friend> friendList) {
		
		followedTableModel.setRowCount(0);
		
		for( Friend friend: friendList ) {
			
			Object[] object = new Object[3];
			object[0] = friend.getUsername();
			object[1] = friend.getCompleteName();
			object[2] = friend.getLastAccess();
			ButtonColumn buttonColumn = new ButtonColumn(followedTable, new AbstractAction() {
				
				public void actionPerformed(ActionEvent e) {
					
					JTable table = (JTable)e.getSource();
					int modelRow = Integer.valueOf(e.getActionCommand());
					
					String followerUsername = (String)((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
					
					CardLayout cl = (CardLayout)(panel.getLayout());
					
					cl.show(panel, "userPanel");
					
					initializeUserPage(followerUsername);
				}
			},0);
			followedTableModel.addRow(object);
		}
		
		return true;
	}
	
	private void initializeHomePage( userType user, String username ) {
		
		String gamesNumber = logicHandler.getLikedGamesNumber(username)!=-1?Integer.toString(logicHandler.getLikedGamesNumber(username)):"N/A";
		String followersNumber = logicHandler.getFollowersNumber(username)!=-1?Integer.toString(logicHandler.getFollowersNumber(username)):"N/A";
		
		gamesNumberHPLabel.setText(gamesNumber);
		followerNumberHPLabel.setText(followersNumber);
		
		gamesNumberHPLabel.setToolTipText("You Have " + gamesNumber + " preferred games");
		followerNumberHPLabel.setToolTipText(followersNumber + " people follow you");
		
		usernameHPLabel.setText(username);
		
		String mostViewedGameImageURL = logicHandler.getMostViewedGame().getPreviewPicURL();
				
		if( mostViewedGameImageURL == null ) {
			mostViewedGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGameBackground.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		} else {
			try {
				mostViewedGamesLabel.setIcon(new ImageIcon(ImageIO.read(new URL(mostViewedGameImageURL))));
			} catch (Exception e){
				mostViewedGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGameBackground.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		    }
		}	
		
		String mostPopularGameImageURL = logicHandler.getMostPopularGame().getPreviewPicURL();
		
		if( mostPopularGameImageURL == null ) {
			mostPopularGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGameBackground.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		} else {
			try {
				mostPopularGamesLabel.setIcon(new ImageIcon(ImageIO.read(new URL(mostPopularGameImageURL))));
			} catch (Exception e){
				mostPopularGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGameBackground.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		    }
		}	
				
		Image profilePicture = logicHandler.getUserPicture(username);
		
		if( profilePicture != null ) {
			userTypeIconHPLabel.setIcon(new ImageIcon(profilePicture.getScaledInstance(83, 83, Image.SCALE_SMOOTH)));
		} else {
			userTypeIconHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultProfilePicture.png")).getImage().getScaledInstance(83, 83, Image.SCALE_SMOOTH)));
		}
		
		fillGamesList(logicHandler.getMyGames(username));
		
		fillFollowedTable(logicHandler.getFriends(username));
		
		switch(user) {
			case ADMINISTRATOR:
				adminHPButton.setVisible(true);
				becomeAnalystButton.setVisible(false);
				analystHPButton.setVisible(true);
				break;
			case ANALYST:
				adminHPButton.setVisible(false);
				becomeAnalystButton.setVisible(false);
				analystHPButton.setVisible(true);
				break;
			case USER:
				adminHPButton.setVisible(false);
				becomeAnalystButton.setVisible(true);
				analystHPButton.setVisible(false);
				break;
			default:
				return;
		}
	}

	private void cleanHomePage() {
		
		gamesNumberHPLabel.setText("");
		followerNumberHPLabel.setText("");
		welcomeHPLabel.setText("");
		usernameHPLabel.setText("");
		userTypeIconHPLabel.setIcon(null);
		
		gamesListModel.removeAllElements();
		followedTableModel.setRowCount(0);
		
	}
	
	private void initializeGamePage( String title ) {
		
	}
	
	private void cleanGamePage() {
		
	}
	
	private void initializeUserPage( String username ) {
		
	}
	
	private void cleanUserPage() {
		
	}
	
	private void initializeAdminPage() {
		
		String userCount, gameCount;
		int userC = logicHandler.getUserCount();
		int gameC = logicHandler.getGameCount();
		
		if( userC == -1 ) {
			userCount = "N/A";
		} else {
			userCount = Integer.toString(userC);
		}
		
		if( gameC == -1 ) {
			gameCount = "N/A";
		} else {
			gameCount = Integer.toString(gameC);
		}
		
		userCountLabel.setText("User Count: " + userCount);
		gameCountLabel.setText("Game Count: " + gameCount);
		
		deleteUserResultLabel.setVisible(false);
		deleteGameResultLabel.setVisible(false);
		updateDatabaseResultLabel.setVisible(false);
	}
	
	private void cleanAdminPage() {
		
		userCountLabel.setText("User Count:");
		gameCountLabel.setText("Game Count:");
		
		deleteUserTextField.setText("User");
		deleteGameTextField.setText("Game");
		
		deleteUserResultLabel.setVisible(false);
		deleteGameResultLabel.setVisible(false);
		updateDatabaseResultLabel.setVisible(false);
	}
	
	
	private void initializeSearchGamePage() {
		
	}
	
	private void cleanSearchGamePage() {
		
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
		homePagePanel.setName("homePagePanel");
		homePagePanel.setBackground(new Color(87, 86, 82));
		panel.add(homePagePanel, "homePagePanel");
		homePagePanel.setLayout(null);
		
		welcomeHPLabel = new JLabel("Welcome,");
		welcomeHPLabel.setForeground(Color.WHITE);
		welcomeHPLabel.setFont(new Font("Corbel", Font.PLAIN, 16));
		welcomeHPLabel.setName("usertypeHPLabel");
		welcomeHPLabel.setBounds(113, 13, 106, 16);
		homePagePanel.add(welcomeHPLabel);
		
		usernameHPLabel = new JLabel("username");
		usernameHPLabel.setFont(new Font("Corbel", Font.BOLD, 17));
		usernameHPLabel.setForeground(Color.WHITE);
		usernameHPLabel.setName("usernameHPLabel");
		usernameHPLabel.setBounds(113, 37, 106, 16);
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
		logoutHPButton.setBounds(110, 61, 74, 21);
		logoutHPButton.setBorderPainted(false);
		logoutHPButton.setBackground(new Color(0, 128, 128));
		logoutHPButton.setOpaque(false);
		logoutHPButton.setContentAreaFilled(false);
		logoutHPButton.setBorder(null);
		logoutHPButton.setForeground(new Color(128, 0, 0));
		logoutHPButton.setFont(new Font("Corbel", Font.BOLD, 21));
		homePagePanel.add(logoutHPButton);
		
		gamesNumberHPLabel = new JLabel("999");
		gamesNumberHPLabel.setIconTextGap(10);
		gamesNumberHPLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
		gamesNumberHPLabel.setOpaque(true);
		gamesNumberHPLabel.setBackground(SystemColor.controlDkShadow);
		gamesNumberHPLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gamesNumberHPLabel.setForeground(Color.WHITE);
		gamesNumberHPLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		gamesNumberHPLabel.setToolTipText("Number of Games You Like");
		gamesNumberHPLabel.setName("gamesNumberHPLabel");
		gamesNumberHPLabel.setBounds(629, 13, 141, 69);
		gamesNumberHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/controller.png")).getImage().getScaledInstance(69, 50, Image.SCALE_SMOOTH)));
		homePagePanel.add(gamesNumberHPLabel);
		
		followerNumberHPLabel = new JLabel("999");
		followerNumberHPLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
		followerNumberHPLabel.setOpaque(true);
		followerNumberHPLabel.setBackground(SystemColor.controlDkShadow);
		followerNumberHPLabel.setForeground(Color.WHITE);
		followerNumberHPLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		followerNumberHPLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		followerNumberHPLabel.setToolTipText("Number of  People Who Follow You");
		followerNumberHPLabel.setName("followerNumberHPLabel");
		followerNumberHPLabel.setBounds(782, 13, 128, 69);
		followerNumberHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/followers.png")).getImage().getScaledInstance(69, 50, Image.SCALE_SMOOTH)));
		homePagePanel.add(followerNumberHPLabel);
		
		userTypeIconHPLabel = new JLabel("");
		userTypeIconHPLabel.setOpaque(true);
		userTypeIconHPLabel.setName("userTypeIconHPLabel");
		userTypeIconHPLabel.setBounds(27, 13, 74, 69);
		homePagePanel.add(userTypeIconHPLabel);
		
		adminHPButton = new JButton("");
		adminHPButton.setContentAreaFilled(false);
		adminHPButton.setOpaque(true);
		adminHPButton.setName("adminHPButton");
		adminHPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "adminPanel");
			}
		});
		adminHPButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		adminHPButton.setFont(new Font("Corbel", Font.PLAIN, 13));
		adminHPButton.setBounds(286, 13, 97, 69);
		adminHPButton.setToolTipText("Click Here To Enter into Admin Section");
		adminHPButton.setBackground(SystemColor.controlDkShadow);
		adminHPButton.setBorder(null);
		adminHPButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/gear.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		homePagePanel.add(adminHPButton);
		
		becomeAnalystButton = new JButton("");
		becomeAnalystButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if( logicHandler.becomeAnalyst(currentUser) ) {
					
					becomeAnalystButton.setVisible(false);
					analystHPButton.setVisible(true);
				}
			}
		});
		becomeAnalystButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		becomeAnalystButton.setBounds(395, 13, 97, 69);
		becomeAnalystButton.setToolTipText("Click Here To Become an Analyst");
		becomeAnalystButton.setBackground(SystemColor.controlDkShadow);
		becomeAnalystButton.setContentAreaFilled(false);
		becomeAnalystButton.setBorder(null);
		becomeAnalystButton.setOpaque(true);
		becomeAnalystButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/becomeAnalyst.png")).getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH)));
		homePagePanel.add(becomeAnalystButton);
		
		analystHPButton = new JButton("");
		analystHPButton.setName("analystHPButton");
		analystHPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "analystPanel");
			}
		});
		analystHPButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		analystHPButton.setBounds(504, 13, 97, 69);
		analystHPButton.setToolTipText("Click Here To Enter into Analyst Section");
		analystHPButton.setBackground(SystemColor.controlDkShadow);
		analystHPButton.setBorder(null);
		analystHPButton.setContentAreaFilled(false);
		analystHPButton.setOpaque(true);
		analystHPButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/statistics.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		homePagePanel.add(analystHPButton);
		
		followedTableScrollPane = new JScrollPane();
		followedTableScrollPane.setName("followedTableScrollPane");
		followedTableScrollPane.setBackground(Color.BLACK);
		followedTableScrollPane.setBounds(27, 142, 356, 168);
		homePagePanel.add(followedTableScrollPane);
		
		followedTable = new JTable();
		followedTable.setName("followedTable");
		followedTable.setModel(followedTableModel);
		followedTable.setFont(new Font("Corbel",Font.PLAIN,16));
		followedTable.getColumnModel().getColumn(2).setPreferredWidth(77);
		followedTableHeader = followedTable.getTableHeader();
		followedTableHeader.setFont(titleFont);
		followedTableHeader.setForeground(Color.WHITE);
		followedTableHeader.setBackground(new Color(121,166,210));
		followedTableScrollPane.setViewportView(followedTable);
		
		myGamesScrollPane = new JScrollPane();
		myGamesScrollPane.setName("myGamesScrollPane");
		myGamesScrollPane.setBounds(27, 348, 356, 174);
		homePagePanel.add(myGamesScrollPane);
		
		myGamesList = new JList<PreviewGame>(gamesListModel);
		myGamesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				PreviewGame game = myGamesList.getSelectedValue();
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				initializeGamePage(game.getGameTitle());
			}
		});
		myGamesList.setName("myGamesList");
		myGamesList.setCellRenderer(new GameRenderer());
		myGamesScrollPane.add(myGamesList);
		
		searchGameLabel = new JLabel("");
		searchGameLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		searchGameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchGameLabel.setBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255), 2), "Find Game", TitledBorder.LEADING, TitledBorder.BOTTOM, titleFont, Color.WHITE));
		searchGameLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "searchGamePanel");
			}
		});
		searchGameLabel.setVerticalAlignment(SwingConstants.TOP);
		searchGameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		searchGameLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		searchGameLabel.setForeground(Color.WHITE);
		searchGameLabel.setBounds(714, 142, 196, 380);
		searchGameLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/testPicture.png")).getImage().getScaledInstance(185, 350, Image.SCALE_SMOOTH)));
		homePagePanel.add(searchGameLabel);
		
		mostViewedGamesLabel = new JLabel("");
		mostViewedGamesLabel.setVerticalAlignment(SwingConstants.TOP);
		mostViewedGamesLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mostViewedGamesLabel.setAlignmentY(0.0f);
		mostViewedGamesLabel.setToolTipText("Click Here to See the Most Viewed Game");
		mostViewedGamesLabel.setBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255), 2), "Most Viewed Games", TitledBorder.LEADING, TitledBorder.BOTTOM, titleFont, new Color(255, 255, 255)));
		mostViewedGamesLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		mostViewedGamesLabel.setVerticalTextPosition(SwingConstants.TOP);
		mostViewedGamesLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				initializeGamePage(logicHandler.getMostPopularGame().getGameTitle());
			}
		});
		mostViewedGamesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mostViewedGamesLabel.setForeground(Color.WHITE);
		mostViewedGamesLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		mostViewedGamesLabel.setName("mostViewedGamesLabel");
		mostViewedGamesLabel.setBounds(427, 142, 223, 168);
		mostViewedGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/testPicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));
		homePagePanel.add(mostViewedGamesLabel);
		
		mostPopularGamesLabel = new JLabel("");
		mostPopularGamesLabel.setToolTipText("Click Here to See the Most Popular Game");
		mostPopularGamesLabel.setVerticalTextPosition(SwingConstants.TOP);
		mostPopularGamesLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		mostPopularGamesLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mostPopularGamesLabel.setBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255), 2), "Most Popular Games", TitledBorder.LEADING, TitledBorder.BOTTOM, titleFont, Color.WHITE));
		mostPopularGamesLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				initializeGamePage(logicHandler.getMostPopularGame().getGameTitle());
			
			}
		});
		mostPopularGamesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mostPopularGamesLabel.setVerticalAlignment(SwingConstants.TOP);
		mostPopularGamesLabel.setForeground(Color.WHITE);
		mostPopularGamesLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		mostPopularGamesLabel.setName("mostPopularGamesLabel");
		mostPopularGamesLabel.setBounds(427, 348, 223, 168);
		mostPopularGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/testPicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));
		homePagePanel.add(mostPopularGamesLabel);
		
		adminPanel = new JPanel();
		adminPanel.setName("adminPanel");
		adminPanel.setBackground(new Color(87, 86, 82));
		panel.add(adminPanel, "adminPanel");
		adminPanel.setLayout(null);
		
		homeADButton = new JButton("");
		homeADButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanAdminPage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage(userType.ADMINISTRATOR,currentUser);
			}
		});
		homeADButton.setToolTipText("Return to Homepage");
		homeADButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeADButton.setName("homeADButton");
		homeADButton.setBounds(730, 30, 97, 69);
		homeADButton.setBackground(SystemColor.controlDkShadow);
		homeADButton.setBorder(null);
		homeADButton.setContentAreaFilled(false);
		homeADButton.setOpaque(true);
		homeADButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		adminPanel.add(homeADButton);
		
		adminActionContainer = new JPanel();
		adminActionContainer.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		adminActionContainer.setName("adminActionContainer");
		adminActionContainer.setBackground(Color.LIGHT_GRAY);
		adminActionContainer.setBounds(446, 151, 384, 196);
		adminPanel.add(adminActionContainer);
		adminActionContainer.setLayout(null);
		
		deleteUserTextField = new JTextField();
		deleteUserTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				deleteUserTextField.setText("");
			}
		});
		deleteUserTextField.setBounds(12, 30, 189, 37);
		adminActionContainer.add(deleteUserTextField);
		deleteUserTextField.setText("User");
		deleteUserTextField.setFont(new Font("Corbel", Font.ITALIC, 17));
		deleteUserTextField.setName("deleteUserTextField");
		deleteUserTextField.setColumns(10);
		
		deleteGameTextField = new JTextField();
		deleteGameTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				deleteGameTextField.setText("");
			}
		});
		deleteGameTextField.setBounds(12, 114, 189, 37);
		adminActionContainer.add(deleteGameTextField);
		deleteGameTextField.setFont(new Font("Corbel", Font.ITALIC, 17));
		deleteGameTextField.setText("Game");
		deleteGameTextField.setName("deleteGameTextField");
		deleteGameTextField.setColumns(10);
		
		deleteUserButton = new JButton("Delete User");
		deleteUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String username = deleteUserTextField.getText();
				
				if( username == "" ) {
					deleteUserResultLabel.setText("Failure!");
					deleteUserResultLabel.setVisible(true);
				}
				
				if( logicHandler.deleteUser(username) ) {
					deleteUserResultLabel.setText("Success!");
					deleteUserResultLabel.setVisible(true);
				} else {
					deleteUserResultLabel.setText("Failure!");
					deleteUserResultLabel.setVisible(true);
				}
				
				new Timer(3000,new ActionListener() {
				      public void actionPerformed(ActionEvent evt) {
				          deleteUserResultLabel.setVisible(false);
				      }
				  }).start();
			}
		});
		deleteUserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		deleteUserButton.setBounds(227, 29, 133, 37);
		adminActionContainer.add(deleteUserButton);
		deleteUserButton.setBorder(new LineBorder(Color.RED, 1, true));
		deleteUserButton.setName("deleteUserButton");
		deleteUserButton.setForeground(Color.WHITE);
		deleteUserButton.setFont(new Font("Corbel", Font.BOLD, 18));
		deleteUserButton.setBackground(Color.RED);
		deleteUserButton.setContentAreaFilled(false);
		deleteUserButton.setOpaque(true);
		
		deleteGameButton = new JButton("Delete Game");
		deleteGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String game = deleteGameTextField.getText();
				
				if( game == "" ) {
					deleteGameResultLabel.setText("Failure!");
					deleteGameResultLabel.setVisible(true);
				}
				
				if( logicHandler.deleteUser(game) ) {
					deleteGameResultLabel.setText("Success!");
					deleteGameResultLabel.setVisible(true);
				} else {
					deleteGameResultLabel.setText("Failure!");
					deleteGameResultLabel.setVisible(true);
				}
				
				new Timer(3000,new ActionListener() {
				      public void actionPerformed(ActionEvent evt) {
				          deleteGameResultLabel.setVisible(false);
				      }
				  }).start();
			
			}
		});
		deleteGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		deleteGameButton.setBorder(new LineBorder(Color.RED, 2, true));
		deleteGameButton.setBounds(227, 113, 133, 37);
		adminActionContainer.add(deleteGameButton);
		deleteGameButton.setForeground(Color.WHITE);
		deleteGameButton.setName("deleteGameButton");
		deleteGameButton.setFont(new Font("Corbel", Font.BOLD, 18));
		deleteGameButton.setBackground(Color.RED);
		deleteGameButton.setContentAreaFilled(false);
		deleteGameButton.setOpaque(true);
		
		deleteUserResultLabel = new JLabel("Success!");
		deleteUserResultLabel.setName("deleteUserResultLabel");
		deleteUserResultLabel.setFont(new Font("Corbel", Font.PLAIN, 14));
		deleteUserResultLabel.setBounds(12, 68, 189, 21);
		adminActionContainer.add(deleteUserResultLabel);
		
		deleteGameResultLabel = new JLabel("Success!");
		deleteGameResultLabel.setName("deleteGameResultLabel");
		deleteGameResultLabel.setFont(new Font("Corbel", Font.PLAIN, 14));
		deleteGameResultLabel.setBounds(12, 150, 189, 21);
		adminActionContainer.add(deleteGameResultLabel);
		
		adminSectionLabel = new JLabel("Admin Section");
		adminSectionLabel.setName("adminSectionLabel");
		adminSectionLabel.setForeground(Color.WHITE);
		adminSectionLabel.setFont(new Font("Corbel", Font.BOLD, 41));
		adminSectionLabel.setBounds(104, 40, 289, 49);
		adminPanel.add(adminSectionLabel);
		
		updateDatabaseButton = new JButton("Update Database");
		updateDatabaseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if( logicHandler.updateDatabase() ) {
					updateDatabaseResultLabel.setText("Success!");
					updateDatabaseResultLabel.setVisible(true);
				} else {
					updateDatabaseResultLabel.setText("Failure!");
					updateDatabaseResultLabel.setVisible(true);
				}
				
				new Timer(3000,new ActionListener() {
				      public void actionPerformed(ActionEvent evt) {
				          updateDatabaseResultLabel.setVisible(false);
				      }
				  }).start();
			}
		});
		updateDatabaseButton.setBackground(new Color(30, 144, 255));
		updateDatabaseButton.setForeground(Color.WHITE);
		updateDatabaseButton.setFont(new Font("Corbel", Font.BOLD, 20));
		updateDatabaseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		updateDatabaseButton.setName("updateDatabaseButton");
		updateDatabaseButton.setBounds(339, 410, 218, 49);
		updateDatabaseButton.setContentAreaFilled(false);
		updateDatabaseButton.setOpaque(true);
		adminPanel.add(updateDatabaseButton);
		
		userCountLabel = new JLabel("User Count: 10000");
		userCountLabel.setName("userCountLabel");
		userCountLabel.setForeground(Color.WHITE);
		userCountLabel.setFont(new Font("Corbel", Font.BOLD, 26));
		userCountLabel.setBounds(104, 180, 226, 35);
		adminPanel.add(userCountLabel);
		
		gameCountLabel = new JLabel("Game Count: 10000");
		gameCountLabel.setName("gameCountLabel");
		gameCountLabel.setForeground(Color.WHITE);
		gameCountLabel.setFont(new Font("Corbel", Font.BOLD, 26));
		gameCountLabel.setBounds(104, 269, 226, 35);
		adminPanel.add(gameCountLabel);
		
		updateDatabaseResultLabel = new JLabel("Success!");
		updateDatabaseResultLabel.setName("updateDatabaseResultLabel");
		updateDatabaseResultLabel.setFont(new Font("Corbel", Font.PLAIN, 14));
		updateDatabaseResultLabel.setBounds(418, 461, 57, 21);
		adminPanel.add(updateDatabaseResultLabel);
		
		analystPanel = new JPanel();
		analystPanel.setBackground(new Color(87, 86, 82));
		panel.add(analystPanel, "analystPanel");
		analystPanel.setLayout(null);
		
		searchGamePanel = new JPanel();
		searchGamePanel.setBackground(new Color(87, 86, 82));
		searchGamePanel.setName("searchGamePanel");
		panel.add(searchGamePanel, "searchGamePanel");
		searchGamePanel.setLayout(null);
		
		homeSEButton = new JButton("");
		homeSEButton.setName("homeSEButton");
		homeSEButton.setBounds(63, 34, 97, 70);
		homeSEButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanSearchGamePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				//initializeHomePage(userType.,currentUser);
			}
		});
		homeSEButton.setToolTipText("Return to Homepage");
		homeSEButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeSEButton.setBackground(SystemColor.controlDkShadow);
		homeSEButton.setBorder(null);
		homeSEButton.setContentAreaFilled(false);
		homeSEButton.setOpaque(true);
		homeSEButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		searchGamePanel.add(homeSEButton);
		
		searchTextField = new JTextField();
		searchTextField.setText("Search");
		searchTextField.setFont(new Font("Corbel", Font.ITALIC, 16));
		searchTextField.setName("searchTextField");
		searchTextField.setBounds(589, 69, 207, 35);
		searchGamePanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		searchButton = new JButton("");
		searchButton.setName("searchButton");
		searchButton.setBounds(797, 69, 52, 35);
		searchButton.setToolTipText("Return to Homepage");
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBackground(SystemColor.controlDkShadow);
		searchButton.setBorder(null);
		searchButton.setContentAreaFilled(false);
		searchButton.setOpaque(true);
		searchButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/search.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		searchGamePanel.add(searchButton);
		
		gamePanel = new JPanel();
		gamePanel.setBackground(new Color(87, 86, 82));
		gamePanel.setName("gamePanel");
		panel.add(gamePanel, "gamePanel");
		gamePanel.setLayout(null);
	}
}
