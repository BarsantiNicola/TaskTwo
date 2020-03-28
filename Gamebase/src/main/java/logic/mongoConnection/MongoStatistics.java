package logic.mongoConnection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import logic.StatusCode;
import logic.data.Statistics;
import logic.data.StatusObject;

public class MongoStatistics {
	
    private final MongoCollection<Document> statisticsCollection;
    private final Object[] cache;
    
	MongoStatistics( MongoConnection connection , com.mongodb.MongoClient mongoClient ) throws Exception{
		
		this.statisticsCollection = mongoClient.getDatabase("myDb").getCollection("games",Document.class);
		this.cache= new Object[11];
		for( int a= 0; a<11;a++)
			cache[a] = null;
		new Thread(new CacheThread(this)).start();
		
		
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public StatusObject<List<Statistics>> getMaxRatedGameByYear(){
		
		if( cache[0] != null )
			return new StatusObject<List<Statistics>>(StatusCode.OK,(List<Statistics>)cache[0]);
		
		BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year",  new BasicDBObject("$year", "$releaseDate")));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$first" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.rating", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("rating",-1));
		         
		List<Statistics> result = new ArrayList<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		
		try {
			
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][getMaxRatedGameByYear] Error, network unreachable" );
			return new StatusObject<List<Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}
	
		while( res.hasNext()) {
			
			data = res.next();

			try {
				
				if(data.get("_id",Document.class).getInteger("year") != null )
					result.add(new Statistics(df1.parse(String.valueOf(data.get("_id",Document.class).getInteger("year"))).getYear(), 
						null,
						data.get("max",Document.class).getString("title"),data.get("max",Document.class).getDouble("rating")));

			}catch(Exception e) {}
		
		}

		return new StatusObject<List<Statistics>>(StatusCode.OK,result);
	
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public StatusObject<List<Statistics>> getMaxViewedGameByYear(){
		
		if( cache[1] != null )
			return new StatusObject<List<Statistics>>(StatusCode.OK,(List<Statistics>)cache[1]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year",  new BasicDBObject("$year", "$releaseDate")));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$first" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.viewsCount", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("viewsCount",-1));
		         
		List<Statistics> result = new ArrayList<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");

		try {
			
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();

		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][getMaxViewedGameByYear] Error, network unreachable" );
			return new StatusObject<List<Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}
		
		while( res.hasNext()) {
			
			data = res.next();

			try {
				if(data.get("_id",Document.class).getInteger("year") != null )
					result.add(new Statistics(df1.parse(String.valueOf(data.get("_id",Document.class).getInteger("year"))).getYear(), 
						data.get("max",Document.class).getInteger("viewsCount"),
						data.get("max",Document.class).getString("title"),null));

			}catch(Exception e) {}
		
		}

		return new StatusObject<List<Statistics>>(StatusCode.OK,result);
		
	}
	
	@SuppressWarnings("unchecked")
	public StatusObject<HashMap<String,Statistics>> getMaxRatedGameByGen(){

		if( cache[2] != null )
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.OK,(HashMap<String,Statistics>)cache[2]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("generes", "$genres"));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$first" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.rating", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("rating",-1));

		MongoCursor<Document> res = null;
		Document data;
		HashMap<String,Statistics> result = new HashMap<>();
		
		try {
			
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();

		}catch(Exception e ) {
			
			e.printStackTrace();
			System.out.println("---> [MongoConnection][getMaxRatedGameByGen] Error, network unreachable" );
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}
	
		while( res.hasNext()) {
			
			data = res.next();
			

			try {
				if(data.get("_id",Document.class).getString("generes") != null )
					result.put(data.get("_id",Document.class).getString("generes"), new Statistics(null,null,data.get("max",Document.class).getString("title"),data.get("max",Document.class).getDouble("rating")));

			}catch(Exception e) {
				e.printStackTrace();
			}
		
		}

