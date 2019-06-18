//helper class for queueing up requests
public class Request {
	protected int floor = 0;
	protected int requestNo;
	
	//for simulating an error within the elevator. scheduler is "blind" to this information
	protected int errorCode = 0;
	
	public Request( int req) {
		requestNo = req;
	}
	
	//setters and getters
	
	public int getRequestNo() {return requestNo;}
	public int getFloorNo() {return floor;}

	public void setFloorNo(int floorN) {
		floor = floorN;
		
	}

	public int getError() {
		return errorCode;
	}
	
	public void setError(int e) {
		errorCode = e;
	}
}
