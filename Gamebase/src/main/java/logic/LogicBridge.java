package logic;

import java.awt.Image;
import java.time.Year;
import java.util.HashMap;
import java.util.List;

import logic.data.*;
import logic.mongoConnection.DataNavigator;
import logic.mongoConnection.MongoConnection;

///////////////////////////////////////////////////////////////////////////////////////////////////
//  The class is used to bridge the graphic interface to the program logic.
//  The class defines a simple API by which the upper layers could operate on the data
//  without any knowledge of the management the data will get. The class is designed to
//  work with the Neo4j database, the MongoDB document database and a webscraper API. 
//  However due to it's simplicity any other type of connection or data management could be added
//  changing the function in charge
///////////////////////////////////////////////////////////////////////////////////////////////////

public class LogicBridge {
	
	MongoConnection MONGO;
	
	public LogicBridge(){
		
		try {
			
			MONGO = new MongoConnection("127.0.0.1",27018);
			
		}catch( Exception e ) {
			
			System.out.println("-->[LogicBridge] Fatal Error, Invalid NET configuration");
			System.exit(1);
			
		}
		
	}
	
	//////  GRAPH FUNCTIONS
	
	//return USER,ADMINISTRATOR,ANALYST,NO_USER( no user with the given username ), WRONG_PASSWORD( user exists, but password is wrong)
	public UserType login( String USERNAME , String PASSWORD ) { return UserType.NO_USER; }
	
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
	public boolean updateUserInformation( int AGE, String NAME, String SURNAME, String FAVORITE_GENRE, String GENDER, String EMAIL ) { return false; }
	
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
	
	//////  DOCUMENT FUNCTIONS
	
	public StatusObject<Game> getGame( int gameId ){ 
		return MONGO.getGame( gameId );
	}
	
	public StatusCode updateGameViews( int gameId ){
		return MONGO.incrementGameViews(gameId);
	}
	
	private StatusCode addGameDescription( int ID , String DESCRIPTION ) { 
		return MONGO.addGameDescription( ID ,DESCRIPTION);
	}
	
	public StatusCode deleteGame( int ID ) { 
		return MONGO.deleteGame(ID);
	}
	
	public StatusCode voteGame( int gameId , int vote ) { 
		return MONGO.voteGame( gameId, vote );
	}
	
	//return the total number of games; -1 in case of failure
	public StatusObject<Long> getGameCount() { return MONGO.getTotalGamesCount(); }
	
	//return a list filled with all possible genres
	public StatusObject<List<String>> getGenres(){ return MONGO.getGenres(); }
	
	
	public StatusObject<PreviewGame> getMostViewedGame() { return MONGO.getMostViewedPreview(); }
	
	public StatusObject<PreviewGame> getMostPopularGame() { return MONGO.getMostPopularPreview(); }
	
	public StatusObject<DataNavigator> getMostViewedGames(){ return MONGO.getMostViewedPreviews(); }
	
	public StatusObject<DataNavigator> getMostLikedGames(){ return MONGO.getMostLikedPreviews(); }
	
	public StatusObject<DataNavigator> getMostRecentGames(){ return MONGO.getMostRecentPreviews(); }
	
	public StatusObject<DataNavigator> searchGames( String SEARCHED_STRING ){ return MONGO.searchGames(SEARCHED_STRING); }
	
	//  STATISTICS
	
	//  Most liked games year by year
	public StatusObject<List<Statistics>> getMostLikedGameByYearStats(){ return MONGO.getMostLikedGameByYearStats(); }
		
	//  Most viewed games year by year
	public StatusObject<List<Statistics>> getMostViewedGameByYearStats(){ return MONGO.getMostViewedGameByYearStats(); }
		
	//  Number of games released year by year
	public StatusObject<HashMap<Integer,Integer>>  getReleasedGameCountByYearStats(){ return MONGO.getReleasedGameCountByYearStats(); }
	
	//  Number of games released year by year and grouped by genres
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>> getReleasedGameCountByYearAndGenStats(){ return MONGO.getReleasedGameCountByYearAndGenStats(); }
	
	//  Most viewed games for each generes
	public StatusObject<HashMap<String,Statistics>>  getMostViewedGameByGenStats(){ return MONGO.getMostViewedGameByGenStats();	}
		
	//  Most liked games for each generes
	public StatusObject<HashMap<String,Statistics>> getMostLikedGamesByGenStats(){ return MONGO.getMostLikedGamesByGenStats(); }
			
	//  Percentage of the total games used by each generes(games of the generes/total games)
	public StatusObject<HashMap<String,Double>> getGeneresGameCountStats(){ return MONGO.getGeneresGameCountStats(); }
		
	//  Percentage of the total views give for each generes(sum of the viewsCount of the generes/total views)
	public StatusObject<HashMap<String,Double>> getGeneresMostViewedCountStats(){ return MONGO.getGeneresMostViewedCountStats(); }
		
	//  Percentage of games add to favourites for each generes(sum of the favouritesCount of the generes/total favouritesCount)
	public StatusObject<HashMap<String,Double>> getGeneresPreferencesStats(){ return MONGO.getGeneresPreferencesStats(); }
	
	
	//  DATASCRAPER FUNCTIONS
	
	public boolean updateDatabase() { return false; }
	
	public static String getTwitchURLChannel( String GAME ) { return null; }
	
	public String getGameDescription( int GAME_ID) { return null; }
	
	//OTHER
	
	public void closeConnection(){
		MONGO.closeConnection();
	}
}
