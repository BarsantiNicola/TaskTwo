package logic;

public enum StatusCode
{
 //--------------------------------------------------------
 //                      GENERAL
 //--------------------------------------------------------
 OK,
 ERR_UNKNOWN,
 ERR_WRONG_PORT,
 ERR_WRONG_IP_ADDR,
 ERR_NETWORK_UNREACHABLE,
 ERR_NETWORK_PARTIAL_UNREACHABLE,
 
 //--------------------------------------------------------
 //                 MongoDB Error Codes
 //--------------------------------------------------------
 ERR_DOCUMENT_UNKNOWN,
 ERR_DOCUMENT_GAME_NOT_FOUND,
 ERR_DOCUMENT_INVALID_VOTE,
 ERR_DOCUMENT_MULTIMEDIA_FIELD_NOT_FOUND,
 ERR_DOCUMENT_MIN_INDEX_REACHED,
 ERR_DOCUMENT_NO_NEXT,
 ERR_DOCUMENT_HAS_NEXT,
 ERR_DOCUMENT_HAS_PREV,
 ERR_DOCUMENT_NO_PREV,
 ERR_DOCUMENT_BAD_GAME_ID,
 
 
 //---------------------------------------------------------------------------------
 //                      GraphConnector Error Codes
 //---------------------------------------------------------------------------------
 
 //----------------------------General Errors---------------------------------------
 ERR_GRAPH_UNKNOWN                     { @Override public String toString() { return "[GraphConnectorError]: Unknown Error"; } },
 ERR_GRAPH_MISSINGARGUMENTS            { @Override public String toString() { return "[GraphConnectorError]: Insufficient arguments to perform the operation"; } },
 ERR_GRAPH_NOTPOSITIVELIMIT            { @Override public String toString() { return "[GraphConnectorError]: The \"max\" parameter passed to the function is non-positive"; } },
 
 //------------------------Connection-related Errors--------------------------------
 ERR_GRAPH_CONN_ALREADYCONNECTED       { @Override public String toString() { return "[GraphConnectorError]: The GraphConnector is already connected to a Neo4j database"; } },
 ERR_GRAPH_CONN_NEGATIVERETRIES        { @Override public String toString() { return "[GraphConnectorError]: The passed \"maxRetries\" or the \"retryIntervalMs\" parameters are negative"; } },
 ERR_GRAPH_CONN_NOTCONNECTED           { @Override public String toString() { return "[GraphConnectorError]: The GraphConnector is not connected to a Neo4j database"; } },
 ERR_GRAPH_CONN_CANTCONNECT            { @Override public String toString() { return "[GraphConnectorError]: The GraphConnector couldn't connect to the specified Neo4j database "; } },

 //---------------------------User-related Errors-----------------------------------
 ERR_GRAPH_USER_NOTALLOWED             { @Override public String toString() { return "[GraphConnectorError]: Insufficient privileges to perform the operation"; } },
 ERR_GRAPH_USER_ALREADYLOGGED          { @Override public String toString() { return "[GraphConnectorError]: The user is already logged within the application"; } },
 ERR_GRAPH_USER_NOTLOGGED              { @Override public String toString() { return "[GraphConnectorError]: The user is not logged within the application"; } },
 ERR_GRAPH_USER_ALREADYEXISTS          { @Override public String toString() { return "[GraphConnectorError]: A user with such username already exists within the application"; } },
 ERR_GRAPH_USER_NOTEXISTS              { @Override public String toString() { return "[GraphConnectorError]: A user with such username doesn't exist within the application"; } },
 ERR_GRAPH_USER_WRONGPASSWORD          { @Override public String toString() { return "[GraphConnectorError]: The user exists within the database, but its password doesn't match"; } },
 ERR_GRAPH_USER_UNKNOWNTYPE            { @Override public String toString() { return "[GraphConnectorError]: The Usertype of the user is unknown (possible inconsistency within the database)"; } },
 ERR_GRAPH_USER_SELFDELETE             { @Override public String toString() { return "[GraphConnectorError]: The administrator user is attempting to delete himself from the database"; } },
 ERR_GRAPH_USER_UPGRADEUNNECESSARY     { @Override public String toString() { return "[GraphConnectorError]: The user doesn't require an upgrade (it's already an analyst or an administrator)"; } },
 ERR_GRAPH_USER_SELFRELATION           { @Override public String toString() { return "[GraphConnectorError]: The user is attempting to follow himself"; } },
 ERR_GRAPH_USER_INCONSISTENCY          { @Override public String toString() { return "[GraphConnectorError]: An inconsistency was detected between the local followed users list and the database, where the former has been aligned to the latter"; } },
 ERR_GRAPH_USER_ALREADYFOLLOWS         { @Override public String toString() { return "[GraphConnectorError]: The current user already follows the target user"; } },
 ERR_GRAPH_USER_NOTFOLLOWS             { @Override public String toString() { return "[GraphConnectorError]: The current user doesn't follow the target user"; } },
 
 //----------------------------Game-Related Errors----------------------------------
 ERR_GRAPH_GAME_ALREADYEXISTS          { @Override public String toString() { return "[GraphConnectorError]: A game with such \"_id\" already exists within the database"; } },
 ERR_GRAPH_GAME_NOTEXISTS              { @Override public String toString() { return "[GraphConnectorError]: A game with such \"_id\" doesn't exist within the database"; } },
 ERR_GRAPH_GAME_ALREADYADDED           { @Override public String toString() { return "[GraphConnectorError]: The game is already in the user favourite games list"; } },
 ERR_GRAPH_GAME_NOTADDED               { @Override public String toString() { return "[GraphConnectorError]: The game is not in the user favourite games list"; } },
 ERR_GRAPH_GAME_INCONSISTENCY          { @Override public String toString() { return "[GraphConnectorError]: An inconsistency was detected between the local favourite games list and the database, where the former has been aligned to the latter"; } },
 ERR_GRAPH_GAME_NOTFOLLOWER            { @Override public String toString() { return "[GraphConnectorError]: The non-administrator user is attempting to retrieve the favourite games list of an user he doesn't follow"; } },
 ERR_GRAPH_GAME_VOTERANGE              { @Override public String toString() { return "[GraphConnectorError]: The game user rating is out of bounds [1,5]"; } },
 ;  
}