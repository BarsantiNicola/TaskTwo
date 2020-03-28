package logic.mongoConnection;

import java.util.List;
import logic.data.PreviewGame;

class NavElem {
	
	private List<PreviewGame> data;
	private NavElem next;
	private NavElem prev;
	
	NavElem( List<PreviewGame> data ){
		
		this.data = data;
		next = null;
		prev = null;
	}
	
	boolean setNext( NavElem next ){
		
		this.next = next;
		 
		return true;
		 
	}
	
	boolean setPrev( NavElem prev ){
		
		this.prev = prev;
		return true;
		 
	}
	
	List<PreviewGame> getData(){ 
		return this.data;
	}
	
	NavElem getNext() {
		
		return this.next;
	}
	
	NavElem getPrev() {
		return this.prev;
	}
	
	boolean hasNext() {
		return next != null;
	}
	
	boolean hasPrev() {
		return prev != null;
	}

}
