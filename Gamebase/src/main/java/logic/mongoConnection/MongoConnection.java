package logic.mongoConnection;

import java.util.List;
import logic.data.Game;
import logic.data.PreviewGame;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import java.util.ArrayList;
import java.text.NumberFormat;
import com.mongodb.MongoClient;
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
    
    public MongoConnection(){
    	
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    	mongoClient = new MongoClient( new ServerAddress("172.0.0.1" , 27017) , MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        gamesCollection = mongoClient.getDatabase("myDb").getCollection("games",Game.class); 
    
    }
    
    public Game getGame(String title, boolean doVote ) {
    	
    	try {
    		Game game = gamesCollection.find(eq("title",title)).first();
    		if( game == null ) return null;
    		if( doVote ) {
    			game.setViewsCount(game.getViewsCount()+1);
    			UpdateResult res = gamesCollection.updateOne(eq("_id",game.getId()), new Document("$set", new Document("viewsCount", game.getViewsCount())));
    	
    			if(res.getMatchedCount() == 0 ){
    				System.out.println("--> [MongoConnector][GetGame] Error, game not found");
    				return null;
    			}
    	
    			if(res.getModifiedCount() == 0 ) {
    				System.out.println("--> [MongoConnector][GetGame] Error, game not modified, the game has already a description");
    				return game;
    			}
    		}
    		return game;
    		
    	}catch(Exception e) {
    		
    		System.out.println("--> [MongoConnector][GetGame] Error, Connection Lost" );
    		return null;
    		
    	}
    }
    
    public boolean addGameDescription(String title , String description ) {
    	
    	try {
    		Game game = getGame(title,false);
    		UpdateResult res = gamesCollection.updateOne(eq("_id", game.getId()), new Document("$set", new Document("description", description)));
    	
    		if(res.getMatchedCount() == 0 ){
    			System.out.println("--> [MongoConnector][AddGameDescription] Error, game not found");
    			return false;
    		}
    	
    		if(res.getModifiedCount() == 0 ) {
    			System.out.println("--> [MongoConnector][AddGameDescription] Error, game not modified, the game has already a description");
    			return false;
    		}
    		return true;
    		
    	}catch(Exception e) {
    		
    		System.out.println("--> [MongoConnector][AddGameDescription] Error, Connection Lost");
    		return false;
    		
    	}
    	
    }
    
    public boolean deleteGame(String title) {
    	try {
    		Game game = getGame(title,false);
    		DeleteResult res = gamesCollection.deleteOne(eq("_id",game.getId()));
    		if(res.getDeletedCount()==0 ) {
    			System.out.println("--> [MongoConnector][DeleteGame] Error,game not found");
    			return false;
    		}
    		return true;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][DeleteGame] Error, Connection Lost");
    		return false;
    	}
    }
    
    public boolean voteGame(String title, int vote ) {
    	try {
    		Game game = getGame(title,false);
    		if( vote < 0 || vote > 5) {
    			System.out.println( "-- [MongoConnector][VoteGame] Error, the vote must be in [0,5]" );
    			return false;
    		}
    	
    		if( game == null ) {
    			System.out.println( "--> [MongoConnector][VoteGame] Error, game not found" );
    			return false;
    		}
    		int count = game.getRatingCount();
    		NumberFormat nf = new DecimalFormat("0.0000");
    		double newRate = Double.parseDouble(nf.format((game.getRating()*count + vote)/(count+1)).replace(",", "."));
    		count++;
    		UpdateResult res = gamesCollection.updateOne(eq("_id", game.getId()), new Document("$set", new Document("rating", newRate).append("ratingCount", count )));
    	
    		if(res.getMatchedCount() == 0 ){
    			System.out.println("--> [MongoConnector][VoteGame] Error, game not found");
    			return false;
    		}
    	
    		if(res.getModifiedCount() == 0 ) {
    			System.out.println("--> [MongoConnector][VoteGame] Error, game not modified, the game has already a description");
    			return false;
    		}
    		return true;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][VotoGame] Error, Connection Lost");
    		return false;
    	}
    	
    }
    
    public List<String> getGamePics( String title ){
    	
    	try {
    		Game game = getGame(title,false);
    		if( game == null ) {
    			System.out.println("--> [MongoConnector][GetGamePics] Error, game not found" );
    			return new ArrayList<>();
    		}
    		if( game.getMultimedia() == null ) 
    			return new ArrayList<>();
    		
    		return game.getMultimedia().getImages(); 
    		
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][GetGamePics] Error, connection lost");
    		return new ArrayList<>();
    	}
    	
    }
    
    public long getTotalGamesCount() {
    	try {
    		long value = gamesCollection.countDocuments();
    		return value;
    	}catch(Exception e ) {
    		System.out.println("--> [MongoConnector][GetTotalGamesCount] Error, connection lost");
    		return -1;
    	}
    }
    
    public List<String> getGenres(){
    	
    	List<String> retList = new ArrayList<>();
    	try {
    		MongoCursor<String> genres = gamesCollection.distinct("genres", String.class).iterator();
        	while(genres.hasNext())
        		retList.add(genres.next());
        	return retList;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][GetGenres] Error, connection lost");
    		return retList;
    	}

    }
    
    public void closeConnection() {
    	try {
    		this.mongoClient.close();
    	}catch(Exception e) {}
    }
    
    public PreviewGame getMostViewedPreview(){
    	
    	try {
    		Game game = gamesCollection.find().sort(Sorts.descending("viewsCount")).projection(Projections.include("title","background_image")).first();
    		if( game == null ) return null;
    		else
    			return game.getPreview();
    	}catch( Exception e) {
    		System.out.println("--> [MongoConnector][GetMostViewedPrevidew] Error, Connection Lost");
    		return null;
    	}
    	
    }
    
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
    
    public List<PreviewGame> getMostViewedPreviews(){
    	
    	try {
    		List<PreviewGame> previews = new ArrayList<>();
    		MongoCursor<Game> games = gamesCollection.find().sort(Sorts.descending("viewsCount")).projection(Projections.include("title","background_image")).iterator();
    		while(games.hasNext())
    			previews.add(games.next().getPreview());
    		return previews;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][GetMostViewedPreviews] Error, Connection Lost");
    		return new ArrayList<>();
    	}
    	
    }
    
    public List<PreviewGame> getMostLikedPreviews(){
    	
    	try {
    		List<PreviewGame> previews = new ArrayList<>();
    		MongoCursor<Game> games = gamesCollection.find().sort(Sorts.descending("rating")).projection(Projections.include("title","background_image")).iterator();
    		while(games.hasNext())
    			previews.add(games.next().getPreview());
    		return previews;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][GetMostLikedPreviews] Error, Connection Lost" );
    		return new ArrayList<>();
    	}
    	
    }
    
    public List<PreviewGame> getMostRecentPreviews(){
    	
    	try {
    		List<PreviewGame> previews = new ArrayList<>();
    		MongoCursor<Game> games = gamesCollection.find().sort(Sorts.descending("_id")).projection(Projections.include("title","background_image")).iterator();
    		while(games.hasNext())
    			previews.add(games.next().getPreview());
    		return previews;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][GetMostRecentPreviews] Error, Connection Lost");
    		return new ArrayList<>();
    	}
    	
    }
    
    public List<PreviewGame> searchGames(String search ){
    	
    	try {
    		List<PreviewGame> previews = new ArrayList<>();
    		MongoCursor<Game> games = gamesCollection.find(or(eq("title" , search ) , elemMatch("genres", eq(search)))).projection(Projections.include("title","background_image")).iterator();
    		while(games.hasNext())
    			previews.add(games.next().getPreview());
    		return previews;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][SearchGames] Error, Connection Lost");
    		return new ArrayList<>();
    	}
    	
    }
    
    public boolean updateFavouritesCount( String title , int count ) {
    	try {
    		Game game = getGame(title,false);
    		UpdateResult res = gamesCollection.updateOne(eq("_id", game.getId()), new Document("$set", new Document("favouritesCount", game.getFavouritesCount()+count)));
    	
    		if(res.getMatchedCount() == 0 ){
    			System.out.println("-->  [MongoConnector][UpdateFavouritesCount] Error, game not found");
    			return false;
    		}
    	
    		if(res.getModifiedCount() == 0 ) {
    			System.out.println("--> [MongoConnector][UpdateFavouritesCount] Error, game not modified, the game has already a description");
    			return false;
    		}
    		return true;
    	}catch(Exception e) {
    		System.out.println("--> [MongoConnector][UpdateFavouritesCount] Error, Connection Lost");
    		return false;
    	}
    }

    
    public static void main(String[] args) {
    	
    	System.out.println("------  [Mongo Connection test]  ------");
    	MongoConnection client = new MongoConnection();
    	//System.out.println(client.voteGame("BioShock", 5));
    	System.out.println(client.getGame("BioShock",true));
    	//System.out.println(client.addGameDescription("BioShock", "Andiamocene a Newtopia"));

    	System.out.println(client.getGamePics("BioShock"));
    	System.out.println(client.getTotalGamesCount());
    	System.out.println(client.getGenres());
    	System.out.println(client.searchGames("BioShock").get(0).getGameTitle());
    	System.out.println(client.getMostViewedPreview().getGameTitle());
    	System.out.println(client.getMostPopularPreview().getGameTitle());
    	//System.out.println(client.getMostLikedPreviews().size());
    	//System.out.println(client.getMostViewedPreviews().size());
    	//System.out.println(client.getMostRecentPreviews().size());
    	DataNavigator games = new DataNavigator(client.gamesCollection,20,NavType.PREVIEW_RECENT,"");
    	System.out.println(games.getNextData().size());
    	System.out.println(games.getNextData().size());
    	System.out.println(games.getNextData().size());
    	System.out.println(games.getNextData().size());
    	System.out.println(games.getNextData().size());
    	System.out.println(games.getPrevData().size());
    	System.out.println(games.getPrevData().size());
    	System.out.println(games.getPrevData().size());
    	System.out.println(games.getPrevData().size());
    	System.out.println(games.getPrevData().size());
    	System.out.println("MUST BE ZERO: " + games.getPrevData().size());
    	
    	

    	client.closeConnection();
    	
    }
    


}
