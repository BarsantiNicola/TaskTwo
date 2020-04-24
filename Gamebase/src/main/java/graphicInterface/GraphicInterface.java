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
import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import javax.swing.border.*;
import javax.swing.table.*;


public class GraphicInterface {

	private JFrame frame;
	private JPanel panel;
	
	//LOGIN PANEL 
	private JPanel loginPanel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameTextfield;
	private JPasswordField passwordField;
	private JButton signUpButton;
	private JButton loginButton;
	private JLabel errorMessageLabel;
	private JLabel myGamesLabel;

	//HOME PANEL 
	private JPanel homePagePanel;
	private JLabel gamesNumberHPLabel;
	private JLabel followerNumberHPLabel;
	private JLabel welcomeHPLabel;
	private JLabel usernameHPLabel;
	private JButton logoutHPButton;
	private JButton userButton;
	private JButton adminHPButton;
	private JButton becomeAnalystButton;
	private JButton analystHPButton;
	private JButton userInfoButton ;
	private JScrollPane myGamesScrollPane;
	private JLabel mostPopularGamesLabel;
	private JLabel mostViewedGamesLabel;
	private JLabel searchGameLabel;
	private JLabel favouriteGamesLabel;
	private JLabel followedUsersLabel;
	private JScrollPane followedTableScrollPane;
	private JTable followedTable;
	private JTableHeader followedTableHeader;
	private JList<BufferedGame> myGamesList;
	private DefaultListModel<BufferedGame> myGamesListModel = new DefaultListModel<BufferedGame>();	
	private DefaultTableModel followedTableModel = new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Username", "Games", 
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class
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
	private JButton analystHomeButton;
	private JButton topUsersButton;
	private JPanel plotContainer;
	private BarChartPanel topUsersPanel;
	private BarChartPanel topGamesPanel;
	private PieChartPanel topGenresPanel;
	private BarChartPanel topRatedGameByYearPanel;
	private BarChartPanel topViewedGameByYearPanel;
	private PieChartPanel maxViewedGameByGenrePanel;
	private PieChartPanel maxRatedGameByGenrePanel;
	private PieChartPanel viewCountByGenrePanel;
	private PieChartPanel gamesCountByGenrePanel;
	private PieChartPanel ratingsCountByGenrePanel;
	private AreaChartPanel gamesCountByYearPanel;
	private AreaChartPanel viewsCountByYearPanel;
	private AreaChartPanel ratingsCountByYearPanel;
	private PieChartPanel ratingsCountYearGenPanel;
	private PieChartPanel viewsCountYearGenPanel;
	private PieChartPanel gameCountYearGenPanel;
	
	private JButton topGamesButton;
	private JButton topGenresButton;
	private JButton topRatedGameByYearButton;
	private JButton topViewedGameByYearButton;
	private JButton maxViewedgameByGenreButton;
	private JButton maxRatedGameByGenreButton;
	private JButton viewCountByGenreButton;
	private JButton gamesCountByGenreButton;
	private JButton ratingsCountByGenButton;
	private JButton gamesCountByYearButton;
	private JButton viewsCountByYearButton;
	private JButton ratingsCountByYearButton;
	private JButton ratingsCountYearGenButton;
    private JButton viewsCountByYearGenButton;
    private JButton gameCountYearGenButton;
	
	private JTextField gameCountTextField;
	private JTextField viewCountTextField;
	private JTextField ratingsCountTextField;
		
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
	private JList<BufferedGame> searchedGamesJList;	
	private DefaultListModel<BufferedGame> searchedGamesListModel = new DefaultListModel<BufferedGame>();
	private JMenu gameGenreMenu;
	private JMenuBar gameGenreMenuBar;
	private AdjustmentListener searchGamesVerticalScrollBarListener;
	private JScrollBar searchGamesVerticalScrollBar;
	private DataNavigator searchGamesDataNavigator;
	
	/////// GAME PANEL
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
	
	////////USER PANEL
	private JPanel userPanel;
	private JButton searchUserButton;
	private JButton homeUserButton;
	private JScrollPane userGamesScrollPane;
	private JList<BufferedGame> userGamesList;
	private DefaultListModel<BufferedGame> userGamesListModel = new DefaultListModel<BufferedGame>();
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
	private JLabel searchUserLabel;
	private JLabel searchUserWelcomeLabel;
	
	///////// USER INFORMATION PANEL
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
	private JTextField countryTextField;
	private JLabel ageLabel;
	private JLabel nameLabel;
	private JLabel surnameLabel;
	private JLabel emailLabel;
	private JLabel countryLabel;
	private JLabel genderLabel;
	private JLabel genreLabel;
	
	///////// LOGIC AND SUPPORT INFO
	private LogicBridge logicHandler = new LogicBridge();
	private GraphConnector graphHandler = new GraphConnector();
	private User currentUser = null;
	private Game currentGame = null;
	private Font titleFont = new Font("Corbel", Font.BOLD, 20);
	private List<BufferedGame> supportGamesList = null;
	private List<String> currentVideosURLlist = null;
	private int currentVideoIndex = 0;
	private int lastVideoIndex = 0;
	private Boolean isGameFavourite =  null;
	private DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	
	/////// SUPPORT FUNCTIONS
	

	private void fillMyGamesList(List<BufferedGame> gamesList) {
		
		if( myGamesListModel.size() != 0 ) {
			
			myGamesListModel.removeAllElements();
		}
		
		if( gamesList == null ) {
			
			return;
		}
		
		for( int i = 0; i < gamesList.size(); i++ ) {
			
			myGamesListModel.addElement(gamesList.get(i));
		}

	}
	
	private void fillFollowedTable(List<User> friendList) {
		
		followedTableModel.setRowCount(0);
		
		for( User friend: friendList ) {
			
			String username = friend.getUsername();
			
			Object[] object = new Object[2];
			object[0] = username;
			object[1] = "SEE GAMES";
			followedTableModel.addRow(object);
		}
		
		ButtonColumn buttonColumn = new ButtonColumn(followedTable, new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {
				
				JTable table = (JTable)e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				
				String followerUsername = (String)((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "userPanel");
				
				initializeUserPage(followerUsername);
			}
		},1);
	}
	
