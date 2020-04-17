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
        Socket socket = null;
        
        while(true) {
            socket = serversocket.accept();
            final ConnectionHandler handler = new ConnectionHandler(socket);
            new Thread((Runnable) handler).start();
            handlers.add(handler);
        }
    }
    
    public boolean isTimerRunning(){
        
    }
    
    public long getTimerMillis() {
        
    }
    
    public static void main(String[] args) {
        new Server();
    }
    
    
}
