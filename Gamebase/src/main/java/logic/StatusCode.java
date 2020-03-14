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
 
 //--------------------------------------------------------
 //                  Neo4j Error Codes
 //--------------------------------------------------------
 ERR_GRAPH_UNKNOWN,
 
 //------------------User-Related Errors-------------------
 ERR_GRAPH_USER_MISSINGARGUMENTS,
 ERR_GRAPH_USER_ALREADYEXISTS,
 ERR_GRAPH_USER_NOTEXISTS,
 ERR_GRAPH_USER_WRONGPASSWORD,
 ERR_GRAPH_USER_UNKNOWNTYPE
 
 ;

}