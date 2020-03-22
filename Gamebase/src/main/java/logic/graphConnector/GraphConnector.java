package logic.graphConnector;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import static org.neo4j.driver.Values.parameters;

import logic.data.*;
import logic.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/* This class represents the connector of the GameBase application to an associated Neo4j database */
public class GraphConnector implements GraphInterface,AutoCloseable
 { 
  //---------------------------------------------------------------------------------
  //                                 Attributes
  //---------------------------------------------------------------------------------
  
  //------------------------Database Connection Attributes---------------------------
  private Driver driver;                          //The driver used to connect to the Neo4j database (also used to determine whether the user is connected to the database or not)
  private String databaseURI;                     //The URI of the Neo4j database to connect to
  private String databaseUser;                    //The user used to authenticate into the Neo4j database
  private String databasePassword;                //The password used to authenticate into the Neo4j database

  //---------------------------User Session Attributes-------------------------------
  
  //User Information Attributes
  private User _user;                             //The user currently authenticated into the database 
  private UserType _userType = UserType.NO_USER;  //The privilege level of the current user into the application (also used to determine whether the user is logged into the application or not)
  
  //User Lists Attributes
  private List<User> _followedUsersList;          //The list of users followed by the current user              (where their UserTypes, passwords and e-mails are hidden)
  private List<User> _suggestedUsersList;         //The list of users following suggestion for the current user (where their UserTypes, passwords  and e-mails are hidden)
  private List<GraphGame> _favouritesGamesList;   //The list of the user favourite games
  private List<GraphGame> _featuredGamesList;     //The list of featured games proposed to the user
  
  //Node Counters Attributes
  private long standardUsersCount    = -1;        //The number of Standard Users registered into the database  Visible by: ALL
  private long analystsCount         = -1;        //The number of Analysts registered into the database        Visible by: ADMINISTRATORS
  private long administratorsCount   = -1;        //The number of Administrators registered into the database  Visible by  ADMINISTRATORS
  private long totalUsersCount       = -1;        //The total number of Users registered into the database     Visible by  ADMINISTRATORS
  private long gamesCount            = -1;        //The total number of games registered in the database       Visible by: ALL
 
  
  
 /*==================================================================================================================================================*
  |                                                                                                                                                  |
  |                                                     PUBLIC API IMPLEMENTATION                                                                    |
  |                                                                                                                                                  |
  *==================================================================================================================================================*/
  
  //---------------------------------------------------------------------------------
  //                                Constructor
  //---------------------------------------------------------------------------------
  
  /* Default Constructor (which performs no operation) */
  public GraphConnector()
   {}
  
  
  //---------------------------------------------------------------------------------
  //                            Connection-related API
  //---------------------------------------------------------------------------------
  
  /* DESCRIPTION: Attempts up to "maxRetries" every "retryIntervalms" millisecond to connect to the Neo4j database specified in the "uri" argument, authenticating via the "user" and "password" arguments
   *              
   * ARGUMENTS:   - String uri:                       The URI of the Neo4j database to connect to
   *              - String user:                      The user used to authenticate into the Neo4j database
   *              - String password:                  The password used to authenticate into the Neo4j database
   *              - int maxRetries:                   The maximum number of connection retries to perform before giving up on connecting to the database (default = 5) 
   *              - int retryIntervalms:              The time interval in millisecond between each connection attempt (default = 1000)
   *
   * CALLABLE BY: ALL users that have not yet connected to the database
   * 
   * RETURNS:     - OK:                               The connection to the Neo4j database was established successfully 
   *              - ERR_GRAPH_CONN_ALREADYCONNECTED:  This Connector is already connected to a Neo4j database
   *              - ERR_GRAPH_MISSINGARGUMENTS:       Insufficient arguments to attempt to connect to the Neo4j database (missing database URI, username or password)
   *              - ERR_GRAPH_CONN_RETRYARGUMENTS:    The passed "maxRetries" or the "retryIntervalMs" arguments are negative
   *              - ERR_GRAPH_CONN_CANTCONNECT:       The connector couldn't connect to the target Neo4j database
   */
  public StatusCode connect(String uri, String user, String password,int maxRetries, long retryIntervalMs)
   {
    //Check whether the Connector is already connected to a Neo4j database
    if(this.driver != null)
     return StatusCode.ERR_GRAPH_CONN_ALREADYCONNECTED;
    
    //Check whether enough arguments to attempt to connect to the Neo4j database were provided (Database URI, username and password)
    if((uri==null)||(user==null)||(password==null))
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
    
    //Check the maxRetries and retryIntervalMs parameters to be non-negative
    if((maxRetries<0)||(retryIntervalMs<0))
     return StatusCode.ERR_GRAPH_CONN_NEGATIVERETRIES;
    
    //Initialize the database connection attributes
    this.databaseURI = uri;
    this.databaseUser = user;
    this.databasePassword = password;

    //Attempt every "retryIntervalMs" ms and up "maxRetries" times to connect with the Neo4j database
    return tryConnect(maxRetries,retryIntervalMs);
   }
       

  /* Overloaded connect() function with default parameters maxRetries = 5 and retryLongIntervalMs = 1000 */
  public StatusCode connect(String uri, String user, String password)
   { return connect(uri, user, password, 5, 1000); }
  
  
  /* DESCRIPTION: Disconnects the connector from the current Neo4j database
   * 
   * CALLABLE BY: ALL users that have connected to the database
   * 
   * RETURNS:     - OK:                          Connector successfully disconnected from the Neo4j database 
   *              - ERR_GRAPH_CONN_NOTCONNECTED: The Connector is not connected to a Neo4j database
   *              
   * NOTE:        This method (or the equivalent method "close") MUST be called once all operations involving
   *              the Neo4j database have finished, in order to release the system resources used for the connection              
   */
  public StatusCode disconnect()
   {
    //Check whether the Connector is connected to a Neo4j database
    if(this.driver==null)
     return StatusCode.ERR_GRAPH_CONN_NOTCONNECTED;
    else
     try
      {
       //Attempt to disconnect the Connector from the database
       driver.close();
       
       //If the close() function didn't raise an exception, the Connector successfully closed all connection resources
       System.out.println("[GraphConnector]: Closed connection with the Neo4j database @" + databaseURI);  
       return StatusCode.OK;
      }
     catch(Exception e)
      { 
       //If the close() function did raise an exception, a fatal error has occured
       System.out.println("[GraphConnector]: FATAL ERROR in closing the connection with the Neo4j database @" + databaseURI + ": " + e.getMessage());
       return StatusCode.ERR_GRAPH_UNKNOWN;
      }
     finally
      {
       //Reset the database connection attributes
       this.driver = null;
       this.databaseURI = null;
       this.databaseUser = null;
       this.databasePassword = null;
       
       //Reset the user session attributes
       this._user = null;
       this._userType = UserType.NO_USER;
       
       this._followedUsersList = null;
       this._suggestedUsersList = null;
       this._favouritesGamesList = null;
       this._featuredGamesList = null;
       
       this.standardUsersCount    = -1;
       this.analystsCount         = -1;
       this.administratorsCount   = -1;
       this.totalUsersCount       = -1;
       this.gamesCount            = -1;
      }
   }
  
  
  /* Required by the AutoCloseable interface (proxies the disconnect() function)*/
  @Override
  public void close()
   { disconnect(); }
  
  
  //---------------------------Connection Utility Functions----------------------------
  
  /* DESCRIPTION: Returns a boolean representing whether the Connector is currently connected to a Neo4j database (true) or not (false)
   * 
   * CALLABLE BY: ALL
   * 
   * RETURNS:     A boolean representing whether the Connector is connected to a Neo4j database (true) or not (false)
   */
  public boolean isConnected()
  {
   if(this.driver==null)
    return false;
   try
    {
     driver.verifyConnectivity();  //If this function doesn't raise an exception, the Connector is connected to the database
     return true;
    }
   catch(Exception e)
    { return false; }              //If verifyConnectivity() did raise an exception, the Connector is NOT connected to the database
  }
  

  /* DESCRIPTION: Returns the database URI associated with the connector
   * 
   * CALLABLE BY: ALL
   * 
   * RETURNS:     The database URI associated with the connector (possibly null)
   */
  public String getDatabaseURI()                    
   { return databaseURI; }
  
  
  /* DESCRIPTION: Returns the database user used by the connector
   * 
   * CALLABLE BY: ALL
   * 
   * RETURNS:     The database user used with the connector (possibly null)
   */ 
  public String getDatabaseUser()
   { return databaseUser; }
  
  
  /* DESCRIPTION: Returns the database password used by the connector
   * 
   * CALLABLE BY: ALL
   * 
   * RETURNS:     The database password used with the connector (possibly null)
   */
  public String getDatabasePassword()
   { return databasePassword; }
  
  
  //---------------------------------------------------------------------------------
  //                       Users Registration and Login API
  //--------------------------------------------------------------------------------- 
 
  /* DESCRIPTION: Attempts to register a new user into the database as a Standard User, logging him into the application on success
   *
   * ARGUMENTS:   - User user: The object containing the information of the user willing to register within the database
   * 
   * CALLABLE BY: ALL connected users that have not yet logged within the application
   *    
   * RETURNS:     An StatusObject<UserInfo> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                              New user successfully added to the database as a Standard User and logged into the application
   *                 - ERR_GRAPH_CONN_NOTCONNECTED:     The Connector is not connected to a Neo4j database
   *                 - ERR_GRAPH_USER_ALREADYLOGGED:    The user is already logged within the application
   *                 - ERR_GRAPH_MISSINGARGUMENTS:      Not enough arguments to register the new user into the database (missing username or password)
   *                 - ERR_GRAPH_USER_ALREADYEXISTS:    An user with the specified username already exists within the database
   *              2) The UserType of the newly created user (UserType.USER, or UserType.NO_USER on failure)
   *              3) The User object representing the user in the context of the application (or "null" in case of failure)
   *              
   * NOTES:       1) On success ALL further modifications and operations involving the user MUST be performed on the User object returned by the function 
   *              2) The "joinDate" and "followedCount" of the User argument are ignored and overridden respectively to the current date (LocalDate.now()) and to 0
   *              3) On a success the User password is encrypted via the SHA-256 algorithm before being stored and is hidden from the returned User object              
   */
  public StatusObject<UserInfo> register(User user)
   {
    //Check whether the Connector is connected to a Neo4j database
    if(this.driver==null)
     return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_CONN_NOTCONNECTED);
    
    //Check whether the user is already logged within the application
    if(_userType != UserType.NO_USER)
     return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_ALREADYLOGGED);
    
    //Check whether there are enough arguments to attempt the new user registration (the username and password)
    if((user==null)||(user.getUsername() == null)||(user.getPassword() == null))
     return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_MISSINGARGUMENTS);
    
    try(Session session = driver.session())
     {
      //Check whether a user with such username already exists within the database
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadUser(tx,user.getUsername(),false); }
       } );
      if(record!=null)
       return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_ALREADYEXISTS);
      else
       {
        //Crypt the user password
        String cryptedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(user.getPassword());
        
        //Insert the new user into the database as a Standard User
        session.writeTransaction(new TransactionWork<Void>()
         {
          @Override
          public Void execute(Transaction tx)
           {
            tx.run("CREATE (n:user {username: $username, password: $cryptedPassword, "                 +
                                   "joinDate: $joinDate, firstName: $firstName, lastName: $lastName, " +
                                   "age: $age, email: $email, gender: $gender, country: $country,"     +
                                   "favouriteGenre: $favouriteGenre, followedCount: 0})",
                   parameters("username",user.getUsername(),"cryptedPassword",cryptedPassword,
                              "joinDate",LocalDate.now(),"firstName",user.getFirstName(),
                              "lastName",user.getLastName(),"age",user.getAge(),
                              "email",user.getEmail(),"gender",user.getGender(),
                              "country",user.getCountry(),"favouriteGenre",user.getFavouriteGenre()));
            return null;
           }
         } );
        
        //Initialize the User information attributes
        _user = new User(user.getUsername(),"x",LocalDate.now(),user.getFirstName(),user.getLastName(),user.getAge(),user.getEmail(),user.getGender(),user.getCountry(),user.getFavouriteGenre(),new Long(0));
        _userType = UserType.USER;
        
        //Initialize the user list attributes
        _followedUsersList = new ArrayList<>();      //Followed users list
        _favouritesGamesList = new ArrayList<>();    //Favourite games list
        _featuredGamesList = new ArrayList<>();      //Featured games list
        
        //Initialize the number of Standard Users and Games in the database
        loadUsersCount(session);
        loadGamesCount(session);
          
        return new StatusObject<UserInfo>(StatusCode.OK,new UserInfo(UserType.USER,_user));
       }
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in register(): " + e.getMessage());
      System.out.println("[GraphConnector]: User = " + user);
      return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   }


   /* DESCRIPTION: Attempts to authenticate (login) a user within the application, returning his related information on success
    *
    * ARGUMENTS:   - String username: The user's username
    *              - String password: The user's password
    * 
    * CALLABLE BY: ALL connected users that have not yet logged within the application
    * 
    * RETURNS:     An StatusObject<UserInfo> object composed of:
    *              1) A StatusCode, representing the result of the operation:
    *                 - OK:                             The user authentication was successful
    *                 - ERR_GRAPH_CONN_NOTCONNECTED:    The Connector is not connected to a Neo4j database
    *                 - ERR_GRAPH_USER_ALREADYLOGGED:   The user is already logged within the application
    *                 - ERR_GRAPH_MISSINGARGUMENTS:     Not enough arguments to authenticate the user within the application (missing username or password)
    *                 - ERR_GRAPH_USER_NOTEXISTS:       The user doesn't exist within the database
    *                 - ERR_GRAPH_USER_WRONGPASSWORD:   The user exists within the database, but the provided password is wrong
    *                 - ERR_GRAPH_USER_UNKNOWNTYPE:     The user exists within the database, but its UserType is unknown (possible inconsistency within the database)
    *              2) The UserType of the newly created user (UserType.USER, or UserType.NO_USER on failure)
    *              3) The User object representing the user in the context of the application (or "null" in case of failure)
    *              
    * NOTES:       1) On success ALL further modifications and operations involving the user MUST be performed on the User object returned by the function 
    *              2) On a successful authentication, the user's password is hidden from the returned User object
    */
   public StatusObject<UserInfo> login(String username, String password)
    {
     //Check whether the Connector is connected to a Neo4j database
     if(this.driver==null)
      return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_CONN_NOTCONNECTED);
     
     //Check whether the user is already logged within the application
     if(_userType != UserType.NO_USER)
      return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_ALREADYLOGGED);
     
     //Check whether there are enough arguments to authenticate the user (the username and password)
     if((username == null)||(password == null))
      return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_MISSINGARGUMENTS);
     
     try(Session session = driver.session())
      {
       //Check whether a user with such username already exists within the database
       Record record = session.readTransaction(new TransactionWork<Record>()
        {
         @Override
         public Record execute(Transaction tx)
          { return checkLoadUser(tx,username,true); }
        } );
       if(record==null)
        return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_NOTEXISTS);
       else
        {
         //Otherwise, check whether the provided password is correct
         String cryptedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
         
         if(!cryptedPassword.equals(record.get("n.password").asString()))
          return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_WRONGPASSWORD);
         else  //Otherwise login the user within the application and return its information
          {
           //Initialize the UserType and instantiate the user object of the appropriate subclass with the user final attributes
           String userType = record.get("labels(n)").asList().get(0).toString();
           if(userType.equals("administrator"))
            {
             _user = new Administrator(username,"x",record.get("n.joinDate").asLocalDate());
             _userType = UserType.ADMINISTRATOR;
            }
           else
            if(userType.equals("analyst"))
             {
              _user = new Analyst(username,"x",record.get("n.joinDate").asLocalDate());
              _userType = UserType.ANALYST;
             }             
            else
             if(userType.equals("user"))
              {
               _user = new User(username,"x",record.get("n.joinDate").asLocalDate());
               _userType = UserType.USER;
              }
             else  //If the UserType of the user into the database is unknown, return an error
              return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_UNKNOWNTYPE);
           
           //Initialize the user variable attributes
           _user.setFirstName(record.get("n.firstName").asString());
           _user.setLastName(record.get("n.lastName").asString());
           if((record.get("n.age").asObject() != null))
            _user.setAge(new Long(record.get("n.age").asLong()));
           _user.setEmail(record.get("n.email").asString());
           if((record.get("n.gender").asObject() != null))
            _user.setGender(new Character(record.get("n.gender").asString().charAt(0)));
           _user.setCountry(record.get("n.country").asString());
           _user.setFavouriteGenre(record.get("n.favouriteGenre").asString());
           _user.setFollowedCount(record.get("n.followedCount").asLong());    
          
           //Load the user followed users and favourite games lists
           loadFollowedUsersList(session);
           loadFavouriteGamesList(session);
           
           //If the user is an administrator initialize all the nodes counters attributes, otherwise just the Standard Users and Games counters
           if(_userType == UserType.ADMINISTRATOR)
            loadTotalNodesCount();
           else
            {
             loadUsersCount(session);
             loadGamesCount(session);
            }
           
           return new StatusObject<UserInfo>(StatusCode.OK,new UserInfo(_userType,_user));
          }
        }
      }
     catch(Exception e)
      {
       System.out.println("[GraphConnector]: Error in login(): " + e.getMessage());
       System.out.println("[GraphConnector]: username = " + username + ", password = " + password);
       return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_UNKNOWN);
      }
   }
  
   
   /* DESCRIPTION: Logs out the user from the application
    * 
    * CALLABLE BY: ALL connected users logged into the application
    * 
    * RETURNS:     - OK:                        User successfully logged out from the application
    *              - ERR_GRAPH_USER_NOTLOGGED:  The user is not logged into the application
    */
   public StatusCode logout()
    {  
     //Check whether the user is logged into the application
     if(_userType == UserType.NO_USER)
      return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
     else
      {
       //Reset the user session attributes
       this._user = null;
       this._userType = UserType.NO_USER;
       
       this._followedUsersList = null;
       this._suggestedUsersList = null;
       this._favouritesGamesList = null;
       this._featuredGamesList = null;
       
       this.standardUsersCount    = -1;
       this.analystsCount         = -1;
       this.administratorsCount   = -1;
       this.totalUsersCount       = -1;
       this.gamesCount            = -1;

       return StatusCode.OK;
      }
    }   
    
   
   /* DESCRIPTION: Loads the Node Counter Attributes (number of users divided by type and number of games) from the database
    * 
    * CALLABLE BY: ADMINISTRATORS;
    * 
    * RETURNS:     - OK:                          Node counters loaded succesfully
    *              - ERR_GRAPH_USER_NOTALLOWED:   Insufficient privileges to perform the operation (the user is not an administrator)
    *              
    * NOTE:        This function is called automatically whenever an administrator logs in, and can be called again to refresh the node counter attributes
    */ 
   public StatusCode loadTotalNodesCount()
    {
     //Check if the user has sufficient privileges to perform the operation (i.e. it's an administrator)
     if(_userType != UserType.ADMINISTRATOR)
      return StatusCode.ERR_GRAPH_USER_NOTALLOWED;
     
     try(Session session = driver.session())
      {
       //Retrieve the Standard Users count
       standardUsersCount = session.readTransaction(new TransactionWork<Long>()
        {
         @Override
         public Long execute(Transaction tx)
          {
           Result result = tx.run("MATCH (n:user)"+
                                  "RETURN COUNT(n)");
           return (result.single().get(0).asLong());
          }
        } );
       
       //Retrieve the Analysts count
       analystsCount = session.readTransaction(new TransactionWork<Long>()
        {
         @Override
         public Long execute(Transaction tx)
          {
           Result result = tx.run("MATCH (n:analyst)"+
                                  "RETURN COUNT(n)");
           return (result.single().get(0).asLong());
          }
        } );
       
       //Retrieve the Administrators count
       administratorsCount = session.readTransaction(new TransactionWork<Long>()
        {
         @Override
         public Long execute(Transaction tx)
          {
           Result result = tx.run("MATCH (n:administrator)"+
                                  "RETURN COUNT(n)");
           return (result.single().get(0).asLong());
          }
        } );
       
       //Set the total users count
       totalUsersCount = standardUsersCount + analystsCount + administratorsCount;
       
       //Retrieve the Games count
       loadGamesCount(session);
       
       return StatusCode.OK;
      } 
     catch(Exception e)
      {
       System.out.println("[GraphConnector]: Error in loadTotalNodesCount(): " + e.getMessage());
       return StatusCode.ERR_GRAPH_UNKNOWN;
      }
    }
   
  
   //-------------------------User Session Utility Functions---------------------------
      
   /* DESCRIPTION: Checks whether the user is logged into the application
    * 
    * CALLABLE BY: ALL
    * 
    * RETURNS:     A boolean representing whether the user is logged into the application (true) or not (false)
    */  
   public boolean isLoggedIn()
    { return (_userType != UserType.NO_USER); }
   
   
   /* DESCRIPTION: Returns the User object associated to user currently logged within the application
    * 
    * CALLABLE BY: ALL
    * 
    * RETURNS:     The User object associated to user currently logged within the application (possibly null)
    */  
   public User getUser() 
    { return _user; }
   
   
   /* DESCRIPTION: Returns the UserType of the user currently logged within the application
    * 
    * CALLABLE BY: ALL
    * 
    * RETURNS:     The UserType of the user currently logged within the application (possibly UserType.NO_USER)
    */  
   public UserType getUserType()
    { return _userType; }
   
   
   /* DESCRIPTION: Returns the number of Standard Users registered within the application
    * 
    * CALLABLE BY: ALL connected users logged into the application
    * 
    * RETURNS:     An StatusObject<Long> object composed of:
    *              1) A StatusCode, representing the result of the operation:
    *                 - OK:                         Number of Standard Users registered within the application correctly retrieved
    *                 - ERR_GRAPH_USER_NOTLOGGED:   The user is not logged into the application   
    *              2) The number of Standard Users registered within the application (or null if the operation failed)
    *              
    * NOTE:        For non-administrators the number of Standard Users registered within the application is fetched only at their
    *              registration or login, while administrators can refresh such value by calling the loadTotalNodesCount() function            
    */    
   public StatusObject<Long> getStandardUsersCount()
    {
     //Check if the user is logged within the application
     if(_userType == UserType.NO_USER)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
     
     return new StatusObject<Long>(StatusCode.OK,standardUsersCount); 
    }
   
   
   /* DESCRIPTION: Returns the number of Analysts registered within the application
    * 
    * CALLABLE BY: ADMINISTRATORS
    * 
    * RETURNS:     An StatusObject<Long> object composed of:
    *              1) A StatusCode, representing the result of the operation:
    *                 - OK:                         Number of Analysts registered within the application correctly retrieved
    *                 - ERR_GRAPH_USER_NOTLOGGED:   The user is not logged into the application  
    *                 - ERR_GRAPH_USER_NOTALLOWED:  Insufficient privileges to perform the operation (the user is not an administrator)
    *              2) The number of Analysts registered within the application (or null if the operation failed)
    *              
    * NOTE:        The number of Analysts registered within the application is fetched when an administrator 
    *              logs in, and can be refreshed by calling the loadTotalNodesCount() function            
    */
   public StatusObject<Long> getAnalystsCount()
    { 
     //Check if the user is logged within the application
     if(_userType == UserType.NO_USER)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
     
     //Check if the user has sufficient privileges to perform the operation (i.e. it's an administrator)
     if(_userType != UserType.ADMINISTRATOR)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTALLOWED);
     
     return new StatusObject<Long>(StatusCode.OK,analystsCount); 
    }
     
   
   /* DESCRIPTION: Returns the number of Administrators registered within the application
    * 
    * CALLABLE BY: ADMINISTRATORS
    * 
    * RETURNS:     An StatusObject<Long> object composed of:
    *              1) A StatusCode, representing the result of the operation:
    *                 - OK:                         Number of Administrators registered within the application correctly retrieved
    *                 - ERR_GRAPH_USER_NOTLOGGED:   The user is not logged into the application  
    *                 - ERR_GRAPH_USER_NOTALLOWED:  Insufficient privileges to perform the operation (the user is not an administrator)
    *              2) The number of Administrators registered within the application (or null if the operation failed)
    *              
    * NOTE:        The number of Administrators registered within the application is fetched when an 
    *              administrator logs in, and can be refreshed by calling the loadTotalNodesCount() function              
    */
   public StatusObject<Long> getAdministratorsCount()
    { 
     //Check if the user is logged within the application
     if(_userType == UserType.NO_USER)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
     
     //Check if the user has sufficient privileges to perform the operation (i.e. it's an administrator)
     if(_userType != UserType.ADMINISTRATOR)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTALLOWED);
     
     return new StatusObject<Long>(StatusCode.OK,administratorsCount); 
    }

   
   /* DESCRIPTION: Returns the total number of users registered within the application
    * 
    * CALLABLE BY: ADMINISTRATORS
    * 
    * RETURNS:     An StatusObject<Long> object composed of:
    *              1) A StatusCode, representing the result of the operation:
    *                 - OK:                         Total number of users registered within the application correctly retrieved
    *                 - ERR_GRAPH_USER_NOTLOGGED:   The user is not logged into the application  
    *                 - ERR_GRAPH_USER_NOTALLOWED:  Insufficient privileges to perform the operation (the user is not an administrator)
    *              2) The total number of users registered within the application (or null if the operation failed)
    *              
    * NOTE:        The total number of users registered within the application is fetched when an 
    *              administrator logs in, and can be refreshed by calling the loadTotalNodesCount() function              
    */
   public StatusObject<Long> getTotalUsersCount()
    { 
     //Check if the user is logged within the application
     if(_userType == UserType.NO_USER)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
     
     //Check if the user has sufficient privileges to perform the operation (i.e. it's an administrator)
     if(_userType != UserType.ADMINISTRATOR)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTALLOWED);
     
     return new StatusObject<Long>(StatusCode.OK,totalUsersCount); 
    }

   
   /* DESCRIPTION: Returns the number games stored within the database
    * 
    * CALLABLE BY: ALL connected users logged into the application
    * 
    * RETURNS:     An StatusObject<Long> object composed of:
    *              1) A StatusCode, representing the result of the operation:
    *                 - OK:                         Number of games stored within the database correctly retrieved
    *                 - ERR_GRAPH_USER_NOTLOGGED:   The user is not logged into the application   
    *              2) The number of games stored within the database (or null if the operation failed)
    *              
    * NOTE:        For non-administrators the number of games stored within the database is fetched only at their registration
    *              or login, while administrators can refresh such value by calling the loadTotalNodesCount() function            
    */   
   public StatusObject<Long> getGamesCount()
    { 
     //Check if the user is logged within the application
     if(_userType == UserType.NO_USER)
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
     
     return new StatusObject<Long>(StatusCode.OK,gamesCount); 
    }
  
  
  //---------------------------------------------------------------------------------
  //                            Users Manipulation API
  //--------------------------------------------------------------------------------- 

  /* DESCRIPTION: Saves the current user information from the User object into the database 
   * 
   * CALLABLE BY: ALL connected users logged into the application
   * 
   * RETURNS:     - OK:                        User information successfully saved into the database
   *              - ERR_GRAPH_USER_NOTLOGGED:  The user is not logged into the application
   *              - ERR_GRAPH_USER_NOTEXISTS:  The user doesn't exist within the database (it was removed while it was logged in)
   *              
   * NOTE:        The user final attributes (username, password, joinDate) and the followedCount attribute are not updated into the database
   */
  public StatusCode saveUser()
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
    
    try(Session session = driver.session())
     {
      //Check if the user still exists in the database (it might have been removed while it was logged in)
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadUser(tx,_user.getUsername(),false); }
       } );
      if(record==null)
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS;
      else
       {
        //Otherwise, save the user information into the database
        session.writeTransaction(new TransactionWork<Void>()
         {
          @Override
          public Void execute(Transaction tx)
           {
            tx.run("MATCH (n{username: $username})"+
                   "SET n.firstName = $firstName, n.lastName = $lastName,"   +
                   "    n.age = $age, n.email = $email, n.gender = $gender," +
                   "    n.country = $country, n.favouriteGenre = $favouriteGenre",
                        parameters("username",_user.getUsername(),"firstName",_user.getFirstName(),"lastName",_user.getLastName(),
                                   "age",_user.getAge(),"email",_user.getEmail(),"gender",_user.getGender(),
                                   "country",_user.getCountry(),"favouriteGenre",_user.getFavouriteGenre()));
            return null;
           }
         } );
        return StatusCode.OK;
       }
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in saveUser(): " + e.getMessage());
      System.out.println("[GraphConnector]: " + _user);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   }
  
  
  /* DESCRIPTION: Deletes a user from the database
   * 
   * ARGUMENTS:   - String username:              The username of the user to be removed from the database
   * 
   * CALLABLE BY: ADMINISTRATORS
   * 
   * RETURNS:     - OK:                           User deleted from the database
   *              - ERR_GRAPH_USER_NOTLOGGED:     The user requesting the operation is not logged into the application
   *              - ERR_GRAPH_USER_NOTALLOWED:    Insufficient privileges to perform the operation (the user is not an administrator)
   *              - ERR_GRAPH_MISSINGARGUMENTS:   Not enough arguments to delete the user (missing username)       
   *              - ERR_GRAPH_USER_SELFDELETE:    The administrator is trying to delete himself from the database
   *              - ERR_GRAPH_USER_NOTEXISTS:     The user to delete doesn't exist within the database
   *              - ERR_GRAPH_USER_UNKNOWNTYPE:   An user of unknown type was deleted (removed possible inconsistency within the database)
   */  
  public StatusCode deleteUser(String username)
   {
    //Check if the user requesting the operation is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
    
    //Check if the user has sufficient privileges to perform the operation (i.e. it's an administrator)
    if(_userType != UserType.ADMINISTRATOR)
     return StatusCode.ERR_GRAPH_USER_NOTALLOWED;
        
    //Check whether there are enough arguments to delete the user (its username)
    if(username == null)
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;

    //Check whether the administrator is trying to delete himself
    if(username.equals(_user.getUsername()))
     return StatusCode.ERR_GRAPH_USER_SELFDELETE;
    
    try(Session session = driver.session())
     {   
      //Check if the user to delete exists within the database (where a full load is used to retrieve its UserType in order to correctly update the node counter attribtues)
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadUser(tx,username,true); }
       } );
      if(record==null)
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS;
      else
        {        
         String userType = record.get("labels(n)").asList().get(0).toString();  //Retrieve the UserType of the user
        
         //Delete the user from the database
         session.writeTransaction(new TransactionWork<Void>()
          {
           @Override
           public Void execute(Transaction tx)
            {
             tx.run("MATCH (n{username: $username})" +
                    "DETACH DELETE n",
                    parameters("username",username));
             return null;
            }
          } );
        
        //Decrement the corresponding UserType and the total users counters attributes
        totalUsersCount--;
        if(userType.equalsIgnoreCase("user"))
         standardUsersCount--;
        else
         if(userType.equalsIgnoreCase("analyst"))
          analystsCount--;
         else
          if(userType.equalsIgnoreCase("administrator"))
           administratorsCount--;
          else                      //In case a user with an unknown UserType was deleted, notify to the caller
           return StatusCode.ERR_GRAPH_USER_UNKNOWNTYPE;
        
        return StatusCode.OK;
       }
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in deleteUser(): " + e.getMessage());
      System.out.println("[GraphConnector]: username = " + username);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   }
  
  
  /* DESCRIPTION: Updates a Standard User to an Analyst
   * 
   * CALLABLE BY: STANDARD USERS
   *    
   * RETURNS:     An StatusObject<UserInfo> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                                User successfully updated to Analyst
   *                 - ERR_GRAPH_USER_NOTLOGGED:          The user is not logged into the application
   *                 - ERR_GRAPH_USER_UPGRADEUNNECESSARY: The user doesn't require an upgrade (it's already an analyst or an administrator)
   *                 - ERR_GRAPH_USER_NOTEXISTS:          The user doesn't exist within the database (it was removed while it was logged in)
   *                 - ERR_GRAPH_USER_UNKNOWNTYPE:        The user UserType stored into the database is unknown (possible inconsistency within the database)   
   *              2) The user UserType (UserType.ANALYST on success and the current UserType on failure)
   *              3) The new Analyst object representing the user in the context of the application (or the current object in case of failure)
   *                   
   * NOTE:        On success ALL further modifications and operations involving the user MUST be performed on the Analyst object returned by the function
   */
  public StatusObject<UserInfo> upgradeToAnalyst()
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check whether an update to its UserType is applicable (i.e. the user must be a Standard User)
    if(_userType != UserType.USER)
     return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_UPGRADEUNNECESSARY,new UserInfo(_userType,_user));
    
    try(Session session = driver.session())
     {
      //Check if the user still exists in the database (it might have been removed while it was logged in), and to further check its label
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadUser(tx,_user.getUsername(),true); }
       } );
      if(record==null)
       return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_NOTEXISTS,new UserInfo(_userType,_user));
      else
       {
        //Ensure the consistency of the stored UserType
        String userType = record.get("labels(n)").asList().get(0).toString();
        if(!userType.equalsIgnoreCase("user"))
         return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_USER_UNKNOWNTYPE,new UserInfo(_userType,_user));
        
        //Otherwise, update the user from a Standard User to an Analyst 
        session.writeTransaction(new TransactionWork<Void>()
         {
          @Override
          public Void execute(Transaction tx)
           {
            tx.run("MATCH (n{username: $username})" +
                   "REMOVE n:user "                 +
                   "SET n:analyst", 
                   parameters("username",_user.getUsername()));
            return null;
           }
         } );
          
        //Update the local _userType
        _userType = UserType.ANALYST;         
       
        //Update the local _user object to the Analyst class
        Analyst analyst = new Analyst(_user.getUsername(),_user.getPassword(),_user.getJoinDate());
        analyst.setFirstName(_user.getFirstName());
        analyst.setLastName(_user.getLastName());
        analyst.setAge(_user.getAge());
        analyst.setEmail(_user.getEmail());
        analyst.setGender(_user.getGender());
        analyst.setCountry(_user.getCountry());
        analyst.setFavouriteGenre(_user.getFavouriteGenre());
        analyst.setFollowedCount(record.get("n.followedCount").asLong());  //Free update since it was fetched before
        _user = analyst;
        
        //Increment the analysts and decrement the standard users node counters
        standardUsersCount--;
        analystsCount++;
          
        return new StatusObject<UserInfo>(StatusCode.OK,new UserInfo(_userType,_user));
       }
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in upgradeToAnalyst(): " + e.getMessage());
      return new StatusObject<UserInfo>(StatusCode.ERR_GRAPH_UNKNOWN,new UserInfo(_userType,_user));
     }
   }  
  
  
  //---------------------------------------------------------------------------------
  //                           Users Relationships API
  //--------------------------------------------------------------------------------- 
  
  /* DESCRIPTION: Adds the target user to the current user followed users list
   * 
   * ARGUMENTS:   - String username:                The username of the user to add to the current user followed users list
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   * 
   * RETURNS:     - OK:                             Target user successfully added to the current user followed users list
   *              - ERR_GRAPH_USER_NOTLOGGED:       The user is not logged into the application
   *              - ERR_GRAPH_MISSINGARGUMENTS:     Not enough arguments to create the relationship (missing target username)
   *              - ERR_GRAPH_USER_SELFRELATION:    The user is attempting to follow himself
   *              - ERR_GRAPH_USER_ALREADYFOLLOWS:  The current user already follows the target user
   *              - ERR_GRAPH_USER_NOTEXISTS:       Either the current or the target user don't exist within the database
   *              - ERR_GRAPH_USER_INCONSISTENCY:   An inconsistency was detected between the local followed users list and the database,
   *                                                where the former has been aligned to the latter (the current user follows the target user)
   *
   * NOTES:       1) In case of success the target user UserType, password and e-mail are hidden from its User object in the followed users list
   *              2) In case of success if the target user is included in the current user following suggestions, it is automatically removed from such list  
   */  
  public StatusCode followUser(String username)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
    
    //Check whether there are enough arguments to retrieve information on the target user and the possible existing relationship (the target username)
    if(username== null)
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
    
    //Check whether the current user is attempting follow himself
    if(_user.getUsername().equals(username))
     return StatusCode.ERR_GRAPH_USER_SELFRELATION;
    
    //Check if the local followed users list contains the target user
    User followed = searchUsersList(username,_followedUsersList);
    if(followed!=null)
     return StatusCode.ERR_GRAPH_USER_ALREADYFOLLOWS;
    
    try(Session session = driver.session())
     {
      //Double check in the database if both user and their possible :FOLLOW relationship exist, retrieving information on the target user
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { 
          Result result;
          result = tx.run("OPTIONAL MATCH (n {username: $username1})"    +
                          "OPTIONAL MATCH (p {username: $username2})"    +
                          "OPTIONAL MATCH (n)-[r:FOLLOWS]->(p)"          +
                          "RETURN n,p,r,"                                +
                          "       p.joinDate, p.firstName, p.lastName, " +
                          "       p.age, p.email, p.gender, p.country, " +
                          "       p.favouriteGenre, p.followedCount",
                          parameters("username1",_user.getUsername(),"username2",username));  
          return result.next(); //Note that the result.hasNext() check is not necessary due to the OPTIONAL MATCH keywords being used in the query
         }
       } ); 
     
      //If the current or the target user don't exist within the database, return an error
      if((record.get("n").asObject()==null)||(record.get("p").asObject() == null))
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS;
      
      //Build the User object relative to the target user (where his UserType as well as his password are hidden)
      followed = new User(username,"x",record.get("p.joinDate").asLocalDate());

      followed.setFirstName(record.get("p.firstName").asString());
      followed.setLastName(record.get("p.lastName").asString());
      if((record.get("p.age").asObject() != null))
       followed.setAge(new Long(record.get("p.age").asLong()));
      followed.setEmail("x");                                      //The e-mail is hidden from the results
      if((record.get("p.gender").asObject() != null))
       followed.setGender(new Character(record.get("p.gender").asString().charAt(0)));
      followed.setCountry(record.get("p.country").asString());
      followed.setFavouriteGenre(record.get("p.favouriteGenre").asString());
      followed.setFollowedCount(record.get("p.followedCount").asLong()+1);
      
      //Insert the target user in front of the followed user list
      _followedUsersList.add(0,followed);
      
      //If the target user is in the user following suggestions list, remove it
      User suggested = searchUsersList(username,_suggestedUsersList);
      if(suggested!=null)
       _suggestedUsersList.remove(suggested);
      
      //Double-check that in the database the current user wasn't already following the target user, notifying that the local inconsistency has been fixed in the case
      if(record.get("r").asObject() != null)
        return StatusCode.ERR_GRAPH_USER_INCONSISTENCY;
      else
       {
        //Otherwise create the :FOLLOW relationship from the current to the target user, incrementing the latter followedCount
        session.writeTransaction(new TransactionWork<Void>()
         {
          @Override
          public Void execute(Transaction tx)
           {
            tx.run("MATCH (n {username: $username1}),(p {username: $username2})" +
                   "CREATE (n)-[:FOLLOWS]->(p)"                                  +
                   "SET p.followedCount = p.followedCount + 1",
                   parameters("username1",_user.getUsername(),"username2",username));
            return null;
           }
         } );
        return StatusCode.OK;
       }
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in followUser(): " + e.getMessage());
      System.out.println("[GraphConnector]: username = " + username);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   }
      
  
  /* DESCRIPTION: Removes an user from the current user followed users list
   * 
   * ARGUMENTS:   - String username:                The username of the user to remove from the current user followed users list
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   *
   * RETURNS:     - OK:                             Target user successfully removed from the current user followed users list
   *              - ERR_GRAPH_USER_NOTLOGGED:       The user is not logged into the application
   *              - ERR_GRAPH_MISSINGARGUMENTS:     Not enough arguments to delete the relationship (missing target username)
   *              - ERR_GRAPH_USER_SELFRELATION:    The user is attempting to delete a relationship with himself (which shoudln't exist in any case)
   *              - ERR_GRAPH_USER_NOTEXISTS:       Either the current or the target user don't exist within the database
   *              - ERR_GRAPH_USER_NOTFOLLOWS:      The current user doesn't follow the target user
   *              - ERR_GRAPH_USER_INCONSISTENCY:   An inconsistency was detected between the local followed users list and the database,
   *                                                where the former has been aligned to the latter (the current user doesn't follow the target user)
   */  
  public StatusCode unFollowUser(String username)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
    
    //Check whether there are enough arguments to retrieve information on the target user and the possible existing relationship (the target username)
    if(username== null)
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
    
    //Check whether the current user is attempting to delete a relationship with himself (which should not exist in any case)
    if(_user.getUsername().equals(username))
     return StatusCode.ERR_GRAPH_USER_SELFRELATION;
    
    //Check if the local followed users list contains the target user
    User followed = searchUsersList(username,_followedUsersList);
    if(followed==null)
     return StatusCode.ERR_GRAPH_USER_NOTFOLLOWS;
    
    //Remove the target user from the followed users list
    _followedUsersList.remove(followed);      
    
    try(Session session = driver.session())
     {
      //Double check in the database if both user and their possible :FOLLOW relationship exist
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { 
          Result result;
          result = tx.run("OPTIONAL MATCH (n {username: $username1})" +
                          "OPTIONAL MATCH (p {username: $username2})" +
                          "OPTIONAL MATCH (n)-[r:FOLLOWS]->(p)"       +
                          "RETURN n,p,r",
                          parameters("username1",_user.getUsername(),"username2",username));  
          return result.next(); //Note that the result.hasNext() check is not necessary due to the OPTIONAL MATCH keywords being used in the query
         }
       } ); 
     
      //If the current or the target user don't exist within the database return an error
      if((record.get("n").asObject()==null)||(record.get("p").asObject() == null))
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS;
       
      //Double-check the current user to follow the target user, notifying the inconsistency between the local followed users list and the database in the case
      if(record.get("r").asObject() == null)
       return StatusCode.ERR_GRAPH_USER_INCONSISTENCY;
       
      //Delete the :FOLLOW relationship from the current to the target user, decrementing the latter followedCount
      session.writeTransaction(new TransactionWork<Void>()
       {
        @Override
        public Void execute(Transaction tx)
         {
          tx.run("MATCH (n {username: $username1})-[r:FOLLOWS]->(p {username: $username2}) " +
                 "DELETE r "                                                                 +
                 "SET p.followedCount = p.followedCount - 1", 
                 parameters("username1",_user.getUsername(),"username2",username));
          return null;
         }
       } );
      
      return StatusCode.OK;
     }   
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in unFollowUser(): " + e.getMessage());
      System.out.println("[GraphConnector]: username = " + username);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   } 
  
  
  /* DESCRIPTION: Returns a list of up to "max" Standard Users having their username matching the given mask in decreasing followedCounter order, excluding the current user
   * 
   * ARGUMENTS:   - String mask:                     The mask used to match the usernames of the users into the database
   *              - int max:                         The maximum number of search results to be returned (default = 25)
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   * 
   * RETURNS:     A StatusObject<List<User>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           Search results correctly retrieved (possibly empty)
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application
   *                 - ERR_GRAPH_MISSINGARGUMENTS:   Not enough arguments to perform the search (missing mask)
   *                 - ERR_GRAPH_NOTPOSITIVELIMIT:   The passed "max" parameter of Users to return is non-positive 
   *              2) The list of Standard Users having their username matching the given mask in decreasing followedCounter order, excluding the current user
   *              
   * NOTE:        In case of success the UserTypes, passwords and e-mails are hidden from the search results
   */
  public StatusObject<List<User>> searchUsers(String mask, int max)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check whether enough arguments to perform the search were provided (the mask)
    if(mask == null)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_MISSINGARGUMENTS);
    
    //Check the passed "max" parameter to be positive
    if(max<=0)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_NOTPOSITIVELIMIT);  

    try(Session session = driver.session())
     {    
      //Retrieve up to "max" users having their username matching the mask, ordered by followedCount, and excluding the current user
      List<User> searchResults = session.readTransaction(new TransactionWork<List<User>>()
       {
        @Override
        public List<User> execute(Transaction tx)
         {
          List<User> results = new ArrayList<>();
          Result result = tx.run("MATCH (n)"                        +
                                 "WHERE n.username CONTAINS $mask AND NOT n.username = $currentUser "      +
                                 "RETURN n.username, n.joinDate, n.firstName, n.lastName, n.age,"          +
                                 "       n.email, n.gender, n.country, n.favouriteGenre, n.followedCount " +
                                 "ORDER BY n.followedCount DESC LIMIT $max",
                                 parameters("mask",mask,"currentUser",_user.getUsername(),"max",max));
          while (result.hasNext())
           { 
            //Populate the User object relative to each result and add it to the overall search results
            Record record = result.next();
            User temp = new User(record.get("n.username").asString(),"x",record.get("n.joinDate").asLocalDate());

            temp.setFirstName(record.get("n.firstName").asString());
            temp.setLastName(record.get("n.lastName").asString());
            if((record.get("n.age").asObject() != null))
             temp.setAge(new Long(record.get("n.age").asLong()));
            temp.setEmail("x");                                      //The e-mail is hidden from the results
            if((record.get("n.gender").asObject() != null))
             temp.setGender(new Character(record.get("n.gender").asString().charAt(0)));
            temp.setCountry(record.get("n.country").asString());
            temp.setFavouriteGenre(record.get("n.favouriteGenre").asString());
            temp.setFollowedCount(record.get("n.followedCount").asLong());
            
            results.add(temp);
           }
          return results;
         }
       } );
      
      return new StatusObject<List<User>>(StatusCode.OK,searchResults);
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in searchUsers(): " + e.getMessage());
      System.out.println("[GraphConnector]: mask = " + mask + ", max = " + max);
      return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   }  
 
  
  /* Overloaded searchUsers() function with default parameter max = 25 */
  public StatusObject<List<User>> searchUsers(String mask)
   { return searchUsers(mask,25); } 
  
  
  /* DESCRIPTION: Returns a list of up to "max" Standard Users representing following suggestion for the current user, which are selected by 
   *              choosing the first "max" Users in the application in descending followedCount order that are not already followed by the user
   * 
   * ARGUMENTS:   - int max:                         The maximum number of following suggestions to be returned (default = 25)
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   * 
   * RETURNS:     A StatusObject<List<User>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           Following suggestions correctly retrieved (possibly empty)
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application
   *                 - ERR_GRAPH_NOTPOSITIVELIMIT:   The passed "max" parameter of following suggestions to return is non-positive
   *                 - ERR_GRAPH_USER_NOTEXISTS:     The user doesn't exist within the database (it was removed while it was logged in) 
   *              2) The list of Standard Users representing following suggestions for the user, as described above
   *              
   * NOTE:        In case of success the UserTypes, passwords and e-mails are hidden from the suggestion results
   */
  public StatusObject<List<User>> getSuggestedUsersList(int max)
   { 
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check the passed "max" parameter to be positive
    if(max<=0)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_NOTPOSITIVELIMIT);  
    
    //If the local suggested users list has at least "max" elements, return it directly
    if((_suggestedUsersList!=null)&&(_suggestedUsersList.size()>=max))
     {
      _suggestedUsersList = _suggestedUsersList.subList(0,max);
      return new StatusObject<List<User>>(StatusCode.OK,_suggestedUsersList);
     }
    
    //Otherwise, fetch the users following suggestions from the database
    try(Session session = driver.session())
     {
      //Check if the user still exists in the database (it might have been removed while it was logged in)
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadUser(tx,_user.getUsername(),false); }
       } );
      if(record==null)
       return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_USER_NOTEXISTS);
      else
       {
        _suggestedUsersList = session.readTransaction(new TransactionWork<List<User>>()
         {
          @Override
          public List<User> execute(Transaction tx)
           {
            List<User> results = new ArrayList<>();
            Result result = tx.run("MATCH (p{username: $username}),(n:user) "                                +
                                   "WHERE NOT (p)-->(n) AND NOT p.username = n.username "                    +
                                   "RETURN n.username, n.joinDate, n.firstName, n.lastName, n.age, "         +
                                   "       n.email, n.gender, n.country, n.favouriteGenre, n.followedCount " +
                                   "ORDER BY n.followedCount DESC LIMIT $max",
                                   parameters("username",_user.getUsername(),"max",max));
            while (result.hasNext())
             { 
              //Populate the User object relative to each result and add it to the overall suggestion results
              Record record = result.next();
              User temp = new User(record.get("n.username").asString(),"x",record.get("n.joinDate").asLocalDate());

              temp.setFirstName(record.get("n.firstName").asString());
              temp.setLastName(record.get("n.lastName").asString());
              if((record.get("n.age").asObject() != null))
               temp.setAge(new Long(record.get("n.age").asLong()));
              if((record.get("n.gender").asObject() != null))
               temp.setGender(new Character(record.get("n.gender").asString().charAt(0)));
              temp.setCountry(record.get("n.country").asString());
              temp.setFavouriteGenre(record.get("n.favouriteGenre").asString());
              temp.setFollowedCount(record.get("n.followedCount").asLong());
              
              results.add(temp);
             }
            return results;
           }
         } );
        
        return new StatusObject<List<User>>(StatusCode.OK,_suggestedUsersList);
       }
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in getSuggestedUsersList(): " + e.getMessage());
      System.out.println("[GraphConnector]: max = " + max);
      _suggestedUsersList = null;
      return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   }  
  
  
  /* Overloaded getSuggestedUsersList() method with default parameter max = 25 */
  public StatusObject<List<User>> getSuggestedUsersList()
   { return getSuggestedUsersList(25); }
  
  
  //---------------------Users Relationships Utility Functions-----------------------
  
  /* DESCRIPTION: Returns a Boolean representing whether the current user is following the target user (true) or not (false)
   * 
   * ARGUMENTS:   - String username:                The username of the target user whose belongingness to the current user followed users list is to be determined
   *
   * CALLABLE BY: ALL connected users logged into the application 
   *       
   * RETURNS:     A StatusObject<Boolean> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                          Correctly determined whether the current user follows the target user or not (the Boolean value is valid)
   *                 - ERR_GRAPH_USER_NOTLOGGED:    The user is not logged into the application 
   *                 - ERR_GRAPH_MISSINGARGUMENTS:  Not enough arguments to determine whether the current user follows the target user or not (missing username)
   *              2) A Boolean representing whether the current user is following the target user (true) or not (false)
   */
  public StatusObject<Boolean> doIFollow(String username)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<Boolean>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check whether enough arguments were provided (the target username)
    if(username == null)
     return new StatusObject<Boolean>(StatusCode.ERR_GRAPH_MISSINGARGUMENTS);
    
    //Return "true" or "false" depending whether the user follows the target user
    return new StatusObject<Boolean>(StatusCode.OK, new Boolean(searchUsersList(username,_followedUsersList)!=null));
   }
  
  
  /* DESCRIPTION: Returns the current user followed users list
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   *       
   * RETURNS:     A StatusObject<List<User>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                          User followed users list returned successfully
   *                 - ERR_GRAPH_USER_NOTLOGGED:   The user is not logged into the application
   *              2) The List<User> of users followed by the current user
   *              
   * NOTE:        The followed users UserTypes and passwords are hidden from the followed users list             
   */
  public StatusObject<List<User>> getFollowedUsersList()           
   { 
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    return new StatusObject<List<User>>(StatusCode.OK,_followedUsersList);
  } 
  
  
  //---------------------------------------------------------------------------------
  //                            Games Manipulation API
  //---------------------------------------------------------------------------------
  
  /* DESCRIPTION: Adds a game to the database
   *
   * ARGUMENTS:   - GraphGame game:                The game object to be added to the database
   *
   * CALLABLE BY: ADMINISTRATORS
   *
   * RETURNS:     - OK:                            Game successfully added to the database
   *              - ERR_GRAPH_USER_NOTLOGGED:      The user is not logged into the application
   *              - ERR_GRAPH_USER_NOTALLOWED:     Insufficient privileges to perform the operation (the user is not an administrator) 
   *              - ERR_GRAPH_MISSINGARGUMENTS:    Not enough arguments to add the game into the database (missing _id or title)
   *              - ERR_GRAPH_USER_NOTEXISTS:      The administrator user doesn't exist within the database (it was removed while it was logged in)
   *              - ERR_GRAPH_GAME_ALREADYEXISTS:  A game with the same "_id" already exists within the database
   *
   * NOTE: The "favouriteCount" and "vote" attribute of the passed "game" object are ignored when adding the game to the database
   */  
  public StatusCode addGame(GraphGame game)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
    
    //Check if the user has sufficient privileges to perform the operation (i.e. it's an administrator)
    if(_userType != UserType.ADMINISTRATOR)
     return StatusCode.ERR_GRAPH_USER_NOTALLOWED;
    
    //Check whether enough arguments to add the game were provided (its _id and title)
    if((game==null)||(game._id==null)||(game.title==null))
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
    
    try(Session session = driver.session())
     {
      //Retrieve information on the current user and the game from the database
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadGame(tx,_user.getUsername(),game._id,false); }
       } );

      //Double-check that the administrator still exists within the database
      if(record.get("n").asObject()==null)
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS; 
      
      //If a game with such _id already exists, return an error
      if(record.get("g").asObject()!=null)
       return StatusCode.ERR_GRAPH_GAME_ALREADYEXISTS;
    
      //Otherwise, add the new game into the database
      session.writeTransaction(new TransactionWork<Void>()
       {
        @Override
        public Void execute(Transaction tx)
         {
          tx.run("CREATE (g:game {_id: $_id, title: $title, favouriteCount: 0})",
                 parameters("_id",game._id,"title",game.title));
          return null;
         }
       } );
      
      //Increment the local games counter
      gamesCount++;
      
      return StatusCode.OK;
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in addGame(): " + e.getMessage());
      System.out.println("[GraphConnector]: game = " + game);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   }
  
  
 /* DESCRIPTION: Removes a game from the database
  *
  * ARGUMENTS:   - String _id:                  The "_id" of the game to be removed from the database 
  *
  * CALLABLE BY: ADMINISTRATORS 
  * 
  * RETURNS:     - OK:                          Game successfully removed from the database
  *              - ERR_GRAPH_USER_NOTLOGGED:    The user is not logged into the application
  *              - ERR_GRAPH_USER_NOTALLOWED:   Insufficient privileges to perform the operation (the user is not an administrator) 
  *              - ERR_GRAPH_MISSINGARGUMENTS:  Not enough arguments to delete the game from the database (missing _id)
  *              - ERR_GRAPH_USER_NOTEXISTS:    The administrator user doesn't exist within the database (it was removed while it was logged in)
  *              - ERR_GRAPH_GAME_NOTEXISTS:    The game doesn't exist within the database
  */  
 public StatusCode deleteGame(String _id)
  {
   //Check if the user is logged within the application
   if(_userType == UserType.NO_USER)
    return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
   
   //Check if the user has sufficient privileges to perform the operation (i.e. it's an administrator)
   if(_userType != UserType.ADMINISTRATOR)
    return StatusCode.ERR_GRAPH_USER_NOTALLOWED;
   
   //Check whether enough arguments to delete the game were provided (its _id)
   if(_id==null)
    return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
   
   try(Session session = driver.session())
    {
     //Retrieve information on the current user and the game from the database
     Record record = session.readTransaction(new TransactionWork<Record>()
      {
       @Override
       public Record execute(Transaction tx)
        { return checkLoadGame(tx,_user.getUsername(),_id,false); }
      } );

     //Double-check that the administrator still exists within the database
     if(record.get("n").asObject()==null)
      return StatusCode.ERR_GRAPH_USER_NOTEXISTS; 
     
     //If a game with such _id doesn't exist, return an error
     if(record.get("g").asObject()==null)
      return StatusCode.ERR_GRAPH_GAME_NOTEXISTS;
   
     //Otherwise, delete the game from the database
     session.writeTransaction(new TransactionWork<Void>()
      {
       @Override
       public Void execute(Transaction tx)
        {
         tx.run("MATCH (g:game{_id: $_id})" +
                "DETACH DELETE g",
                parameters("_id",_id));
         return null;
        }
      } );
     
     //If the game is in the user favourite games list, remove it
     GraphGame favGame = searchGamesList(_id,_favouritesGamesList);
     if(favGame!=null)
      _favouritesGamesList.remove(favGame);
    
     //Decrement the local games counter
     gamesCount--;
     
     return StatusCode.OK;
    }
   catch(Exception e)
    {
     System.out.println("[GraphConnector]: Error in deleteGame(): " + e.getMessage());
     System.out.println("[GraphConnector]: _id = " + _id);
     return StatusCode.ERR_GRAPH_UNKNOWN;
    }
  }  
  
 
  //---------------------------------------------------------------------------------
  //                           Games Relationships API
  //---------------------------------------------------------------------------------
 
  /* DESCRIPTION: Adds a game to the user's favourite games list
   *
   * ARGUMENTS:   - String _id:                    The "_id" of the game to be added to the current user favourite games list 
   *
   * CALLABLE BY: ALL connected users logged into the application 
   * 
   * RETURNS:     - OK:                            Game successfully added to the user's favourite games list
   *              - ERR_GRAPH_USER_NOTLOGGED:      The user is not logged into the application
   *              - ERR_GRAPH_MISSINGARGUMENTS:    Not enough arguments to add the game to the favourite games list (missing game _id)
   *              - ERR_GRAPH_GAME_ALREADYADDED:   The game is already in the user's favourite games list
   *              - ERR_GRAPH_USER_NOTEXISTS:      The user doesn't exist within the database (it was removed while it was logged in)
   *              - ERR_GRAPH_GAME_NOTEXISTS:      The game doesn't exist within the database
   *              - ERR_GRAPH_GAME_INCONSISTENCY:  An inconsistency was detected between the local favourite games list and the database,
   *                                               where the former has been aligned to the latter (the game is in the user's favourite games list)
   *
   * NOTE:        In case of success if the game is included in the user featured games list, it is automatically removed from such list
   */   
  public StatusCode addToFavourites(String _id)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
    
    //Check whether there are enough arguments to add the game to the user's favourite list (the game _id)
    if(_id==null)
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
    
    //Check if the game is in the local user favourite games list
    GraphGame favGame = searchGamesList(_id,_favouritesGamesList);
    if(favGame!=null)
     return StatusCode.ERR_GRAPH_GAME_ALREADYADDED;
    
    try(Session session = driver.session())
     {
      //Retrieve information on the current user, the game and their possible relationships from the database
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadGame(tx,_user.getUsername(),_id,true); }
       } );
      
      //Double-check that the user still exists within the database
      if(record.get("n").asObject()==null)
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS; 
      
      //Check that the game exists within the database
      if(record.get("g").asObject()==null)
       return StatusCode.ERR_GRAPH_GAME_NOTEXISTS;    
      
      //Insert the game in front of the local favourite games list
      favGame = new GraphGame(_id,record.get("g.title").asString(),record.get("g.favouriteCount").asLong()+1);
      _favouritesGamesList.add(0,favGame);
      
      //If the game is in the user featured games list, remove it
      favGame = searchGamesList(_id,_featuredGamesList);
      if(favGame!=null)
       _featuredGamesList.remove(favGame);
      
      //Double-check that in the database the current user hadn't already added the game to his favourites, notifying that the local inconsistency has been fixed in the case
      if(record.get("a").asObject() != null)
        return StatusCode.ERR_GRAPH_GAME_INCONSISTENCY;
      else
       {
        //Otherwise create the :ADD relationship from the user to the game
        session.writeTransaction(new TransactionWork<Void>()
         {
          @Override
          public Void execute(Transaction tx)
           {
            tx.run("MATCH (n {username: $username}),(p:game {_id: $_id})" +
                   "CREATE (n)-[:ADD]->(p)"                               +
                   "SET p.favouriteCount = p.favouriteCount + 1",
                   parameters("username",_user.getUsername(),"_id",_id));
            return null;
           }
         } );
      
       return StatusCode.OK;
      }
     } 
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in addToFavourites(): " + e.getMessage());
      System.out.println("[GraphConnector]: _id = " + _id);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   }      
      
      
  /* DESCRIPTION: Removes a game from the user's favourite games list
   * 
   * ARGUMENTS:   - String _id:                    The "_id" of the game to be removed from the current user favourite games list 
   *
   * CALLABLE BY: ALL connected users logged into the application 
   *     
   * RETURNS:     - OK:                            Game successfully removed from the user's favourite games list
   *              - ERR_GRAPH_USER_NOTLOGGED:      The user is not logged into the application
   *              - ERR_GRAPH_MISSINGARGUMENTS:    Not enough arguments to remove the game from the favourite games list (missing game _id)
   *              - ERR_GRAPH_GAME_NOTADDED:       The game is not in the user's favourite games list
   *              - ERR_GRAPH_USER_NOTEXISTS:      The user doesn't exist within the database (it was removed while it was logged in)
   *              - ERR_GRAPH_GAME_NOTEXISTS:      The game doesn't exist within the database
   *              - ERR_GRAPH_GAME_INCONSISTENCY:  An inconsistency was detected between the local favourite games list and the database,
   *                                               where the former has been aligned to the latter (the game was removed from the user's favourite games list)  
   */ 
  public StatusCode removeFromFavourites(String _id)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;
    
    //Check whether there are enough arguments to remove the game from the user's favourite list (the game _id)
    if(_id==null)
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
    
    //Check if the game is in the local user favourite games list
    GraphGame favGame = searchGamesList(_id,_favouritesGamesList);
    if(favGame==null)
     return StatusCode.ERR_GRAPH_GAME_NOTADDED;
    
    //Remove the game from the user's favourite games list
    _favouritesGamesList.remove(favGame);
    
    try(Session session = driver.session())
     {
      //Retrieve information on the current user, the game and their possible relationships from the database
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadGame(tx,_user.getUsername(),_id,true); }
       } );
    
      //Double-check that the user still exists within the database
      if(record.get("n").asObject()==null)
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS; 
      
      //Check that the game exists within the database
      if(record.get("g").asObject()==null)
       return StatusCode.ERR_GRAPH_GAME_NOTEXISTS;
      
      //Double-check that the game is in the user's favourites within the database
      if(record.get("a").asObject()==null)
       return StatusCode.ERR_GRAPH_GAME_INCONSISTENCY; 
      
      //Delete the :ADD relationship between the user and the game from the database
      session.writeTransaction(new TransactionWork<Void>()
       {
        @Override
        public Void execute(Transaction tx)
         {
          tx.run("MATCH (n {username: $username})-[a:ADD]->(g:game {_id: $_id})" +
                 "DELETE a "                                                     +
                 "SET g.favouriteCount = g.favouriteCount-1", 
                 parameters("username",_user.getUsername(),"_id",_id));
          return null;
         }
       } );
      
      return StatusCode.OK;
     }      
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in removeFromFavourites(): " + e.getMessage());
      System.out.println("[GraphConnector]: _id = " + _id);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   }          
  
  
  /* DESCRIPTION: Returns the favourite games list of a user, where non-administrators may retrieve only their own and their followed users favourite games lists
   * 
   * ARGUMENTS:   - String username:                The username of the target user whose favourite games list is to be retrieved
   * 
   * CALLABLE BY: ALL connected users logged into the application (even if non-administrators may retrieve only their own and their followed users favourite games lists
   *
   * RETURNS:     A StatusObject<List<GraphGame>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                          Favourite games list of the user successfully retrieved
   *                 - ERR_GRAPH_USER_NOTLOGGED:    The user is not logged into the application
   *                 - ERR_GRAPH_MISSINGARGUMENTS:  Not enough arguments to retrieve the user favourite games list (missing username)
   *                 - ERR_GRAPH_GAME_NOTFOLLOWER:  The current non-administrator user is attempting to retrieve the favourite games list of a user which does not follow
   *                                                (note that this is also returned in case the target user the current user follows has been deleted without the latter's acknowledgment)
   *                 - ERR_GRAPH_USER_NOTEXISTS:    The target user doesn't exist within the database
   *              2) The favourite games list of the target user
   *              
   * NOTE:        In the GraphGame objects returned by the function the "value" attribute represents the target user game rating [1,5]
   */ 
  public StatusObject<List<GraphGame>> getFavouritesGamesList(String username)
   { 
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check whether there are enough arguments to retrieve the user's favourite games list (the game username)
    if(username==null)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_MISSINGARGUMENTS);
    
    //Check whether the current user is attempting to retrieve his own favourite games list (which should do by calling the overloaded getFavouriteGames() function with no arguments)
    if(_user.getUsername().equals(username))
     return new StatusObject<List<GraphGame>>(StatusCode.OK,_favouritesGamesList);
     
    //Check if a non-administrator user is attempting to retrieve the favourite games list of a user he doesn't follow
    //(which may also happen in case the target user the current user follows has been deleted without the latter's acknowledgment)
    User followed = searchUsersList(username,_followedUsersList);
    if((followed==null)&&(_userType != UserType.ADMINISTRATOR))
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_GAME_NOTFOLLOWER);
    
    try(Session session = driver.session())
     {
      //Double check the target user to exist within the database
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadUser(tx,username,false); }
       } );
      
      //If the target user doesn't exist (it was removed while the current user was logged), remove him from his local followed list and return an error
      if(record==null)
       {
        _followedUsersList.remove(followed); 
        return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_USER_NOTEXISTS);
       }
      
      //Otherwise, load the target user favourite games list and return it
      List<GraphGame> friendFavouritesList = session.readTransaction(new TransactionWork<List<GraphGame>>()
       {
        @Override
        public List<GraphGame> execute(Transaction tx)
         {
          List<GraphGame> favourites = new ArrayList<>();
          
          Result result = tx.run("MATCH (n {username: $username})-[a:ADD]->(p) "   +
                                 "OPTIONAL MATCH (n)-[r:RATE]->(p) "               +
                                 "RETURN p._id, p.title, p.favouriteCount, r.vote ORDER BY p.favouriteCount",
                                 parameters("username",username));  
          while (result.hasNext())
           { 
            Record record = result.next();
            GraphGame temp = new GraphGame(record.get("p._id").asString(),record.get("p.title").asString(),record.get("p.favouriteCount").asLong());
            if((record.get("r.vote").asObject() != null))
             temp.setVote(new Long(record.get("r.vote").asLong()));
            favourites.add(temp);
           }
          return favourites;
         }
       } );
      
      return new StatusObject<List<GraphGame>>(StatusCode.OK,friendFavouritesList);
     }      
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in getFavouritesGamesList(): " + e.getMessage());
      System.out.println("[GraphConnector]: username = " + username);
      return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   }    
  
  
  /* DESCRIPTION: Returns a game's favouriteCount
   * 
   * ARGUMENTS:   - String _id:                  The "_id" of the game whose favouriteCount is to be returned
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   *    
   *    
   * RETURNS:     A StatusObject<Long> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                          Game favouriteCount correctly retrieved
   *                 - ERR_GRAPH_USER_NOTLOGGED:    The user is not logged into the application
   *                 - ERR_GRAPH_MISSINGARGUMENTS:  Not enough arguments to retrieve the game favouriteCount (missing game _id)  
   *                 - ERR_GRAPH_USER_NOTEXISTS:    The user doesn't exist within the database (it was removed while it was logged in)
   *                 - ERR_GRAPH_GAME_NOTEXISTS:    The game doesn't exist within the database
   *              2) A Long object representing the game's favouriteCount (or null if the operation failed)    
   */   
  public StatusObject<Long> getGameFavouriteCount(String _id)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);    
    
    //Check whether we have enough arguments to retrieve the game's favouriteCount (the game _id)
    if(_id==null)
     return new StatusObject<Long>(StatusCode.ERR_GRAPH_MISSINGARGUMENTS);
    
    try(Session session = driver.session())
     {
      //Retrieve information on the current user and the game from the database
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadGame(tx,_user.getUsername(),_id,true); }
       } );
   
      //Double-check that the user still exists within the database
      if(record.get("n").asObject()==null)
       return new StatusObject<Long>(StatusCode.ERR_GRAPH_USER_NOTEXISTS); 
     
      //Check that the game exists within the database
      if(record.get("g").asObject()==null)
       return new StatusObject<Long>(StatusCode.ERR_GRAPH_GAME_NOTEXISTS);
      
      //Otherwise, return the game's favourite count
      return new StatusObject<Long>(StatusCode.OK,record.get("g.favouriteCount").asLong());
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in getGameFavouriteCount(): " + e.getMessage());
      System.out.println("[GraphConnector]: _id = " + _id);
      return new StatusObject<Long>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   }    

  
  /* DESCRIPTION: Sets or updates a game user rating
   * 
   * ARGUMENTS:   - String _id:                  The "_id" of the game to be rated by the user
   *              - int vote:                    The user game rating [1,5]
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   *    
   * RETURNS:     - OK:                          Game user rating correctly set or updated
   *              - ERR_GRAPH_USER_NOTLOGGED:    The user is not logged into the application
   *              - ERR_GRAPH_MISSINGARGUMENTS:  Not enough arguments to set/update the game user rating (missing game _id)
   *              - ERR_GRAPH_GAME_VOTERANGE:    The game user rating is out of bounds [1,5]  
   *              - ERR_GRAPH_USER_NOTEXISTS:    The user doesn't exist within the database (it was removed while it was logged in)
   *              - ERR_GRAPH_GAME_NOTEXISTS:    The game doesn't exist within the database
   */   
  public StatusCode rateGame(String _id, int vote)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return StatusCode.ERR_GRAPH_USER_NOTLOGGED;    
    
    //Check whether we have enough arguments to set/update the game user rating (the game _id)
    if(_id==null)
     return StatusCode.ERR_GRAPH_MISSINGARGUMENTS;
    
    //Check the game user rating to be inbounds [1,5]
    if((vote<1)||(vote>5))
     return StatusCode.ERR_GRAPH_GAME_VOTERANGE;
    
    //Check if the game is in the local user favourite games list, and set/update its vote if so
    GraphGame favGame = searchGamesList(_id,_favouritesGamesList);
    if(favGame!=null)
     favGame.setVote((long)vote);
      
    try(Session session = driver.session())
     {
      //Retrieve information on the current user, the game and their possible relationships from the database
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadGame(tx,_user.getUsername(),_id,true); }
       } );
    
      //Double-check that the user still exists within the database
      if(record.get("n").asObject()==null)
       return StatusCode.ERR_GRAPH_USER_NOTEXISTS; 
      
      //Check that the game exists within the database
      if(record.get("g").asObject()==null)
       {
        //If the game don't exist any longer but is in the user favourite games list, remove it
        if(favGame!=null)
         _favouritesGamesList.remove(favGame);
        return StatusCode.ERR_GRAPH_GAME_NOTEXISTS;
       }
      
      //If the user never rated such game, create the :RATE relationship with the appropriate vote attribute
      if(record.get("r").asObject()==null)
       session.writeTransaction(new TransactionWork<Void>()
        {
         @Override
         public Void execute(Transaction tx)
          {
           tx.run("MATCH (n {username: $username}),(g:game {_id: $_id})"+
                  "CREATE (n)-[r:RATE]->(g)"                      +
                  "SET r.vote = $vote", 
                  parameters("username",_user.getUsername(),"_id",_id,"vote",vote));
           return null;
          }
        } );
      else   //Otherwise just update the "vote" attribute of the already existing relationship
       session.writeTransaction(new TransactionWork<Void>()
        {
         @Override
         public Void execute(Transaction tx)
          {
           tx.run("MATCH (n {username: $username})-[r:RATE]->(g:game {_id: $_id})"+
                  "SET r.vote = $vote", 
                  parameters("username",_user.getUsername(),"_id",_id,"vote",vote));
           return null;
          }
        } );
       
      return StatusCode.OK;
     }      
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in rateGame(): " + e.getMessage());
      System.out.println("[GraphConnector]: _id = " + _id + ", vote = " + vote);
      return StatusCode.ERR_GRAPH_UNKNOWN;
     }
   }     
  
  
  /* DESCRIPTION: Returns a list of up to "max" featured games representing favourite suggestions for the current user, which are selected by retrieving on the 
   *              first hand the games favourited by the users followed by the user in descending followedCount order, and on the second the most favourited 
   *              games in the application, again in descending followedCount order, where games already in the user favourite games list are filtered out
   *  
   * ARGUMENTS:   - int max:                         The maximum number of featured games to be returned (default = 25)
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   * 
   * RETURNS:     A StatusObject<List<GraphGame>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           Favourite games suggestions correctly retrieved (possibly empty)
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application
   *                 - ERR_GRAPH_NOTPOSITIVELIMIT:   The passed "max" parameter of favourite suggestions to return is non-positive
   *                 - ERR_GRAPH_USER_NOTEXISTS:     The user doesn't exist within the database (it was removed while it was logged in) 
   *              2) The list of GraphGames representing favourite suggestions for the user, as described above
   *              
   * NOTE:        In the GraphGame objects returned by the function the "value" attribute represents the game's favouriteCount
   */
  public StatusObject<List<GraphGame>> getFeaturedGamesList(int max)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check the passed "max" parameter to be positive
    if(max<=0)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_NOTPOSITIVELIMIT); 
    
    //If the local featured games list has at least "max" elements, return it directly
    if((_featuredGamesList!=null)&&(_featuredGamesList.size()>=max))
     {
      _featuredGamesList = _featuredGamesList.subList(0,max);
      return new StatusObject<List<GraphGame>>(StatusCode.OK,_featuredGamesList);
     }

    //Otherwise, fetch the featured games suggestions from the database
    try(Session session = driver.session())
     {
      //Check if the user still exists in the database (it might have been removed while it was logged in)
      Record record = session.readTransaction(new TransactionWork<Record>()
       {
        @Override
        public Record execute(Transaction tx)
         { return checkLoadUser(tx,_user.getUsername(),false); }
       } );
      if(record==null)
       return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_USER_NOTEXISTS);
      else
       {
        //Build the featured games suggestions list  by retrieving on the first hand the games favourited by the users followed by the user, and on
        //the second the most favourited games in the application, filtering out in both cases the games already in the user favourite games list 
        _featuredGamesList = session.readTransaction(new TransactionWork<List<GraphGame>>()
         {
          @Override
          public List<GraphGame> execute(Transaction tx)
           {
            List<GraphGame> featured = new ArrayList<>();
            
            Result result = tx.run("CALL "                                                            +    //----------------------------CALL-----------------------------
                                   "{ "                                                               +    
                                   "MATCH (n{username: $username})-[:FOLLOWS]->(p)-[:ADD]->(g:game) " +    //Query 1
                                   "WHERE NOT (n)-[:ADD]->(g) "                                       +    //(games favourited by users the user follows, order by global
                                   "RETURN g ORDER BY g.favouriteCount DESC LIMIT $limit "            +    //favouriteCount, excluding the games the user already follows)
                                   "UNION "                                                           +    //                       ----UNION-----
                                   "MATCH (g:game) "                                                  +    //Query 2
                                   "WHERE NOT ({username: $username})-[:ADD]->(g) "                   +    //(Most favourited games ordered by global favouriteCount,
                                   "RETURN g ORDER BY g.favouriteCount DESC LIMIT $limit "            +    //excluding the games the user already follows)
                                   "} "                                                               +    //----------------------------CALL-----------------------------
                                   "RETURN g._id,g.title,g.favouriteCount LIMIT $limit",                   //Post-UNION processing
                                   parameters("username",_user.getUsername(),"limit",max));  
            while (result.hasNext())
             { 
              Record record = result.next();
              GraphGame temp = new GraphGame(record.get("g._id").asString(),record.get("g.title").asString(),record.get("g.favouriteCount").asLong());
              featured.add(temp);
             }
            return featured;
           }
         } );
        
        return new StatusObject<List<GraphGame>>(StatusCode.OK,_featuredGamesList);
       }
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in getFeaturedGamesList(): " + e.getMessage());
      System.out.println("[GraphConnector]: max = " + max);
      _featuredGamesList = null;
      return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   }  
  
  
  /* Overloaded getFeaturedGamesList() method with default parameter max = 25 */
  public StatusObject<List<GraphGame>> getFeaturedGamesList()
   { return getFeaturedGamesList(25); }    
  
  
  //---------------------Games Relationships Utility Functions-----------------------
  
  /* DESCRIPTION: Returns a Boolean representing whether a game is in the user's favourite games list (true) or not (false)
   * 
   * ARGUMENTS:   - String _id:                      The "_id" of the game whose belongingness to the current user favourite games list is to be determined
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   * 
   * RETURNS:     A StatusObject<Boolean> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           Correctly determined whether the game is in the user's favourite games list or not (the Boolean value is valid)
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application 
   *                 - ERR_GRAPH_MISSINGARGUMENTS:   Not enough arguments to determine whether the game is in the user's favourite games list (missing _id)
   *              2) A Boolean representing whether the game is in the user's favourite games list (true) or not (false)
   */
  public StatusObject<Boolean> doIFavourite(String _id)
   {
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<Boolean>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check whether enough arguments were provided (the game _id)
    if(_id == null)
     return new StatusObject<Boolean>(StatusCode.ERR_GRAPH_MISSINGARGUMENTS);
    
    //Return "true" or "false" depending whether the game is in the user's favourite games list
    return new StatusObject<Boolean>(StatusCode.OK, new Boolean(searchGamesList(_id,_favouritesGamesList)!=null));
   }
  
  
  /* DESCRIPTION: Returns the user favourite games list
   * 
   * CALLABLE BY: ALL connected users logged into the application 
   * 
   * RETURNS:     A StatusObject<List<GraphGame>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           User favourite games list correctly retrieved
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application
   *              2) A List<GraphGame> object representing the user favourite games list
   *              
   * NOTE:        In the GraphGame objects returned by the function the "value" attribute represents the current user game rating [1,5]              
   */
  public StatusObject<List<GraphGame>> getFavouritesGamesList()
   { 
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    return new StatusObject<List<GraphGame>>(StatusCode.OK,_favouritesGamesList);
   }
  

  //---------------------------------------------------------------------------------
  //                              Analytics API
  //---------------------------------------------------------------------------------  
  
  /* DESCRIPTION: Returns a list of up to "max" Standard Users in descending followedCount order
   *
   * ARGUMENTS:   - int max:                         The maximum number Standard Users to be returned (default = 25)
   *  
   * CALLABLE BY: ANALYSTS, ADMINSITRATORS
   *
   * RETURNS:     A StatusObject<List<User>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           Most followed users correctly retrieved from the database (possibly empty)
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application
   *                 - ERR_GRAPH_USER_NOTALLOWED:    Insufficient privileges to perform the operation (the user is not an analyst or an administrator) 
   *                 - ERR_GRAPH_NOTPOSITIVELIMIT:   The passed "max" parameter of most followed users to return is non-positive 
   *              2) The list of Users representing the most followed users in the database, as described above
   *              
   * NOTE:        Differently from the searchUsers() function, this method also returns the users' email address
   */
  public StatusObject<List<User>> getMostFollowedUsers(int max)
   { 
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check if the user has sufficient privileges to perform the operation (i.e. it's not a Standard User)
    if(_userType == UserType.USER)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_USER_NOTALLOWED);
    
    //Check the passed "max" parameter to be positive
    if(max<=0)
     return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_NOTPOSITIVELIMIT);  
    
    try(Session session = driver.session())
     {
      //Retrieve and return the list of most followed users in the application
      List<User> mostFollowedUsers = session.readTransaction(new TransactionWork<List<User>>()
       {
        @Override
        public List<User> execute(Transaction tx)
         {
          List<User> results = new ArrayList<>();
          Result result = tx.run("MATCH (n:user) "                                                         +
                                 "RETURN n.username, n.joinDate, n.firstName, n.lastName, n.age, "         +
                                 "       n.email, n.gender, n.country, n.favouriteGenre, n.followedCount " +
                                 "ORDER BY n.followedCount DESC LIMIT $max",
                                 parameters("max",max));
          while (result.hasNext())
           { 
            //Populate the User object relative to each result and add it to the overall search results
            Record record = result.next();
            User temp = new User(record.get("n.username").asString(),"x",record.get("n.joinDate").asLocalDate());

            temp.setFirstName(record.get("n.firstName").asString());
            temp.setLastName(record.get("n.lastName").asString());
            if((record.get("n.age").asObject() != null))
             temp.setAge(new Long(record.get("n.age").asLong()));
            temp.setEmail(record.get("n.email").asString());
            if((record.get("n.gender").asObject() != null))
             temp.setGender(new Character(record.get("n.gender").asString().charAt(0)));
            temp.setCountry(record.get("n.country").asString());
            temp.setFavouriteGenre(record.get("n.favouriteGenre").asString());
            temp.setFollowedCount(record.get("n.followedCount").asLong());
              
            results.add(temp);
           }
          return results;
         }
       } );
        
      return new StatusObject<List<User>>(StatusCode.OK,mostFollowedUsers);
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in getMostFollowedUsers(): " + e.getMessage());
      System.out.println("[GraphConnector]: max = " + max);
      return new StatusObject<List<User>>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   } 
  
  
  /* Overloaded getMostFollowedUsers() method with default parameter max = 25 */
  public StatusObject<List<User>> getMostFollowedUsers()
   { return getMostFollowedUsers(25); }  
  
  
  /* DESCRIPTION: Returns a list of up to "max" games in descending favouriteCount order
   *
   * ARGUMENTS:   - int max:                         The maximum number of games to be returned (default = 25)
   * 
   * CALLABLE BY: ANALYSTS, ADMINSITRATORS
   *    
   * RETURNS:     A StatusObject<List<GraphGame>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           Most favourite games correctly retrieved from the database (possibly empty)
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application
   *                 - ERR_GRAPH_USER_NOTALLOWED:    Insufficient privileges to perform the operation (the user is not an analyst or an administrator) 
   *                 - ERR_GRAPH_NOTPOSITIVELIMIT:   The passed "max" parameter of most favourite games to return is non-positive 
   *              2) The list of GraphGame representing the most favourited games in the database, as described above
   *              
   * NOTE:        In the GraphGame objects returned by the function the "value" attribute represents the game's favouriteCount
   */
  public StatusObject<List<GraphGame>> getMostFavouriteGames(int max)
   { 
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check if the user has sufficient privileges to perform the operation (i.e. it's not a Standard User)
    if(_userType == UserType.USER)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_USER_NOTALLOWED);
    
    //Check the passed "max" parameter to be positive
    if(max<=0)
     return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_NOTPOSITIVELIMIT);  
    
    try(Session session = driver.session())
     {
      //Retrieve and return the list of most favourited games in the application    
      List<GraphGame> mostFavouriteGames = session.readTransaction(new TransactionWork<List<GraphGame>>()
       {
        @Override
        public List<GraphGame> execute(Transaction tx)
         {
          List<GraphGame> featured = new ArrayList<>();
          
          Result result = tx.run("MATCH (g:game)" +
                                 "RETURN g._id,g.title,g.favouriteCount ORDER BY g.favouriteCount DESC LIMIT $max",
                                 parameters("max",max));
          while (result.hasNext())
           { 
            Record record = result.next();
            GraphGame temp = new GraphGame(record.get("g._id").asString(),record.get("g.title").asString(),record.get("g.favouriteCount").asLong());
            featured.add(temp);
           }
          return featured;
         }
       } );
      
      return new StatusObject<List<GraphGame>>(StatusCode.OK,mostFavouriteGames); 
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in getMostFavouriteGames(): " + e.getMessage());
      System.out.println("[GraphConnector]: max = " + max);
      return new StatusObject<List<GraphGame>>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   }   
  
  
  /* Overloaded getMostFavouriteGames() method with default parameter max = 25 */
  public StatusObject<List<GraphGame>> getMostFavouriteGames()
   { return getMostFavouriteGames(25); }     
  
  
  /* DESCRIPTION: Returns a list of up to "max" tuples relative to different Standard Users (selectected in followedCount descending order)
   *              containing its joinDate, its age, its country, its gender and its favouriteGenre, information that can be used at an higher
   *              application level to compute and present statistics on the users registered within the database
   *
   * ARGUMENTS:   - int max:                         The maximum number of tuples to be returned (default = 100)
   *   
   * CALLABLE BY: ANALYSTS, ADMINSITRATORS
   * 
   * RETURNS:     A StatusObject<List<UserStats>> object composed of:
   *              1) A StatusCode, representing the result of the operation:
   *                 - OK:                           Standard Users summary information correctly retrieved from the database (possibly empty)
   *                 - ERR_GRAPH_USER_NOTLOGGED:     The user is not logged into the application
   *                 - ERR_GRAPH_USER_NOTALLOWED:    Insufficient privileges to perform the operation (the user is not an analyst or an administrator) 
   *                 - ERR_GRAPH_NOTPOSITIVELIMIT:   The passed "max" parameter of tuples to return is non-positive 
   *              2) The list of tuples [joinDate, age, gender, country, favouriteGenre] relative to the Standard Users registered within the database as described above
   */
  public StatusObject<List<UserStats>> getUsersSummaryStats(int max)
   { 
    //Check if the user is logged within the application
    if(_userType == UserType.NO_USER)
     return new StatusObject<List<UserStats>>(StatusCode.ERR_GRAPH_USER_NOTLOGGED);
    
    //Check if the user has sufficient privileges to perform the operation (i.e. it's not a Standard User)
    if(_userType == UserType.USER)
     return new StatusObject<List<UserStats>>(StatusCode.ERR_GRAPH_USER_NOTALLOWED);
    
    //Check the passed "max" parameter to be positive
    if(max<=0)
     return new StatusObject<List<UserStats>>(StatusCode.ERR_GRAPH_NOTPOSITIVELIMIT);  
    
    try(Session session = driver.session())
     {
      //Retrieve and return the [joinDate, age, gender, country, favouriteGenre] tuples relative to the Standard Users
      List<UserStats> summaryUsersStats = session.readTransaction(new TransactionWork<List<UserStats>>()
       {
        @Override
        public List<UserStats> execute(Transaction tx)
         {
          List<UserStats> results = new ArrayList<>();
          Result result = tx.run("MATCH (n:user) "                                      +
                                 "RETURN n.joinDate, n.age, n.gender, "                 +
                                 "       n.country, n.favouriteGenre, n.followedCount " +    //The followedCount is returned for ordering purposes in the query
                                 "ORDER BY n.followedCount DESC LIMIT $max",
                                 parameters("max",max));
          while (result.hasNext())
           { 
            //Populate the UserStats object relative to each result and add it to the overall search results
            Record record = result.next();
            UserStats temp = new UserStats(record.get("n.joinDate").asLocalDate(),(Long)record.get("n.age").asObject(),record.get("n.gender").asString().charAt(0),record.get("n.country").asString(),record.get("n.favouriteGenre").asString());
            results.add(temp);
           }
          return results;
         }
       } );
        
      return new StatusObject<List<UserStats>>(StatusCode.OK,summaryUsersStats);
     }
    catch(Exception e)
     {
      System.out.println("[GraphConnector]: Error in getUsersSummaryStats(): " + e.getMessage());
      System.out.println("[GraphConnector]: max = " + max);
      return new StatusObject<List<UserStats>>(StatusCode.ERR_GRAPH_UNKNOWN);
     }
   } 
  
  
  /* Overloaded getUsersSummaryStats() method with default parameter max = 100 */
  public StatusObject<List<UserStats>> getUsersSummaryStats()
   { return getUsersSummaryStats(100); }      
  

  
 /*==================================================================================================================================================*
  |                                                                                                                                                  |
  |                                                      PRIVATE API IMPLEMENTATION                                                                  |
  |                                                                                                                                                  |
  *==================================================================================================================================================*/
  
  //--------------------------------------------------------
  //         Database Connection Utility Functions
  //--------------------------------------------------------
 
  /* Attempt every "retryIntervalMs" ms and up "maxRetries" times to connect with the Neo4j database */
  private StatusCode tryConnect(int maxRetries, long retryIntervalMs) 
   {
    try
     {
      //Attempt to establish a connection to the Neo4j database
      this.driver = GraphDatabase.driver(this.databaseURI,AuthTokens.basic(this.databaseUser,this.databasePassword));
      this.driver.verifyConnectivity();
      
      //If neither the driver() nor the verifyConnectivity() functions raised an exception, the Connector is connected to the Neo4j database
      System.out.println("[GraphConnector]: Successfully established connection with the Neo4j database @" + this.databaseURI); 
      return StatusCode.OK;     
     } 
    catch(Exception e)
     {
      if(maxRetries == 1)    
       {
        //If the maximum number of connection attempts is reached, close the Connector and return an error
        System.out.println("[GraphConnector]: Coudln't connect to Neo4j database @" + this.databaseURI + ", please try again later");
        disconnect();
        return StatusCode.ERR_GRAPH_CONN_CANTCONNECT;
       }
      else
       {
        //Otherwise wait "retryIntervalMs" ms before the next connection attempt
        try
         { Thread.sleep(retryIntervalMs); }
        catch(InterruptedException f)         //Shouldn't happen (no thread synchronization mechanism is implemented)
         {}
        return tryConnect(maxRetries-1,retryIntervalMs);
       }
     }
   }
  
  
  //---------------------------------------------------------------------------------
  //                    User Session Attributes Initializations
  //---------------------------------------------------------------------------------
  
  /* DESCRIPTION: Initializes the number of Standard Users registered within the database
   *    
   * NOTE:        This is automatically called whenever a Standard User or Analyst logs into the
   *              application, while administrators should rely on the loadTotalNodesCount() method instead 
   */ 
  private void loadUsersCount(Session session)
   {
    standardUsersCount = session.readTransaction(new TransactionWork<Long>()
     {
      @Override
      public Long execute(Transaction tx)
       {
        Result result = tx.run("MATCH (n:user)"+
                               "RETURN COUNT(n)");
        return (result.single().get(0).asLong());
       }
     } );
   }
 
 
  /* DESCRIPTION: Initializes the number of games stored within the database
   *    
   * NOTE:        This is automatically called whenever any user logs within the application, 
   *              and administrators can use the loadTotalNodesCount() method to refresh the counter
   */ 
  private void loadGamesCount(Session session)
   {
    gamesCount = session.readTransaction(new TransactionWork<Long>()
     {
      @Override
      public Long execute(Transaction tx)
       {
        Result result = tx.run("MATCH (n:game)"+
                               "RETURN COUNT(n)");
        return (result.single().get(0).asLong());
       }
     } );  
   }
  
  
  /* DESCRIPTION: Loads the user followed users list
   *    
   * NOTES:       - Automatically called whenever any user logs (but not registers) within the application
   *              - The followed users UserTypes and passwords are hidden from their User objects 
   */ 
  private void loadFollowedUsersList(Session session)
   {
    _followedUsersList = session.readTransaction(new TransactionWork<List<User>>()
     {
      @Override
      public List<User> execute(Transaction tx)
       {
        List<User> followed = new ArrayList<>();
        
        Result result = tx.run("MATCH (n {username: $username})-[r:FOLLOWS]->(p) "   +
                               "RETURN p.username, p.password, p.joinDate, "         +
                               "p.firstName, p.lastName, p.age, p.email, p.gender, " +
                               "p.country,p.favouriteGenre, p.followedCount "        +
                               "ORDER BY p.followedCount DESC",
                               parameters("username",_user.getUsername()));  
        while (result.hasNext())
         { 
          Record record = result.next();
          User temp = new User(record.get("p.username").asString(),"x",record.get("p.joinDate").asLocalDate());

          temp.setFirstName(record.get("p.firstName").asString());
          temp.setLastName(record.get("p.lastName").asString());
          if((record.get("p.age").asObject() != null))
           temp.setAge(new Long(record.get("p.age").asLong()));
          temp.setEmail("x");                                      //The e-mail is hidden from the results
          if((record.get("p.gender").asObject() != null))
           temp.setGender(new Character(record.get("p.gender").asString().charAt(0)));
          temp.setCountry(record.get("p.country").asString());
          temp.setFavouriteGenre(record.get("p.favouriteGenre").asString());
          temp.setFollowedCount(record.get("p.followedCount").asLong());
          
          followed.add(temp);
         }
        return followed;
       }
     } );
   }
  
  
  /* DESCRIPTION: Loads the user favourite games list
   *    
   * NOTES:       - Automatically called whenever any user logs (but not registers) within the application
   */ 
  private void loadFavouriteGamesList(Session session)
   {
    _favouritesGamesList = session.readTransaction(new TransactionWork<List<GraphGame>>()
     {
      @Override
      public List<GraphGame> execute(Transaction tx)
       {
        List<GraphGame> favourites = new ArrayList<>();
        
        Result result = tx.run("MATCH (n {username: $username})-[a:ADD]->(p) "   +
                               "OPTIONAL MATCH (n)-[r:RATE]->(p) "               +
                               "RETURN p._id, p.title, p.favouriteCount, r.vote ORDER BY p.favouriteCount DESC",
                               parameters("username",_user.getUsername()));  
        while (result.hasNext())
         { 
          Record record = result.next();
          GraphGame temp = new GraphGame(record.get("p._id").asString(),record.get("p.title").asString(),record.get("p.favouriteCount").asLong());
          if((record.get("r.vote").asObject() != null))
           temp.setVote(new Long(record.get("r.vote").asLong()));
          favourites.add(temp);
         }
        return favourites;
       }
     } );
   }  
  
  
  //---------------------------------------------------------------------------------
  //                         User Lists Search Functions
  //---------------------------------------------------------------------------------
  
  /* DESCRIPTION: Search for an User by username in a List<User> object
   *    
   * RETURNS:     The User object if found or null otherwise
   */ 
  private User searchUsersList(String username, List<User> userList)
   {
    if(userList == null)
     return null;
    else
     for(User user: userList)
      if(user.getUsername().equals(username))
       return user;
    return null;
   }
  
  
  /* DESCRIPTION: Search for a GraphGame by _id in a List<Graphgame> object
   *    
   * RETURNS:     The GraphGame object if found or null otherwise
   */ 
  private GraphGame searchGamesList(String _id, List<GraphGame> gameList)
   {
    if(gameList == null)
     return null;
    else
     for(GraphGame game: gameList)
      if(game._id.equals(_id))
       return game;
    return null;
   }
  
  
  //---------------------------------------------------------------------------------
  //                     Transaction-level Utility Functions
  //---------------------------------------------------------------------------------
  
  /* Returns the information relative to a user registered within the database 
   * - if "load" == TRUE: Load all the user properties and labels
   * - if "load" == FALSE: Load just the user node <id> (which can be used to determine if the exist exists within the database)
   */
  private Record checkLoadUser(Transaction tx, String username, boolean load)
   {
    Result result;

    if(load)  //Load all the user properties and labels
     result = tx.run("MATCH (n {username: $username})"                     +
                     "RETURN labels(n), n.password, n.joinDate,"           +
                     "n.firstName, n.lastName, n.age, n.email, n.gender,"  +
                     "n.country,n.favouriteGenre, n.followedCount",
                     parameters("username",username));  
    else      //Load just the user node <id> (which can be used to determine if the exist exists within the database)
     result = tx.run("MATCH (n {username: $username})" +
                     "RETURN n",
                     parameters("username",username));
    
    //Return the user record if it was found
    if(result.hasNext())    
     return result.next();
    else 
     return null;
   }
  

  /* Returns the information associated with a user, a game and their possible relationships in the database 
   * - if "load" == TRUE: Load the user, the game and their possible :ADD and :RATE relationships <id> and the "vote" attribute of the latter
   * - if "load" == FALSE: Load just the user and game node <id> (used to check whether the user and game exist within the database)
   */
  private Record checkLoadGame(Transaction tx, String username, String _id, boolean load)
   {
    Result result;
    if(load)  //Load the user, the game and their possible :ADD and :RATE relationships <id> and the "vote" attribute of the latter
     result = tx.run("OPTIONAL MATCH (n {username: $username})" +
                     "OPTIONAL MATCH (g:game{_id: $_id})"       +
                     "OPTIONAL MATCH (n)-[a:ADD]->(g)"          +
                     "OPTIONAL MATCH (n)-[r:RATE]->(g)"         +
                     "RETURN n,g,g.title,g.favouriteCount,a,r,r.vote",
                     parameters("username",username,"_id",_id));
    else     //Load just the user and game node <id> (used to check whether the user and game exist within the database
     result = tx.run("OPTIONAL MATCH (n {username: $username})" +
                     "OPTIONAL MATCH (g:game{_id: $_id})"       +
                     "RETURN n,g",
                     parameters("username",username,"_id",_id));

    //Returned the retrieved information (where note that the result.hasNext() check is not necessary due to the OPTIONAL MATCH keywords being used in the query)
    return result.next();   
   }
  
 }
