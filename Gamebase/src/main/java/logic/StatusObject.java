package logic;

public class StatusObject<T> {

	private StatusCode statusCode;
	private T element;
	
		  
	public StatusObject( StatusCode status, T element ){
		
		   this.statusCode = status;
		   if( this.statusCode != StatusCode.OK)
			   this.element = element;
	}

	public T getValue() {
		
		if( this.statusCode != StatusCode.OK && this.statusCode != StatusCode.ERR_NETWORK_PARTIAL_UNREACHABLE )
			return null;
		
		return element;
	}
	
	public StatusCode getOperationResult(){
		return this.statusCode;
	}
	
	public static StatusObject<String> buildError( StatusCode error ){
		
		String stringError;
		
		switch(error){
			case OK:
				stringError = "Operation correctly Executed";
				break;
			case ERR_DOCUMENT_UNKNOWN:
				stringError = "Unknown error is happen during the execution";
				break;
			case ERR_WRONG_PORT:
				stringError = "Error, the port must be set to 22017 or 22018";
				break;
			case ERR_WRONG_IP_ADDR:
				stringError = "Error, the IP address is not correctly inserted";
				break;
			case ERR_NETWORK_UNREACHABLE:
				stringError = "Error, the network is unreachable at the moment";
				break;
			case ERR_NETWORK_PARTIAL_UNREACHABLE:
				stringError = "Error, the network became unreachable before the ending of the read. Some data could be loaded";
				break;
			case ERR_DOCUMENT_GAME_NOT_FOUND:
				stringError = "Error, unable to find the selected game";
				break;
			case  ERR_DOCUMENT_INVALID_VOTE:
				stringError = "Error, the vote MUST be in [0,5]";
				break;
			case ERR_DOCUMENT_MULTIMEDIA_FIELD_NOT_FOUND:
				stringError = "Error, the game doensn't have the multimedia field";
				break;

			default: stringError = "";
		
		}
		
		return new StatusObject<String>( error, stringError );
	}
}
