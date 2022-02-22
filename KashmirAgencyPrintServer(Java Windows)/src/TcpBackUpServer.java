import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpBackUpServer {
	
	public static final int SERVERPORT = 4444;
    ServerSocket serverSocket = null;
 // callback used to notify new messages received
    
    public TcpBackUpServer() {
    	//System.out.println("Ready to Receive");
    	runServer();
    }
    
	public void runServer() {
	
	    try {
	        serverSocket = new ServerSocket(SERVERPORT);
	    } catch (IOException ex) {
	        //System.out.println("Can't setup server on this port number. ");
	    }
	
	    Socket client = null;
	    InputStream dataIn = null;
	    OutputStream fileOut = null;
	
	    try {
	        client = serverSocket.accept();
	    } catch (IOException ex) {
	        //System.out.println("Can't accept client connection. ");
	    }
	
	    try {
	        dataIn = client.getInputStream();
	    } catch (IOException ex) {
	        //System.out.println("Can't get socket input stream. ");
	    }
	
	    try {
	        fileOut = new FileOutputStream("AgencyDB.sqlite");
	    } catch (FileNotFoundException ex) {
	        //System.out.println("File not found. ");
	    }
	
	    byte[] bytes = new byte[1024];
	    int count;
	    try {
			while ((count = dataIn.read(bytes)) > 0) {
			    fileOut.write(bytes, 0, count);
			}
			//System.out.println("File Received");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//System.out.println("Input Data Stream Cannot be read or file output stream can not be written");
		}
	
	    try {
			dataIn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("Error closing Data Input Stream.....");
		}
	    try {
			fileOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//System.out.println("Error closing file output stream....");
		}
	    try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			//System.out.println("Error closing Client Socket.....");
		}
	    try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("Error closing Server Socket.....");
		}
	}
}