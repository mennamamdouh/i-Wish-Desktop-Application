/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import Server.DTO.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class DataRetrieval {
    
    public static boolean login(User user) throws SQLException{
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("select * from users where email = ? and password = ? ");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());
         
        result = statement.executeQuery();
        if(result.next())
        {
            user.setUserid(result.getInt(1));
            user.setFullname(result.getString(3));
            user.setUserphoto(result.getBlob(4));
            statement.close();
            return true;
        }
        else{
            statement.close();
            return false;
        }
    }
    
    public static ArrayList<User> getFriends(){
        ArrayList<User> arr = new ArrayList<User>();
        return arr ;
    }
    
    public static ArrayList<WishList> getWishList(User user) throws SQLException{
        ArrayList<WishList> arr = new ArrayList<WishList>();
        return arr ;
        
    }
    
    public static ArrayList<Item> getItems() throws SQLException{
        ArrayList<Item> items = new ArrayList<Item>();
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("select * from items" , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
        result = statement.executeQuery();
        if(result.next())
        {
            result.previous();
            while(result.next()){
                items.add(new Item(result.getInt(1),result.getString(2),result.getString(3),result.getDouble(4)));
            }
            return items;
        }
        statement.close();
        return items;
        
    }
    
    public static ArrayList<Contribution> getContribution(){
        ArrayList<Contribution> arr = new ArrayList<Contribution>();
        return arr;
    }
    
    public static ArrayList<Notification> getNotifications(){
        ArrayList<Notification> arr = new ArrayList<Notification>();
        return arr;
    }
    
}
