package logic.data;

import logic.StatusCode;
import java.util.ArrayList;
import java.util.List;

public class StatusList<T>
 {
  public StatusCode statusCode;
  public List<T> elements;
  
  public StatusList()
   {
    this.statusCode = StatusCode.ERR_GRAPH_UNKNOWN;
    this.elements = new ArrayList<>();
   }
  
  public StatusList(StatusCode status)
   {
    this.statusCode = status;
    this.elements = new ArrayList<>();
   }
  
  public StatusList(StatusCode status, List<T> elements)
   {
    this.statusCode = status;
    this.elements = new ArrayList<>(elements);
   }

  @Override
  public String toString()
   {
    return "StatusCode = "+ statusCode + ", Elements = " + elements;
   }
  
 }
