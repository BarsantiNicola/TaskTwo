package logic.graphConnector;

import java.util.List;
import logic.StatusCode;
import logic.data.GraphGame;
import logic.data.StatusObject;
import logic.data.User;
import logic.data.UserInfo;
import logic.data.UserStats;
import logic.UserType;

public interface GraphInterface
 {
  /*====================================================================================================================================
    |                                                GUIDA RAPIDA GRAPHCONNECTOR                                                       |
    ====================================================================================================================================
    |                                                       Setup Rapido                                                               |
    ------------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | try(GraphConnector graph = new GraphConnector())                                                                                 |
    |  {                                                                                                                               |
    |   graph.connect("bolt://172.16.0.78:7687","neo4j","password");       //Connettersi al database (ricordare di avere la VPN attiva)|
    |   ...                                                                                                                            |
    |   graph.login("username","usernamePassword");                        //Login (username e password)                               |
    |   ...                                                                   //USARE:                                                 |
    |                                                                        - "admin","adminPassword"   -->  Administrator            |
    |   ...                                                                     - "adrian","adrianPassword" -->  Analyst               |
    |                                                                        - "adri","adriPassword"     -->  Standard User            |
    |   ...                                                                                                                            |
    |   graph.logout();                                                    //Logout come utente corrente                               |
    |   ...                                                                                                                            |
    |   graph.login("username2","username2Password");                      //Login come altro utente                                   |
    |                                                                                                                                  |
    |   ...                                                                                                                            |
    |  }                                                                                                                               |
    |                                                                                                                                  |
    | NOTA: Se non viene usato un blocco try-with sull oggetto "graph" (come da esempio), e' necessario chiudere esplicitamente la     |
    |       connessione al database al termine dell operazione tramite il metodo graph.close() (o graph.disconnect(), che è uguale)    |
    |                                                                                                                                  |
    ------------------------------------------------------------------------------------------------------------------------------------
    |                                              Gestione degli Errori e Ritorni                                                     |
    ------------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    |   Ogni metodo del GraphConnector effettua sempre tutti i controlli necessari prima di eseguire una qualunque operazione, da      |
    |   controllo sui privilegi di accesso, a controlli di integrità fra il contenuto dei database e il contenuto locale, e dove       |
    |   possibile anche una gestione automatica degli errori minori, dove tutto viene sempre notificato al livello applicazione        |
    |   utilizzando l'enumerato "StatusCode" insieme alla classe template "StatusObject<T>", che funge dal contenitore per il codice   |
    |   di stato dell'operazione (StatusCode.OK se è andato tutto bene) e per cosa è stato richiesto dal livello applicazione.         |
    |   Da notare inoltre che alcuni codici di errori riportati rappresentano "soft errors" recuperabili, possibilmente invocando di   |
    |   nuovo il metodo in questione (fare riferimento ai possibili errori riportati dai singoli metodi più avanti e dall'enumerato    |
    |   "StatusObject" per maggiori dettagli).                                                                                         |
    |                                                                                                                                  |
    ------------------------------------------------------------------------------------------------------------------------------------
    |                                          Informazioni Mantenute nel GraphConnector                                               |
    ------------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | Il GraphConnector mantiene autonomamente un insieme di informazioni relative alla sessione corrente, tra cui:                    |
    |                                                                                                                                  |
    |-----------------------------------------                                                                                         |
    | Informazioni di base sulla connessione |                                                                                         |
    |-----------------------------------------                                                                                         |
    |  Queste informazioni vengono settate appena il connector riesce a connettersi al database                                        |
    |                                                                                                                                  |
    | - URI del database:                           graph.getDatabaseURI()       //Nel nostro caso sempre "172.16.0.78"                |
    |  - Username (di Neo4j) del database:          graph.getDatabaseUser()      //Nel nostro caso sempre "neo4j"                      |
    |  - Password (di Neo4j) del database:          graph.getDatabasePassword()  //Nel nostro caso sempre "password"                   |
    |                                                                                                                                  |
    |--------------------------------------------------------                                                                          |     
    | Informazioni relative all'utente attualmente connesso |                                                                          |
    |--------------------------------------------------------                                                                          |
    |  Le seguenti informazioni vengono caricate al login o alla registrazione di un utente:                                           |
    |                                                                                                                                  |
    |  - Informazioni generali dell'utente (descritte come da classe User):   graph.getUser()                                          |                                                       
    | - Il livello di privilegio dell'utente:                                 graph.getUserType()                                      |
    | - La lista degli utenti seguiti dall'utente:                            graph.getFollowedUsersList()                             |   
    |  - La lista dei giochi preferiti dell'utente:                                                                                    |
    |                                                                                                                                  |
    | Le seguenti informazioni invece sono caricate su richiesta e vengono cachate all'interno del connector:                          |
    |                                                                                                                                  |
    | - La lista di utenti suggeriti per l'utente da seguire:                 graph.getSuggestedUsersList()                            |
    | - La lista dei giochi suggeriti per l'utente da aggiungere:             graph.getFeaturedGamesList()                             |
    |                                                                                                                                  |
    | Da notare inoltre che se l'utente aggiunge un utente/gioco alla sua lista followed/favourite tale utente/gioco sarà rimosso      |
    | automaticamente anche dalla sua lista degli utenti/giochi suggeriti, se presente.                                                |
    |                                                                                                                                  |
    |------------------------------------                                                                                              |
    | Informazioni generali sul dataset |                                                                                              |
    |------------------------------------                                                                                              |
    | Le seguenti informazioni vengono caricate per i non-amministratori solo al login, mentre gli amministratori possono refresharle  |
    | in un qualunque momento chiamando la funzione "loadTotalNodesCount()"                                                            |
    |                                                                                                                                  |
    | - Numero di Standard Users registrati nell'applicazione:                graph.getStandardUsersCount()                            |
    | - Numero di Analisti registrati nell'applicazione:                      graph.getAnalystsCount()         //SOLO AMMINISTRATORI   |
    | - Numero di Amministratori registrati nell'applicazione:                graph.getAdministratorsCount()   //SOLO AMMINISTRATORI   |
    | - Numero totale di utenti registrati nell'applicazione:                 graph.getTotalUsersCount()       //SOLO AMMINISTRATORI   |
    | - Numero di giochi registrati nell'applicazione:                        graph.getGamesCount()                                    |
    |-----------------------------------------------------------------------------------------------------------------------------------
  
  
  
  
  
  
    ====================================================================================================================================
    |                                                  GUIDA API GRAPHCONNECTOR                                                        |
    ====================================================================================================================================
    |                                                       Connection API                                                             |
    ------------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    |  + Connettersi ad un Database:   StatusCode connect(String uri,String user,String password)                                      |
    |                                  StatusCode connect(String uri,String user,String password, int retryMax, long intervalloMs)     |
    |                                                                                                                                  |
    |    Questa deve essere sempre la prima funzione dopo aver chiamato il costruttore, e serve per connettersi al database Neo4j.     |
    |    Da notare che nel caso la connessione fallisca il connettore riproverà automaticamente un certo numero di volte (default = 5) |
    |    a riconnettersi ogni tot millisecondi (default = 1000), parametri che possono essere impostati utilizzando la seconda         |
    |    signature sopra (da usare nel caso la vostra connessione faccia particolarmente schifo).                                      |
    |    Per gli scopi di questo progetto comunque utilizzare sempre, ricordandosi di tenere attiva la VPN:                            |
    |                                                                                                                                  |
    |                    uri = "172.16.0.78"                  user = "neo4j"                 password = "password"                     |
    |                                                                                                                                  |
    |                                                                                                                                  |    
    |  + Disconnettersi dal Database:  StatusCode disconnect()                                                                         |
    |                                  StatusCode close()                                                                              |
    |                                                                                                                                  |
    |    Queste funzioni (che sono equivalenti) disconnettono il connector dal database, e nel nostro caso dovrebbero essere           |
    |    utilizzate solo al termine del programma nel caso non si sia utilizzato per l'oggetto GraphConnector un blocco try-with       |
    |    come nella guida "Setup Rapido" sopra.                                                                                        |
    |                                                                                                                                  |
    |----------------------                                                                                                            |
    | Funzioni di Utilità |                                                                                                            |
    |----------------------                                                                                                            |
    |                                                                                                                                  |
    | + Verificare che il connector sia effettivamente connesso al database:     boolean isConnected()                                 |
    | + Ritornare l'URI del database:                                            String getDatabaseURI()                               |
    | + Ritornare l'user del database:                                           String getDatabaseUser()                              |
    | + Ritornare la password del database:                                      String getDatabasePassword()                          |
    |                                                                                                                                  |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                              User Registration and Login API                                                     |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | + Registrare un nuovo utente al database:                         StatusObject<UserInfo> register(User user)                     |
    |   (se successo conta anche come login)                                                                                           |
    |                                                                                                                                  |
    | + Effettuare il login come utente esistente:                      StatusObject<UserInfo> login(String username, String password) |
    |                                                                                                                                  |
    |   Da notare che in caso di successo dopo aver chiamato le precedenti funzioni tutte le modifiche sulle informazioni dell'utente  |
    |   DEVONO essere effettuate sull'oggetto User (o Analyst o Administrator a seconda del privilegio dell'utente) contenute          |
    |   all'interno dell'oggetto UserInfo ritornato (le quali una volta modificate possono essere salvate nel database tramite il      |
    |   metodo saveUser(), vedi dopo)                                                                                                  |
    |                                                                                                                                  |
    | + Effettuare il logout da utente corrente:                        StatusCode logout()                                            |
    |                                                                                                                                  |
    |   Dopo aver effettuato il logout() è possibile fare di nuovo il login() o register() come nuovo utente senza dover               |
    |   disconnettersi o riconnettersi di nuovo al database.                                                                           |
    |                                                                                                                                  |
    | + Refreshare tutti i contatori dei nodi del database              StatusCode loadTotalNodesCount()  //SOLO AMMINISTRATORI        |
    |                                                                                                                                  |
    |   Questa funzione, chiamabile solo da utenti amministratori, permette di refreshare i numeri degli Standard Users, degli         |
    |   Analysts, degli Administrators, degli utenti totali, e dei giochi presenti nel database (vedere "Informazioni Generali         |
    |   sul Dataset" su)                                                                                                               |
    |                                                                                                                                  |
    |----------------------                                                                                                            |
    | Funzioni di Utilità |                                                                                                            |
    |----------------------                                                                                                            |
    |                                                                                                                                  |
    | + Determinare se si è loggati nell'applicazione:            boolean isLoggedIn()                                                 |
    | + Ritornare l'oggetto User relativo all'utente corrente:    User getUser()                                                       |
    | + Ritornare lo UserType dell'utente corrente:               UserType getUserType()                                               |
    | + Ritornare il numero di Standard Users:                    StatusObject<Long> getStandardUsersCount()                           |
    | + Ritornare il numero di Analysts:                          StatusObject<Long> getAnalystsCount()        //SOLO AMMINISTRATORI   |
    | + Ritornare il numero di Administrators:                    StatusObject<Long> getAdministratorsCount()  //SOLO AMMINISTRATORI   |
    | + Ritornare il numero totale di utenti:                     StatusObject<Long> getTotalUsersCount()      //SOLO AMMINISTRATORI   |
    | + Ritornare il numero di giochi:                            StatusObject<Long> getGamesCount()                                   |
    |                                                                                                                                  |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                  User Manipulation API                                                           |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | + Salvare le informazioni dell'utente contenute nel relativo oggetto User nel database:      StatusCode saveUser()               |
    |                                                                                                                                  |
    | + Rimuovere un utente dal database:                       StatusCode deleteUser(String username)      //SOLO AMMINISTRATORI      |
    |                                                                                                                                  |
    | + Promuovere un utente da Standard User a Analyst:        StatusObject<UserInfo> upgradeToAnalyst()   //SOLO STANDARD USERS      |
    |                                                                                                                                  |
    |   Da notare che in caso di successo ogni successiva modifica alle informazioni dell'utente dovrà essere effettuata               |
    |   utilizzando l'oggetto Analyst contenuto nell'oggetto UserInfo ritornato dalla funzione                                         |
    |                                                                                                                                  |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                 Users Relationships API                                                          |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | + Aggiungere un utente alla lista degli utenti seguiti:             followUser(String username)                                  |
    |                                                                                                                                  |
    | + Rimuovere un utente dalla lista degli utenti seguiti:             unFollowUser(String username)                                |
    |                                                                                                                                  |
    | + Ricerca di utenti per username sulla base di una maschera:        StatusObject<List<User>> searchUsers(String mask)            |    
    |                                                                     StatusObject<List<User>> searchUsers(String mask, int max)   |
    |                                                                                                                                  |
    |   La ricerca ritorna una lista di oggetti User (default max = 25) il cui username contiene la maschera passata come argomento,   |
    |   e il numero massimo di utenti ritornati può essere cambiato chiamando la funzione con la seconda signature                     |
    |                                                                                                                                  |
    | + Ottenere una lista di suggerimenti di utenti da seguire:          StatusObject<List<User>> getSuggestedUsersList()             |
    |                                                                     StatusObject<List<User>> getSuggestedUsersList(int max)      |
    |                                                                                                                                  |
    |   Il metodo ritorna una lista di oggetti User (default max = 25) che rappresentano suggerimenti di user da seguire per l'utente  |
    |   corrente, e il numero massimo di utenti ritornati può essere cambiato chiamando la funzione con la seconda signature           |
    |                                                                                                                                  |
    |----------------------                                                                                                            |
    | Funzioni di Utilità |                                                                                                            |
    |----------------------                                                                                                            |
    |                                                                                                                                  |
    | + Determinare se l'utente corrente segue un altro utente:           StatusObject<Boolean> doIFollow(String username)             |
    | + Ritornare la lista degli utenti seguiti dall'utente corrente:     StatusObject<List<User>> getFollowedUsersList()              |
    |                                                                                                                                  |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                  Games Manipulation API                                                          |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | + Aggiungere un gioco al database:                                  StatusCode addGame(GraphGame game) //SOLO AMMINISTRATORI     |
    |                                                                                                                                  |
    | + Rimuovere un gioco dal database:                                  StatusCode deleteGame(String _id)  //SOLO AMMINISTRATORI     |
    |                                                                                                                                  |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                  Games Relationships API                                                         |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | + Aggiungere un gioco alla lista dei giochi preferiti:               StatusCode addToFavourites(String _id)                      |
    |                                                                                                                                  |
    | + Rimuovere un gioco dalla lista dei giochi preferiti:               StatusCode removeFromFavourites(String _id)                 |
    |                                                                                                                                  |
    | + Ritornare i giochi favoriti di un altro utente:         StatusObject<List<GraphGame>> getFavouritesGamesList(String username)  |
    |                                                                                                                                  |
    |   Da notare che, a meno che questa funzione non venga chiamata da un amministratore, l'utente target identificato da "username"  |
    |   DEVE essere presente nella lista degli utenti seguiti dall'utente attuale affinché la funzione abbia successo                  |
    |                                                                                                                                  |
    | + Ritornare il favouriteCount di un gioco nel database:              StatusObject<Long> getGameFavouriteCount(String _id)        |
    |                                                                                                                                  |
    | + Impostare/aggiornare la valutazione dell'utente di un gioco [1,5]: StatusCode rateGame(String _id, int vote)                   |
    |                                                                                                                                  |
    | + Ritornare la lista di giochi suggeriti da segurie per l'utente:    StatusObject<List<GraphGame>> getFeaturedGamesList()        |
    |                                                                      StatusObject<List<GraphGame>> getFeaturedGamesList(int max) |      
    |                                                                                                                                  |
    |   Il metodo ritorna una lista di oggetti GraphGame (default max = 25) che rappresentano giochi suggeriti all'utente, e il        |
    |   limite massimo di giochi ritornati può essere cambiato chiamando la funzione con la seconda signature                          |
    |                                                                                                                                  |
    |----------------------                                                                                                            |
    | Funzioni di Utilità |                                                                                                            |
    |----------------------                                                                                                            |
    |                                                                                                                                  |
    | + Determinare se un gioco è nella lista dei preferiti:              StatusObject<Boolean> doIFavourite(String _id)               |
    | + Ritornare la lista dei giochi preferiti dell'utente:              StatusObject<List<GraphGame>> getFavouritesGamesList()       |
    |                                                                                                                                  |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                      Analytics API                                                               |
    |-----------------------------------------------------------------------------------------------------------------------------------
    |                                                                                                                                  |
    | + Ritornare gli utenti più seguiti dell'applicazione:               StatusObject<List<User>> getMostFollowedUsers()              |
    |                                                                     StatusObject<List<User>> getMostFollowedUsers(int max)       |
    |                                                                                                                                  |
    | + Ritornare i giochi più preferiti dell'applicazione:               StatusObject<List<GraphGame>> getMostFavouriteGames()        |
    |                                                                     StatusObject<List<GraphGame>> getMostFavouriteGames(int max) |
    |                                                                                                                                  |
    | + Ritornare tuple [joinDate, age, country, gender, favouriteGenre]  getUsersSummaryStats()                                       |
    |   degli utenti (per il calcolo di statistiche a più alto livello):  getUsersSummaryStats(int max)                                |
    |                                                                                                                                  |
    | Tutte queste funzioni sono chiamabili solo da Analyst (e ovviamente Amministratori), e ritornano di default fino a 25 risultati  |
    | (tranne getUsersSummaryStats() che ne ritorna fino a 100, ma tanto attualmente ci sono 42 utenti nell'applicazione), e il numero |
    | massimo di risultati riportati ancora può essere impostato chiamando le funzioni con la loro seconda signature                   |
    |                                                                                                                                  |
    ------------------------------------------------------------------------------------------------------------------------------------
  
  
  
  
  
  
    ====================================================================================================================================
    |                                                  GUIDA API NEL DETTAGLIO                                                         |
    ====================================================================================================================================*/
  
    //----------------------------------------------------------------------------------------------------------------------------------
    //                                                   Connection-related API                                                                              
    //----------------------------------------------------------------------------------------------------------------------------------
  
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
    public StatusCode connect(String uri, String user, String password,int maxRetries, long retryIntervalMs);
  
  
    /* Overloaded connect() function with default parameters maxRetries = 5 and retryLongIntervalMs = 1000 */
    public StatusCode connect(String uri, String user, String password);
    
    
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
    public StatusCode disconnect();    
  
  
    /* Required by the AutoCloseable interface (proxies the disconnect() function)*/
    public void close();
    
    
    //--------------------------------------------------Connection Utility Functions---------------------------------------------------
    
    /* DESCRIPTION: Returns a boolean representing whether the Connector is currently connected to a Neo4j database (true) or not (false)
     * 
     * CALLABLE BY: ALL
     * 
     * RETURNS:     A boolean representing whether the Connector is connected to a Neo4j database (true) or not (false)
     */
    public boolean isConnected();
    
    
    /* DESCRIPTION: Returns the database URI associated with the connector
     * 
     * CALLABLE BY: ALL
     * 
     * RETURNS:     The database URI associated with the connector (possibly null)
     */
    public String getDatabaseURI();
    
    
    /* DESCRIPTION: Returns the database user used by the connector
     * 
     * CALLABLE BY: ALL
     * 
     * RETURNS:     The database user used with the connector (possibly null)
     */ 
    public String getDatabaseUser();
    
    
    /* DESCRIPTION: Returns the database password used by the connector
     * 
     * CALLABLE BY: ALL
     * 
     * RETURNS:     The database password used with the connector (possibly null)
     */
    public String getDatabasePassword();
    
    
    //----------------------------------------------------------------------------------------------------------------------------------
    //                                              Users Registration and Login API                                                                             
    //----------------------------------------------------------------------------------------------------------------------------------
    
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
    public StatusObject<UserInfo> register(User user);
   
   
   
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
    public StatusObject<UserInfo> login(String username, String password);
    
    
    
    /* DESCRIPTION: Logs out the user from the application
     * 
     * CALLABLE BY: ALL connected users logged into the application
     * 
     * RETURNS:     - OK:                        User successfully logged out from the application
     *              - ERR_GRAPH_USER_NOTLOGGED:  The user is not logged into the application
     */
    public StatusCode logout();
    
    
    /* DESCRIPTION: Loads the Node Counter Attributes (number of users divided by type and number of games) from the database
     * 
     * CALLABLE BY: ADMINISTRATORS;
     * 
     * RETURNS:     - OK:                          Node counters loaded succesfully
     *              - ERR_GRAPH_USER_NOTALLOWED:   Insufficient privileges to perform the operation (the user is not an administrator)
     *              
     * NOTE:        This function is called automatically whenever an administrator logs in, and can be called again to refresh the node counter attributes
     */ 
    public StatusCode loadTotalNodesCount();
    
    
    //--------------------------------------------------User Session Utility Functions----------------------------------------------------
    
    /* DESCRIPTION: Checks whether the user is logged into the application
     * 
     * CALLABLE BY: ALL
     * 
     * RETURNS:     A boolean representing whether the user is logged into the application (true) or not (false)
     */  
    public boolean isLoggedIn();
    
    
    /* DESCRIPTION: Returns the User object associated to user currently logged within the application
     * 
     * CALLABLE BY: ALL
     * 
     * RETURNS:     The User object associated to user currently logged within the application (possibly null)
     */  
    public User getUser();
    
    
    /* DESCRIPTION: Returns the UserType of the user currently logged within the application
     * 
     * CALLABLE BY: ALL
     * 
     * RETURNS:     The UserType of the user currently logged within the application (possibly UserType.NO_USER)
     */  
    public UserType getUserType();
    
    
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
    public StatusObject<Long> getStandardUsersCount();
    
    
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
    public StatusObject<Long> getAnalystsCount();
    
    
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
    public StatusObject<Long> getAdministratorsCount();
    
    
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
    public StatusObject<Long> getTotalUsersCount();
    
    
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
    public StatusObject<Long> getGamesCount();
    
    
    //----------------------------------------------------------------------------------------------------------------------------------
    //                                                  Users Manipulation API                                                                             
    //----------------------------------------------------------------------------------------------------------------------------------
    
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
    public StatusCode saveUser();
    
    
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
    public StatusCode deleteUser(String username);
    
    
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
    public StatusObject<UserInfo> upgradeToAnalyst();
    
    
    //----------------------------------------------------------------------------------------------------------------------------------
    //                                                 Users Relationships API                                                                             
    //----------------------------------------------------------------------------------------------------------------------------------    
    
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
    public StatusCode followUser(String username);
    
    
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
    public StatusCode unFollowUser(String username);
    
    
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
    public StatusObject<List<User>> searchUsers(String mask, int max);
    
    
    /* Overloaded searchUsers() function with default parameter max = 25 */
    public StatusObject<List<User>> searchUsers(String mask);
    
    
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
    public StatusObject<List<User>> getSuggestedUsersList(int max);
    
    
    /* Overloaded getSuggestedUsersList() method with default parameter max = 25 */
    public StatusObject<List<User>> getSuggestedUsersList();
    
    
    //----------------------------------------------Users Relationships Utility Functions----------------------------------------------------
    
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
    public StatusObject<Boolean> doIFollow(String username);
    
    
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
    public StatusObject<List<User>> getFollowedUsersList();
    
    
    //----------------------------------------------------------------------------------------------------------------------------------
    //                                                   Games Manipulation API                                                                             
    //----------------------------------------------------------------------------------------------------------------------------------
    
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
    public StatusCode addGame(GraphGame game);
    
    
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
    public StatusCode deleteGame(String _id);
    
    
    //----------------------------------------------------------------------------------------------------------------------------------
    //                                                 Games Relationships API                                                                             
    //----------------------------------------------------------------------------------------------------------------------------------
    
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
     *               - ERR_GRAPH_GAME_NOTEXISTS:      The game doesn't exist within the database
     *                - ERR_GRAPH_GAME_INCONSISTENCY:  An inconsistency was detected between the local favourite games list and the database,
     *                                               where the former has been aligned to the latter (the game is in the user's favourite games list)
     *
     * NOTE:        In case of success if the game is included in the user featured games list, it is automatically removed from such list
     */   
    public StatusCode addToFavourites(String _id);
    
    
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
    public StatusCode removeFromFavourites(String _id);
    
    
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
    public StatusObject<List<GraphGame>> getFavouritesGamesList(String username);
    
    
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
    public StatusObject<Long> getGameFavouriteCount(String _id);
    
    
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
    public StatusCode rateGame(String _id, int vote);
    
    
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
    public StatusObject<List<GraphGame>> getFeaturedGamesList(int max);
    
    
    /* Overloaded getFeaturedGamesList() method with default parameter max = 25 */
    public StatusObject<List<GraphGame>> getFeaturedGamesList();
    
    
    //----------------------------------------------Games Relationships Utility Functions----------------------------------------------------
    
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
    public StatusObject<Boolean> doIFavourite(String _id);
    
    
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
    public StatusObject<List<GraphGame>> getFavouritesGamesList();
    
    
    //----------------------------------------------------------------------------------------------------------------------------------
    //                                                      Analytics API                                                                             
    //----------------------------------------------------------------------------------------------------------------------------------    
    
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
    public StatusObject<List<User>> getMostFollowedUsers(int max);
    
    
    /* Overloaded getMostFollowedUsers() method with default parameter max = 25 */
    public StatusObject<List<User>> getMostFollowedUsers();
    
    
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
    public StatusObject<List<GraphGame>> getMostFavouriteGames(int max);
    
    
    /* Overloaded getMostFavouriteGames() method with default parameter max = 25 */
    public StatusObject<List<GraphGame>> getMostFavouriteGames();
    
    
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
    public StatusObject<List<UserStats>> getUsersSummaryStats(int max);
    
    
    /* Overloaded getUsersSummaryStats() method with default parameter max = 100 */
    public StatusObject<List<UserStats>> getUsersSummaryStats();
    
 }
