/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diaa
 */
public class IWishServer {
   
    static ServerSocket server ;
    static Vector<Client> clients = new Vector<Client>();
    private static boolean run = true ;
    
    /**
     * Initialize Server On Specific Port and Accepting Upcoming Clients
     */
    public IWishServer(){
        try {
         server = new ServerSocket(55555);
         while(run){
             Socket temp = server.accept();
             new Client(temp);       
         }
        } catch (IOException ex) {
        }
    }
    /**
     * Stop Server and end connection with connected clients
     */
    public static void kill(){
        try {
            run = false;
            server.close();
            for (Client client : clients) {
                client.terminate();
            }
        } catch (IOException ex) {
            Logger.getLogger(IWishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class Client extends Thread {
    
   private RequestHandler handler;
   private Socket client_socket;
   private BufferedReader input;
   private PrintStream output ;
   private Thread notify ;
    /**
     *   
     * Create new client , add him to list of connected clients 
     * and initialize communication channels with him (input and output streams)
     * 
     * @param socket The client's socket that requests a connection to the server.
     */
    public Client(Socket socket){
     
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintStream(socket.getOutputStream());
            client_socket = socket;
            handler = new RequestHandler();
            IWishServer.clients.add(this);
            
            this.start();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    /**
     * Listening for Client's Requests if the connection isn't closed from client side
     * and pass the upcoming request for RequestHandler Process function to handle it
     */
    public void run(){
        while(true){
            try {
                notifyUser();
                String msg = input.readLine();
            //    System.out.println(msg);
                if(msg == null){
                    terminate();
                    break;
                }
                String result = handler.process(msg);
             // System.out.println(result);
                output.println(result);
                output.flush();
            } catch (IOException | SQLException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
         }           
        }
            
    }
    /**
     * Close Client's connection by removing him from list of clients 
     * and close every opened stream
     */
    public void terminate(){
        try {
            this.handler.setLogin(false);
            IWishServer.clients.remove(this);
            input.close();
            output.close();
            client_socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    public void notifyUser(){
        if(notify == null & handler.isLogin()){
            
            notify = new Thread(new Runnable() {
            Gson gson = new Gson();
            JsonObject jobject = new JsonObject();
            @Override
            public void run() {
                while(handler.isLogin()){
                    try {
                        if(handler.isNotifyOn() & handler.hasNotifications()){
                            jobject = new JsonObject();
                            jobject.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.GET_NOTIFICATIONS));//GET_NOTIFICATIONS));
                            String notifications = handler.process(jobject.toString());
                            output.println(notifications);
                            output.flush();
                        }
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        notify.start();
      }
    }
}

