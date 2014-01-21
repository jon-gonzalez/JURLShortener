/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jglez.lab.jurlshortener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jon
 */
public class Shortener {
    private CouchDBConnector connection;
    private URL shortURL;     //This is the short url
    private URL longURL;      //This is the long url
    private final String DOMAIN = "http://jgg.es/";  //Domain
    private final char alphaNumCharArray[] = new char[62]; //Alphanumeric Array with 62 chars
    private final Random RAMDOM = new Random();  //Used to generate random integers
    private final int KEYLENGTH = 7;  //Random url length
    
    public Shortener(){
        setLong(null);
        setShort(null);
        setConnection(null);
    }
    
    public Shortener(URL url){
        setLong(null);
        setShort(url);
        setConnection(null);
    }
    
    private CouchDBConnector getConnection() {
        return connection;
    }   
    
    private void setConnection(CouchDBConnector connection) {
        this.connection = connection;
    }
   
    private URL getShort() {
        return shortURL;
    }

    private void setShort(URL _short) {
        this.shortURL = _short;
    }

    private URL getLong() {
        return longURL;
    }

    private void setLong(URL _long) {
        this.longURL = _long;
    }
    
    public boolean doItShort(String urlLong){
        try {
            URL url = new URL(urlLong);
            setLong(url);
            Object response;
            response = getConnection().select("_show_sl/long-short", urlLong, 4);
            if(response == null) {
                System.out.println("Nothing");
                System.out.println("Shorting....");
                trimmer();
            } else {
                System.out.println("I found your url shortened.");
                setShort(new URL(response.toString()));
            }
        } catch (MalformedURLException e) {
            System.out.println("The introudced URL was wrong. => " + e);
            return false;
        }
        return true;
    }
   
    private boolean trimmer() {
        try {
            for(int i = 0; i < 62; i++)
            {
                int j = 0;
                if(i < 10) {
                    j = i + 48;
                } else if(i > 9 && i <= 35) {
                    j = i + 55;
                } else {
                    j = i + 61;
                }
                this.alphaNumCharArray[i] = (char)j;
            }
            
            System.out.println(this.alphaNumCharArray);
            URL url = new URL(this.DOMAIN + generateKey());
            setShort(url);
            System.out.println("Trimming OK: " + getShort());
            insertDB();
            System.out.println("SAVING......");
            System.out.println("SAVED - OK");
            return true;
        } catch (MalformedURLException ex) {
            Logger.getLogger(Shortener.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private String generateKey() {
        String key = "";
        boolean flag = true;
        while(flag) {
            key = "";
            for(int i = 0; i <= KEYLENGTH; i++) {
                key += alphaNumCharArray[RAMDOM.nextInt(62)];
            }
            if(!containsKey(key)) {
                flag = false;
            }
        }
        return key;
    }
    
    private boolean containsKey(String key) {
        //return true if exist, false if not
        System.out.println(DOMAIN + key);
        if (getConnection().select("_show_sl/short-long", DOMAIN + key, 4) == null) {
            System.out.println("I can't find the URL requested.");
            return false;
        } else {
            return true;
        }
    }
    
    private boolean insertDB() {
        Map <String, Object> map = new HashMap<>();
        map.put("short", getShort());
        map.put("long", getLong());
        getConnection().save(map);
        return true;
    }

    public void printStatus() {
        System.out.println("------ URL's ------");
        System.out.println("Short: " + getShort());
        System.out.println("Long: " + getLong());
    }
    
    public boolean connect() {
        if(checkConnection() == false) {
            setConnection(new CouchDBConnector());
            if(getConnection().getConnectionCouchDB() == null){
                return false;
            }
            return true;
        } else {
            return false;
        }   
    }
    
    public boolean disconnect() {
        if(checkConnection()) {
            getConnection().shutdown();
            setConnection(null);
            return true;
        } else {
            return false;
        }
    }
    
    
    public boolean checkConnection() {
        if(getConnection() != null){
            System.out.println("Connected");
            return true;
        } else {
            System.out.println("Disconnected");
            return false;
        }
    }
}
