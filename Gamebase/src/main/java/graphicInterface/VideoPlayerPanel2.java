package graphicInterface;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.util.Duration;

@SuppressWarnings("restriction")
public class VideoPlayerPanel2 extends JFXPanel{
	
	private List<Media> mediaList;
	private List<MediaPlayer> playerList;
	private MediaView viewer;
	private Button next;
	private Button prev;
	private int currentIndex;
	private int size;
	
	public VideoPlayerPanel2() {
		super();
	}
	
	public void initializeVideoPlayerPanel( List<String> videoUrls ) {
		
		mediaList = new ArrayList<Media>();
		playerList = new ArrayList<MediaPlayer>();
		
		if( videoUrls == null ) {
			
			File video_source = new File("src/main/java/resources/notAvailable.mp4");
			Media media = new Media(video_source.toURI().toString());
	    	mediaList.add(media);
	    	playerList.add(new MediaPlayer(media));
	    	size = 1;
		} else {
			
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
			
		   size = videoUrls.size();
		}
		
		currentIndex = 0;
		
		next = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/resources/next.png"),30,30,false,false)));
		next.setOnAction(actionEvent->{
			
			if( currentIndex == size-1 ) {
				
				return;
			}
			
			currentIndex++;
			
			playVideo();
			
			if( currentIndex == 1 ) {
				
				prev.setDisable(false);
			}
			
			if( currentIndex == size-1 ) {
				
				next.setDisable(true);
			}
			
		});
		
		prev = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/resources/back.png"),30,30,false,false)));
		prev.setOnAction(actionEvent->{
			
			if( currentIndex == 0 ) {
				
				return;
			}
			
			currentIndex--;
			
			playVideo();
			
			if( currentIndex == size-2 ) {
				
				next.setDisable(false);
			}
			
			if( currentIndex == 0 ) {
				
				prev.setDisable(true);
			}
			
		});
		
		prev.setDisable(true);
		
		if( size == 1 ) {
			
			next.setDisable(true);
		}
		
	}
	
	public void playVideo() {
		
		MediaPlayer player = playerList.get(currentIndex);
	    
		viewer = new MediaView(player);
		
		AnchorPane root = new AnchorPane();
	    Scene scene = new Scene(root);
	    
	    /*
	    // center video position
	    javafx.geometry.Rectangle2D screen = Screen.getPrimary().getVisualBounds();
	    viewer.setX((screen.getWidth() - this.getWidth()) / 2);
	    viewer.setY((screen.getHeight() - this.getHeight()) / 2);*/
	    
	    root.getChildren().addAll(viewer,next,prev);
	    AnchorPane.setLeftAnchor(prev, 0.0);
	    AnchorPane.setBottomAnchor(prev,0.0);
	    AnchorPane.setRightAnchor(next, 0.0);
	    AnchorPane.setBottomAnchor(next,0.0);
	    
	    // resize video based on screen size
	    DoubleProperty width = viewer.fitWidthProperty();
	    DoubleProperty height = viewer.fitHeightProperty();
	    width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
	    height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
	    viewer.setPreserveRatio(true);
		
	    this.setScene(scene);
	   // player.seek(player.getStartTime());
	    
	    player.play();
	}
	
	public void cleanVideoPlayer() {
		mediaList.clear();
		playerList.clear();		
	}
}
