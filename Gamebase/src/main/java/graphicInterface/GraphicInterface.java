package graphicInterface;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.util.List;

import logic.*;
import logic.data.*;
import logic.graphConnector.GraphConnector;
import logic.mongoConnection.DataNavigator;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;

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
	private JButton userButton;
	private JButton adminHPButton;
	private JButton becomeAnalystButton;
	private JButton analystHPButton;
	private JButton userInfoButton ;
	private JScrollPane myGamesScrollPane;
	private JLabel mostPopularGamesLabel;
	private JLabel mostViewedGamesLabel;
	private JLabel searchGameLabel;
	private JScrollPane followedTableScrollPane;
	private JTable followedTable;
	private JTableHeader followedTableHeader;
	private JList<PreviewGame> myGamesList;
	private DefaultListModel<PreviewGame> gamesListModel = new DefaultListModel<PreviewGame>();
	private DefaultTableModel followedTableModel = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Username", "Name", "Email"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			@SuppressWarnings("unchecked")
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};
	
	///////// ADMIN PANEL
		
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
	
	///////// ANALYST PANEL
	private JPanel analystPanel;
		
	///////// SEARCH GAME PANEL
	private JPanel searchGamePanel;	
	private JButton homeSEButton;
	private JButton featuredButton;
	private JButton mostViewedButton;
	private JButton mostLikedButton;
	private JButton mostRecentButton;
	private JTextField searchTextField;
	private JButton searchButton;
	private JScrollPane searchGameScrollPane;
	private JList<PreviewGame> searchGamesJList;	
	private DefaultListModel<PreviewGame> searchListModel = new DefaultListModel<PreviewGame>();
	private JMenu gameGenreMenu;
	private JMenuBar gameGenreMenuBar;
	private AdjustmentListener searchGamesVerticalScrollBarListener;
	private JScrollBar searchGamesVerticalScrollBar;
	private DataNavigator searchGamesDataNavigator;
	
	//game panel
	private JPanel gamePanel;	
	private JTextArea gameDescriptionTextArea;
	private JScrollPane gameDescriptionScrollPane;
	private JButton playStationButton;
	private JButton nintendoButton;
	private JButton steamButton;
	private JLabel previewImageLabel;
	private JLabel gameTitleLabel;
	private ActionListener steamButtonListener;
	private ActionListener nintendoButtonListener;
	private ActionListener playstationButtonListener;
	private ActionListener xboxButtonListener;
	private JButton homeGameButton;
	private JLabel developerLabel;
	private DefaultListModel<Image> imagesListModel = new DefaultListModel<Image>();
	private JScrollPane gameImagesScrollPane;
	private JList<Image> imagesList;
	private JMenuBar voteMenuBar;
	private JMenu voteMenu;
	private JMenuItem vote1;
	private JMenuItem vote2;
	private JMenuItem vote3;
	private JMenuItem vote4;
	private JMenuItem vote5;
	private JLabel metacriticScoreLabel;
	private JButton actionButton;
	private JLabel releaseDateLabel;
	private VideoPlayerPanel videoPlayer;
	private JButton previousVideoButton;
	private JButton nextVideoButton;
	private JButton XBoxButton;
	
	//user panel
	private JPanel userPanel;
	private JButton searchUserButton;
	private JButton homeUserButton;
	private JScrollPane userGamesScrollPane;
	private JList<PreviewGame> userGamesList;
	private DefaultListModel<PreviewGame> userGamesListModel = new DefaultListModel<PreviewGame>();
	private JScrollPane usersScrollPane;
	private JLabel displayedUserLabel;
	private JButton featuredUserButton;
	private JTextField searchUserTextField;
	private JTable usersTable;
	private JTableHeader usersTableHeader;
	private DefaultTableModel usersTableModel = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Username", "Games", "Action"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
	};
	
	//user information panel
	private JPanel userInformationPanel;	
	private JButton homeUserInformationButton;
	private JTextField ageTextField;
	private JTextField nameTextField;
	private JTextField surnameTextfield;
	private JButton saveButton;
	private JLabel updateInfoLabel;
	private JMenu genreMenu;
	private JMenuBar genreMenuBar;
	private JMenuItem femaleMenuItem;
	private JMenuItem maleMenuItem;
	private JMenu genderMenu;
	private JMenuBar genderMenuBar;
	private JTextField emailTextField;
	
	//Logic and support info
	private LogicBridge logicHandler = new LogicBridge();
	private GraphConnector graphHandler = new GraphConnector();
	private User currentUser = null;
	private UserType currentUsertype = null;
	private Game currentGame = null;
	private Font titleFont = new Font("Corbel", Font.BOLD, 20);
	private List<PreviewGame> supportGamesList = null;
	private List<String> currentVideosURLlist = null;
	private int currentVideoIndex = 0;
	private int lastVideoIndex = 0;
	
	//support functions
	
	private boolean fillGamesList(List<PreviewGame> gamesList) {
		
		gamesListModel.removeAllElements();
		
		for( int i = 0; i < gamesList.size(); i++ ) {
			gamesListModel.addElement(gamesList.get(i));
		}
		
		return true;
	}
	
	private void fillFollowedTable(List<User> friendList) {
		
		followedTableModel.setRowCount(0);
		
		for( User friend: friendList ) {
			
			Object[] object = new Object[3];
			object[0] = friend.getUsername();
			object[1] = friend.getCompleteName()!=null?friend.getCompleteName():"N/A";
			object[2] = friend.getEmail()!=null?friend.getEmail():"N/A";
			ButtonColumn buttonColumn = new ButtonColumn(followedTable, new AbstractAction() {
				
				public void actionPerformed(ActionEvent e) {
					
					JTable table = (JTable)e.getSource();
					int modelRow = Integer.valueOf(e.getActionCommand());
					
					String followerUsername = (String)((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
					
					CardLayout cl = (CardLayout)(panel.getLayout());
					
					cl.show(panel, "userPanel");
					
					initializeUserPage(currentUser.getUsername(),followerUsername);
				}
			},0);
			followedTableModel.addRow(object);
		}
	}
	
	private boolean fillSearchedGamesList(List<PreviewGame> games) {
		
		if( games == null ) {
			return false;
		}
		
		searchListModel.removeAllElements();
		
		for( int i = 0; i < games.size(); i++ ) {
			gamesListModel.addElement(games.get(i));
		}
		
		return true;
	}
	
	
	private void fillUsersTable( List<User> usersList ) {
		
		usersTableModel.setRowCount(0);
		
		for( User friend: usersList ) {
			
			StatusObject<Boolean> followStatus = graphHandler.doIFollow(friend.getUsername());
			if( followStatus.statusCode != StatusCode.OK ) {
				continue;
			}
			
			Object[] object = new Object[3];
			object[0] = friend.getUsername();
			object[1] = "SEE GAMES";
			object[2] = followStatus.element?"UNFOLLOW":"FOLLOW";
			ButtonColumn buttonColumnGames = new ButtonColumn(followedTable, new AbstractAction() {
				
				public void actionPerformed(ActionEvent e) {
					
					JTable table = (JTable)e.getSource();
					int modelRow = Integer.valueOf(e.getActionCommand());
					
					String selectedUsername = (String)((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
					
					fillUserGamesList(logicHandler.getMyGames(selectedUsername));
					
					User selectedFriend = logicHandler.getFriend(selectedUsername);
					String email;
					
					if( selectedFriend != null && selectedFriend.getEmail() != null ) {
						email = selectedFriend.getEmail();
					}else {
						email = "N/A";
					}
					
					displayedUserLabel.setText("Currently Displayed: " + selectedUsername + "'s Games. E-Mail: " + email);
					
				}
			},1);
			ButtonColumn buttonColumnAction = new ButtonColumn(followedTable, new AbstractAction() {
				
				public void actionPerformed(ActionEvent e) {

					JTable table = (JTable)e.getSource();
					int modelRow = Integer.valueOf(e.getActionCommand());
					
					String selectedUsername = (String)((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
					
					if( logicHandler.isFollowed(currentUser, selectedUsername)) {
						
						if( !logicHandler.follow(currentUser,selectedUsername) ) {
							return;
						}
						usersTableModel.setValueAt("UNFOLLOW", modelRow, 2);
					} else {
						
						if( !logicHandler.unfollow(currentUser,selectedUsername)) {
							return;
						}
						usersTableModel.setValueAt("FOLLOW", modelRow, 2);
					}
				}
			},2);
			followedTableModel.addRow(object);
		}
	}
	
	private void fillUserGamesList(List<PreviewGame> gamesList) {
		
		userGamesListModel.removeAllElements();
		
		if( gamesList == null ) {
			return;
		}
		
		for( int i = 0; i < gamesList.size(); i++ ) {
			gamesListModel.addElement(gamesList.get(i));
		}
	}
	
	private void fillImagesList(List<String> imagesURLList) {
		
		imagesListModel.removeAllElements();
		
		if( imagesURLList == null ) {
			return;
		}

		Image image = null;
		URL url;
		
		for( int i=0; i < imagesURLList.size(); i++ ) {
			try {
				url = new URL(imagesURLList.get(i));
				image = ImageIO.read(url);
			} catch (Exception e) {
				try {
					image = ImageIO.read(new File("/resources/defaultGamePicture.png"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			imagesListModel.addElement(image);
		}
	}
	
	private void initializeHomePage( UserType user, String username ) {
		
		String gamesNumber = logicHandler.getLikedGamesNumber(username)!=-1?Integer.toString(logicHandler.getLikedGamesNumber(username)):"N/A";
		String followersNumber = logicHandler.getFollowersNumber(username)!=-1?Integer.toString(logicHandler.getFollowersNumber(username)):"N/A";
		
		gamesNumberHPLabel.setText(gamesNumber);
		followerNumberHPLabel.setText(followersNumber);
		
		gamesNumberHPLabel.setToolTipText("You Have " + gamesNumber + " preferred games");
		followerNumberHPLabel.setToolTipText(followersNumber + " people follow you");
		
		usernameHPLabel.setText(username);
		
		String mostViewedGameImageURL = logicHandler.getMostViewedPreview().element.getPreviewPicURL();
				
		if( mostViewedGameImageURL == null ) {
			mostViewedGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		} else {
			try {
				mostViewedGamesLabel.setIcon(new ImageIcon(ImageIO.read(new URL(mostViewedGameImageURL))));
			} catch (Exception e){
				mostViewedGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		    }
		}	
		
		String mostPopularGameImageURL = logicHandler.getMostPopularPreview().element.getPreviewPicURL();
		
		if( mostPopularGameImageURL == null ) {
			mostPopularGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		} else {
			try {
				mostPopularGamesLabel.setIcon(new ImageIcon(ImageIO.read(new URL(mostPopularGameImageURL))));
			} catch (Exception e){
				mostPopularGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_SMOOTH)));	
		    }
		}	
				
		userTypeIconHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultProfilePicture.png")).getImage().getScaledInstance(83, 83, Image.SCALE_SMOOTH)));
		
		
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
		
		Game game = logicHandler.getGame(title);
		
		if( game == null ) {
			
			cleanGamePage();
			
			CardLayout cl = (CardLayout)(panel.getLayout());
			
			cl.show(panel, "homePagePanel");
			
		}
		
		currentGame = game;
		
		gameDescriptionTextArea.setText(game.getDescription());
		gameTitleLabel.setText(game.getTitle());
		
		final String steamURL = game.getSteamURL();
		
		if( steamURL != null ) {
			
			steamButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI(steamURL));
						} catch (Exception e) {
							e.printStackTrace();
					    }
				    }	
				}
			};
			steamButton.addActionListener(steamButtonListener);
		} else {
			steamButton.setVisible(false);
		}
		
		final String nintendoURL = game.getNintendoURL();
		
		if( nintendoURL != null ) {
			
			nintendoButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI(nintendoURL));
						} catch (Exception e) {
							e.printStackTrace();
					    }
				    }	
				}
			};
			nintendoButton.addActionListener(nintendoButtonListener);
		} else {
			nintendoButton.setEnabled(false);
		}
		
		final String playstationURL = game.getPlaystationURL();
		
		if( playstationURL != null ) {
			
			playstationButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI(playstationURL));
						} catch (Exception e) {
							e.printStackTrace();
					    }
				    }	
				}
			};
			playStationButton.addActionListener(playstationButtonListener);
		} else {
			playStationButton.setEnabled(false);
		}
		
		final String xboxURL = game.getXboxURL();
		
		if( xboxURL != null ) {
			
			xboxButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI(xboxURL));
						} catch (Exception e) {
							e.printStackTrace();
					    }
				    }	
				}
			};
			XBoxButton.addActionListener(xboxButtonListener);
		} else {
			playStationButton.setEnabled(false);
		}
		
		List<String> imagesURL = game.getImagesURLs();
		
		fillImagesList(imagesURL);
		
		double score = game.getMetacritic();
			
		if( score == -1 ) {
			metacriticScoreLabel.setText("N/A");
		} else {
			metacriticScoreLabel.setText(Double.toString(score));
		}
		
		List<String> videoURLs = game.getVideoURLs();
		
		if( videoURLs != null && videoURLs.size()!=0 ) {
			
			currentVideosURLlist = videoURLs;
			currentVideoIndex = 0;
			lastVideoIndex = videoURLs.size()-1;
			
			String firstVideo = videoURLs.get(0);
			videoPlayer.getVideo(firstVideo);
			
			if( videoURLs.size() > 1 ) {
				
				nextVideoButton.setEnabled(true);
			}
		} else {
			
			nextVideoButton.setEnabled(false);
			previousVideoButton.setEnabled(false);
			videoPlayer.getVideo(null);
		}
		
		
	}
	
	private void cleanGamePage() {
		
		gameDescriptionTextArea.setText("");
		
		steamButton.setVisible(true);
		nintendoButton.setVisible(true);
		playStationButton.setVisible(true);
		
		steamButtonListener = null;
		steamButton.addActionListener(null);
		
		nintendoButtonListener = null;
		nintendoButton.addActionListener(null);
		
		playstationButtonListener = null;
		playStationButton.addActionListener(null);
		
		xboxButtonListener = null;
		XBoxButton.addActionListener(null);
		
		metacriticScoreLabel.setText("");
		
		currentGame = null;
		
		imagesListModel.removeAllElements();
		
		previewImageLabel.setIcon(null);
		
		currentVideosURLlist = null;
		currentVideoIndex = 0;
		lastVideoIndex = 0;
		
		nextVideoButton.setEnabled(true);
		previousVideoButton.setEnabled(true);
	}
	
	private void initializeUserPage( String currentUser, String searchedUser ) {
		
		featuredUserButton.setBackground(new Color(30, 144, 255));
		featuredUserButton.setForeground(Color.WHITE);
		
		List<User> featuredUsers = logicHandler.getFeaturedUsers(currentUser);
		
		fillUsersTable(featuredUsers);
		
		String displayedUser = searchedUser==null?featuredUsers.get(0).getUsername():searchedUser;
		
		fillUserGamesList(logicHandler.getMyGames(displayedUser));
		
		displayedUserLabel.setText("Currently Displayed: " + displayedUser + "'s Games. E-Mail: " + logicHandler.getFriend(displayedUser).getEmail() );
		
		searchUserTextField.setText("Search User");
	}
	
	private void cleanUserPage() {
		
		usersTableModel.setRowCount(0);
		userGamesListModel.removeAllElements();
		displayedUserLabel.setText("");
		searchUserTextField.setText("Search User");
		featuredUserButton.setBackground(new Color(30, 144, 255));
		featuredUserButton.setForeground(Color.WHITE);
	}
	
	private void initializeAdminPage() {
		
		String userCount, gameCount;
		int userC = logicHandler.getUserCount();
		StatusObject<Long> gameCounter = logicHandler.getGameCount();
		
		if( userC == -1 ) {
			userCount = "N/A";
		} else {
			userCount = Integer.toString(userC);
		}
		
		if( gameCounter.statusCode == StatusCode.OK ) {
			gameCount = "N/A";
		} else {
			gameCount = Long.toString(gameCounter.element);
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
		
		featuredButton.setForeground(Color.WHITE);
		featuredButton.setBackground(new Color(30,144,255));
		
		searchGamesDataNavigator = null;
		
		searchTextField.setText("Search");
		
		StatusObject<List<String>>genresStatus = logicHandler.getGenres();
		
		if( genresStatus.statusCode == StatusCode.OK ) {
			
			for( final String genre : genresStatus.element ) {

				JMenuItem item = new JMenuItem(genre);
				item.setFont(new Font("Corbel", Font.BOLD, 15));
				item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						List<PreviewGame> genreList = new ArrayList<PreviewGame>();
						PreviewGame previewGame;
						
						for( int i=0; i<supportGamesList.size(); i++ ) {
							
							previewGame = supportGamesList.get(i);
							
							StatusObject<Game> gameStatus = logicHandler.getGame(previewGame.getId());
							
							if( gameStatus.statusCode == StatusCode.OK ) {
								List<String> genres = gameStatus.element.getGenres();
								for( String gen: genres)
								if( gen.compareTo(genre) == 0 ) {
									genreList.add(previewGame);
									break;
							    }
							}
							
						}
						
						fillSearchedGamesList(genreList);
						
					}
				});
				gameGenreMenu.add(item);
			}
		}
		fillSearchedGamesList(logicHandler.getFeaturedGames(currentUser));
		
	}
	
	private void cleanSearchGamePage() {
		
		searchListModel.removeAllElements();
		gameGenreMenu.removeAll();
		
		searchGamesDataNavigator = null;
		
	}
	
	private void initializeUserInformationPage() {
		
		updateInfoLabel.setText("Hi " + currentUser + ", update your information");
		
		User user = logicHandler.getFriend(currentUser);
		
		String currentAge = Long.toString(user.getAge());
		String currentName = user.getFirstName();
		String currentSurname = user.getLastName();
		String currentFavoriteGenre = user.getFavouriteGenre();
		String currentEmail = user.getEmail();
		Character gender = user.getGender();
		
		ageTextField.setText("Age - Current Value " + currentAge!=null?currentAge:"null");
		nameTextField.setText("Name - Current Value " + currentName!=null?currentName:"null");
		surnameTextfield.setText("Surname - Current Value " + currentSurname!=null?currentSurname:"null");
		emailTextField.setText("Email - Current Value " + currentEmail!=null?currentEmail:"null");
		
		if( gender == 'M' ) {
			genderMenu.setText("M");
		} else if( gender == 'F' ) {
			genderMenu.setText("F");
		}
		
		StatusObject<List<String>> genresStatus = logicHandler.getGenres();
		
		if( genresStatus.statusCode == StatusCode.OK ) {
			
			List<String> genres = genresStatus.element;
			
			for( int i = 0; i < genres.size(); i++ ) {
				
				final String genre = genres.get(i);
				JMenuItem item = new JMenuItem(genre);
				item.setFont(new Font("Corbel", Font.BOLD, 15));
				item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						genreMenu.setText(genre);
					}
				});
				genreMenu.add(item);
				
				if( genre == currentFavoriteGenre ) {
					genreMenu.setText(genre);
				}
			}
		}
	}
	
	private void cleanUserInformationPage() {
		
		updateInfoLabel.setText("");
		nameTextField.setText("");
		surnameTextfield.setText("");
		
		genreMenu.removeAll();
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
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				logicHandler.closeConnection();
				graphHandler.close();
			}
		});
		frame.setBounds(100, 100, 952, 615);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new CardLayout(0, 0));
		
		///////LOGIN PANEL
		
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
					initializeHomePage(UserType.USER,username);
					currentUser = username;
					currentUsertype = UserType.USER;
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
				
				UserType usertype = logicHandler.login(username,password);
				
				if( usertype == UserType.NO_USER ) {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->login failed: no user " + username + " found");
					errorMessageLabel.setText("No User " + username + " found");
					errorMessageLabel.setVisible(true);
				} else if( usertype == UserType.WRONG_PASSWORD ) {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->login failed: wrong password for username " + username);
					errorMessageLabel.setText("Uncorrect Password for User " + username );
					errorMessageLabel.setVisible(true);
				} else {
					System.out.println("GRAPHICINTERFACE.JAVA/LOGINACTIONPERFORMED-->login completed:user " + username + " logged in");
					cl.show(panel, "homePagePanel");
					initializeHomePage( usertype, username );
					currentUser = username;
					currentUsertype = usertype;
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
		
		
		
		///////// HOME PAGE PANEL
		
		
		
		homePagePanel = new JPanel();
		homePagePanel.setName("homePagePanel");
		homePagePanel.setBackground(new Color(87, 86, 82));
		panel.add(homePagePanel, "homePagePanel");
		homePagePanel.setLayout(null);
		
		welcomeHPLabel = new JLabel("Welcome,");
		welcomeHPLabel.setForeground(Color.WHITE);
		welcomeHPLabel.setFont(new Font("Corbel", Font.PLAIN, 16));
		welcomeHPLabel.setName("usertypeHPLabel");
		welcomeHPLabel.setBounds(103, 13, 89, 16);
		homePagePanel.add(welcomeHPLabel);
		
		usernameHPLabel = new JLabel("username");
		usernameHPLabel.setFont(new Font("Corbel", Font.BOLD, 17));
		usernameHPLabel.setForeground(Color.WHITE);
		usernameHPLabel.setName("usernameHPLabel");
		usernameHPLabel.setBounds(103, 37, 89, 16);
		homePagePanel.add(usernameHPLabel);
		
		logoutHPButton = new JButton("Logout");
		logoutHPButton.setMargin(new Insets(2, 2, 2, 2));
		logoutHPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				currentUser = null;
				currentUsertype = null;
				cl.show(panel, "loginPanel");
			}
		});
		logoutHPButton.setToolTipText("Click Here To Logout");
		logoutHPButton.setName("logoutHPButton");
		logoutHPButton.setBounds(103, 61, 81, 21);
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
		gamesNumberHPLabel.setBorder(new EmptyBorder(0, 2, 0, 2));
		gamesNumberHPLabel.setOpaque(true);
		gamesNumberHPLabel.setBackground(SystemColor.controlDkShadow);
		gamesNumberHPLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gamesNumberHPLabel.setForeground(Color.WHITE);
		gamesNumberHPLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		gamesNumberHPLabel.setToolTipText("Number of Games You Like");
		gamesNumberHPLabel.setName("gamesNumberHPLabel");
		gamesNumberHPLabel.setBounds(653, 13, 128, 69);
		gamesNumberHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/controller.png")).getImage().getScaledInstance(69, 50, Image.SCALE_SMOOTH)));
		homePagePanel.add(gamesNumberHPLabel);
		
		followerNumberHPLabel = new JLabel("999");
		followerNumberHPLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
		followerNumberHPLabel.setOpaque(true);
		followerNumberHPLabel.setBackground(SystemColor.controlDkShadow);
		followerNumberHPLabel.setForeground(Color.WHITE);
		followerNumberHPLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		followerNumberHPLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		followerNumberHPLabel.setToolTipText("Number of  People Who Follow You");
		followerNumberHPLabel.setName("followerNumberHPLabel");
		followerNumberHPLabel.setBounds(793, 13, 117, 69);
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
				
				initializeAdminPage();
			}
		});
		adminHPButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		adminHPButton.setFont(new Font("Corbel", Font.PLAIN, 13));
		adminHPButton.setBounds(374, 13, 81, 69);
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
					currentUsertype = UserType.ANALYST;
				}
			}
		});
		becomeAnalystButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		becomeAnalystButton.setBounds(467, 13, 81, 69);
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
		analystHPButton.setBounds(560, 13, 81, 69);
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
		myGamesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
				
				initializeGamePage(game.getTitle());
			}
		});
		myGamesList.setName("myGamesList");
		myGamesList.setVisibleRowCount(-1);
		myGamesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
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
				
				initializeSearchGamePage();
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
				
				StatusObject<PreviewGame> viewedGameStatus = logicHandler.getMostPopularPreview();
				
				if( viewedGameStatus.statusCode == StatusCode.OK ) {
					initializeGamePage(viewedGameStatus.element.getTitle());
				} else {
					initializeGamePage(null);
				}
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
				
				StatusObject<PreviewGame> popularGameStatus = logicHandler.getMostPopularPreview();
				
				if( popularGameStatus.statusCode == StatusCode.OK ) {
					initializeGamePage(popularGameStatus.element.getTitle());
				} else {
					initializeGamePage(null);
				}
			
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
		
		userButton = new JButton("");
		userButton.setBackground(SystemColor.controlDkShadow);
		userButton.setName("userButton");
		userButton.setBounds(281, 13, 81, 69);
		userButton.setContentAreaFilled(false);
		userButton.setOpaque(true);
		userButton.setName("userButton");
		userButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "userPanel");
				
				initializeUserPage(currentUser,null);
			}
		});
		userButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		userButton.setFont(new Font("Corbel", Font.PLAIN, 13));
		userButton.setToolTipText("Click Here To Search for Other Users");
		userButton.setBorder(null);
		userButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/addFriend.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		homePagePanel.add(userButton);
		
		userInfoButton = new JButton("");
		userInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "userInformationPanel");
				
				initializeUserInformationPage();
			}
		});
		userInfoButton.setToolTipText("Click Here to Change your Info");
		userInfoButton.setBackground(SystemColor.controlDkShadow);
		userInfoButton.setContentAreaFilled(false);
		userInfoButton.setOpaque(true);
		userInfoButton.setName("userInfoButton");
		userInfoButton.setFont(new Font("Corbel", Font.PLAIN, 13));
		userInfoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		userInfoButton.setBorder(null);
		userInfoButton.setBounds(188, 13, 81, 69);
		userInfoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/info.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		homePagePanel.add(userInfoButton);
		
		
		
		////////// ADMIN PANEL
		
		
		
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
				
				initializeHomePage(UserType.ADMINISTRATOR,currentUser);
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
				}else if( logicHandler.deleteUser(username) ) {
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
				}else if( logicHandler.deleteGame(game) ) {
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
		
		
		
		/////////  ANALYST PANEL
		
		
		
		analystPanel = new JPanel();
		analystPanel.setBackground(new Color(87, 86, 82));
		panel.add(analystPanel, "analystPanel");
		analystPanel.setLayout(null);
		
		
		
		///////// SEARCH GAMES PANEL
		
		
		
		searchGamePanel = new JPanel();
		searchGamePanel.setFont(new Font("Corbel", Font.BOLD, 13));
		searchGamePanel.setBackground(new Color(87, 86, 82));
		searchGamePanel.setName("searchGamePanel");
		panel.add(searchGamePanel, "searchGamePanel");
		searchGamePanel.setLayout(null);
		
		homeSEButton = new JButton("");
		homeSEButton.setName("homeSEButton");
		homeSEButton.setBounds(12, 37, 97, 70);
		homeSEButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanSearchGamePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage(currentUsertype,currentUser);
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
		searchTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				searchTextField.setText("");
			}
		});
		searchTextField.setText("Search");
		searchTextField.setFont(new Font("Corbel", Font.ITALIC, 16));
		searchTextField.setName("searchTextField");
		searchTextField.setBounds(663, 72, 207, 35);
		searchGamePanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		searchButton = new JButton("");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String searchedString = searchTextField.getText();
				
				if( searchedString=="" ) {
					return;
				}
				
				StatusObject<DataNavigator> searchStatusObject  = logicHandler.searchGamesPreviews(searchedString);
				
				if( searchStatusObject.statusCode == StatusCode.OK ) {
					
					searchGamesDataNavigator = searchStatusObject.element;
					
					StatusObject<List <PreviewGame>> listStatusObject = searchStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( fillSearchedGamesList(listStatusObject.element) ) {
							supportGamesList = listStatusObject.element;
					    }
					}	
				}
				
				mostViewedButton.setBackground(Color.LIGHT_GRAY);
				mostViewedButton.setForeground(Color.BLACK);
				featuredButton.setForeground(Color.BLACK);
				featuredButton.setBackground(Color.LIGHT_GRAY);
				mostLikedButton.setForeground(Color.BLACK);
				mostLikedButton.setBackground(Color.LIGHT_GRAY);
				mostRecentButton.setForeground(Color.BLACK);
				mostRecentButton.setBackground(Color.LIGHT_GRAY);
			}
		});
		searchButton.setName("searchButton");
		searchButton.setBounds(870, 72, 52, 35);
		searchButton.setToolTipText("Search for New Games");
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBackground(SystemColor.controlDkShadow);
		searchButton.setBorder(null);
		searchButton.setContentAreaFilled(false);
		searchButton.setOpaque(true);
		searchButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/search.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		searchGamePanel.add(searchButton);
		
		mostViewedButton = new JButton("Most Viewed");
		mostViewedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				mostViewedButton.setBackground(new Color(30,144,255));
				mostViewedButton.setForeground(Color.WHITE);
				
				featuredButton.setForeground(Color.BLACK);
				featuredButton.setBackground(Color.LIGHT_GRAY);
				mostLikedButton.setForeground(Color.BLACK);
				mostLikedButton.setBackground(Color.LIGHT_GRAY);
				mostRecentButton.setForeground(Color.BLACK);
				mostRecentButton.setBackground(Color.LIGHT_GRAY);
				
				searchTextField.setText("Search");
				
				StatusObject<DataNavigator> viewedStatusObject  = logicHandler.getMostViewedPreviews();
				
				if( viewedStatusObject.statusCode == StatusCode.OK ) {
					
					searchGamesDataNavigator = viewedStatusObject.element;
					
					StatusObject<List <PreviewGame>> listStatusObject = viewedStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( fillSearchedGamesList(listStatusObject.element) ) {
							supportGamesList = listStatusObject.element;
					    }
					}	
				}
			}
		});
		mostViewedButton.setBackground(Color.LIGHT_GRAY);
		mostViewedButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		mostViewedButton.setMargin(new Insets(2, 2, 2, 2));
		mostViewedButton.setFont(new Font("Corbel", Font.BOLD, 15));
		mostViewedButton.setName("mostViewedButton");
		mostViewedButton.setBounds(200, 72, 112, 35);
		mostViewedButton.setContentAreaFilled(false);
		mostViewedButton.setOpaque(true);
		searchGamePanel.add(mostViewedButton);
		
		mostLikedButton = new JButton("Most Liked");
		mostLikedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				mostLikedButton.setBackground(new Color(30,144,255));
				mostLikedButton.setForeground(Color.WHITE);
				
				featuredButton.setForeground(Color.BLACK);
				featuredButton.setBackground(Color.LIGHT_GRAY);
				mostViewedButton.setForeground(Color.BLACK);
				mostViewedButton.setBackground(Color.LIGHT_GRAY);
				mostRecentButton.setForeground(Color.BLACK);
				mostRecentButton.setBackground(Color.LIGHT_GRAY);
				
				searchTextField.setText("Search");
				
				StatusObject<DataNavigator> likedStatusObject  = logicHandler.getMostViewedPreviews();
				
				if( likedStatusObject.statusCode == StatusCode.OK ) {
					
					searchGamesDataNavigator = likedStatusObject.element;
					
					StatusObject<List <PreviewGame>> listStatusObject = likedStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( fillSearchedGamesList(listStatusObject.element) ) {
							supportGamesList = listStatusObject.element;
					    }
					}	
				}
			}
		});
		mostLikedButton.setBackground(Color.LIGHT_GRAY);
		mostLikedButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		mostLikedButton.setFont(new Font("Corbel", Font.BOLD, 15));
		mostLikedButton.setMargin(new Insets(2, 2, 2, 2));
		mostLikedButton.setName("mostLikedButton");
		mostLikedButton.setBounds(311, 72, 97, 35);
		mostLikedButton.setContentAreaFilled(false);
		mostLikedButton.setOpaque(true);
		searchGamePanel.add(mostLikedButton);
		
		mostRecentButton = new JButton("Most Recent");
		mostRecentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				mostRecentButton.setBackground(new Color(30,144,255));
				mostRecentButton.setForeground(Color.WHITE);
				
				featuredButton.setForeground(Color.BLACK);
				featuredButton.setBackground(Color.LIGHT_GRAY);
				mostViewedButton.setForeground(Color.BLACK);
				mostViewedButton.setBackground(Color.LIGHT_GRAY);
				mostLikedButton.setForeground(Color.BLACK);
				mostLikedButton.setBackground(Color.LIGHT_GRAY);
				
				searchTextField.setText("Search");
				
				StatusObject<DataNavigator> recentStatusObject  = logicHandler.getMostViewedPreviews();
				
				if( recentStatusObject.statusCode == StatusCode.OK ) {
					
					searchGamesDataNavigator = recentStatusObject.element;
					
					StatusObject<List <PreviewGame>> listStatusObject = recentStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( fillSearchedGamesList(listStatusObject.element) ) {
							supportGamesList = listStatusObject.element;
					    }
					}	
				}
			}
		});
		mostRecentButton.setBackground(Color.LIGHT_GRAY);
		mostRecentButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		mostRecentButton.setMargin(new Insets(2, 2, 2, 2));
		mostRecentButton.setFont(new Font("Corbel", Font.BOLD, 15));
		mostRecentButton.setName("mostRecentButton");
		mostRecentButton.setBounds(403, 72, 102, 35);
		mostRecentButton.setContentAreaFilled(false);
		mostRecentButton.setOpaque(true);
		searchGamePanel.add(mostRecentButton);
		
		featuredButton = new JButton("Featured");
		featuredButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				featuredButton.setBackground(new Color(30,144,255));
				featuredButton.setForeground(Color.WHITE);
				
				mostRecentButton.setForeground(Color.BLACK);
				mostRecentButton.setBackground(Color.LIGHT_GRAY);
				mostViewedButton.setForeground(Color.BLACK);
				mostViewedButton.setBackground(Color.LIGHT_GRAY);
				mostLikedButton.setForeground(Color.BLACK);
				mostLikedButton.setBackground(Color.LIGHT_GRAY);
				
				searchTextField.setText("Search");
				
				StatusObject<DataNavigator> featuredStatusObject  = logicHandler.getMostViewedPreviews();
				
				if( featuredStatusObject.statusCode == StatusCode.OK ) {
					
					searchGamesDataNavigator = featuredStatusObject.element;
					
					StatusObject<List <PreviewGame>> listStatusObject = featuredStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( fillSearchedGamesList(listStatusObject.element) ) {
							supportGamesList = listStatusObject.element;
					    }
					}	
				}
			}
		});
		featuredButton.setBackground(new Color(30, 144, 255));
		featuredButton.setForeground(Color.WHITE);
		featuredButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		featuredButton.setMargin(new Insets(2, 2, 2, 2));
		featuredButton.setFont(new Font("Corbel", Font.BOLD, 15));
		featuredButton.setName("featuredButton");
		featuredButton.setBounds(121, 72, 80, 35);
		featuredButton.setContentAreaFilled(false);
		featuredButton.setOpaque(true);
		searchGamePanel.add(featuredButton);
		
		searchGameScrollPane = new JScrollPane();
		searchGameScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		searchGameScrollPane.setName("searchGameScrollPane");
		searchGameScrollPane.setBounds(31, 160, 871, 352);
		searchGamePanel.add(searchGameScrollPane);
		
		searchGamesJList = new JList<PreviewGame>();
		searchGamesJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		searchGamesJList.setVisibleRowCount(-1);
		searchGamesJList.setName("searchGameJList");
		searchGameScrollPane.setViewportView(searchGamesJList);
		
		searchGamesVerticalScrollBarListener =  new AdjustmentListener() {
		      public void adjustmentValueChanged(AdjustmentEvent e) {
		    	  JScrollBar bar = (JScrollBar)e.getAdjustable();
			      int extent = bar.getModel().getExtent();
			      int value = bar.getValue();
			      int max = bar.getMaximum();
			      
			      if( value == 0 ) {
			    	  
			    	  StatusObject<List<PreviewGame>> status = searchGamesDataNavigator.getPrevData();
			    	  
			    	  if( status.statusCode == StatusCode.OK ) {
			    		  
			    		  fillSearchedGamesList(status.element);
			    		  bar.setValue(1);
			    	  }  
			      }
			      
			      if( value+extent == max ) {
			    	  
			    	  StatusObject<List<PreviewGame>> status = searchGamesDataNavigator.getNextData();
			    	  
			    	  if( status.statusCode == StatusCode.OK ) {
			    		  
			    		  fillSearchedGamesList(status.element);
			    		  bar.setValue(1);
			    	  }
			      }
		      }
		};
		searchGamesVerticalScrollBar = searchGameScrollPane.getVerticalScrollBar();
		searchGamesVerticalScrollBar.addAdjustmentListener(searchGamesVerticalScrollBarListener);
		
		gameGenreMenuBar = new JMenuBar();
		gameGenreMenuBar.setBorder(new LineBorder(Color.BLACK, 1, true));
		gameGenreMenuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gameGenreMenuBar.setFont(new Font("Corbel", Font.PLAIN, 15));
		gameGenreMenuBar.setOpaque(true);
		gameGenreMenuBar.setBackground(Color.LIGHT_GRAY);
		gameGenreMenuBar.setToolTipText("Filter by Game Genre");
		gameGenreMenuBar.setName("gameGenreMenuBar");
		gameGenreMenuBar.setBounds(557, 72, 48, 35);
		searchGamePanel.add(gameGenreMenuBar);
		
		gameGenreMenu = new JMenu("Genre");
		gameGenreMenu.setBorder(null);
		gameGenreMenu.setHorizontalTextPosition(SwingConstants.CENTER);
		gameGenreMenu.setHorizontalAlignment(SwingConstants.CENTER);
		gameGenreMenu.setForeground(Color.BLACK);
		gameGenreMenu.setBackground(Color.LIGHT_GRAY);
		gameGenreMenu.setOpaque(true);
		gameGenreMenu.setContentAreaFilled(true);
		gameGenreMenu.setFont(new Font("Corbel", Font.BOLD, 15));
		gameGenreMenu.setBorderPainted(false);
		gameGenreMenu.setToolTipText("Filter by Game Genre");
		gameGenreMenu.setName("gameGenreMenu");
		gameGenreMenuBar.add(gameGenreMenu);
		
		gamePanel = new JPanel();
		gamePanel.setBackground(new Color(87, 86, 82));
		gamePanel.setName("gamePanel");
		panel.add(gamePanel, "gamePanel");
		gamePanel.setLayout(null);
		
		gameDescriptionScrollPane = new JScrollPane();
		gameDescriptionScrollPane.setBorder(null);
		gameDescriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		gameDescriptionScrollPane.setBounds(62, 54, 324, 190);
		gamePanel.add(gameDescriptionScrollPane);
		
		gameDescriptionTextArea = new JTextArea();
		gameDescriptionTextArea.setFont(new Font("Corbel", Font.PLAIN, 16));
		gameDescriptionScrollPane.setViewportView(gameDescriptionTextArea);
		gameDescriptionTextArea.setLineWrap(true);
		gameDescriptionTextArea.setEditable(false);
		gameDescriptionTextArea.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		gameDescriptionTextArea.setText("");
		gameDescriptionTextArea.setName("gameDescriptionTextArea");
		gameDescriptionTextArea.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		
		gameTitleLabel = new JLabel("Game Title");
		gameTitleLabel.setName("gameTitleLabel");
		gameTitleLabel.setForeground(Color.WHITE);
		gameTitleLabel.setFont(new Font("Corbel", Font.BOLD, 18));
		gameTitleLabel.setBounds(62, 31, 324, 29);
		gamePanel.add(gameTitleLabel);
		
		previewImageLabel = new JLabel("");
		previewImageLabel.setName("previewImageLabel");
		previewImageLabel.setBounds(426, 56, 342, 188);
		gamePanel.add(previewImageLabel);
		
		steamButton = new JButton("");
		steamButton.setBackground(SystemColor.controlDkShadow);
		steamButton.setName("steamButton");
		steamButton.setBounds(62, 257, 73, 62);
		steamButton.setContentAreaFilled(false);
		steamButton.setOpaque(true);
		steamButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/steam.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		gamePanel.add(steamButton);
		
		nintendoButton = new JButton("");
		nintendoButton.setBackground(SystemColor.controlDkShadow);
		nintendoButton.setName("nintendoButton");
		nintendoButton.setContentAreaFilled(false);
		nintendoButton.setOpaque(true);
		nintendoButton.setBounds(147, 257, 73, 62);
		nintendoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/nintendo.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		gamePanel.add(nintendoButton);
		
		playStationButton = new JButton("");
		playStationButton.setBackground(SystemColor.controlDkShadow);
		playStationButton.setName("playStationButton");
		playStationButton.setBounds(313, 257, 73, 62);
		playStationButton.setContentAreaFilled(false);
		playStationButton.setOpaque(true);
		playStationButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/playstation.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		gamePanel.add(playStationButton);
		
		XBoxButton = new JButton("");
		XBoxButton.setBackground(SystemColor.controlDkShadow);
		XBoxButton.setName("XBoxButton");
		XBoxButton.setBounds(232, 257, 73, 62);
		XBoxButton.setContentAreaFilled(false);
		XBoxButton.setOpaque(true);
		XBoxButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/xbox.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		gamePanel.add(XBoxButton);
		
		homeGameButton = new JButton("");
		homeGameButton.setName("homeGameButton");
		homeGameButton.setBounds(790, 54, 97, 68);
		homeGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanGamePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage(currentUsertype,currentUser);
			}
		});
		homeGameButton.setToolTipText("Return to Homepage");
		homeGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeGameButton.setBackground(SystemColor.controlDkShadow);
		homeGameButton.setBorder(null);
		homeGameButton.setContentAreaFilled(false);
		homeGameButton.setOpaque(true);
		homeGameButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		gamePanel.add(homeGameButton);
		
		actionButton = new JButton("");
		actionButton.setToolTipText("Click Here to Add this Game to Your Games");
		actionButton.setName("actionButton");
		actionButton.setBounds(477, 257, 63, 37);
		actionButton.setBackground(SystemColor.controlDkShadow);
		actionButton.setContentAreaFilled(false);
		actionButton.setOpaque(true);
		actionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/minus.png")).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		gamePanel.add(actionButton);
		
		developerLabel = new JLabel("Developer: testDeveloper");
		developerLabel.setFont(new Font("Corbel", Font.PLAIN, 15));
		developerLabel.setForeground(Color.WHITE);
		developerLabel.setName("developerLabel");
		developerLabel.setBounds(563, 257, 189, 16);
		gamePanel.add(developerLabel);
		
		releaseDateLabel = new JLabel("Release Date: 02/02/0202");
		releaseDateLabel.setForeground(Color.WHITE);
		releaseDateLabel.setFont(new Font("Corbel", Font.PLAIN, 15));
		releaseDateLabel.setName("releaseDateLabel");
		releaseDateLabel.setBounds(563, 278, 189, 16);
		gamePanel.add(releaseDateLabel);
		
		gameImagesScrollPane = new JScrollPane();
		gameImagesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		gameImagesScrollPane.setName("gameImagesScrollPane");
		gameImagesScrollPane.setBounds(63, 332, 324, 210);
		gamePanel.add(gameImagesScrollPane);
		
		imagesList = new JList<Image>();
		imagesList.setVisibleRowCount(-1);
		imagesList.setModel(imagesListModel);
		imagesList.setCellRenderer(new ImageRenderer());
		imagesList.setName("imagesList");
		gameImagesScrollPane.setViewportView(imagesList);
		
		voteMenuBar = new JMenuBar();
		voteMenuBar.setName("voteMenuBar");
		voteMenuBar.setBounds(426, 257, 39, 37);
		gamePanel.add(voteMenuBar);
		
		voteMenu = new JMenu("Vote");
		voteMenu.setFont(new Font("Corbel", Font.PLAIN, 15));
		voteMenuBar.add(voteMenu);
		voteMenu.setName("voteMenu");
		
		vote1 = new JMenuItem("1");
		vote1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				logicHandler.voteGame(currentGame.getTitle(), 1);
			}
		});
		vote1.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote1.setName("vote1");
		voteMenu.add(vote1);
		
		vote2 = new JMenuItem("2");
		vote2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				logicHandler.voteGame(currentGame.getTitle(), 2);
			}
		});
		vote2.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote2.setName("vote2");
		voteMenu.add(vote2);
		
		vote3 = new JMenuItem("3");
		vote3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				logicHandler.voteGame(currentGame.getTitle(), 3);
			}
		});
		vote3.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote3.setName("vote3");
		voteMenu.add(vote3);
		
		vote4 = new JMenuItem("4");
		vote4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				logicHandler.voteGame(currentGame.getTitle(), 4);
			}
		});
		vote4.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote4.setName("vote4");
		voteMenu.add(vote4);
		
		vote5 = new JMenuItem("5");
		vote5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				logicHandler.voteGame(currentGame.getTitle(), 5);
			}
		});
		vote5.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote5.setName("vote5");
		voteMenu.add(vote5);
		
		metacriticScoreLabel = new JLabel("");
		metacriticScoreLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		metacriticScoreLabel.setForeground(Color.WHITE);
		metacriticScoreLabel.setBackground(SystemColor.controlDkShadow);
		metacriticScoreLabel.setToolTipText("Metacritic Score");
		metacriticScoreLabel.setName("metaciticScoreLabel");
		metacriticScoreLabel.setBounds(789, 135, 98, 62);
		metacriticScoreLabel.setOpaque(true);
		metacriticScoreLabel.setText("4.7");
		metacriticScoreLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/star.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		gamePanel.add(metacriticScoreLabel);
		
		videoPlayer = new VideoPlayerPanel();
		videoPlayer.setSize(342, 210);
		videoPlayer.setLocation(426, 332);
		gamePanel.add(videoPlayer);
		
		nextVideoButton = new JButton("");
		nextVideoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if( currentVideoIndex == lastVideoIndex ) {
					return;
				}
				
				currentVideoIndex++;
				
				videoPlayer.getVideo(currentVideosURLlist.get(currentVideoIndex));
				
				if( currentVideoIndex == 1 ) {
					
					previousVideoButton.setEnabled(true);
				}
				
				
				if( currentVideoIndex == lastVideoIndex ) {
					
					nextVideoButton.setEnabled(false);
				}
			}
		});
		nextVideoButton.setToolTipText("Next Video");
		nextVideoButton.setName("nextVideoButton");
		nextVideoButton.setBackground(SystemColor.controlDkShadow);
		nextVideoButton.setContentAreaFilled(false);
		nextVideoButton.setOpaque(true);
		nextVideoButton.setBounds(804, 351, 73, 43);
		nextVideoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/next.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
		gamePanel.add(nextVideoButton);
		
		previousVideoButton = new JButton("");
		previousVideoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if( currentVideoIndex == 0 ) {
					
					return;
				}
				
				currentVideoIndex--;
				
				videoPlayer.getVideo(currentVideosURLlist.get(currentVideoIndex));
				
				if( currentVideoIndex == lastVideoIndex-1 ) {
					
					nextVideoButton.setEnabled(true);
				}
				
				if( currentVideoIndex == 0 ) {
					
					previousVideoButton.setEnabled(false);
				}
			}
		});
		previousVideoButton.setToolTipText("Previous Video");
		previousVideoButton.setName("previousVideoButton");
		previousVideoButton.setBackground(SystemColor.controlDkShadow);
		previousVideoButton.setContentAreaFilled(false);
		previousVideoButton.setOpaque(true);
		previousVideoButton.setBounds(804, 469, 73, 45);
		previousVideoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/back.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
		gamePanel.add(previousVideoButton);
		
		
		
		userPanel = new JPanel();
		userPanel.setBackground(new Color(87, 86, 82));
		userPanel.setName("userPanel");
		panel.add(userPanel, "userPanel");
		userPanel.setLayout(null);
		
		featuredUserButton = new JButton("Featured");
		featuredUserButton.setName("featuredUserButton");
		featuredUserButton.setRequestFocusEnabled(false);
		featuredUserButton.setBounds(537, 73, 97, 32);
		featuredUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				featuredUserButton.setBackground(new Color(30, 144, 255));
				featuredUserButton.setForeground(Color.WHITE);

				fillUsersTable(logicHandler.getFeaturedUsers(currentUser));
			}
		});
		featuredUserButton.setBackground(new Color(30, 144, 255));
		featuredUserButton.setForeground(Color.WHITE);
		featuredUserButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		featuredUserButton.setMargin(new Insets(2, 2, 2, 2));
		featuredUserButton.setFont(new Font("Corbel", Font.BOLD, 15));
		featuredUserButton.setContentAreaFilled(false);
		featuredUserButton.setOpaque(true);
		userPanel.add(featuredUserButton);
		
		searchUserTextField = new JTextField();
		searchUserTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				searchUserTextField.setText("");
			}
		});
		searchUserTextField.setFont(new Font("Corbel", Font.ITALIC, 15));
		searchUserTextField.setText("Search User");
		searchUserTextField.setName("searchUserTextField");
		searchUserTextField.setBounds(646, 74, 154, 31);
		userPanel.add(searchUserTextField);
		searchUserTextField.setColumns(10);
		
		searchUserButton = new JButton("");
		searchUserButton.setName("searchUserButton");
		searchUserButton.setBounds(798, 73, 52, 32);
		searchUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String searchedString = searchTextField.getText();
				
				if( searchedString=="" ) {
					return;
				}
				
				featuredUserButton.setBackground(Color.WHITE);
				featuredUserButton.setForeground(Color.BLACK);
				
				fillUsersTable(logicHandler.searchUsers(searchedString, currentUser));
			}
		});
		searchUserButton.setToolTipText("Search for New Users");
		searchUserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchUserButton.setBackground(SystemColor.controlDkShadow);
		searchUserButton.setBorder(null);
		searchUserButton.setContentAreaFilled(false);
		searchUserButton.setOpaque(true);
		searchUserButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/search.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		userPanel.add(searchUserButton);
		
		homeUserButton = new JButton("");
		homeUserButton.setName("homeUserButton");
		homeUserButton.setBounds(57, 38, 97, 67);
		homeUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanUserPage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage(currentUsertype,currentUser);
			}
		});
		homeUserButton.setToolTipText("Return to Homepage");
		homeUserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeUserButton.setBackground(SystemColor.controlDkShadow);
		homeUserButton.setBorder(null);
		homeUserButton.setContentAreaFilled(false);
		homeUserButton.setOpaque(true);
		homeUserButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		userPanel.add(homeUserButton);
		
		userGamesScrollPane = new JScrollPane();
		userGamesScrollPane.setBounds(57, 146, 402, 336);
		userPanel.add(userGamesScrollPane);
		
		userGamesList = new JList<PreviewGame>();
		userGamesList.setModel(userGamesListModel);
		userGamesScrollPane.setViewportView(userGamesList);
		
		usersScrollPane = new JScrollPane();
		usersScrollPane.setBounds(537, 146, 313, 336);
		userPanel.add(usersScrollPane);
		
		usersTable = new JTable();
		usersTable.setName("usersTable");
		usersTable.setModel(usersTableModel);
		usersTable.setFont(new Font("Corbel",Font.PLAIN,16));
		usersTableHeader = usersTable.getTableHeader();
		usersTableHeader.setFont(titleFont);
		usersTableHeader.setForeground(Color.WHITE);
		usersTableHeader.setBackground(new Color(121,166,210));
		usersScrollPane.setViewportView(usersTable);
		
		displayedUserLabel = new JLabel("Currently Displayed: Gianni's Games. E-Mail: gianni@giannimail.com");
		displayedUserLabel.setForeground(Color.WHITE);
		displayedUserLabel.setFont(new Font("Corbel", Font.BOLD, 15));
		displayedUserLabel.setName("displayedUserLabel");
		displayedUserLabel.setAutoscrolls(true);
		displayedUserLabel.setBounds(57, 117, 402, 16);
		userPanel.add(displayedUserLabel);
		
		userInformationPanel = new JPanel();
		userInformationPanel.setBackground(new Color(87, 86, 82));
		userInformationPanel.setName("userInformationPanel");
		panel.add(userInformationPanel, "userInformationPanel");
		userInformationPanel.setLayout(null);
		
		ageTextField = new JTextField();
		ageTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				ageTextField.setText("");
			}
		});
		ageTextField.setFont(new Font("Corbel", Font.ITALIC, 15));
		ageTextField.setText("Age");
		ageTextField.setName("ageTextField");
		ageTextField.setBounds(233, 158, 233, 35);
		userInformationPanel.add(ageTextField);
		ageTextField.setColumns(10);
		
		saveButton = new JButton("Save");
		saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		saveButton.setToolTipText("Save your Personal Information");
		saveButton.setBackground(new Color(0, 191, 255));
		saveButton.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		saveButton.setFont(new Font("Corbel", Font.BOLD, 17));
		saveButton.setContentAreaFilled(false);
		saveButton.setOpaque(true);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int age;
				String ageString = ageTextField.getText();
				String name = nameTextField.getText();
				String surname = surnameTextfield.getText();
				String genre = genreMenu.getText();
				String gender = genderMenu.getText();
				String email = emailTextField.getText();

				if( ageString == "" || ageString.startsWith("Age")) {
					age = -1;
				} else {
					try {
						age = Integer.parseInt(ageString);
					} catch (NumberFormatException e) {
						age = -1;
					}
				}
				
				if( name == "" || name.startsWith("Name") ) {
					name = null;
				}
				
				if( surname == "" || surname.startsWith("Surname") ) {
					surname = null;
				}
				
				if( gender != "M" && gender != "F" ) {
					gender = null;
				}
				
				if( genre == "Genre" ) {
					genre = null;
				}
				
				if( email == "" || email.startsWith("E-Mail")) {
					email = null;
				}
				
				logicHandler.updateUserInformation(age,name,surname,genre,gender,email);
				initializeUserInformationPage();
			}
		});
		saveButton.setName("saveButton");
		saveButton.setBounds(396, 409, 127, 48);
		userInformationPanel.add(saveButton);
		
		nameTextField = new JTextField();
		nameTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				nameTextField.setText("");
			}
		});
		nameTextField.setFont(new Font("Corbel", Font.ITALIC, 15));
		nameTextField.setName("nameTextField");
		nameTextField.setText("Name");
		nameTextField.setBounds(233, 222, 233, 37);
		userInformationPanel.add(nameTextField);
		nameTextField.setColumns(10);
		
		surnameTextfield = new JTextField();
		surnameTextfield.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				nameTextField.setText("");
			}
		});
		surnameTextfield.setFont(new Font("Corbel", Font.ITALIC, 15));
		surnameTextfield.setText("Surname");
		surnameTextfield.setName("surnameTextField");
		surnameTextfield.setBounds(233, 283, 233, 37);
		userInformationPanel.add(surnameTextfield);
		surnameTextfield.setColumns(10);
		
		genderMenuBar = new JMenuBar();
		genderMenuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		genderMenuBar.setToolTipText("Click Here to Select Your Gender");
		genderMenuBar.setFont(new Font("Corbel", Font.BOLD, 15));
		genderMenuBar.setName("genderMenuBar");
		genderMenuBar.setBounds(520, 158, 55, 35);
		userInformationPanel.add(genderMenuBar);
		
		genderMenu = new JMenu("Gender");
		genderMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		genderMenu.setBorder(null);
		genderMenu.setHorizontalTextPosition(SwingConstants.CENTER);
		genderMenu.setHorizontalAlignment(SwingConstants.CENTER);
		genderMenu.setToolTipText("Click Here To Select your Gender");
		genderMenu.setFont(new Font("Corbel", Font.BOLD, 15));
		genderMenu.setName("genderMenu");
		genderMenuBar.add(genderMenu);
		
		maleMenuItem = new JMenuItem("Male");
		maleMenuItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		maleMenuItem.setFont(new Font("Corbel", Font.BOLD, 15));
		maleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				genderMenu.setText("M");
			}
		});
		maleMenuItem.setName("maleMenuItem");
		genderMenu.add(maleMenuItem);
		
		femaleMenuItem = new JMenuItem("Female");
		femaleMenuItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		femaleMenuItem.setFont(new Font("Corbel", Font.BOLD, 15));
		femaleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				genderMenu.setText("F");
			}
		});
		genderMenu.add(femaleMenuItem);
		
		genreMenuBar = new JMenuBar();
		genreMenuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		genreMenuBar.setToolTipText("Click Here to Select your Favorite Genre");
		genreMenuBar.setFont(new Font("Corbel", Font.BOLD, 15));
		genreMenuBar.setName("genreMenuBar");
		genreMenuBar.setBounds(520, 222, 55, 35);
		userInformationPanel.add(genreMenuBar);
		
		genreMenu = new JMenu("Genre");
		genreMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		genreMenu.setToolTipText("Click Here to Select your Favorite Genre");
		genreMenu.setFont(new Font("Corbel", Font.BOLD, 15));
		genreMenu.setName("genreMenu");
		genreMenuBar.add(genreMenu);
		
		updateInfoLabel = new JLabel("Hi user, update your information");
		updateInfoLabel.setName("updateInformationLabel");
		updateInfoLabel.setForeground(Color.WHITE);
		updateInfoLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		updateInfoLabel.setBounds(367, 81, 349, 25);
		userInformationPanel.add(updateInfoLabel);
		
		homeUserInformationButton = new JButton("");
		homeUserInformationButton.setName("homeUserInformationButton");
		homeUserInformationButton.setBounds(233, 38, 97, 68);
		homeUserInformationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanUserInformationPage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage(currentUsertype,currentUser);
			}
		});
		homeUserInformationButton.setToolTipText("Return to Homepage");
		homeUserInformationButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeUserInformationButton.setBackground(SystemColor.controlDkShadow);
		homeUserInformationButton.setBorder(null);
		homeUserInformationButton.setContentAreaFilled(false);
		homeUserInformationButton.setOpaque(true);
		homeUserInformationButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		userInformationPanel.add(homeUserInformationButton);
		
		emailTextField = new JTextField();
		emailTextField.setText("E-Mail");
		emailTextField.setName("emailTextField");
		emailTextField.setFont(new Font("Corbel", Font.ITALIC, 15));
		emailTextField.setColumns(10);
		emailTextField.setBounds(233, 348, 233, 37);
		userInformationPanel.add(emailTextField);
		
	}
}
