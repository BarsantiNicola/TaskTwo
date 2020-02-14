package scraping;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpClient {

	// one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public void close() throws IOException {
        httpClient.close();
    }

    public String sendGet(String GAME) throws Exception {

        HttpGet request = new HttpGet("https://api.twitch.tv/kraken/streams/?game=" + GAME);

        // Add request headers
        request.addHeader("Accept", "application/vnd.twitchtv.v5+json");
        request.addHeader("Client-ID", "ndtm4x05vr0kvymsiv0s3hgwtgbrjy");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);
                String streams = jsonObject.get("streams").toString();
                JSONArray jsonArray = new JSONArray(streams);
                JSONObject jsonobject = jsonArray.getJSONObject(0);
                JSONObject channel = jsonobject.getJSONObject("channel");
                String url = channel.getString("url");
                return url;
                }
            
  
            }
		return null;
            
           
        }

    }
