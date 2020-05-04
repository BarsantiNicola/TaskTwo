package scraping;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

//-----------------------------------------------------------------------------------
//The class is used for all the different GET request implemented by WebScraping
//-----------------------------------------------------------------------------------

public class HttpClient {

	//Configuration for httpClient
	int timeout = 5;
	RequestConfig config = RequestConfig.custom()
	  .setConnectTimeout(timeout * 1000)
	  .setConnectionRequestTimeout(timeout * 1000)
	  .setSocketTimeout(timeout * 1000).build();
	
	// one instance, reuse
    private final CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    //Close the response
    public void close() throws IOException {
    	System.out.println("-->[HttpClient][close] Closing httpClient");
        httpClient.close();
    }
    
    
    //Get a new game
    public JSONObject sendGetNewGame(int GAME_ID) throws Exception {
    	System.out.println("-->[HttpClient][sendGetNewGame] Preparing request for new game");
    	
        HttpGet request = new HttpGet("https://api.rawg.io/api/games/" + GAME_ID);

        CloseableHttpResponse response = httpClient.execute(request);
        try {
        	System.out.println("-->[HttpClient][sendGetNewGame] Request sent");
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity);
            JSONObject newGame = new JSONObject(result);
           
            System.out.println("-->[HttpClient][sendGetNewGame] Returning a new game");
            return newGame;
        } catch (Exception e){
     	   System.out.println("-->[HttpClient][sendGetNewGame] Error: something went wrong. Please check your connection");
        }
        finally {
        	response.close();
        }
		return null;
    }

    
    //Get twitch channel for a game
    public String sendGetTwitch(String GAME) throws Exception {
    	System.out.println("-->[HttpClient][sendGetTwitch] Preparing request for twitch channel");

        HttpGet request = new HttpGet("https://api.twitch.tv/kraken/streams/?game=" + GAME);

        // Add request headers
        request.addHeader("Accept", "application/vnd.twitchtv.v5+json");
        request.addHeader("Client-ID", "ndtm4x05vr0kvymsiv0s3hgwtgbrjy");
        
       // request.getParams().setIntParameter("http.connection.timeout", 1);

        CloseableHttpResponse response = httpClient.execute(request);
        	
        try {
    	    System.out.println("-->[HttpClient][sendGetTwitch] Request sent");
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();

            //Get result to string->JSONObject->JSONArray
            String result = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = new JSONArray();
            if (jsonObject.has("streams")) {
            	String streams = jsonObject.get("streams").toString();
            	jsonArray = new JSONArray(streams);
            	if (jsonArray.length() == 0) {
            		return "No streaming available!";
            	}
            }
            else {
            	return "No streaming available!";
            }
            //Return the first element in the Twitch channels array
            JSONObject jsonobject = jsonArray.getJSONObject(0);
            if (jsonobject.has("channel")) {
            	JSONObject channel = jsonobject.getJSONObject("channel");
            	String url = channel.getString("url");
            	return url;
            }
            else {
            	return "No streaming available!";
            }
       } catch (Exception e){
    	   System.out.println("-->[HttpClient][sendGetTwitch] Error: something went wrong. Please check your connection");
       }
        finally {
    	   response.close();
       }
		return "No streaming available!";
    }
    
    
    //Get game description
    public String sendGetGameDescription(int GAME_ID) throws Exception {
    	System.out.println("-->[HttpClient][sendGetGameDescription] Preparing request for game description");

        HttpGet request = new HttpGet("https://api.rawg.io/api/games/" + GAME_ID);

       CloseableHttpResponse response = httpClient.execute(request);
       try {
        	System.out.println("-->[HttpClient][sendGetGameDescription] Request sent");
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(result);
            
            if(jsonObject.has("description_raw")){
            	return jsonObject.getString("description_raw");
            }
            return "No description available";
       } catch (Exception e){
    	   System.out.println("-->[HttpClient][sendGetGameDescription] Error: something went wrong. Please check your connection");
       }
       finally {
    	   response.close();
       }
	return null;
       
    }
    

}
