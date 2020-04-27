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
	
	ImgCache( String cachePath ){
		
		databasePath = cachePath;  
		createConnection();   
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
    		if( createConnection() == false ) 
    			return null;
    	
    	try {
    		
    		image = levelDb.get(URL.getBytes());

    		if( image == null ) return null;

    		return decodeImage(image);
    		
    	}catch( Exception e ) {
    		
			try{ levelDb.close(); } catch( Exception a ) {}

			e.printStackTrace();
    		levelDb = null;
    		return null;
    		
    	}
		
	}
	
	public boolean cacheImg( String URL , ImageIcon img ) {
		
		byte[] ret  = null;

    	if( levelDb == null )
    		if( createConnection() == false ) 
    			return false;

    	ret = encodeImage(img);

    	if( ret == null || ret.length == 0 ) return false;

    	try {

    		if(levelDb.get( URL.getBytes() ) == null ) 
    			levelDb.put( URL.getBytes() , ret );

    		return true;
    		
    	}catch( Exception e ) {

			try{ levelDb.close(); } catch( Exception a ) {}

    		levelDb = null;
    		return false;
    		
    	}
    	
	}
	
	
	private ImageIcon decodeImage( byte[] img ){

		try {

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

	private byte[] encodeImage( ImageIcon imgData ) {
	   		
	   	    Image  img = imgData.getImage();
	   	    BufferedImage bufferedImage = new BufferedImage( img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB );

	   	    Graphics g = bufferedImage.createGraphics();
	   	    g.drawImage(img, 0, 0, null);
	   	    g.dispose();

	   	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   	    try {
	   	    	ImageIO.write(bufferedImage, "jpg", baos);
	   	    }catch(Exception e ) {
	   	    	return null;
	   	    }
	   	    
	   	    return baos.toByteArray();
	}
	   	
	private boolean createConnection(){
		
		Options options = new Options();
		File levelDbDirectory = new File(databasePath);
			
		if( !levelDbDirectory.exists() ) {

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
