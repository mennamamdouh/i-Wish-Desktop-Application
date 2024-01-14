/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAO;

import Server.DTO.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class DataModification {
    
    public static boolean addFriend(){
        return false;
    }
    public static boolean addToWishList(){
        return false;
    }
    public static boolean contribute(){
        return false;
    }
    public static boolean register(){
        return false;
    }
    public static boolean removeFriend(User user, User friend) throws SQLException{
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("DELETE FROM Friendship WHERE UserID = ? AND FriendID = ?");
        statement.setInt(1, user.getUserid());
        statement.setInt(2, friend.getUserid());
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
}
