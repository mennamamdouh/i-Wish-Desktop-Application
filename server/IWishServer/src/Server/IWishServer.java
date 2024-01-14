/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Server.DTO.User;
import com.google.gson.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
   private DataInputStream input ;
   private PrintStream output ;
    /**
     *   
     * Create new client , add him to list of connected clients 
     * and initialize communication channels with him (input and output streams)
     * 
     * @param socket The client's socket that requests a connection to the server.
     */
    public Client(Socket socket){
     
        try {
            input = new DataInputStream(socket.getInputStream());
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
                String msg = input.readLine();
                if(msg == null){
                    terminate();
                    break;
                }
                String result = handler.process(msg);
                output.println(result);
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
}

