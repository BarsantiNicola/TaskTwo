package scraping;

import java.io.IOException;

public class WebScraping {

	
	//Update database using dynamic scraping (NEED TEST)
	public boolean updateDatabase() { 
		
		int NGames = getGameCount();
		
		
		return false; }
	
	//Get URL of the Twitch channel currently having the higher number of views for a game
	public static String getTwitchURLChannel( String GAME ) { 
		
		//String for return
		String twitchChannel = null;
		//Create http object for request
		 HttpClient objRequest = new HttpClient();
		 
		 try {
	            System.out.println("Testing 1 - Send Http GET request");
	            try {
					twitchChannel = objRequest.sendGetTwitch(GAME);
				} catch (Exception e) {
					e.printStackTrace();
				}

	        } finally {
	            try {
					objRequest.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		
		
		return twitchChannel; 
	}
	
	//Get description of a given Game (Needs Game_id)
	public static String getGameDescription(int GAME_ID ) { 
		//String for return
		String gameDescription = null;
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		
		 try {
	            System.out.println("Testing 1 - Send Http GET request");
	            try {
					gameDescription = objRequest.sendGetGameDescription(GAME_ID);
				} catch (Exception e) {
					e.printStackTrace();
				}

	        } finally {
	            try {
					objRequest.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		
		return gameDescription; 
		
	
	}
	

	
	
	//Main per fare prove
	 public static void main(String[] args) throws Exception {
		 		
	 }
}
