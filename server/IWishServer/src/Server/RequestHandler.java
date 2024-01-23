/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Server.DAO.DataModification;
import Server.DAO.DataRetrieval;
import Server.DTO.*;
import Server.DTO.User;
import com.google.gson.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class RequestHandler  {
    
    private User user;
    private volatile boolean login;
    private boolean notifyon;
    private ArrayList<Notification> notifications = new ArrayList<Notification>() ;
    private Gson gson;
    private JsonObject capsule;

    /**
     * Class Responsible of handling client's requests 
     */
    public RequestHandler() {
        gson = new Gson();
    }
    /**
     * Differentiate between client's request types and process it to get what he wants
     * @param Request Request from client in form of JSON Object 
     * @return Desired data or Completion status of request based on which type of request client made
     * @throws SQLException 
     */
    public String process(String Request) throws SQLException{
        
        JsonObject client = gson.fromJson(Request, JsonObject.class);
        String type = client.get("request").getAsString();
        
        MessageProtocol.RETRIEVAL retriev = gson.fromJson(type, MessageProtocol.RETRIEVAL.class);
        MessageProtocol.MODIFY modify = gson.fromJson(type, MessageProtocol.MODIFY.class);
        
        if(retriev != null)
        { 
            if(login)
                switch(retriev){                   
                    case GET_FRIENDS :
                        return dataToJson(DataRetrieval.getFriends(user),MessageProtocol.RETRIEVAL.GET_FRIENDS );

                    case GET_WISHLIST :
                        return dataToJson(DataRetrieval.getWishList(user),MessageProtocol.RETRIEVAL.GET_WISHLIST );
                   
                    case GET_FRIEND_WISHLIST :
                        User friend  = gson.fromJson(client.get("data").getAsString(), User.class);
                        return dataToJson(DataRetrieval.getWishList(friend),MessageProtocol.RETRIEVAL.GET_FRIEND_WISHLIST );
        
                    case GET_ITEMS :
                        return dataToJson(DataRetrieval.getItems(user),MessageProtocol.RETRIEVAL.GET_ITEMS );

                    case GET_CONTRIBUTION :
                        Item item = gson.fromJson(client.get("data").getAsString() , Item.class); 
                        return dataToJson(DataRetrieval.getContribution(user ,item),MessageProtocol.RETRIEVAL.GET_CONTRIBUTION );

                    case GET_NOTIFICATIONS :
                        notifications = DataRetrieval.getNotifications(user);
                        notifyon = true;
                        return dataToJson(notifications,MessageProtocol.RETRIEVAL.GET_NOTIFICATIONS );
                    
                    case GET_USERS :
                        String searchedText = gson.fromJson(client.get("data").getAsString(), String.class);
                        return dataToJson(DataRetrieval.getUsers(user, searchedText),MessageProtocol.RETRIEVAL.GET_USERS );
                }
            if(retriev == MessageProtocol.RETRIEVAL.LOGIN ){
                  user = gson.fromJson(client.get("data").getAsString(), User.class);
                  login = DataRetrieval.login(user);
                  return loginReplyToJson(login); 
            }
        } else if(modify != null )
        {
            /* 
               Recieve Data from json before calling any method
               ex : user = gson.fromJson(client.get("data").getAsString(), User.class); 
            */
            User friend;
            Item item;
            switch(modify){
                case ADD_FRIEND :
                    friend  = gson.fromJson(client.get("data").getAsString(), User.class);
                    return statusToJson(DataModification.addFriend(user, friend),MessageProtocol.MODIFY.ADD_FRIEND );
                    
                case ADD_TO_WISHLIST :
                    item = gson.fromJson(client.get("data").getAsString(), Item.class);
                    return statusToJson(DataModification.addToWishList(user, item),MessageProtocol.MODIFY.ADD_TO_WISHLIST );    
                    
                case CONTRIBUTE :
                    friend  = gson.fromJson(client.get("friend_data").getAsString(), User.class);
                    Item conitem  = gson.fromJson(client.get("item_data").getAsString(), Item.class);
                    double amount  = client.get("amount").getAsDouble();
                 return statusToJson(DataModification.contribute(user ,friend , conitem , amount),MessageProtocol.MODIFY.CONTRIBUTE );
                     
                case REGISTER :
                   user = gson.fromJson(client.get("data").getAsString(), User.class);   
                   login = DataModification.register(user);
                   return statusToJson(login,MessageProtocol.MODIFY.REGISTER); 
                
                case REMOVE_FRIEND :
                    friend  = gson.fromJson(client.get("data").getAsString(), User.class);
                    return statusToJson(DataModification.removeFriend(user, friend),MessageProtocol.MODIFY.REMOVE_FRIEND); 
                    
                case ACCEPT_FRIEND :
                    friend  = gson.fromJson(client.get("data").getAsString(), User.class);
                    return statusToJson(DataModification.acceptFriend(user, friend),MessageProtocol.MODIFY.ACCEPT_FRIEND); 
                    
                case DENY_REQUEST:
                    friend = gson.fromJson(client.get("data").getAsString(), User.class);
                     return statusToJson(DataModification.removeFriend(user, friend),MessageProtocol.MODIFY.DENY_REQUEST);
                case CLEAR_WISHLIST:
                     return statusToJson(DataModification.clearWishlist(user),MessageProtocol.MODIFY.CLEAR_WISHLIST);
            
            }
        }
        
        capsule = new JsonObject();
        capsule.addProperty("reply", type);
        capsule.addProperty("status", false);
        
        if(login)
            capsule.addProperty("message", "Bad Request");
        else
            capsule.addProperty("message", "Login in First!!");
        return capsule.toString();

    }
    
    /**
     * Converting data client wants in form of JSON object
     * to be sent as reply from server to client
     * @param arr contains data which customer requested
     * @param msg type of request server are replying to , only for MessageProtocol of type RETRIEVAL
     * @return requested Data and request type in form of JSON
     */
    
    private String dataToJson(ArrayList<?> arr  ,MessageProtocol.RETRIEVAL msg ){
        capsule = new JsonObject();
        String json = gson.toJson(arr);
        capsule.addProperty("reply", gson.toJson(msg));
        capsule.addProperty("data", json);
        
        return capsule.toString();
    }
    /**
     * Converting completion status of request in form of JSON object
     * to be sent as reply from server to client
     * @param status true or false depends on user's modification request is done or not
     * @param msg type of request server are replying to , only for MessageProtocol of type MODIFY
     * @return completion status and request type in form of JSON
     */
    private String statusToJson(boolean status ,MessageProtocol.MODIFY msg ){
        capsule = new JsonObject();
        capsule.addProperty("reply", gson.toJson(msg));
        capsule.addProperty("status", status);
        return capsule.toString();
    }
    /**
     * Preparing JSON object to be send as reply to client's Login request
     * @param status true or false depends on login is successful or not 
     * @return Client data , login status and request type in form of JSON
     */
    private String loginReplyToJson(boolean status){
        capsule = new JsonObject();
        capsule.addProperty("reply", gson.toJson(MessageProtocol.RETRIEVAL.LOGIN));
        capsule.addProperty("status", status);
        capsule.addProperty("data", gson.toJson(user));
        return capsule.toString();
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isNotifyOn() {
        return notifyon;
    }
    public boolean hasNotifications() {  
        try {
            if(DataRetrieval.getNotifications(user).size() != notifications.size() ){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
}
