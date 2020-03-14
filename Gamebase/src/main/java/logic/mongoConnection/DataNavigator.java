package logic.mongoConnection;

import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import logic.StatusCode;
import logic.StatusObject;
import logic.data.Game;
import logic.data.PreviewGame;

public class DataNavigator{
	
	private final MongoCollection<Game> data;
	private final int numGames;
	private int position;
	private final NavType queryType;
	private final String optString;
	private final Bson projection;
	private final Bson idSort;
	private final Bson ratingSort;
	private final Bson viewsSort;
	
	DataNavigator( MongoCollection<Game> data , int maxNumGames , NavType type , String search ){
		
		this.data = data;
		this.numGames = maxNumGames;
		this.queryType = type;
		this.position = 0;
		this.optString = search;
		this.projection = Projections.include( "_id", "title", "background_image" );
		this.idSort = Sorts.descending( "_id" );
		this.ratingSort = Sorts.descending("rating");
		this.viewsSort = Sorts.descending( "viewsCount" );
		
	}
	
	public StatusObject<List<PreviewGame>> getNextData(){
		
		List<PreviewGame> previews = new ArrayList<>();
		MongoCursor<Game> games;
		
		try {
			switch(queryType) {
		
				case PREVIEW_LIKED:
					games = data.find().sort(idSort).projection(projection).skip(position).limit(numGames).iterator();
					position += numGames;
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
				case PREVIEW_VIEWED:
					games = data.find().sort(idSort).projection(projection).skip(position).limit(numGames).iterator();
					position += numGames;
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
				case PREVIEW_RECENT:
					games = data.find().sort(idSort).projection(projection).skip(position).limit(numGames).iterator();
					position += numGames;
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
				case PREVIEW_SEARCH:
					games = data.find(or(eq("title" , optString) , elemMatch("genres", eq(optString)))).projection(projection).skip(position).limit(numGames).iterator();
					position += numGames;
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
	
			}
			
			return new StatusObject<List<PreviewGame>>( StatusCode.OK , previews );
			
		}catch( Exception e ) {
			
			System.out.println("--->[DataNavigator][GetNextData] Error, network unreachable" );
			return new StatusObject<List<PreviewGame>>(StatusCode.ERR_NETWORK_PARTIAL_UNREACHABLE, previews );
			
		}
		
	}
	
	public StatusObject<List<PreviewGame>> getPrevData(){
		
		List<PreviewGame> previews = new ArrayList<>();
		if( position == 0 ) 
			return new StatusObject<List<PreviewGame>>(StatusCode.ERR_DOCUMENT_MIN_INDEX_REACHED , null);
		
		MongoCursor<Game> games;
		try {
			switch(queryType) {
				case PREVIEW_LIKED:
					position -= numGames;
					games = data.find().sort(ratingSort).projection(projection).skip(position).limit(numGames).iterator();
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
				case PREVIEW_VIEWED:
					position -= numGames;
					games = data.find().sort(viewsSort).projection(projection).skip(position).limit(numGames).iterator();
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
				case PREVIEW_RECENT:
					position -= numGames;
					games = data.find().sort(idSort).projection(projection).skip(position).limit(numGames).iterator();
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
				case PREVIEW_SEARCH:
					position -= numGames;
					games = data.find(or(eq("title" , optString) , elemMatch("genres", eq(optString)))).projection(projection).skip(position).limit(numGames).iterator();
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
			
			}
			
			return new StatusObject<List<PreviewGame>>(StatusCode.OK,previews);
			
		}catch( Exception e ) {
			
			System.out.println("--->[DataNavigator][GetNextData] Error, network unreachable" );
			return new StatusObject<List<PreviewGame>>(StatusCode.ERR_NETWORK_PARTIAL_UNREACHABLE, previews );
			
		}
	}
	
	

}