		return new StatusObject<HashMap<String,Statistics>>(StatusCode.OK,result);	
	}
	
	@SuppressWarnings("unchecked")
	public StatusObject<HashMap<String,Statistics>> getMaxViewedGameByGen(){
		
		if( cache[3] != null )
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.OK,(HashMap<String,Statistics>)cache[3]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("generes", "$genres"));
	    BasicDBObject groupFields = group_id.append("max", new BasicDBObject("$first" , "$$ROOT"));
	    BasicDBObject project = new BasicDBObject("$project" , new BasicDBObject("max.title",1).append("max.viewsCount", 1));
	    
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));
		BasicDBObject iniSort = new BasicDBObject("$sort" , new BasicDBObject("viewsCount",-1));

		MongoCursor<Document> res = null;
		Document data;
		HashMap<String,Statistics> result = new HashMap<>();
		
		try {
			
			res = statisticsCollection.aggregate(Arrays.asList(iniSort,group,project,sort)).iterator();

		}catch(Exception e ) {
			
			e.printStackTrace();
			System.out.println("---> [MongoConnection][getMaxViewedGameByGen] Error, network unreachable" );
			return new StatusObject<HashMap<String,Statistics>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}
		
		while( res.hasNext()) {
			
			data = res.next();
			

			try {
				if(data.get("_id",Document.class).getString("generes") != null )
					result.put(data.get("_id",Document.class).getString("generes"), new Statistics(null,data.get("max",Document.class).getInteger("viewsCount"),data.get("max",Document.class).getString("title"),null));

			}catch(Exception e) {
				e.printStackTrace();
			}
		
		}

		return new StatusObject<HashMap<String,Statistics>>(StatusCode.OK,result);	
	}
	
	@SuppressWarnings({ "unchecked" })
	public StatusObject<HashMap<String,Integer>> getGamesCountByGen(){
	
		if( cache[4] != null )
			return new StatusObject<HashMap<String,Integer>>(StatusCode.OK,(HashMap<String,Integer>)cache[4]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", "$genres" );
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , 1));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<String,Integer> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		
		try {

			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][getGamesCountByGen] Error, network unreachable" );
			return new StatusObject<HashMap<String,Integer>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		while( res.hasNext()) {
			
			data = res.next();
			try {
				if(data.getString("_id")!=null)
					result.put(data.getString("_id"), data.getInteger("count"));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<String,Integer>>(StatusCode.OK,result);
		
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public StatusObject<HashMap<Integer,Integer>>  getGamesCountByYear(){
	
		if( cache[5] != null )
			return new StatusObject<HashMap<Integer,Integer>>(StatusCode.OK,(HashMap<Integer,Integer>)cache[5]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", "$releaseDate")));
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
			
			System.out.println("---> [MongoConnection][getGamesCountByYear] Error, network unreachable" );
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
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public StatusObject<HashMap<Integer,HashMap<String,Integer>>> getGamesCountByYearGen(){

		if( cache[6] != null )
			return new StatusObject<HashMap<Integer,HashMap<String,Integer>>>(StatusCode.OK,(HashMap<Integer,HashMap<String,Integer>>)cache[6]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", "$releaseDate")).append("generes", "$genres"));
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

			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			e.printStackTrace();
			System.out.println("---> [MongoConnection][getGamesCountByYearGen] Error, network unreachable" );
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
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public StatusObject<HashMap<Integer,Integer>>  getViewsCountByYear(){
	
		if( cache[7] != null )
			return new StatusObject<HashMap<Integer,Integer>>(StatusCode.OK,(HashMap<Integer,Integer>)cache[7]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", "$releaseDate")));
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , "$viewsCount"));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<Integer,Integer> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		
		try {

			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][getViewsCountByYear] Error, network unreachable" );
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
	
	@SuppressWarnings({ "unchecked" })
	public StatusObject<HashMap<String,Integer>> getViewsCountByGen(){
	
		if( cache[8] != null )
			return new StatusObject<HashMap<String,Integer>>(StatusCode.OK,(HashMap<String,Integer>)cache[8]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", "$genres" );
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , "$viewsCount"));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<String,Integer> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		
		try {

			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][getViewsCountByGen] Error, network unreachable" );
			return new StatusObject<HashMap<String,Integer>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		while( res.hasNext()) {
			
			data = res.next();
			try {
				if(data.getString("_id")!=null)
					result.put(data.getString("_id"), data.getInteger("count"));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<String,Integer>>(StatusCode.OK,result);
		
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public StatusObject<HashMap<Integer,Integer>>  getRatingsCountByYear(){
	
		if( cache[9] != null )
			return new StatusObject<HashMap<Integer,Integer>>(StatusCode.OK,(HashMap<Integer,Integer>)cache[9]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", new BasicDBObject("year", new BasicDBObject("$year", "$releaseDate")));
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , "$ratingCount"));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<Integer,Integer> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		DateFormat df1 = new SimpleDateFormat("yyyy");
		
		try {
			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][getRatingsCountByYear] Error, network unreachable" );
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
	
	@SuppressWarnings({ "unchecked" })
	public StatusObject<HashMap<String,Integer>> getRatingsCountByGen(){
	
		if( cache[10] != null )
			return new StatusObject<HashMap<String,Integer>>(StatusCode.OK,(HashMap<String,Integer>)cache[10]);
		
	    BasicDBObject group_id = new BasicDBObject("_id", "$genres" );
	    BasicDBObject groupFields = group_id.append("count", new BasicDBObject("$sum" , "$ratingCount"));
	    BasicDBObject group = new BasicDBObject("$group", groupFields);
		BasicDBObject sort = new BasicDBObject("$sort" , new BasicDBObject("_id",-1));

		HashMap<String,Integer> result = new HashMap<>();
		MongoCursor<Document> res = null;
		Document data;
		
		try {

			res = statisticsCollection.aggregate(Arrays.asList(group,sort)).iterator();
			
		}catch(Exception e ) {
			
			System.out.println("---> [MongoConnection][getRatingsCountByGen] Error, network unreachable" );
			return new StatusObject<HashMap<String,Integer>>(StatusCode.ERR_NETWORK_UNREACHABLE, null);
			
		}

		while( res.hasNext()) {
			
			data = res.next();
			try {
				if(data.getString("_id")!=null)
					result.put(data.getString("_id"), data.getInteger("count"));
			
			}catch(Exception e) {}
		
		}

		return new StatusObject<HashMap<String,Integer>>(StatusCode.OK,result);
		
	}
	
    public boolean statsTest() {

    	long timer; 
    	System.out.println("---> [TEST][Statistics]" );
    	StatusObject<List<Statistics>> result;
    	StatusObject<HashMap<String,Statistics>> result2;
    	StatusObject<HashMap<String,Integer>> result3;
    	StatusObject<HashMap<Integer,Integer>> result4;
    	
    	timer = System.currentTimeMillis();
    	result = getMaxRatedGameByYear(); 	
    	if( result.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getMaxRatedGameByYear] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( Statistics s : result.element )
    	    	System.out.println("Year:" + s.getYear() + " Rating: " + s.getRating() + " Title: " + s.getGames());
    	}else
    	    System.out.println("----> [TEST][getMaxRatedGameByYear] RESULT: ERROR" );
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result = getMaxViewedGameByYear(); 	

    	if( result.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getMaxViewedGameByYear] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( Statistics s : result.element )
    	    	System.out.println("Year:" + s.getYear() + " Views: " + s.getViewsCount() + " Title: " + s.getGames());
    	}else
    	    System.out.println("----> [TEST][getMaxViewedGameByYear] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result2 = getMaxRatedGameByGen(); 	
    	if( result2.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getMaxRatedGameByGen] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( String generes : result2.element.keySet())
    	    	System.out.println("Genres:" + generes + " Rate: " + result2.element.get(generes).getRating() + " Title: " + result2.element.get(generes).getGames());
    	}else
    	    System.out.println("----> [TEST][getMaxRatedGameByGen] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	
    	timer = System.currentTimeMillis();
    	result2 = getMaxViewedGameByGen(); 	
    	if( result2.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getMaxViewedGameByGen] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( String generes : result2.element.keySet())
    	    	System.out.println("Genres:" + generes + " Views: " + result2.element.get(generes).getViewsCount() + " Title: " + result2.element.get(generes).getGames());
    	}else
    	    System.out.println("----> [TEST][getMaxViewedGameByGen] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result3 = getGamesCountByGen(); 	
    	if( result3.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getGamesCountByGen] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( String generes : result3.element.keySet())
    	    	System.out.println("Genres:" + generes + " Count: " + result3.element.get(generes));
    	}else
    	    System.out.println("----> [TEST][getGamesCountByGen] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result4 = getGamesCountByYear(); 	
    	if( result4.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getGamesCountByYear] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( Integer year : result4.element.keySet())
    	    	System.out.println("Genres:" + year + " Count: " + result4.element.get(year));
    	}else
    	    System.out.println("----> [TEST][getGamesCountByYear] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result3 = getViewsCountByGen(); 	
    	if( result3.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getViewsCountByGen] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( String generes : result3.element.keySet())
    	    	System.out.println("Genres:" + generes + " Views: " + result3.element.get(generes));
    	}else
    	    System.out.println("----> [TEST][getViewsCountByGen] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result4 = getViewsCountByYear(); 	
    	if( result4.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getViewsCountByYear] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( Integer year : result4.element.keySet())
    	    	System.out.println("Genres:" + year + " Views: " + result4.element.get(year));
    	}else
    	    System.out.println("----> [TEST][getViewsCountByYear] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result3 = getRatingsCountByGen(); 	
    	if( result3.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getRatingsCountByGen] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( String generes : result3.element.keySet())
    	    	System.out.println("Genres:" + generes + " RatingCount: " + result3.element.get(generes));
    	}else
    	    System.out.println("----> [TEST][getRatingsCountByGen] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	timer = System.currentTimeMillis();
    	result4 = getRatingsCountByYear(); 	
    	if( result4.statusCode == StatusCode.OK ){
    	    System.out.println("----> [TEST][getRatingsCountByYear] RESULT: OK" );
    	    System.out.println("RESULT");
    	    for( Integer year : result4.element.keySet())
    	    	System.out.println("Genres:" + year + " RatingCount: " + result4.element.get(year));
    	}else
    	    System.out.println("----> [TEST][getRatingsCountByYear] RESULT: ERROR" );    	
    	System.out.println("----------- TEMPO IMPIEGATO: " + (System.currentTimeMillis()-timer)+" ms");
    	
    	return true;
    }
    
    public void setCache(int pos,Object element) {
    	if( pos >=0 && pos<cache.length)
    		cache[pos] = element;

    }
    
}
