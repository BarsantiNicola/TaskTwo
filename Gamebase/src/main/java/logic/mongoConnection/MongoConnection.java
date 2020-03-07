package logic.mongoConnection;

import java.util.List;
import logic.data.Game;
import logic.data.PreviewGame;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import java.util.ArrayList;
import java.text.NumberFormat;

import com.mongodb.BasicDBObject;
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
    	mongoClient = new MongoClient( new ServerAddress("172.16.0.80" , 27018) , MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        gamesCollection = mongoClient.getDatabase("myDb").getCollection("games",Game.class); 
    
    }
    
    public Game getGame(String title) {
    	
    	Game game = gamesCollection.find(eq("title",title)).first();
    	if( game == null ) return null;
    	game.setViewsCount(game.getViewsCount()+1);
    	UpdateResult res = gamesCollection.updateOne(and(eq("_id",game.getId()),eq("title", title)), new Document("$set", new Document("viewsCount", game.getViewsCount())));
    	
    	if(res.getMatchedCount() == 0 ){
    		System.out.println("--> [MONGO] Error, game not found");
    		return null;
    	}
    	
    	if(res.getModifiedCount() == 0 ) {
    		System.out.println("--> [MONGO] Error, game not modified, the game has already a description");
    		return game;
    	}
    	return game;
    }
    
    public boolean addGameDescription(String title , String description ) {
    	
    	UpdateResult res = gamesCollection.updateOne(eq("title", title), new Document("$set", new Document("description", description)));
    	
    	if(res.getMatchedCount() == 0 ){
    		System.out.println("--> [MONGO] Error, game not found");
    		return false;
    	}
    	
    	if(res.getModifiedCount() == 0 ) {
    		System.out.println("--> [MONGO] Error, game not modified, the game has already a description");
    		return false;
    	}
    	return true;
    	
    }
    
    public boolean deleteGame(String title) {
    	
    	DeleteResult res = gamesCollection.deleteOne(eq("title",title));
    	if(res.getDeletedCount()==0 ) {
    		System.out.println("--> [MONGO] Error,game not found");
    		return false;
    	}
    	return true;
    }
    
    public boolean voteGame(String title, int vote ) {
    	Game game = getGame(title);
    	if( vote < 0 || vote > 5) {
    		System.out.println( "-- [MONGO] Error, the vote must be in [0,5]" );
    		return false;
    	}
    	
    	if( game == null ) {
    		System.out.println( "--> [MONGO] Error, game not found" );
    		return false;
    	}
    	int count = game.getRatingCount();
    	NumberFormat nf = new DecimalFormat("0.0000");
        double newRate = Double.parseDouble(nf.format((game.getRating()*count + vote)/(count+1)).replace(",", "."));
    	count++;
    	UpdateResult res = gamesCollection.updateOne(eq("title", title), new Document("$set", new Document("rating", newRate).append("ratingCount", count )));
    	
    	if(res.getMatchedCount() == 0 ){
    		System.out.println("--> [MONGO] Error, game not found");
    		return false;
    	}
    	
    	if(res.getModifiedCount() == 0 ) {
    		System.out.println("--> [MONGO] Error, game not modified, the game has already a description");
    		return false;
    	}
    	return true;
    	
    }
    
    public List<String> getGamePics( String title ){
    	
    	Game game = getGame(title);
    	if( game == null ) {
    		System.out.println("--> [MONGO][GETGAMEPICS] Error, game not found" );
    		return new ArrayList<>();
    	}
    	if( game.getMultimedia() == null ) 
    		return new ArrayList<>();
    		
    	return game.getMultimedia().getImages(); 
    	
    }
    
    public long getTotalGamesCount() {
    	
    	return gamesCollection.countDocuments();
    }
    
    public List<String> getGenres(){
    	MongoCursor<String> genres = gamesCollection.distinct("genres", String.class).iterator();
    	List<String> retList = new ArrayList<>();
    	while(genres.hasNext())
    		retList.add(genres.next());
    	return retList;
    }
    
    public void closeConnection() {
    	this.mongoClient.close();
    }
    
    public PreviewGame getMostViewedPreview(){
    	
    		Game game = gamesCollection.find().sort(Sorts.descending("viewsCount")).projection(Projections.include("title","background_image")).first();
    		if( game == null ) return null;
    		else
    			return game.getPreview();
    	
    }
    
    public PreviewGame getMostPopularPreview(){
    	
		Game game = gamesCollection.find().sort(Sorts.descending("ratingCount")).projection(Projections.include("title","background_image")).first();
		if( game == null ) return null;
		else
			return game.getPreview();
	
    }
    
    public List<PreviewGame> getMostViewedPreviews(){
    	
    	List<PreviewGame> previews = new ArrayList<>();
    	MongoCursor<Game> games = gamesCollection.find().sort(Sorts.descending("viewsCount")).projection(Projections.include("title","background_image")).iterator();
    	while(games.hasNext())
    		previews.add(games.next().getPreview());
    	return previews;
    	
    }
    
    public List<PreviewGame> getMostLikedPreviews(){
    	
    	List<PreviewGame> previews = new ArrayList<>();
    	MongoCursor<Game> games = gamesCollection.find().sort(Sorts.descending("rating")).projection(Projections.include("title","background_image")).iterator();
    	while(games.hasNext())
    		previews.add(games.next().getPreview());
    	return previews;
    	
    }
    
    public List<PreviewGame> getMostRecentPreviews(){
    	
    	List<PreviewGame> previews = new ArrayList<>();
    	MongoCursor<Game> games = gamesCollection.find().sort(Sorts.descending("_id")).projection(Projections.include("title","background_image")).iterator();
    	while(games.hasNext())
    		previews.add(games.next().getPreview());
    	return previews;
    	
    }
    
    public List<PreviewGame> searchGames(String search ){
    	
    	List<PreviewGame> previews = new ArrayList<>();
    	MongoCursor<Game> games = gamesCollection.find(or(eq("title" , search ) , elemMatch("genres", eq(search)))).projection(Projections.include("title","background_image")).iterator();
    	while(games.hasNext())
    		previews.add(games.next().getPreview());
    	return previews;
    	
    }

    
    public static void main(String[] args) {
    	
    	System.out.println("------  [Mongo Connection test]  ------");
    	MongoConnection client = new MongoConnection();
    	//System.out.println(client.voteGame("BioShock", 5));
    	System.out.println(client.getGame("BioShock"));
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
