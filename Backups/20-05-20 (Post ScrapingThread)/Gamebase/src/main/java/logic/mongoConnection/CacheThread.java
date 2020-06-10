package logic.mongoConnection;

public class CacheThread implements Runnable{

	private final MongoStatistics statistics;
	private final CachingPolicy policy;
	
	CacheThread( MongoStatistics statistics ,CachingPolicy policy ){
		this.statistics = statistics;
		this.policy = policy;
	}
	
	public void run() {

    	statistics.setCache(0,statistics.getMaxRatedGameByYear().element);
 
    	statistics.setCache(1,statistics.getMaxViewedGameByYear().element);
    	
    	statistics.setCache(2,statistics.getMaxRatedGameByGen().element);

    	statistics.setCache(3,statistics.getMaxViewedGameByGen().element);
    	
    	if( policy == CachingPolicy.RESTRICT ) return;
    	
    	statistics.setCache(4,statistics.getGamesCountByGen().element);
    	
    	statistics.setCache(5,statistics.getGamesCountByYear().element);
    	
    	statistics.setCache(6,statistics.getGamesCountByYearGen().element);

    	statistics.setCache(7,statistics.getViewsCountByYear().element);
    	
    	statistics.setCache(8,statistics.getViewsCountByGen().element);
    	
    	statistics.setCache(9,statistics.getViewsCountByYearGen().element);
    	
    	statistics.setCache(10,statistics.getRatingsCountByYear().element);
    	
    	statistics.setCache(11,statistics.getRatingsCountByGen().element);
    	
    	statistics.setCache(12,statistics.getRatingsCountByYearGen().element);
    	
	}

}
