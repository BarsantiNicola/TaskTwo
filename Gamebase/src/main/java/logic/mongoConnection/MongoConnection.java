package logic.mongoConnection;

import java.util.List;
import logic.StatusCode;
import logic.StatusObject;
import logic.data.Game;
import logic.data.PreviewGame;
import logic.data.Statistics;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoSocketOpenException;
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
    	
    	//  first we verify the address is correct
    	if( addressVerify(ipAddr,port) != StatusCode.OK )
    		throw new UnknownHostException();
    	else{
    		
        	ServerAddress server = new ServerAddress( ipAddr , port );
    		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    		mongoClient = new MongoClient( server , MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());      
    		gamesCollection = mongoClient.getDatabase("myDb2").getCollection("games",Game.class); 
    		statisticsCollection = mongoClient.getDatabase("myDb2").getCollection("games",Document.class);
    		
    	}
    
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               FUNCTIONS                                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public StatusObject<Game> getGame( int gameId ) {
    	
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
    
    public StatusObject<String> incrementGameViews( int gameID ) {
		
    	Bson filter = eq( "_id", gameID );
    	Bson updateOperation = Updates.inc( "viewsCount", 1 );
    	
    	try {
    		
    		UpdateResult res = gamesCollection.updateOne( filter, updateOperation );
		
    		System.out.println( "---> [MongoConnector][GetGame] Updating the game views count" );
    		if( res.getMatchedCount() == 0 ){
    			System.out.println( "---> [MongoConnector][GetGame] Error, game not found" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND );
    		}

    		if( res.getModifiedCount() == 0 ) {
    			System.out.println( "---> [MongoConnector][GetGame] Error, game found but not modified" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_UNKNOWN );
    		}
		
    		System.out.println( "---> [MongoConnector][GetGame] Views count correctly updated" );
    		return StatusObject.buildError( StatusCode.OK );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGame] Error, network unreachable" );
    		return StatusObject.buildError( StatusCode.ERR_NETWORK_UNREACHABLE );
    		
    	}
    	
    }
    
    public StatusObject<String> addGameDescription( int gameId , String description ) {
    	
    	try {

    		Bson filter = eq( "_id", gameId );
    		Bson update = Updates.set( "description", description );
    		
    		UpdateResult res = gamesCollection.updateOne( filter , update );
    	
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][AddGameDescription] Error, game not found" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND );
    			
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][AddGameDescription] Error, game not modified, the game has already a description" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_UNKNOWN );
    			
    		}
    		
    		return StatusObject.buildError( StatusCode.OK );
    		
    	}catch( Exception e ){
    		
    		System.out.println( "---> [MongoConnector][AddGameDescription] Error, Connection Lost" );
    		return StatusObject.buildError( StatusCode.ERR_NETWORK_UNREACHABLE );
    		
    	}
    	
    }
    
    public StatusObject<String> deleteGame( int id ) {
    	
    	try {
    		
    		Bson filter = eq( "_id" , id );
    		DeleteResult res = gamesCollection.deleteOne( filter );
    		
    		if( res.getDeletedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][DeleteGame] Error, game not found" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND );
    			
    		}
    		
    		return StatusObject.buildError( StatusCode.OK );
    		
    	}catch(Exception e) {
    		
    		System.out.println( "---> [MongoConnector][DeleteGame] Error, Connection Lost" );
    		return StatusObject.buildError( StatusCode.ERR_NETWORK_UNREACHABLE );
    		
    	}
    }
    
    public StatusObject<String> voteGame( int gameId , int vote ) {
    	
    	try {

    		if( vote < 0 || vote > 5 ) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, the vote must be in [0,5]" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_INVALID_VOTE );
    			
    		}
    	
    		Game game = getGame( gameId ).getValue();
    		
    		if( game == null ) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not found" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND );
    			
    		}
    		
    		Bson filter = eq( "_id" , gameId );  		
    		List<Bson> updates = new ArrayList<Bson>();
    		updates.add(Updates.inc( "ratingCount" , 1 ));
    		updates.add(Updates.set( "rating" , Double.parseDouble( new DecimalFormat( "0.0000" ).format(( game.getRating()*game.getRatingCount() + vote )/( game.getRatingCount()+1 )).replace( "," , "." ))));

    		UpdateResult res = gamesCollection.updateOne( filter , updates );
    	
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not found" );
    			return StatusObject.buildError(StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND);
    			
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not modified, the game has already a description" );
    			return StatusObject.buildError(StatusCode.ERR_DOCUMENT_UNKNOWN);
    			
    		}
    		
    		return StatusObject.buildError(StatusCode.OK);
    		
    	}catch(Exception e) {
    		
    		System.out.println("---> [MongoConnector][VotoGame] Error, Connection Lost");
    		return StatusObject.buildError( StatusCode.ERR_NETWORK_UNREACHABLE );
    		
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

    public StatusObject<String> closeConnection() {
    	
    	try{ 
    		
    		this.mongoClient.close();
    		System.out.println( "---> [MongoConnector][CloseConnection] Connection close" );
    		return StatusObject.buildError( StatusCode.OK );
    	}catch( Exception e ) {

    		System.out.println( "---> [MongoConnector][CloseConnection] Unknown error during the close of the connection" );
    		return StatusObject.buildError( StatusCode.ERR_DOCUMENT_UNKNOWN );
    		
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
    			return new StatusObject<PreviewGame>( StatusCode.OK, game.getPreview());
    		
    	}catch( Exception e) {
    		
    		System.out.println( "---> [MongoConnector][GetMostViewedPrevidew] Error, Connection Lost" );
			return new StatusObject<PreviewGame>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
    	}
    	
    }
    
    //  gives the most popular game into the database
    public StatusObject<PreviewGame> getMostPopularPreview(){
    	
    	try {
    		
    		Bson sort = Sorts.descending( "ratingCount" );
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
    
    //  gives a structure to navigate into the ordered list(by views) of the games
    public StatusObject<DataNavigator> getMostViewedPreviews(){
    	
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_VIEWED , "" ));
    	
    }
    
    //  gives a structure to navigate into the ordered list(by rate) of the games
    public StatusObject<DataNavigator> getMostLikedPreviews(){

    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_LIKED , "" ));
    	
    }
    
    //  gives a structure to navigate into the ordered list(by id) of the games
    public StatusObject<DataNavigator> getMostRecentPreviews(){
    		
    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_RECENT , "" ));
    	
    }
    
    //  gives a structure to navigate into the ordered list(by given search string) of the games
    public StatusObject<DataNavigator> searchGames( String search ){

    	return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_SEARCH , search ));
    	
    }
    
    public StatusObject<String> updateFavouritesCount( int gameId , int count ) {
    	
    	try {
    		
    		Bson filter = eq( "_id" , gameId );
    		Bson update = Updates.inc( "favouritesCount", 1 );

    		UpdateResult res = gamesCollection.updateOne( filter , update );
    	
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "--->  [MongoConnector][UpdateFavouritesCount] Error, game not found" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND );
    			
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][UpdateFavouritesCount] Error, game not modified, the game has already updated" );
    			return StatusObject.buildError( StatusCode.ERR_DOCUMENT_UNKNOWN );
    			
    		}
    		
			return StatusObject.buildError( StatusCode.OK );
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][UpdateFavouritesCount] Error, Connection Lost" );
			return StatusObject.buildError( StatusCode.ERR_NETWORK_UNREACHABLE );
    		
    	}
    }

	@SuppressWarnings("deprecation")
	public StatusObject<List<Statistics>> getMostLikedGameByYearStats(){
		
		BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", new BasicDBObject("$arrayElemAt" ,  Arrays.asList("$releases.releaseDate",0)))));
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
			
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();
			System.out.println("Statistiche raccolte " + res );
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<List<Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
	
	//  Most viewed games year by year
	@SuppressWarnings({ "deprecation" })
	public StatusObject<List<Statistics>> getMostViewedGameByYearStats(){
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", new BasicDBObject("$arrayElemAt" ,  Arrays.asList("$releases.releaseDate",0)))));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$last" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.viewsCount", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("viewsCount",1));
		         
		List<Statistics> result = new ArrayList<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		
		try {
			
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();
			System.out.println("Statistiche raccolte " + res );
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<List<Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
	
	//  Number of games released year by year
	@SuppressWarnings("deprecation")
	public StatusObject<HashMap<Integer,Integer>>  getReleasedGameCountByYearStats(){
	
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", new BasicDBObject("$arrayElemAt" ,  Arrays.asList("$releases.releaseDate",0)))));
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , 1));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<Integer,Integer> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		
		try {
			
			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<Integer,Integer>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		while( res.hasNext()) {
			
			data = res.next();
			try {
				
				result.put(df1.parse(String.valueOf(data.get("_id",Document.class).getInteger("year"))).getYear(), data.getInteger("count"));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<Integer,Integer>>(StatusCode.OK,result);
		
	}
	
	//  Number of games released year by year and grouped by genres
	@SuppressWarnings("deprecation")
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>> getReleasedGameCountByYearAndGenStats(){

		BasicDBObject split = new BasicDBObject("$unwind" , "$genres" );
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", new BasicDBObject("$arrayElemAt" ,  Arrays.asList("$releases.releaseDate",0)))).append("generes", "$genres"));
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
			
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			e.printStackTrace();
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<Integer,HashMap<String,Integer>>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
	
	//  Most viewed games for each generes
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
			
			res = statisticsCollection.aggregate(Arrays.asList(split,iniSort,group,project,sort)).allowDiskUse(true).iterator();
			System.out.println("Statistiche raccolte " + res );
		}catch(Exception e ) {
			e.printStackTrace();
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
	
	//  Most liked games for each generes
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
			
			res = statisticsCollection.aggregate(Arrays.asList(split,iniSort,group,project,sort)).allowDiskUse(true).iterator();
			System.out.println("Statistiche raccolte " + res );
		}catch(Exception e ) {
			e.printStackTrace();
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
	
	//  Percentage of the total games used by each generes(games of the generes/total games)
	public StatusObject<HashMap<String,Double>> getGeneresGameCountStats(){
		
		Long total = getTotalGamesCount().getValue();
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
			
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
		Long total = getTotalGamesCount().getValue();
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
			
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
			
			res = statisticsCollection.aggregate(Arrays.asList(split,group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][GetReleaeGameCountByYearStats] Error, network unreachable" );
			return new StatusObject<HashMap<String,Double>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

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
		
		System.out.print("---> [MongoConnector][AddressVerify] Initial verification of address" );
		System.out.flush();
		
		
		if( port != 27018 && port != 27017 ) {
			System.out.println(" ------- ERROR");
			return StatusCode.ERR_WRONG_PORT;
		}
		System.out.println("ok1");
		if( ipAddr.length()>15 || ipAddr.length()<7) {
				System.out.println(" ------- ERROR");
				return StatusCode.ERR_WRONG_IP_ADDR;
		}
		System.out.println("ok2");		
		for( int a = 0; a<ipAddr.length(); a++ ) 	
			for( int b = 0; b<whiteList.length; b++ ) {
				if(ipAddr.charAt(a) == whiteList[b] )
					break;
				if( b == whiteList.length-1){
					System.out.println(" ------- ERROR");
					return StatusCode.ERR_WRONG_IP_ADDR;
				}
			}
		System.out.println("ok3 " + ipAddr);
		values = ipAddr.split("\\.");
		
		if( values.length < 4 ){
			System.out.println(" ------- ERROR");
			return StatusCode.ERR_WRONG_IP_ADDR;
		}	
		System.out.println("ok4 ");
		for( int a = 0; a<4;a++ ) {
			addr[a] = Integer.parseInt(values[a]);
			if( addr[a] > 255 || addr[a] < 0) {
				System.out.println(" ------- ERROR");
				return StatusCode.ERR_WRONG_IP_ADDR;
			}
		}
		return StatusCode.OK;
			
	}
	
    
    public static void main(String[] args) throws InterruptedException {
    	
    	System.out.println("------  [Mongo Connection test]  ------");
    	try {
    		MongoConnection client = new MongoConnection("127.0.0.1",27017);
        	System.out.println("ok!");
        	List<Statistics> prova = client.getMostViewedGameByYearStats().getValue();
        	System.out.println("prova: " + prova );
        	for( Statistics p: prova )
        		System.out.println("Anno: " + p.getYear() + " Views: " + p.getViewsCount() + " Game: " + p.getGames() );
        	
        	prova = client.getMostLikedGameByYearStats().getValue();
        	System.out.println("prova: " + prova );
        	for( Statistics p: prova )
        		System.out.println("Anno: " + p.getYear() + " Views: " + p.getRating() + " Game: " + p.getGames() );
        	//client.favouriterConsistencyAdapt();
        	
        	HashMap<Integer,HashMap<String,Integer>> val = client.getReleasedGameCountByYearAndGenStats().getValue();
            HashMap<String,Integer> values;
        	for( Integer year: val.keySet()) {
        		values = val.get(year);
        		for( String gen : values.keySet())
        			System.out.println("Anno: " + year + " Genere: " + gen + " Count: " + values.get(gen));
        	}
        	HashMap<String,Statistics> val2 = client.getMostViewedGameByGenStats().getValue();
        	for( String gen:val2.keySet())
        		System.out.println("Genere: " + gen + " Count: " + val2.get(gen).getViewsCount() + "Game: "+ val2.get(gen).getGames() );
        	client.closeConnection();
    	}catch(UnknownHostException | MongoSocketOpenException e) {
    		System.out.println("errore");
    		//System.out.println(e.getClass());
    	}

    
    	


    	System.out.println("Programma terminato");
    	
    }
    


}
