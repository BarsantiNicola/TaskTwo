package scraping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class WebScraping {

	
	//Update database using dynamic scraping (NEED TEST)
	public static boolean updateDatabase() { 
		
		int NPage = getGameCount()/40;
		if (NPage < 3750 )
			NPage = 3750;
		
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		String newGames = null;
		
		try {
	           System.out.println("WEBSCRAPING/GETTWITCHURLCHANNEL-->Sending Http GET request for Twitch channel");
	           try {
				newGames = objRequest.sendGetNewGames(NPage);
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
		
		//Save on file
		try (PrintWriter out = new PrintWriter("src/main/java/resources/New_games.txt")) {
		    out.println(newGames);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("WEBSCRAPING/UPDATEDATABASE-->New games saved on file");
		
		return false; 
	}
	
	
	//Get URL of the Twitch channel currently having the higher number of views for a game
	public static String getTwitchURLChannel( String GAME ) { 
		System.out.println("WEBSCRAPING/GETTWITCHURLCHANNEL--> Getting Twitch channel for game " + GAME);
		
		String twitchChannel = null;
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		 
		try {
	           System.out.println("WEBSCRAPING/GETTWITCHURLCHANNEL-->Sending Http GET request for Twitch channel");
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
		
		System.out.println("WEBSCRAPING/GETTWITCHURLCHANNEL--> Twitch channel obtained. Returning URL: " + twitchChannel);
		return twitchChannel; 
	}
	
	
	//Get description of a given Game (Needs Game_id)
	public static String getGameDescription(int GAME_ID ) { 
		System.out.println("WEBSCRAPING/GETGAMEDESCRIPTION--> Getting Description for game_id = " + GAME_ID);
		
		String gameDescription = null;
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		
		 try {
	            System.out.println("WEBSCRAPING/GETGAMEDESCRIPTION-->Sending Http GET request for GameDescription");
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
		
		 System.out.println("WEBSCRAPING/GETGAMEDESCRIPTION--> Returning Description: " + gameDescription);
		return gameDescription; 
	}
	

	
	
	//Main per fare prove
	 public static void main(String[] args) throws Exception {
		 updateDatabase();
	 }
}
