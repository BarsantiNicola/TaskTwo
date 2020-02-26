package graphicInterface;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javafx.embed.swing.*;
import javafx.scene.media.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.beans.property.*;
import javafx.stage.*;
import javafx.beans.binding.*;

public class JavaFXVideoBuilder {
	
	private JPanel panel;
	private JFXPanel VFXPanel;
	private Media media;
	private MediaPlayer player;
	private MediaView viewer;
	
	public JavaFXVideoBuilder(JPanel jpanel) {
		panel = jpanel;
		VFXPanel = new JFXPanel();
	}
	
	public void getVideo(String location) {

		//File video_source = new File(location);
	    //Media m = new Media(video_source.toURI().toString());
		try {
			media = new Media(location);
		} catch (Exception e) {
			File video_source = new File("/resources/notAvailable.mp4");
	    	media = new Media(video_source.toURI().toString());
		}
	    
	    player = new MediaPlayer(media);
	    viewer = new MediaView(player);
	
	    StackPane root = new StackPane();
	    Scene scene = new Scene(root);
	
	    
	    // center video position
	    javafx.geometry.Rectangle2D screen = Screen.getPrimary().getVisualBounds();
	    viewer.setX((screen.getWidth() - panel.getWidth()) / 2);
	    viewer.setY((screen.getHeight() - panel.getHeight()) / 2);
	
	    // resize video based on screen size
	    DoubleProperty width = viewer.fitWidthProperty();
	    DoubleProperty height = viewer.fitHeightProperty();
	    width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
	    height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
	    viewer.setPreserveRatio(true);
		
	    // add video to stackpane
	    root.getChildren().add(viewer);
	
	    VFXPanel.setScene(scene);
	    player.play();
	    panel.setLayout(new BorderLayout());
	    panel.add(VFXPanel, BorderLayout.CENTER);
    
	}
	
	public void stopVideo() {
		player.stop();
	}
}
