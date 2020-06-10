package logic.data;

import java.time.LocalDate;

/* This class represents an Administrator in the context of the application
*
* NOTE: While it currently adds nothing with respect to its User superclass, this class was introduced taking into account possible future additions in the application */

public class Administrator extends User
 {
  //---------------------------------------------------------------------------------
  //                                Constructors
  //---------------------------------------------------------------------------------
  
  /* Default Constructor (used when an errors occurs) */
  public Administrator()
   { super(); }
  
  /* Username + Password Constructor */
  public Administrator(String username, String password)
   { super(username,password); }
  
  /* Username + Password + joinDate Constructor */
  public Administrator(String username,String password,LocalDate joinDate)
   { super(username,password,joinDate); }
  
  /* All-parameters Constructor */
  public Administrator(String username,String password,LocalDate joinDate,String firstName,String lastName,Long age,String email,Character gender,String country,String favouriteGenre,Long followedCount)
   { super(username,password,joinDate,firstName,lastName,age,email,gender,country,favouriteGenre,followedCount); }
  
 }
