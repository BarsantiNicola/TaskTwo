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
	
	MongoConnection MONGO;
	GraphConnector graph;
	
	public LogicBridge()
	 {
		 try 
		  {
			  MONGO = new MongoConnection("172.16.0.80",27028);
			  graph.connect("bolt://172.16.0.78:7687","neo4j","password");       //Connessione a Neo4j (ricordare di avere la VPN attiva)
			 }
		 catch( Exception e )
		  {
		   System.out.println("-->[LogicBridge] Fatal Error, Invalid NET configuration");
			  System.exit(1);
			 }
	 }

	
	/* Graph Functions moved in the appropriate GraphInterface.java (logic.graphConnector package) */
	
	
	//////  DOCUMENT FUNCTIONS
	
	public StatusObject<Game> getGame( int gameId ){ 
		return MONGO.getGame( gameId );
	}
	
	public StatusCode updateGameViews( int gameId ){
		return MONGO.incrementGameViews(gameId);
	}
	
	private StatusCode addGameDescription( int gameId , String description ) { 
		return MONGO.addGameDescription( gameId , description);
	}
	
	public StatusCode deleteGame( int gameId ) { 
		return MONGO.deleteGame( gameId );
	}
	
	public StatusCode voteGame( int gameId , int vote ) { 
		return MONGO.voteGame( gameId, vote );
	}
	
	public StatusObject<Long> getGameCount() { return MONGO.getTotalGamesCount(); }
	
	public StatusObject<List<String>> getGenres(){ return MONGO.getGenres(); }
	
	public StatusObject<PreviewGame> getMostViewedGame() { return MONGO.getMostViewedPreview(); }
	
	public StatusObject<PreviewGame> getMostPopularGame() { return MONGO.getMostPopularPreview(); }
	
	public StatusObject<DataNavigator> getMostViewedGames(){ return MONGO.getMostViewedPreviews(); }
	
	public StatusObject<DataNavigator> getMostLikedGames(){ return MONGO.getMostLikedPreviews(); }
	
	public StatusObject<DataNavigator> getMostRecentGames(){ return MONGO.getMostRecentPreviews(); }
	
	public StatusObject<DataNavigator> searchGames( String searchedString ){ return MONGO.searchGames( searchedString ); }
	
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
		graph.close();                 //Chiude la connessione a Neo4j
	}
}
