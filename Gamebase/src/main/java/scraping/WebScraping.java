package scraping;

import java.io.IOException;

public class WebScraping {

	public boolean updateDatabase() { return false; }
	
	//Get URL of the Twitch channel currently having the higher number of views for a game
	public static String getTwitchURLChannel( String GAME ) { 
		
		//String for return
		String twitchChannel = null;
		//Create http object for request
		 HttpClient objRequest = new HttpClient();
		 
		 try {
	            System.out.println("Testing 1 - Send Http GET request");
	            try {
					twitchChannel = objRequest.sendGet(GAME);
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
		
		
		return twitchChannel; }
	
	public String getGameDescription( String GAME ) { return null; }
	
	
	 public static void main(String[] args) throws Exception {
		 getTwitchURLChannel("Overwatch");
		 
	 }
}
