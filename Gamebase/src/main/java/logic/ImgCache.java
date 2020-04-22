package logic;

import java.io.File;

import javax.swing.ImageIcon;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import com.google.gson.Gson;

public class ImgCache {
	
	private final String databasePath;
	private DB levelDb;
	private Gson gson;
	
	public ImgCache( String cachePath ){
		
		
		databasePath = cachePath;  
		gson = new Gson();
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
        	
    		return gson.fromJson( new String(image) , ImageIcon.class );
    		
    	}catch( Exception e ) {
    		
			try{ levelDb.close(); } catch( Exception a ) {}
    		levelDb = null;
    		return null;
    		
    	}
		
	}
	
	public boolean cacheImg( String URL , ImageIcon img ) {
		
    	if( levelDb == null )
    		return false;
    	
    	
    	try {
    		
    		if(levelDb.get( URL.getBytes()) == null )
    			levelDb.put( URL.getBytes() , gson.toJson( img ).getBytes());
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
