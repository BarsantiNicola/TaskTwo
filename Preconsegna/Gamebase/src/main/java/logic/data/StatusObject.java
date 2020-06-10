package logic.data;

import logic.StatusCode;

/* This template class is used to attach to the returns of the different functions 
   in the application a StatusCode, which summarizes the result of the operation   */
public class StatusObject<T>
 {
  //---------------------------------------------------------------------------------
  //                          Attributes (public final)
  //---------------------------------------------------------------------------------
  public final StatusCode statusCode;    //The StatusCode of the operation
  public final T element;                //The result of the operation
  
  //---------------------------------------------------------------------------------
  //                                Constructors
  //---------------------------------------------------------------------------------
  
  /* Default Constructor (used when an unknown error occurs) */
  public StatusObject()
   { 
    this.statusCode = StatusCode.ERR_UNKNOWN; 
    this.element = null;
   }
  
  /* StatusCode Constructor (used when a specific error occurs) */
  public StatusObject(StatusCode statusCode)
   { 
    this.statusCode = statusCode;
    this.element = null; 
   }
  
  /* All-parameters Constructor (used when the operation was succesful or a minor error occured) */
  public StatusObject(StatusCode statusCode,T element)
   {
    this.statusCode = statusCode;
    this.element = element;
   }
  
  //---------------------------------------------------------------------------------
  //                                   Other
  //---------------------------------------------------------------------------------
  @Override
  public String toString()
   { return statusCode + ", Value = " + element; }
  
 }