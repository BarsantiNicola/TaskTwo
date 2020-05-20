package scraping;

import logic.StatusCode;
import logic.mongoConnection.MongoConnection;

public class ScrapingThread extends Thread
{
 int IDThread;
 int maxGameID;
 int maxThreads;
 MongoConnection mongo;
 
 int alreadyHaveDescription;
 int successfullyAdded;
 int noDescriptionAvailable;
 int failed;
 
 public ScrapingThread(int IDThread, int maxGameID, int maxThreads, MongoConnection mongo)
 {
  this.IDThread = IDThread;
  this.maxGameID = maxGameID;
  this.mongo = mongo;
  this.maxThreads = maxThreads;
  alreadyHaveDescription = 0;
  successfullyAdded = 0;
  noDescriptionAvailable = 0;
  failed = 0;
 }
  
 public void run()
  {
   String description;
   StatusCode mongoAddDescriptionStatus = StatusCode.ERR_UNKNOWN;
   int i;
   
   for(i = IDThread; i<maxGameID; i+=maxThreads)
    {
     if(mongo.hasDescription(i) != StatusCode.ERR_DOCUMENT_NO_DESCR)
      alreadyHaveDescription++;
     else
      {
       //System.out.println("[Thread " + (IDThread+1) + "]: Attempting to add description for game "+ i);
       description = WebScraping.getGameDescription(i);
       if((description == null) || description.equalsIgnoreCase("No description available"))
        {
         System.out.println("[Thread " + (IDThread+1) + "]: ERROR in adding description for game "+ i + " (RAWG has no description)");
         noDescriptionAvailable++;
        }
       else     
        {
         mongoAddDescriptionStatus = mongo.addGameDescription(i,description);
         if(mongoAddDescriptionStatus == StatusCode.OK)
          {
           System.out.println("[Thread " + (IDThread+1) + "]: SUCCESS in adding description for game "+ i);
           successfullyAdded++;
          }
         else
          {
           System.out.println("[Thread " + (IDThread+1) + "]: ERROR in adding description for game "+ i + "(" + mongoAddDescriptionStatus + ")");
           failed++;
          }
        }
       try
        { Thread.sleep(100); }
       catch(InterruptedException e)
        {}
      }
    }
   System.out.println("\n=====================================================================================================");
   System.out.println("[Thread " + (IDThread+1) + "]: Summary:");
   System.out.println("Already have description: " + alreadyHaveDescription + " Successfully Added: " + successfullyAdded);
   System.out.println("No Description Available: " + noDescriptionAvailable + " Failed: " + failed);
   System.out.println("=====================================================================================================\n");
   mongo.closeConnection();
  }
}