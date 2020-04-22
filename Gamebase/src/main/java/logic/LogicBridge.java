package logic;

import java.util.HashMap;
import java.util.List;

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
	private ImgCache CACHE;
	//private GraphConnector graph;
	
	public LogicBridge()
	 {
		 try 
		  {
			 // ricordare di avere la VPN attiva

			  MONGO = new MongoConnection("172.16.0.80",27018);					 
			  CACHE = new ImgCache("cache");
			  //graph.connect("bolt://172.16.0.78:7687","neo4j","password");       

			 }
		 catch( Exception e )
		  {
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
	
	public StatusObject<DataNavigator> getMostViewedPreviews(){ return MONGO.getMostViewedPreviews(); }
	
	public StatusObject<DataNavigator> getMostLikedPreviews(){ return MONGO.getMostLikedPreviews(); }
	
	public StatusObject<DataNavigator> getMostRecentPreviews(){ return MONGO.getMostRecentPreviews(); }
	
	public StatusObject<DataNavigator> searchGamesPreviews( String searchedString ){ return MONGO.searchGames( searchedString ); }
	
	//////  SCRAPER & ADMIN INTERFACE
	StatusCode addGameDescription( int gameId , String description ) { return MONGO.addGameDescription( gameId , description);}
	
	public StatusCode addGame( Game game ) {return MONGO.addGame(game);}
	
	public StatusCode deleteGame( String gameTitle ) { return MONGO.deleteGame( gameTitle );}
	
	
	
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
		return WebScraping.updateDatabase(MaxGameId); 
		
		}
	
	public static String getTwitchURLChannel( String GAME ) { return WebScraping.getTwitchURLChannel(GAME); }
	
	public String getGameDescription( int GAME_ID) { return WebScraping.getGameDescription(GAME_ID); }
	
	public String getGameLowerResScreenshot( String GAME) { return WebScraping.getGameLowerResScreenshot(GAME); }
	
	///////////////  OTHER
	
	public boolean cacheImg( String URL , ImageIcon img ) {
		return CACHE.cacheImg(URL, img );
	}
	
	public ImageIcon getCachedImg( String URL ) {
		return CACHE.getCachedImg(URL);
	}
	
	
	public void closeConnection(){
		MONGO.closeConnection();
		//graph.close();                 
	}
	
	public static void main(String[] args) {
		ImgCache prova = new ImgCache("cache");
		//ImageIcon img = new ImageIcon("https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg");
		//System.out.println(img.getIconHeight() + " : " + img.getIconWidth());
		//System.out.println(img.toString());
		//prova.cacheImg("https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg", img);
		ImageIcon img = prova.getCachedImg("https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg");
		System.out.println(img.toString());
		//System.out.println(img.getIconHeight() + " : " + img.getIconWidth());
	}
	
}
