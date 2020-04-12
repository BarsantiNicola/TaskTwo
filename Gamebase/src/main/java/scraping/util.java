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
import logic.data.GraphGame;
import logic.data.Multimedia;
import logic.data.PlatformInfo;
import logic.data.Video;

public class util {
	
	//Create a Game consistent with the new game just scraped
	public static Game initializeGameToAdd(JSONObject newGame) {
		System.out.println("UTIL/INITIALIZEGAMETOADD--> Initializing new game");
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
		// multimedia: images;
		Multimedia multimedia = new Multimedia();
		ArrayList<String> images = new ArrayList<String>();
		ArrayList<Video> videos = new ArrayList<Video>();
		if(newGame.has("short_screenshots")) {;
			JSONArray short_screenshots = newGame.getJSONArray("short_screenshots");
			for(int i = 0; i < short_screenshots.length(); i++) {
				if(short_screenshots.getJSONObject(i).has("image")) {
					images.add(short_screenshots.getJSONObject(i).getString("image"));
				}
			}
		}
		//multimedia: video
		if(newGame.has("clip")) {
			JSONObject clip = newGame.getJSONObject("clip");
			if(clip.has("clips")) {
				JSONObject clips = newGame.getJSONObject("clip").getJSONObject("clips");
				if(clips.has("320")) {
					Video videoToAdd = new Video();
					videoToAdd.setResolution("320");
					videoToAdd.setMediaUrl(clips.getString("320"));
					videos.add(videoToAdd);
				}
				if(clips.has("640")) {
					Video videoToAdd = new Video();
					videoToAdd.setResolution("640");
					videoToAdd.setMediaUrl(clips.getString("640"));
					videos.add(videoToAdd);
				}
				if(clips.has("full")) {
					Video videoToAdd = new Video();
					videoToAdd.setResolution("full");
					videoToAdd.setMediaUrl(clips.getString("full"));
					videos.add(videoToAdd);
				}
			}
		}
		if(images.size() > 0) {
			multimedia.setImages(images);
		}
		if(videos.size() > 0) {
			multimedia.setVideos(videos);
		}
		if(multimedia.getImages().size() > 0 || multimedia.getVideos().size() > 0) {
			gameToAdd.setMultimedia(multimedia);
		}
		
		System.out.println("UTIL/INITIALIZEGAMETOADD--> Created new game");
		return gameToAdd;
	}
	
	
	//Create GraphGame consistent with the new game just scraped
	public static GraphGame initializeGraphGameToAdd(Game gameToAdd) {
		System.out.println("UTIL/INITIALIZEGRAPHGAMETOADD--> Initializing GraphGame for game: " + gameToAdd.getTitle());
		
		Long zero = new Long(0);
		/* All-parameters Constructor */
		/* Id, title, background_image, favorite_count, rating */
		GraphGame graphGameToAdd = new GraphGame(gameToAdd.getId().toString(), gameToAdd.getTitle(), gameToAdd.getBackground_image(), zero, zero);
		System.out.println("UTIL/INITIALIZEGRAPHGAMETOADD--> Created GraphGame");
		return graphGameToAdd;
	}

}
