import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Queue;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

public class Scheduler_Queue {							//This class is used to set a "line up" of what floor is next in queue

   DatagramPacket sendPacket, receivePacket, receivePacket2;
   static DatagramSocket sendSocket;
   static DatagramSocket receiveSocket, receiveSocket2;
   Queue<Request> outelevatorQ;
   LinkedList<Request> inelevatorQ;
   ArrayList<ArrivalSensor> arrivalList;
   int RequestNumber = 0;
   static Logger log;
   
   public Scheduler_Queue(Logger l1)
   {
	   log = l1;
	   outelevatorQ = new LinkedList<Request>();
	   inelevatorQ = new LinkedList<Request>();
	   arrivalList = new ArrayList<ArrivalSensor>(5);
	   arrivalList.add(0, null);
	   arrivalList.add(1, createArrivalSensor(1,2050)); 
	   arrivalList.add(2, createArrivalSensor(2,2051)); 
	   arrivalList.add(3, createArrivalSensor(3,2052)); 
	   arrivalList.add(4, createArrivalSensor(4,2053));
	    
      try {
         // Construct a datagram socket and bind it to any available 
         // port on the local host machine. This socket will be used to
         // send UDP Datagram packets
         sendSocket = new DatagramSocket();

         // Construct a datagram socket and bind it to port 5000 
         // on the local host machine. This socket will be used to
         // receive UDP Datagram packets.
         receiveSocket = new DatagramSocket(2300);
         //receiveSocket2 = new DatagramSocket(600);
         
         // to test socket timeout (2 seconds)
         //receiveSocket.setSoTimeout(2000);
      } catch (SocketException se) {
         se.printStackTrace();
         System.exit(1);
      } 
   }

