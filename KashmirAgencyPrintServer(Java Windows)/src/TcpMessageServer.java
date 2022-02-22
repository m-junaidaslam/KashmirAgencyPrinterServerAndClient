import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import javax.swing.JFrame;

/**
 * The class extends the Thread class so we can receive and send messages at the same time
 *
 * @author Muhammad Junaid Aslam
 *         Date: 25/09/16
 */
public class TcpMessageServer extends Thread {

    public static final int SERVERPORT = 4444;
    // while this is true the server will run
    private boolean running = false;
    // used to send messages
    private PrintWriter bufferSender;
    // callback used to notify new messages received
    private OnMessageReceived messageListener;
    private ServerSocket serverSocket;
    private Socket client;

    /**
     * Constructor of the class
     *
     * @param messageListener listens for the messages
     */
    public TcpMessageServer(OnMessageReceived messageListener) {
        this.messageListener = messageListener;
    }

    public static void main(String[] args) {

        //opens the window where the messages will be received and sent
        MainScreen frame = new MainScreen();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Close the server
     */
    public void close() {
        running = false;
        
        if (bufferSender != null) {
            bufferSender.flush();
            bufferSender.close();
            bufferSender = null;
        }

        try {
        	if(client != null)
        		client.close();
        	if(serverSocket != null)
        		serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("S: Done.");
        serverSocket = null;
        client = null;

    }

    /**
     * Method to send the messages from server to client
     *
     * @param message the message sent by the server
     */
    public void sendMessage(String message) {
        if (bufferSender != null && !bufferSender.checkError()) {
            bufferSender.println(message);
            bufferSender.flush();
        }
    }

    public boolean hasCommand(String message) {
        if (message != null) {
            if (message.contains(Constants.CLOSED_CONNECTION)) {
                messageListener.messageReceived(message.replaceAll(Constants.CLOSED_CONNECTION, "") + Constants.LOGIN_NAME + " disconnected from room.");
                // close the server connection if we have this command and rebuild a new one
                close();
                runServer();
                return true;
            } else if (message.contains(Constants.LOGIN_NAME)) {
                messageListener.messageReceived(message.replaceAll(Constants.LOGIN_NAME, "") + Constants.LOGIN_NAME + " connected.");
                return true;
            }
        }

        return false;
    }

    /**
     * Builds a new server connection
     */
    public void runServer() {
        running = true;

        try {
            //System.out.println("S: Connecting...");

            //create a server socket. A server socket waits for requests to come in over the network.
            serverSocket = new ServerSocket(SERVERPORT);

            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            try {
            	client = serverSocket.accept();
            } catch(SocketException e) {
            	
            }
            //System.out.println("S: Receiving...");

            try {
            	Charset charset = java.nio.charset.StandardCharsets.UTF_8;
                //sends the message to the client
            	if(client != null)
            		bufferSender = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), charset)), true);

                //read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), charset));

                //in this while we wait to receive messages from client (it's an infinite loop)
                //this while it's like a listener for messages
                while (running) {

                    String message = null;
                    try {
                        message = in.readLine();
                    } catch (IOException e) {
                        //System.out.println("Error reading message: " + e.getMessage());
                    }

                    if (hasCommand(message)) {
                        continue;
                    }

                    if (message != null && messageListener != null) {
                        //call the method messageReceived from ServerBoard class
                        messageListener.messageReceived(message);
                    }
                }

            } catch (Exception e) {
                //System.out.println("S: Error");
                e.printStackTrace();
            }

        } catch (Exception e) {
            //System.out.println("S: Error");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        runServer();
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the ServerBoard
    //class at on startServer button click
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

}