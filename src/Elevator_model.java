
import java.awt.Canvas;
import java.awt.Color;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

public class Elevator_model{		//this class creates a new elevator
	   static DatagramPacket sendPacket, receivePacket, sendPacket2;
	   static DatagramSocket sendSocket;
	   static DatagramSocket receiveSocket;
	   static int id;
	   static int currentFloor;
	   static int nextFloor;
	   static int direction = 2;
	   static int servingID;
	   
	   static JFrame frame;
		
	   static JButton button101;
	   static JButton button102;
	   static JButton button103;
	   static JButton button104;	
	   static JButton button105;	
	   static JButton button106;	
	   static JButton button107;	
	   static JButton button108;	
	   static JButton button109;	
	   static JButton button110;	
	   static JButton button111;	
	   static JButton button112;	
	   static JButton button113;	
	   static JButton button114;	
	   static JButton button115;	
	   static JButton button116;	
	   static JButton button117;	
	   static JButton button118;	
	   static JButton button119;	
	   static JButton button120;
	   static JButton button121;	
	   static JButton button122;	
	   
	   
		/*static JButton button222 = new JButton("Elevator 2");
		static JButton button221 = new JButton("Elevator 2");
		static JButton button220 = new JButton("Elevator 2");
		static JButton button219 = new JButton("Elevator 2");
		static JButton button218 = new JButton("Elevator 2");
		static JButton button217 = new JButton("Elevator 2");
		static JButton button216 = new JButton("Elevator 2");
		static JButton button215 = new JButton("Elevator 2");
		static JButton button214 = new JButton("Elevator 2");
		static JButton button213 = new JButton("Elevator 2");
		static JButton button212 = new JButton("Elevator 2");
		static JButton button211 = new JButton("Elevator 2");
		static JButton button210 = new JButton("Elevator 2");
		static JButton button209 = new JButton("Elevator 2");
		static JButton button208 = new JButton("Elevator 2");
		static JButton button207 = new JButton("Elevator 2");
		static JButton button206 = new JButton("Elevator 2");
		static JButton button205 = new JButton("Elevator 2");
		static JButton button204 = new JButton("Elevator 2");
		static JButton button203 = new JButton("Elevator 2");
		static JButton button202 = new JButton("Elevator 2");
		static JButton button201 = new JButton("Elevator 2");
		
		static JButton button322 = new JButton("Elevator 3");
		static JButton button321 = new JButton("Elevator 3");
		static JButton button320 = new JButton("Elevator 3");
		static JButton button319 = new JButton("Elevator 3");
		static JButton button318 = new JButton("Elevator 3");
		static JButton button317 = new JButton("Elevator 3");
		static JButton button316 = new JButton("Elevator 3");
		static JButton button315 = new JButton("Elevator 3");
		static JButton button314 = new JButton("Elevator 3");
		static JButton button313 = new JButton("Elevator 3");
		static JButton button312 = new JButton("Elevator 3");
		static JButton button311 = new JButton("Elevator 3");
		static JButton button310 = new JButton("Elevator 3");
		static JButton button309 = new JButton("Elevator 3");
		static JButton button308 = new JButton("Elevator 3");
		static JButton button307 = new JButton("Elevator 3");
		static JButton button306 = new JButton("Elevator 3");
		static JButton button305 = new JButton("Elevator 3");
		static JButton button304 = new JButton("Elevator 3");
		static JButton button303 = new JButton("Elevator 3");
		static JButton button302 = new JButton("Elevator 3");
		static JButton button301 = new JButton("Elevator 3");
		
		static JButton button422 = new JButton("Elevator 4");
		static JButton button421 = new JButton("Elevator 4");
		static JButton button420 = new JButton("Elevator 4");
		static JButton button419 = new JButton("Elevator 4");
		static JButton button418 = new JButton("Elevator 4");
		static JButton button417 = new JButton("Elevator 4");
		static JButton button416 = new JButton("Elevator 4");
		static JButton button415 = new JButton("Elevator 4");
		static JButton button414 = new JButton("Elevator 4");
		static JButton button413 = new JButton("Elevator 4");
		static JButton button412 = new JButton("Elevator 4");
		static JButton button411 = new JButton("Elevator 4");
		static JButton button410 = new JButton("Elevator 4");
		static JButton button409 = new JButton("Elevator 4");
		static JButton button408 = new JButton("Elevator 4");
		static JButton button407 = new JButton("Elevator 4");
		static JButton button406 = new JButton("Elevator 4");
		static JButton button405 = new JButton("Elevator 4");
		static JButton button404 = new JButton("Elevator 4");
		static JButton button403 = new JButton("Elevator 4");
		static JButton button402 = new JButton("Elevator 4");
		static JButton button401 = new JButton("Elevator 4");
		*/

public Elevator_model(int floor, int port, int idNum)
{
	setCurrentFloor(floor);
	id = idNum;
   try {
      // Construct a datagram socket and bind it to any available 
      // port on the local host machine. This socket will be used to
      // send UDP Datagram packets.
      sendSocket = new DatagramSocket();

      // Construct a datagram socket and bind it to port 5000 
      // on the local host machine. This socket will be used to
      // receive UDP Datagram packets.
      receiveSocket = new DatagramSocket(port);
      
      // to test socket timeout (2 seconds)
      //receiveSocket.setSoTimeout(2000);
   } catch (SocketException se) {
      se.printStackTrace();
      System.exit(1);
   } 
}
public void receiveAndReply()			//this receives the message and sends a reply after motor has run
{
   // Construct a DatagramPacket for receiving packets up 
   // to 100 bytes long (the length of the byte array).

   byte data[] = new byte[30];
   receivePacket = new DatagramPacket(data, data.length);
   //System.out.println("Server: Waiting for Packet.\n");

   // Block until a datagram packet is received from receiveSocket.
   try {        
      System.out.println("Waiting..."); // so we know we're waiting
      receiveSocket.receive(receivePacket);
   } catch (IOException e) {
      System.out.print("IO Exception: likely:");
      System.out.println("Receive Socket Timed Out.\n" + e);
      e.printStackTrace();
      System.exit(1);
   }
   
// Process the received datagram.
   
   int destOrArrival = data[2];
   setNextFloor(data[4]);
   servingID = data[3];
   //int UpOrDown = data[6];
   //int ID = data[8];
   byte[] arr = Arrays.copyOfRange(data, 10, 18);
   String received = new String(arr,0,arr.length);
   String str = "Idle";
   if(destOrArrival == 0) {
	   System.out.println( "invalid destination packet");
   }
   else if(destOrArrival == 1){
		   if(data[6] == 1) {
			   str = "going up to " + getNextFloor();
			   direction = 1;
		   }
		   else if(data[6] == 0){
			   str = "going down to "+ getNextFloor();
			   direction = 0;
		   }
		   //if test error occurs
		   if(data[9] != 0) {
			   executeTestError(data[9]);
			   System.out.println("Restarted");
			   System.out.println( "@ "+ received + " The Elevator is "+ str);
			   runMotor(getCurrentFloor(),getNextFloor());
			   setCurrentFloor(getNextFloor());
			   System.out.println( "@ "+ generateDate() + " The Elevator is now on floor " + getCurrentFloor());
			   // Slow things down (wait 10 ms)
			   
			   try {
			       Thread.sleep(10);
			   } catch (InterruptedException e ) {
			       e.printStackTrace();
			       System.exit(1);
			   }
			   sendPacket = new DatagramPacket(data, data.length,receivePacket.getAddress(), 2300);
			    sendPacket2 = new DatagramPacket(data, data.length,receivePacket.getAddress(), 2350);
				 try {
			         sendSocket.send(sendPacket);
			         sendSocket.send(sendPacket2);
			      } catch (IOException e) {
			         e.printStackTrace();
			         System.exit(1);
			      }
			
			      System.out.println("Server: packet sent");
		   }
		   //continue normally
		   else {
		   System.out.println( "@ "+ received + " The Elevator is "+ str);
		   runMotor(getCurrentFloor(),getNextFloor());
		   setCurrentFloor(getNextFloor());
		   System.out.println( "@ "+ generateDate() + " The Elevator is now on floor " + getCurrentFloor());
		   // Slow things down (wait 10 ms)
		   
		   try {
		       Thread.sleep(10);
		   } catch (InterruptedException e ) {
		       e.printStackTrace();
		       System.exit(1);
		   }
		    sendPacket = new DatagramPacket(data, data.length,receivePacket.getAddress(), 2300);
		    sendPacket2 = new DatagramPacket(data, data.length,receivePacket.getAddress(), 2350);
			 try {
		         sendSocket.send(sendPacket);
		         sendSocket.send(sendPacket2);
		      } catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		      }
		
		      System.out.println("Server: packet sent");
		   }
      // We're finished, so close the sockets.
   		}
}
//handling of error cases iter 3
private void executeTestError(byte b) {
	 switch(b) {
	 case 1: 
		 System.out.println("Doors are stuck open. Elevator will restart.");
		
		 if(button101.isEnabled() == false) {
			 button101.setBackground(Color.YELLOW);
		 }
		 
		 if(button102.isEnabled() == false) {
			 button102.setBackground(Color.YELLOW);
		 }
		 
		 if(button103.isEnabled() == false) {
			 button103.setBackground(Color.YELLOW);
		 }
		 
		 if(button104.isEnabled() == false) {
			 button104.setBackground(Color.YELLOW);
		 }
		 
		 if(button105.isEnabled() == false) {
			 button105.setBackground(Color.YELLOW);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(Color.YELLOW);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(Color.YELLOW);
		 }
		 
		 if(button107.isEnabled() == false) {
			 button107.setBackground(Color.YELLOW);
		 }
		 
		 if(button108.isEnabled() == false) {
			 button108.setBackground(Color.YELLOW);
		 }
		 
		 if(button109.isEnabled() == false) {
			 button109.setBackground(Color.YELLOW);
		 }
		 
		 if(button110.isEnabled() == false) {
			 button110.setBackground(Color.YELLOW);
		 }
		 
		 if(button111.isEnabled() == false) {
			 button111.setBackground(Color.YELLOW);
		 }
		 
		 if(button112.isEnabled() == false) {
			 button112.setBackground(Color.YELLOW);
		 }
		 
		 if(button113.isEnabled() == false) {
			 button113.setBackground(Color.YELLOW);
		 }
		 
		 if(button114.isEnabled() == false) {
			 button114.setBackground(Color.YELLOW);
		 }
		 
		 if(button115.isEnabled() == false) {
			 button115.setBackground(Color.YELLOW);
		 }
		 
		 if(button116.isEnabled() == false) {
			 button116.setBackground(Color.YELLOW);
		 }
		 
		 if(button117.isEnabled() == false) {
			 button117.setBackground(Color.YELLOW);
		 }
		 
		 if(button118.isEnabled() == false) {
			 button118.setBackground(Color.YELLOW);
		 }
		 
		 if(button119.isEnabled() == false) {
			 button119.setBackground(Color.YELLOW);
		 }
		 
		 if(button120.isEnabled() == false) {
			 button120.setBackground(Color.YELLOW);
		 }
		 
		 if(button121.isEnabled() == false) {
			 button121.setBackground(Color.YELLOW);
		 }
		 
		 if(button122.isEnabled() == false) {
			 button122.setBackground(Color.YELLOW);
		 }
		 
		 handleFault(b);
		 
		 if(button101.isEnabled() == false) {
			 button101.setBackground(null);
		 }
		 
		 if(button102.isEnabled() == false) {
			 button102.setBackground(null);
		 }
		 
		 if(button103.isEnabled() == false) {
			 button103.setBackground(null);
		 }
		 
		 if(button104.isEnabled() == false) {
			 button104.setBackground(null);
		 }
		 
		 if(button105.isEnabled() == false) {
			 button105.setBackground(null);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(null);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(null);
		 }
		 
		 if(button107.isEnabled() == false) {
			 button107.setBackground(null);
		 }
		 
		 if(button108.isEnabled() == false) {
			 button108.setBackground(null);
		 }
		 
		 if(button109.isEnabled() == false) {
			 button109.setBackground(null);
		 }
		 
		 if(button110.isEnabled() == false) {
			 button110.setBackground(null);
		 }
		 
		 if(button111.isEnabled() == false) {
			 button111.setBackground(null);
		 }
		 
		 if(button112.isEnabled() == false) {
			 button112.setBackground(null);
		 }
		 
		 if(button113.isEnabled() == false) {
			 button113.setBackground(null);
		 }
		 
		 if(button114.isEnabled() == false) {
			 button114.setBackground(null);
		 }
		 
		 if(button115.isEnabled() == false) {
			 button115.setBackground(null);
		 }
		 
		 if(button116.isEnabled() == false) {
			 button116.setBackground(null);
		 }
		 
		 if(button117.isEnabled() == false) {
			 button117.setBackground(null);
		 }
		 
		 if(button118.isEnabled() == false) {
			 button118.setBackground(null);
		 }
		 
		 if(button119.isEnabled() == false) {
			 button119.setBackground(null);
		 }
		 
		 if(button120.isEnabled() == false) {
			 button120.setBackground(null);
		 }
		 
		 if(button121.isEnabled() == false) {
			 button121.setBackground(null);
		 }
		 
		 if(button122.isEnabled() == false) {
			 button122.setBackground(null);
		 }
		 break;
	 case 2: 
		 System.out.println("Doors are stuck closed. Elevator will restart.");
		 if(button101.isEnabled() == false) {
			 button101.setBackground(Color.YELLOW);
		 }
		 
		 if(button102.isEnabled() == false) {
			 button102.setBackground(Color.YELLOW);
		 }
		 
		 if(button103.isEnabled() == false) {
			 button103.setBackground(Color.YELLOW);
		 }
		 
		 if(button104.isEnabled() == false) {
			 button104.setBackground(Color.YELLOW);
		 }
		 
		 if(button105.isEnabled() == false) {
			 button105.setBackground(Color.YELLOW);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(Color.YELLOW);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(Color.YELLOW);
		 }
		 
		 if(button107.isEnabled() == false) {
			 button107.setBackground(Color.YELLOW);
		 }
		 
		 if(button108.isEnabled() == false) {
			 button108.setBackground(Color.YELLOW);
		 }
		 
		 if(button109.isEnabled() == false) {
			 button109.setBackground(Color.YELLOW);
		 }
		 
		 if(button110.isEnabled() == false) {
			 button110.setBackground(Color.YELLOW);
		 }
		 
		 if(button111.isEnabled() == false) {
			 button111.setBackground(Color.YELLOW);
		 }
		 
		 if(button112.isEnabled() == false) {
			 button112.setBackground(Color.YELLOW);
		 }
		 
		 if(button113.isEnabled() == false) {
			 button113.setBackground(Color.YELLOW);
		 }
		 
		 if(button114.isEnabled() == false) {
			 button114.setBackground(Color.YELLOW);
		 }
		 
		 if(button115.isEnabled() == false) {
			 button115.setBackground(Color.YELLOW);
		 }
		 
		 if(button116.isEnabled() == false) {
			 button116.setBackground(Color.YELLOW);
		 }
		 
		 if(button117.isEnabled() == false) {
			 button117.setBackground(Color.YELLOW);
		 }
		 
		 if(button118.isEnabled() == false) {
			 button118.setBackground(Color.YELLOW);
		 }
		 
		 if(button119.isEnabled() == false) {
			 button119.setBackground(Color.YELLOW);
		 }
		 
		 if(button120.isEnabled() == false) {
			 button120.setBackground(Color.YELLOW);
		 }
		 
		 if(button121.isEnabled() == false) {
			 button121.setBackground(Color.YELLOW);
		 }
		 
		 if(button122.isEnabled() == false) {
			 button122.setBackground(Color.YELLOW);
		 }
		 
		 handleFault(b);
		 
		 if(button101.isEnabled() == false) {
			 button101.setBackground(null);
		 }
		 
		 if(button102.isEnabled() == false) {
			 button102.setBackground(null);
		 }
		 
		 if(button103.isEnabled() == false) {
			 button103.setBackground(null);
		 }
		 
		 if(button104.isEnabled() == false) {
			 button104.setBackground(null);
		 }
		 
		 if(button105.isEnabled() == false) {
			 button105.setBackground(null);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(null);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(null);
		 }
		 
		 if(button107.isEnabled() == false) {
			 button107.setBackground(null);
		 }
		 
		 if(button108.isEnabled() == false) {
			 button108.setBackground(null);
		 }
		 
		 if(button109.isEnabled() == false) {
			 button109.setBackground(null);
		 }
		 
		 if(button110.isEnabled() == false) {
			 button110.setBackground(null);
		 }
		 
		 if(button111.isEnabled() == false) {
			 button111.setBackground(null);
		 }
		 
		 if(button112.isEnabled() == false) {
			 button112.setBackground(null);
		 }
		 
		 if(button113.isEnabled() == false) {
			 button113.setBackground(null);
		 }
		 
		 if(button114.isEnabled() == false) {
			 button114.setBackground(null);
		 }
		 
		 if(button115.isEnabled() == false) {
			 button115.setBackground(null);
		 }
		 
		 if(button116.isEnabled() == false) {
			 button116.setBackground(null);
		 }
		 
		 if(button117.isEnabled() == false) {
			 button117.setBackground(null);
		 }
		 
		 if(button118.isEnabled() == false) {
			 button118.setBackground(null);
		 }
		 
		 if(button119.isEnabled() == false) {
			 button119.setBackground(null);
		 }
		 
		 if(button120.isEnabled() == false) {
			 button120.setBackground(null);
		 }
		 
		 if(button121.isEnabled() == false) {
			 button121.setBackground(null);
		 }
		 
		 if(button122.isEnabled() == false) {
			 button122.setBackground(null);
		 }
		 break;
	 case 3: 
		 System.out.println("Fatal elevator flaw. Requests will be rescheduled and elevator will be shut down.");
		 
		 if(button101.isEnabled() == false) {
			 button101.setBackground(Color.RED);
		 }
		 
		 if(button102.isEnabled() == false) {
			 button102.setBackground(Color.RED);
		 }
		 
		 if(button103.isEnabled() == false) {
			 button103.setBackground(Color.RED);
		 }
		 
		 if(button104.isEnabled() == false) {
			 button104.setBackground(Color.RED);
		 }
		 
		 if(button105.isEnabled() == false) {
			 button105.setBackground(Color.RED);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(Color.RED);
		 }
		 
		 if(button106.isEnabled() == false) {
			 button106.setBackground(Color.RED);
		 }
		 
		 if(button107.isEnabled() == false) {
			 button107.setBackground(Color.RED);
		 }
		 
		 if(button108.isEnabled() == false) {
			 button108.setBackground(Color.RED);
		 }
		 
		 if(button109.isEnabled() == false) {
			 button109.setBackground(Color.RED);
		 }
		 
		 if(button110.isEnabled() == false) {
			 button110.setBackground(Color.RED);
		 }
		 
		 if(button111.isEnabled() == false) {
			 button111.setBackground(Color.RED);
		 }
		 
		 if(button112.isEnabled() == false) {
			 button112.setBackground(Color.RED);
		 }
		 
		 if(button113.isEnabled() == false) {
			 button113.setBackground(Color.RED);
		 }
		 
		 if(button114.isEnabled() == false) {
			 button114.setBackground(Color.RED);
		 }
		 
		 if(button115.isEnabled() == false) {
			 button115.setBackground(Color.RED);
		 }
		 
		 if(button116.isEnabled() == false) {
			 button116.setBackground(Color.RED);
		 }
		 
		 if(button117.isEnabled() == false) {
			 button117.setBackground(Color.RED);
		 }
		 
		 if(button118.isEnabled() == false) {
			 button118.setBackground(Color.RED);
		 }
		 
		 if(button119.isEnabled() == false) {
			 button119.setBackground(Color.RED);
		 }
		 
		 if(button120.isEnabled() == false) {
			 button120.setBackground(Color.RED);
		 }
		 
		 if(button121.isEnabled() == false) {
			 button121.setBackground(Color.RED);
		 }
		 
		 if(button122.isEnabled() == false) {
			 button122.setBackground(Color.RED);
		 }
		 handleFault(b);
		 //System.exit(0);
	 }
}

