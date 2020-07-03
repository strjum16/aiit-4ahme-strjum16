/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stopwatch;

import com.google.gson.Gson;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingWorker;
import stopwatch.gui.Client;


/**
 *
 * @author JulianFew
 */
public class ConnectionWorker extends SwingWorker<Object, Integer> {
    
    private boolean tryToStart;
    private boolean tryToStop;
    private boolean tryToClear;
    private boolean tryToEnd;
    
    private int sliderState;
    private boolean cancel;
    private int sliderValue = 0;
    
    private Socket socket;
   
    
    public ConnectionWorker(int port, String hostName) throws IOException{
        socket = new Socket(hostName, port);
    }
    public void setTryToStart(boolean tryToStart) {
        this.tryToStart = tryToStart;
    }

    public void setTryToStop(boolean tryToStop) {
        this.tryToStop = tryToStop;
    }

    public void setTryToClear(boolean tryToClear) {
        this.tryToClear = tryToClear;
    }

    public void setTryToEnd(boolean tryToEnd) {
        this.tryToEnd = tryToEnd;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public void setSliderState(int sliderState) {
        this.sliderState = sliderState;
    }    
    
    

    @Override
    protected Object doInBackground() throws Exception{
        final Gson g = new Gson();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        while (true) {
            try {
                final Server s = new Server();
                final Request req = s.new Request();
                final String reqString = g.toJson(req);
                writer.write(reqString + "\n");
                writer.flush();

                final String respString = reader.readLine();
                final Response resp = g.fromJson(respString, Response.class);
                publish(resp);

                synchronized() {
                    int localSliderState = sliderState; // wird im event dispash thread gesetzt und hier konsumiert
                    Thread.sleep(1000 - localSliderState);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
}
