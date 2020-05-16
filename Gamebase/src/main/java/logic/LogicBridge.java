package logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;

import logic.data.*;
import logic.graphConnector.*;
import logic.mongoConnection.DataNavigator;
import logic.mongoConnection.MongoConnection;
import scraping.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
//  The class is used to bridge the graphic interface to the program logic.
//  The class defines a simple API by which the upper layers could operate on the data
//  without any knowledge of the management the data will get. The class is designed to
//  work with the Neo4j database, the MongoDB document database and a webscraper API. 
//  However due to it's simplicity any other type of connection or data management could be added
//  changing the function in charge
///////////////////////////////////////////////////////////////////////////////////////////////////

public class LogicBridge {
	
	private MongoConnection MONGO;
	private GraphConnector GRAPH;
	private ImgCache CACHE;
	
	public LogicBridge()
	 {	
		// ricordare di avere la VPN attiva
		
		GRAPH = new GraphConnector();
		StatusCode graphConnection = GRAPH.connect("bolt://172.16.0.78:7687","neo4j","password");
		
		if( graphConnection != StatusCode.OK ) {
			
			System.out.println("-->[LogicBridge] Fatal Error, error in graph database connection");
			System.exit(1);
		}
		
		try {
			 
			MONGO = new MongoConnection("172.16.0.80",27018);					 
			CACHE = new ImgCache("cache");    

		} catch( Exception e ){
		   System.out.println("-->[LogicBridge] Fatal Error, Invalid NET configuration");
		   System.exit(1);
	    }
	 }

	
	/* Graph Functions moved in the appropriate GraphInterface.java (logic.graphConnector package) */
	
	
	///////////////  DOCUMENT FUNCTIONS
	//  Description of each function(errors,timing,description) could be found in MongoDb.docx
	//  Example of usage of the function could be found into MongoConnection.acidTest, MongoStatistics.statsTest, DataNavigator.navTest
	//////  USER INTERFACE
	
	public StatusObject<Game> getGame( int gameId ){ return MONGO.getGame( gameId );}
	
	public StatusCode incrementGameViews( int gameId ){return MONGO.incrementGameViews(gameId);}
	
	public StatusCode voteGame( int gameId , int vote ) { return MONGO.voteGame( gameId, vote );}
	
	public StatusObject<Long> getGameCount() { return MONGO.getTotalGamesCount(); }
	
	public StatusObject<List<String>> getGenres(){ return MONGO.getGenres(); }
	
	public StatusObject<PreviewGame> getMostViewedPreview() { return MONGO.getMostViewedPreview(); }
	
	public StatusObject<PreviewGame> getMostPopularPreview() { return MONGO.getMostPopularPreview(); }
	
	public StatusObject<DataNavigator> getMostViewedPreviews(){ return MONGO.getMostViewedPreviews(12); }
	
	public StatusObject<DataNavigator> getMostLikedPreviews(){ return MONGO.getMostLikedPreviews(12); }
	
	public StatusObject<DataNavigator> getMostRecentPreviews(){ return MONGO.getMostRecentPreviews(12); }
	
	public StatusObject<DataNavigator> searchGamesPreviews( String searchedString ){ return MONGO.searchGames( 12,searchedString ); }
	
	//////  SCRAPER & ADMIN INTERFACE
	public StatusCode addGameDescription( int gameId , String description ) { return MONGO.addGameDescription( gameId , description);}
	
	public StatusCode addGame( Game game ) {return MONGO.addGame(game);}
	
	public StatusObject<Integer> deleteGameMongo( String gameTitle ) { return MONGO.deleteGame( gameTitle );}
	

	//////  STATISTICS
	
	public StatusObject<List<Statistics>> getMaxRatedGameByYear(){ return MONGO.statistics.getMaxRatedGameByYear(); }
		
	public StatusObject<List<Statistics>> getMaxViewedGameByYear(){ return MONGO.statistics.getMaxViewedGameByYear(); }
	
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>> getGamesCountByYearGen(){ return MONGO.statistics.getGamesCountByYearGen(); }
	
	public StatusObject<HashMap<String,Statistics>> getMaxViewedGameByGen(){ return MONGO.statistics.getMaxViewedGameByGen();	}
		
