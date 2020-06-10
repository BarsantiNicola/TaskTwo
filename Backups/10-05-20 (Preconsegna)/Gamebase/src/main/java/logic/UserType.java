package logic;

/* This enumeration is used to identify the type (or class) of an user in the application, which also determines its privileges */
public enum UserType
{	
 NO_USER,            //Unauthenticated user, can only register() or login() in the application
 USER,               //Standard User, can perform all operations that don't require an higher level of privilege
 ANALYST,            //Analyst, can perform all operations allowed to the Standard Users plus a set of data-analysis oriented operations
	ADMINISTRATOR,      //Administrator, can perform all operations within the application
 ;
}


