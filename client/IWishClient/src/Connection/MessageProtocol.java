/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

/**
 *
 * @author DELL
 */
public interface MessageProtocol {
    
    enum RETRIEVAL {
        LOGIN , GET_FRIENDS,GET_WISHLIST ,GET_FRIEND_WISHLIST,GET_ITEMS ,GET_CONTRIBUTION , GET_NOTIFICATIONS, GET_USERS
    }
    enum MODIFY {
        ADD_FRIEND, ADD_TO_WISHLIST ,CONTRIBUTE , REGISTER, REMOVE_FRIEND , ACCEPT_FRIEND , DENY_REQUEST, CLEAR_WISHLIST
    }
}
 