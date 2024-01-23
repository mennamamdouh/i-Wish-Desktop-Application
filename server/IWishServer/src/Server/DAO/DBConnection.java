/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import com.mysql.jdbc.Driver;

/**
 *
 * @author DELL
 */
public abstract class DBConnection {
    
    private volatile static Connection dbconnection;
    
    private DBConnection(){
        
    }
    /**
     * @return Database connection , following Singleton design pattern to have only one database connection in the server
     * @throws SQLException 
     */
    public static Connection getConnection() throws SQLException{
        if(dbconnection == null)
            dbconnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/iwishdb", "root", "admin");
        return dbconnection;
    }
    
}
