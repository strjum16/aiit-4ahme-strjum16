/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stopwatch;

import java.awt.Window;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingWorker;
import stopwatch.gui.Client;

/**
 *
 * @author JulianFew
 */
public class ConnectionWorker extends SwingWorker<String, Integer> {
    private Socket socket;
   
    
    public ConnectionWorker(int port, String hostName){
        
    
    }
    
    
    

    @Override
    protected String doInBackground() throws Exception{
         System.out.println("Do in Background" + Thread.currentThread().getId());
         Thread.sleep(1000);
         
         publish(1);
       
         Thread.sleep(1000);
         
         publish(2);
         
         Thread.sleep(1000);
        return "OK";
    }
    
}
