package logic.mongoConnection;

import logic.data.Game;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
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
    	
    	return gamesCollection.find(eq("title",title)).first();
    }
    
    public static void main(String[] args) {
    	MongoConnection client = new MongoConnection();
    	System.out.println(client.getGame("BioShock"));
    }
    


}