	public StatusObject<HashMap<String,Statistics>> getMaxRatedGamesByGen(){ return MONGO.statistics.getMaxRatedGameByGen(); }
			
	public StatusObject<HashMap<String,Integer>>    getGamesCountByGen(){ return MONGO.statistics.getGamesCountByGen(); }
		
	public StatusObject<HashMap<Integer,Integer>>   getGamesCountByYear(){ return MONGO.statistics.getGamesCountByYear(); }
		
	public StatusObject<HashMap<String,Integer>>    getViewsCountByGen(){ return MONGO.statistics.getViewsCountByGen(); }
	
	public StatusObject<HashMap<Integer,Integer>>   getViewsCountByYear(){ return MONGO.statistics.getViewsCountByYear(); }
	
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>>   getViewsCountByYearGen(){ return MONGO.statistics.getViewsCountByYearGen(); }
	
	public StatusObject<HashMap<String,Integer>>    getRatingsCountByGen(){ return MONGO.statistics.getRatingsCountByGen(); }
	
	public StatusObject<HashMap<Integer,Integer>>   getRatingsCountByYear(){ return MONGO.statistics.getRatingsCountByYear(); }
	
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>>   getRatingsCountByYearGen(){ return MONGO.statistics.getRatingsCountByYearGen(); }
	
	
	///////////////  DATASCRAPER FUNCTIONS
	
	public boolean updateDatabase() { 
		int MaxGameId= MONGO.getMaxGameId().element;
		List<Game> gamesToAdd = WebScraping.scrapeNewGames(MaxGameId + 1); 
		
		if (gamesToAdd.isEmpty()){
			System.out.println("-->[LogicBridge][updateDatabase] List gamesToAdd is empty. Returning false.");
			return false;
		}
		for(int i= 0; i < gamesToAdd.size(); i++) {
			GraphGame graphGameToAdd = util.initializeGraphGameToAdd(gamesToAdd.get(i));
			if(GRAPH.addGame(graphGameToAdd)!=StatusCode.OK) {
				System.out.println("-->[LogicBridge][updateDatabase] Failing in adding game" + graphGameToAdd._id + " : " + graphGameToAdd.title + " to Graph database. Interrupting update");
				util.recapUpdate(gamesToAdd, i);
				util.writeErrorLog("Failing in adding game" + graphGameToAdd._id + " : " + graphGameToAdd.title + " to Graph database. Interrupting update.");
				return true;
			}
			if(MONGO.addGame(gamesToAdd.get(i)) != StatusCode.OK) {
				System.out.println("-->[LogicBridge][updateDatabase] Failing in adding game" + gamesToAdd.get(i).getId() + " : " + gamesToAdd.get(i).getTitle() + " to Document database. Interrupting update");
				util.writeErrorLog("Failing in adding game" + gamesToAdd.get(i).getId() + " : " + gamesToAdd.get(i).getTitle() + " to Document database. Interrupting update");
				if(GRAPH.deleteGame(graphGameToAdd._id) !=StatusCode.OK) {
					util.writeErrorLog("Failing in deleting game" + graphGameToAdd._id + " : " + graphGameToAdd.title + " from Graph database");
				}
				util.recapUpdate(gamesToAdd, i);
				return true;
			}
			System.out.println("-->[LogicBridge][updateDatabase] Added game:" + gamesToAdd.get(i).getTitle() + " to the database");
		}
		util.recapUpdate(gamesToAdd, gamesToAdd.size());
	return true;
	}
	
	public String getGameDescription( int GAME_ID) { return WebScraping.getGameDescription(GAME_ID); }
	
	///////////////  GRAPH FUNCTIONS
	
	public StatusObject<UserInfo> register(User user){ return GRAPH.register(user); }
	
	public StatusObject<UserInfo> login(String username, String password){ return GRAPH.login(username, password); }
	
	public StatusCode logout() { return GRAPH.logout(); }
	
	public User getUser() { return GRAPH.getUser(); }
	
	public UserType getUserType() { return GRAPH.getUserType(); }
	
	public StatusObject<Long> getTotalUsersCount(){ return GRAPH.getTotalUsersCount(); }
	
	public StatusObject<Long> getGamesCount(){ return GRAPH.getGamesCount(); }
	
	public StatusCode saveUser() { return GRAPH.saveUser(); }
	
