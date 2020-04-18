package logic.data;

import java.awt.Image;

import javax.swing.ImageIcon;

public class BufferedGame {
	
	private final Integer id;
	private final String title;        
	private final ImageIcon previewPic; 
	
	public BufferedGame( int id , String title , ImageIcon previewPic ){
		
		this.id = id;
		this.title = title;
		this.previewPic = previewPic;
	}
	
	public String getTitle() { return title; }
	
	public ImageIcon getPreviewPic() { return previewPic; }
	
	public Integer getId() { return id; }
}
