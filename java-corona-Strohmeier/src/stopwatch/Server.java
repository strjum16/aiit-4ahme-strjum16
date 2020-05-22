package stopwatch;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JulianFew
 */
public class Server {
    private ServerSocket serverSocket;
    private final List <ConnectionHandler> handlers = new ArrayList<>();
    private long timeOffset;
    private long startMillis;
    
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        timeOffset = 0;
        startMillis = 0;
        
        while(true) {
            Socket clientSocket = serverSocket.accept();
            for(ConnectionHandler h: handlers){
                if(h.isClosed()){
                    handlers.remove(h);
                }
            }
            if (handlers.size() < 3){
                final ConnectionHandler handler = new ConnectionHandler(clientSocket);
                new Thread((Runnable) handler).start();
                handlers.add(handler);
            } else {
                clientSocket.close();
            }
        }
    }
    
    public boolean isTimeRunning(){
        return startMillis > 0;
    }
    
    public long getTimeMillis() {
        if(startMillis == 0){
            return timeOffset;
        } else{
            return (System.currentTimeMillis() - startMillis) + timeOffset;
        }
        
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8080);
    }

    private class Request {

        boolean master;
        boolean start;
        boolean stop;
        boolean clear;
        boolean end;
        

        public boolean isMaster() {
            return master;
        }

        
        public boolean isStart() {
            return start;
        }

        

        public boolean isStop() {
            return stop;
        }

        

        public boolean isClear() {
            return clear;
        }

        

        public boolean isEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "Request{" + "master=" + master + ", start=" + start + ", stop=" + stop + ", clear=" + clear + ", end=" + end + '}';
        }
        

        
    }

    private class Response {

        public boolean master;
        public Long count;
        public Boolean running;
        public Long time;
        
        public Response() {
        }

        private Response(boolean master, int i, boolean timerRunning, long timerMillis) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public boolean isMaster() {
            return master;
        }

        

        public Long getCount() {
            return count;
        }

        

        public Boolean getRunning() {
            return running;
        }

        

        public Long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return "Response{" + "master=" + master + ", count=" + count + ", running=" + running + ", time=" + time + '}';
        }
        
        
        
    }
    
    

private class ConnectionHandler extends Thread {
    
    private Socket socket;
    private boolean master;
    
    public ConnectionHandler(Socket socket){
        this.socket = socket;
    }
    
    public boolean isClosed() throws IOException{
        return socket.isClosed();
    }
    
    public boolean isMaster(){
        return master;
    }

    @Override
    public void run() {
        int count = 0;
        try{
            while(true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                String line = reader.readLine(); //Zeichen werden im Attribut req gespeichert
                
                if(line == null){
                        socket.close();
                        return;
                }
                count++;
                
                final Gson gson = new Gson();
                gson.toJson(line); // die einkommenden Zeilen werden in ein Objekt gespeichert
                System.out.println(line);
                final Request req = gson.fromJson(line, Request.class); //neues Request Projekt, welches die Zeichen beinhaltet
                System.out.println(req);
                
                
                if(req.isMaster()) {
                    master = true;
                    for(ConnectionHandler c : handlers) {
                        if(c != this && c.isMaster() == true) {
                            master = false;
                            break;
                        }
                    }
                }
                
                if(master) {
                    if(req.isStart()) {
                        startMillis = System.currentTimeMillis();
                    }
                    
                    if(req.isClear()) {
                        if(isTimeRunning()){
                                startMillis = System.currentTimeMillis();
                            }
                            timeOffset = 0; 
                    }

                    if(req.isStop()) {
                        timeOffset = getTimeMillis();
                            startMillis = 0;
                    }
                    

                    if(req.isEnd()) {
                        handlers.remove(this);
                        //Server schließen-----------------------------
                        serverSocket.close();
                        socket.close();
                        return;
                    }        
                }

                
                //Response
                    final Response resp = new Response(master, count, isTimeRunning(), getTimeMillis());
                    System.out.println(resp);
                    final String respString = gson.toJson(resp);
                    System.out.println(respString);
                    writer.write(respString);
                    writer.flush();
                
            }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
} 
}
