package networking;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Connect {
    private final String _server = "icarus2.engr.uconn.edu";
    private final int _port = 3499;
    private Socket _socket = null;
    private PrintWriter _out = null;
    private BufferedReader _in = null;

    
    /**
     * Opens a socket and sets the reader and writers.
     * @return Returns the socket.
     */
    public Socket openSocket(){
        try {
                        _socket = new Socket(_server, _port);
                        _out = new PrintWriter(_socket.getOutputStream(), true);
                        _in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                } catch (UnknownHostException e) {
                        System.out.println("Uknown Host: "+_server);
                        System.exit(1);
                } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                }
        return _socket;
    }
    
    public String readMessage() throws IOException {
                String readMessage = _in.readLine();
                return readMessage;
    }
    
    public String readAndEcho() throws IOException {
                String readMessage = _in.readLine();
                System.out.println("read: "+readMessage);
                return readMessage;
    }

    public void writeMessage(String message) throws IOException{
                _out.print(message+"\r\n");  
                _out.flush();
    }
 
    public void writeMessageAndEcho(String message) throws IOException {
                _out.print(message+"\r\n");  
                _out.flush();
                System.out.println("sent: "+ message);
    }
    
    
    
    /**
     * Closes the connection to the server.
     */
    public void close(){
        try {
                        _socket.close();
                } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                }
    }
    
}
