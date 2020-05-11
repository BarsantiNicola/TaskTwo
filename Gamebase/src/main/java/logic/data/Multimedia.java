package logic.data;

import org.bson.Document;

import java.io.Serializable;
import java.util.ArrayList;

public class Multimedia implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<String> images;
    private ArrayList<String> videos;

    public Multimedia(){}

    Multimedia( Document data ){

        @SuppressWarnings("unchecked")
		ArrayList<Document> images =   data.get("short_screenshots", ArrayList.class );
        this.images = new ArrayList<>();
        this.videos = new ArrayList<>();
        Document videos = ((Document) data.get("clip"));

        if( images != null )
            for (Document image : images)
                if( image != null)
                    this.images.add(image.getString("image"));

        if( videos != null )
            videos = videos.get("clips",Document.class);
        if( videos != null ) {
            try {
                this.videos.add(videos.getString("320"));
            } catch (Exception e) {
            }
            try {
                this.videos.add(videos.getString("640"));
            } catch (Exception e) {
            }
            try {
                this.videos.add(videos.getString("full"));
            } catch (Exception e) {
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               GETTER                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public ArrayList<String> getImages() { return images; }

    public ArrayList<String> getVideos(){ return videos; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                               SETTER                                                           //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void setImages( ArrayList<String> images ){ this.images = images; }

    public void setVideos( ArrayList<String> videos ){ this.videos = videos; }

}
