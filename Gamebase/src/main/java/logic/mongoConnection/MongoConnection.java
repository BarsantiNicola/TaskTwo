package logic.mongoConnection;

import java.util.List;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import logic.StatusCode;
import logic.data.StatusObject;
import logic.data.Game;
import logic.data.PreviewGame;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Filters.*;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConnection {
    
	private MongoClient mongoClient;    
    private MongoCollection<Game> gamesCollection; 
    public final MongoStatistics statistics;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               COSTRUCTORS                                                      //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public MongoConnection( String ipAddr , int port ) throws UnknownHostException,Exception{
    	
    	LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    	Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
    	rootLogger.setLevel(Level.OFF);

    	if( addressVerify(ipAddr,port) != StatusCode.OK )
    		throw new UnknownHostException();
    	else{
    		
        	ServerAddress server = new ServerAddress( ipAddr , port );
    		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    		mongoClient = new MongoClient( server , MongoClientOptions.builder().readConcern(ReadConcern.LOCAL).readPreference(ReadPreference.nearest()).writeConcern(WriteConcern.W1).codecRegistry(pojoCodecRegistry).build());
    		
    		gamesCollection = mongoClient.getDatabase("myDb").getCollection("games",Game.class); 
    		
    		statistics = new MongoStatistics( this , mongoClient );
    		
    	}
    
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               FUNCTIONS                                                        //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public StatusObject<Game> getGame( int gameId ) {

		if( gameId<0){
			
			System.out.println( "---> [MongoConnector][GetGame] Error, bad game id" );
			return new StatusObject<Game>( StatusCode.ERR_DOCUMENT_BAD_GAME_ID, null );
			
		}
		
    	try {
    		
    		Bson filter = eq( "_id", gameId );
    		Game game = gamesCollection.find( filter ).first();
			
    		if( game == null ) {
    			
    			//System.out.println( "---> [MongoConnector][GetGame] Error, game not found" );
    			return new StatusObject<Game>( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND, null );
    			
    		}

    		return new StatusObject<Game>( StatusCode.OK, game );
    		
    	}catch( Exception e ) {
    		
    		e.printStackTrace();
    		System.out.println( "---> [MongoConnector][GetGame] Error, Connection Lost" );
    		return new StatusObject<Game>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
    	}
    }
    
    public StatusCode addGame( Game game ) {
    	
    	if( game.getId() == null ) {
    		System.out.println( "---> [MongoConnector][GetGame] Error, Game must have an id" );
    		return StatusCode.ERR_DOCUMENT_BAD_GAME_ID;	
    	}
    		
    	try {
    		
    		if(gamesCollection.countDocuments(eq("_id",game.getId()))>0) {
        		System.out.println( "---> [MongoConnector][GetGame] Error, There is already a game with that id" );
        		return StatusCode.ERR_DOCUMENT_BAD_GAME_ID;	
    		}
    		
    		gamesCollection.insertOne(game);
    		return StatusCode.OK;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGame] Error, Connection Lost" );
    		return StatusCode.ERR_NETWORK_UNREACHABLE;
    		
    	}
    }
    
    public StatusCode incrementGameViews( int gameId ) {
		
		if( gameId<0 ){
			
			System.out.println( "---> [MongoConnector][IncrementGameViews] Error, bad game id" );
			return StatusCode.ERR_DOCUMENT_BAD_GAME_ID;
			
		}
		
    	Bson filter = eq( "_id", gameId );
    	Bson update = Updates.inc( "viewsCount", 1 );
    	
    	try {
    		
    		UpdateResult res = gamesCollection.updateOne( filter, update );
    		
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][IncrementGameViews] Error, game not found" );
    			return StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND;
    			
    		}

    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][IncrementGameViews] Error, game found but not modified" );
    			return StatusCode.ERR_DOCUMENT_UNKNOWN;
    			
    		}

    		return StatusCode.OK;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][IncrementGameViews] Error, network unreachable" );
    		return StatusCode.ERR_NETWORK_UNREACHABLE;
    		
    	}
    	
    }
    
    public StatusCode addGameDescription( int gameId , String description ) {
    	
		if( gameId<0 ){
			
			System.out.println( "---> [MongoConnector][AddGameDescription] Error, bad game id" );
			return StatusCode.ERR_DOCUMENT_BAD_GAME_ID;
			
		}
		
    	try {

    		Bson filter = eq( "_id", gameId );
    		Bson update = Updates.set( "description", description );
			
    		UpdateResult res = gamesCollection.updateOne( filter , update );
    	
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][AddGameDescription] Error, game not found" );
    			return StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND;
    			
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][AddGameDescription] Error, game not modified, the game has already a description" );
    			return StatusCode.ERR_DOCUMENT_UNKNOWN;
    			
    		}
    		
    		return StatusCode.OK;
    		
    	}catch( Exception e ){
    		
    		System.out.println( "---> [MongoConnector][AddGameDescription] Error, Connection Lost" );
    		return StatusCode.ERR_NETWORK_UNREACHABLE;
    		
    	}
    	
    }
    
    public StatusObject<Integer> deleteGame( String gameId ) {
    	
    	gameId = gameId + ".*";
    	Bson findFilter = regex("title", gameId , "i");
		Bson projection = Projections.include( "_id");		
		Game game;
		
    	try {
    		
    		game = gamesCollection.find(findFilter).projection(projection).first();

    		if( game == null || game.getId() == null)
    			return new StatusObject<Integer>(StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND,null);

    		Bson deleteFilter = eq( "_id" , game.getId() );
    		DeleteResult res = gamesCollection.deleteOne( deleteFilter );
    		
    		if( res.getDeletedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][DeleteGame] Error, game not found" );
    			return new StatusObject<Integer>(StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND,null);
    			
    		}
    		
    		return new StatusObject<Integer>(StatusCode.OK,game.getId());
    		
    	}catch(Exception e) {
    		
    		System.out.println( "---> [MongoConnector][DeleteGame] Error, Connection Lost" );
    		return new StatusObject<Integer>(StatusCode.ERR_NETWORK_UNREACHABLE,null);
    		
    	}
    }
    
    public StatusCode voteGame( int gameId , int previousVote,  int vote ) {
    	
		if( gameId < 0 ){
			
			System.out.println( "---> [MongoConnector][VoteGame] Error, bad game id" );
			return StatusCode.ERR_DOCUMENT_BAD_GAME_ID;
			
		}
		
   		if(( vote < 0 || vote > 5 ) || ( previousVote <-1 || previousVote > 5 )){
			
			System.out.println( "---> [MongoConnector][VoteGame] Error, the vote must be in [0,5]" );
			return StatusCode.ERR_DOCUMENT_INVALID_VOTE;
			
		}
   		
    	try {

    		Game game = getGame( gameId ).element;
    		
    		if( game == null ) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not found" );
    			return StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND;
    			
    		}
    		
    		Bson filter = eq( "_id" , gameId );  		
    		Double setVote;
    		Bson val;
    		
    		if( previousVote == -1 ){
    			
    			setVote = Double.parseDouble( new DecimalFormat( "0.0000" ).format(( game.getRating()*game.getRatingCount() + vote )/( game.getRatingCount()+1 )).replace( "," , "." ));
    			val = new Document("ratingCount", game.getRatingCount()+1).append("rating", setVote);      

    		}else {
    			setVote = Double.parseDouble( new DecimalFormat( "0.0000" ).format(( game.getRating()*game.getRatingCount() -previousVote+ vote )/( game.getRatingCount())).replace( "," , "." ));
    			val = new Document("rating", setVote);      

    		}
    		
    		UpdateResult res = gamesCollection.updateOne( filter , new Document("$set", val));
    	
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not found" );
    			return StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND;
    			
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not modified, the game has already a description" );
    			return StatusCode.ERR_DOCUMENT_UNKNOWN;
    			
    		}
    		
    		return StatusCode.OK;
    		
    	}catch(Exception e) {
    		
    		System.out.println("---> [MongoConnector][VotoGame] Error, Connection Lost");
    		return StatusCode.ERR_NETWORK_UNREACHABLE;
    		
    	}
    	
    }
    
    public StatusObject<Long> getTotalGamesCount() {
    	
    	try {

    		Long value = gamesCollection.countDocuments();
    		return new StatusObject<Long>( StatusCode.OK , value );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetTotalGamesCount] Error, connection lost" );
    		return new StatusObject<Long>( StatusCode.ERR_NETWORK_UNREACHABLE , null );
    	}
    	
    }
    
    public StatusObject<List<String>> getGenres(){
    	
    	List<String> retList = new ArrayList<>();
    	
    	try {
    		
    		MongoCursor<String> genres = gamesCollection.distinct( "genres" , String.class ).iterator();
    		
        	while( genres.hasNext() )
        		retList.add( genres.next() );
        	
        	return new StatusObject<List<String>>( StatusCode.OK , retList );
        	
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGenres] Error, connection lost" );
    		return new StatusObject<List<String>>( StatusCode.ERR_NETWORK_PARTIAL_UNREACHABLE, retList );
    		
    	}

    }

    public StatusCode closeConnection() {
    	
    	try{ 
    		
    		this.mongoClient.close();
    		return StatusCode.OK;
    	}catch( Exception e ) {

    		System.out.println( "---> [MongoConnector][CloseConnection] Unknown error during the close of the connection" );
    		return StatusCode.ERR_DOCUMENT_UNKNOWN;
    		
    	}
    	
    }
    
    public StatusObject<PreviewGame> getMostViewedPreview(){
    	
    	try {
    		
    		Bson sort = Sorts.descending( "viewsCount" );
    		Bson projection = Projections.include( "_id", "title", "background_image" );
    		
    		Game game = gamesCollection.find().sort(sort).projection(projection).first();
    		
    		if( game == null ) {
    			
        		System.out.println( "---> [MongoConnector][GetMostViewedPrevidew] Error, Game not found" );
    			return new StatusObject<PreviewGame>( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND, null );
    			
    		}else
    			return new StatusObject<PreviewGame>( StatusCode.OK, game.generatePreview());
    		
    	}catch( Exception e) {
    		
    		System.out.println( "---> [MongoConnector][GetMostViewedPrevidew] Error, Connection Lost" );
			return new StatusObject<PreviewGame>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
    	}
    	
    }
    
    public StatusObject<PreviewGame> getMostPopularPreview(){
    	
    	try {
    		
    		Bson sort = Sorts.descending( "rating" );
    		Bson projection = Projections.include( "_id", "title", "background_image" );
    		
    		Game game = gamesCollection.find().sort(sort).projection(projection).first();
    		
    		if( game == null ) {
    			
        		System.out.println( "---> [MongoConnector][GetMostPopularPreview] Error, Game not found" );
    			return new StatusObject<PreviewGame>( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND, null );
    			
    		}else
    			return new StatusObject<PreviewGame>( StatusCode.OK, game.generatePreview());
    		
    	}catch( Exception e) {
    		
    		System.out.println( "---> [MongoConnector][GetMostPopularPreview] Error, Connection Lost" );
			return new StatusObject<PreviewGame>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
    	}
	
    }
    
    public StatusObject<DataNavigator> getMostViewedPreviews( int nGames ){
    	
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , nGames , NavType.PREVIEW_VIEWED , "" ));
    	
    }
    
    public StatusObject<DataNavigator> getMostLikedPreviews( int nGames ){

    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , nGames , NavType.PREVIEW_LIKED , "" ));
    	
    }
    
    public StatusObject<DataNavigator> getMostRecentPreviews( int nGames ){
    		
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , nGames , NavType.PREVIEW_RECENT , "" ));
    	
    }
    
    public StatusObject<DataNavigator> searchGames(  int nGames , String search ){

    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , nGames , NavType.PREVIEW_SEARCH , search ));
    	
    }
    
    public StatusObject<DataNavigator> getGamesByGenre( int nGames , String genre ){
    	
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection, nGames , NavType.PREVIEW_GENRE , genre ));
    }
    
   
	private StatusCode addressVerify( String ipAddr , int port ) {
		
		int[] addr = new int[4];
		String[] values;
		char[] whiteList = {'0','1','2','3','4','5','6','7','8','9','.'};
		
		if( port != 27018 && port != 27017 ) {
			System.out.println("---> [MongoConnection][AddressVerify] Error, wrong port" );
			return StatusCode.ERR_WRONG_PORT;
		}

		if( ipAddr.length()>15 || ipAddr.length()<7) {
				System.out.println("---> [MongoConnection][AddressVerify] Error, invalid IP address" );
				return StatusCode.ERR_WRONG_IP_ADDR;
		}	
		for( int a = 0; a<ipAddr.length(); a++ ) 	
			for( int b = 0; b<whiteList.length; b++ ) {
				if(ipAddr.charAt(a) == whiteList[b] )
					break;
				if( b == whiteList.length-1){
					System.out.println("---> [MongoConnection][AddressVerify] Error, invalid IP address" );
					return StatusCode.ERR_WRONG_IP_ADDR;
				}
			}
		values = ipAddr.split("\\.");
		
		if( values.length < 4 ){
			System.out.println("---> [MongoConnection][AddressVerify] Error, invalid IP address" );
			return StatusCode.ERR_WRONG_IP_ADDR;
		}	

		for( int a = 0; a<4;a++ ) {
			addr[a] = Integer.parseInt(values[a]);
			if( addr[a] > 255 || addr[a] < 0) {
				System.out.println("---> [MongoConnection][AddressVerify] Error, invalid IP address" );
				return StatusCode.ERR_WRONG_IP_ADDR;
			}
		}
		return StatusCode.OK;

	}
		
    public static void main(String[] args) throws InterruptedException {

    	try {

    		MongoConnection client = new MongoConnection("172.16.0.80",27018);
<<<<<<< HEAD
    		client.incrementGameViews(3328);
    		client.voteGame(3328, 5, 4);
=======
    		DataNavigator games = client.getGamesByGenre(12, "Shooter").element;

    		List<PreviewGame> list;
    		if( games != null ) 
    			for(int a = 0; a<10;a++) {
    				list = games.getNextData().element;
    				if( list != null )
    					for( PreviewGame game : list )
    						System.out.println(game.getTitle());
    			}
    			
>>>>>>> adf44c1a316c5ed502b4d9bf82207659e1f573c8
    		client.closeConnection();
    		
    	}catch(Exception e) {
    		
    		System.out.println("Errore");
    		e.printStackTrace();
    	
    	}
    	
    	
    }        	
    
    public static boolean acidTest() {
    	
       	System.out.println( "--- Testing function for MongoDB connector ---" );
    	System.out.println("------  [Mongo Connection test]  ------");
    	
    	System.out.println("----> [TEST] MongoConnection Costructor" );
    	
    	String[] ipAddr = { "127.0.0.1" , "172.16.0.80","305.0.0.0","0..2.0","5,2:3,4","-1.0.0.0","0.0.0.0","255.255.255.255","","50.50.256.50","0.0.0.1000"};
    	int[] ports = { 27017,27018,80};
    	MongoConnection client;

    	
    	boolean[] cResults = { true,true,false,false,false,false,true,true,false,false,false};
    	for( int a = 0; a<ports.length; a++ )
    		for( int b = 0; b<ipAddr.length; b++ )
    			try {
    				System.out.println("--->[TEST][MongoConnection] Testing ADDR: " + ipAddr[b] +":" + ports[a]);
    				client = new MongoConnection(ipAddr[b],ports[a]);
    				client.closeConnection();
    				if( a==3 || cResults[b] == false ) {
    					System.out.println("--->[TEST][MongoConnection] Error during the test on IPADDR: " + ipAddr[a] + " PORT: " + ports[b] + " RESULT: true EXPECTED RESULT: false" );
    					return false;
    				}
    				
    			}catch( Exception e ) {
    				
    				if( a<2 && cResults[b] == true ){
    					System.out.println("--->[TEST][MongoConnection] Error during the test on IPADDR: " + ipAddr[a] + " PORT: " + ports[b] + " RESULT: false EXPECTED RESULT: true" );
    					return false;
    				}
    			}
    	
    	System.out.println("--->[TEST][MongoConnection] Test Correctly Executed");
    	try {
    		client = new MongoConnection( "172.16.0.80" , 27018 );
    	}catch(Exception e) {
    		return false;
    	}
    	
    	System.out.println("----> [TEST] MongoConnection.getGame()" );
    	int[] games = { 10,100,1000,10000,-1,31,54,97,600000,1};
    	boolean[] gResults = {true,true,true,true,false,true,true,true,false,true};
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	StatusObject<Game> code;
    	for( int a= 0; a<games.length;a++) {
    		code = client.getGame(games[a]);
    		if( code.statusCode == StatusCode.OK && gResults[a] == false ) {
    			System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT: true EXPECTED RESULT: false" );
    			return false;
    		}
    		if( code.statusCode == StatusCode.OK && code.statusCode == null ) {
    			System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT: false EXPECTED RESULT: true" );
    			return false;
    		}
    	}
    	code = client.getGame(1);
    	System.out.println("--->[TEST][MongoConnection.getGame] GAME EXAMPLE:\n" + gson.toJson(code.statusCode) );
    	      	
    	System.out.println("--->[TEST][MongoConnection.getGame] Test Correctly Executed");        	
    	
    	System.out.println("----> [TEST] MongoConnection.incrementGameViews()" );
    	Game game;
    	StatusCode rCode;
    	for( int a= 0; a<games.length;a++) {
    		game = client.getGame(games[a]).element;
    		rCode = client.incrementGameViews(games[a]);
    		if( rCode == StatusCode.OK && gResults[a] == false ){
    			System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT: true EXPECTED RESULT: false" );
    			return false;
    		}
    		if( rCode != StatusCode.OK && gResults[a] == true ){
    			System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT: true EXPECTED RESULT: false" );
    			return false;
    		}
    		if( game != null )
    			if( game.getViewsCount() != (client.getGame(games[a]).element.getViewsCount()-1)){
    				System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT NOT UPDATED" );
    				return false;
    			}

    	}
    	System.out.println("----> [TEST][MongoConnection.incrementGameViews] Test Correctly Executed" );
    	return true;
    	
    }
    
    @SuppressWarnings({ "unused"})
	private void updateViewsCount() {
    	
    	MongoCursor<Game> games = this.gamesCollection.find().noCursorTimeout(true).iterator();
    	Game next;
    	Bson filter;
    	Bson update;
    	ExponentialDistribution exp = new ExponentialDistribution(500);
    	int counter = 0;
    	int innerCounter = 0;
    	int jumpCounter = 47026;
    	while(games.hasNext()) {
    		try {
    		next = games.next();
    		if(jumpCounter >0) {
    			jumpCounter--;
    			innerCounter++;
    			continue;
    		}
    			filter = eq( "_id", next.getId() ); 
    		update = Updates.set("viewsCount" , Math.abs(new Double(Math.floor(exp.sample())).intValue()));
    		System.out.println(next.getTitle());
    		gamesCollection.updateOne(filter,update );
    		System.out.println(++innerCounter);
    		}catch(Exception e ) {
    			e.printStackTrace();
    			counter++;
    		}
    	}
    	System.out.println("ERRORE SU: " + counter);
    }
    
    public StatusObject<Integer> getMaxGameId(){
    	try {
    		Bson sort = Sorts.descending( "_id" );
    		Game game =  gamesCollection.find().sort(sort).first();
    		if( game != null ) {
    			Integer maxId = game.getId();
        		if( maxId != null ) return new StatusObject<Integer>(StatusCode.OK, maxId);
        		else return new StatusObject<Integer>(StatusCode.ERR_DOCUMENT_UNKNOWN,null);
    		}else 
    			return new StatusObject<Integer>(StatusCode.ERR_DOCUMENT_UNKNOWN,null);

    	}catch(Exception e ){
    		System.out.println( "---> [MongoConnector][GetMaxId] Error, Connection Lost" );
    		e.printStackTrace();
    		return new StatusObject<Integer>(StatusCode.ERR_NETWORK_UNREACHABLE);
    	}
    }
    
    public void riccardoRequest(String file) {
		Bson projection = Projections.include( "_id", "title", "background_image" );
    	MongoCursor<Game> games = this.gamesCollection.find().projection(projection).noCursorTimeout(true).iterator();
    	int counter = 0;
    	PreviewGame game;
    	try {
    		PrintWriter out = new PrintWriter(new FileOutputStream(file),true);
    		while(games.hasNext()) {
    			game = games.next().generatePreview();
    			counter++;
    			
    			if(game.getPreviewPicURL() != null )
    				out.println(game.getId() + "ç" + game.getTitle() + "ç" + game.getPreviewPicURL() );
    			else
    				out.println(game.getId()+ "ç" + game.getTitle() + "ç");
    			
    			if( counter % 1000 == 0 )
    				System.out.println(counter + " games exported");
    		}
    		out.close();
    	}catch(IOException e ) {
    		e.printStackTrace();
    		return;
    	}
    }
    
    public StatusCode hasDescription( int _id ) {
    	
    	StatusObject<Game> game =  getGame( _id );
    	
    	if( game.element == null )
    		return game.statusCode;
    	
    	if( (game.element.getDescription() == null))
    		return StatusCode.ERR_DOCUMENT_NO_DESCR;
    	
    	return StatusCode.OK;

    }
    
}
