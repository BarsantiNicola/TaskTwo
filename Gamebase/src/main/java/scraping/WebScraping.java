package scraping;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import logic.LogicBridge;
import logic.data.Game;
import logic.mongoConnection.MongoConnection;

//-----------------------------------------------------------------------------------------------------
//The class is used to contain all the methods used to perform dynamic web scraping by the application.
//A CloseableHttpClient instance is used for all methods in order to perform a GET request.
//-----------------------------------------------------------------------------------------------------

public class WebScraping {

	//Scrape 10 new games from rawg, starting from the id subsequent the max game id in the database. 
	//Games are scraped in json form and need to be converted into Game class.
	public static Game searchNewGames(int MaxGameID) { 
		System.out.println("--->[WebScraping][searchNewGames] Search for new games");
		JSONObject newGame = new JSONObject(); 
		
		boolean suitableGame = false;
		int failed=0;
		while (!suitableGame) {
			
			//Wait 3 seconds after each request, for polite scraping
			try {
			TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				System.out.println("--->[WebScraping][searchNewGames] Error: sleep() function failed");
			}
			if (failed == 100){
				System.out.println("--->[WebScraping][searchNewGames] More than 100 attempts failed. Stopping the search for new games...");
				return null;
			}
			
			System.out.println("--->[WebScraping][searchNewGames] Search for new game: ID=" + MaxGameID);
			newGame = scrapeNewGame(MaxGameID);
			System.out.println("--->[WebScraping][searchNewGames] New game obtained");
			MaxGameID++;
			
			//Check if the new game is suitable
			if(!newGame.has("id") || !newGame.has("name")) {
				System.out.println("--->[WebScraping][searchNewGames] Game not suitable");
				failed ++;
				continue;
			}
			
			System.out.println("--->[WebScraping][searchNewGames] Game suitable");
			suitableGame = true;
		}
		
		//Create object Game
		System.out.println("--->[WebScraping][searchNewGames] Creating object Game for new game");
		Game gameToAdd = util.initializeGameToAdd(newGame);		
		
		//Return list of games to add
		return gameToAdd;
	}
	
	
	

	//Scrape new game using id
	public static JSONObject scrapeNewGame(int ID_GAME) {
		
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		JSONObject newGame = new JSONObject();
			
		try {
	           System.out.println("--->[WebScraping][scrapeNewGame] Sending Http GET request for new game. ID:" + ID_GAME);
	           try {
				newGame = objRequest.sendGetNewGame(ID_GAME);
	           } catch (Exception e) {
	        	   System.out.println("--->[WebScraping][scrapeNewGame] Something went wrong in sending http request");
				}

	        } finally {
	            try {
					objRequest.close();
				} catch (IOException e) {
					System.out.println("--->[WebScraping][scrapeNewGame] Something went wrong in closing http request");
				}
	        }	
		
		return newGame;
	}
	

	//Get description of a given Game (Needs Game id)
	public static String getGameDescription(int GAME_ID ) { 
		System.out.println("--->[WebScraping][getGameDescription] Getting Description for game_id = " + GAME_ID);
		
		String gameDescription = "No description available";
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		
		 try {
	            //System.out.println("--->[WebScraping][getGameDescription] Sending Http GET request for GameDescription");
	            try {
					gameDescription = objRequest.sendGetGameDescription(GAME_ID);
				} catch (Exception e) {
					System.out.println("--->[WebScraping][getGameDescription] Something went wrong in sending http request" +  e.getMessage());
				}

	        } finally {
	            try {
					objRequest.close();
				} catch (IOException e) {
					System.out.println("--->[WebScraping][getGameDescription] Something went wrong in closing http request" +  e.getMessage());
				}
	        }
		
		 //System.out.println("--->[WebScraping][getGameDescription] Returning Description");
		return gameDescription; 
	}
	

/*
 //GAME DESCRIPTION SCRAPING BRUTEFORCE
	///*
 public static void addDescriptionToAllGames(MongoConnection MONGO) throws Exception
  {
   int maxGameID= MONGO.getMaxGameId().element;
   int maxThreads = 30;                                //Number of threads
   
   System.out.println("Attempting to create " + maxThreads + " threads for scraping (maxGameID = " + maxGameID + ")");
   for(int i=0;i<maxThreads;i++)
    new ScrapingThread(i,maxGameID,maxThreads,new MongoConnection("172.16.0.80",27018)).start();
   System.out.println("Threads Successfully created");
  }
 
 public static void main(String[] args) throws Exception 
  {
   LogicBridge logicBridge = new LogicBridge();
   System.out.println("Maximum gameID = " + logicBridge.getMONGO().getMaxGameId().element);
   try 
    { addDescriptionToAllGames(logicBridge.getMONGO()); } 
   catch (Exception e)
    { System.out.println("[Logic Bridge Main]: Error in addDescriptionToAllGames(): " + e.getMessage()); }
   logicBridge.closeConnection();
  }
 //*/ 
	
 
	/*
	//Main (for DEBUG)
	 public static void main(String[] args) throws Exception {
		 searchNewGames(99999999);

	 }
	 */

}
