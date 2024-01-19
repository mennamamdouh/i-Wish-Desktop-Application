/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class MyConnection {
    private static MyConnection connection;
    private static ReceiverHandler handler;
    private Socket server;
    private BufferedReader input ;
    private BufferedReader notification ;
    private PrintStream output ;

    private MyConnection() throws IOException {
        server = new Socket("127.0.0.1",55555) ;
        InputStreamReader inputStreamReader = new InputStreamReader(server.getInputStream());
        input = new BufferedReader(inputStreamReader);
        output  = new PrintStream(server.getOutputStream());
        notification  = new BufferedReader(inputStreamReader);
        handler = new ReceiverHandler();
    }
    public static MyConnection getInstance() throws IOException {
        if(connection == null)
            connection = new MyConnection();
        return connection;
    }
    public void closeConnection() throws IOException{
        if(connection != null){
            server.close();
            input.close();
            notification.close();
            output.close();
            connection = null;
        }       
    }
    public static boolean getStatus(){
        return connection != null;
    }
    public BufferedReader getInputStream() {
        return input;
    }
    
    public PrintStream getOutputStream() {
        return output;
    } 

    public BufferedReader getNotification() {
        return notification;
    }

    public static ReceiverHandler getHandler() {
        return handler;
    }
    
}
