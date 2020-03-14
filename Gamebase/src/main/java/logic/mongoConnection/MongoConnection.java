package logic.mongoConnection;

import java.util.List;
import java.util.Scanner;

import logic.data.Game;
import logic.data.PreviewGame;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import java.util.ArrayList;
import java.text.NumberFormat;
import com.mongodb.MongoClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.pojo.PojoCodecProvider;
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

    
    public MongoConnection( String ipAddr , int port ){
    	
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    	mongoClient = new MongoClient( new ServerAddress( ipAddr , port ) , MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        gamesCollection = mongoClient.getDatabase("myDb").getCollection("games",Game.class); 
    
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               FUNCTIONS                                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //  It gives all the information of a game identified by its title. If the argument doVote is setted to true
    //  the function increment the viewedCount of the game(a new visit for the game page)
    
    public Game getGame( String title , boolean doVote ) {
    	
    	try {
    		
    		Game game = gamesCollection.find( eq( "title" , title )).first();
    		
    		if( game == null ) {
    			
    			System.out.println( "---> [MongoConnector][GetGame] Error, game not found" );
    			return null;
    			
    		}
    		
    		if( doVote ) {
    			
    			game.setViewsCount( game.getViewsCount()+1 );
    			UpdateResult res = gamesCollection.updateOne( eq( "_id" , game.getId() ), new Document( "$set" , new Document( "viewsCount" , game.getViewsCount() )));
    	
    			if( res.getMatchedCount() == 0 ){
    				System.out.println( "---> [MongoConnector][GetGame] Error, game not found" );
    				return null;
    			}
    	
    			if( res.getModifiedCount() == 0 ) {
    				System.out.println( "---> [MongoConnector][GetGame] Error, game not modified, the game has already a description" );
    				return game;
    			}
    		}
    		
    		return game;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGame] Error, Connection Lost" );
    		return null;
    		
    	}
    }
    
    private Game getGameById(int id ) {
    	
		Game game = gamesCollection.find( eq( "_id" , id )).first();
		
		if( game == null ) {
			
			System.out.println( "---> [MongoConnector][GetGame] Error, game not found" );
			return null;
			
		}
		return game;
    }
    
    //  Used by the webscrape function to update the description of a game, if the same description is already added
    //  it signals an error
    
    public boolean addGameDescription( int id , String description ) {
    	
    	try {

    		UpdateResult res = gamesCollection.updateOne( eq( "_id" , id ) , new Document( "$set", new Document( "description" , description )));
    	
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][AddGameDescription] Error, game not found" );
    			return false;
    			
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][AddGameDescription] Error, game not modified, the game has already a description" );
    			return false;
    			
    		}
    		
    		return true;
    		
    	}catch( Exception e ){
    		
    		System.out.println( "---> [MongoConnector][AddGameDescription] Error, Connection Lost" );
    		return false;
    		
    	}
    	
    }
    
    //  delete a game from the document database, user by the administrator interface 
    public boolean deleteGame( int id ) {
    	
    	try {

    		DeleteResult res = gamesCollection.deleteOne( eq( "_id" , id ));
    		if( res.getDeletedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][DeleteGame] Error, game not found" );
    			return false;
    			
    		}
    		
    		return true;
    		
    	}catch(Exception e) {
    		
    		System.out.println( "---> [MongoConnector][DeleteGame] Error, Connection Lost" );
    		return false;
    		
    	}
    }
    
    // recalculate the rating and ratingCount of a game to add a new user vote
    public boolean voteGame( String title, int vote ) {
    	
    	try {
    		
    		Game game = getGame( title , false );
    	
    		if( vote < 0 || vote > 5) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, the vote must be in [0,5]" );
    			return false;
    			
    		}
    	
    		if( game == null ) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not found" );
    			return false;
    			
    		}
    		
    		int count = game.getRatingCount();
    		NumberFormat nf = new DecimalFormat( "0.0000" );
    		double newRate = Double.parseDouble( nf.format(( game.getRating()*count + vote )/( count+1 )).replace( "," , "." ));
    		count++;
    		UpdateResult res = gamesCollection.updateOne( eq( "_id" , game.getId() ), new Document( "$set" , new Document( "rating" , newRate ).append( "ratingCount", count )));
    	
    		if( res.getMatchedCount() == 0 ){
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not found" );
    			return false;
    			
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			
    			System.out.println( "---> [MongoConnector][VoteGame] Error, game not modified, the game has already a description" );
    			return false;
    			
    		}
    		
    		return true;
    		
    	}catch(Exception e) {
    		
    		System.out.println("---> [MongoConnector][VotoGame] Error, Connection Lost");
    		return false;
    		
    	}
    	
    }
    
    //  the function gives all the images URL of the given game
    public List<String> getGamePics( String title ){
    	
    	try {
    		
    		Game game = getGame(title,false);
    		
    		if( game == null ) {
    			
    			System.out.println("---> [MongoConnector][GetGamePics] Error, game not found" );
    			return new ArrayList<>();
    			
    		}
    		
    		if( game.getMultimedia() == null ) 
    			return new ArrayList<>();
    		
    		return game.getMultimedia().getImages(); 
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGamePics] Error, connection lost" );
    		return new ArrayList<>();
    		
    	}
    	
    }
    
    //  gives the total number of games into the database
    public long getTotalGamesCount() {
    	
    	try {
    		
    		long value = gamesCollection.countDocuments();
    		return value;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetTotalGamesCount] Error, connection lost" );
    		return -1;
    	}
    	
    }
    
    //  gives all the generes used by the games into all the database
    public List<String> getGenres(){
    	
    	List<String> retList = new ArrayList<>();
    	
    	try {
    		
    		MongoCursor<String> genres = gamesCollection.distinct( "genres" , String.class ).iterator();
    		
        	while( genres.hasNext() )
        		retList.add( genres.next() );
        	
        	return retList;
        	
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][GetGenres] Error, connection lost" );
    		return retList;
    		
    	}

    }
    
    //  close the connection used by the mongo client
    public void closeConnection() {
    	
    	try{ this.mongoClient.close(); }catch( Exception e ) {}
    	
    }
    
    //  gives the most viewed game into the database
    public PreviewGame getMostViewedPreview(){
    	
    	try {
    		
    		Game game = gamesCollection.find().sort(Sorts.descending("viewsCount")).projection(Projections.include("title","background_image")).first();
    		if( game == null ) {
    			
        		System.out.println( "---> [MongoConnector][GetMostViewedPrevidew] Error, Game not found" );
    			return null;
    			
    		}else
    			return game.getPreview();
    		
    	}catch( Exception e) {
    		
    		System.out.println( "---> [MongoConnector][GetMostViewedPrevidew] Error, Connection Lost" );
    		return null;
    		
    	}
    	
    }
    
    //  gives the most popular game into the database
    public PreviewGame getMostPopularPreview(){
    	
    	try {
    		Game game = gamesCollection.find().sort(Sorts.descending("ratingCount")).projection(Projections.include("title","background_image")).first();
    		if( game == null ) return null;
    		else
    			return game.getPreview();
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][GetMostPopularPreview] Error, Connection Lost");
    		return null;
    	}
	
    }
    
    //  gives a structure to navigate into the ordered list(by views) of the games
    public DataNavigator getMostViewedPreviews(){
    	 
    	return new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_VIEWED , "" );
    	
    }
    
    //  gives a structure to navigate into the ordered list(by rate) of the games
    public DataNavigator getMostLikedPreviews(){
    	
    	return new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_LIKED , "" );
    	
    }
    
    //  gives a structure to navigate into the ordered list(by id) of the games
    public DataNavigator getMostRecentPreviews(){
    	
    	return new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_RECENT , "" );
    	
    }
    
    //  gives a structure to navigate into the ordered list(by given search string) of the games
    public DataNavigator searchGames( String search ){
    	
    	return new DataNavigator( gamesCollection , 50 , NavType.PREVIEW_SEARCH , search );
    	
    }
    
    //  update the favouritesCount of a game by adding the given count param
    public boolean updateFavouritesCount( String title , int count ) {
    	
    	try {
    		
    		Game game = getGame( title , false );
    		System.out.println("ok tutto bene");
    		UpdateResult res = gamesCollection.updateOne( eq( "_id" , game.getId() ), new Document( "$set" , new Document( "favouritesCount" , game.getFavouritesCount()+count )));
    	
    		if( res.getMatchedCount() == 0 ){
    			System.out.println( "--->  [MongoConnector][UpdateFavouritesCount] Error, game not found" );
    			return false;
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			System.out.println( "---> [MongoConnector][UpdateFavouritesCount] Error, game not modified, the game has already updated" );
    			return false;
    		}
    		
    		return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][UpdateFavouritesCount] Error, Connection Lost" );
    		return false;
    		
    	}
    }
    
    //  update the favouritesCount of a game by adding the given count param
    private boolean setFavouritesCount( int id, int count ) {
    	
    	try {
    		
    		UpdateResult res = gamesCollection.updateOne( eq( "_id" , id ), new Document( "$set" , new Document( "favouritesCount" , count )));
    	
    		if( res.getMatchedCount() == 0 ){
    			System.out.println( "--->  [MongoConnector][UpdateFavouritesCount] Error, game not found" );
    			return false;
    		}
    	
    		if( res.getModifiedCount() == 0 ) {
    			System.out.println( "---> [MongoConnector][UpdateFavouritesCount] Error, game not modified, the game has already updated" );
    			return false;
    		}
    		
    		return true;
    		
    	}catch( Exception e ) {
    		
    		System.out.println( "---> [MongoConnector][UpdateFavouritesCount] Error, Connection Lost" );
    		return false;
    		
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
	
	public void favouriterConsistencyAdapt() {
		
		try {
			Scanner in = new Scanner(new FileInputStream("/home/nico/Documenti/results.txt"));
			String[] values;
			int nVotes;
			int vote;
			
			while( in.hasNext()) {
				nVotes = 0;
				values = in.nextLine().split("#");
				nVotes = Integer.parseInt(values[3]);
				vote = Integer.parseInt(values[2])/nVotes;
				setFavouritesCount(Integer.parseInt(values[0]),Integer.parseInt(values[1]));
				Game game = getGameById( Integer.parseInt(values[0]));
				for( int a = 0; a<nVotes;a++)
					voteGame(game.getTitle(), vote );

			}
			
			in.close();
    		
    		return;
    		
    	}catch(Exception e) {
    		
    		System.out.println("---> [MongoConnector][VotoGame] Error, Connection Lost");
    		e.printStackTrace();
    		return;
    		
    	}
	}
	

    
    public static void main(String[] args) throws InterruptedException {
    	
    	System.out.println("------  [Mongo Connection test]  ------");
    	MongoConnection client = new MongoConnection("172.16.0.80",27018);
    	System.out.println("ok!");
    	client.favouriterConsistencyAdapt();
    	//System.out.println(client.voteGame("BioShock", 5));
    	/*System.out.println(client.getGame("BioShock",true));
    	//System.out.println(client.addGameDescription("BioShock", "Andiamocene a Newtopia"));

    	System.out.println(client.getGamePics("BioShock"));
    	System.out.println(client.getTotalGamesCount());
    	System.out.println(client.getGenres());

    	System.out.println(client.getMostViewedPreview().getGameTitle());
    	System.out.println(client.getMostPopularPreview().getGameTitle());
    	//System.out.println(client.getMostLikedPreviews().size());
    	//System.out.println(client.getMostViewedPreviews().size());
    	//System.out.println(client.getMostRecentPreviews().size());
    	DataNavigator games = new DataNavigator(client.gamesCollection,20,NavType.PREVIEW_RECENT,"");
    	List<PreviewGame> previews;
    	previews = games.getNextData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getNextData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getNextData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getNextData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getNextData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getNextData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getPrevData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getPrevData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getPrevData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getPrevData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getPrevData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	previews = games.getPrevData();
    	for( PreviewGame game : previews )
    		System.out.println("Title: " + game.getGameTitle());
    	

    	System.out.println("MUST BE ZERO: " + games.getPrevData().size());*/
    
    	

    	client.closeConnection();
    	System.out.println("Programma terminato");
    	
    }
    


}
