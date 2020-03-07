package logic.mongoConnection;

import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import logic.data.Game;
import logic.data.PreviewGame;

public class DataNavigator{
	
	private final MongoCollection<Game> data;
	private final int numGames;
	private int position;
	private final NavType queryType;
	private final String optString;
	
	DataNavigator( MongoCollection<Game> data , int maxNumGames , NavType type , String search ){
		
		this.data = data;
		this.numGames = maxNumGames;
		this.queryType = type;
		this.position = 0;
		this.optString = search;
	}
	
	public List<PreviewGame> getNextData(){
		
		List<PreviewGame> previews = new ArrayList<>();
		MongoCursor<Game> games;
		
		switch(queryType) {
		case PREVIEW_LIKED:
			games = data.find().sort(Sorts.descending("_id")).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
	    	position += numGames;
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
	    	break;
		case PREVIEW_VIEWED:
			games = data.find().sort(Sorts.descending("_id")).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
	    	position += numGames;
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
	    	break;
		case PREVIEW_RECENT:
			games = data.find().sort(Sorts.descending("_id")).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
	    	position += numGames;
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
	    	break;
		case PREVIEW_SEARCH:
			games = data.find(or(eq("title" , optString) , elemMatch("genres", eq(optString)))).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
	    	position += numGames;
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
	    	break;
	
		}
		return previews;
	}
	
	public List<PreviewGame> getPrevData(){
		
		List<PreviewGame> previews = new ArrayList<>();
		if( position == 0 ) return previews;
		MongoCursor<Game> games;
		
		switch(queryType) {
		case PREVIEW_LIKED:
			position -= numGames;
			games = data.find().sort(Sorts.descending("rating")).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
	    	break;
		case PREVIEW_VIEWED:
			position -= numGames;
			games = data.find().sort(Sorts.descending("viewsCount")).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
	    	break;
		case PREVIEW_RECENT:
	    	position -= numGames;
			games = data.find().sort(Sorts.descending("_id")).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
			break;
		case PREVIEW_SEARCH:
	    	position -= numGames;
			games = data.find(or(eq("title" , optString) , elemMatch("genres", eq(optString)))).projection(Projections.include("title","background_image")).skip(position).limit(numGames).iterator();
			while(games.hasNext())
	    		previews.add(games.next().getPreview());
	    	break;
			
		}
		return previews;
	}
	
	

}
