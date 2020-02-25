package graphicInterface;

import javax.swing.JPanel;

public class VideoPlayerPanel extends JPanel{
	
	private JavaFXVideoBuilder video;
	
	public VideoPlayerPanel() {
		super();
		video = new JavaFXVideoBuilder(this);
	}
	
	public void getVideo(String location) {
		video.getVideo(location);
	}
	
	public void stopVideo() {
		video.stopVideo();
	}
}
