package scraping;
import java.io.IOException;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


//-----------------------------------------------------------------------------------
//The class is used for all the different GET request implemented by WebScraping. 
//-----------------------------------------------------------------------------------

public class HttpClient {

	//Configuration for httpClient
	int timeout = 20;
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
            return "Game description not available";
       } catch (Exception e){
    	   System.out.println("-->[HttpClient][sendGetGameDescription] Error: something went wrong. Please check your connection");
       }
       finally {
    	   response.close();
       }
	return null;
       
    }
    

}
