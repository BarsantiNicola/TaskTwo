package logic.data;

import logic.StatusCode;
import logic.UserType;

/* Returned at login attempts */

public class UserInfo
 {
  public StatusCode statusCode;
  public UserType userType;
  public User user;
  
  public UserInfo()
   {
    this.statusCode = StatusCode.ERR_GRAPH_UNKNOWN;
    this.userType = UserType.NO_USER;
    user = null;
   }
  
  public UserInfo(StatusCode status)
   {
   this.statusCode = status;
   this.userType = UserType.NO_USER;
   user = null;
  }
  
  public UserInfo(StatusCode status, UserType userType, User user)
   {
   this.statusCode = status;
   this.userType = userType;
   this.user = user;
  }
  
  @Override
  public String toString()
   {
    return "StatusCode = "+ statusCode + ", UserType = " + userType + "\n" + user;
   }
  
 }