	public StatusCode deleteUser(String username) { return GRAPH.deleteUser(username); }
	
	public StatusObject<UserInfo> upgradeToAnalyst(){ return GRAPH.upgradeToAnalyst(); }
	
	public StatusCode followUser(String username) { return GRAPH.followUser(username); }
	
	public StatusCode unFollowUser(String username) { return GRAPH.unFollowUser(username); }
	
	public StatusObject<List<User>> searchUsers(String mask){ return GRAPH.searchUsers(mask); }
	
	public StatusObject<List<User>> getSuggestedUsersList(){ return GRAPH.getSuggestedUsersList(); }
	
	public StatusObject<Boolean> doIFollow(String username){ return GRAPH.doIFollow(username); }
	
	public StatusObject<List<User>> getFollowedUsersList(){ return GRAPH.getFollowedUsersList(); }
	
	public StatusCode addToFavourites(String _id) { return GRAPH.addToFavourites(_id); }
	
	public StatusCode removeFromFavourites(String _id) { return GRAPH.removeFromFavourites(_id); }
	
	public StatusObject<List<GraphGame>> getFavouritesGamesList(String username){ return GRAPH.getFavouritesGamesList(username); }
	
	public StatusObject<Long> getGameFavouriteCount(String _id){ return GRAPH.getGameFavouriteCount(_id); }
	
	public StatusCode rateGame(String _id, int vote) { return GRAPH.rateGame(_id, vote); }
	
	public StatusObject<List<GraphGame>> getFeaturedGamesList(int max){ return GRAPH.getFeaturedGamesList(max); }
	
	public StatusObject<Boolean> doIFavourite(String _id){ return GRAPH.doIFavourite(_id); }
	
	public StatusObject<List<GraphGame>> getFavouritesGamesList(){ return GRAPH.getFavouritesGamesList(); }
	
	public StatusObject<List<User>> getMostFollowedUsers(int max){ return GRAPH.getMostFollowedUsers(max); }
	
	public StatusObject<List<GraphGame>> getMostFavouriteGames(int max){ return GRAPH.getMostFavouriteGames(max); }
	
	public StatusObject<List<UserStats>> getUsersSummaryStats(){ return GRAPH.getUsersSummaryStats(); }
	
	///////////////  CACHE
	
	public StatusCode cacheImg( String URL , ImageIcon img ) {
		return CACHE.cacheImg(URL, img );
	}
	
	public StatusObject<ImageIcon> getCachedImg( String URL ) {
		return CACHE.getCachedImg(URL);
	}
	
	////////////// DELETE GAME and CLOSE CONNECTION
	
	public boolean deleteGame( String gameTitle ) {
		
		StatusObject<Integer> mongoDeleteGameStatus = MONGO.deleteGame(gameTitle);
		
		if( mongoDeleteGameStatus.statusCode != StatusCode.OK ) {
			
			try {
				FileWriter fw = new FileWriter("logs/errors.txt",true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("DELETE GAME ERROR: " + gameTitle + " cannot be deleted on Mongo database. The game is still present on Graph database.");
				bw.newLine();
				bw.close();
			} catch( Exception e) {
				
				System.out.println("-->[LogicBridge] cannot delete " + gameTitle + " from Mongo database. Failed to write into errors.txt file.");
			}

			return false;
		}
		
		int id = mongoDeleteGameStatus.element;
		
		StatusCode graphDeleteGameStatus = GRAPH.deleteGame(Integer.toString(id));
		
		if( graphDeleteGameStatus != StatusCode.OK ) {
			
			try {
				FileWriter fw = new FileWriter("logs/errors.txt",true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("DELETE GAME ERROR: " + gameTitle + " cannot be deleted on Graph database. The game has already been cancelled from Mongo database.");
				bw.newLine();
				bw.close();
			} catch( Exception e) {
				
				System.out.println("-->[LogicBridge] cannot delete " + gameTitle + " from Graph database. Failed to write into errors.txt file.");
			}
			
			return false;
		}		
		
		return true;
	}
	
	public void closeConnection(){
		MONGO.closeConnection();
		GRAPH.close();
	}
	
	//Mongo Getter (for bruteforce description scraping)
	public MongoConnection getMONGO()
	 { return MONGO; }

  
}
