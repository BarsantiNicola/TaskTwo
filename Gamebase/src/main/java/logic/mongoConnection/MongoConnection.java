package logic.mongoConnection;

import java.util.List;
import logic.StatusCode;
import logic.StatusObject;
import logic.data.Game;
import logic.data.PreviewGame;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
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
import org.bson.codecs.configuration.CodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConnection {
    
	private MongoClient mongoClient;    
    private MongoCollection<Game> gamesCollection; 
    
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
    		gamesCollection = mongoClient.getDatabase("myDb").getCollection("games",Game.class); 
    		
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
    
    //  the function gives all the images URL of the given game
    public StatusObject<List<String>> getGamePics( int gameId ){
    	
    	try {
    		
    		Game game = getGame(gameId).getValue();
    		
    		if( game == null ) {
    			
    			System.out.println("---> [MongoConnector][GetGamePics] Error, game not found" );
    			return new StatusObject<List<String>>( StatusCode.ERR_DOCUMENT_GAME_NOT_FOUND , null );
    			
    		}
    		
    		if( game.getMultimedia() == null ){
    			System.out.println("---> [MongoConnector][GetGamePics] Error, the game doesn't have multimedia datas" );
    			return new StatusObject<List<String>>( StatusCode.ERR_DOCUMENT_MULTIMEDIA_FIELD_NOT_FOUND, null );
    		}
    		
    		return new StatusObject<List<String>>( StatusCode.OK, game.getMultimedia().getImages() ); 
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGamePics] Error, connection lost" );
			return new StatusObject<List<String>>( StatusCode.ERR_NETWORK_UNREACHABLE, null );
    		
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
    	
    	try {
    		return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_VIEWED , "" ));
    	}catch( Exception e ) {
    		return new StatusObject<DataNavigator>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
    	}
    	
    }
    
    //  gives a structure to navigate into the ordered list(by rate) of the games
    public StatusObject<DataNavigator> getMostLikedPreviews(){
    	
    	try {
    		
    		return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_LIKED , "" ));
    		
		}catch( Exception e ) {
			
    		return new StatusObject<DataNavigator>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}
    	
    }
    
    //  gives a structure to navigate into the ordered list(by id) of the games
    public StatusObject<DataNavigator> getMostRecentPreviews(){
    	
    	try {
    		
    		return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_RECENT , "" ));
    		
    	}catch( Exception e ) {
    		
    		return new StatusObject<DataNavigator>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
    	}
    	
    }
    
    //  gives a structure to navigate into the ordered list(by given search string) of the games
    public StatusObject<DataNavigator> searchGames( String search ){
    	
    	try {
    		
    		return new StatusObject<DataNavigator>( StatusCode.OK, new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_SEARCH , search ));
    		
    	}catch( Exception e ) {
    		return new StatusObject<DataNavigator>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
    	}
    	
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

    
    //  Most liked games year by year
	public List<Game> getMostLikedGameByYearStats(){
		return null;
	}
	
	//  Most liked games for each generes
	public List<Game> getMostLikedGamesByGenStats(){
		return null;
	}
	
	//  Most viewed games year by year
	public List<Game> getMostViewedGameByYearStats(){
		return null;
	}
	
	//  Most viewed games for each generes
	public List<Game> getMostViewedGameByGenStats(){
		return null;		
	}
	
	//  Number of games released year by year
	public List<Integer> getReleasedGameByYearStats(){
		return null;
	}
	
	//  Number of games released year by year and grouped by genres
	public List<Integer> getReleasedGameByYearAndGenStats(){
		return null;
	}
	
	//  Percentage of the total games used by each generes(games of the generes/total games)
	public List<Integer> getGeneresWeightStats(){
		return null;
	}
	
	//  Percentage of the total views give for each generes(sum of the viewsCount of the generes/total views)
	public List<Integer> getGeneresMostViewedStats(){
		return null;
	}
	
	//  Percentage of games add to favourites for each generes(sum of the favouritesCount of the generes/total favouritesCount)
	public List<Integer> getGeneresPreferencesStats(){
		return null;
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
    		MongoConnection client = new MongoConnection("172.16.0.80",27018);
        	System.out.println("ok!");
        	//client.favouriterConsistencyAdapt();
        	System.out.println(client.getGenres());
        	client.closeConnection();
    	}catch(UnknownHostException | MongoSocketOpenException e) {
    		System.out.println("errore");
    		//System.out.println(e.getClass());
    	}

    
    	


    	System.out.println("Programma terminato");
    	
    }
    


}
