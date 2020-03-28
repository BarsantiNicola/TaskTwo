package logic;

import java.util.HashMap;
import java.util.List;

import logic.data.*;
import logic.graphConnector.*;
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
	
	private MongoConnection MONGO;
	private GraphConnector graph;
	
	public LogicBridge()
	 {
		 try 
		  {
			 // ricordare di avere la VPN attiva
			  MONGO = new MongoConnection("172.16.0.80",27028);					 
			  graph.connect("bolt://172.16.0.78:7687","neo4j","password");       

			 }
		 catch( Exception e )
		  {
		   System.out.println("-->[LogicBridge] Fatal Error, Invalid NET configuration");
			  System.exit(1);
			 }
	 }

	
	/* Graph Functions moved in the appropriate GraphInterface.java (logic.graphConnector package) */
	
	
	///////////////  DOCUMENT FUNCTIONS
	//////  STATISTICS(Description of each statistic usage could be found in MongoDb.docx)
	//////  USER INTERFACE
	
	public StatusObject<Game> getGame( int gameId ){ return MONGO.getGame( gameId );}
	
	public StatusCode updateGameViews( int gameId ){return MONGO.incrementGameViews(gameId);}
	
	public StatusCode voteGame( int gameId , int vote ) { return MONGO.voteGame( gameId, vote );}
	
	public StatusObject<Long> getGameCount() { return MONGO.getTotalGamesCount(); }
	
	public StatusObject<List<String>> getGenres(){ return MONGO.getGenres(); }
	
	public StatusObject<PreviewGame> getMostViewedGame() { return MONGO.getMostViewedPreview(); }
	
	public StatusObject<PreviewGame> getMostPopularGame() { return MONGO.getMostPopularPreview(); }
	
	public StatusObject<DataNavigator> getMostViewedGames(){ return MONGO.getMostViewedPreviews(); }
	
	public StatusObject<DataNavigator> getMostLikedGames(){ return MONGO.getMostLikedPreviews(); }
	
	public StatusObject<DataNavigator> getMostRecentGames(){ return MONGO.getMostRecentPreviews(); }
	
	public StatusObject<DataNavigator> searchGames( String searchedString ){ return MONGO.searchGames( searchedString ); }
	
	//////  SCRAPER & ADMIN INTERFACE
	StatusCode addGameDescription( int gameId , String description ) { return MONGO.addGameDescription( gameId , description);}
	
	public StatusCode addGame( Game game ) {return MONGO.addGame(game);}
	
	public StatusCode deleteGame( int gameId ) { return MONGO.deleteGame( gameId );}
	
	
	//////  STATISTICS(Description of each statistic usage could be found in Statistics.docx)
	
	public StatusObject<List<Statistics>> getMaxRatedGameByYear(){ return MONGO.statistics.getMaxRatedGameByYear(); }
		
	public StatusObject<List<Statistics>> getMaxViewedGameByYear(){ return MONGO.statistics.getMaxViewedGameByYear(); }
		
	public StatusObject<HashMap<Integer,Integer>>  getReleasedGameCountByYearStats(){ return MONGO.statistics.getGamesCountByYear(); }
	
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>> getReleasedGameCountByYearAndGenStats(){ return MONGO.statistics.getGamesCountByYearGen(); }
	
	public StatusObject<HashMap<String,Statistics>> getMaxViewedGameByGen(){ return MONGO.statistics.getMaxViewedGameByGen();	}
		
	public StatusObject<HashMap<String,Statistics>> getMaxRatedGamesByGen(){ return MONGO.statistics.getMaxRatedGameByGen(); }
			
	public StatusObject<HashMap<String,Integer>>    getGamesCountByGen(){ return MONGO.statistics.getGamesCountByGen(); }
		
	public StatusObject<HashMap<Integer,Integer>>   getGamesCountByYear(){ return MONGO.statistics.getGamesCountByYear(); }
		
	public StatusObject<HashMap<String,Integer>>    getViewsCountByGen(){ return MONGO.statistics.getViewsCountByGen(); }
	
	public StatusObject<HashMap<Integer,Integer>>   getViewsCountByYear(){ return MONGO.statistics.getViewsCountByYear(); }
	
	public StatusObject<HashMap<String,Integer>>    getRatingsCountByGen(){ return MONGO.statistics.getRatingsCountByGen(); }
	
	public StatusObject<HashMap<Integer,Integer>>   getRatingsCountByYear(){ return MONGO.statistics.getRatingsCountByYear(); }
	
	
	///////////////  DATASCRAPER FUNCTIONS
	
	public boolean updateDatabase() { return false; }
	
	public static String getTwitchURLChannel( String GAME ) { return null; }
	
	public String getGameDescription( int GAME_ID) { return null; }
	
	
	///////////////  OTHER
	
	public void closeConnection(){
		MONGO.closeConnection();
		graph.close();                 
	}
}
