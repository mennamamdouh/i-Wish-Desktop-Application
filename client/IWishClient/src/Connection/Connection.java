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

/**
 *
 * @author DELL
 */
public class Connection {
    private static Connection connection;
    private Socket server;
    private DataInputStream input ;
    private DataInputStream notification ;
    private PrintStream output ;

    private Connection() throws IOException {
        server = new Socket("127.0.0.1",55555) ;
        input = new DataInputStream(server.getInputStream());
        output  = new PrintStream(server.getOutputStream());
        notification  = new DataInputStream(server.getInputStream());
    }
    public static Connection getInstance() throws IOException {
        if(connection == null)
            connection = new Connection();
        return connection;
    }
    public DataInputStream getInputStream() {
        return input;
    }
    
    public PrintStream getOutputStream() {
        return output;
    } 
    
}
