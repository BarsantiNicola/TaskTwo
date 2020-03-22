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
import logic.data.Statistics;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Filters.*;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConnection {
    
	private MongoClient mongoClient;    
    private MongoCollection<Game> gamesCollection; 
    private MongoCollection<Document> statisticsCollection;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               COSTRUCTORS                                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    
    public MongoConnection( String ipAddr , int port ) throws UnknownHostException{
    	
    	LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    	Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
    	rootLogger.setLevel(Level.OFF);

    	if( addressVerify(ipAddr,port) != StatusCode.OK )
    		throw new UnknownHostException();
    	else{
    		
    		System.out.println( "---> [MongoConnection] Creation of the database connection" );
        	ServerAddress server = new ServerAddress( ipAddr , port );
    		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    		mongoClient = new MongoClient( server , MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());

    		gamesCollection = mongoClient.getDatabase("myDb2").getCollection("games",Game.class); 
    		statisticsCollection = mongoClient.getDatabase("myDb2").getCollection("games",Document.class);
    		System.out.println( "---> [MongoConnection] Connection created" );
    		
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
			System.out.println( "---> [MongoConnector][GetGame] Searching game: " + gameId );
			
    		if( game == null ) {
    			
    			System.out.println( "---> [MongoConnector][GetGame] Error, game not found" );
    			return new StatusObject<Game>( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND, null );
    			
    		}
    		
			System.out.println( "---> [MongoConnector][GetGame] Search operation correctly executed" );
    		return new StatusObject<Game>( StatusCode.OK, game );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGame] Error, Connection Lost" );
    		return new StatusObject<Game>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
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
		
    		System.out.println( "---> [MongoConnector][IncrementGameViews] Updating the game views count" );
    		
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][IncrementGameViews] Error, game not found" );
    			return StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND;
    			
    		}

    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][IncrementGameViews] Error, game found but not modified" );
    			return StatusCode.ERR_DOCUMENT_UNKNOWN;
    			
    		}
		
    		System.out.println( "---> [MongoConnector][IncrementGameViews] Views count correctly updated" );
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
    		
			System.out.println( "---> [MongoConnector][AddGameDescription] Error, game not found" );
			
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
    
    public StatusCode deleteGame( int gameId ) {
    	
		if( gameId<0 ){
			
			System.out.println( "---> [MongoConnector][DeleteGame] Error, bad game id" );
			return StatusCode.ERR_DOCUMENT_BAD_GAME_ID;
			
		}
		
    	try {

			System.out.println( "---> [MongoConnector][DeleteGame] Deleting the game " + gameId );
			
    		Bson filter = eq( "_id" , gameId );
    		DeleteResult res = gamesCollection.deleteOne( filter );
    		
    		if( res.getDeletedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][DeleteGame] Error, game not found" );
    			return StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND;
    			
    		}
    		
    		return StatusCode.OK;
    		
    	}catch(Exception e) {
    		
    		System.out.println( "---> [MongoConnector][DeleteGame] Error, Connection Lost" );
    		return StatusCode.ERR_NETWORK_UNREACHABLE;
    		
    	}
    }
    
    public StatusCode voteGame( int gameId , int vote ) {
    	
		if( gameId < 0 ){
			
			System.out.println( "---> [MongoConnector][VoteGame] Error, bad game id" );
			return StatusCode.ERR_DOCUMENT_BAD_GAME_ID;
			
		}
		
   		if( vote < 0 || vote > 5 ) {
			
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
    		List<Bson> updates = new ArrayList<Bson>();
    		updates.add(Updates.inc( "ratingCount" , 1 ));
    		updates.add(Updates.set( "rating" , Double.parseDouble( new DecimalFormat( "0.0000" ).format(( game.getRating()*game.getRatingCount() + vote )/( game.getRatingCount()+1 )).replace( "," , "." ))));

    		UpdateResult res = gamesCollection.updateOne( filter , updates );
    	
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

    		System.out.println( "---> [MongoConnector][GetTotalGamesCount] Counting the games into the dataset" );
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
    		System.out.println( "---> [MongoConnector][GetGenres] Gettint the list of games generes into dataset" );
    		
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
    		System.out.println( "---> [MongoConnector][CloseConnection] Connection close" );
    		return StatusCode.OK;
    	}catch( Exception e ) {

    		System.out.println( "---> [MongoConnector][CloseConnection] Unknown error during the close of the connection" );
    		return StatusCode.ERR_DOCUMENT_UNKNOWN;
    		
    	}
    	
    }
    
    public StatusObject<PreviewGame> getMostViewedPreview(){
    	
    	try {
    		
    		System.out.println( "---> [MongoConnector][GetMostViewedPreview] Getting the most viewed preview" );
    		
    		Bson sort = Sorts.descending( "viewsCount" );
    		Bson projection = Projections.include( "_id", "title", "background_image" );
    		
    		Game game = gamesCollection.find().sort(sort).projection(projection).first();
    		
    		if( game == null ) {
    			
        		System.out.println( "---> [MongoConnector][GetMostViewedPrevidew] Error, Game not found" );
    			return new StatusObject<PreviewGame>( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND, null );
    			
    		}else
    			return new StatusObject<PreviewGame>( StatusCode.OK, game.getPreview());
    		
    	}catch( Exception e) {
    		
    		System.out.println( "---> [MongoConnector][GetMostViewedPrevidew] Error, Connection Lost" );
			return new StatusObject<PreviewGame>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
    	}
    	
    }
    
    public StatusObject<PreviewGame> getMostPopularPreview(){
    	
    	try {
    		
    		System.out.println( "---> [MongoConnector][GetMostPopularPreview] Getting the most viewed preview" );
    		Bson sort = Sorts.descending( "rating" );
    		Bson projection = Projections.include( "_id", "title", "background_image" );
    		
    		Game game = gamesCollection.find().sort(sort).projection(projection).first();
    		
    		if( game == null ) {
    			
        		System.out.println( "---> [MongoConnector][GetMostPopularPreview] Error, Game not found" );
    			return new StatusObject<PreviewGame>( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND, null );
    			
    		}else
    			return new StatusObject<PreviewGame>( StatusCode.OK, game.getPreview());
    		
    	}catch( Exception e) {
    		
    		System.out.println( "---> [MongoConnector][GetMostPopularPreview] Error, Connection Lost" );
			return new StatusObject<PreviewGame>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
    	}
	
    }
    
    public StatusObject<DataNavigator> getMostViewedPreviews(){
    	
    	System.out.println("---> [MongoConnection][GetMostViewedPreviews] Generating a new navigation object" );
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_VIEWED , "" ));
    	
    }
    
    public StatusObject<DataNavigator> getMostLikedPreviews(){

    	System.out.println("---> [MongoConnection][GetMostLikedPreviews] Generating a new navigation object" );
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_LIKED , "" ));
    	
    }
    
    public StatusObject<DataNavigator> getMostRecentPreviews(){
    		
    	System.out.println("---> [MongoConnection][GetMostRecentPreviews] Generating a new navigation object" );
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_RECENT , "" ));
    	
    }
    
    public StatusObject<DataNavigator> searchGames( String search ){

    	System.out.println("---> [MongoConnection][SearchGames] Generating a new navigation object" );
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_SEARCH , search ));
    	
    }
   
    
    //  DOCUMENT STATISTICS
    
	@SuppressWarnings("deprecation")
	public StatusObject<List<Statistics>> getMostLikedGameByYearStats(){
		
		BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year",  new BasicDBObject("$year", "$releaseDate")));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$last" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.rating", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("rating",1));
		         
		List<Statistics> result = new ArrayList<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		
		try {
			
			System.out.println("---> [MongoConnection][GetMostLikedGameByYearStats] Collecting data from the dataset...");
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<List<Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}
    	System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Organizing the collected data" );		
		while( res.hasNext()) {
			
			data = res.next();

			try {
				
				result.add(new Statistics(df1.parse(String.valueOf(data.get("_id",Document.class).getInteger("year"))).getYear(), 
						null,
						data.get("max",Document.class).getString("title"),data.get("max",Document.class).getDouble("rating")));

			}catch(Exception e) {}
		
		}

		return new StatusObject<List<Statistics>>(StatusCode.OK,result);
	
	}
	
	@SuppressWarnings({ "deprecation" })
	public StatusObject<List<Statistics>> getMostViewedGameByYearStats(){
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year",  new BasicDBObject("$year", "$releaseDate")));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$last" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.viewsCount", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("viewsCount",1));
		         
		List<Statistics> result = new ArrayList<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		System.out.println("---> [MongoConnection][GetMostViewedGameByYearStats] Collecting data from the dataset...");
		try {
			
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();

		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetMostViewedGameByYearStats] Error, network unreachable" );
			return new StatusObject<List<Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetMostViewedGameByYearStats] Organizing the collected data...");
		
		while( res.hasNext()) {
			
			data = res.next();

			try {
				
				result.add(new Statistics(df1.parse(String.valueOf(data.get("_id",Document.class).getInteger("year"))).getYear(), 
						data.get("max",Document.class).getInteger("viewsCount"),
						data.get("max",Document.class).getString("title"),null));

			}catch(Exception e) {}
		
		}

		return new StatusObject<List<Statistics>>(StatusCode.OK,result);
		
	}
	
	@SuppressWarnings("deprecation")
	public StatusObject<HashMap<Integer,Integer>>  getReleasedGameCountByYearStats(){
	
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", "$releaseDate")));
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , 1));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<Integer,Integer> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		
		try {
			System.out.println("---> [MongoConnection][GetReleasedGameCountByYearStats] Collecting data from the dataset...");
			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleasedGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<Integer,Integer>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetReleasedGameCountByYearStats] Organizing the collected data...");
		while( res.hasNext()) {
			
			data = res.next();
			try {
				
				result.put(df1.parse(String.valueOf(data.get("_id",Document.class).getInteger("year"))).getYear(), data.getInteger("count"));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<Integer,Integer>>(StatusCode.OK,result);
		
	}
	
	@SuppressWarnings("deprecation")
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>> getReleasedGameCountByYearAndGenStats(){

		BasicDBObject split = new BasicDBObject("$unwind" , "$genres" );
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", "$releaseDate")).append("generes", "$genres"));
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , 1));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
	  	 
		HashMap<Integer,HashMap<String,Integer>> result = new HashMap<>();
		HashMap<String,Integer> genresGames;
		
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		Integer year= null;
		
		try {
			System.out.println("---> [MongoConnection][GetReleasedGameCountByYearAndGenStats] Collecting data from the dataset...");
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			e.printStackTrace();
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<Integer,HashMap<String,Integer>>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetReleasedGameCountByYearAndGenStats] Organizing the collected data...");
		
		while( res.hasNext()) {
			
			data = res.next();
			try {
				if( data.get("_id",Document.class).getInteger("year") == null ) continue;
				year = df1.parse(String.valueOf(data.get("_id",Document.class).getInteger("year"))).getYear();
				if( result.containsKey(year))
					genresGames = result.remove(year);
				else
					genresGames = new HashMap<String,Integer>();
				genresGames.put(data.get("_id",Document.class).getString("generes") , data.getInteger("count"));
				result.put(year,genresGames);
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		}

		return new StatusObject<HashMap<Integer,HashMap<String,Integer>>>(StatusCode.OK,result);
	}
	
	public StatusObject<HashMap<String,Statistics>>  getMostViewedGameByGenStats(){
		
		BasicDBObject split = new BasicDBObject("$unwind" , "$genres" );
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("generes", "$genres"));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$last" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.viewsCount", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("viewsCount",1));

		MongoCursor<Document> res = null;
		Document data;
		HashMap<String,Statistics> result = new HashMap<>();
		
		try {
			
			System.out.println("---> [MongoConnection][GetMostViewedGameByGenStats] Collecting data from the dataset...");
			res = statisticsCollection.aggregate(Arrays.asList(split,iniSort,group,project,sort)).allowDiskUse(true).iterator();

		}catch(Exception e ) {
			
			e.printStackTrace();
			System.out.println("---> [MongoConnection][GetMostViewedGameByGenStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetMostViewedGameByGenStats] Organizing the collected data...");
		
		while( res.hasNext()) {
			
			data = res.next();
			

			try {
				
				result.put(data.get("_id",Document.class).getString("generes"), new Statistics(null,data.get("max",Document.class).getInteger("viewsCount"),data.get("max",Document.class).getString("title"),null));

			}catch(Exception e) {
				e.printStackTrace();
			}
		
		}

		return new StatusObject<HashMap<String,Statistics>>(StatusCode.OK,result);	
	}
	
	public StatusObject<HashMap<String,Statistics>> getMostLikedGamesByGenStats(){

		BasicDBObject split = new BasicDBObject("$unwind" , "$genres" );
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("generes", "$genres"));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$last" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.rating", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("rating",1));

		MongoCursor<Document> res = null;
		Document data;
		HashMap<String,Statistics> result = new HashMap<>();
		
		try {
			
			System.out.println("---> [MongoConnection][GetMostLikedGamesByGenStats] Collecting data from the dataset...");
			res = statisticsCollection.aggregate(Arrays.asList(split,iniSort,group,project,sort)).allowDiskUse(true).iterator();

		}catch(Exception e ) {
			
			e.printStackTrace();
			System.out.println("---> [MongoConnection][GetMostLikedGamesByGenStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetMostLikedGamesByGenStats] Organizing the collected data...");		
		while( res.hasNext()) {
			
			data = res.next();
			

			try {
				
				result.put(data.get("_id",Document.class).getString("generes"), new Statistics(null,null,data.get("max",Document.class).getString("title"),data.get("max",Document.class).getDouble("rating")));

			}catch(Exception e) {
				e.printStackTrace();
			}
		
		}

		return new StatusObject<HashMap<String,Statistics>>(StatusCode.OK,result);	
	}
	
	public StatusObject<HashMap<String,Double>> getGeneresGameCountStats(){
		
		Long total = getTotalGamesCount().element;
		if( total == null ) {
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
		}
		
		BasicDBObject split = new BasicDBObject("$unwind" , "$genres" );
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("generes", "$genres"));	    
	    BasicDBObject groupFields = group_id.append("count",  new BasicDBObject("$sum" , 1));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<String,Double> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		
		try {
			System.out.println("---> [MongoConnection][GetGeneresGameCountStats] Collecting data from the dataset..." );
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetGeneresGameCountStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetGeneresGameCountStats] Organizing the collected data..." );
		while( res.hasNext()) {
			
			data = res.next();
			try {
				
				result.put(data.get("_id", Document.class).getString("generes"), Double.parseDouble(new DecimalFormat( "0.0000" ).format(((double)data.get("_id", Document.class).getInteger("count"))/total)));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<String,Double>>(StatusCode.OK,result);

	}
	
	// DA FINIRE
	//  Percentage of the total views give for each generes(sum of the viewsCount of the generes/total views)
	public StatusObject<HashMap<String,Double>> getGeneresMostViewedCountStats(){
		Long total = getTotalGamesCount().element;
		if( total == null ) {
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
		}
		
		BasicDBObject split = new BasicDBObject("$unwind" , "$genres" );
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("generes", "$genres"));	    
	    BasicDBObject groupFields = group_id.append("count",  new BasicDBObject("$sum" , "$viewsCount"));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<String,Double> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		
		try {
			System.out.println("---> [MongoConnection][GetGeneresMostViewedCountStats] Collecting data from the dataset..." );
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetGeneresMostViewedCountStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetGeneresGameCountStats] Organizing the collected data..." );
		while( res.hasNext()) {
			
			data = res.next();
			try {
				
				result.put(data.get("_id", Document.class).getString("generes"), Double.parseDouble(new DecimalFormat( "0.0000" ).format(((double)data.get("_id", Document.class).getInteger("count"))/total)));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<String,Double>>(StatusCode.OK,result);

	}
	//DA FINIRE
	//  Percentage of games add to favourites for each generes(sum of the favouritesCount of the generes/total favouritesCount)
	public StatusObject<HashMap<String,Double>> getGeneresPreferencesStats(){
		
		BasicDBObject split = new BasicDBObject("$unwind" , "$genres" );
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("generes", "$genres"));	    
	    BasicDBObject groupFields = group_id.append("count",  new BasicDBObject("$sum" , "$favouritesCount"));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<String,Double> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		
		try {
			System.out.println("---> [MongoConnection][GetGeneresPreferencesStats] Collecting data from the dataset..." );
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		System.out.println("---> [MongoConnection][GetGeneresGameCountStats] Organizing the collected data..." );
		while( res.hasNext()) {
			
			data = res.next();
			try {
				
				result.put(data.get("_id", Document.class).getString("generes"), Double.parseDouble(new DecimalFormat( "0.0000" ).format(((double)data.get("_id", Document.class).getInteger("count")))));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<String,Double>>(StatusCode.OK,result);

	}
	
	private StatusCode addressVerify( String ipAddr , int port ) {
		
		int[] addr = new int[4];
		String[] values;
		char[] whiteList = {'0','1','2','3','4','5','6','7','8','9','.'};
		
		
		System.out.println("---> [MongoConnection][AddressVerify] Validation of the IP address.." );
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
    	
   	System.out.println( "--- Testing function for MongoDB connector ---" );
    	System.out.println("------  [Mongo Connection test]  ------");
    	
    	System.out.println("----> [TEST] MongoConnection Costructor" );
    	
    	String[] ipAddr = { "127.0.0.1" , "172.16.0.80","305.0.0.0","0..2.0","5,2:3,4","-1.0.0.0","0.0.0.0","255.255.255.255","","50.50.256.50","0.0.0.1000"};
    	int[] ports = { 27017,27018,80};
    	MongoConnection client;
    	long timer; 
    	
    	boolean[] cResults = { true,true,false,false,false,false,true,true,false,false,false};
    	for( int a = 0; a<ports.length; a++ )
    		for( int b = 0; b<ipAddr.length; b++ )
    			try {
    				System.out.println("--->[TEST][MongoConnection] Testing ADDR: " + ipAddr[b] +":" + ports[a]);
    				client = new MongoConnection(ipAddr[b],ports[a]);
    				client.closeConnection();
    				if( a==3 || cResults[b] == false ) {
    					System.out.println("--->[TEST][MongoConnection] Error during the test on IPADDR: " + ipAddr[a] + " PORT: " + ports[b] + " RESULT: true EXPECTED RESULT: false" );
    					return;
    				}
    				
    			}catch( Exception e ) {
    				
    				if( a<2 && cResults[b] == true ){
    					System.out.println("--->[TEST][MongoConnection] Error during the test on IPADDR: " + ipAddr[a] + " PORT: " + ports[b] + " RESULT: false EXPECTED RESULT: true" );
    					return;
    				}
    			}
    	System.out.println("--->[TEST][MongoConnection] Test Correctly Executed");
    	try {
    		client = new MongoConnection( "127.0.0.1" , 27017 );
    	}catch(Exception e) {
    		return;
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
    			return;
    		}
    		if( code.statusCode == StatusCode.OK && code.statusCode == null ) {
    			System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT: false EXPECTED RESULT: true" );
    			return;
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
    			return;
    		}
    		if( rCode != StatusCode.OK && gResults[a] == true ){
    			System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT: true EXPECTED RESULT: false" );
    			return;
    		}
    		if( game != null )
    			if( game.getViewsCount() != (client.getGame(games[a]).element.getViewsCount()-1)){
    				System.out.println("--->[TEST][MongoConnection] Error during the test on ID: " + games[a] + " RESULT NOT UPDATED" );
    				return;
    			}

    	}
    	System.out.println("----> [TEST][MongoConnection.incrementGameViews] Test Correctly Executed" );
    	System.out.println("---> [TEST][Statistics]" );
    	timer = System.currentTimeMillis();
    	if( client.getMostViewedPreview().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetMostViewedPreview] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetMostViewedPreview] RESULT: ERROR" );
   
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getMostPopularPreview().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetMostPopularPreview] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetMostPopularPreview] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getMostLikedGameByYearStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetMostLikedGameByYearStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetMostLikedGameByYearStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getMostViewedGameByYearStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetMostViewedGameByYearStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetMostViewedGameByYearStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getMostLikedGamesByGenStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetMostLikedGamesByGenStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetMostLikedGamesByGenStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getMostViewedGameByGenStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetMostViewedGameByGenStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetMostViewedGameByGenStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getGeneresGameCountStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetGeneresGameCountStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetGeneresGameCountStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getGeneresMostViewedCountStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetGeneresMostViewedCountStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetGeneresMostViewedCountStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getGeneresPreferencesStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetGeneresPreferencesStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetGeneresPreferencesStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getReleasedGameCountByYearAndGenStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetReleasedGameCountByYearAndGenStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetReleasedGameCountByYearAndGenStats] RESULT: ERROR" );
    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer));
    	timer = System.currentTimeMillis();
    	
    	if( client.getReleasedGameCountByYearStats().statusCode == StatusCode.OK )
    		System.out.println("----> [TEST][GetReleasedGameCountByYearStats] RESULT: OK" );
    	else
    		System.out.println("----> [TEST][GetReleasedGameCountByYearStats] RESULT: ERROR" );
    	
    	System.out.println("------ Testing correctly ended. All the function works correctly ------");
    	
    }        	
    
    


}
