package logic.data;

public class Game extends PreviewGame{
	
	private String description;
	private String steamURL;
	private String originURL;
	private String playstationURL;
	private String genre;
	private String developer;
	private String releaseDate;
	public double metacriticScore;
	
	Game( String gameTitle , String pic ){
		
		super( gameTitle , pic );
		
	}

	public String getDescription() {
		
		return description;
	}
	
	public String getSteamURL() {
		
		return steamURL;
	}
	
	public String getOriginURL() {
		
		return originURL;
	}
	
	public String getPlaystationURL() {
		
		return playstationURL;
	}
	
	public String getGenre() {
		
		return genre;
	}
	
	public String getDeveloper() {
		
		return developer;
	}
	
	public String releaseDate() {
		
		return releaseDate;
	}
	
	public double getMetacriticScore() {
		
		return metacriticScore;
	}
}
