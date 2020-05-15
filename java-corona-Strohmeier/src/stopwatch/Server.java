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
        timeOffset = 0;
        Socket socket;
        
        while(true) {
            socket = serversocket.accept();
            final ConnectionHandler handler = new ConnectionHandler(socket);
            new Thread((Runnable) handler).start();
            handlers.add(handler);
        }
    }
    
    public boolean isTimerRunning(){
        return startMillis > 0;
    }
    
    public long getTimerMillis() {
        return (System.currentTimeMillis() - startMillis) + timeOffset;
        
    }
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(8080);
    }

    private static class Request {

        boolean master;
        boolean start;
        boolean stop;
        boolean clear;
        boolean end;
        

        public boolean isMaster() {
            return master;
        }

        public void setMaster(boolean master) {
            this.master = master;
        }

        public boolean isStart() {
            return start;
        }

        public void setStart(boolean start) {
            this.start = start;
        }

        public boolean isStop() {
            return stop;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public boolean isClear() {
            return clear;
        }

        public void setClear(boolean clear) {
            this.clear = clear;
        }

        public boolean isEnd() {
            return end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }

        
    }

    private static class Response {

        boolean master;
        Long count;
        Boolean running;
        Long time;
        
        public Response() {
        }

        private Response(boolean master, int i, boolean timerRunning, long timerMillis) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public boolean isMaster() {
            return master;
        }

        public void setMaster(boolean master) {
            this.master = master;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public Boolean getRunning() {
            return running;
        }

        public void setRunning(Boolean running) {
            this.running = running;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
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
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String req = reader.readLine(); //Zeichen werden im Attribut req gespeichert
                
                Gson gson = new Gson();
                gson.toJson(req); // die einkommenden Zeilen werden in ein Objekt gespeichert
                Request r = gson.fromJson(req, Request.class); //neues Request Projekt, welches die Zeichen beinhaltet

                final Gson gsonrsp = new Gson();
                Response rsp = gsonrsp.fromJson(req, Response.class);//response objekt erstellt und unten zurückgesendet
		count++;
		rsp.setCount(count);
                
                if(r.isMaster()) {
                    boolean setMasterTrue = true;
                    for(ConnectionHandler h : handlers) {
                        if(!h.equals(this) && h.isMaster() == true) {
                            setMasterTrue = false;
                            break;
                        }
                    }
                    master = setMasterTrue;
                }
                
                if(r.isMaster()) {
                    if(r.isStart()) {
                        startMillis = System.currentTimeMillis();
                    }

                    if(r.isStop()) {
                        startMillis = -1;
                    } else {
                        timeOffset = System.currentTimeMillis() - startMillis;
                    }

                    if(r.isClear()) {
                        timeOffset = 0;
                        
                    }

                    if(r.isEnd()) {
                        serversocket.close();
                        socket.close();
                        handlers.remove(this);
                    }        
                }

                
                
                //Response
                Response resp = new Response(master, 5, isTimerRunning(), getTimerMillis());
                String respString = gson.toJson(resp);
                OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                
            } catch(Exception ex) {
                ex.printStackTrace();
            } 
        }
        
        
    }
}
