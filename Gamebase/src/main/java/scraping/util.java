package scraping;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import logic.data.Game;
import logic.data.PlatformInfo;

public class util {
	
	public static Game initializeGameToAdd(JSONObject newGame) {
		
		Game gameToAdd = new Game();
		
		//Id
		gameToAdd.setId(newGame.getInt("id")); 
		//FavoriteCount
		gameToAdd.setFavouritesCount(0);
		//Title
		if(newGame.has("name")) {
			gameToAdd.setTitle(newGame.getString("name"));
		}
		//Background_image
		if(newGame.has("background_image")) {
			gameToAdd.setBackground_image(newGame.getString("background_image"));
		}		
		//Rating
		gameToAdd.setRating(0.0);
		//Rating Count
		gameToAdd.setRatingCount(0);
		//Metacritic
		if(newGame.has("metacritic")) {
			gameToAdd.setMetacritic(newGame.getInt("metacritic"));
		}
		//ViewsCount
		gameToAdd.setViewsCount(0);
		if(newGame.has("description_raw")) {
			gameToAdd.setDescription(newGame.getString("description_raw"));;
		}
		//Released
		if(newGame.has("released")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date releaseDate = new Date();
			try {
				releaseDate = sdf.parse(newGame.getString("released"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			gameToAdd.setReleaseDate(releaseDate); 
		}
		//Genres and subgenres
		if(newGame.has("genres")) {
			JSONArray genres = newGame.getJSONArray("genres");
			ArrayList<String> subgenres = new ArrayList<String>();
			if (genres.getJSONObject(0).has("name")) {
				gameToAdd.setGenres(genres.getJSONObject(0).getString("name"));
			}
			for (int i = 1; i < genres.length(); i++) {
				if(genres.getJSONObject(1).has("name")) {
					subgenres.add(genres.getJSONObject(0).getString("name"));
				}
			}
			if (subgenres.size() > 0) {
				gameToAdd.setSubGenres(subgenres);
			}
		}
		//Releases
		if(newGame.has("platforms")) {
			JSONArray platforms = newGame.getJSONArray("platforms");
			ArrayList<String> releases = new ArrayList<String>();
			for (int i = 1; i < platforms.length(); i++) {
				if(platforms.getJSONObject(i).has("plaform")) {
					if(platforms.getJSONObject(i).getJSONObject("plaform").has("name")) {
						releases.add(platforms.getJSONObject(i).getJSONObject("platform").getString("name"));
					}
				}
			}
			if(releases.size() > 0) {
				gameToAdd.setReleases(releases);
			}
			
		}
		//Sales
		if(newGame.has("stores")) {
			ArrayList<PlatformInfo> sales = new ArrayList<PlatformInfo>();
			//store, company, url 
			//name, domain, url_en
			JSONArray stores = newGame.getJSONArray("stores");
			for(int i = 0; i < stores.length(); i++) {
				PlatformInfo PI = new PlatformInfo();
				if(stores.getJSONObject(i).has("store")) {
					if(stores.getJSONObject(i).getJSONObject("store").has("name")) {
						PI.setStore(stores.getJSONObject(i).getJSONObject("store").getString("name"));
					}
					if(stores.getJSONObject(i).getJSONObject("store").has("domain")) {
						PI.setCompany(stores.getJSONObject(i).getJSONObject("store").getString("domain"));
					}
					if(stores.getJSONObject(i).getJSONObject("store").has("url_en")) {
						PI.setSaleUrl(stores.getJSONObject(i).getJSONObject("store").getString("url_en"));
					}
					sales.add(PI);
				}		
			}
			if(sales.size() > 0) {
				gameToAdd.setSales(sales);
			}
		}
		
		
		// multimedia;
		return gameToAdd;
	}

}
