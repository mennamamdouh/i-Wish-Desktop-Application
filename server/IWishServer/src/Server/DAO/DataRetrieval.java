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
            user.setUserphoto(result.getString(4));
            statement.close();
            return true;
        }
        else{
            statement.close();
            return false;
        }
    }
    
    public static ArrayList<User> getFriends(User user) throws SQLException{
        ArrayList<User> friends = new ArrayList<User>();
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("SELECT F.FriendID, U.FullName, U.UserPhoto\n" +
                                                        "FROM Users AS U\n" +
                                                        "INNER JOIN Friendship AS F\n" +
                                                        "    ON F.FriendID = U.userID\n" +
                                                        "WHERE F.UserID = ? AND FriendshipStatus = 'Accepted'", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, user.getUserid());
        result = statement.executeQuery();
        if(result.next())
        {
            result.previous();
            while(result.next()){
                friends.add(new User(result.getInt(1), result.getString(2), result.getString(3)));
            }
        }
        statement.close();
        return friends;
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
    
    public static ArrayList<User> getUsers(User user, String searchedText) throws SQLException{
        ArrayList<User> users = new ArrayList<User>();
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("WITH friends AS(\n" +
                                                        "    SELECT F.FriendID, U.FullName, U.UserPhoto, F.FriendshipStatus\n" +
                                                        "    FROM Users AS U\n" +
                                                        "    INNER JOIN Friendship AS F\n" +
                                                        "        ON F.FriendID = U.userID\n" +
                                                        "    WHERE F.UserID = ? AND FriendshipStatus = 'Accepted'\n" +
                                                        "),\n" +
                                                        "pending AS(\n" +
                                                        "    SELECT F.FriendID, U.FullName, U.UserPhoto, F.FriendshipStatus\n" +
                                                        "    FROM Users AS U\n" +
                                                        "    INNER JOIN Friendship AS F\n" +
                                                        "        ON F.FriendID = U.userID\n" +
                                                        "    WHERE F.UserID = ? AND FriendshipStatus = 'Pending'\n" +
                                                        ")\n" +
                                                        "SELECT U.UserID, U.FullName, U.UserPhoto\n" +
                                                        "FROM Users AS U\n" +
                                                        "WHERE (U.UserID NOT IN(SELECT FriendID FROM friends) AND U.UserID NOT IN(SELECT FriendID FROM pending)) AND U.UserID != ? AND U.FullName LIKE ?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, user.getUserid());
        statement.setInt(2, user.getUserid());
        statement.setInt(3, user.getUserid());
        statement.setString(4, searchedText+'%');
        result = statement.executeQuery();
        if(result.next())
        {
            result.previous();
            while(result.next()){
                users.add(new User(result.getInt(1), result.getString(2), result.getString(3)));
            }
        }
        statement.close();
        return users;
    }
    
}
