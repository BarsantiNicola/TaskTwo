package logic.data;


public class Statistics{
	
	private final Integer year;
	private final Integer viewsCount;
	private final String games;
	private final Double rate;

	public Statistics( Integer year , Integer stat , String games, Double stat2){
		this.year = year;
		this.viewsCount = stat;
		this.games = games;
		this.rate = stat2;
	}
	
	public Integer getYear(){ 
		return year;
	}
	
	public Integer getViewsCount() { return viewsCount; }
	
	public Double getRating() { return rate; }
	
	public String getGames(){ return games; }
	
}
