package logic.data;

/* This class represents a game in the context of the Neo4j database */
public class GraphGame
 {
  //Final Attributes
  public final String _id;           //The game unique identifier
  public final String title;         //The game title
  public final Long favouriteCount;  //The game favouriteCount
  Long vote;
  
  /* NOTE: For the "_id" and "title" attributes it is supposed a 1-to-1 relation with a game stored into the MongoDB database */
  
  
  
  /* NOTE: the "value" is a multi-purpose attribute, which depending on the function called represents:
   * 
   * 1) getFavouriteGamesList()                                   -->  The current user game rating [1,5]
   * 
   * 2) getFavouriteGamesList(String username)                    -->  The target user game rating  [1,5]
   * 
   * 3) getFeaturedGamesList(), getFeaturedGamesList(int max),    -->  The game favouriteCount
   *    getMostFavouriteGames(), getMostFavouriteGames(int max)
   */
  
  //---------------------------------------------------------------------------------
  //                                Constructors
  //---------------------------------------------------------------------------------
  
  /* Default Constructor (used when an errors occurs) */
  public GraphGame()
   {
    this._id = null;
    this.title = null;
    this.favouriteCount = null;
    this.vote = null;
   }
  
  /* _id + title Constructor */
  public GraphGame(String _id,String title)
   {
    this._id = _id;
    this.title = title;
    this.favouriteCount = null;
    this.vote = null;
   }
  
  /* _id + title + favouriteCount Constructor */
  public GraphGame(String _id,String title, Long favouriteCount)
   {
    this._id = _id;
    this.title = title;
    this.favouriteCount = favouriteCount;
   }
  
  /* All-parameters Constructor */
  public GraphGame(String _id,String title, Long favouriteCount, Long vote)
   {
    this._id = _id;
    this.title = title;
    this.favouriteCount = favouriteCount;
    this.vote = vote;
   }
  
  //---------------------------------------------------------------------------------
  //                                   Getters
  //---------------------------------------------------------------------------------
  public Long getVote()
   { return vote; }
 
  //---------------------------------------------------------------------------------
  //                      Setters (non-final attributes only)
  //---------------------------------------------------------------------------------
  public void setVote(Long vote)
   { this.vote = vote; } 
  
  //---------------------------------------------------------------------------------
  //                                     Other
  //---------------------------------------------------------------------------------
  @Override
  public String toString()
   { return "GraphGame [_id=" + _id + ", title=" + title + ", favouriteCount=" + favouriteCount + ", vote=" + vote + "]"; }
  
 }
