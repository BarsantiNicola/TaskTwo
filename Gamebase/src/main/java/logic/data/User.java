package logic.data;

import java.time.LocalDate;

/* This class contains the information associated to an user in the context of the application */
public class User
 {
  //---------------------------------------------------------------------------------
  //                                 Attributes
  //---------------------------------------------------------------------------------
  
  //Final Attributes
 	private final String username;         //The user's username, which must be unique in the context of the application
 	private final String password;         //The user's password, which is required by him to access the application and that is stored encrypted via SHA-256
 	private final LocalDate joinDate;      //The date the user registered within the application (YYY-MM-DD)
	
	 //Variable Attributes 
	 private String firstName;              //The user's first name              
	 private String lastName;               //The user's last name
  private Long age;                      //The user's age (where as described below a Long is used for compatibility reasons)
  private String email;                  //The user's email
	 private Character gender;              //The user's gender (M or F)
	 private String country;                //The user's country
	 private String favouriteGenre;         //The user's favourite videogame genre
	 private Long followedCount;            //The number of the user's followers
	
  /* NOTES:   1) Wrapper classes were used to allow for a better handling of null values
   *          2) The use of selected classes is dictated due to current implementation of the Neo4j driver
   *             (as the LocalDate and the use of the Long class, since Integer/int is not natively supported)
   */   
 
  //---------------------------------------------------------------------------------
  //                                Constructors
  //---------------------------------------------------------------------------------
	
	 /* Default Constructor (used when an errors occurs) */
	 public User()
	  {
	   username = null;
	   password = null;
	   joinDate = null;
	  }

	 /* Username + Password Constructor */
  public User(String username,String password)
   {
    this.username = username;
    this.password = password;
    this.joinDate = null;
   }

  /* Username + Password + joinDate Constructor */
  public User(String username,String password,LocalDate joinDate)
   {
    this.username = username;
    this.password = password;
    this.joinDate = joinDate;
   }
 
  /* All-parameters Constructor */
  public User(String username,String password,LocalDate joinDate,String firstName,String lastName,Long age,String email,Character gender,String country,String favouriteGenre,Long followedCount)
   {
    this.username = username;
    this.password = password;
    this.joinDate = joinDate;
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.email = email;
    this.gender = gender;
    this.country = country;
    this.favouriteGenre = favouriteGenre;
    this.followedCount = followedCount;
   }

  //---------------------------------------------------------------------------------
  //                                   Getters
  //---------------------------------------------------------------------------------
  public String getUsername()
   { return username; }

  public String getPassword()
   { return password; }

  public LocalDate getJoinDate()
   { return joinDate; }

  public String getFirstName()
   { return firstName; }

  public String getLastName()
   { return lastName; }

  public Long getAge()
   { return age; }

  public String getEmail()
   { return email; }

  public Character getGender()
   { return gender; }

  public String getCountry()
   { return country; }

  public String getFavouriteGenre()
   { return favouriteGenre; }

  public Long getFollowedCount()
   { return followedCount; }

  //---------------------------------------------------------------------------------
  //                      Setters (non-final attributes only)
  //---------------------------------------------------------------------------------
  public void setFirstName(String firstName)
   { this.firstName = firstName; }

  public void setLastName(String lastName)
   { this.lastName = lastName; }

  public void setAge(Long age)
   { this.age = age; }

  public void setEmail(String email)
   { this.email = email; }

  public void setGender(Character gender)
   { this.gender = gender; }

  public void setCountry(String country)
   { this.country = country; }

  public void setFavouriteGenre(String favouriteGenre)
   { this.favouriteGenre = favouriteGenre; }

  public void setFollowedCount(Long followedCount)
   { this.followedCount = followedCount; }

  //---------------------------------------------------------------------------------
  //                                     Other
  //---------------------------------------------------------------------------------
  @Override
  public String toString()
   {
    return "User [username=" + username + ", password=" + password + ", joinDate=" + joinDate + ", firstName=" + firstName + 
           ", lastName=" + lastName + ", age=" + age + ", email=" + email + ", gender=" + gender + ", country=" + country + 
           ", favouriteGenre=" + favouriteGenre + ", followedCount=" + followedCount + "]";
   }
 }


 