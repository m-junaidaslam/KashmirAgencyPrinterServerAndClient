package aslam.junaid.kashmiragencyprintclient;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Description
 *
 * @author Catalin Prata
 *         Date: 2/12/13
 */
public class TcpClient implements Serializable {

    public String SERVER_IP = ""; //your computer IP address
    public static final int SERVER_PORT = 4444;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    //used to send object from client
    private ObjectOutputStream mObjectOut;
    private boolean receivingFile;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener, String serverIp) {
        mMessageListener = listener;
        this.SERVER_IP = serverIp;
        this.receivingFile = false;
    }

    public void setReceivingObject(boolean receivingFile) {
        this.receivingFile = receivingFile;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message) {
        if (mBufferOut != null && !mBufferOut.checkError()) {
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    public void sendDataBase() {

        Log.d("CMDCheck", "Came to ACK File send");
        File sd = Environment.getExternalStorageDirectory();

        File data = Environment.getDataDirectory();

        String currentDBPath = "/data/" + "aslam.junaid.aslamkashmiragency" + "/databases/" + "AgencyDB";
        File currentDB = new File(data, currentDBPath);

        //String currentFilePath = "Download/gapps.zip";

        //File currentFile = new File(sd, currentFilePath);

        try {
            FileInputStream fis = new FileInputStream(currentDB);
            //FileInputStream fis = new FileInputStream(currentFile);
            //Log.d("File", "Current File Path: " + currentFilePath);
            if(fis.available() > 0) {
                Log.d("File", "File is Available");
            } else {
                Log.d("File", "File is NOT Available");
            }
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);

            mObjectOut.writeObject(buffer);
            fis.close();
            mObjectOut.flush();

            Log.d("Buffer", buffer.toString() + " : " + buffer.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        // send mesage that we are closing the connection
        sendMessage(Constants.CLOSED_CONNECTION);

        mRun = false;

        if (mBufferOut != null) {
            mBufferOut.flush();
            mBufferOut.close();
        }

        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVER_PORT);

            try {
                Charset charset = StandardCharsets.UTF_8;
                //sends the message to the server
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset)), true);
                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));

                //sends object to the server
                mObjectOut = new ObjectOutputStream(socket.getOutputStream());
                //receives object from server
                //ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                // send login name
                sendMessage(Constants.LOGIN_NAME);

                //in this while the client listens for the messages sent by the server
                while (mRun) {

                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }
                    /*if(receivingFile) {
                        mMessageListener.objectReceived(objectInputStream);
                    }*/

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
        //public void objectReceived(ObjectInputStream objectInputStream);
    }
}