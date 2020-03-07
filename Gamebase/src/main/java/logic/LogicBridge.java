package logic;

import java.awt.Image;
import java.util.List;

import logic.data.*;
import logic.mongoConnection.MongoConnection;

///////////////////////////////////////////////////////////////////////////////////////////////////
//  The class is used to bridge the graphic interface to the program logic.
//  The class defines a simple API by which the upper layers could operate on the data
//  without any knowledge of the management the data will get. The class is designed to
//  work with the Neo4j database, the MongoDB keyvalue database and a webscraper API. 
//  However due to it's simplicity any other type of connection or data management could be added
//  changing the function in charge
///////////////////////////////////////////////////////////////////////////////////////////////////

public class LogicBridge {
	
	MongoConnection MONGO;
	
	public LogicBridge(){
		
		MONGO = new MongoConnection();
		
	}
	//  GRAPH FUNCTIONS
	
	//return USER,ADMINISTRATOR,ANALYST,NO_USER( no user with the given username ), WRONG_PASSWORD( user exists, but password is wrong)
	public userType login( String USERNAME , String PASSWORD ) { return userType.NO_USER; }
	
	//return false if the username is already used
	public boolean signUp( String USERNAME, String PASSWORD ) { return false; }
	
	//in case of failure return -1
	public int getFollowersNumber( String USERNAME ) { return 0; }
	
	//in case of failure return -1
	public int getLikedGamesNumber( String USERNAME ) { return 0; }
	
	public List<User> getFriends( String USERNAME ) { return null; }
	
	//simply return a friend given his/her username
	public User getFriend( String USERNAME ) { return null; }
	
	public boolean addFriend( String USERNAME ) { return false; }
	
	public boolean removeFriend( String USERNAME ) { return false; }

	public boolean deleteUser( String USERNAME ) { return false; }
	
	public boolean becomeAnalyst( String USERNAME ) { return false; }
	
	public Image getUserPicture( String USERNAME ) {return null; }
	
	public List<PreviewGame> getMyGames( String USERNAME){ return null;}
	
	//if AGE==-1, don't update AGE whithin the db. The same if NAME, SURNAME, FAVORITE_GENRE, GENDER are NULL.
	public boolean updateUserInformation( int AGE, String NAME, String SURNAME, String FAVORITE_GENRE, String GENDER ) { return false; }
	
	//USERNAME follow FOLLOWED?
	public boolean isFollowed( String USERNAME, String FOLLOWED ) { return false; }
	
	//return the total number of users; -1 in case of failure
	public int getUserCount() { return -1;}
	
	public List<PreviewGame> getFeaturedGames( String USERNAME ){ return null; }
	
	public List<User> getFeaturedUsers( String USERNAME ){ return null; }
	
	//pass the username in order to avoid returning a list containing the username itself
	public List<User> searchUsers( String USERNAME, String SEARCHED_STRING ){ return null; }
	
	public boolean follow( String USERNAME, String USERNAME_TO_FOLLOW ) { return false; }
	
	public boolean unfollow( String USERNAME, String USERNAME_TO_UNFOLLOW ) { return false; }
	
	public List<Game> getUserGames( String USERNAME ){ return null; }
	
	public boolean addUserGame( String USERNAME , String GAME ) { 

		//MONGO.updateFavouritesCount( GAME , 1);  //  TO ACTIVATE WHEN GRAPH PART IS READY
		return false; 
	}
	
	public boolean removeUserGame( String USERNAME , String GAME ) {
	
		//MONGO.updateFavouritesCount( GAME , -1);  // TO ACTIVATE WHEN GRAPH PART IS READY
		return false;
	}
	//  DOCUMENT FUNCTIONS
	
	public Game getGame( String GAME , boolean doVote ){ 
		return MONGO.getGame(GAME, doVote);
	}
	
	private boolean addGameDescription( String GAME , String DESCRIPTION ) { 
		return MONGO.addGameDescription(GAME,DESCRIPTION);
	}
	
	public boolean deleteGame( String GAME ) { 
		return MONGO.deleteGame(GAME);
	}
	
	public boolean voteGame( String GAME , int VOTE ) { 
		return MONGO.voteGame( GAME, VOTE );
	}
	
	public List<String> getGamePicsURL( String GAME_TITLE ){ return MONGO.getGamePics(GAME_TITLE); }
	
	//return the total number of games; -1 in case of failure
	public long getGameCount() { return MONGO.getTotalGamesCount(); }
	
	//return a list filled with all possible genres
	public List<String> getGenres(){ return MONGO.getGenres(); }
	
	
	public PreviewGame getMostViewedGame() { return MONGO.getMostViewedPreview(); }
	
	public PreviewGame getMostPopularGame() { return MONGO.getMostPopularPreview(); }
	
	public List<PreviewGame> getMostViewedGames(){ return MONGO.getMostViewedPreviews(); }
	
	public List<PreviewGame> getMostLikedGames(){ return MONGO.getMostLikedPreviews(); }
	
	public List<PreviewGame> getMostRecentGames(){ return MONGO.getMostRecentPreviews(); }
	
	public List<PreviewGame> searchGames( String SEARCHED_STRING ){ return MONGO.searchGames(SEARCHED_STRING); }
	
	//  KEYVALUE --- STATISTICS FUNCTIONS
	
	//  DATASCRAPER FUNCTIONS
	
	public boolean updateDatabase() { return false; }
	
	public static String getTwitchURLChannel( String GAME ) { return null; }
	
	public String getGameDescription( int GAME_ID) { return null; }
	
	//OTHER
	
	public void closeConnection(){
		MONGO.closeConnection();
	}
}
