/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jglez.lab.jurlshortener;

import java.util.Map;
import org.lightcouch.CouchDbClient;
import org.lightcouch.View;

/**
 *
 * @author Jon
 */
public class CouchDBConnector {
    private CouchDbClient connectionCouchDB; //Connection

    public CouchDBConnector() {
        try{
            this.connectionCouchDB = new CouchDbClient();
        } catch (org.lightcouch.CouchDbException e){
            System.out.println("El Servidor no responde.");
            this.connectionCouchDB = null;
        }
    }
    
    public CouchDbClient getConnectionCouchDB() {
        return connectionCouchDB;
    }
    
    public void setConnectionCouchDB(CouchDbClient connectionCouchDB) {
        this.connectionCouchDB = connectionCouchDB;
    }
    
    public void shutdown() {
        getConnectionCouchDB().shutdown();
    }
    
    public Object select(String view, String key, int type) {
        Object response = null;
        View resultView = getConnectionCouchDB().view(view).key(key);
            try {
                switch (type) {
                    case 0: 
                        //boolean
                         response = resultView.queryForBoolean();
                    case 1:
                        //integer
                        response =  resultView.queryForInt();
                    case 2:
                        //long
                        response =  resultView.queryForLong();
                    case 3:
                        //stream
                        response =  resultView.queryForStream();
                    case 4: 
                        //string
                        response =  resultView.queryForString();
                    default:
                        return response;
                }
            }catch (Exception ex) {
                System.out.println("I can't found your URL.");
                return response;
            }
    }
    
    public boolean save(Map map) {
        if (select("_show_sl/long-short", map.get("long").toString(), 4) == null){
            getConnectionCouchDB().save(map);
            return true;
        } else {
            System.out.println("Has intentado colarte!!");
            return false;
        } 
    }
    
}
