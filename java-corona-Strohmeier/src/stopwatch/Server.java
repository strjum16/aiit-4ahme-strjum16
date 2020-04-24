package stopwatch;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JulianFew
 */
public class Server {
    private ServerSocket serversocket;
    private final List <ConnectionHandler> handlers = new ArrayList<>();
    private long timeOffset;
    private long startMillis;
    
    public void start(int port) throws IOException {
        serversocket = new ServerSocket(port);
        Socket socket;
        
        while(true) {
            socket = serversocket.accept();
            final ConnectionHandler handler = new ConnectionHandler(socket);
            new Thread((Runnable) handler).start();
            handlers.add(handler);
        }
    }
    
    public boolean isTimerRunning(){
        if (startMillis > 0){
            
            return true;
            
        } else { return false;}
    }
    
    public long getTimerMillis() {
        
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8080);
    }
    
    
    
}

class ConnectionHandler{
    
    ConnectionHandler(Socket socket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
