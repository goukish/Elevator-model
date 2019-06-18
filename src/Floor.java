
import java.io.*;
import java.net.*;
//import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.io.IOException;
import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.io.IOException;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;



public class Floor { // this class creates a new "floor"
	DatagramPacket sendPacket, receivePacket1;
	static DatagramSocket sendSocket;
	static DatagramSocket receiveSocket1;
	static Logger log;
	
	
	
	public Floor(Logger l1) {
		log = l1;
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();

			// Construct a datagram socket and bind it to port 5000
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets.
			receiveSocket1 = new DatagramSocket(2350);
			

			// to test socket timeout (2 seconds)
			// receiveSocket1.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void receiveAndFoward() // outputs statements about inputs generated by out readmethod, fowards packet to
									// scheduler
	{
		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).

		byte data[] = new byte[30];
		receivePacket1 = new DatagramPacket(data, data.length);
		

		// Block until a datagram packet is received from receiveSocket1.
		try {
			 // so we know we're waiting
			receiveSocket1.receive(receivePacket1);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

// Process the received datagram.
		

		
		//shut down case
		if(data[1] == 1) {
			log.closeReader();
			System.exit(1);
		}
		int destOrArrival = data[2];
		int floorNo = data[4];
		// int UpOrDown = data[6];
		int flag = data[7];
		int ID = data[8];
		byte[] arr = Arrays.copyOfRange(data, 10, 17);
		String received = new String(arr, 0, arr.length);
		String str;
		if (destOrArrival == 1 && flag == 1) {

			if (data[6] == 1) {
				str = "up";

			} else {
				str = "down";

			}
			System.out.println(
					"@ " + received + " On floor " + floorNo + ", the " + str + " arrow is off.");
			System.out.println("Elevator " + ID + " has arrived.");
			
			
			
		}

		else if (destOrArrival == 0){
			if (data[6] == 1) {
				str = "up";

			} else {
				str = "down";

			}
			System.out.println(
					"@ " + received + " On floor " + floorNo + ",  the " + str + " arrow is lit.");

			
			log.writeFloorButtonPress(data[4]);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
			sendPacket = new DatagramPacket(data, data.length, receivePacket1.getAddress(), 2300);

			

			try {
				sendSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}

			

			// We're finished, so close the sockets.
		}
	}

	public static void main(String args[]) {
		Logger l1 = new Logger(args[0]);
		System.out.println("Program starting...");
		Floor c = new Floor(l1);
		
		//new visualGraphics().main(args);

		int i = 1;
		while (i <= 10000) {
			c.receiveAndFoward();
			i++;
		
		}
		sendSocket.close();
		receiveSocket1.close();
		System.out.println("Program Terminated");
	}
}


