package scraping;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;


public class HttpClient {

	// one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public void close() throws IOException {
    	System.out.println("HTTPCLIENT/HTTPCLIENT.CLOSE--> Closing httpClient");
        httpClient.close();
    }
    
    
    public JSONObject sendGetNewGame(int GAME_ID) throws Exception {
    	System.out.println("HTTPCLIENT/SENDGETNEWGAME-->Preparing request for new game");
    	
        HttpGet request = new HttpGet("https://api.rawg.io/api/games/" + GAME_ID);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

        	System.out.println("HTTPCLIENT/SENDGETNEWGAME-->Request sent");
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            String result = EntityUtils.toString(entity);
            JSONObject newGame = new JSONObject(result);
           
            System.out.println("HTTPCLIENT/SENDGETNEWGAME-->Returning a new game");
            return newGame;
        }
    }

    //Get twitch channel for a game
    public String sendGetTwitch(String GAME) throws Exception {
    	System.out.println("HTTPCLIENT/SENDGETTWITCH-->Preparing request for twitch channel");

        HttpGet request = new HttpGet("https://api.twitch.tv/kraken/streams/?game=" + GAME);

        // Add request headers
        request.addHeader("Accept", "application/vnd.twitchtv.v5+json");
        request.addHeader("Client-ID", "ndtm4x05vr0kvymsiv0s3hgwtgbrjy");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
        	
        	System.out.println("HTTPCLIENT/SENDGETTWITCH-->Request sent");
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            //Get result to string->JSONObject->JSONArray
            String result = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(result);
            String streams = jsonObject.get("streams").toString();
            JSONArray jsonArray = new JSONArray(streams);
            if (jsonArray.length() == 0) {
            	return "No streaming available!";
            }
            
            //Return the first element in the Twitch channels array
            JSONObject jsonobject = jsonArray.getJSONObject(0);
            JSONObject channel = jsonobject.getJSONObject("channel");
            String url = channel.getString("url");
            System.out.println(url); //Debug
            return url;
  
            }
        }
    
    //Get game description
    public String sendGetGameDescription(int GAME_ID) throws Exception {
    	System.out.println("HTTPCLIENT/SENDGETGAMEDESCRIPTION-->Preparing request for game description");

        HttpGet request = new HttpGet("https://api.rawg.io/api/games/" + GAME_ID);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

        	System.out.println("HTTPCLIENT/SENDGETGAMEDESCRIPTION-->Request sent");
            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            String result = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(result);
            	
            return jsonObject.getString("description_raw");
            
  
            }
        }
    
    public void sendGetGameBackgroundImage(String GAME) throws Exception {
    	
    	 HttpGet request = new HttpGet("https://serpapi.com/search?q=" + GAME + "&tbm=isch&ijn=0");
    		 try (CloseableHttpResponse response = httpClient.execute(request)) {

    	        	System.out.println("HTTPCLIENT/SENDGETGAMEDESCRIPTION-->Request sent");
    	            // Get HttpResponse Status
    	            System.out.println(response.getStatusLine().toString());

    	            HttpEntity entity = response.getEntity();
    	            Header headers = entity.getContentType();
    	            System.out.println(headers);
    	            
    	            String result = EntityUtils.toString(entity);
    	            System.out.println(result);
    		 }
    }


    }
