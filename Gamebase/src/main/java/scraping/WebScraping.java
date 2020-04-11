package scraping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import logic.data.Game;
import logic.data.PlatformInfo;

public class WebScraping {

	
	//Update database using dynamic scraping (NEED TEST)
	public static boolean updateDatabase() { 
		System.out.println("WEBSCRAPING/UPDATEDATABASE--> Starting.");
		System.out.println("WEBSCRAPING/UPDATEDATABASE--> Search for new games");
		List<JSONObject> newGames = new ArrayList<JSONObject>();
		
		
		int newGameID=4200;
		int i = 0;
		int failed=0;
		while (i < 10) {
			System.out.println("WEBSCRAPING/UPDATEDATABASE--> Search for new game: ID=" + newGameID);
			JSONObject newGame = searchNewGame(newGameID);
			System.out.println("WEBSCRAPING/UPDATEDATABASE--> New game obtained");
			newGameID++;
			if(newGame.has("detail")) {
				System.out.println("WEBSCRAPING/UPDATEDATABASE--> Game not suitable");
				continue;
			}
			i++;
			System.out.println(i);
			newGames.add(newGame);
		}
		
		System.out.println("WEBSCRAPING/UPDATEDATABASE--> lista:"+ newGames.size());
		for(int j =0; j < newGames.size(); j++) {
			System.out.println(newGames.get(j));
			
		}
		
		
		
		//Add Games to database
		List<Game> gamesToAdd = new ArrayList<Game>();
		for(int k = 0; k < newGames.size(); k++) {
			
			Game gameToAdd = initializeGameToAdd(newGames.get(k));

			//Inserisci il gioco nel database
			
			//Ricordati di aggiungere la background image anche al Graph
			
		}
		
		return false; 
	}
	
	
	public static Game initializeGameToAdd(JSONObject newGame) {
		
		Game gameToAdd = new Game();
		
		gameToAdd.setId(newGame.getInt("id")); 
		gameToAdd.setFavouritesCount(0);
		if(newGame.has("name")) {
			gameToAdd.setTitle(newGame.getString("name"));
		}
		else {
			gameToAdd.setTitle("Not Available");
		}
		if(newGame.has("background_image")) {
			gameToAdd.setBackground_image(newGame.getString("background_image"));
		}
		else {
			gameToAdd.setBackground_image("https://image.shutterstock.com/image-vector/no-image-available-sign-internet-260nw-261719003.jpg"); //Default?
		}
		
		gameToAdd.setRating(0.0);
		gameToAdd.setRatingCount(0);
		if(newGame.has("metacritic")) {
			gameToAdd.setMetacritic(newGame.getInt("metacritic"));
		}
		else {
			gameToAdd.setMetacritic(0); //Metacritic?
		}
		gameToAdd.setViewsCount(0);
		if(newGame.has("description_raw")) {
			gameToAdd.setDescription(newGame.getString("description_raw"));;
		}
		else {
			gameToAdd.setDescription("Not available"); //Description?
		}
		if(newGame.has("released")) {
			//gameToAdd.setReleaseDate(); //Da vedere
		}
		else {
			//gameToAdd.setReleaseDate();  //Da vedere
		}
		
		//Mancano genres, subgenres, releases, sales;

		
		return gameToAdd;
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
	
	
	//UNUSED
	/*public static void getBackgroundImage(String GAME) {
		
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
*/
	
	
	//Main per fare prove
	 public static void main(String[] args) throws Exception {
		 JSONObject newGame = searchNewGame(4200);
		 
			System.out.println(newGame.getInt("id"));
			System.out.println(newGame.getString("name")); //title
			System.out.println(newGame.getString("background_image"));
			System.out.println(newGame.getDouble("rating"));
			System.out.println(newGame.getInt("ratings_count"));
			System.out.println(newGame.getInt("metacritic"));
			//Inserire viewsCount nel costruttore
			System.out.println(newGame.getJSONArray("genres"));
			//Inserire favoritesCount nel costruttore
			System.out.println(newGame.getString("description_raw"));
			System.out.println(newGame.getString("released")); //Andrà trasformato in Date poi
			
			//Mancano subgenres, releases and sales;
			
			
	 }
	 
}
