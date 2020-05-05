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
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.scene.control.Button;

@SuppressWarnings("restriction")
public class VideoPlayerPanel2 extends JPanel{
	
	private JFXPanel VFXPanel;
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
		
		currentIndex = 0;
		size = videoUrls.size();
		
		next = new Button("NEXT");
		next.setOnAction(actionEvent->{
			
			if( currentIndex == size ) {
				
				return;
			}
			
			currentIndex++;
			
			if( currentIndex == 1 ) {
				
				prev.setDisable(false);
			}
			
			playVideo();
		});
		
		prev = new Button("PREV");
		prev.setOnAction(actionEvent->{
			
			if( currentIndex == 0 ) {
				
				return;
			}
			
			currentIndex--;
			
			if( currentIndex == size-1 ) {
				
				next.setDisable(false);
			}
			
			playVideo();
		});
		
		prev.setDisable(true);
	}
	
	public void playVideo() {
		
		MediaPlayer player = playerList.get(currentIndex);
		
		viewer = new MediaView(player);
		
		StackPane root = new StackPane();
	    Scene scene = new Scene(root);
	    
	    // center video position
	    javafx.geometry.Rectangle2D screen = Screen.getPrimary().getVisualBounds();
	    viewer.setX((screen.getWidth() - this.getWidth()) / 2);
	    viewer.setY((screen.getHeight() - this.getHeight()) / 2);
	    
	    root.getChildren().add(viewer);
	    // resize video based on screen size
	    DoubleProperty width = viewer.fitWidthProperty();
	    DoubleProperty height = viewer.fitHeightProperty();
	    width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
	    height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
	    viewer.setPreserveRatio(true);
		
	    root.getChildren().add(next);
	    root.getChildren().add(prev);
	
	    VFXPanel.setScene(scene);
	    this.setLayout(new BorderLayout());
	    this.add(VFXPanel, BorderLayout.CENTER);
	    player.play();
	}
}
