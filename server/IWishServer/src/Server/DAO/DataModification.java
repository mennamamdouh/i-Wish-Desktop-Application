/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import Server.DTO.Item;
import Server.DTO.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class DataModification {
    
    public static boolean addFriend(User user, User friend) throws SQLException{
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("INSERT INTO Friendship VALUES(?, ?, 'Pending')");
        statement.setInt(1, friend.getUserid());
        statement.setInt(2, user.getUserid());
        try{
            int result = statement.executeUpdate();
            if(result == 1){
                statement.close();
                System.out.println("Friend was added successfuly.");
                return true;
            }
            else{
                System.out.println("Friend wasn't added.");
                return false;
            }
        } catch(SQLException ex){
            System.out.println("You've already added this friend.");
            return false;
        }
    }
    public static boolean addToWishList(User user, Item item) throws SQLException{
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("INSERT INTO Wishlist(UserID, ItemID) VALUES(?, ?)");
        statement.setInt(1, user.getUserid());
        statement.setInt(2, item.getItemid());
        int result = statement.executeUpdate();
        if(result == 1){
            statement.close();
            System.out.println("Item was added successfuly.");
            return true;
        }
        else{
            System.out.println("Item wasn't added.");
            return false;
        }
    }
    public static boolean contribute(User user , User friend , Item item ,double amount ) throws SQLException{
    
        Connection con = DBConnection.getConnection();
        PreparedStatement result = con.prepareCall("INSERT INTO contributions ( userid, friendid, itemid, amount) VALUES ( ?, ?, ?, ?)");
        result.setInt(1, friend.getUserid());
        result.setInt(2, user.getUserid());
        result.setInt(3, item.getItemid()); 
        result.setDouble(4,amount);

         int out = result.executeUpdate();
         
         if(out == 1){
            result.close();
            return true ;}
        return false;
    }
    public static boolean register(User user) throws SQLException{
        int result;
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareCall("INSERT INTO users (FullName, Email, Password, DateOfBirth) VALUES (?, ?, ?, ?)");
        statement.setString(1, user.getFullname());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setDate(4, user.getDateOfBirth());
        try{
            result = statement.executeUpdate();
            if(result == 1){
                return true;
            }
            else{
                return false;
            }
        }
        catch(SQLException ex){
            return false;
        }
    }
    public static boolean removeFriend(User user, User friend) throws SQLException{
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("DELETE FROM Friendship WHERE ((userID = ? AND friendID = ?) OR (userID = ? AND friendID = ? )) ");
        statement.setInt(1, friend.getUserid());
        statement.setInt(2, user.getUserid());
        statement.setInt(4, friend.getUserid());
        statement.setInt(3, user.getUserid());
        int result = statement.executeUpdate();
        if(result == 1){
            statement.close();
            System.out.println("Friend was deleted successfuly.");
            return true;
        }
        else{
            System.out.println("Friend wasn't deleted.");
            return false;
        }
    }
    public static boolean acceptFriend(User user, User friend) throws SQLException{
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("UPDATE friendship SET friendshipStatus = 'Accepted' WHERE (userID = ? AND friendID = ?) OR (userID = ? AND friendID = ?)");
        statement.setInt(1, friend.getUserid());
        statement.setInt(2, user.getUserid());
        statement.setInt(4, friend.getUserid());
        statement.setInt(3, user.getUserid());
        int result = statement.executeUpdate();
        if(result > 0)
            return true;
        return false;
    }
    
    public static boolean clearWishlist (User user) throws SQLException {
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("delete from wishlist where userID = ? and Progress in (0.00);");
        statement.setInt(1, user.getUserid());
        int result = statement.executeUpdate();
        if (result > 0){
            statement.close();
            System.out.println("Wishlist successfully cleared!");
            return true;
        }
        else {
            System.out.println("No item affected, your friends already contributed!");
            return false;
        }
    }
}
