package logic.data;

public class Game extends PreviewGame{
	
	private String description;
	
	private String steamURL;
	
	private String originURL;
	
	private String playstationURL;
	
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
	
}
