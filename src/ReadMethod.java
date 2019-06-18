import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class ReadMethod{
	
	 static String filename ;
	 DatagramSocket sendSocket;
	 DatagramPacket sendPacket;
	 int i = 0;
	 long timeBinSec = 0;
	 long timeAinSec ;
	 //int portTEMPADDRESS = 6000; //CHANGE THIS LATER

	
	public ReadMethod(String fileName) throws UnknownHostException{
		filename = fileName;
		try {
			sendSocket = new DatagramSocket();
		}
		catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);;
		}
	}
	//parses string to create byte array
	public void readFile(String path) {
		
		try(BufferedReader br = new BufferedReader(new FileReader(path))){
			String line;
			while((line = br.readLine()) != null) {
				System.out.println(line);
				//end of file
				if (line.contains("end")) {
					try
					{
					    Thread.sleep(140000);
					}
					catch(InterruptedException ex)
					{
					    Thread.currentThread().interrupt();
					}
					byte a[] = new byte[] {(byte)6,(byte)1};
					//send shut down msg
					sending(a,2350);
					sending(a,2300);
					System.exit(1);
				}
				String numberOnly= line.replaceAll("[^0-9]", " ");
				    
				String lineNum = numberOnly.trim().replaceAll(" +", " ");
			    System.out.println(lineNum);
				    
				String timeHourString = lineNum.split(" ")[0];
				String timeMinString  = lineNum.split(" ")[1];
				String timeSecString  = lineNum.split(" ")[2];
				String timeMilliString  = lineNum.split(" ")[3];
				String firstFloorString  = lineNum.split(" ")[4];
				String secondFloorString  = lineNum.split(" ")[5];
				String errorString  = lineNum.split(" ")[6];
				
				long timeHour = Integer.parseInt(timeHourString);
				long timeHourtoMillisec = (long) (timeHour * (3.6 * 10 * 10 *10 *10 *10 *10));
				
				long timeMin = Integer.parseInt(timeMinString);
				long timeMintoMillisec = timeMin * 60000;
				
				long timeSec = Integer.parseInt(timeSecString);
				long timeSectoMillisec = timeSec * 1000;
				
				long timeMilli = Integer.parseInt(timeMilliString);
				
				timeAinSec = (timeHourtoMillisec + timeMintoMillisec + timeSectoMillisec +timeMilli);
				
				
				System.out.println(timeAinSec);
				long delay = 0;
				
				if(i == 0) {
					delay = 0;
					i++;
				}
				else {
					delay = Math.abs(timeAinSec - timeBinSec);
					
				}
				
				timeBinSec = timeAinSec;

				int firstFloorNo = Integer.parseInt(firstFloorString);
				int secondFloorNo = Integer.parseInt(secondFloorString);
				int errorNo = Integer.parseInt(errorString);
				
				
				System.out.println("delay: " + delay + "\n");
				
				try
				{
				    Thread.sleep(delay);
				}
				catch(InterruptedException ex)
				{
				    Thread.currentThread().interrupt();
				}
				
				line.toLowerCase();
				byte[] bytesToSend;
				
				if(line.contains("Up")) {
					byte a[] = new byte[] {(byte)6,(byte)0,(byte)0,(byte)0,(byte)firstFloorNo,(byte)secondFloorNo,(byte)1,
						(byte)0,(byte)0,(byte)errorNo,};
					bytesToSend = a;
				}
				
				else {
					byte a[] = new byte[] {(byte)6,(byte)0,(byte)0,(byte)0,(byte)firstFloorNo,(byte)secondFloorNo,(byte)0,
							(byte)0,(byte)0,(byte)errorNo,};
					bytesToSend = a;
				}
				   
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				String time = generateDate();
				try {
					outputStream.write(bytesToSend);
					outputStream.write(time.getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				byte msg[] = outputStream.toByteArray();
				sending(msg,2350);
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//sends byte array over udp
	public void sending(byte[] msg,int port) {
		try {

			sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
		 System.out.println("Sending packet:" + msg);
         System.out.println("To host: " + sendPacket.getAddress());
         System.out.println("Destination host port: " + sendPacket.getPort());
         int len = sendPacket.getLength();
         System.out.println("Length: " + len);
         System.out.print("Containing: ");
         //System.out.println(new String(sendPacket.getData(), 0, len)); // or could print "s"    
         
         try {
             System.out.println("Client sending packet to port " + sendPacket.getPort());
             sendSocket.send(sendPacket);
         } catch (IOException e) {
        	 e.printStackTrace();
        	 System.exit(1);
         }
         catch (NullPointerException ee) {
        	 ee.printStackTrace();
        	 throw ee;
         }

         System.out.println("Client: Packet sent.\n");
         
        
	}
	public static String generateDate() { 
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		return sdf.format(cal.getTime());
	}
	
	public static void main(String args[]) throws Exception {
		ReadMethod readMethod = new ReadMethod(args[0]);
		readMethod.readFile(filename);
		
	}

}
