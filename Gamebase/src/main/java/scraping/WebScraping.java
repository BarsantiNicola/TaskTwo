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
			//Wait 3 seconds after each request, for polite scraping
			try {
			TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				System.out.println("--->[WebScraping][scrapeNewGames] Error: sleep() function failed");
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
		
		//The empty list case is handled in the calling function
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
	
	
	

	//Scrape new game using id
	public static JSONObject searchNewGame(int ID_GAME) {
		
		//Create http object for request
		HttpClient objRequest = new HttpClient();
		JSONObject newGame = new JSONObject();
			
		try {
	           System.out.println("--->[WebScraping][searchNewGame] Sending Http GET request for new game. ID:" + ID_GAME);
	           try {
				newGame = objRequest.sendGetNewGame(ID_GAME);
	           } catch (Exception e) {
	        	   System.out.println("--->[WebScraping][searchNewGamel] Something went wrong in sending http request");
				}

	        } finally {
	            try {
					objRequest.close();
				} catch (IOException e) {
					System.out.println("--->[WebScraping][searchNewGamel] Something went wrong in closing http request");
				}
	        }
		
		System.out.println("--->[WebScraping][searchNewGame] New game obtained:" + newGame); //Debug
		
		return newGame;
	}
	

	//Get description of a given Game (Needs Game_id)
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
  
 //OLD GAME DESCRIPTION SCRAPING (Federico)
	/*
 //OLD Scraping (Federico) 
 public void addDescriptionToAllGames() 
  {
   int MaxGameId= MONGO.getMaxGameId().element;
   int i = 0;
   while (i < MaxGameId) 
    {
     i++;
     try 
      { TimeUnit.MILLISECONDS.sleep(10); } 
     catch (InterruptedException e) 
      { System.out.println("--->[] Error: sleep() function failed"); }
   
   String description = getGameDescription(i);
   System.out.println("Adding description for game: " + i);
   StatusCode addDescriptionStatus = addGameDescription(i, description); 
   
   if(addDescriptionStatus == StatusCode.ERR_NETWORK_UNREACHABLE) 
   {
    System.out.println("-->[] Network Unreachable. Exit after game: " + i);
    break;
   }
  }
  MONGO.closeConnection();
 }
 
  public static void main(String[] args) throws Exception 
   {
   LogicBridge logicBridge = new LogicBridge();
   System.out.println(logicBridge.MONGO.getMaxGameId().element);
   try 
    { logicBridge.addDescriptionToAllGames(); } 
   catch (Exception e)
    { 
     System.out.println("[Logic Bridge Main]: Error in addDescriptionToAllGames(): " + e.getMessage());
    }
   logicBridge.closeConnection();
   }
  }
 */
	
 /*

	//Main (for DEBUG)
	 public static void main(String[] args) throws Exception {
		 scrapeNewGames(99999999);

	 }
	 */
}
