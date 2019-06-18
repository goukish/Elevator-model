//node for the array of arrival sensors

public class ArrivalSensor {
	protected int id;
	protected int currentFloor = 0;
	protected int nextFloor = 0;
	protected int direction = 2;
	protected int port;
	protected Boolean isRequestPending = false;
	protected long lastDispatch;
	
	public ArrivalSensor(int idNo, int portNo) {
		id = idNo;
		port = portNo;
	}
	//check if an elevator has timed out
	public boolean checkForTimeout() {
		Boolean b = false;
		if ( isRequestPending == true &&( System.nanoTime()- lastDispatch )/1000000 > 25000 ) {
			b = true;
		}
		return b;
		
	}
	//update direction,ect
	public void updateArrivalSensor(int curr, int next,int dir) {
		currentFloor = curr;
		nextFloor = next;
		direction = dir;
	}
	//update to reflect a sent request
	public void setPendingRequest(Boolean t) {
		isRequestPending = t;
		if (t == true) {lastDispatch = System.nanoTime();}
		}
	//getters and setters
	public Boolean getPendingRequest() {return isRequestPending; }
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	public int getNextFloor() {
		return nextFloor;
	}
	public int getId() {
		return id;
	}

	public void setNextFloor(int destination) {
		nextFloor = destination;
		
	}

	public int getPort() {
		return port;
	}
}
