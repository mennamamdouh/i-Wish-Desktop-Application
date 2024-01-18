/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.DataInputStream;
import java.io.IOException;
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
    private Socket server;
    private DataInputStream input ;
    private DataInputStream notification ;
    private PrintStream output ;

    private MyConnection() throws IOException {
        server = new Socket("127.0.0.1",55555) ;
        input = new DataInputStream(server.getInputStream());
        output  = new PrintStream(server.getOutputStream());
        notification  = new DataInputStream(server.getInputStream());
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
        }       
    }
    public static boolean getStatus(){
        return connection != null;
    }
    public DataInputStream getInputStream() {
        return input;
    }
    
    public PrintStream getOutputStream() {
        return output;
    } 

    public DataInputStream getNotification() {
        return notification;
    }
    
}