//wait for scheduler to catch error and reply iter 3
private void handleFault(byte b) {
	byte data[] = new byte[30];
	   receivePacket = new DatagramPacket(data, data.length);
	   // Block until a datagram packet is received from receiveSocket.
	   try {        
	      System.out.println("Waiting..."); // so we know we're waiting
	      receiveSocket.receive(receivePacket);
	   } catch (IOException e) {
	      System.out.print("IO Exception: likely:");
	      System.out.println("Receive Socket Timed Out.\n" + e);
	      e.printStackTrace();
	      System.exit(1);
	   }
	   
	    // Process the received datagram. and set fault flag
	   data[3] = (byte) servingID;
	   data[4] = (byte) currentFloor;
	   data[5] = (byte) nextFloor;
	   data[9] = b;
	   //send response
	   sendPacket = new DatagramPacket(data, data.length,receivePacket.getAddress(), 2300);
		
		 try {
	         sendSocket.send(sendPacket);
	      } catch (IOException e) {
	         e.printStackTrace();
	         System.exit(1);
	      }
	
	      System.out.println("fault sent to scheduler sucessfully");
	      if (b == 3) {
			sendSocket.close();
			receiveSocket.close();
		}
}

public void runMotor(int start,int end) {		//this method is used to run the motor to the destination floor with the appropriate wait times
	int time = 2000;
	if(start == end) {
		System.out.println("Opening doors...");
		try {
		       Thread.sleep(time);
		   } catch (InterruptedException e ) {
		       e.printStackTrace();
		       System.exit(1);
		   }
	}
	
	
	else {
		System.out.println("closing doors...");
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("motor running...");
		if (direction == 1) {
			for (int i = start; i < end; i++) {
				time = 1000;
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				sendUpdate(i+1);
				System.out.println("Elevator traveling at floor " + (i+1));
			} 
		}
		else if (direction == 0) {
			for (int i = start; i > end; i--) {
				time = 1000;
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
				sendUpdate(i-1);
				System.out.println("Elevator traveling at floor " + (i-1));
			} 
		}
		System.out.println("Opening doors...");
		time = 2000;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		} 
	}
}
//update scheduler by sending a packet with important info on position
private static void sendUpdate(int currentFloor) {
	byte a[] = new byte[] { (byte) 5, (byte) 0, (byte) 1, (byte) 0, (byte) currentFloor, (byte) nextFloor, (byte)direction,
			(byte) 0, (byte) id, (byte) 0 };
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	String time = generateDate();
	try {
		outputStream.write(a);
		outputStream.write(time.getBytes());
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	byte msg[] = outputStream.toByteArray();
	
	if(currentFloor == 1) {
		button101.setEnabled(false);
		button102.setEnabled(true);
	}
	if(currentFloor == 2) {
		button101.setEnabled(true);
		button102.setEnabled(false);
		button103.setEnabled(true);
	}
	if(currentFloor == 3) {
		button102.setEnabled(true);
		button103.setEnabled(false);
		button104.setEnabled(true);
	}
	if(currentFloor == 4) {
		button103.setEnabled(true);
		button104.setEnabled(false);
		button105.setEnabled(true);
	}
	if(currentFloor == 5) {
		button104.setEnabled(true);
		button105.setEnabled(false);
		button106.setEnabled(true);
	}
	if(currentFloor == 6) {
		button105.setEnabled(true);
		button106.setEnabled(false);
		button107.setEnabled(true);
	}
	if(currentFloor == 7) {
		button106.setEnabled(true);
		button107.setEnabled(false);
		button108.setEnabled(true);
	}
	if(currentFloor == 8) {
		button107.setEnabled(true);
		button108.setEnabled(false);
		button109.setEnabled(true);
	}
	if(currentFloor == 9) {
		button108.setEnabled(true);
		button109.setEnabled(false);
		button110.setEnabled(true);
	}
	if(currentFloor == 10) {
		button109.setEnabled(true);
		button110.setEnabled(false);
		button111.setEnabled(true);
	}
	if(currentFloor == 11) {
		button110.setEnabled(true);
		button111.setEnabled(false);
		button112.setEnabled(true);
	}
	if(currentFloor == 12) {
		button111.setEnabled(true);
		button112.setEnabled(false);
		button113.setEnabled(true);
	}
	if(currentFloor == 13) {
		button112.setEnabled(true);
		button113.setEnabled(false);
		button114.setEnabled(true);
	}
	if(currentFloor == 14) {
		button113.setEnabled(true);
		button114.setEnabled(false);
		button115.setEnabled(true);
	}
	if(currentFloor == 15) {
		button114.setEnabled(true);
		button115.setEnabled(false);
		button116.setEnabled(true);
	}
	if(currentFloor == 16) {
		button115.setEnabled(true);
		button116.setEnabled(false);
		button117.setEnabled(true);
	}
	if(currentFloor == 17) {
		button116.setEnabled(true);
		button117.setEnabled(false);
		button118.setEnabled(true);
	}
	if(currentFloor == 18) {
		button117.setEnabled(true);
		button118.setEnabled(false);
		button119.setEnabled(true);
	}
	if(currentFloor == 19) {
		button118.setEnabled(true);
		button119.setEnabled(false);
		button120.setEnabled(true);
	}
	if(currentFloor == 20) {
		button119.setEnabled(true);
		button120.setEnabled(false);
		button121.setEnabled(true);
	}
	if(currentFloor == 21) {
		button120.setEnabled(true);
		button121.setEnabled(false);
		button122.setEnabled(true);
	}
	if(currentFloor == 22) {
		button121.setEnabled(true);
		button122.setEnabled(false);
	}	
	
	sendPacket = new DatagramPacket(msg, msg.length, receivePacket.getAddress(), 2300);
	try {
		sendSocket.send(sendPacket);
	} catch (IOException e) {
		e.printStackTrace();
		System.exit(1);
	}
}
//utility methods
public static int getCurrentFloor() {
	return currentFloor;
}

public static void setCurrentFloor(int floor) {
	currentFloor = floor;
}

public static int getNextFloor() {
	return nextFloor;
}

public static void setNextFloor(int floor) {
	nextFloor = floor;
}

public static String generateDate() { 
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	return sdf.format(cal.getTime());
}

public static void initialize() throws IOException{
	frame = new JFrame();
	frame.setBounds(100, 100, 200, 720);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(new MigLayout("", "[513px]", "[33px][32px][33px][33px][32px][33px][33px][32px][33px][33px][32px][33px][33px][33px][32px][33px][33px][32px][33px][33px][32px][33px][33px]"));
	frame.getContentPane().setLayout(new MigLayout("", "[1px]", "[1px]"));
	
	JLabel lblFloor_22 = new JLabel("Floor 22");
	frame.getContentPane().add(lblFloor_22, "flowx,cell 0 0,grow");
	
	JLabel lblFloor_21 = new JLabel("Floor 21");
	frame.getContentPane().add(lblFloor_21, "flowx,cell 0 1,grow");
	
	JLabel lblFloor_20 = new JLabel("Floor 20");
	frame.getContentPane().add(lblFloor_20, "flowx,cell 0 2,grow");
	
	JLabel lblFloor_19 = new JLabel("Floor 19");
	frame.getContentPane().add(lblFloor_19, "flowx,cell 0 3,grow");
	
	JLabel lblFloor_18 = new JLabel("Floor 18");
	frame.getContentPane().add(lblFloor_18, "flowx,cell 0 4,grow");
	
	JLabel lblFloor_17 = new JLabel("Floor 17");
	frame.getContentPane().add(lblFloor_17, "flowx,cell 0 5,grow");
	
	JLabel lblFloor_16 = new JLabel("Floor 16");
	frame.getContentPane().add(lblFloor_16, "flowx,cell 0 6,grow");
	
	JLabel lblFloor_15 = new JLabel("Floor 15");
	frame.getContentPane().add(lblFloor_15, "flowx,cell 0 7,grow");
	
	JLabel lblFloor_14 = new JLabel("Floor 14");
	frame.getContentPane().add(lblFloor_14, "flowx,cell 0 8,grow");
	
	JLabel lblFloor_13 = new JLabel("Floor 13");
	frame.getContentPane().add(lblFloor_13, "flowx,cell 0 9,grow");
	
	JLabel lblFloor_12 = new JLabel("Floor 12");
	frame.getContentPane().add(lblFloor_12, "flowx,cell 0 10,grow");
	
	JLabel lblFloor_11 = new JLabel("Floor 11");
	frame.getContentPane().add(lblFloor_11, "flowx,cell 0 11,grow");
	
	JLabel lblFloor_10 = new JLabel("Floor 10");
	frame.getContentPane().add(lblFloor_10, "flowx,cell 0 12,grow");
	
	JLabel lblFloor_9 = new JLabel("Floor 9");
	frame.getContentPane().add(lblFloor_9, "flowx,cell 0 13,grow");
	
	JLabel lblFloor_8 = new JLabel("Floor 8");
	frame.getContentPane().add(lblFloor_8, "flowx,cell 0 14,grow");
	
	JLabel lblFloor_7 = new JLabel("Floor 7");
	frame.getContentPane().add(lblFloor_7, "flowx,cell 0 15,grow");
	
	JLabel lblFloor_6 = new JLabel("Floor 6");
	frame.getContentPane().add(lblFloor_6, "flowx,cell 0 16,grow");
	
	JLabel lblFloor_5 = new JLabel("Floor 5");
	frame.getContentPane().add(lblFloor_5, "flowx,cell 0 17,grow");
	
	JLabel lblFloor_4 = new JLabel("Floor 4");
	frame.getContentPane().add(lblFloor_4, "flowx,cell 0 18,grow");
	
	JLabel lblFloor_3 = new JLabel("Floor 3");
	frame.getContentPane().add(lblFloor_3, "flowx,cell 0 19,grow");
	
	JLabel lblFloor_2 = new JLabel("Floor 2");
	frame.getContentPane().add(lblFloor_2, "flowx,cell 0 20,grow");
	
	JLabel lblFloor_1 = new JLabel("Floor 1");
	frame.getContentPane().add(lblFloor_1, "flowx,cell 0 21,grow");
	
	Canvas canvas = new Canvas();
	frame.getContentPane().add(canvas, "cell 0 22,grow");
	
	button122 = new JButton("Elevator " + id);
	button121 = new JButton("Elevator " + id);
	button120 = new JButton("Elevator " + id);
	button119 = new JButton("Elevator " + id);
	button118 = new JButton("Elevator " + id);
	button117 = new JButton("Elevator " + id);
	button116 = new JButton("Elevator " + id);
	button115 = new JButton("Elevator " + id);
	button114 = new JButton("Elevator " + id);
	button113 = new JButton("Elevator " + id);
	button112 = new JButton("Elevator " + id);
	button111 = new JButton("Elevator " + id);
	button110 = new JButton("Elevator " + id);
	button109 = new JButton("Elevator " + id);
	button108 = new JButton("Elevator " + id);
	button107 = new JButton("Elevator " + id);
	button106 = new JButton("Elevator " + id);
	button105 = new JButton("Elevator " + id);
    button104 = new JButton("Elevator " + id);
	button103 = new JButton("Elevator " + id);
	button102 = new JButton("Elevator " + id);
	button101 = new JButton("Elevator " + id);
	
	frame.getContentPane().add(button122, "cell 0 0");
	frame.getContentPane().add(button121, "cell 0 1");
	frame.getContentPane().add(button120, "cell 0 2");
	frame.getContentPane().add(button119, "cell 0 3");
	frame.getContentPane().add(button118, "cell 0 4");
	frame.getContentPane().add(button117, "cell 0 5");
	frame.getContentPane().add(button116, "cell 0 6");
	frame.getContentPane().add(button115, "cell 0 7");
	frame.getContentPane().add(button114, "cell 0 8");
	frame.getContentPane().add(button113, "cell 0 9");
	frame.getContentPane().add(button112, "cell 0 10");
	frame.getContentPane().add(button111, "cell 0 11");
	frame.getContentPane().add(button110, "cell 0 12");
	frame.getContentPane().add(button109, "cell 0 13");
	frame.getContentPane().add(button108, "cell 0 14");
	frame.getContentPane().add(button107, "cell 0 15");
	frame.getContentPane().add(button106, "cell 0 16");
	frame.getContentPane().add(button105, "cell 0 17");
	frame.getContentPane().add(button104, "cell 0 18");
	frame.getContentPane().add(button103, "cell 0 19");
	frame.getContentPane().add(button102, "cell 0 20");
	frame.getContentPane().add(button101, "cell 0 21");
	
	button101.setEnabled(false);
			

	/*frame.getContentPane().add(button222, "cell 0 0");
	frame.getContentPane().add(button221, "cell 0 1");
	frame.getContentPane().add(button220, "cell 0 2");
	frame.getContentPane().add(button219, "cell 0 3");
	frame.getContentPane().add(button218, "cell 0 4");
	frame.getContentPane().add(button217, "cell 0 5");
	frame.getContentPane().add(button216, "cell 0 6");
	frame.getContentPane().add(button215, "cell 0 7");
	frame.getContentPane().add(button214, "cell 0 8");
	frame.getContentPane().add(button213, "cell 0 9");
	frame.getContentPane().add(button212, "cell 0 10");
	frame.getContentPane().add(button211, "cell 0 11");
	frame.getContentPane().add(button210, "cell 0 12");
	frame.getContentPane().add(button209, "cell 0 13");
	frame.getContentPane().add(button208, "cell 0 14");
	frame.getContentPane().add(button207, "cell 0 15");
	frame.getContentPane().add(button206, "cell 0 16");
	frame.getContentPane().add(button205, "cell 0 17");
	frame.getContentPane().add(button204, "cell 0 18");
	frame.getContentPane().add(button203, "cell 0 19");
	frame.getContentPane().add(button202, "cell 0 20");
	frame.getContentPane().add(button201, "cell 0 21");
	
	//button201.setEnabled(false);
	
	
	frame.getContentPane().add(button322, "cell 0 0");
	frame.getContentPane().add(button321, "cell 0 1");
	frame.getContentPane().add(button320, "cell 0 2");
	frame.getContentPane().add(button319, "cell 0 3");
	frame.getContentPane().add(button318, "cell 0 4");
	frame.getContentPane().add(button317, "cell 0 5");
	frame.getContentPane().add(button316, "cell 0 6");
	frame.getContentPane().add(button315, "cell 0 7");
	frame.getContentPane().add(button314, "cell 0 8");
	frame.getContentPane().add(button313, "cell 0 9");
	frame.getContentPane().add(button312, "cell 0 10");
	frame.getContentPane().add(button311, "cell 0 11");
	frame.getContentPane().add(button310, "cell 0 12");
	frame.getContentPane().add(button309, "cell 0 13");
	frame.getContentPane().add(button308, "cell 0 14");
	frame.getContentPane().add(button307, "cell 0 15");
	frame.getContentPane().add(button306, "cell 0 16");
	frame.getContentPane().add(button305, "cell 0 17");
	frame.getContentPane().add(button304, "cell 0 18");
	frame.getContentPane().add(button303, "cell 0 19");
	frame.getContentPane().add(button302, "cell 0 20");
	frame.getContentPane().add(button301, "cell 0 21");
	
	//button301.setEnabled(false);
	
	frame.getContentPane().add(button422, "cell 0 0");
	frame.getContentPane().add(button421, "cell 0 1");
	frame.getContentPane().add(button420, "cell 0 2");
	frame.getContentPane().add(button419, "cell 0 3");
	frame.getContentPane().add(button418, "cell 0 4");
	frame.getContentPane().add(button417, "cell 0 5");
	frame.getContentPane().add(button416, "cell 0 6");
	frame.getContentPane().add(button415, "cell 0 7");
	frame.getContentPane().add(button414, "cell 0 8");
	frame.getContentPane().add(button413, "cell 0 9");
	frame.getContentPane().add(button412, "cell 0 10");
	frame.getContentPane().add(button411, "cell 0 11");
	frame.getContentPane().add(button410, "cell 0 12");
	frame.getContentPane().add(button409, "cell 0 13");
	frame.getContentPane().add(button408, "cell 0 14");
	frame.getContentPane().add(button407, "cell 0 15");
	frame.getContentPane().add(button406, "cell 0 16");
	frame.getContentPane().add(button405, "cell 0 17");
	frame.getContentPane().add(button404, "cell 0 18");
	frame.getContentPane().add(button403, "cell 0 19");
	frame.getContentPane().add(button402, "cell 0 20");
	frame.getContentPane().add(button401, "cell 0 21");
	
	//button401.setEnabled(false);*/
}

public static void main( String args[] )
{
   Elevator_model e = new Elevator_model(1, Integer.parseInt(args[0]) , Integer.parseInt(args[1]));
   int i = 1;
	try {
		initialize();
		frame.setVisible(true);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
   while(i <= 10000) {
	   e.receiveAndReply();
	   i++;
   }
   sendSocket.close();
   receiveSocket.close();
}
}
