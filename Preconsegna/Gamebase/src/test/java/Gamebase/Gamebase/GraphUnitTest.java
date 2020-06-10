package Gamebase.Gamebase;

import logic.graphConnector.GraphConnector;

public class GraphUnitTest
{
 //--------------------------------------------------------
 //                      MAIN
 //--------------------------------------------------------
 
 /* Testing purposes */
 public static void main(String[] args) throws Exception
  {
   try(GraphConnector graph = new GraphConnector())
    { 
     //graph.connect("bolt://172.16.0.78:7687","neo4j","password");
     graph.connect("bolt://172.16.0.78:7687","neo4j","password");
     

     //============================================================================================== Connection-related Tests ==============================================================================================
     
     /* Connection Setup (connect(), disconnect(), isConnected(), getDatabaseURI(), getDatabaseUser(), getDatabasePassword())
     System.out.println(graph.disconnect());
     System.out.println(graph.isConnected());
     System.out.println(graph.getDatabaseURI());
     System.out.println(graph.getDatabaseUser());
     System.out.println(graph.getDatabasePassword());
     System.out.println(graph.disconnect());
     
     System.out.println(graph.connect("bolt://localhost:7687","neo4j","password"));
     System.out.println(graph.isConnected());
     System.out.println(graph.connect("bolt://localhost:7687","neo4j","password"));
     System.out.println(graph.getDatabaseURI());
     System.out.println(graph.getDatabaseUser());
     System.out.println(graph.getDatabasePassword());
     */
  
    
     //========================================================================================= Users Registration and Login Tests =========================================================================================
     
     /* Adding new users (register(), getStandardUsersCount(), getAnalystsCount(), getAdministratorsCount(), getGamesCount(), getUser(), getUserType())
     User adri = new User("adri","adriPassword",LocalDate.of(2020,01,02),"Adrienne","LaCroix",new Long(25),"adri@example.com",new Character('F'),"France","Action",null);  //User 1 (all attributes)
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);                                                 //User 2 (some attributes)
     
     System.out.println("StandardUsers = " + graph.getStandardUsersCount() + ", Analysts = " + graph.getAnalystsCount() + ", Administrators = " + graph.getAdministratorsCount() + ", Total = " + graph.getTotalUsersCount());
     System.out.println("Games = " + graph.getGamesCount());
     System.out.println(graph.getUser());
     System.out.println(graph.getUserType());
     
     System.out.println(graph.register(adri));       //Register as User 1
     
     System.out.println(graph.getUser());
     System.out.println(graph.getUserType());
     System.out.println("StandardUsers = " + graph.getStandardUsersCount() + ", Analysts = " + graph.getAnalystsCount() + ", Administrators = " + graph.getAdministratorsCount() + ", Total = " + graph.getTotalUsersCount());
     System.out.println("Games = " + graph.getGamesCount());
   
     System.out.println(graph.register(brad));       //Attempt to register as User2
     */
       
   
     /* User login (login(), logout() isLoggedIn())
     User adri = new User("adri","adriPassword",LocalDate.of(2020,01,02),"Adrienne","LaCroix",new Long(25),"adri@example.com",new Character('F'),"France","Action",null);  //User 1 (all attributes)
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);                                                 //User 2 (some attributes)
     
     System.out.println(graph.isConnected() + "," + graph.isLoggedIn());                  
     System.out.println(graph.register(adri));                             //Register as User 1
     System.out.println(graph.isConnected() + "," + graph.isLoggedIn()); 
     System.out.println(graph.logout());                                   //Logout as User 1
     System.out.println(graph.isConnected() + "," + graph.isLoggedIn()); 
     System.out.println(graph.register(brad));                             //Register as User 2
     System.out.println(graph.logout());                                   //Logout as User 2
     
     System.out.println(graph.login("adri","adriPassword"));               //Login as User 1
     System.out.println("StandardUsers = " + graph.getStandardUsersCount() + ", Analysts = " + graph.getAnalystsCount() + ", Administrators = " + graph.getAdministratorsCount() + ", Total = " + graph.getTotalUsersCount());
     System.out.println(graph.login("brad","bradPassword"));               //Attempt to login as User 2
     */
   
     
     //============================================================================================== Users Manipulation Tests ==============================================================================================
     
     /* saveUser()
     User adri = new User("adri","adriPassword",LocalDate.of(2020,01,02),"Adrienne","LaCroix",new Long(25),"adri@example.com",new Character('F'),"France","Action",null);  //User 1 (all attributes)
     StatusObject<UserInfo> me = graph.register(adri);        //Register as User 1
     graph.logout();
     me = graph.login("adri","adriPassword");
     System.out.println(me.element.user);               //Print the User1 information
     
     //Update the User 1 information
     me.element.user.setFirstName("Luigi");
     me.element.user.setLastName("Bianchi");
     me.element.user.setAge((long)39);
     me.element.user.setEmail("luigi@example.com");
     me.element.user.setGender('M');
     me.element.user.setCountry("Spain");
     me.element.user.setFavouriteGenre("RPG");
     
     System.out.println(graph.saveUser());     //Save the updated User1 information
     System.out.println(me.element.user);      //Print the User1 information
     graph.logout();                           //Logout as User 1
     me = graph.login("adri","adriPassword");  //Login as User1
     System.out.println(me.element.user);      //Print the User1 information
     */
     
     
     /* deleteUser()
     //The User 1 (adri) is supposed to be registered in the application as an administrator 
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null); //User 2 (some attributes)
     graph.register(brad);                                    //Register as User 2
     graph.login("brad","bradPassword");                      //Login as User 2 (fallback)
     System.out.println(graph.deleteUser("brad"));            //NOTALLOWED removal attempt
     graph.logout();                                          //Logout as User 2
     
     System.out.println(graph.deleteUser("brad"));            //NOTLOGGED removal attempt
     System.out.println(graph.login("adri","adriPassword"));  //Login as User 1 (administrator)
     System.out.println("StandardUsers = " + graph.getStandardUsersCount() + ", Analysts = " + graph.getAnalystsCount() + ", Administrators = " + graph.getAdministratorsCount() + ", Total = " + graph.getTotalUsersCount());
     System.out.println(graph.deleteUser("brad"));            //Delete User 2
     System.out.println("StandardUsers = " + graph.getStandardUsersCount() + ", Analysts = " + graph.getAnalystsCount() + ", Administrators = " + graph.getAdministratorsCount() + ", Total = " + graph.getTotalUsersCount());
     */
    
     
     /* upgradeToAnalyst()
     User adri = new User("adri","adriPassword",LocalDate.of(2020,01,02),"Adrienne","LaCroix",new Long(25),"adri@example.com",new Character('F'),"France","Action",null);  //User 1 (all attributes)
     System.out.println(graph.register(adri));                             //Register as User 1
     System.out.println(graph.login("adri","adriPassword"));               //Login as User 1 (fallback)
     System.out.println(graph.upgradeToAnalyst());                         //Upgrade the User to Analyst
     */
     
     
     //============================================================================================== Users Relationships API ==============================================================================================
     
     /* Users Follow Functions (followUser(), unFollowUser(), doIFollow(), getFollowedUsersList())
     User adri = new User("adri","adriPassword",LocalDate.of(2020,01,02),"Adrienne","LaCroix",new Long(25),"adri@example.com",new Character('F'),"France","Action",null);  //User 1
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);                                                 //User 2
     User maria = new User("maria","mariaPassword",LocalDate.of(2020,03,17),"Maria","Peterson",null,"maria@example.com",null,null,"Puzzle",null);                             //User 3 
     
     //Register all users
     graph.register(adri);
     System.out.println(graph.getFollowedUsersList());
     graph.logout();
     graph.register(brad);
     graph.logout();    
     graph.register(maria);
     graph.logout();
     
     //Create some :FOLLOW relationships
     graph.login("brad","bradPassword");
     graph.followUser("adri");
     graph.followUser("maria");
     graph.logout();
     graph.login("maria","mariaPassword");
     graph.followUser("adri");
     graph.logout();
     
     
     graph.login("brad","bradPassword");
     System.out.println(graph.doIFollow("maria"));
     System.out.println(graph.getFollowedUsersList());
     System.out.println(graph.unFollowUser("maria"));
     System.out.println(graph.doIFollow("maria"));
     System.out.println(graph.getFollowedUsersList());
     */
     
     
     /* searchUsers()
     User adrian = new User("adrian","adrianPassword",LocalDate.of(2019,12,27),"Adrian","Johnson",new Long(32),"adrian@example.com",new Character('M'),"United States",null,null);  //User 1
     User maria = new User("maria","mariaPassword",LocalDate.of(2020,03,17),"Maria","Peterson",null,"maria@example.com",null,null,"Puzzle",null);                                      //User 2
     User Marianne = new User("Marianne","MariannePassword",LocalDate.of(2020,03,21),null,null,new Long(29),"Marianne@example.com",new Character('F'),"France","RPG",null);         //User 3
     
     //Register all users
     graph.register(adrian);
     graph.logout();
     graph.register(maria);
     graph.logout();
     graph.register(Marianne);
     graph.logout();
     
     //Perform the search with different Users
     graph.login("adrian","adrianPassword");
     System.out.println(graph.searchUsers("ria"));
     graph.logout();
     graph.login("maria","mariaPassword");
     System.out.println(graph.searchUsers("ria"));
     graph.logout();
     graph.login("Marianne","MariannePassword");
     System.out.println(graph.searchUsers("ria"));
     graph.logout();    
     */
   

     /* getSuggestedUsers()
     User adri = new User("adri","adriPassword",LocalDate.of(2020,01,02),"Adrienne","LaCroix",new Long(25),"adri@example.com",new Character('F'),"France","Action",null);  //User 1
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);                                                 //User 2
     User maria = new User("maria","mariaPassword",LocalDate.of(2020,03,17),"Maria","Peterson",null,"maria@example.com",null,null,"Puzzle",null);                             //User 3 
     
     //Register all users
     graph.register(adri);
     graph.logout();
     graph.register(brad);
     graph.logout();    
     graph.register(maria);
     graph.logout();
     
     //Create some :FOLLOW relationships
     graph.login("brad","bradPassword");
     graph.followUser("adri");
     graph.followUser("maria");
     graph.logout();
     graph.login("maria","mariaPassword");
     graph.followUser("adri");
     graph.logout();
     

     graph.login("adri","adriPassword");
     System.out.println(graph.getSuggestedUsersList());
     System.out.println(graph.getSuggestedUsersList(1));
     graph.followUser("maria");
     System.out.println(graph.getSuggestedUsersList());
     graph.unFollowUser("maria");
     */


     //================================================================================================= Games-related API =================================================================================================
     
     /* Games management (addGame(), deleteGame(), getGamesCount())
     //The User 1 (adri) is supposed to be registered in the application as an administrator 
     GraphGame game1 = new GraphGame("0","mario","https://marioImage.png");    //Game 1  
     GraphGame game2 = new GraphGame("1","luigi");                             //Game 2 (no PreviewImage)  
     graph.login("adri","adriPassword");
     System.out.println(graph.getGamesCount());
     
     System.out.println(graph.addGame(game1));
     System.out.println(graph.getGamesCount());
     System.out.println(graph.addGame(game2));
     System.out.println(graph.getGamesCount());
     
     System.out.println(graph.deleteGame("0"));
     System.out.println(graph.deleteGame("1"));
     System.out.println(graph.getGamesCount());
     */


     /* Games Favourites Functions (addToFavourites(), removeFromFavourites(), doIFavourite(), getFavouriteGamesList())
     //The User 1 (adri) is supposed to be registered in the application as an administrator 
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);  //User 2
     GraphGame game1 = new GraphGame("0","mario","https://marioImage.png");    //Game 1  
     GraphGame game2 = new GraphGame("1","luigi","https://luigiImage.png");    //Game 2        
     
     //Add User 2 and the two games to the database
     graph.register(brad);
     graph.logout();
     graph.login("adri","adriPassword");      
     graph.addGame(game1);
     graph.addGame(game2);
     graph.logout();
     
     graph.login("brad","bradPassword");
     System.out.println(graph.getFavouritesGamesList());
     System.out.println(graph.doIFavourite("0"));
     System.out.println(graph.addToFavourites("0"));
     System.out.println(graph.doIFavourite("0"));
     System.out.println(graph.addToFavourites("1"));
     System.out.println(graph.getFavouritesGamesList());
     System.out.println(graph.removeFromFavourites("0"));
     System.out.println(graph.doIFavourite("0"));
     System.out.println(graph.getFavouritesGamesList());
     */
     
     
     /* getFavouritesGamesList(username)
     //The User 1 (adri) is supposed to be registered in the application as an administrator 
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);                     //User 2
     User maria = new User("maria","mariaPassword",LocalDate.of(2020,03,17),"Maria","Peterson",null,"maria@example.com",null,null,"Puzzle",null); //User 3
     GraphGame game1 = new GraphGame("0","mario","https://marioImage.png");    //Game 1  
     GraphGame game2 = new GraphGame("1","luigi","https://luigiImage.png");    //Game 2    
     GraphGame game3 = new GraphGame("2","peach","https://peachImage.png");    //Game 3   
     
     
     //Add the users and the games to the database
     graph.register(brad);
     graph.logout();
     graph.register(maria);
     graph.logout();
     graph.login("adri","adriPassword");      
     graph.addGame(game1);
     graph.addGame(game2);
     graph.addGame(game3);
     graph.logout();
     
     //Add some favourite games and follow relationships
     graph.login("brad","bradPassword");
     graph.followUser("maria");
     graph.addToFavourites("0");
     graph.logout();
     graph.login("maria","mariaPassword");
     graph.addToFavourites("1");
     graph.addToFavourites("2");
     graph.logout();
     
     graph.login("brad","bradPassword");
     System.out.println(graph.getFollowedUsersList());
     System.out.println(graph.getFavouritesGamesList("maria"));
     graph.logout();
   
     graph.login("adri","adriPassword");
     System.out.println(graph.getFavouritesGamesList("brad"));
     System.out.println(graph.getFavouritesGamesList("maria"));
     */
     
     
     /* getGameFavouriteCount()
     //The User 1 (adri) is supposed to be registered in the application as an administrator 
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);                     //User 2
     GraphGame game1 = new GraphGame("0","mario","https://marioImage.png");    //Game 1  
     GraphGame game2 = new GraphGame("1","luigi","https://luigiImage.png");    //Game 2   
     
     //Add the User 2 and the games to the database
     graph.register(brad);
     graph.logout();
     graph.login("adri","adriPassword");      
     graph.addGame(game1);
     graph.addGame(game2);
     graph.logout();
     
     //Add some favourite games
     graph.login("adri","adriPassword");      
     graph.addToFavourites("0");
     graph.logout();
     graph.login("brad","bradPassword");
     graph.addToFavourites("0");
     graph.addToFavourites("1");
     graph.logout();
     
     graph.login("brad","bradPassword");
     System.out.println(graph.getGameFavouriteCount("0"));
     System.out.println(graph.getGameFavouriteCount("1"));
     System.out.println(graph.removeFromFavourites("0"));
     System.out.println(graph.removeFromFavourites("1"));
     System.out.println(graph.getGameFavouriteCount("0"));
     System.out.println(graph.getGameFavouriteCount("1"));
     */
     
     
     /* rateGame()
     //The User 1 (adri) is supposed to be registered in the application as an administrator 
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null); //User 2
     GraphGame game1 = new GraphGame("0","mario","https://marioImage.png");    //Game 1   
     
     //Add the user and the game to the database
     graph.register(brad);
     graph.logout();
     graph.login("adri","adriPassword");      
     graph.addGame(game1);
     graph.logout();
     
     graph.login("brad","bradPassword");
     System.out.println(graph.getFavouritesGamesList());
     graph.addToFavourites("0");
     System.out.println(graph.getFavouritesGamesList());
     System.out.println(graph.rateGame("0",3));      
     System.out.println(graph.getFavouritesGamesList());
     graph.removeFromFavourites("0");
     */
     
     
     /* getFeaturedGamesList()
     //The User 1 (adri) is supposed to be registered in the application as an administrator 
     User brad = new User("brad","bradPassword",null,null,null,null,null,new Character('M'),"United States","Platform",null);                      //User 2
     User maria = new User("maria","mariaPassword",LocalDate.of(2020,03,17),"Maria","Peterson",null,"maria@example.com",null,null,"Puzzle",null);  //User 3 
     GraphGame game1 = new GraphGame("0","mario","https://marioImage.png");    //Game 1  
     GraphGame game2 = new GraphGame("1","luigi","https://luigiImage.png");    //Game 2    
     GraphGame game3 = new GraphGame("2","peach","https://peachImage.png");    //Game 3   
     GraphGame game4 = new GraphGame("3","bowser");                            //Game 4
     
     
     //Register all users and add all the games
     graph.login("adri","adriPassword");
     graph.addGame(game1);
     graph.addGame(game2);
     graph.addGame(game3);
     graph.addGame(game4);
     graph.logout();
     graph.register(brad);
     graph.logout();    
     graph.register(maria);
     graph.logout();
     
     //Add some favourite games and follow relationships
     graph.login("adri","adriPassword");
     graph.addToFavourites("0");
     graph.addToFavourites("2");
     graph.logout();
     graph.login("brad","bradPassword");
     graph.addToFavourites("3");
     graph.followUser("maria");
     graph.logout();
     graph.login("maria","mariaPassword");
     graph.addToFavourites("0");
     graph.addToFavourites("1");
     graph.addToFavourites("3");
     graph.logout();
     
     graph.login("brad","bradPassword");  
     System.out.println(graph.getFeaturedGamesList());
     graph.addToFavourites("0");
     System.out.println(graph.getFeaturedGamesList(2));
     graph.removeFromFavourites("0");
     */
     
     
     //=============================================================================================== Analytics-related API ===============================================================================================
      
     /* getMostFollowedUsers(), getMostFavouriteGames()
     //The full dataset must be used for these tests

     
     System.out.println(graph.login("adrian","adrianPassword"));  //adrian is an Analyst
     System.out.println(graph.getMostFollowedUsers()); 
     System.out.println(graph.getMostFavouriteGames());
     System.out.println(graph.getUsersSummaryStats());
     */
    }
  }
 
}