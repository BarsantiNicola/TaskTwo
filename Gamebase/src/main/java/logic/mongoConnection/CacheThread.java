package logic.mongoConnection;

public class CacheThread implements Runnable{

	private final MongoStatistics statistics;
	
	CacheThread( MongoStatistics statistics ){
		this.statistics = statistics;
	}
	
	public void run() {

    	statistics.setCache(0,statistics.getMaxRatedGameByYear().element);
 
    	statistics.setCache(1,statistics.getMaxViewedGameByYear().element);
    	
    	statistics.setCache(2,statistics.getMaxRatedGameByGen().element);

    	statistics.setCache(3,statistics.getMaxViewedGameByGen().element);
    	
    	statistics.setCache(4,statistics.getGamesCountByGen().element);
    	
    	statistics.setCache(5,statistics.getGamesCountByYear().element);
    	
    	statistics.setCache(6,statistics.getGamesCountByYearGen().element);

    	statistics.setCache(7,statistics.getViewsCountByYear().element);
    	

    	statistics.setCache(8,statistics.getViewsCountByGen().element);
    	
    	statistics.setCache(9,statistics.getRatingsCountByYear().element);
    	
    	statistics.setCache(10,statistics.getRatingsCountByGen().element);
    	
	}

}
