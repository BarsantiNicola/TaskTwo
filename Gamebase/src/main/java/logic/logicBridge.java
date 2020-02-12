package logic;

import java.awt.Image;
import java.util.HashMap;
import java.util.List;

import logic.data.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
//  The class is used to bridge the graphic interface to the program logic.
//  The class defines a simple API by which the upper layers could operate on the data
//  without any knowledge of the management the data will get. The class is designed to
//  work with the Neo4j database, the MongoDB keyvalue database and a webscraper API. 
//  However due to it's simplicity any other type of connection or data management could be added
//  changing the function in charge
///////////////////////////////////////////////////////////////////////////////////////////////////

public class logicBridge {
	
	//  GRAPH FUNCTIONS
	
	//return USER,ADMINISTRATOR,ANALYST,NO_USER( no user with the given username ), WRONG_PASSWORD( user exists, but password is wrong)
	public userType login( String USERNAME , String PASSWORD ) { return userType.NO_USER; }
	
	//return false if the username is already used
	public boolean signUp( String USERNAME, String PASSWORD ) { return false; }
	
	//in case of failure return -1
	public int getFollowersNumber( String USERNAME ) { return 0; }
	
	//in case of failure return -1
	public int getLikedGamesNumber( String USERNAME ) { return 0; }
	
	public List<Friend> getFriends( String USERNAME ) { return null; }
	
	public boolean addFriend( String USERNAME ) { return false; }
	
	public boolean removeFriend( String USERNAME ) { return false; }

	public boolean deleteUser( String USERNAME ) { return false; }
	
	public boolean becomeAnalyst( String USERNAME ) { return false; }
	
	public Image getUserPicture( String USERNAME ) {return null; }
	
	public List<PreviewGame> getMyGames( String USERNAME){ return null;}
	
	//  KEYVALUE FUNCTIONS
	
	public List<Game> getUserGames( String USERNAME ){ return null; }
	
	public boolean addUserGame( String USERNAME , String GAME ) { return false; }
	
	public boolean removeUserGame( String USERNAME , String GAME ) { return false; }
	
	public Game getGame( String GAME ){ return null; }
	
	private boolean addGameDescription( String GAME , String DESCRIPTION ) { return false; }
	
	public boolean deleteGame( String GAME ) { return false; }
	
	public boolean voteGame( String GAME , int VOTE ) { return false; }
	
	public List<PreviewGame> getPreviews( HashMap<String,String> OPTIONS ){ return null; }
	
	public PreviewGame getMostViewedGame() { return null; }
	
	public PreviewGame getMostPopularGame() { return null; }
	
	//  KEYVALUE --- STATISTICS FUNCTIONS
	
	//  DATASCRAPER FUNCTIONS
	
	public String getTwitchURLChannel( String GAME ) { return null; }
	
	public String getGameDescription( String GAME ) { return null; }

}
