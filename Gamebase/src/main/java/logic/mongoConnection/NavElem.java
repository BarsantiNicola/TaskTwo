package logic.mongoConnection;

import java.util.List;

import logic.StatusObject;
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
		
		if( this.next != null )
			return false;
		next.setPrev(this);
		this.next = next;
		 
		return true;
		 
	}
	
	boolean setPrev( NavElem prev ){
		
		if( this.prev != null )
			return false;
		next.setNext(this);
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
