/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

/**
 * Divide requests type coming to server into two categories :
 * RETRIEVAL : request client make when he needs to get data from database  
 * MODIFY : request client make when he needs to change , add or delete from database
 * 
 * @author Diaa
 */
public interface MessageProtocol {
    enum RETRIEVAL {
        LOGIN , GET_FRIENDS,GET_WISHLIST ,GET_FRIEND_WISHLIST ,GET_ITEMS ,GET_CONTRIBUTION , GET_NOTIFICATIONS, GET_USERS
    }
    enum MODIFY {
        ADD_FRIEND, ADD_TO_WISHLIST ,CONTRIBUTE , REGISTER, REMOVE_FRIEND , ACCEPT_FRIEND , DENY_REQUEST, CLEAR_WISHLIST
    }
}