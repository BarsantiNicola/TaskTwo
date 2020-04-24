package logic;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class ImgCache {
	
	private final String databasePath;
	private DB levelDb;
	
	public ImgCache( String cachePath ){
		
		databasePath = cachePath;  
		createConnection();   //  initializing of the connection
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if( levelDb != null ) 
					try{
						levelDb.close();
					}catch( Exception e ) {}
			}
		});
		
	}

	public ImageIcon getCachedImg( String URL ) {
		
    	byte[] image;

		
    	if( levelDb == null )
    		return null;
    	
    	try {
    		
    		image = levelDb.get(URL.getBytes());

    		if( image == null ) return null;

    		return createImage(image);
    		
    	}catch( Exception e ) {
    		
			try{ levelDb.close(); } catch( Exception a ) {}

			e.printStackTrace();
    		levelDb = null;
    		return null;
    		
    	}
		
	}
	
	   public ImageIcon createImage(byte[] img){

		   	try {
		   	  System.out.println(img.length);
		   	  BufferedImage bImageFromConvert = ImageIO.read(new ByteArrayInputStream(img));
		      ByteArrayOutputStream bos = new ByteArrayOutputStream();
		      ImageIO.write(bImageFromConvert, "jpg", bos );
		      byte [] data = bos.toByteArray();
		      ByteArrayInputStream bis = new ByteArrayInputStream(data);
		      BufferedImage bImage2 = ImageIO.read(bis);
		   	
		      return new ImageIcon(bImage2);
		   	}catch(Exception e) {
		   		e.printStackTrace();
		   		return null;
		   	}

		   }

	   	public byte[] encoder(ImageIcon imgData) {
	   		
	   	    Image  img = imgData.getImage();

	   	    BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
	   	        BufferedImage.TYPE_INT_RGB);

	   	    Graphics g = bufferedImage.createGraphics();
	   	    g.drawImage(img, 0, 0, null);
	   	    g.dispose();

	   	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   	    try {
	   	    	ImageIO.write(bufferedImage, "jpg", baos);
	   	    }catch(Exception e ) {}
	   	    byte[] ret = baos.toByteArray();

	   	    return ret;
	}
	   	
	
	
	public boolean cacheImg( String URL , ImageIcon img ) {
		byte[] ret  = null;

		
    	if( levelDb == null )
    		return false;

    		ret = encoder(img);

    	if(ret == null || ret.length == 0 ) return false;

    	try {

    		if(levelDb.get( URL.getBytes() ) == null ) 
    			levelDb.put( URL.getBytes() , ret );
    		else 
    			return false;

    		return true;
    		
    	}catch( Exception e ) {

			try{ levelDb.close(); } catch( Exception a ) {}

    		levelDb = null;
    		return false;
    		
    	}
    	
	}
	
	private boolean createConnection(){
		
		Options options = new Options();
		File levelDbDirectory = new File(databasePath);
			
		//  we verify the database already exists
		if( !levelDbDirectory.exists() ) {


			//  we try to create the main directory
			if( !levelDbDirectory.mkdir())
				return false;
			
			options.createIfMissing( true );	
				
			try {
					
				levelDb = factory.open( levelDbDirectory ,options );
					
			}catch( Exception e ) {
						
				e.printStackTrace();
				try{ levelDb.close(); } catch( Exception a ) {}
				levelDb = null;
				return false;
						
			}
			
			return true;
		}
			
		try {
				
			levelDb = factory.open( levelDbDirectory ,options );
			return true;
				
		}catch( Exception e ) {

			e.printStackTrace();
			try{ levelDb.close(); } catch( Exception a ) {}
			levelDb = null;
			return false;
					
		}
					
	}
}