	private void fillSearchedGamesList(List<BufferedGame> games) {
		
		if( searchedGamesListModel.size() != 0 ) {
			
			searchedGamesListModel.removeAllElements();
		}
		
		if( games == null ) {
			return;
		}
		
		for( int i = 0; i < games.size(); i++ ) {
			searchedGamesListModel.addElement(games.get(i));
		}
	}
	
	
	private void fillUsersTable( List<User> usersList ) {
		
		usersTableModel.setRowCount(0);
		
		for( User friend: usersList ) {
			
			StatusObject<Boolean> followStatus = graphHandler.doIFollow(friend.getUsername());
			
			if( followStatus.statusCode != StatusCode.OK ) {
				System.out.println("->[GraphicInterface] impossible to determine if user " + friend.getUsername() + 
						" is followed. User not inserted into the list.");
				continue;
			}
			
			boolean followed = followStatus.element;
			
			Object[] object = new Object[3];
			object[0] = friend.getUsername();
			object[1] = followed?"SEE GAMES":"x";
			object[2] = followed?"UNFOLLOW":"FOLLOW";
			
			usersTableModel.addRow(object);
		}
		
		ButtonColumn buttonColumnGames = new ButtonColumn(usersTable, new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {
				
				JTable table = (JTable)e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				
				String selectedUsername = (String)((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
				
				StatusObject<Boolean> followed = graphHandler.doIFollow(selectedUsername);
				
				if( followed.statusCode != StatusCode.OK ) {
					
					System.out.println("->[GraphicInterface] impossible to determine if " + selectedUsername + " is followed. See games operation aborted.");
				    return;
				}
				
				if( !followed.element ) {
					
					System.out.println("->[GraphicInterface] you have no rights to see "  + selectedUsername + "'s games.");
					displayedUserLabel.setText("Follow " + selectedUsername + " to see his/her games");
					fillUserGamesList(null);
					return;
				}
				
				StatusObject<List<GraphGame>> favGamesStatus = graphHandler.getFavouritesGamesList(selectedUsername);
				
				if( favGamesStatus.statusCode == StatusCode.OK ) {
					
					List<BufferedGame> favGamesList = new ArrayList<>();
					
					for( int i = 0; i < favGamesStatus.element.size(); i++ ) {
						
						GraphGame gm = favGamesStatus.element.get(i);
						String url = gm.previewImage;
						ImageIcon icon = null;
						
						icon = logicHandler.getCachedImg(url);
						
						if( icon == null ) {
							
							String replacement = "media/crop/600/400/games";
							String croppedUrl = null;
							
							try {
								croppedUrl = url.replaceFirst("media/games", replacement);
								icon = new ImageIcon(ImageIO.read(new URL(croppedUrl)).getScaledInstance(80, 100, Image.SCALE_FAST));
								
								if(logicHandler.cacheImg(url, icon)) {
									
									System.out.println("->[GraphicInterface] image " + url + " stored in cache");
								}
								
							} catch(Exception ee) {
								icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							}
						} else {
							
							System.out.println("->[GraphicInterface] image " + url + " retrieved from cache.");
							
							if( icon.getIconHeight() != 80 || icon.getIconHeight() != 100 ) {
								
								System.out.println("->[GraphicInterface] cached image need to be resized.");
								icon = new ImageIcon(icon.getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							}
						}
						favGamesList.add(new BufferedGame(Integer.parseInt(gm._id),gm.title,icon));
					}
					
					fillUserGamesList(favGamesList);
					displayedUserLabel.setText("Currently Displayed: " + selectedUsername + "'s Games.");
				}	
			}
		},1);
		
		ButtonColumn buttonColumnAction = new ButtonColumn(usersTable, new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {

				JTable table = (JTable)e.getSource();
				int modelRow = Integer.valueOf(e.getActionCommand());
				
				String selectedUsername = (String)((DefaultTableModel)table.getModel()).getValueAt(modelRow, 0);
				
				StatusObject<Boolean> followStatus = graphHandler.doIFollow(selectedUsername);
				
				if( followStatus.statusCode == StatusCode.OK ) {
					
					if( followStatus.element ) { //I already follow the user, so I may unfollow him/her
						
						if( graphHandler.unFollowUser(selectedUsername) != StatusCode.OK ) {
							System.out.println("->[GraphicInterface] error in unfollow user procedure.");
							return;
						}
						
						System.out.println("->[GraphicInterface] unfollow procedure terminated correctly.");
						usersTableModel.setValueAt("x", modelRow, 1);
				    	usersTableModel.setValueAt("FOLLOW", modelRow, 2);
					} else { //I don't follow the user so I may follow him/her
					
						if( graphHandler.followUser(selectedUsername) != StatusCode.OK ) {
							System.out.println("->[GraphicInterface] error in follow user procedure.");
							return;
						}
						
						System.out.println("->[GraphicInterface] follow procedure terminated correctly.");
						usersTableModel.setValueAt("SEE GAMES", modelRow, 1);
						usersTableModel.setValueAt("UNFOLLOW", modelRow, 2);
					}
				} else {
					
					System.out.println("->[GraphicInterface] error: impossible to determine if user is followed or not. Action set to N/A.");
					usersTableModel.setValueAt("N/A", modelRow, 2);
				}
			}
		},2);
	}
	
	private void fillUserGamesList(List<BufferedGame> gamesList) {
		
		if( userGamesListModel.size() != 0 ) {
			
			userGamesListModel.removeAllElements();
		}
		
		if( gamesList == null ) {
			return;
		}
		
		for( int i = 0; i < gamesList.size(); i++ ) {
			userGamesListModel.addElement(gamesList.get(i));
		}
	}
	
	private void fillImagesList(List<String> imagesURLList) {
		
		imagesListModel.removeAllElements();
		
		if( imagesURLList == null ) {
			return;
		}

		Image image = null;
		ImageIcon icon = null;
		String url;
		
		for( int i=0; i < imagesURLList.size(); i++ ) {
			
			url = imagesURLList.get(i);
			icon = logicHandler.getCachedImg(url);
			
			if( icon == null ) {
				
				try {
					image = ImageIO.read(new URL(url));
					
					if(logicHandler.cacheImg(url, new ImageIcon(image))) {
						
						System.out.println("->[GraphicInterface] image " + url + " stored in cache");
					}
				} catch (Exception e) {
					try {
						image = ImageIO.read(new File("/resources/defaultGamePicture.png"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				
				image = icon.getImage();
			}
			
			imagesListModel.addElement(image);
		}
	}
	
	private void cleanLoginPage() {
		
		usernameTextfield.setText("");
		passwordField.setText("");
		errorMessageLabel.setVisible(false);
	}
	
	private void initializeHomePage() {
		
		System.out.println("->[GraphicInterface] Initializing home page.");
		
		long followersLong = currentUser.getFollowedCount();
		String followersNumber = Long.toString(followersLong);
		
		StatusObject<List<GraphGame>> gamesListStatus = graphHandler.getFavouritesGamesList();
		
		String gamesNumber = null;
		
		if( gamesListStatus.statusCode == StatusCode.OK ) {
			
			List<BufferedGame> favouriteGamesList = new ArrayList<>();
			
			for( int i=0; i<gamesListStatus.element.size(); i++) {
				
				GraphGame gm = gamesListStatus.element.get(i);
				String url = gm.previewImage;
				ImageIcon icon = null;
				
				icon = logicHandler.getCachedImg(url);
				
				if( icon == null ) {
					
					String replacement = "media/crop/600/400/games"; 
					String croppedUrl = null;
					
					try {
						croppedUrl = url.replaceFirst("media/games", replacement); 
						icon = new ImageIcon(ImageIO.read(new URL(croppedUrl)).getScaledInstance(80, 100, Image.SCALE_FAST));
						
						if(logicHandler.cacheImg(url, icon)) {
							
							System.out.println("->[GraphicInterface] image " + url + " stored in cache");
						}
						
					} catch(Exception e) {
						icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
					}
				} else {
					
					System.out.println("->[GraphicInterface] image " + url + " retrieved from cache.");
					
					if( icon.getIconWidth() != 80 || icon.getIconHeight() != 100 ) {
						
						icon = new ImageIcon(icon.getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
					}
				}

				favouriteGamesList.add(new BufferedGame(Integer.parseInt(gm._id),gm.title,icon));
			}
				
			long favGamesNumber = gamesListStatus.element.size();
			gamesNumber = Long.toString(favGamesNumber);
			
			fillMyGamesList(favouriteGamesList);
		} else {
			
			gamesNumber = "N/A";
			System.out.println("->[GraphicInterface] impossible to retrieve favourite games list.");
		}
		
		gamesNumberHPLabel.setText(gamesNumber);
		followerNumberHPLabel.setText(followersNumber);
		
		gamesNumberHPLabel.setToolTipText("You Have " + gamesNumber + " preferred games");
		followerNumberHPLabel.setToolTipText(followersNumber + " people follow you");
		
		usernameHPLabel.setText(currentUser.getUsername());
		
		StatusObject<PreviewGame> mostViewedStatus = logicHandler.getMostViewedPreview();
		
		if( mostViewedStatus.statusCode == StatusCode.OK ) {
			
			String mostViewedGameImageURL = mostViewedStatus.element.getPreviewPicURL();
			
			if( mostViewedGameImageURL == null ) {
				mostViewedGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_FAST)));	
			} else {
				try {
					String replacement = "media/crop/600/400/games";
					mostViewedGameImageURL = mostViewedGameImageURL.replaceFirst("media/games", replacement);
					mostViewedGamesLabel.setIcon(new ImageIcon(ImageIO.read(new URL(mostViewedGameImageURL)).getScaledInstance(238, 155, Image.SCALE_SMOOTH)));
				} catch (Exception e){
					mostViewedGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_FAST)));	
			    }
			}
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve most viewed game.");
		}
		
		StatusObject<PreviewGame> mostPopularStatus = logicHandler.getMostPopularPreview();
		
		if( mostPopularStatus.statusCode == StatusCode.OK ) {
			
			String mostPopularGameImageURL = mostPopularStatus.element.getPreviewPicURL();
			
			if( mostPopularGameImageURL == null ) {
				mostPopularGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_FAST)));	
			} else {
				try {
					String replacement = "media/crop/600/400/games";
					mostPopularGameImageURL = mostPopularGameImageURL.replaceFirst("media/games", replacement);
					mostPopularGamesLabel.setIcon(new ImageIcon(ImageIO.read(new URL(mostPopularGameImageURL)).getScaledInstance(238, 155, Image.SCALE_SMOOTH)));
				} catch (Exception e){
					mostPopularGamesLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(211, 145, Image.SCALE_FAST)));	
			    }
			}	
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve most popular game.");
		}
	
		StatusObject<List<User>> friendListStatus = graphHandler.getFollowedUsersList();
		
		if( friendListStatus.statusCode == StatusCode.OK ) {
			
			fillFollowedTable(friendListStatus.element);
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve followed users list");
		}
		
		UserType type = graphHandler.getUserType();
		
		switch(type) {
			case ADMINISTRATOR:
				adminHPButton.setVisible(true);
				adminHPButton.setEnabled(true);
				becomeAnalystButton.setVisible(false);
				becomeAnalystButton.setEnabled(false);
				analystHPButton.setVisible(true);
				analystHPButton.setEnabled(true);
				break;
			case ANALYST:
				adminHPButton.setVisible(false);
				adminHPButton.setEnabled(false);
				becomeAnalystButton.setVisible(false);
				becomeAnalystButton.setEnabled(false);
				analystHPButton.setVisible(true);
				analystHPButton.setEnabled(true);
				break;
			case USER:
				adminHPButton.setVisible(false);
				adminHPButton.setEnabled(false);
				becomeAnalystButton.setVisible(true);
				becomeAnalystButton.setEnabled(true);
				analystHPButton.setVisible(false);
				analystHPButton.setEnabled(false);
				break;
			default:
				return;
		}
	}

	private void cleanHomePage() {
		
		System.out.println("->[GraphicInterface] cleaning home page.");
		
		gamesNumberHPLabel.setText("");
		followerNumberHPLabel.setText("");
		
		usernameHPLabel.setText("");
		
		myGamesListModel.removeAllElements();
		followedTableModel.setRowCount(0);
		
		adminHPButton.setEnabled(true);
		becomeAnalystButton.setEnabled(true);
		analystHPButton.setEnabled(true);
		adminHPButton.setVisible(true);
		becomeAnalystButton.setVisible(true);
		analystHPButton.setVisible(true);
		
	}
	
	@SuppressWarnings("deprecation")
	private void initializeGamePage( int id ) {
		
		StatusObject<Game> gameStatus = logicHandler.getGame(id);
		
		if( gameStatus.statusCode != StatusCode.OK || gameStatus.element == null ) {
			
			System.out.println("->[GraphicInterface] game not found. Redirecting to home page.");
			
			CardLayout cl = (CardLayout)(panel.getLayout());
			
			cl.show(panel, "homePagePanel");
			
			return;
		}
		
		System.out.println("->[GraphicInterface] initializing game page.");
		
		Game game = gameStatus.element;
		currentGame = game;
		
		String gameDescription = game.getDescription();
		
		if( gameDescription == null ) {
			
			gameDescriptionTextArea.setText("          -- Game Description not Available --");
		} else {
			
			gameDescriptionTextArea.setText(gameDescription);
		}
		
		gameTitleLabel.setText(game.getTitle());
		
		Date releaseDate = game.getReleaseDate();
		
		if( releaseDate == null ) {
			
			releaseDateLabel.setText("Release Date Not Available");
		} else {
			
			releaseDateLabel.setText("Release Date: " + Integer.toString(releaseDate.getDate())+"/"+Integer.toString(releaseDate.getMonth()) +"/"+Integer.toString(releaseDate.getYear()));
		}
		
		String background_image = game.getBackground_image();
		
		if( background_image == null  ) {
			
			previewImageLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(342, 188, Image.SCALE_SMOOTH)));
		} else {
			
			try {
				
				previewImageLabel.setIcon( new ImageIcon(ImageIO.read(new URL(background_image)).getScaledInstance(342, 188, Image.SCALE_SMOOTH)));
			} catch(Exception e) {
				
				previewImageLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(342, 188, Image.SCALE_SMOOTH)));
			}
		}
		
		final String steamURL = game.getSteamURL();
		
		if( steamURL != null ) {
			
			steamButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI(steamURL));
						} catch (Exception e) {
							steamButton.setEnabled(false);
							e.printStackTrace();
					    }
				    }	
				}
			};
			steamButton.addActionListener(steamButtonListener);
		} else {
			steamButton.setEnabled(false);
		}
		
		final String nintendoURL = game.getNintendoURL();
		
		if( nintendoURL != null ) {
			
			nintendoButtonListener = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI(nintendoURL));
						} catch (Exception e) {
							nintendoButton.setEnabled(false);
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
							playStationButton.setEnabled(false);
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
							XBoxButton.setEnabled(false);
							e.printStackTrace();
					    }
				    }	
				}
			};
			XBoxButton.addActionListener(xboxButtonListener);
		} else {
			XBoxButton.setEnabled(false);
		}
		
		StatusObject<List<GraphGame>> myGamesStatus = graphHandler.getFavouritesGamesList();
		
		if( myGamesStatus.statusCode == StatusCode.OK ) {
			
			boolean favourite = false;
			
			for( int i = 0; i < myGamesStatus.element.size(); i++ ) {
				
				if( Integer.parseInt(myGamesStatus.element.get(i)._id) == game.getId() ) {
					
					favourite = true;
					break;
				}
			}
			
			isGameFavourite = favourite;
			
			if( favourite ) {
				
				actionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/minus.png")).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));
				isGameFavourite = true;
			} else {
				
				actionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png")).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));
				isGameFavourite = false;
			}
		} else {
			
			System.out.println("->[GraphicInterface] impossible to determine if game is favourite.");
		}
			
		List<String> imagesURL = game.getImagesURLs();
		
		while( imagesURL.remove(background_image) ) {
			
		}
		
		fillImagesList(imagesURL);
		
		Integer score = game.getMetacritic();
			
		if( score == null ) {
			metacriticScoreLabel.setText("N/A");
		} else {
			metacriticScoreLabel.setText(Integer.toString(score));
		}
		
		List<String> videoURLs = game.getVideoURLs();
		
		if( videoURLs != null && videoURLs.size()!=0 ) {
			
			System.out.println("->[GraphicInterface] " + videoURLs.size() + " video(s) found for " + game.getTitle() + ".");
			System.out.println("->[GraphicInterface] currently displayed video 1.");
			
			currentVideosURLlist = videoURLs;
			currentVideoIndex = 0;
			lastVideoIndex = videoURLs.size()-1;
			
			String firstVideo = videoURLs.get(0);
			videoPlayer.playVideo(firstVideo);
			
			if( videoURLs.size() > 1 ) {
				
				nextVideoButton.setEnabled(true);
			}
			previousVideoButton.setEnabled(false);
		} else {
			
			System.out.println("->[GraphicInterface] no videos available for " + game.getTitle() +".");
			nextVideoButton.setEnabled(false);
			previousVideoButton.setEnabled(false);
			videoPlayer.playVideo(null);
		}
		
		if( logicHandler.incrementGameViews(game.getId()) != StatusCode.OK ) {
			System.out.println("->[GraphicInterface] error while incrementing view count.");
		} else {
			System.out.println("->[GraphicInterface] increment view count performed.");
		}
	}
	
	private void cleanGamePage() {
		
		System.out.println("->[GraphicInterface] cleaning game page.");
		
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
		
		videoPlayer.stopVideo();
		
		currentVideosURLlist = null;
		currentVideoIndex = 0;
		lastVideoIndex = 0;
		
		nextVideoButton.setEnabled(true);
		previousVideoButton.setEnabled(true);
		
		isGameFavourite = null;
	}
	
	private void initializeUserPage( String searchedUser ) {
		
		System.out.println("->[GraphicInterface] initializing user page.");
		
		featuredUserButton.setBackground(new Color(30, 144, 255));
		featuredUserButton.setForeground(Color.WHITE);
		
		StatusObject<List<User>> featuredUsersStatus = graphHandler.getSuggestedUsersList();
		
		if( featuredUsersStatus.statusCode == StatusCode.OK ) {
			
			fillUsersTable(featuredUsersStatus.element);
			
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve featured users.");
		}
		
		if( searchedUser != null ) {
			
			StatusObject<List<GraphGame>> friendGamesStatus = graphHandler.getFavouritesGamesList(searchedUser);
			
			if( friendGamesStatus.statusCode == StatusCode.OK ) {
				
				List<BufferedGame> friendGamesList = new ArrayList<>();
				
				for( int i = 0; i < friendGamesStatus.element.size(); i++ ) {
					
					GraphGame gm = friendGamesStatus.element.get(i);
					String url = gm.previewImage;
			        String replacement = "media/crop/600/400/games";
					ImageIcon icon = null;
					
					try {
						url = url.replaceFirst("media/games", replacement);
						icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
					} catch(Exception e) {
						icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
					}
					
					friendGamesList.add(new BufferedGame(Integer.parseInt(gm._id),gm.title,icon));
				}
				
				fillUserGamesList(friendGamesList);
				displayedUserLabel.setText("Currently Displayed: " + searchedUser + "'s Games." );
			} else {
				
				System.out.println("->[GraphicInterface] impossible to retrieve friend's games.");
			}	
		} else {
			
			displayedUserLabel.setText("No games displayed.");
		}
		
		searchUserTextField.setText("Search User");
		
		searchUserWelcomeLabel.setText("Hi " + currentUser.getUsername() + "!");
	}
	
	private void cleanUserPage() {
		
		System.out.println("->[GraphicInterface] cleaning user page");
		
		usersTableModel.setRowCount(0);
		userGamesListModel.removeAllElements();
		displayedUserLabel.setText("");
		searchUserTextField.setText("Search User");
		featuredUserButton.setBackground(new Color(30, 144, 255));
		featuredUserButton.setForeground(Color.WHITE);
		searchUserWelcomeLabel.setText("");
	}
	
	private void initializeAdminPage() {
		
		System.out.println("->[GraphicInterface] initializing admin page.");
		
		String userCount, gameCount;
		
		StatusObject<Long> userCountStatus = graphHandler.getTotalUsersCount();
		
		if( userCountStatus.statusCode == StatusCode.OK ) {
			
			userCount = Long.toString(userCountStatus.element);
		} else {
			
			userCount = "N/A";
		}
		
		StatusObject<Long> gameCountStatus = logicHandler.getGameCount();
		
		if( gameCountStatus.statusCode == StatusCode.OK ) {
			
			gameCount = Long.toString(gameCountStatus.element);
		} else {
			
			gameCount = "N/A";
		}
		
		userCountLabel.setText("User Count: " + userCount);
		gameCountLabel.setText("Game Count: " + gameCount);
		
		deleteUserResultLabel.setVisible(false);
		deleteGameResultLabel.setVisible(false);
		updateDatabaseResultLabel.setVisible(false);
	}
	
	private void cleanAdminPage() {
		
		System.out.println("->[GraphicInterface] cleaning admin page");
		
		userCountLabel.setText("User Count:");
		gameCountLabel.setText("Game Count:");
		
		deleteUserTextField.setText("User");
		deleteGameTextField.setText("Game");
		
		deleteUserResultLabel.setVisible(false);
		deleteGameResultLabel.setVisible(false);
		updateDatabaseResultLabel.setVisible(false);
	}
	
	
	private void initializeSearchGamePage() {
		
		System.out.println("->[GraphicInterface] initializing admin page");
		
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
						
						List<BufferedGame> genreList = new ArrayList<BufferedGame>();
						BufferedGame game;
						
						for( int i=0; i<supportGamesList.size(); i++ ) {
							
							game = supportGamesList.get(i);
							
							StatusObject<Game> gameStatus = logicHandler.getGame(game.getId());
							
							if( gameStatus.statusCode == StatusCode.OK ) {
								
								List<String> genres = gameStatus.element.getAllGenres();
								System.out.println(genres);
								for( String gen: genres) {
									if( gen == null ) {
										continue;
									}
									if( gen.compareTo(genre) == 0 ) {
										genreList.add(game);
										break;
								    }
								}	
							} else {
								
								System.out.println("->[GraphicInterface] impossible to retrieve " + game.getTitle() + " genre.");
							}
						}
						
						fillSearchedGamesList(genreList);
						
					}
				});
				gameGenreMenu.add(item);
			}
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve genre list.");
		}
		
		StatusObject<List<GraphGame>> featuredGamesStatus = graphHandler.getFeaturedGamesList();
		
		if( featuredGamesStatus.statusCode == StatusCode.OK ) {
			
			List<BufferedGame> featuredGamesList = new ArrayList<>();
			
			for( int i = 0; i < featuredGamesStatus.element.size(); i++ ) {
				
				GraphGame gm = featuredGamesStatus.element.get(i);
				String url = gm.previewImage;
		        String replacement = "media/crop/600/400/games";
				ImageIcon icon = null;
				
				try {
					url = url.replaceFirst("media/games", replacement);
					icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
				} catch(Exception e) {
					icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
				}
				
				featuredGamesList.add(new BufferedGame(Integer.parseInt(gm._id),gm.title,icon));
			}
			
			fillSearchedGamesList(featuredGamesList);
			supportGamesList = featuredGamesList;
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve featured games list.");
		}	
	}
	
	private void cleanSearchGamePage() {
		
		System.out.println("->[GraphicInterface] cleaning search game page");
		
		searchedGamesListModel.removeAllElements();
		gameGenreMenu.removeAll();
		
		searchGamesDataNavigator = null;
		
	}
	
	private void initializeUserInformationPage() {
		
		System.out.println("->[GraphicInterface] initializing user information page");
		
		updateInfoLabel.setText("Hi " + currentUser.getUsername() + ", update your information");
		
		String currentAge = null;
		if( currentUser.getAge() != null ) {
			currentAge = Long.toString(currentUser.getAge());
		} 
		
		String currentName = currentUser.getFirstName();
		String currentSurname = currentUser.getLastName();
		String currentFavouriteGenre = currentUser.getFavouriteGenre();
		String currentEmail = currentUser.getEmail();
		Character gender = currentUser.getGender();
		String currentCountry = currentUser.getCountry();
		
		ageTextField.setText("Current: " + (currentAge!=null?currentAge:"null"));
		nameTextField.setText("Current: " + (currentName!=null?currentName:"null"));
		surnameTextfield.setText("Current: " + (currentSurname!=null?currentSurname:"null"));
		emailTextField.setText("Current: " + (currentEmail!=null?currentEmail:"null"));
		countryTextField.setText("Current: " + (currentCountry!=null?currentCountry:"null"));
		
		if( gender == null || gender.equals('M') ) {
			genderMenu.setText("M");
		} else if( gender.equals('F') ) {
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
				
				if( currentFavouriteGenre != null && currentFavouriteGenre.equals(genre) ) {
					genreMenu.setText(genre);
				}
			}
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve genre list.");
		}
	}
	
	private void cleanUserInformationPage() {
		
		System.out.println("->[GraphicInterface] cleaning user information page.");
		
		updateInfoLabel.setText("");
		nameTextField.setText("");
		surnameTextfield.setText("");
		
		genreMenu.removeAll();
	}
	
	private void initializeAnalystPanel() {
		
		System.out.println("->[GraphicInterface] initializing analyst page.");
		
		StatusObject<List<User>> topUsersStatus = graphHandler.getMostFollowedUsers(6);
		
		if( topUsersStatus.statusCode == StatusCode.OK ) {
			
			HashMap<String,Double> topUsersHashmap = new HashMap<String,Double>();
			
			for( int i = 0; i < topUsersStatus.element.size(); i++ ){
				
				topUsersHashmap.put(topUsersStatus.element.get(i).getUsername() + " - " + topUsersStatus.element.get(i).getFollowedCount(), topUsersStatus.element.get(i).getFollowedCount().doubleValue());
			}
			
			topUsersPanel = new BarChartPanel("Most Followed Users", "User", "Followers", topUsersHashmap, "V", true, false, false, "valueDesc");
			topUsersPanel.setName("topUsersPanel");
			
			plotContainer.add(topUsersPanel,"topUsersPanel");
			
			CardLayout cl = (CardLayout)(plotContainer.getLayout());
			
			cl.show(plotContainer, "topUsersPanel");
			
		} else {
			
			System.out.println("->[GraphicInterface] impossible to retrieve statistics about most followed users.");
		}
		
		ratingsCountTextField.setText("Insert Year");
		viewCountTextField.setText("Insert Year");
		gameCountTextField.setText("Insert Year");
		
	}
	
	private void cleanAnalystPanel() {
		
		System.out.println("->[GraphicInterface] cleaning analyst page.");
		
		topUsersPanel = null;
		topGamesPanel = null;
		topGenresPanel = null;
		topRatedGameByYearPanel = null;
		topViewedGameByYearPanel = null;
		maxViewedGameByGenrePanel = null;
		maxRatedGameByGenrePanel = null;
	    viewCountByGenrePanel = null;
	    gamesCountByGenrePanel = null;
		ratingsCountByGenrePanel = null;
		gamesCountByYearPanel = null;
		viewsCountByYearPanel = null;
		ratingsCountByYearPanel= null;
		ratingsCountYearGenPanel= null;
		viewsCountYearGenPanel= null;
		gameCountYearGenPanel= null;
		
		ratingsCountTextField.setText("Insert Year");
		viewCountTextField.setText("Insert Year");
		gameCountTextField.setText("Insert Year");
		
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
		
		StatusCode graphConnection = graphHandler.connect("bolt://172.16.0.78:7687","neo4j","password");
		
		if( graphConnection != StatusCode.OK ) {
			
			System.out.println("->[GraphicInterface] Failed to connect to graph database");
			System.exit(0);
		}
		
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
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
		usernameTextfield.setFont(new Font("Corbel", Font.BOLD, 16));
		usernameTextfield.setBounds(269, 275, 387, 43);
		loginPanel.add(usernameTextfield);
		usernameTextfield.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Corbel", Font.BOLD, 16));
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
				
				System.out.println("->[GraphicInterface] Trying to sign up " + username + ".");
				
				if( username.equals("") || password.equals("") ) {
					System.out.println("->[GraphicInterface] Sign up failed: empty username and(or) password.");
					errorMessageLabel.setText("Please Insert Username and Password");
					errorMessageLabel.setVisible(true);
					return;
				}
				
				User registeredUser = new User(username, password,LocalDate.now());
				
				StatusObject<UserInfo> registrationStatus = graphHandler.register(registeredUser);
				
				if( registrationStatus.statusCode == StatusCode.OK ) {
					
					currentUser = registrationStatus.element.user; //or currentUser = registeredUser;
					
					System.out.println("->[GraphicInterface] Sign up completed: username " + username + " registered.");
					cleanLoginPage();
					cl.show(panel, "homePagePanel");
					initializeHomePage();
					
				} else {
					
					System.out.println("->[GraphicInterface] Sign up failed.");
					
					if( registrationStatus.statusCode == StatusCode.ERR_GRAPH_USER_ALREADYEXISTS ) {
						
						errorMessageLabel.setText("->[GraphicInterface] Error occured during registration: user already exists");
					} else {
						
						errorMessageLabel.setText("Generic Error");
					}
					
					errorMessageLabel.setVisible(true);
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
				
				System.out.println("->[GraphicInterface] User " + username + " is trying to login");
				
				if( username.equals("") || password.equals("") ) {
					System.out.println("->[GraphicInterface] Empty username and(or) password");
					errorMessageLabel.setText("Please Insert Username and Password");
					errorMessageLabel.setVisible(true);
					return;
				}
				
				StatusObject<UserInfo> loginStatus = graphHandler.login(username, password);
				
				if( loginStatus.statusCode == StatusCode.OK ) {
					
					currentUser = loginStatus.element.user;
					
					System.out.println("->[GraphicInterface] Login completed:user " + username + " logged in");
					cl.show(panel, "homePagePanel");
					initializeHomePage();
					cleanLoginPage();
				} else {
					
					System.out.println("->[GraphicInterface] Login failed");
					errorMessageLabel.setText("Login failed");
					errorMessageLabel.setVisible(true);
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
		welcomeHPLabel.setFont(new Font("Corbel", Font.PLAIN, 20));
		welcomeHPLabel.setName("usertypeHPLabel");
		welcomeHPLabel.setBounds(26, 25, 89, 23);
		homePagePanel.add(welcomeHPLabel);
		
		usernameHPLabel = new JLabel("username");
		usernameHPLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		usernameHPLabel.setForeground(Color.WHITE);
		usernameHPLabel.setName("usernameHPLabel");
		usernameHPLabel.setBounds(117, 28, 100, 16);
		homePagePanel.add(usernameHPLabel);
		
		logoutHPButton = new JButton("Logout");
		logoutHPButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logoutHPButton.setMargin(new Insets(2, 2, 2, 2));
		logoutHPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				if( graphHandler.logout() == StatusCode.OK ) {
					
					currentUser = null;
					cleanHomePage();
					cl.show(panel, "loginPanel");
				}

			}
		});
		logoutHPButton.setToolTipText("Click Here To Logout");
		logoutHPButton.setName("logoutHPButton");
		logoutHPButton.setBounds(27, 61, 67, 21);
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
		gamesNumberHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/controller.png")).getImage().getScaledInstance(69, 50, Image.SCALE_FAST)));
		homePagePanel.add(gamesNumberHPLabel);
		
		followerNumberHPLabel = new JLabel("155");
		followerNumberHPLabel.setBorder(new EmptyBorder(0, 3, 0, 10));
		followerNumberHPLabel.setOpaque(true);
		followerNumberHPLabel.setBackground(SystemColor.controlDkShadow);
		followerNumberHPLabel.setForeground(Color.WHITE);
		followerNumberHPLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		followerNumberHPLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		followerNumberHPLabel.setToolTipText("Number of  People Who Follow You");
		followerNumberHPLabel.setName("followerNumberHPLabel");
		followerNumberHPLabel.setBounds(793, 13, 117, 69);
		followerNumberHPLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/followers.png")).getImage().getScaledInstance(69, 50, Image.SCALE_FAST)));
		homePagePanel.add(followerNumberHPLabel);
		
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
		adminHPButton.setBounds(281, 13, 81, 69);
		adminHPButton.setToolTipText("Click Here To Enter into Admin Section");
		adminHPButton.setBackground(SystemColor.controlDkShadow);
		adminHPButton.setBorder(null);
		adminHPButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/gear.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		homePagePanel.add(adminHPButton);
		
		becomeAnalystButton = new JButton("");
		becomeAnalystButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				StatusObject<UserInfo> upgradeStatus = graphHandler.upgradeToAnalyst();
				
				if( upgradeStatus.statusCode == StatusCode.OK ) {
					
					currentUser = upgradeStatus.element.user;
					becomeAnalystButton.setVisible(false);
					becomeAnalystButton.setEnabled(false);
					analystHPButton.setVisible(true);
					analystHPButton.setEnabled(true);
				}
			}
		});
		becomeAnalystButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		becomeAnalystButton.setBounds(374, 13, 81, 69);
		becomeAnalystButton.setToolTipText("Click Here To Become an Analyst");
		becomeAnalystButton.setBackground(SystemColor.controlDkShadow);
		becomeAnalystButton.setContentAreaFilled(false);
		becomeAnalystButton.setBorder(null);
		becomeAnalystButton.setOpaque(true);
		becomeAnalystButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/becomeAnalyst.png")).getImage().getScaledInstance(100, 60, Image.SCALE_FAST)));
		homePagePanel.add(becomeAnalystButton);
		
		analystHPButton = new JButton("");
		analystHPButton.setName("analystHPButton");
		analystHPButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "analystPanel");
				
				initializeAnalystPanel();
			}
		});
		analystHPButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		analystHPButton.setBounds(374, 13, 81, 69);
		analystHPButton.setToolTipText("Click Here To Enter into Analyst Section");
		analystHPButton.setBackground(SystemColor.controlDkShadow);
		analystHPButton.setBorder(null);
		analystHPButton.setContentAreaFilled(false);
		analystHPButton.setOpaque(true);
		analystHPButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/statistics.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		homePagePanel.add(analystHPButton);
		
		followedTableScrollPane = new JScrollPane();
		followedTableScrollPane.setName("followedTableScrollPane");
		followedTableScrollPane.setBackground(Color.BLACK);
		followedTableScrollPane.setBounds(27, 142, 356, 174);
		homePagePanel.add(followedTableScrollPane);
		
		followedTable = new JTable();
		followedTable.setFocusable(false);
		followedTable.setRowSelectionAllowed(false);
		followedTable.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseDragged(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void updateCursor(MouseEvent e) {
				if( followedTable.columnAtPoint(e.getPoint()) == 1) {
					followedTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					followedTable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}	
			}
		});
		followedTable.setName("followedTable");
		followedTable.setModel(followedTableModel);
		followedTable.setFont(new Font("Corbel",Font.PLAIN,16));
		followedTable.setRowHeight(30);
		followedTable.setDefaultRenderer(String.class, centerRenderer);
		followedTableHeader = followedTable.getTableHeader();
		followedTableHeader.setFont(titleFont);
		followedTableHeader.setForeground(Color.WHITE);
		followedTableHeader.setBackground(new Color(121,166,210));
		followedTableScrollPane.setViewportView(followedTable);
		
		myGamesScrollPane = new JScrollPane();
		myGamesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		myGamesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		myGamesScrollPane.setName("myGamesScrollPane");
		myGamesScrollPane.setBounds(27, 348, 356, 174);
		homePagePanel.add(myGamesScrollPane);
		
		myGamesList = new JList<BufferedGame>(myGamesListModel);
		myGamesList.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseDragged(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void updateCursor(MouseEvent e) {
				if( myGamesList.getToolTipText(e) != null ) {
					myGamesList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					myGamesList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}	
			}
		});
		myGamesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if( myGamesList.getToolTipText(arg0) == null ) {
					
					return;
				}
				
				BufferedGame selectedGame = myGamesList.getSelectedValue();
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				initializeGamePage(selectedGame.getId());
			}
		});
		myGamesList.setName("myGamesList");
		myGamesList.setVisibleRowCount(-1);
		myGamesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		myGamesList.setCellRenderer(new BufferedGameRenderer());
		myGamesScrollPane.setViewportView(myGamesList);
		
		searchGameLabel = new JLabel("");
		searchGameLabel.setToolTipText("Click Here in order to Search for new Games");
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
		searchGameLabel.setBounds(714, 142, 196, 390);
		searchGameLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/exampleGamePicture.png")).getImage().getScaledInstance(185, 360, Image.SCALE_SMOOTH)));
		homePagePanel.add(searchGameLabel);
		
		mostViewedGamesLabel = new JLabel("");
		mostViewedGamesLabel.setVerticalAlignment(SwingConstants.TOP);
		mostViewedGamesLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mostViewedGamesLabel.setAlignmentY(0.0f);
		mostViewedGamesLabel.setToolTipText("Click Here to See the Most Viewed Game");
		mostViewedGamesLabel.setBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255), 2), "Most Viewed Game", TitledBorder.LEADING, TitledBorder.BOTTOM, titleFont, new Color(255, 255, 255)));
		mostViewedGamesLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		mostViewedGamesLabel.setVerticalTextPosition(SwingConstants.TOP);
		mostViewedGamesLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				StatusObject<PreviewGame> viewedGameStatus = logicHandler.getMostViewedPreview();
				
				if( viewedGameStatus.statusCode == StatusCode.OK ) {
					
					initializeGamePage(viewedGameStatus.element.getId());
				} 
			}
		});
		mostViewedGamesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mostViewedGamesLabel.setForeground(Color.WHITE);
		mostViewedGamesLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		mostViewedGamesLabel.setName("mostViewedGamesLabel");
		mostViewedGamesLabel.setBounds(427, 142, 246, 184);
		homePagePanel.add(mostViewedGamesLabel);
		
		mostPopularGamesLabel = new JLabel("");
		mostPopularGamesLabel.setToolTipText("Click Here to See the Most Popular Game");
		mostPopularGamesLabel.setVerticalTextPosition(SwingConstants.TOP);
		mostPopularGamesLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		mostPopularGamesLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mostPopularGamesLabel.setBorder(new TitledBorder(new LineBorder(new Color(255, 255, 255), 2), "Most Popular Game", TitledBorder.LEADING, TitledBorder.BOTTOM, titleFont, Color.WHITE));
		mostPopularGamesLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				StatusObject<PreviewGame> popularGameStatus = logicHandler.getMostPopularPreview();
				
				if( popularGameStatus.statusCode == StatusCode.OK ) {
					
					initializeGamePage(popularGameStatus.element.getId());
				} 
			
			}
		});
		mostPopularGamesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mostPopularGamesLabel.setVerticalAlignment(SwingConstants.TOP);
		mostPopularGamesLabel.setForeground(Color.WHITE);
		mostPopularGamesLabel.setFont(new Font("Corbel", Font.BOLD, 20));
		mostPopularGamesLabel.setName("mostPopularGamesLabel");
		mostPopularGamesLabel.setBounds(427, 348, 246, 184);
		homePagePanel.add(mostPopularGamesLabel);
		
		userButton = new JButton("");
		userButton.setBackground(SystemColor.controlDkShadow);
		userButton.setName("userButton");
		userButton.setBounds(560, 13, 81, 69);
		userButton.setContentAreaFilled(false);
		userButton.setOpaque(true);
		userButton.setName("userButton");
		userButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "userPanel");
				
				initializeUserPage(null);
			}
		});
		userButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		userButton.setFont(new Font("Corbel", Font.PLAIN, 13));
		userButton.setToolTipText("Click Here To Search for Other Users");
		userButton.setBorder(null);
		userButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/addFriend.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
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
		userInfoButton.setBounds(467, 13, 81, 69);
		userInfoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/info.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		homePagePanel.add(userInfoButton);
		
		favouriteGamesLabel = new JLabel("Favourite Games");
		favouriteGamesLabel.setName("favouriteGamesLabel");
		favouriteGamesLabel.setForeground(Color.WHITE);
		favouriteGamesLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		favouriteGamesLabel.setBounds(26, 320, 191, 26);
		homePagePanel.add(favouriteGamesLabel);
		
		followedUsersLabel = new JLabel("Followed Users");
		followedUsersLabel.setName("followedUsersLabel");
		followedUsersLabel.setForeground(Color.WHITE);
		followedUsersLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		followedUsersLabel.setBounds(26, 117, 191, 26);
		homePagePanel.add(followedUsersLabel);
		
		
		
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
				
				initializeHomePage();
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
		homeADButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
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
				
				if( username.equals("") ) {
					deleteUserResultLabel.setText("Failure!");
					deleteUserResultLabel.setVisible(true);
					return;
				}
				
				if( graphHandler.deleteUser(username) == StatusCode.OK ) {
					deleteUserResultLabel.setText("Success!");
					deleteUserResultLabel.setVisible(true);
				} else {
					deleteUserResultLabel.setText("Failure!");
					deleteUserResultLabel.setVisible(true);
				}
				
				new Timer(5000,new ActionListener() {
				      public void actionPerformed(ActionEvent evt) {
				          deleteUserResultLabel.setVisible(false);
				          deleteUserTextField.setText("");
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
				
				if( game.equals("") ) {
					deleteGameResultLabel.setText("Failure!");
					deleteGameResultLabel.setVisible(true);
					return;
				}
				
				if( logicHandler.deleteGame(game) == StatusCode.OK ) {
					deleteGameResultLabel.setText("Success!");
					deleteGameResultLabel.setVisible(true);
				} else {
					deleteGameResultLabel.setText("Failure!");
					deleteGameResultLabel.setVisible(true);
				}
				
				new Timer(5000,new ActionListener() {
				      public void actionPerformed(ActionEvent evt) {
				          deleteGameResultLabel.setVisible(false);
				          deleteGameTextField.setText("");
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
		
		userCountLabel = new JLabel("User Count:");
		userCountLabel.setName("userCountLabel");
		userCountLabel.setForeground(Color.WHITE);
		userCountLabel.setFont(new Font("Corbel", Font.BOLD, 26));
		userCountLabel.setBounds(104, 180, 226, 35);
		adminPanel.add(userCountLabel);
		
		gameCountLabel = new JLabel("Game Count:");
		gameCountLabel.setName("gameCountLabel");
		gameCountLabel.setForeground(Color.WHITE);
		gameCountLabel.setFont(new Font("Corbel", Font.BOLD, 26));
		gameCountLabel.setBounds(104, 269, 277, 35);
		adminPanel.add(gameCountLabel);
		
		updateDatabaseResultLabel = new JLabel("");
		updateDatabaseResultLabel.setName("updateDatabaseResultLabel");
		updateDatabaseResultLabel.setFont(new Font("Corbel", Font.PLAIN, 14));
		updateDatabaseResultLabel.setBounds(418, 461, 57, 21);
		adminPanel.add(updateDatabaseResultLabel);
		
		
		
		/////////  ANALYST PANEL
		
		
		
		analystPanel = new JPanel();
		analystPanel.setBackground(new Color(87, 86, 82));
		panel.add(analystPanel, "analystPanel");
		analystPanel.setLayout(null);
		
		analystHomeButton = new JButton("");
		analystHomeButton.setName("analystHomeButton");
		analystHomeButton.setBounds(83, 23, 97, 87);
		analystHomeButton.setToolTipText("Return to Homepage");
		analystHomeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanAnalystPanel();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage();
			}
		});
		analystHomeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		analystHomeButton.setBackground(SystemColor.controlDkShadow);
		analystHomeButton.setBorder(null);
		analystHomeButton.setContentAreaFilled(false);
		analystHomeButton.setOpaque(true);
		analystHomeButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		analystPanel.add(analystHomeButton);
		
		plotContainer = new JPanel();
		plotContainer.setBounds(83, 140, 764, 381);
		analystPanel.add(plotContainer);
		plotContainer.setLayout(new CardLayout(0, 0));
		
		topUsersButton = new JButton("Top Users");
		topUsersButton.setBackground(Color.LIGHT_GRAY);
		topUsersButton.setContentAreaFilled(false);
		topUsersButton.setOpaque(true);
		topUsersButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		topUsersButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		topUsersButton.setMargin(new Insets(2, 2, 2, 2));
		topUsersButton.setFont(new Font("Corbel", Font.PLAIN, 15));
		topUsersButton.setToolTipText("Click Here to see the most followed users");
		topUsersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					
				StatusObject<List<User>> topUsersStatus = graphHandler.getMostFollowedUsers(6);
				
				if( topUsersStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> topUsersHashmap = new HashMap<String,Double>();
					
					for( int i = 0; i < topUsersStatus.element.size(); i++ ){
						
						topUsersHashmap.put(topUsersStatus.element.get(i).getUsername() + " - " + topUsersStatus.element.get(i).getFollowedCount() , topUsersStatus.element.get(i).getFollowedCount().doubleValue());
					}
					
					topUsersPanel = new BarChartPanel("Most Followed Users", "User", "Followers", topUsersHashmap, "V", true, false, false, "valueDesc");
					topUsersPanel.setName("topUsersPanel");
					
					plotContainer.add(topUsersPanel,"topUsersPanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
					
					cl.show(plotContainer, "topUsersPanel");
					
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve most followed users.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		topUsersButton.setName("topUserButton");
		topUsersButton.setBounds(192, 23, 75, 27);
		analystPanel.add(topUsersButton);
		
		topGamesButton = new JButton("Top Games");
		topGamesButton.setBackground(Color.LIGHT_GRAY);
		topGamesButton.setContentAreaFilled(false);
		topGamesButton.setOpaque(true);
		topGamesButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		topGamesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		topGamesButton.setMargin(new Insets(2, 2, 2, 2));
		topGamesButton.setFont(new Font("Corbel", Font.PLAIN, 13));
		topGamesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				StatusObject<List<GraphGame>> topGamesStatus = graphHandler.getMostFavouriteGames(6);
				
				if( topGamesStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> topGamesHashMap = new HashMap<String,Double>();
					
					for( int i = 0; i < topGamesStatus.element.size(); i++ ){
						
						topGamesHashMap.put(topGamesStatus.element.get(i).title + " - " + topGamesStatus.element.get(i).favouriteCount, topGamesStatus.element.get(i).favouriteCount.doubleValue());
					}
					
					topGamesPanel = new BarChartPanel("Most Liked Games", "Game", "Favourite Count", topGamesHashMap, "V", true, false, false, "valueDesc");
					topGamesPanel.setName("topGamesPanel");
					
					plotContainer.add(topGamesPanel,"topGamesPanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "topGamesPanel");
							
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve most liked games.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		topGamesButton.setToolTipText("Click Here to See the most liked games");
		topGamesButton.setName("topGamesButton");
		topGamesButton.setBounds(192, 53, 75, 27);
		analystPanel.add(topGamesButton);
		
		topGenresButton = new JButton("Top Genres");
		topGenresButton.setBackground(Color.LIGHT_GRAY);
		topGenresButton.setContentAreaFilled(false);
		topGenresButton.setOpaque(true);
		topGenresButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		topGenresButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		topGenresButton.setFont(new Font("Corbel", Font.PLAIN, 13));
		topGenresButton.setMargin(new Insets(2, 2, 2, 2));
		topGenresButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StatusObject<List<UserStats>> userStatsStatus = graphHandler.getUsersSummaryStats();
				
				if( userStatsStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> topGenresHashMap = new HashMap<String,Double>();
					
					for( int i = 0; i < userStatsStatus.element.size(); i++ ) {
						
						String genre = userStatsStatus.element.get(i).favouriteGenre;
						
						if( genre.equals("null") ) {
							continue;
						}
						
						Double value = topGenresHashMap.get(genre);
						
						if( value == null ) {
							
							topGenresHashMap.put(genre, new Integer(1).doubleValue());
						} else {
							
							value++;
							topGenresHashMap.put(genre, value);
						}
					}
					
					topGenresPanel = new PieChartPanel("Most Liked Genres", "Genre", "Favourite Count", topGenresHashMap, "V", true, false, false);
					topGenresPanel.setName("topGenresPanel");
					
					plotContainer.add(topGenresPanel,"topGenresPanel");
						
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "topGenresPanel");
				
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve most liked genres.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		topGenresButton.setToolTipText("Click Here to see the most liked genres");
		topGenresButton.setName("topGenresButton");
		topGenresButton.setBounds(792, 23, 75, 27);
		analystPanel.add(topGenresButton);
		
		topRatedGameByYearButton = new JButton("Max Rate (Year)");
		topRatedGameByYearButton.setBackground(Color.LIGHT_GRAY);
		topRatedGameByYearButton.setContentAreaFilled(false);
		topRatedGameByYearButton.setOpaque(true);
		topRatedGameByYearButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		topRatedGameByYearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		topRatedGameByYearButton.setMargin(new Insets(2, 2, 2, 2));
		topRatedGameByYearButton.setFont(new Font("Corbel", Font.PLAIN, 12));
		topRatedGameByYearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StatusObject<List<Statistics>> maxRatedGameByYearStatus = logicHandler.getMaxRatedGameByYear();
				
				if( maxRatedGameByYearStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> maxRatedYearHashMap = new HashMap<String,Double>();
					
					for( int i = 0; i < maxRatedGameByYearStatus.element.size(); i++ ){
						
						if( maxRatedGameByYearStatus.element.get(i).getYear() >= Year.now().getValue()-20 && 
								maxRatedGameByYearStatus.element.get(i).getYear() <= Year.now().getValue()) {
							maxRatedYearHashMap.put(maxRatedGameByYearStatus.element.get(i).getYear() + " - " + maxRatedGameByYearStatus.element.get(i).getGames(),
								maxRatedGameByYearStatus.element.get(i).getRating());
						}	
					}
					
					topRatedGameByYearPanel = new BarChartPanel("Most Rated Games by Year", "Year - Game", "Rate", maxRatedYearHashMap, "V", true, false, false, "keyAsc");
					topRatedGameByYearPanel.setName("topRatedGamesByYearPanel");
					
					topRatedGameByYearButton.setEnabled(true);
					
					plotContainer.add(topRatedGameByYearPanel,"topRatedGamesByYearPanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
					
					cl.show(plotContainer, "topRatedGamesByYearPanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve top rated games by year.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		topRatedGameByYearButton.setToolTipText("Click Here to See the mos rated games for each year");
		topRatedGameByYearButton.setName("topRatedGameByYearButton");
		topRatedGameByYearButton.setBounds(270, 23, 97, 27);
		analystPanel.add(topRatedGameByYearButton);
		
		topViewedGameByYearButton = new JButton("Max View (Year)");
		topViewedGameByYearButton.setBackground(Color.LIGHT_GRAY);
		topViewedGameByYearButton.setContentAreaFilled(false);
		topViewedGameByYearButton.setOpaque(true);
		topViewedGameByYearButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		topViewedGameByYearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		topViewedGameByYearButton.setFont(new Font("Corbel", Font.PLAIN, 12));
		topViewedGameByYearButton.setMargin(new Insets(2, 1, 2, 1));
		topViewedGameByYearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StatusObject<List<Statistics>> maxViewedGameByYearStatus = logicHandler.getMaxViewedGameByYear();
				
				if( maxViewedGameByYearStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> maxViewedYearHashMap = new HashMap<String,Double>();
					
					for( int i = 0; i < maxViewedGameByYearStatus.element.size(); i++ ){
						
						if( maxViewedGameByYearStatus.element.get(i).getYear() >= Year.now().getValue()-20 && 
								maxViewedGameByYearStatus.element.get(i).getYear() <= Year.now().getValue()) {
									maxViewedYearHashMap.put(maxViewedGameByYearStatus.element.get(i).getYear() + " - " + maxViewedGameByYearStatus.element.get(i).getGames(),
											maxViewedGameByYearStatus.element.get(i).getViewsCount().doubleValue());
						}
					}
					
					topViewedGameByYearPanel = new BarChartPanel("Most Viewed Games by Year", "Game - Year", "Views", maxViewedYearHashMap, "V", true, false, false, "keyAsc");
					topViewedGameByYearPanel.setName("topViewedGamesByYearPanel");
					
					topViewedGameByYearButton.setEnabled(true);
					
					plotContainer.add(topViewedGameByYearPanel,"topViewedGamesByYearPanel");
					
				    CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "topViewedGamesByYearPanel");
						
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve most viewed games by year.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		topViewedGameByYearButton.setToolTipText("Click Here to see the most viewed games (by year)");
		topViewedGameByYearButton.setName("topViewedGameByYearButton");
		topViewedGameByYearButton.setBounds(270, 53, 97, 27);
		analystPanel.add(topViewedGameByYearButton);
		
		maxViewedgameByGenreButton = new JButton("Max View (Genre)");
		maxViewedgameByGenreButton.setBackground(Color.LIGHT_GRAY);
		maxViewedgameByGenreButton.setContentAreaFilled(false);
		maxViewedgameByGenreButton.setOpaque(true);
		maxViewedgameByGenreButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		maxViewedgameByGenreButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		maxViewedgameByGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				StatusObject<HashMap<String,Statistics>> maxViewedGameByGenreStatus = logicHandler.getMaxViewedGameByGen();
				
				if( maxViewedGameByGenreStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> maxViewedGenreHashMap = new HashMap<String,Double>();
					
					for( String key: maxViewedGameByGenreStatus.element.keySet() ){
						
						maxViewedGenreHashMap.put(key + " - " + maxViewedGameByGenreStatus.element.get(key).getGames() + 
								" - " + maxViewedGameByGenreStatus.element.get(key).getViewsCount().doubleValue(),
								maxViewedGameByGenreStatus.element.get(key).getViewsCount().doubleValue());
					}
					
					maxViewedGameByGenrePanel = new PieChartPanel("Most Viewed Games by Genre", "Genre - Game", "Views", maxViewedGenreHashMap, "V", false, false, false);
					maxViewedGameByGenrePanel.setName("maxViewedGameByGenrePanel");
					
					maxViewedgameByGenreButton.setEnabled(true);
					
					plotContainer.add(maxViewedGameByGenrePanel,"maxViewedGameByGenrePanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
					
					cl.show(plotContainer, "maxViewedGameByGenrePanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve most viewed games by genre.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
				
			}
		});
		maxViewedgameByGenreButton.setToolTipText("Click Here to see the most viewed games (by genre)");
		maxViewedgameByGenreButton.setName("maxViewedGameByGenreButton");
		maxViewedgameByGenreButton.setMargin(new Insets(2, 2, 2, 2));
		maxViewedgameByGenreButton.setFont(new Font("Corbel", Font.PLAIN, 12));
		maxViewedgameByGenreButton.setBounds(586, 53, 97, 27);
		analystPanel.add(maxViewedgameByGenreButton);
		
		maxRatedGameByGenreButton = new JButton("Max Rate (Genre)");
		maxRatedGameByGenreButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		maxRatedGameByGenreButton.setBackground(Color.LIGHT_GRAY);
		maxRatedGameByGenreButton.setContentAreaFilled(false);
		maxRatedGameByGenreButton.setOpaque(true);
		maxRatedGameByGenreButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		maxRatedGameByGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				StatusObject<HashMap<String,Statistics>> maxRatedGameByGenreStatus = logicHandler.getMaxRatedGamesByGen();
				
				if( maxRatedGameByGenreStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> maxRatedGenreHashMap = new HashMap<String,Double>();
					
					for( String key: maxRatedGameByGenreStatus.element.keySet() ){
						
						if( maxRatedGameByGenreStatus.element.get(key).getRating() == null 
								|| maxRatedGameByGenreStatus.element.get(key).getGames() == null ) {
							continue;
						}
						
						maxRatedGenreHashMap.put(key + " - " + maxRatedGameByGenreStatus.element.get(key).getGames() + 
								" - " + maxRatedGameByGenreStatus.element.get(key).getRating(),
								maxRatedGameByGenreStatus.element.get(key).getRating());
					}
					
					maxRatedGameByGenrePanel = new PieChartPanel("Most Viewed Games by Genre", "Genre - Game", "Rate", maxRatedGenreHashMap, "V", false, false, false);
					maxRatedGameByGenrePanel.setName("maxRatedGameByGenrePanel");
					
					maxRatedGameByGenreButton.setEnabled(true);
					
					plotContainer.add(maxRatedGameByGenrePanel,"maxRatedGameByGenrePanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
					
					cl.show(plotContainer, "maxRatedGameByGenrePanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve max rated games by genre.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		maxRatedGameByGenreButton.setToolTipText("Click Here to see the most Rated games (by genre)");
		maxRatedGameByGenreButton.setName("maxRatedGameByGenreButton");
		maxRatedGameByGenreButton.setMargin(new Insets(2, 2, 2, 2));
		maxRatedGameByGenreButton.setFont(new Font("Corbel", Font.PLAIN, 12));
		maxRatedGameByGenreButton.setBounds(586, 23, 97, 27);
		analystPanel.add(maxRatedGameByGenreButton);
		
		viewCountByGenreButton = new JButton("View Count (Genre)");
		viewCountByGenreButton.setBackground(Color.LIGHT_GRAY);
		viewCountByGenreButton.setContentAreaFilled(false);
		viewCountByGenreButton.setOpaque(true);
		viewCountByGenreButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		viewCountByGenreButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		viewCountByGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StatusObject<HashMap<String,Integer>> viewCountGenreStatus = logicHandler.getViewsCountByGen();
				
				if( viewCountGenreStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> viewCountGenreHashMap = new HashMap<String,Double>();
					
					for( String key: viewCountGenreStatus.element.keySet() ){
						
						if( key == null || viewCountGenreStatus.element.get(key) == null ) {
							continue;
						}
						
						viewCountGenreHashMap.put(key + " - " +viewCountGenreStatus.element.get(key),viewCountGenreStatus.element.get(key).doubleValue());
					}
					
					viewCountByGenrePanel = new PieChartPanel("View Count by Genre", "Genre", "Number of Views", viewCountGenreHashMap, "V", false, false, false);
					viewCountByGenrePanel.setName("viewCountByGenrePanel");
					
					viewCountByGenreButton.setEnabled(true);
					
					plotContainer.add(viewCountByGenrePanel,"viewCountByGenrePanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
					
					cl.show(plotContainer, "viewCountByGenrePanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve most viewed games by genre.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		viewCountByGenreButton.setToolTipText("Click Here to See the view count for each genre");
		viewCountByGenreButton.setName("viewCountByGenreButton");
		viewCountByGenreButton.setMargin(new Insets(2, 2, 2, 2));
		viewCountByGenreButton.setFont(new Font("Corbel", Font.PLAIN, 11));
		viewCountByGenreButton.setBounds(685, 23, 105, 27);
		analystPanel.add(viewCountByGenreButton);
		
		gamesCountByGenreButton = new JButton("Game Count (Genre)");
		gamesCountByGenreButton.setBackground(Color.LIGHT_GRAY);
		gamesCountByGenreButton.setContentAreaFilled(false);
		gamesCountByGenreButton.setOpaque(true);
		gamesCountByGenreButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		gamesCountByGenreButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gamesCountByGenreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StatusObject<HashMap<String,Integer>> gamesCountGenreStatus = logicHandler.getGamesCountByGen();
				
				if( gamesCountGenreStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> gamesCountGenreHashMap = new HashMap<String,Double>();
					
					for( String key: gamesCountGenreStatus.element.keySet() ){
						
						if( key == null || gamesCountGenreStatus.element.get(key) == null ) {
							continue;
						}
						
						gamesCountGenreHashMap.put(key + " - " +gamesCountGenreStatus.element.get(key),gamesCountGenreStatus.element.get(key).doubleValue());
					}
					
					gamesCountByGenrePanel = new PieChartPanel("Game Count by Genre", "Genre", "Number of Games", gamesCountGenreHashMap, "V", false, false, false);
					gamesCountByGenrePanel.setName("gamesCountByGenrePanel");
					
					gamesCountByGenreButton.setEnabled(true);
					
					plotContainer.add(gamesCountByGenrePanel,"gamesCountByGenrePanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
					
					cl.show(plotContainer, "gamesCountByGenrePanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve games count by genre.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		gamesCountByGenreButton.setToolTipText("Click Here to See the game count for each genre");
		gamesCountByGenreButton.setName("gameCountByGenreButton");
		gamesCountByGenreButton.setMargin(new Insets(2, 2, 2, 2));
		gamesCountByGenreButton.setFont(new Font("Corbel", Font.PLAIN, 11));
		gamesCountByGenreButton.setBounds(685, 53, 105, 27);
		analystPanel.add(gamesCountByGenreButton);
		
		ratingsCountByGenButton = new JButton("Ratings Count (Genre)");
		ratingsCountByGenButton.setBackground(Color.LIGHT_GRAY);
		ratingsCountByGenButton.setContentAreaFilled(false);
		ratingsCountByGenButton.setOpaque(true);
		ratingsCountByGenButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		ratingsCountByGenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				StatusObject<HashMap<String,Integer>> ratingsCountGenreStatus = logicHandler.getRatingsCountByGen();
				
				if( ratingsCountGenreStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> ratingsCountGenreHashMap = new HashMap<String,Double>();
					
					for( String key: ratingsCountGenreStatus.element.keySet() ){
						
						if( key == null || ratingsCountGenreStatus.element.get(key) == null ) {
							continue;
						}
						
						ratingsCountGenreHashMap.put(key + " - " +ratingsCountGenreStatus.element.get(key),ratingsCountGenreStatus.element.get(key).doubleValue());
					}
					
					ratingsCountByGenrePanel = new PieChartPanel("Ratings Count by Genre", "Genre", "Number of Ratings", ratingsCountGenreHashMap, "V", false, false, false);
					ratingsCountByGenrePanel.setName("ratingsCountByGenrePanel");
					
					ratingsCountByGenButton.setEnabled(true);
					
					plotContainer.add(ratingsCountByGenrePanel,"ratingsCountByGenrePanel");
					
					CardLayout cl = (CardLayout)(plotContainer.getLayout());
					
					cl.show(plotContainer, "ratingsCountByGenrePanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve ratings count by genre.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		ratingsCountByGenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ratingsCountByGenButton.setToolTipText("Click Here to See ratings count for each genre");
		ratingsCountByGenButton.setName("ratingsCountByGenreButton");
		ratingsCountByGenButton.setMargin(new Insets(2, 1, 2, 1));
		ratingsCountByGenButton.setFont(new Font("Corbel", Font.PLAIN, 11));
		ratingsCountByGenButton.setBounds(479, 53, 105, 27);
		analystPanel.add(ratingsCountByGenButton);
		
		gamesCountByYearButton = new JButton("Games Count (Year)");
		gamesCountByYearButton.setBackground(Color.LIGHT_GRAY);
		gamesCountByYearButton.setContentAreaFilled(false);
		gamesCountByYearButton.setOpaque(true);
		gamesCountByYearButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		gamesCountByYearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StatusObject<HashMap<Integer,Integer>> gamesCountByYearStatus = logicHandler.getGamesCountByYear();
				
				if( gamesCountByYearStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> gamesCountYearHashMap = new HashMap<String,Double>();
					
					for( Integer key: gamesCountByYearStatus.element.keySet() ){
						
						if( key == null || gamesCountByYearStatus.element.get(key) == null || key <= Year.now().getValue()-20  
								|| key >  Year.now().getValue() ) {
							
							continue;
						}
						
						gamesCountYearHashMap.put(key.toString(), gamesCountByYearStatus.element.get(key).doubleValue());
						
					}
					
					gamesCountByYearPanel = new AreaChartPanel("Games Count by Year", "Year", "Games Count", gamesCountYearHashMap, "V", true, false, false, "keyAsc");
					gamesCountByYearPanel.setName("gamesCountByYearPanel");
					
					gamesCountByYearButton.setEnabled(true);
					
					plotContainer.add(gamesCountByYearPanel,"gamesCountByYearPanel");
					
				    CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "gamesCountByYearPanel");
						
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve games count by year.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		gamesCountByYearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gamesCountByYearButton.setToolTipText("Click Here to See games count for each year");
		gamesCountByYearButton.setName("gamesCountByYearButton");
		gamesCountByYearButton.setMargin(new Insets(2, 1, 2, 1));
		gamesCountByYearButton.setFont(new Font("Corbel", Font.PLAIN, 12));
		gamesCountByYearButton.setBounds(371, 53, 105, 27);
		analystPanel.add(gamesCountByYearButton);
		
		viewsCountByYearButton = new JButton("Views Count (Year)");
		viewsCountByYearButton.setBackground(Color.LIGHT_GRAY);
		viewsCountByYearButton.setContentAreaFilled(false);
		viewsCountByYearButton.setOpaque(true);
		viewsCountByYearButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		viewsCountByYearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				StatusObject<HashMap<Integer,Integer>> viewsCountByYearStatus = logicHandler.getViewsCountByYear();
				
				if( viewsCountByYearStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> viewsCountYearHashMap = new HashMap<String,Double>();
					
					for( Integer key: viewsCountByYearStatus.element.keySet() ){
						
						if( key == null || viewsCountByYearStatus.element.get(key) == null || key <= Year.now().getValue()-20  
								|| key >  Year.now().getValue() ) {
							
							continue;
						}
						
						viewsCountYearHashMap.put(key.toString(), viewsCountByYearStatus.element.get(key).doubleValue());
						
					}
					
					viewsCountByYearPanel = new AreaChartPanel("Views Count by Year", "Year", "View Count", viewsCountYearHashMap, "V", true, false, false, "keyAsc");
					viewsCountByYearPanel.setName("viewsCountByYearPanel");
					
					viewsCountByYearButton.setEnabled(true);
					
					plotContainer.add(viewsCountByYearPanel,"viewsCountByYearPanel");
					
				    CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "viewsCountByYearPanel");
						
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve most viewed games by year.");
				}
								
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		viewsCountByYearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		viewsCountByYearButton.setToolTipText("Click Here to See views count for each year");
		viewsCountByYearButton.setName("viewsCountByYearButton");
		viewsCountByYearButton.setMargin(new Insets(2, 1, 2, 1));
		viewsCountByYearButton.setFont(new Font("Corbel", Font.PLAIN, 12));
		viewsCountByYearButton.setBounds(371, 23, 105, 27);
		analystPanel.add(viewsCountByYearButton);
		
		ratingsCountByYearButton = new JButton("Ratings Count (Year)");
		ratingsCountByYearButton.setBackground(Color.LIGHT_GRAY);
		ratingsCountByYearButton.setContentAreaFilled(false);
		ratingsCountByYearButton.setOpaque(true);
		ratingsCountByYearButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		ratingsCountByYearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				StatusObject<HashMap<Integer,Integer>> ratingsCountByYearStatus = logicHandler.getRatingsCountByYear();
				
				if( ratingsCountByYearStatus.statusCode == StatusCode.OK ) {
					
					HashMap<String,Double> ratingsCountYearHashMap = new HashMap<String,Double>();
					
					for( Integer key: ratingsCountByYearStatus.element.keySet() ){
						
						if( key == null || ratingsCountByYearStatus.element.get(key) == null || key <= Year.now().getValue()-20  
								|| key >  Year.now().getValue() ) {
							
							continue;
						}
						
						ratingsCountYearHashMap.put(key.toString(), ratingsCountByYearStatus.element.get(key).doubleValue());
						
					}
					
					ratingsCountByYearPanel = new AreaChartPanel("Ratings Count by Year", "Year", "Ratings Count", ratingsCountYearHashMap, "V", true, false, false, "keyAsc");
					ratingsCountByYearPanel.setName("gamesCountByYearPanel");
					
					ratingsCountByYearButton.setEnabled(true);
					
					plotContainer.add(ratingsCountByYearPanel,"ratingsCountByYearPanel");
					
				    CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "ratingsCountByYearPanel");
						
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve ratings count by year.");
				}
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
			}
		});
		ratingsCountByYearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ratingsCountByYearButton.setToolTipText("Click Here to See rate count for each year");
		ratingsCountByYearButton.setName("ratingsCountByYearButton");
		ratingsCountByYearButton.setMargin(new Insets(2, 2, 2, 2));
		ratingsCountByYearButton.setFont(new Font("Corbel", Font.PLAIN, 11));
		ratingsCountByYearButton.setBounds(479, 23, 105, 27);
		analystPanel.add(ratingsCountByYearButton);
		
		gameCountYearGenButton = new JButton("Games Count By Genre");
		gameCountYearGenButton.setBackground(Color.LIGHT_GRAY);
		gameCountYearGenButton.setContentAreaFilled(false);
		gameCountYearGenButton.setOpaque(true);
		gameCountYearGenButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		gameCountYearGenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				String text = gameCountTextField.getText();
				
				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
				
				int year = 0;
				
				try {	
					year = Integer.parseInt(text);
				} catch(Exception ee) {
					System.out.println("->[GraphicInterface] error in parsing gameCountTextField string. Year set to 2000.");
					year = 2000;
				}
				
				if( year < 1900 || year > Year.now().getValue() ) {
					
					return;
				}
				
				StatusObject<HashMap<Integer,HashMap<String,Integer>>> gameCountYearGenStatus = logicHandler.getGamesCountByYearGen();
				
				if( gameCountYearGenStatus.statusCode == StatusCode.OK ) {
					
					boolean found = false;
					HashMap<String,Double> data = new HashMap<String,Double>();
					
					for( int key : gameCountYearGenStatus.element.keySet() ) {
						
						if( key != year ) {
							
							continue;
						}
						
						found = true;
						
						for( String genre: gameCountYearGenStatus.element.get(key).keySet() ) {
							
							if( genre != null && gameCountYearGenStatus.element.get(key).get(genre) != null ) {
								
								data.put(genre+" - "+gameCountYearGenStatus.element.get(key).get(genre), gameCountYearGenStatus.element.get(key).get(genre).doubleValue());
							}
						}	
					}
					
					if( !found ) {
						return;
					}
					
					gameCountYearGenPanel = new PieChartPanel("Game Genre Distribution in " + year, "Genre", "Game Count", data, "V", true, false, false);
					gameCountYearGenPanel.setName("gameCountYearGenPanel");
					
					gameCountYearGenButton.setEnabled(true);
					
					plotContainer.add(gameCountYearGenPanel,"gameCountYearGenPanel");
					
				    CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "gameCountYearGenPanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve game count for year " + year + ".");
				}
			}
		});
		gameCountYearGenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gameCountYearGenButton.setToolTipText("Select Year In Order To See Genre Distribution");
		gameCountYearGenButton.setName("gameCountYearGenButton");
		gameCountYearGenButton.setMargin(new Insets(2, 1, 2, 1));
		gameCountYearGenButton.setFont(new Font("Corbel", Font.PLAIN, 10));
		gameCountYearGenButton.setBounds(267, 93, 105, 27);
		analystPanel.add(gameCountYearGenButton);
		
		ratingsCountYearGenButton = new JButton("Ratings Count Year Gen");
		ratingsCountYearGenButton.setBackground(Color.LIGHT_GRAY);
		ratingsCountYearGenButton.setContentAreaFilled(false);
		ratingsCountYearGenButton.setOpaque(true);
		ratingsCountYearGenButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		ratingsCountYearGenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String text = ratingsCountTextField.getText();

				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
				
				int year = 0;
				
				try {	
					year = Integer.parseInt(text);
				} catch(Exception ee) {
					System.out.println("->[GraphicInterface] error in parsing ratingsCountTextField string. Year set to 2000");
					year = 2000;
				}
				
				if( year < 1900 || year > Year.now().getValue() ) {
					
					return;
				}
				
				StatusObject<HashMap<Integer,HashMap<String,Integer>>> ratingsYearGenStatus = logicHandler.getRatingsCountByYearGen();
				
				if( ratingsYearGenStatus.statusCode == StatusCode.OK ) {
					
					boolean found = false;
					HashMap<String,Double> data = new HashMap<String,Double>();
					
					for( int key : ratingsYearGenStatus.element.keySet() ) {
						
						if( key != year ) {
							
							continue;
						}
						
						found = true;
						
						for( String genre: ratingsYearGenStatus.element.get(key).keySet() ) {
							
							if( genre != null && ratingsYearGenStatus.element.get(key).get(genre) != null ) {
								
								data.put(genre+" - "+ratingsYearGenStatus.element.get(key).get(genre), ratingsYearGenStatus.element.get(key).get(genre).doubleValue());
							}
						}	
					}
					
					if( !found ) {
						return;
					}
					
					ratingsCountYearGenPanel = new PieChartPanel("Ratings Genre Distribution in " + year, "Genre", "Ratings Count", data, "V", true, false, false);
					ratingsCountYearGenPanel.setName("ratingsCountYearGenPanel");
					
					ratingsCountYearGenButton.setEnabled(true);
					
					plotContainer.add(ratingsCountYearGenPanel,"ratingsCountYearGenPanel");
					
				    CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "ratingsCountYearGenPanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve ratings count for year " + year + ".");
				}
			}
		});
		ratingsCountYearGenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ratingsCountYearGenButton.setToolTipText("Click Here to See rate count for each year");
		ratingsCountYearGenButton.setName("ratingsCountYearGenButton");
		ratingsCountYearGenButton.setMargin(new Insets(2, 2, 2, 2));
		ratingsCountYearGenButton.setFont(new Font("Corbel", Font.PLAIN, 11));
		ratingsCountYearGenButton.setBounds(685, 93, 105, 27);
		analystPanel.add(ratingsCountYearGenButton);
		
		viewsCountByYearGenButton = new JButton("ViewCount Year Gen");
		viewsCountByYearGenButton.setBackground(Color.LIGHT_GRAY);
		viewsCountByYearGenButton.setContentAreaFilled(false);
		viewsCountByYearGenButton.setOpaque(true);
		viewsCountByYearGenButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		viewsCountByYearGenButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				String text = viewCountTextField.getText();

				ratingsCountTextField.setText("Insert Year");
				viewCountTextField.setText("Insert Year");
				gameCountTextField.setText("Insert Year");
				
				int year = 0;
				
				try {	
					year = Integer.parseInt(text);
				} catch(Exception ee) {
					System.out.println("->[GraphicInterface] error in parsing ratingsCountTextField string. Years set to 2000");
					year = 2000;
				}
				
				if( year < 1900 || year > Year.now().getValue() ) {
					
					return;
				}
				
				StatusObject<HashMap<Integer,HashMap<String,Integer>>> viewsCountYearGenStatus = logicHandler.getViewsCountByYearGen();
				
				if( viewsCountYearGenStatus.statusCode == StatusCode.OK ) {
					
					boolean found = false;
					HashMap<String,Double> data = new HashMap<String,Double>();
					
					for( int key : viewsCountYearGenStatus.element.keySet() ) {
						
						if( key != year ) {
							
							continue;
						}
						
						found = true;
						
						for( String genre: viewsCountYearGenStatus.element.get(key).keySet() ) {
							
							if( genre != null && viewsCountYearGenStatus.element.get(key).get(genre) != null ) {
								
								data.put(genre+" - "+viewsCountYearGenStatus.element.get(key).get(genre), viewsCountYearGenStatus.element.get(key).get(genre).doubleValue());
							}
						}	
					}
					
					if( !found ) {
						return;
					}
					
					viewsCountYearGenPanel = new PieChartPanel("Views Count Genre Distribution in " + year, "Genre", "Views Count", data, "V", true, false, false);
					viewsCountYearGenPanel.setName("viewsCountYearGenPanel");
					
					viewsCountByYearGenButton.setEnabled(true);
					
					plotContainer.add(viewsCountYearGenPanel,"viewsCountYearGenPanel");
					
				    CardLayout cl = (CardLayout)(plotContainer.getLayout());
						
					cl.show(plotContainer, "viewsCountYearGenPanel");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve views count for year " + year + ".");
				}
			}
		});
		viewsCountByYearGenButton.setToolTipText("Click Here to See rate count for each year");
		viewsCountByYearGenButton.setName("viewCountByYearGenButton");
		viewsCountByYearGenButton.setMargin(new Insets(2, 2, 2, 2));
		viewsCountByYearGenButton.setFont(new Font("Corbel", Font.PLAIN, 11));
		viewsCountByYearGenButton.setBounds(479, 93, 105, 27);
		analystPanel.add(viewsCountByYearGenButton);
		
		gameCountTextField = new JTextField();
		gameCountTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				gameCountTextField.setText("");
			}
		});
		gameCountTextField.setFont(new Font("Corbel", Font.PLAIN, 15));
		gameCountTextField.setText("Insert Year");
		gameCountTextField.setBounds(192, 93, 75, 27);
		analystPanel.add(gameCountTextField);
		gameCountTextField.setColumns(10);
		
		viewCountTextField = new JTextField();
		viewCountTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				viewCountTextField.setText("");
			}
		});
		viewCountTextField.setFont(new Font("Corbel", Font.PLAIN, 15));
		viewCountTextField.setText("Insert Year");
		viewCountTextField.setColumns(10);
		viewCountTextField.setBounds(406, 93, 75, 27);
		analystPanel.add(viewCountTextField);
		
		ratingsCountTextField = new JTextField();
		ratingsCountTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				ratingsCountTextField.setText("");
			}
		});
		ratingsCountTextField.setFont(new Font("Corbel", Font.PLAIN, 15));
		ratingsCountTextField.setText("Insert Year");
		ratingsCountTextField.setColumns(10);
		ratingsCountTextField.setBounds(610, 93, 75, 27);
		analystPanel.add(ratingsCountTextField);
		
		
		
		
		///////// SEARCH GAMES PANEL
		
		
		
		searchGamePanel = new JPanel();
		searchGamePanel.setFont(new Font("Corbel", Font.BOLD, 13));
		searchGamePanel.setBackground(new Color(87, 86, 82));
		searchGamePanel.setName("searchGamePanel");
		panel.add(searchGamePanel, "searchGamePanel");
		searchGamePanel.setLayout(null);
		
		homeSEButton = new JButton("");
		homeSEButton.setName("homeSEButton");
		homeSEButton.setBounds(33, 37, 97, 70);
		homeSEButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanSearchGamePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage();
			}
		});
		homeSEButton.setToolTipText("Return to Homepage");
		homeSEButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeSEButton.setBackground(SystemColor.controlDkShadow);
		homeSEButton.setBorder(null);
		homeSEButton.setContentAreaFilled(false);
		homeSEButton.setOpaque(true);
		homeSEButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
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
		searchTextField.setBounds(643, 72, 207, 35);
		searchGamePanel.add(searchTextField);
		searchTextField.setColumns(10);
		
		searchButton = new JButton("");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				searchedGamesListModel.removeAllElements();
				
				String searchedString = searchTextField.getText();
				
				if( searchedString.equals("") ) {
					return;
				}
				
				System.out.println("->[GraphicInterface] searched string: " + searchedString + "." );
				
				StatusObject<DataNavigator> searchStatusObject  = logicHandler.searchGamesPreviews(searchedString);
				
				if( searchStatusObject.statusCode == StatusCode.OK ) {
					
					if( searchStatusObject.element == null ) {
						
						System.out.println("->[GraphicInterface] data navigator object is null.");
						return;
					}

					StatusObject<List <PreviewGame>> listStatusObject = searchStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( listStatusObject.element == null || listStatusObject.element.size() == 0 ) {
							return;
						}
						
						List<BufferedGame> searchedGamesList = new ArrayList<BufferedGame>();
						
						for( int i = 0; i < listStatusObject.element.size(); i++  ) {
							
							PreviewGame game = listStatusObject.element.get(i);
							String url = game.getPreviewPicURL();
							String replacement = "media/crop/600/400/games"; 
							ImageIcon icon = null;
						
							try {
								url = url.replaceFirst("media/games", replacement); 
								icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
							} catch(Exception e) {
								icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							}
							
							searchedGamesList.add(new BufferedGame(game.getId(),game.getTitle(),icon));
						} 
						
						searchGamesDataNavigator = searchStatusObject.element;
						fillSearchedGamesList(searchedGamesList);
						supportGamesList = searchedGamesList;
					    
					} else {
						
						System.out.println("->[GraphicInterface] impossible to retrieve list of preview games.");
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
		searchButton.setBounds(850, 72, 52, 35);
		searchButton.setToolTipText("Search for New Games");
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBackground(SystemColor.controlDkShadow);
		searchButton.setBorder(null);
		searchButton.setContentAreaFilled(false);
		searchButton.setOpaque(true);
		searchButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/search.png")).getImage().getScaledInstance(25, 25, Image.SCALE_FAST)));
		searchGamePanel.add(searchButton);
		
		mostViewedButton = new JButton("Most Viewed");
		mostViewedButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mostViewedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				searchedGamesListModel.removeAllElements();
	
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
					
					if( viewedStatusObject.element == null ) {
						
						System.out.println("->[GraphicInterface] impossible to retrieve data navigator object.");
						return;
					}

					StatusObject<List <PreviewGame>> listStatusObject = viewedStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( listStatusObject.element == null || listStatusObject.element.size() == 0 ) {
							return;
						}
						
						List<BufferedGame> mostViewedGamesList = new ArrayList<BufferedGame>();
						
						for( int i = 0; i < listStatusObject.element.size(); i++  ) {
							
							PreviewGame game = listStatusObject.element.get(i);
							String url = game.getPreviewPicURL();
							String replacement = "media/crop/600/400/games"; 
							ImageIcon icon = null;
						
							try {
								url = url.replaceFirst("media/games", replacement); 
								icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
							} catch(Exception ee) {
								icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							}
							
							mostViewedGamesList.add(new BufferedGame(game.getId(),game.getTitle(),icon));
						}
						
						searchGamesDataNavigator = viewedStatusObject.element;
						fillSearchedGamesList(mostViewedGamesList);
						supportGamesList = mostViewedGamesList;   
					} else {
						
						System.out.println("->[GraphicInterface] impossible to retrieve list of preview game (most viewed).");
					}
				}
			}
		});
		mostViewedButton.setBackground(Color.LIGHT_GRAY);
		mostViewedButton.setContentAreaFilled(false);
		mostViewedButton.setOpaque(true);
		mostViewedButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		mostViewedButton.setMargin(new Insets(2, 2, 2, 2));
		mostViewedButton.setFont(new Font("Corbel", Font.BOLD, 15));
		mostViewedButton.setName("mostViewedButton");
		mostViewedButton.setBounds(142, 79, 112, 28);
		searchGamePanel.add(mostViewedButton);
		
		mostLikedButton = new JButton("Most Liked");
		mostLikedButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mostLikedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				searchedGamesListModel.removeAllElements();
				
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
					
					if( likedStatusObject.element == null ) {
						
						System.out.println("->[GraphicInterface] impossible to retrieve data navigator object.");
						return;
					}

					StatusObject<List <PreviewGame>> listStatusObject = likedStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( listStatusObject.element == null || listStatusObject.element.size() == 0 ) {
							return;
						}
						
						List<BufferedGame> mostLikedGamesList = new ArrayList<BufferedGame>();
						
						for( int i = 0; i < listStatusObject.element.size(); i++  ) {
							
							PreviewGame game = listStatusObject.element.get(i);
							String url = game.getPreviewPicURL();
							String replacement = "media/crop/600/400/games"; 
							ImageIcon icon = null;
						
							try {
								url = url.replaceFirst("media/games", replacement); 
								icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
							} catch(Exception ee) {
								icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							}
							
							mostLikedGamesList.add(new BufferedGame(game.getId(),game.getTitle(),icon));
						}
						
						searchGamesDataNavigator = likedStatusObject.element;
						fillSearchedGamesList(mostLikedGamesList);
						supportGamesList = mostLikedGamesList;   
					} else{
						
						System.out.println("->[GraphicInterface] impossible to retieve list of preview games (most liked).");
					}
				}
			}
		});
		mostLikedButton.setBackground(Color.LIGHT_GRAY);
		mostLikedButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		mostLikedButton.setFont(new Font("Corbel", Font.BOLD, 15));
		mostLikedButton.setMargin(new Insets(2, 2, 2, 2));
		mostLikedButton.setName("mostLikedButton");
		mostLikedButton.setBounds(266, 37, 112, 28);
		mostLikedButton.setContentAreaFilled(false);
		mostLikedButton.setOpaque(true);
		searchGamePanel.add(mostLikedButton);
		
		mostRecentButton = new JButton("Most Recent");
		mostRecentButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mostRecentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				searchedGamesListModel.removeAllElements();
				
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
					
					if( recentStatusObject.element == null ) {
						
						System.out.println("->[GraphicInterface] impossible to retrieve data navigator object.");
						return;
					}

					StatusObject<List <PreviewGame>> listStatusObject = recentStatusObject.element.getNextData();
					
					if( listStatusObject.statusCode == StatusCode.OK ) {
						
						if( listStatusObject.element == null || listStatusObject.element.size() == 0 ) {
							return;
						}
						
						List<BufferedGame> mostRecentGamesList = new ArrayList<BufferedGame>();
						
						for( int i = 0; i < listStatusObject.element.size(); i++  ) {
							
							PreviewGame game = listStatusObject.element.get(i);
							String url = game.getPreviewPicURL();
							String replacement = "media/crop/600/400/games"; 
							ImageIcon icon = null;
						
							try {
								url = url.replaceFirst("media/games", replacement); 
								icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
							} catch(Exception ee) {
								icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							}
							
							mostRecentGamesList.add(new BufferedGame(game.getId(),game.getTitle(),icon));
						}
						
						searchGamesDataNavigator = recentStatusObject.element;
						fillSearchedGamesList(mostRecentGamesList);
						supportGamesList = mostRecentGamesList;   
					} else {
						
						System.out.println("->[GraphicInterface] impossible to retrieve list of preview games (most recent).");
					}
				}
			}
		});
		mostRecentButton.setBackground(Color.LIGHT_GRAY);
		mostRecentButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		mostRecentButton.setMargin(new Insets(2, 2, 2, 2));
		mostRecentButton.setFont(new Font("Corbel", Font.BOLD, 15));
		mostRecentButton.setName("mostRecentButton");
		mostRecentButton.setBounds(266, 79, 112, 28);
		mostRecentButton.setContentAreaFilled(false);
		mostRecentButton.setOpaque(true);
		searchGamePanel.add(mostRecentButton);
		
		featuredButton = new JButton("Featured");
		featuredButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		featuredButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				searchedGamesListModel.removeAllElements();
				
				featuredButton.setBackground(new Color(30,144,255));
				featuredButton.setForeground(Color.WHITE);
				
				mostRecentButton.setForeground(Color.BLACK);
				mostRecentButton.setBackground(Color.LIGHT_GRAY);
				mostViewedButton.setForeground(Color.BLACK);
				mostViewedButton.setBackground(Color.LIGHT_GRAY);
				mostLikedButton.setForeground(Color.BLACK);
				mostLikedButton.setBackground(Color.LIGHT_GRAY);
				
				searchTextField.setText("Search");
				
				StatusObject<List<GraphGame>> featuredStatusObject  = graphHandler.getFeaturedGamesList();
				
				if( featuredStatusObject.statusCode == StatusCode.OK ) {
					
					if( featuredStatusObject.element == null || featuredStatusObject.element.size() == 0 ) {
						
						System.out.println("->[GraphicInterface] no feaured games.");
						return;
					}
					
					List<BufferedGame> featuredGamesList = new ArrayList<BufferedGame>();
					
					for( int i = 0; i < featuredStatusObject.element.size(); i++ ) {
						
						GraphGame game = featuredStatusObject.element.get(i);
						String url = game.previewImage;
						String replacement = "media/crop/600/400/games"; 
						ImageIcon icon = null;
					
						try {
							url = url.replaceFirst("media/games", replacement); 
							icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
						} catch(Exception ee) {
							icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
						}
						
						featuredGamesList.add(new BufferedGame(Integer.parseInt(game._id),game.title,icon));
					}
					
					searchGamesDataNavigator = null;
					fillSearchedGamesList(featuredGamesList);
					supportGamesList = featuredGamesList;
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve featured games.");
				}
			}
		});
		featuredButton.setBackground(new Color(30, 144, 255));
		featuredButton.setForeground(Color.WHITE);
		featuredButton.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		featuredButton.setMargin(new Insets(2, 2, 2, 2));
		featuredButton.setFont(new Font("Corbel", Font.BOLD, 15));
		featuredButton.setName("featuredButton");
		featuredButton.setBounds(142, 37, 112, 28);
		featuredButton.setContentAreaFilled(false);
		featuredButton.setOpaque(true);
		searchGamePanel.add(featuredButton);
		
		searchGameScrollPane = new JScrollPane();
		searchGameScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		searchGameScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		searchGameScrollPane.setName("searchGameScrollPane");
		searchGameScrollPane.setBounds(31, 160, 871, 352);
		searchGamePanel.add(searchGameScrollPane);
	
		searchedGamesJList = new JList<BufferedGame>(searchedGamesListModel);
		searchedGamesJList.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseDragged(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void updateCursor(MouseEvent e) {
				if( searchedGamesJList.getToolTipText(e) != null ) {
					searchedGamesJList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					searchedGamesJList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}	
			}
		});
		searchedGamesJList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if( searchedGamesJList.getToolTipText(arg0) == null ) {
					
					return;
				}
				
				BufferedGame selectedGame = searchedGamesJList.getSelectedValue();
				
				cleanSearchGamePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				initializeGamePage(selectedGame.getId());
			}
		});
		searchedGamesJList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		searchedGamesJList.setVisibleRowCount(-1);
		searchedGamesJList.setName("searchGameJList");
		searchedGamesJList.setCellRenderer(new BufferedGameRenderer());
		searchGameScrollPane.setViewportView(searchedGamesJList);
		
		searchGamesVerticalScrollBarListener =  new AdjustmentListener() {
		      public void adjustmentValueChanged(AdjustmentEvent e) {
		    	  JScrollBar bar = (JScrollBar)e.getAdjustable();
			      int extent = bar.getModel().getExtent();
			      int value = bar.getValue();
			      int max = bar.getMaximum();
			      
			      if( searchGamesDataNavigator == null ) {
			    	  
			    	  return;
			      }
			      
			      if( value == 0 ) {
			    	  
			    	  StatusObject<List<PreviewGame>> status = searchGamesDataNavigator.getPrevData();
			    	  
			    	  if( status.statusCode == StatusCode.OK ) {
			    		  
			    		  if( status.element == null || status.element.size() == 0) {
			    			  
			    			  System.out.println("->[GraphicInterface] no more games.");
			    			  return;
			    		  }
			    		  
			    		  List<BufferedGame> gamesList = new ArrayList<BufferedGame>();
			    		  
			    		  for( int i=0; i < status.element.size(); i++ ) {
			    			  
			    			  PreviewGame game = status.element.get(i);
			    			  String url = game.getPreviewPicURL();
							  String replacement = "media/crop/600/400/games"; 
							  ImageIcon icon = null;
							
							  try {
								  url = url.replaceFirst("media/games", replacement); 
								  icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
						      } catch(Exception ee) {
								  icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							  }
								
								gamesList.add(new BufferedGame(game.getId(),game.getTitle(),icon));  
			    		  }
			    		  
			    		  fillSearchedGamesList(gamesList);
			    		  supportGamesList = gamesList;
			    		  bar.setValue(1);
			    	  }  else {
			    		  
			    		  System.out.println("->[GraphicInterface] impossible to retrieve prev games.");
			    	  }
			      } 
			      
			      if( value+extent == max  ) {
			    	  
			    	  StatusObject<List<PreviewGame>> status = searchGamesDataNavigator.getNextData();
			    	  
			    	  if( status.statusCode == StatusCode.OK ) {
			    		  
			    		  if( status.element == null || status.element.size() == 0) {
			    			  
			    			  System.out.println("->[GraphicInterface] no more games.");
			    			  return;
			    		  }
			    		  
			    		  List<BufferedGame> gamesList = new ArrayList<BufferedGame>();
			    		  
			    		  for( int i=0; i < status.element.size(); i++ ) {
			    			  
			    			  PreviewGame game = status.element.get(i);
			    			  String url = game.getPreviewPicURL();
							  String replacement = "media/crop/600/400/games"; 
							  ImageIcon icon = null;
							
							  try {
								  url = url.replaceFirst("media/games", replacement); 
								  icon = new ImageIcon(ImageIO.read(new URL(url)).getScaledInstance(80, 100, Image.SCALE_FAST));
						      } catch(Exception ee) {
								  icon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGamePicture.png")).getImage().getScaledInstance(80, 100, Image.SCALE_FAST));
							  }
								
								gamesList.add(new BufferedGame(game.getId(),game.getTitle(),icon));  
			    		  }
			    		  
			    		  fillSearchedGamesList(gamesList);
			    		  supportGamesList = gamesList;
			    		  bar.setValue(1);
			    	  } else{
			    		  
			    		  System.out.println("->[GraphicInterface] impossible to retrieve next games.");
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
		gameGenreMenuBar.setBounds(400, 53, 48, 35);
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
		
		
		
		/////////// GAME PANEL
		
		
		
		gamePanel = new JPanel();
		gamePanel.setBackground(new Color(87, 86, 82));
		gamePanel.setName("gamePanel");
		panel.add(gamePanel, "gamePanel");
		gamePanel.setLayout(null);
		
		gameDescriptionScrollPane = new JScrollPane();
		gameDescriptionScrollPane.setBorder(null);
		gameDescriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		gameDescriptionScrollPane.setBounds(62, 70, 324, 174);
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
		gameTitleLabel.setFont(new Font("Corbel", Font.BOLD, 24));
		gameTitleLabel.setBounds(62, 33, 825, 37);
		gamePanel.add(gameTitleLabel);
		
		previewImageLabel = new JLabel("");
		previewImageLabel.setName("previewImageLabel");
		previewImageLabel.setBounds(426, 70, 342, 174);
		gamePanel.add(previewImageLabel);
		
		steamButton = new JButton("");
		steamButton.setBackground(SystemColor.controlDkShadow);
		steamButton.setName("steamButton");
		steamButton.setBounds(62, 257, 73, 62);
		steamButton.setContentAreaFilled(false);
		steamButton.setOpaque(true);
		steamButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/steam.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		gamePanel.add(steamButton);
		
		nintendoButton = new JButton("");
		nintendoButton.setBackground(SystemColor.controlDkShadow);
		nintendoButton.setName("nintendoButton");
		nintendoButton.setContentAreaFilled(false);
		nintendoButton.setOpaque(true);
		nintendoButton.setBounds(147, 257, 73, 62);
		nintendoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/nintendo.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		gamePanel.add(nintendoButton);
		
		playStationButton = new JButton("");
		playStationButton.setBackground(SystemColor.controlDkShadow);
		playStationButton.setName("playStationButton");
		playStationButton.setBounds(313, 257, 73, 62);
		playStationButton.setContentAreaFilled(false);
		playStationButton.setOpaque(true);
		playStationButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/playstation.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		gamePanel.add(playStationButton);
		
		XBoxButton = new JButton("");
		XBoxButton.setBackground(SystemColor.controlDkShadow);
		XBoxButton.setName("XBoxButton");
		XBoxButton.setBounds(232, 257, 73, 62);
		XBoxButton.setContentAreaFilled(false);
		XBoxButton.setOpaque(true);
		XBoxButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/xbox.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		gamePanel.add(XBoxButton);
		
		homeGameButton = new JButton("");
		homeGameButton.setName("homeGameButton");
		homeGameButton.setBounds(790, 70, 97, 68);
		homeGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanGamePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage();
			}
		});
		homeGameButton.setToolTipText("Return to Homepage");
		homeGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeGameButton.setBackground(SystemColor.controlDkShadow);
		homeGameButton.setBorder(null);
		homeGameButton.setContentAreaFilled(false);
		homeGameButton.setOpaque(true);
		homeGameButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		gamePanel.add(homeGameButton);
		
		actionButton = new JButton("");
		actionButton.setToolTipText("Click Here to Add/Remove to/from Your Favourite Games");
		actionButton.setName("actionButton");
		actionButton.setBounds(494, 257, 73, 62);
		actionButton.setBackground(SystemColor.controlDkShadow);
		actionButton.setContentAreaFilled(false);
		actionButton.setOpaque(true);
		actionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/minus.png")).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));
		actionButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				if( isGameFavourite == null ) {
					return;
				}
				
				if( isGameFavourite ) {
					
					if( graphHandler.removeFromFavourites(currentGame.getId().toString()) == StatusCode.OK ) {
						
						actionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png")).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));
						isGameFavourite = false;
						
						System.out.println("->[GraphicInterface] game correctly remove from favourites.");
					}else {
						
						System.out.println("->[GraphicInterface] impossbile to remove game from favourites.");
						isGameFavourite = (Boolean) null;
					}
				} else if( !isGameFavourite ){
					
					if( graphHandler.addToFavourites(currentGame.getId().toString()) == StatusCode.OK ) {
						
						actionButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/add.png")).getImage().getScaledInstance(30, 30, Image.SCALE_FAST)));
						isGameFavourite = false;
						
						System.out.println("->[GraphicInterface] game correctly added to favourites.");
					} else {
						
						System.out.println("->[GraphicInterface] impossible to add game to favourites.");
						isGameFavourite = (Boolean) null;
					}
				} 
				
			}
		});
		gamePanel.add(actionButton);
		
		releaseDateLabel = new JLabel("Release Date: 02/02/0202");
		releaseDateLabel.setForeground(Color.WHITE);
		releaseDateLabel.setFont(new Font("Corbel", Font.PLAIN, 17));
		releaseDateLabel.setName("releaseDateLabel");
		releaseDateLabel.setBounds(579, 257, 189, 62);
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
		voteMenuBar.setBounds(426, 257, 52, 62);
		gamePanel.add(voteMenuBar);
		
		voteMenu = new JMenu("Vote");
		voteMenu.setFont(new Font("Corbel", Font.PLAIN, 15));
		voteMenuBar.add(voteMenu);
		voteMenu.setName("voteMenu");
		
		vote1 = new JMenuItem("1");
		vote1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				graphHandler.rateGame(currentGame.getTitle(), 1);
					
			}
		});
		vote1.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote1.setName("vote1");
		voteMenu.add(vote1);
		
		vote2 = new JMenuItem("2");
		vote2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				graphHandler.rateGame(currentGame.getTitle(), 2);
			}
		});
		vote2.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote2.setName("vote2");
		voteMenu.add(vote2);
		
		vote3 = new JMenuItem("3");
		vote3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				graphHandler.rateGame(currentGame.getTitle(), 3);
			}
		});
		vote3.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote3.setName("vote3");
		voteMenu.add(vote3);
		
		vote4 = new JMenuItem("4");
		vote4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				graphHandler.rateGame(currentGame.getTitle(), 4);
			}
		});
		vote4.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote4.setName("vote4");
		voteMenu.add(vote4);
		
		vote5 = new JMenuItem("5");
		vote5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				graphHandler.rateGame(currentGame.getTitle(), 5);
			}
		});
		vote5.setFont(new Font("Corbel", Font.PLAIN, 15));
		vote5.setName("vote5");
		voteMenu.add(vote5);
		
		metacriticScoreLabel = new JLabel("");
		metacriticScoreLabel.setBorder(new EmptyBorder(0, 0, 0, 5));
		metacriticScoreLabel.setFont(new Font("Corbel", Font.BOLD, 17));
		metacriticScoreLabel.setForeground(Color.WHITE);
		metacriticScoreLabel.setBackground(SystemColor.controlDkShadow);
		metacriticScoreLabel.setToolTipText("Metacritic Score");
		metacriticScoreLabel.setName("metaciticScoreLabel");
		metacriticScoreLabel.setBounds(789, 182, 98, 62);
		metacriticScoreLabel.setOpaque(true);
		metacriticScoreLabel.setText("4.7");
		metacriticScoreLabel.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/star.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
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
				
				videoPlayer.stopVideo();
				
				videoPlayer.playVideo(currentVideosURLlist.get(currentVideoIndex));
				
				System.out.println("->[GraphicInterface] currently displayed video " + (currentVideoIndex+1) + ".");
				
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
		nextVideoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/next.png")).getImage().getScaledInstance(40, 40, Image.SCALE_FAST)));
		gamePanel.add(nextVideoButton);
		
		previousVideoButton = new JButton("");
		previousVideoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if( currentVideoIndex == 0 ) {
					
					return;
				}
				
				currentVideoIndex--;
				
				videoPlayer.stopVideo();
				
				videoPlayer.playVideo(currentVideosURLlist.get(currentVideoIndex));
				
				System.out.println("->[GraphicInterface] currently displayed video " + (currentVideoIndex+1) + ".");
				
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
		previousVideoButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/back.png")).getImage().getScaledInstance(40, 40, Image.SCALE_FAST)));
		gamePanel.add(previousVideoButton);
		
		
		
		//////// USER PANEL
		
		
		
		userPanel = new JPanel();
		userPanel.setBackground(new Color(87, 86, 82));
		userPanel.setName("userPanel");
		panel.add(userPanel, "userPanel");
		userPanel.setLayout(null);
		
		featuredUserButton = new JButton("Featured");
		featuredUserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		featuredUserButton.setName("featuredUserButton");
		featuredUserButton.setRequestFocusEnabled(false);
		featuredUserButton.setBounds(502, 73, 97, 32);
		featuredUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				featuredUserButton.setBackground(new Color(30, 144, 255));
				featuredUserButton.setForeground(Color.WHITE);
				
				StatusObject<List<User>> featuredUserStatus = graphHandler.getSuggestedUsersList();
				
				if( featuredUserStatus.statusCode == StatusCode.OK ) {
					
					fillUsersTable(featuredUserStatus.element);
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve featured users.");
				}
				
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
		searchUserTextField.setBounds(611, 74, 189, 31);
		userPanel.add(searchUserTextField);
		searchUserTextField.setColumns(10);
		
		searchUserButton = new JButton("");
		searchUserButton.setName("searchUserButton");
		searchUserButton.setBounds(798, 73, 52, 32);
		searchUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String searchedString = searchUserTextField.getText();
				
				if( searchedString.equals("") ) {
					
					System.out.println("->[GraphicInterface] searched empty string.");
					return;
				}
				
				System.out.println("->[GraphicInterface] searched string: " + searchedString + ".");
				
				featuredUserButton.setBackground(Color.WHITE);
				featuredUserButton.setForeground(Color.BLACK);
				
				StatusObject<List<User>> searchedUserStatus = graphHandler.searchUsers(searchedString);
				
				if( searchedUserStatus.statusCode == StatusCode.OK ) {
					
					fillUsersTable(searchedUserStatus.element);
					userGamesListModel.removeAllElements();
					displayedUserLabel.setText("Currently Displayed: ");
				} else {
					
					System.out.println("->[GraphicInterface] impossible to retrieve searched users.");
				}
			}
		});
		searchUserButton.setToolTipText("Search for New Users");
		searchUserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchUserButton.setBackground(SystemColor.controlDkShadow);
		searchUserButton.setBorder(null);
		searchUserButton.setContentAreaFilled(false);
		searchUserButton.setOpaque(true);
		searchUserButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/search.png")).getImage().getScaledInstance(25, 25, Image.SCALE_FAST)));
		userPanel.add(searchUserButton);
		
		homeUserButton = new JButton("");
		homeUserButton.setName("homeUserButton");
		homeUserButton.setBounds(57, 38, 97, 67);
		homeUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				cleanUserPage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "homePagePanel");
				
				initializeHomePage();
			}
		});
		homeUserButton.setToolTipText("Return to Homepage");
		homeUserButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeUserButton.setBackground(SystemColor.controlDkShadow);
		homeUserButton.setBorder(null);
		homeUserButton.setContentAreaFilled(false);
		homeUserButton.setOpaque(true);
		homeUserButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		userPanel.add(homeUserButton);
		
		userGamesScrollPane = new JScrollPane();
		userGamesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userGamesScrollPane.setBounds(57, 146, 355, 336);
		userPanel.add(userGamesScrollPane);
		
		userGamesList = new JList<BufferedGame>(userGamesListModel);
		userGamesList.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseDragged(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void updateCursor(MouseEvent e) {
				if( userGamesList.getToolTipText(e) != null ) {
					userGamesList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					userGamesList.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}	
			}
		});
		userGamesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if( userGamesList.getToolTipText(arg0) == null ) {
					
					return;
				}
				
				BufferedGame selectedGame = userGamesList.getSelectedValue();
				
				cleanHomePage();
				
				CardLayout cl = (CardLayout)(panel.getLayout());
				
				cl.show(panel, "gamePanel");
				
				initializeGamePage(selectedGame.getId());
			}
		});
		userGamesList.setName("userGamesList");
		userGamesList.setVisibleRowCount(-1);
		userGamesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		userGamesList.setCellRenderer(new BufferedGameRenderer());
		userGamesScrollPane.setViewportView(userGamesList);
		
		usersScrollPane = new JScrollPane();
		usersScrollPane.setBounds(502, 146, 348, 336);
		userPanel.add(usersScrollPane);
		
		usersTable = new JTable();
		usersTable.setRowSelectionAllowed(false);
		usersTable.setFocusable(false);
		usersTable.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseDragged(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void mouseMoved(MouseEvent e) {
				
				updateCursor(e);
			}
			
			public void updateCursor(MouseEvent e) {
				if( usersTable.columnAtPoint(e.getPoint()) == 1 || usersTable.columnAtPoint(e.getPoint()) == 2) {
					usersTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					usersTable.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}	
			}
		});
		usersTable.setName("usersTable");
		usersTable.setModel(usersTableModel);
		usersTable.setFont(new Font("Corbel",Font.PLAIN,16));
		usersTable.setRowHeight(30);
		usersTable.setDefaultRenderer(String.class, centerRenderer);
		usersTableHeader = usersTable.getTableHeader();
		usersTableHeader.setFont(titleFont);
		usersTableHeader.setForeground(Color.WHITE);
		usersTableHeader.setBackground(new Color(121,166,210));
		usersScrollPane.setViewportView(usersTable);
		
		displayedUserLabel = new JLabel("Currently Displayed: Gianni's Games");
		displayedUserLabel.setForeground(Color.WHITE);
		displayedUserLabel.setFont(new Font("Corbel", Font.BOLD, 15));
		displayedUserLabel.setName("displayedUserLabel");
		displayedUserLabel.setAutoscrolls(true);
		displayedUserLabel.setBounds(57, 128, 363, 16);
		userPanel.add(displayedUserLabel);
		
		searchUserWelcomeLabel = new JLabel("Hi User!");
		searchUserWelcomeLabel.setName("searchUserWelcomeLabel");
		searchUserWelcomeLabel.setForeground(Color.WHITE);
		searchUserWelcomeLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		searchUserWelcomeLabel.setAutoscrolls(true);
		searchUserWelcomeLabel.setBounds(166, 38, 250, 26);
		userPanel.add(searchUserWelcomeLabel);
		
		searchUserLabel = new JLabel("Here you can search for other users.");
		searchUserLabel.setName("searchUserLabel");
		searchUserLabel.setForeground(Color.WHITE);
		searchUserLabel.setFont(new Font("Corbel", Font.BOLD, 16));
		searchUserLabel.setAutoscrolls(true);
		searchUserLabel.setBounds(166, 89, 250, 16);
		userPanel.add(searchUserLabel);
		
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
				
				Long age;
				String ageString = ageTextField.getText();
				String name = nameTextField.getText();
				String surname = surnameTextfield.getText();
				String genre = genreMenu.getText();
				String gender = genderMenu.getText();
				String email = emailTextField.getText();
				String country = countryTextField.getText();
				
				if( !ageString.equals("") && !ageString.startsWith("Current")) {
					try {
						age = Long.parseLong(ageString);
						currentUser.setAge(age);
					} catch (Exception e) {
						System.out.println("->[GraphicInterface] Error in parsing age. Age not updated.");
					}
				}
				
				if( !name.equals("") && !name.startsWith("Current") ) {
					currentUser.setFirstName(name);
				}
				
				if( !surname.equals("") && !surname.startsWith("Current") ) {
					currentUser.setLastName(surname);
				}
				
				if( gender.equals("M") || gender.equals("F") ) {
					currentUser.setGender(gender.charAt(0));
				}
				
				if( !genre.equals("Genre") ) {
					currentUser.setFavouriteGenre(genre);
				}
				
				if( !email.equals("") && !email.startsWith("Current")) {
					currentUser.setEmail(email);
				}
				
				if( !country.equals("") && !country.startsWith("Current") ) {
					currentUser.setCountry(country);
				}
				
				if( graphHandler.saveUser() == StatusCode.OK ) {
					
					initializeUserInformationPage();
				} else {
					
					System.out.println("->[GraphicInterface] impossible to save user info.");
				}
				
			}
		});
		saveButton.setName("saveButton");
		saveButton.setBounds(520, 397, 127, 48);
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
		genderMenuBar.setBounds(520, 158, 80, 35);
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
		genreMenuBar.setBounds(520, 222, 80, 35);
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
				
				initializeHomePage();
			}
		});
		homeUserInformationButton.setToolTipText("Return to Homepage");
		homeUserInformationButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		homeUserInformationButton.setBackground(SystemColor.controlDkShadow);
		homeUserInformationButton.setBorder(null);
		homeUserInformationButton.setContentAreaFilled(false);
		homeUserInformationButton.setOpaque(true);
		homeUserInformationButton.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/home.png")).getImage().getScaledInstance(60, 60, Image.SCALE_FAST)));
		userInformationPanel.add(homeUserInformationButton);
		
		emailTextField = new JTextField();
		emailTextField.setText("E-Mail");
		emailTextField.setName("emailTextField");
		emailTextField.setFont(new Font("Corbel", Font.ITALIC, 15));
		emailTextField.setColumns(10);
		emailTextField.setBounds(233, 348, 233, 37);
		userInformationPanel.add(emailTextField);
		
		countryTextField = new JTextField();
		countryTextField.setFont(new Font("Corbel", Font.ITALIC, 15));
		countryTextField.setName("countryTextField");
		countryTextField.setText("Country");
		countryTextField.setBounds(233, 413, 235, 37);
		userInformationPanel.add(countryTextField);
		countryTextField.setColumns(10);
		
		ageLabel = new JLabel("Age");
		ageLabel.setName("ageLabel");
		ageLabel.setForeground(Color.WHITE);
		ageLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		ageLabel.setBounds(233, 131, 233, 25);
		userInformationPanel.add(ageLabel);
		
		nameLabel = new JLabel("Name");
		nameLabel.setName("nameLabel");
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		nameLabel.setBounds(233, 196, 233, 25);
		userInformationPanel.add(nameLabel);
		
		surnameLabel = new JLabel("Surname");
		surnameLabel.setName("surnameLabel");
		surnameLabel.setForeground(Color.WHITE);
		surnameLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		surnameLabel.setBounds(233, 258, 233, 25);
		userInformationPanel.add(surnameLabel);
		
		emailLabel = new JLabel("E-Mail");
		emailLabel.setName("emailLabel");
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		emailLabel.setBounds(233, 322, 233, 25);
		userInformationPanel.add(emailLabel);
		
		countryLabel = new JLabel("Country");
		countryLabel.setName("countryLabel");
		countryLabel.setForeground(Color.WHITE);
		countryLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		countryLabel.setBounds(233, 387, 233, 25);
		userInformationPanel.add(countryLabel);
		
		genderLabel = new JLabel("Gender");
		genderLabel.setName("genderLabel");
		genderLabel.setForeground(Color.WHITE);
		genderLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		genderLabel.setBounds(520, 131, 77, 25);
		userInformationPanel.add(genderLabel);
		
		genreLabel = new JLabel("Genre");
		genreLabel.setName("genreLabel");
		genreLabel.setForeground(Color.WHITE);
		genreLabel.setFont(new Font("Corbel", Font.BOLD, 21));
		genreLabel.setBounds(520, 196, 233, 25);
		userInformationPanel.add(genreLabel);
		
	}
}
