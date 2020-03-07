package logic.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Game implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
    private String background_image;
    private String title;
    private Double rating;
    private Integer ratingCount;
    private Integer metacritic;
    private Integer viewsCount;
    private Integer favouritesCount;
    private String  description;

    private ArrayList<String> genres;
    private ArrayList<ReleaseInfo> releases;
    private ArrayList<PlatformInfo> sales;
    private Multimedia multimedia;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               COSTRUCTORS                                                       //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Game() {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               GETTER                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackground_image(){ return background_image; }

    public Double getRating() {
        return rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public Integer getMetacritic() {
        return metacritic;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }
    
    public String getDescription() {
    	return description;
    }

    public ArrayList<ReleaseInfo> getReleases() {
        return releases;
    }

    public ArrayList<PlatformInfo> getSales() {
        return sales;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }
    
    public String getNintendoURL() {
    	return sales.get(0).getSaleUrl();
    }
    
    public String getSteamURL() {
    	return sales.get(1).getSaleUrl();    	
    }
    
    public String getPlaystationURL() {
    	return sales.get(2).getSaleUrl();
    }
    
    public String getXboxURL() {
    	return sales.get(3).getSaleUrl();    	
    }
     
    public List<String> getVideoURLs(){
    	if( multimedia == null ) {
    		return new ArrayList<>();
    	}
    	ArrayList<Video> videos = multimedia.getVideos();
    	List<String> URLs = new ArrayList<>();
    	for(Video v:videos)
    		URLs.add(v.getMediaUrl());
    	return URLs;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               SETTER                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackground_image( String background_image ){ this.background_image = background_image; }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public void setMetacritic(Integer metacritic) {
        this.metacritic = metacritic;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }

    public void setReleases(ArrayList<ReleaseInfo> releases) {
        this.releases = releases;
    }

    public void setSales(ArrayList<PlatformInfo> sales) {
        this.sales = sales;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               FUNCTIONS                                                        //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public Document toMongo() {
        Gson gson = new Gson();
        
        return new Document(gson.fromJson(gson.toJson(this),Map.class));
    }
	
	public PreviewGame getPreview() {
		return new PreviewGame(title,background_image);
	}

    public String toString(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this );
    }

}