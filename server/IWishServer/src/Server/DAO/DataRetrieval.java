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

    public static boolean login(User user) throws SQLException {
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("select * from users where email = ? and password = ? ");
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getPassword());

        result = statement.executeQuery();
        if (result.next()) {
            user.setUserid(result.getInt(1));
            user.setFullname(result.getString(3));
            user.setUserphoto(result.getString(4));
            user.setDateOfBirth(result.getDate(6));
            statement.close();
            return true;
        } else {
            statement.close();
            return false;
        }
    }

    public static ArrayList<User> getFriends(User user) throws SQLException {
        ArrayList<User> friends = new ArrayList<User>();
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("SELECT F.FriendID, U.FullName, U.UserPhoto, F.friendshipStatus\n" +
                                                    "FROM users AS U\n" +
                                                    "INNER JOIN friendship AS F\n" +
                                                    "    ON F.FriendID = U.UserID\n" +
                                                    "WHERE F.UserID = ? AND friendshipStatus = 'Accepted'\n" +
                                                    "UNION\n" +
                                                    "SELECT F.UserID, U.FullName, U.UserPhoto, F.friendshipStatus\n" +
                                                    "FROM users AS U\n" +
                                                    "INNER JOIN friendship AS F\n" +
                                                    "    ON F.UserID = U.UserID\n" +
                                                    "WHERE F.FriendID = ? AND FriendshipStatus = 'Accepted'", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, user.getUserid());
        statement.setInt(2, user.getUserid());
        result = statement.executeQuery();
        if (result.next()) {
            result.previous();
            while (result.next()) {
                friends.add(new User(result.getInt(1), result.getString(2), result.getString(3)));
            }
        }
        statement.close();
        return friends;
    }

    public static ArrayList<WishList> getWishList(User user) throws SQLException{
        ArrayList<WishList> wishlistArr = new ArrayList<WishList>();
        ResultSet result;
        Connection con = DBConnection.getConnection();
        
        PreparedStatement statement = con.prepareCall("select * from wishlist where userid = ?" , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, Integer.toString(user.getUserid()));
        result = statement.executeQuery();
        if(result.next())
        {
            result.previous();
            while(result.next()){
                wishlistArr.add(new WishList(user.getUserid(),getItem(result.getInt(2)),result.getDouble(3), result.getDouble(4)));

            }
            return wishlistArr;
        }
        statement.close();
        
        return wishlistArr;
    }
    
    private static Item getItem(int itemId) throws SQLException{
        Item item;
            Connection con = DBConnection.getConnection();
            ResultSet result;
            PreparedStatement statement = con.prepareCall("select * from items where itemid = ?" , ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, Integer.toString(itemId));
            result = statement.executeQuery();
            result.next();
            item = new Item(result.getInt(1),result.getString(2),result.getString(3),result.getDouble(4));
        return item;
    }

    public static ArrayList<Item> getItems(User user) throws SQLException {
        ArrayList<Item> items = new ArrayList<Item>();
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("SELECT * FROM Items WHERE ItemID NOT IN (SELECT ItemID FROM Wishlist WHERE UserID = ?)", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, user.getUserid());
        result = statement.executeQuery();
        if (result.next()) {
            result.previous();
            while (result.next()) {
                items.add(new Item(result.getInt(1), result.getString(2), result.getString(3), result.getDouble(4)));
            }
            return items;
        }
        statement.close();
        return items;

    }

    public static ArrayList<Contribution> getContribution(User user, Item item) throws SQLException {
        ArrayList<Contribution> arr = new ArrayList<Contribution>();
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("select * from contributions where userid= ? and itemid = ? order by contributionid", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, user.getUserid());
        statement.setInt(2, item.getItemid());
        result = statement.executeQuery();
        if (result.next()) {
            result.previous();
            while (result.next()) {
                arr.add(new Contribution(getFriendData(result.getInt(3)),result.getDouble(5)));
            }
            return arr;
        }
        statement.close();
        return arr;
    }

    public static User getFriendData(int id) throws SQLException {
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("select * from users where userid= ? ");
        statement.setInt(1, id);
        result = statement.executeQuery();
        if (result.next())
            return new User(id, result.getString(3), result.getString(4));
        return null;
    }

    public static ArrayList<Notification> getNotifications(User user) throws SQLException {
        ArrayList<Notification> arr = new ArrayList<Notification>();
        ResultSet result;
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareCall("select * from notifications where userid = ? order by notificationid desc", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, user.getUserid());
        result = statement.executeQuery();
        if (result.next()) {
            result.previous();
            while (result.next()) {
                arr.add(new Notification(result.getInt(1) , result.getInt(2) ,result.getInt(3),result.getInt(4),result.getBoolean(5) , result.getString(6) ,result.getString(7) ));
            }
        }
        return arr;
    }

    public static ArrayList<User> getUsers(User user, String searchedText) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        Connection con = DBConnection.getConnection();
        ResultSet result;
        PreparedStatement statement = con.prepareCall("WITH friends AS(\n" +
                                                    "    SELECT F.FriendID, U.FullName, U.UserPhoto, F.FriendshipStatus\n" +
                                                    "    FROM Users AS U\n" +
                                                    "    INNER JOIN Friendship AS F\n" +
                                                    "        ON F.FriendID = U.UserID\n" +
                                                    "    WHERE F.UserID = ? AND FriendshipStatus = 'Accepted'\n" +
                                                    "    UNION\n" +
                                                    "    SELECT F.UserID, U.FullName, U.UserPhoto, F.FriendshipStatus\n" +
                                                    "    FROM Users AS U\n" +
                                                    "    INNER JOIN Friendship AS F\n" +
                                                    "        ON F.UserID = U.UserID\n" +
                                                    "    WHERE F.FriendID = ? AND FriendshipStatus = 'Accepted'\n" +
                                                    "),\n" +
                                                    "pending AS(\n" +
                                                    "    SELECT F.FriendID, U.FullName, U.UserPhoto, F.FriendshipStatus\n" +
                                                    "    FROM Users AS U\n" +
                                                    "    INNER JOIN Friendship AS F\n" +
                                                    "        ON F.FriendID = U.UserID\n" +
                                                    "    WHERE F.UserID = ? AND FriendshipStatus = 'Pending'\n" +
                                                    "    UNION\n" +
                                                    "    SELECT F.UserID, U.FullName, U.UserPhoto, F.FriendshipStatus\n" +
                                                    "    FROM Users AS U\n" +
                                                    "    INNER JOIN Friendship AS F\n" +
                                                    "        ON F.UserID = U.UserID\n" +
                                                    "    WHERE F.FriendID = ? AND FriendshipStatus = 'Pending'\n" +
                                                    ")\n" +
                                                    "SELECT U.UserID, U.FullName, U.UserPhoto\n" +
                                                    "FROM Users AS U\n" +
                                                    "WHERE (U.UserID NOT IN(SELECT FriendID FROM friends)) AND (U.UserID NOT IN(SELECT FriendID FROM pending)) AND (U.UserID != ?) AND (U.FullName LIKE ?);", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, user.getUserid());
        statement.setInt(2, user.getUserid());
        statement.setInt(3, user.getUserid());
        statement.setInt(4, user.getUserid());
        statement.setInt(5, user.getUserid());
        statement.setString(6, searchedText + '%');
        result = statement.executeQuery();
        if (result.next()) {
            result.previous();
            while (result.next()) {
                users.add(new User(result.getInt(1), result.getString(2), result.getString(3)));
            }
        }
        statement.close();
        return users;
    }

}
