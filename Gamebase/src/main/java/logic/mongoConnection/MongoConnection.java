package logic.mongoConnection;

import logic.data.Game;
import com.mongodb.MongoClient;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;


public class MongoConnection {
    
	private MongoClient mongoClient;    
    private MongoCollection<Game> gamesCollection; 
    
    public MongoConnection(){
    	
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    	mongoClient = new MongoClient( new ServerAddress("127.0.0.1" , 27017) , MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
        gamesCollection = mongoClient.getDatabase("myDb2").getCollection("games",Game.class); 
    
    }
    
    public Game getGame(String title) {
    	
    	return gamesCollection.find(eq("title",title)).first();
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

    
    public static void main(String[] args) {
    	
    	System.out.println("------  [Mongo Connection test]  ------");
    	MongoConnection client = new MongoConnection();
    	System.out.println(client.voteGame("BioShock", 5));
    	System.out.println(client.getGame("BioShock"));
    	System.out.println(client.addGameDescription("BioShock", "Andiamocene a Newtopia"));

    	System.out.println(client.getGamePics("BioShock"));
    	System.out.println(client.getTotalGamesCount());
    	System.out.println(client.getGenres());
    	client.closeConnection();
    	
    }
    


}
