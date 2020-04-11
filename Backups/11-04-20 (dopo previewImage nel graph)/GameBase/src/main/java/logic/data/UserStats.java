package logic.data;

import java.time.LocalDate;

/* This represents the summary information of a Standard User in the database (namely its joinDate, age, country, gender and favouriteGenre), which can be used
    at an higher application level to compute and presents statistics on the users registered within the database (used by the getUserSummaryStats() function */
public class UserStats
 {
  //---------------------------------------------------------------------------------
  //                          Attributes (public final)
  //---------------------------------------------------------------------------------
  public final LocalDate joinDate;
  public final Long age;
  public final Character gender;
  public final String country;
  public final String favouriteGenre;
  
  //---------------------------------------------------------------------------------
  //                                Constructors
  //---------------------------------------------------------------------------------
  
  /* Default Constructor (used when an errors occurs) */
  public UserStats()
   {
    this.joinDate = null;
    this.age = null;
    this.gender = null;
    this.country = null;
    this.favouriteGenre = null;
   }
  
  /* All-parameters Constructor */
  public UserStats(LocalDate joinDate, Long age, Character gender, String country, String favouriteGenre)
   {
    this.joinDate = joinDate;
    this.age = age;
    if(gender.charValue()=='n')     //This "barbatrick" is required since Neo4j doesn't natively support the char type
     this.gender = null;
    else
     this.gender = gender;
    this.country = country;
    this.favouriteGenre = favouriteGenre;
   }
    
  //---------------------------------------------------------------------------------
  //                                     Other
  //---------------------------------------------------------------------------------
  @Override
  public String toString()
   { return "[joinDate=" + joinDate + ", age=" + age + ", gender=" + gender + ", country=" + country + ", favouriteGenre=" + favouriteGenre + "]"; }
  
 }
