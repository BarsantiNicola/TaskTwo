package graphicInterface;

import java.io.File;
import java.util.List;

import javax.swing.JPanel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoPlayerPanel2 extends JPanel{
	
	private JFXPanel VFXPanel;
	private List<Media> mediaList;
	private List<MediaPlayer> playerList;
	private MediaView viewer;
	//private Button next;
	//private Button prev;
	private int currentIndex;
	
	public VideoPlayerPanel2() {
		super();
	}
	
	public void initializeVideos( List<String> videoUrls ) {
		
		for( int i=0; i < videoUrls.size(); i++ ) {
			
			Media media;
			
			try {
				media = new Media(videoUrls.get(i));
			} catch (Exception e) {
				File video_source = new File("src/main/java/resources/notAvailable.mp4");
		    	media = new Media(video_source.toURI().toString());
			}
			
			mediaList.add(media);
			playerList.add(new MediaPlayer(media));
		}
	}
}
