package logic.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Date    releaseDate;
    private String genres;
    
    private ArrayList<String> subgenres;
    private ArrayList<String> releases;
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

    public String getGenres() {
    	return genres;
    }
    
    public ArrayList<String> getSubGenres() {
        return subgenres;
    }

    public ArrayList<String> getAllGenres(){
    	ArrayList<String> genres = new ArrayList<String>();
    	if( genres != null )
    		genres.add( 0,this.genres );
    	if( subgenres != null )
    		genres.addAll(subgenres);
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
    
    public Date getReleaseDate() {
    	return releaseDate;
    }

    public ArrayList<String> getReleases() {
        return releases;
    }

    public ArrayList<PlatformInfo> getSales() {
        return sales;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }
    
    public String getNintendoURL() {
    	for( PlatformInfo plat : sales )
    		if( plat.getStore().charAt(4) == 'e')
    			return plat.getSaleUrl();
    	return null;
    }
    
    public String getSteamURL() {

    	for( PlatformInfo plat : sales )
    		if( plat.getStore().charAt(4) == 'm')
    			return plat.getSaleUrl();
    	return null;
    }
    
    public String getPlaystationURL() {

    	for( PlatformInfo plat : sales )
    		if( plat.getStore().charAt(4) == 's')
    			return plat.getSaleUrl();
    	return null;
    }
    
    public String getXboxURL() {

    	for( PlatformInfo plat : sales )
    		if( plat.getStore().charAt(4) == 'O'|| plat.getStore().charAt(4) == '3')
    			return plat.getSaleUrl();    	

    	return null;
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

    public List<String> getImagesURLs(){
    	if( multimedia == null ) {
    		return new ArrayList<>();
    	}
    	return multimedia.getImages();	
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

    public void setGenres(String genres) {
    	this.genres = genres;
    }
    
    public void setSubGenres(ArrayList<String> subgenres) {
        this.subgenres = subgenres;
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

    public void setReleases(ArrayList<String> releases) {
        this.releases = releases;
    }

    public void setSales(ArrayList<PlatformInfo> sales) {
        this.sales = sales;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }
    
    public void setReleaseDate(Date releaseDate) {
    	this.releaseDate = releaseDate;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               FUNCTIONS                                                        //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	public PreviewGame generatePreview() {
		return new PreviewGame(id,title,background_image);
	}

    public String toString(){

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this );
    }

}