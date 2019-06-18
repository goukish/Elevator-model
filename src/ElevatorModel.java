import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Canvas;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

/*
 * CHANGES:
 *
 * Formatted everything so it's more consistent
 *
 * Optimized imports so you're not importing unnecessary classes
 *
 * Added buttons array and for loops to shorten code
 *
 * Changed visibility of variables and methods to minimum requirement (mostly private)
 *
 * Tidied up exception handling so there's no duplicates within the same methods.
 * Any methods with duplicate try/catch blocks can instead "throw Exception" instead, this way the try/catch is only
 * done by the method calling the code that may cause the exception.
 *
 * Added some exception handling in main() for invalid arguments
 */

public class ElevatorModel {        //this class creates a new elevator
    private DatagramPacket sendPacket, receivePacket, sendPacket2; // Use of sendPacket2 may by unnecessary
    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;
    private int elevatorId, currentFloor, nextFloor, servingID;
    private int direction = 2;
    private final int MAX_FLOOR = 22;

    private JFrame frame;
    private JButton[] buttons = new JButton[MAX_FLOOR];

    public ElevatorModel(int floor, int port, int idNum) throws SocketException {
        setCurrentFloor(floor);
        elevatorId = idNum;

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
    }

    private void receiveAndReply() {            //this receives the message and sends a reply after motor has run
        // Construct a DatagramPacket for receiving packets up to 100 bytes long (the length of the byte array).

        byte[] data = new byte[30];
        receivePacket = new DatagramPacket(data, data.length);

        // Block until a datagram packet is received from receiveSocket.
        try {
            print("Waiting..."); // so we know we're waiting
            receiveSocket.receive(receivePacket);
            print("Received packet");
        } catch (IOException e) {
            print("IOException occurred. " + e.getMessage());
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
        String received = new String(arr, 0, arr.length);

        String str = "Idle";
        if (destOrArrival == 0) {
            System.out.println("invalid destination packet");
        } else if (destOrArrival == 1) {
            if (data[6] == 1) {
                str = "going up to " + getNextFloor();
                direction = 1;
            } else if (data[6] == 0) {
                str = "going down to " + getNextFloor();
                direction = 0;
            }
            try {
                // Test error
                if (data[9] != 0) {
                    executeTestError(data[9]);
                    System.out.println("Restarted");
                    processElevator(data, received, str);
                } else {
                    // Continue normally
                    processElevator(data, received, str);
                }
            } catch (InterruptedException e) {
                print("Interrupted while running motor.");
                e.printStackTrace();
                System.exit(1);
            }
            // We're finished, so close the sockets.
        }
    }

    private void processElevator(byte[] data, String received, String str) throws InterruptedException {
        print("@ " + received + " The Elevator is " + str);

        runMotor(getCurrentFloor(), getNextFloor());

        setCurrentFloor(getNextFloor());
        print("@ " + generateDate() + " The Elevator is now on floor " + getCurrentFloor());
        // Slow things down (wait 10 ms)

        Thread.sleep(10);

        sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), 2300);
        sendPacket2 = new DatagramPacket(data, data.length, receivePacket.getAddress(), 2350);
        try {
            sendSocket.send(sendPacket);
            sendSocket.send(sendPacket2);
        } catch (IOException e) {
            print("IOException occurred in processElevator(). " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        print("packet sent");
    }

    //handling of error cases iter 3
    private void executeTestError(byte b) {
        switch (b) {
            case 1:
                print("Doors are stuck open. Elevator will restart.");

                for (JButton button : buttons)
                    if (!button.isEnabled())
                        button.setBackground(Color.YELLOW);

                handleFault(b);

                for (JButton button : buttons)
                    if (!button.isEnabled())
                        button.setBackground(null);

                break;
            case 2:
                print("Doors are stuck closed. Elevator will restart.");

                for (JButton button : buttons)
                    if (!button.isEnabled())
                        button.setBackground(Color.YELLOW);

                handleFault(b);

                for (JButton button : buttons)
                    if (!button.isEnabled())
                        button.setBackground(null);
                break;
            case 3:
                print("Fatal elevator flaw. Requests will be rescheduled and elevator will be shut down.");

                for (JButton button : buttons)
                    if (!button.isEnabled())
                        button.setBackground(Color.RED);

                handleFault(b);
                //System.exit(0);
        }
    }

    //wait for scheduler to catch error and reply iter 3
    private void handleFault(byte b) {
        byte[] data = new byte[30];
        receivePacket = new DatagramPacket(data, data.length);
        // Block until a datagram packet is received from receiveSocket.
        try {
            print("Handling fault. Waiting..."); // so we know we're waiting
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            print("IOException occurred in handleFault(). " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Process the received datagram. and set fault flag
        data[3] = (byte) servingID;
        data[4] = (byte) currentFloor;
        data[5] = (byte) nextFloor;
        data[9] = b;

        // Send response
        try {
            sendPacket(data);
        } catch (IOException e) {
            print("IOException while sending packet in handleFault(): " + e.getMessage() + ".");
            print("quitting...");
            System.exit(1);
        }

        print("fault sent to scheduler successfully");
        if (b == 3) {
            sendSocket.close();
            receiveSocket.close();
        }
    }

    private void runMotor(int start, int end) throws InterruptedException {        //this method is used to run the motor to the destination floor with the appropriate wait times
        int time = 2000;
        if (start == end) {
            print("Opening doors...");
            Thread.sleep(time);
        } else {
            print("Closing doors...");
            Thread.sleep(time);
            print("Motor running...");

            if (direction == 1) {
                for (int i = start; i < end; i++) {
                    Thread.sleep(1000);
                    sendUpdate(i + 1);
                    print("traveling at floor " + (i + 1));
                }
            } else if (direction == 0) {
                for (int i = start; i > end; i--) {
                    Thread.sleep(1000);
                    sendUpdate(i - 1);
                    print("Travelling at floor " + (i - 1));
                }
            }
            System.out.println("E" + elevatorId + ": Opening doors...");
            Thread.sleep(2000);
        }
    }

    //update scheduler by sending a packet with important info on position
    private void sendUpdate(int currentFloor) {
        byte[] a = new byte[]{
                (byte) 5, (byte) 0, (byte) 1, (byte) 0, (byte) currentFloor, (byte) nextFloor, (byte) direction,
                (byte) 0, (byte) elevatorId, (byte) 0
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String time = generateDate();
        try {
            outputStream.write(a);
            outputStream.write(time.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        byte[] msg = outputStream.toByteArray();

        // Update the button that corresponds to the current floor
        int buttonFloor = buttons.length;
        for (JButton button : buttons) {
            // Set enabled status of all buttons. The button on the currentFloor should be disabled
            boolean enabled = (buttonFloor != currentFloor);
            button.setEnabled(enabled);
            buttonFloor--;
        }
        try {
            sendPacket(msg);
        } catch (IOException e) {
            print("IOException while sending packet in sendUpdate(): " + e.getMessage() + ".");
            print("quitting...");
            System.exit(1);
        }
    }

    private void sendPacket(byte[] msg) throws IOException {
        sendPacket = new DatagramPacket(msg, msg.length, receivePacket.getAddress(), 2300);
        sendSocket.send(sendPacket);
    }

    //utility methods
    private int getCurrentFloor() {
        return currentFloor;
    }

    private void setCurrentFloor(int floor) {
        currentFloor = floor;
    }

    private int getNextFloor() {
        return nextFloor;
    }

    private void setNextFloor(int floor) {
        nextFloor = floor;
    }

    private String generateDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        return sdf.format(cal.getTime());
    }
    
    private void print(String message) {
        System.out.println("E" + elevatorId + ": " + message);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 200, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[513px]", "[33px][32px][33px][33px][32px][33px]" +
                "[33px][32px][33px][33px][32px][33px][33px][33px][32px][33px][33px][32px][33px][33px][32px][33px][33px]"));
        frame.getContentPane().setLayout(new MigLayout("", "[1px]", "[1px]"));

        Canvas canvas = new Canvas();
        frame.getContentPane().add(canvas, "cell 0 " + MAX_FLOOR + ",grow");

        for (int i = 0; i < MAX_FLOOR; i++) {
            int floor = MAX_FLOOR - i;
            JLabel floorLabel = new JLabel("Floor " + floor);
            frame.getContentPane().add(floorLabel, "flowx,cell 0 " + i + ",grow");

            buttons[i] = new JButton("Elevator " + elevatorId);
            frame.getContentPane().add(buttons[i], "cell 0 " + i);
        }
        buttons[MAX_FLOOR -1].setEnabled(false);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough args to start elevator. Use (port, elevatorID).");
            System.exit(-1);
        }
        int port = 0, elevatorId = 0;
        try {
            port = Integer.parseInt(args[0]);
            elevatorId = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            System.out.println("Can't parse (port, elevatorID) from args " + Arrays.toString(args));
            System.exit(-1);
        }

        ElevatorModel instance = null;
        try {
            instance = new ElevatorModel(1, port, elevatorId);
        } catch (SocketException e) {
            System.out.println("Elevator " + elevatorId + " SocketException in constructor. " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        instance.initialize();
        instance.frame.setVisible(true);

        for (int i = 0; i < 10000; i++) {
            instance.receiveAndReply();
        }

        instance.sendSocket.close();
        instance.receiveSocket.close();
    }
}