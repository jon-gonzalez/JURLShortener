package com.jglez.lab.jurlshortener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            String url = "http://jongonzalezguerra.com/blog/7";
            Shortener s = new Shortener();
            s.checkConnection();
            if(s.connect()) {
                System.out.print("CONNECTED");
                s.doItShort(url); 
                s.disconnect();
                
                s.checkConnection();
                s.printStatus();
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
