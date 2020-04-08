package scraping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class WebScraping {

	
	//Update database using dynamic scraping (NEED TEST)
	public static boolean updateDatabase() { 
		
		List<JSONObject> newGames = new ArrayList<JSONObject>();
		
		int newGameID=1;
	
		searchNewGame(newGameID);

		//Save on file
		try (PrintWriter out = new PrintWriter("src/main/java/resources/New_games.txt")) {
		    out.println(newGames);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("WEBSCRAPING/UPDATEDATABASE-->New game saved on file");
		
		return false; 
	}
	
	
	//Search new game
	public static JSONObject searchNewGame(int ID_GAME) {
		
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		JSONObject newGame = new JSONObject();
			
		try {
	           System.out.println("WEBSCRAPING/SEARCHNEWGAME-->Sending Http GET request for new game. ID:" + ID_GAME);
	           try {
				newGame = objRequest.sendGetNewGame(ID_GAME);
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
		
		System.out.println("WEBSCRAPING/SEARCHNEWGAME-->New game obtained:" + newGame);
		
		return newGame;
	}
	
	
	//Get URL of the Twitch channel currently having the higher number of views for a game
	public static String getTwitchURLChannel( String GAME ) { 
		System.out.println("WEBSCRAPING/GETTWITCHURLCHANNEL--> Getting Twitch channel for game " + GAME);
		
		//Replace spaces in the game title
		GAME = GAME.replaceAll(" ", "%20");
		
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
	
	
	
	public static void getBackgroundImage(String GAME) {
		
		//Replace spaces in the game title
		GAME = GAME.replaceAll(" ", "%20");
		
		HttpClient objRequest = new HttpClient();
		 try {
	            System.out.println("WEBSCRAPING/GETBACKGROUNDIMAGE-->Sending Http GET request for image");
	            try {
					objRequest.sendGetGameBackgroundImage(GAME);
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
		
		 System.out.println("WEBSCRAPING/GETBACKGROUNDIMAGE--> Returning image");
	}

	
	
	//Main per fare prove
	 public static void main(String[] args) throws Exception {
		 getBackgroundImage("Dark Souls");
		 
	 }
	 
}
