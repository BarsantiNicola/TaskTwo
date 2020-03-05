package logic.data;

import java.util.List;

public class Game extends PreviewGame{
	
	private String description;
	private String steamURL;
	private String nintendoURL;
	private String playstationURL;
	private String xboxURL;
	private String genre;
	private String developer;
	private String releaseDate;
	private double metacriticScore;
	private List<String> videoURLs;
	
	Game( String gameTitle , String pic ){
		
		super( gameTitle , pic );
		
	}

	public String getDescription() {
		
		return description;
	}
	
	public String getSteamURL() {
		
		return steamURL;
	}
	
	public String getNintendoURL() {
		
		return nintendoURL;
	}
	
	public String getPlaystationURL() {
		
		return playstationURL;
	}
	
	public String getXboxURL() {
		
		return xboxURL;
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
	
	public List<String> getVideoURLs() {
		
		return videoURLs;
	}
}