   public void receive()					//receives the messages from the floor, and decodes weather it is a destination or arrival, and if the user wants to go up or down
   {
	   
	   //periodically check for errors
	   int x = getStatus();
       if(x != 0) {
    	   System.out.println("Critical fault, elevator " + x + " to be sent diagnostic message.");
    	   //send a diagnostic message to elevator to see what component is broken
    	   byte a[] = new byte[] { (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte)0,
   				(byte) 0, (byte) x, (byte) 0 };
    	   sendPacket = new DatagramPacket(a, a.length, receivePacket.getAddress(), arrivalList.get(x).getPort());
    	   arrivalList.get(x).setPendingRequest(false);

   		try {
   			sendSocket.send(sendPacket);
   		} catch (IOException e) {
   			e.printStackTrace();
   			System.exit(1);
   		}
       }
       
	   
      byte data[] = new byte[30];
      receivePacket = new DatagramPacket(data, data.length);
      try {        
         receiveSocket.receive(receivePacket);
      } catch (IOException e) {
         System.out.print("IO Exception: likely:");
         System.out.println("Receive Socket Timed Out.\n" + e);
         e.printStackTrace();
         System.exit(1);
      }

    //shut down case
    	if(data[1] == 1) {
    		log.closeReader();
    		System.exit(1);
    	}
    		
      int destOrArrival = data[2];
      //int floorNo = data[4];
      //int UpOrDown = data[6];
      int ID = data[8];
      if (data[0] == 6) {
		readFile(data);
	}
      //elevator finishes request
	if(data[0] == 2 && data[9] == 0) {
    	  arrivalList.get(ID).setPendingRequest(false);
      }
	//elevator updates position
      else if (data[0] == 5) {
    	  arrivalList.get(ID).updateArrivalSensor(data[4],data[5],data[6]);
      }
	//error message
      else if (data[0] == 2 && data[9] != 0) {
    	  handleError(data);
      }
	
      //elevator completed a job
      if(data[0] == 2 && destOrArrival == 1) {
    	  if ( data[7] == 0) {
    		  dispatchRequest(ID);
    		  log.writeOutArrival(data[4], data[3]);
    	  }
    	  else {
    		  dispatchInRequest(ID,data[3]);
    		  log.writeOutArrival(data[4], data[3]);
    		  }
      }
      
   }



private void dispatchInRequest(int ID,int r) {		//this tells the elevator to go to the certain floor from the in pannel
	  for(Request req:inelevatorQ) {
		  if(req.getRequestNo()==r) {
			  int destination = req.getFloorNo();
			  inelevatorQ.remove(req);
			  log.writeElevatorButtonPress(destination);
			  int direction = 2; 
				if(arrivalList.get(ID).getCurrentFloor() > destination) 
					{direction = 0;}
				if(arrivalList.get(ID).getCurrentFloor() < destination) 
					{direction = 1;}
				arrivalList.get(ID).setNextFloor(destination);
				byte a[] = new byte[] { (byte) 2, (byte) 0, (byte) 1, (byte) 0, (byte) destination, (byte) 0, (byte)direction,
						(byte) 0, (byte) ID, (byte) 0 };
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				String time = generateDate();
				try {
					outputStream.write(a);
					outputStream.write(time.getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				byte msg[] = outputStream.toByteArray();
				sendPacket = new DatagramPacket(msg, msg.length, receivePacket.getAddress(), arrivalList.get(ID).getPort());
				try {
					sendSocket.send(sendPacket);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
				//System.out.println("Server: packet sent");
				arrivalList.get(ID).setPendingRequest(true);
				return;
		  }
	  }
	
}

private void dispatchRequest(int ID) {		//this tells the elevator to go to the certain floor from the out pannel
	   if (!(outelevatorQ.isEmpty())) {
		Request r = outelevatorQ.poll();
		int destination = r.getFloorNo();
		int requestnum = r.getRequestNo();
		int direction = 2; 
		
		//pass an error to elevator. scheduler is "blind" to this and will see it when elevator times out.
		int err = r.getError();
		
		if(arrivalList.get(ID).getCurrentFloor() > destination) 
			{direction = 0;}
		if(arrivalList.get(ID).getCurrentFloor() < destination) 
			{direction = 1;}
		arrivalList.get(ID).setNextFloor(destination);
		byte a[] = new byte[] { (byte) 2, (byte) 0, (byte) 1, (byte) requestnum, (byte) destination, (byte) 0, (byte)direction,
				(byte) 1, (byte) ID, (byte) err };
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String time = generateDate();
		try {
			outputStream.write(a);
			outputStream.write(time.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		byte msg[] = outputStream.toByteArray();
		sendPacket = new DatagramPacket(msg, msg.length, receivePacket.getAddress(), arrivalList.get(ID).getPort());
		
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//System.out.println("Server: packet sent");
		arrivalList.get(ID).setPendingRequest(true);
	}
		
		// We're finished, so close the sockets.
}

//add a request to the queues
private void scheduleRequest(int fl1,int fl2,int err) {		//this method is used to schedule a request, sending the elevator to go to the the floor
	Request r1 = new Request(RequestNumber);
	r1.setFloorNo(fl1);
	r1.setError(err);
	Request r2 = new Request(RequestNumber);
	r2.setFloorNo(fl2);
	if(outelevatorQ.isEmpty() ) {
			outelevatorQ.add(r1);
			inelevatorQ.add(r2);
			int id = chooseElevator(fl1);
			if (arrivalList.get(id).getPendingRequest()== false) {
				dispatchRequest(id);
			}
			
	}
	else if(!(outelevatorQ.isEmpty())){
		outelevatorQ.add(r1);
		inelevatorQ.add(r2);
	}
	RequestNumber++;
}
//selection of nearest elevator for iter 2
private int chooseElevator(int fl) {
	int closest = 1;
	int distance = 23;
	for(ArrivalSensor a : arrivalList.subList(1, arrivalList.size())) {
		if(a.getPendingRequest()==false) {	
			int x = Math.abs(fl - a.getNextFloor());
			if(x<distance) {
				closest = a.getId();
				distance = x;
			}
		}
	}
	return closest;
}

//error checking on each arrival sensor
public int getStatus() {
	for(ArrivalSensor a : arrivalList.subList(1, arrivalList.size())) {
		if(a.checkForTimeout()) {
			return a.getId();
		}
	}
return 0;
}

//error handler for iteration 3
private void handleError(byte[] data) {
	switch(data[9]) {
	case 3:
		System.out.println("Elevator " + data[8] + " Stuck. Request will be re-routed to a different elevator.");
		//remove faulty in panel request 
		
		for(Request r : inelevatorQ) {
			if (r.getRequestNo() == data[3]) {
				inelevatorQ.remove(r);
				break;
			}
			break;
		}
		
		scheduleRequest(data[4],data[5],0);
		System.out.println("request sucessfuly re-queued.");
		break;
	case 2:
		System.out.println("Doors are stuck closed. Elevator " + data[8] + " Restarted");
		arrivalList.get(data[8]).setPendingRequest(false);
		break;
	case 1:
		System.out.println("Doors are stuck open. Elevator " + data[8] + " Restarted");
		arrivalList.get(data[8]).setPendingRequest(false);
		break;
	}
}

public static String generateDate() { 				//we created this method to generate a time stamp
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	return sdf.format(cal.getTime());
}
//utility function
public ArrivalSensor createArrivalSensor(int idNo,int portNo) {
	return new ArrivalSensor(idNo,portNo);
}

//schedules a request with information from received packet. it also passes on error data to trigger an error in elevator.
public void readFile(byte[] data) {
		scheduleRequest(data[4],data[5],data[9]);
	}
	


//driver program
public static void main( String args[] )
   {
	  Logger l1 = new Logger(args[0]);
	  System.out.println("Program Starting: ");
      Scheduler_Queue c = new Scheduler_Queue(l1);
      int i = 1;
      while(i <= 10000) {
    	  c.receive();
    	  i++;
      }
      sendSocket.close();
      receiveSocket.close();
      System.out.println("Program finished");
   }
}