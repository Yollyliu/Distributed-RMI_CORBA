

import java.util.HashMap;

public class RequestBuffer {

	private HashMap<Integer, Request> RequestMap = new HashMap<>();
	
	
	
	public void PutRequest(int SequenceNum, Request request){
		RequestMap.put(SequenceNum, request);
	}
	
	public Request GetRequest(int SequenceNum){
		Request ret = new Request();
		if(RequestMap.containsKey(SequenceNum))
			ret = RequestMap.get(SequenceNum);
		return ret;
	}
	
	public boolean Exist(int SequenceNum){
		
		if(RequestMap.containsKey(SequenceNum))
			return true;
		else
			return false;
	}
	
}
