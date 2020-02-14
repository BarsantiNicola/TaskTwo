package logic.data;

public class Game extends PreviewGame{
	
	private String description;
	
	Game( String gameTitle , String pic ){
		
		super( gameTitle , pic );
		
	}

	public String getDescription() {
		
		return description;
	}
	
}
