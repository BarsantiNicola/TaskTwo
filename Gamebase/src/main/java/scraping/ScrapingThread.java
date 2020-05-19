package scraping;

import logic.StatusCode;
import logic.mongoConnection.MongoConnection;

public class ScrapingThread extends Thread
{
 int IDThread;
 int maxGameID;
 int maxThreads;
 MongoConnection mongo;
 
 public ScrapingThread(int IDThread, int maxGameID, int maxThreads, MongoConnection mongo)
 {
  this.IDThread = IDThread;
  this.maxGameID = maxGameID;
  this.mongo = mongo;
  this.maxThreads = maxThreads;
 }
  
 public void run()
  {
   String description;
   StatusCode addDescriptionStatus = StatusCode.ERR_UNKNOWN;
   int i;
   
   for(i = IDThread; i<maxGameID; i+=maxThreads)
    {     
     try
      { Thread.sleep(50*IDThread); }
     catch(InterruptedException e)
      {}
     
     System.out.println("[Thread " + (IDThread+1) + "]: Attempting to add description for game "+ i);
     
     description = WebScraping.getGameDescription(i);
     addDescriptionStatus = mongo.addGameDescription(i,description);
     if(addDescriptionStatus == StatusCode.ERR_NETWORK_UNREACHABLE) 
      break;
    }
  
   if(addDescriptionStatus == StatusCode.ERR_NETWORK_UNREACHABLE) 
    System.out.println("[Thread " + (IDThread+1) + "]: ERROR! Network unreachable, exiting (exited at game " + i + " (parsed " + ((i/maxGameID)+1) + "games)");
   else
    System.out.println("[Thread " + (IDThread+1) + "]: SUCCESS! (exited at game " + i + " (parsed " + ((i/maxGameID)+1) + "games)");
   
  }
}