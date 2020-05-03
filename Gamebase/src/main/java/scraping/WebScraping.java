package scraping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import logic.LogicBridge;
import logic.data.Game;
import logic.data.GraphGame;
import logic.mongoConnection.MongoConnection;
import logic.mongoConnection.*;
import logic.graphConnector.*;

//----------------------------------------------------------------------------------------------------
//The class is used to contain all the methods used to perform dynamic web scraping by the application
//A CloseableHttpClient instance is used for all methods in order to perform a GET request
//----------------------------------------------------------------------------------------------------

public class WebScraping {

	
	//Update database using dynamic scraping (NEED TEST)
	public static List<Game> scrapeNewGames(int MaxGameID) { 
		System.out.println("--->[WebScraping] Starting...");
		if (MaxGameID == 0) {
			System.out.println("--->[WebScraping][scrapeNewGames] MaxGameID value is not accettable");
			System.out.println("--->[WebScraping][scrapeNewGames] UpdateDatabase can't be executed. Returning.");
			return null;
		}
		System.out.println("--->[WebScraping][scrapeNewGames] Search for new games");
		List<JSONObject> newGames = new ArrayList<JSONObject>();
		List<Game> gamesToAdd = new ArrayList<Game>();
		
		int i = 0;
		int failed=0;
		while (i < 10) {
			//Wait for polite scraping
			try {
			TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (failed == 100){
				System.out.println("--->[WebScraping][scrapeNewGames] More than 100 attempts failed. Stopping the search for new games...");
				break;
			}
			System.out.println("--->[WebScraping][scrapeNewGames] Search for new game: ID=" + MaxGameID);
			JSONObject newGame = searchNewGame(MaxGameID);
			System.out.println("--->[WebScraping][scrapeNewGames] New game obtained");
			MaxGameID++;
			if(!newGame.has("id") || !newGame.has("name")) {
				System.out.println("--->[WebScraping][scrapeNewGames] Game not suitable");
				failed ++;
				continue;
			}
			i++;
			System.out.println("--->[WebScraping][scrapeNewGames] Game suitable");
			newGames.add(newGame);
		}
		
		System.out.println("--->[WebScraping][scrapeNewGames] New games list:"+ newGames.size());
		/* DEBUG
		for(int j =0; j < newGames.size(); j++) {
			System.out.println(newGames.get(j));
			
		}*/
		
		if (newGames.isEmpty()) {
			return gamesToAdd;
		}
		
		//Create list of objects Game
		System.out.println("--->[WebScraping][scrapeNewGames] Creating objects Game for new games");
		for(int k = 0; k < newGames.size(); k++) {
			
			Game gameToAdd = util.initializeGameToAdd(newGames.get(k));	
			gamesToAdd.add(gameToAdd);
		}		
		
		//Return list of games to add
		return gamesToAdd;
	}
	
	
	
	
	
	//Search new game
	public static JSONObject searchNewGame(int ID_GAME) {
		
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		JSONObject newGame = new JSONObject();
			
		try {
	           System.out.println("--->[WebScraping][searchNewGame] Sending Http GET request for new game. ID:" + ID_GAME);
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
		
		System.out.println("--->[WebScraping][searchNewGame] New game obtained:" + newGame);
		
		return newGame;
	}
	
	
	//Get URL of the Twitch channel currently having the higher number of views for a game
	public static String getTwitchURLChannel( String GAME ) { 
		System.out.println("--->[WebScraping][getTwitchURLChannel] Getting Twitch channel for game " + GAME);
		
		//Replace spaces in the game title
		GAME = GAME.replaceAll(" ", "%20");
		
		String twitchChannel = "No twitch channels available";
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		 
		try {
	           System.out.println("--->[WebScraping][getTwitchURLChannel] Sending Http GET request for Twitch channel");
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
		
		System.out.println("--->[WebScraping][getTwitchURLChannel] Twitch channel obtained. Returning URL: " + twitchChannel);
		return twitchChannel; 
	}
	
	
	//Get description of a given Game (Needs Game_id)
	public static String getGameDescription(int GAME_ID ) { 
		System.out.println("--->[WebScraping][getGameDescription] Getting Description for game_id = " + GAME_ID);
		
		String gameDescription = "No description available";
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		
		 try {
	            System.out.println("--->[WebScraping][getGameDescription] Sending Http GET request for GameDescription");
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
		
		 System.out.println("--->[WebScraping][getGameDescription] Returning Description: " + gameDescription);
		return gameDescription; 
	}
	

	
	/*
	//Main (for DEBUG)
	 public static void main(String[] args) throws Exception {
		 scrapeNewGames(4200);
	 }
	 */
}
