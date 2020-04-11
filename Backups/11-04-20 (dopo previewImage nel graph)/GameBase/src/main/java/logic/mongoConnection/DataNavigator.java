package logic.mongoConnection;

import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import logic.StatusCode;
import logic.data.StatusObject;
import logic.data.Game;
import logic.data.PreviewGame;

public class DataNavigator{
	
	private final MongoCollection<Game> data;
	private final int numGames;
	private int position;
	private final NavType queryType;
	private final Bson like;
	private final Bson projection;
	private final Bson idSort;
	private final Bson ratingSort;
	private final Bson viewsSort;
	private NavElem cache;
	private int cacheSize;
	private final int MAX_CACHE=50;
	private final int CACHE_RESIZE = 25;
	
	DataNavigator( MongoCollection<Game> data , int maxNumGames , NavType type , String search ){
		
		this.data = data;
		this.numGames = maxNumGames;
		this.queryType = type;
		this.position = 0;
		this.projection = Projections.include( "_id", "title", "background_image" );
		this.idSort = Sorts.descending( "_id" );
		this.ratingSort = Sorts.descending("rating");
		this.viewsSort = Sorts.descending( "viewsCount" );
		this.like = or(regex("title" , ".*"+search+".*","i") , regex("genres", ".*"+search+".*","i"));
		this.cache = null;
		this.cacheSize = 0;
		
	}
	
	public StatusObject<List<PreviewGame>> getNextData(){
		
		List<PreviewGame> previews = new ArrayList<>();
		MongoCursor<Game> games;
		
		if( cache != null && cache.hasNext()) {
			cache = cache.getNext();
			position += numGames;
			return new StatusObject<List<PreviewGame>>(StatusCode.OK, cache.getData());
			
		}
		
		try {
			switch(queryType) {
		
				case PREVIEW_LIKED:
					games = data.find().sort(ratingSort).projection(projection).skip(position).limit(numGames).iterator();
					position += numGames;
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
				case PREVIEW_VIEWED:
					games = data.find().sort(viewsSort).projection(projection).skip(position).limit(numGames).iterator();
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
					games = data.find(like).projection(projection).skip(position).limit(numGames).iterator();
					position += numGames;
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
	
			}
			
			if( this.cache == null ) 
				
				this.cache = new NavElem(previews);
				
			else
				if( this.cache.hasNext() == false ) {
					this.cache.setNext(new NavElem(previews));
					this.cache.getNext().setPrev(this.cache);
					this.cache = this.cache.getNext();
				}
			
			this.cacheSize++;
			if( cacheSize> MAX_CACHE )
				dropCache();
			
			return new StatusObject<List<PreviewGame>>( StatusCode.OK , previews );
			
		}catch( Exception e ) {
			System.out.println("--->[DataNavigator][GetNextData] Error, network unreachable" );
			return new StatusObject<List<PreviewGame>>(StatusCode.ERR_NETWORK_PARTIAL_UNREACHABLE, previews );
			
		}
		
	}
	
	public StatusObject<List<PreviewGame>> getPrevData(){
		
		List<PreviewGame> previews = new ArrayList<>();
		if( position == 0 || position == numGames ) 
			return new StatusObject<List<PreviewGame>>(StatusCode.ERR_DOCUMENT_MIN_INDEX_REACHED , null);

		if( cache != null && cache.hasPrev()) {
			cache = cache.getPrev();
			position-=numGames;
			return new StatusObject<List<PreviewGame>>(StatusCode.OK, cache.getData());
			
		}
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
					games = data.find(like).projection(projection).skip(position).limit(numGames).iterator();
					while(games.hasNext())
						previews.add(games.next().getPreview());
					break;
			
			}
			
			if( this.cache == null )
				this.cache = new NavElem(previews);
			else 
				if( this.cache.hasPrev() == false ) {
					this.cache.setPrev(new NavElem(previews));
					this.cache.getPrev().setNext(this.cache);
					this.cache = this.cache.getPrev();
				}
			this.cacheSize++;
			
			if( cacheSize> MAX_CACHE )
				dropCache();
			
			return new StatusObject<List<PreviewGame>>(StatusCode.OK,previews);
			
		}catch( Exception e ) {

			System.out.println("--->[DataNavigator][GetNextData] Error, network unreachable" );
			return new StatusObject<List<PreviewGame>>(StatusCode.ERR_NETWORK_PARTIAL_UNREACHABLE, previews );
			
		}
	}
	
	private void dropCache() {
		
		NavElem leftApp = this.cache;
		NavElem rightApp = this.cache;
		int leftElems = 0,rightElems = 0;
		
		if( this.cache == null ) return;

		if( cacheSize>MAX_CACHE && MAX_CACHE > CACHE_RESIZE ){

			while( rightApp.hasNext()) { 
				rightApp = rightApp.getNext();
				rightElems++;
			}
			while( leftApp.hasPrev()) {
				leftApp = leftApp.getPrev();
				leftElems++;
			}

			if( rightElems > leftElems ) {
				
				for( int a = 0; a<CACHE_RESIZE;a++) 
					rightApp = rightApp.getPrev();
				rightApp.setNext(null);
			}else {
				for( int a = 0; a<CACHE_RESIZE;a++) 
					leftApp = leftApp.getNext();

				leftApp.setPrev(null);
			}
			this.cacheSize-=CACHE_RESIZE;
		}
	}
	
	public static void main(String[] args) {
		MongoConnection client = null;
		int[] testBehavior = {0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1};
		boolean[] results = {true,true,true,false,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
		
		try {
			client = new MongoConnection("172.16.0.80",27018);
		}catch(Exception e) {
			System.out.println("Error trying to connecto to Mongo");
			return;
		}
		System.out.println("------- DataNavigator Test -------");
		
		DataNavigator nav = client.getMostLikedPreviews().element;
		List<PreviewGame> app;
		if( nav == null ) {
			System.out.println("Error trying to get navigator");
			return;
		}
		for( int a=0; a<testBehavior.length;a++) {
			if( testBehavior[a] == 0  ) {
				app = nav.getNextData().element;
				if(( app == null && results[a] == true )|| ( app != null && results[a] == false )){
					System.out.println("Error in the data navigator at " +a+" trial");
					return;
				}else {
					if( app != null ){
						System.out.println("POSITION: " + nav.position);
						for( PreviewGame game : app )
							System.out.println("Title: " + game.getTitle() + " Id: " + game.getId() + " Pic: " +game.getPreviewPicURL() );
					}
				}
			}
			if( testBehavior[a] == 1 ) {
				app = nav.getPrevData().element;
				if(( app == null && results[a] == true )|| ( app != null && results[a] == false )){
					System.out.println("Error in the data navigator at " +a+" trial");
					return;
				}else {
					if( app != null ) {
						System.out.println("POSITION: " + nav.position);
						for( PreviewGame game : app )
							System.out.println("Title: " + game.getTitle() + " Id: " + game.getId() + " Pic: " +game.getPreviewPicURL() );
					}
				}
			}
		}
		System.out.println("Test correctly executed");
		
		
	
	}
	
	

}
