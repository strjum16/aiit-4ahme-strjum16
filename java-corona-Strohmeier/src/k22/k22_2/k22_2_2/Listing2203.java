/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k22.k22_2.k22_2_2;

/**
 *
 * @author JulianFew
 */
public class Listing2203 extends Thread
{
    int cnt = 0;
    
    public void run(){
        
        while(true) {
            if (isInterrupted()) {
                break;
            }
            printLine(++cnt);
        }
    }
    
    private void printLine(int cnt){
        //Zeile ausgeben
        System.out.println(cnt + ": ");
        for(int i = 0; i < 30; i++){
            System.out.println(1 == cnt % 30 ? "* " : ". ");
        }
        System.out.println();
        //100 ms warten
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            interrupt();
        }
    }
    
    public static void main(String[] args) {
        Listing2203 th = new Listing2203();
        {
            //Thread starten
            th.start();
            //2 Sek warten
            try{
                Thread.sleep(2000);
            }catch (InterruptedException e) {
            }
            //Thread unterbrechen
            th.interrupt();
        }
    }
    
}
