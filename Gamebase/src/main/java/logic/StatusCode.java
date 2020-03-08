package logic;

public enum StatusCode
{
 //--------------------------------------------------------
 //                      GENERAL
 //--------------------------------------------------------
 OK,
 ERR_UNKNOWN,
 
 //--------------------------------------------------------
 //                 MongoDB Error Codes
 //--------------------------------------------------------
 //ERR_MONGO_....
 
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