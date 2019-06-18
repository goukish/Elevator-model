import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {

	BufferedWriter bw = null;
	FileWriter fw = null;

	public Logger(String FILENAME) {
		try {
			fw = new FileWriter(FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);
	}
    //following functions create string that will be written to log file using the byte array that is in the communication protocol
	public void writeFloorButtonPress(int floor) {
		long time = System.nanoTime();
		String realTime = generateTime();
		try {
			String content = ("Button " + floor + " pressed at " + realTime + " raw: " + time + "\n");
			bw.write(content);
			//System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeElevatorButtonPress(int floor) {
		long time = System.nanoTime();
		String realTime = generateTime();
		try {
			String content = ("Button " + floor + " pressed at " + realTime + " raw: " + time + "\n");
			bw.write(content);
			//System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void writeOutArrival(int floor,int request) {
		long time = System.nanoTime();
		String realTime = generateTime();
		try {
			String content = ("Request " + request + ": Arrival on floor " + floor + " at " + realTime + " raw: " + time + "\n");
			bw.write(content);
			//System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void closeReader() {
		System.out.println("Done");
		try {
			if (bw != null)
				bw.close();
			if (fw != null)
				fw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static String generateTime() { 				//we created this method to generate a time stamp
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSS");
		return sdf.format(cal.getTime());
	}
	
	/**public static void main( String args[] ) {
		Logger f1 = new Logger(args[0]);
		f1.writeRequest();
		f1.writeRequest();
		f1.closeReader();
		
	}
	*/

}