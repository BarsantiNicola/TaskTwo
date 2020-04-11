package logic.data;

import logic.UserType;

/* This class contains the information associated to the current user in the application, which 
   are its UserType and its User object (returned by the login() and upgradeToAnalyst() functions) */
public class UserInfo
 {
  //---------------------------------------------------------------------------------
  //                          Attributes (public final)
  //---------------------------------------------------------------------------------
  public final UserType userType;     //The user's UserType
  public final User user;             //The user's associated User object
  
  //---------------------------------------------------------------------------------
  //                                Constructors
  //---------------------------------------------------------------------------------
  
  /* Default Constructor (used when an errors occurs) */
  public UserInfo()
   {
    this.userType = UserType.NO_USER;
    user = null;
   }
  
  /* All-parameters Constructor */
  public UserInfo(UserType userType, User user)
   {
    this.userType = userType;
    this.user = user;
   }
  
  //---------------------------------------------------------------------------------
  //                                     Other
  //---------------------------------------------------------------------------------
  @Override
  public String toString()
   { return "UserType = " + userType + ", " + user; }
  
 }
