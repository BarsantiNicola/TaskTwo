package graphicInterface;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class VideoPlayerPanel extends JPanel{
	
	private JavaFXVideoBuilder video;
	
	public VideoPlayerPanel() {
		super();
		video = new JavaFXVideoBuilder(this);
	}
	
	public void playVideo(String location) {
		video.playVideo(location);
	}
	
	public void stopVideo() {
		video.stopVideo();
	}
}
