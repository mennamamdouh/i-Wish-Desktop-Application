/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Server.DAO.DataModification;
import Server.DAO.DataRetrieval;
import Server.DTO.User;
import com.google.gson.*;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class RequestHandler  {
    
    private User user;
    private boolean login;
    private boolean hasnotifications;
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
                        return dataToJson(DataRetrieval.getFriends(),MessageProtocol.RETRIEVAL.GET_FRIENDS );

                    case GET_WISHLIST :
                        return dataToJson(DataRetrieval.getWishList(user),MessageProtocol.RETRIEVAL.GET_WISHLIST );

                    case GET_ITEMS :
                        return dataToJson(DataRetrieval.getItems(),MessageProtocol.RETRIEVAL.GET_ITEMS );

                    case GET_CONTRIBUTION :
                        return dataToJson(DataRetrieval.getContribution(),MessageProtocol.RETRIEVAL.GET_CONTRIBUTION );

                    case GET_NOTIFICATIONS :
                        return dataToJson(DataRetrieval.getNotifications(),MessageProtocol.RETRIEVAL.GET_NOTIFICATIONS );
                }
            if(retriev == MessageProtocol.RETRIEVAL.LOGIN ){
                  user = gson.fromJson(client.get("data").getAsString(), User.class);
                  login = DataRetrieval.login(user);
                  return loginReplyToJson(login); 
            }
        } else if(modify != null & login)
        {
            /* 
               Recieve Data from json before calling any method
               ex : user = gson.fromJson(client.get("data").getAsString(), User.class); 
            */
            switch(modify){
                case ADD_FRIEND :
                    return statusToJson(DataModification.addFriend(),MessageProtocol.MODIFY.ADD_FRIEND );
                    
                case ADD_TO_WISHLIST :
                    return statusToJson(DataModification.addToWishList(),MessageProtocol.MODIFY.ADD_TO_WISHLIST );    
                    
                case CONTRIBUTE :
                    return statusToJson(DataModification.contribute(),MessageProtocol.MODIFY.CONTRIBUTE );
                    
                case REGISTER :
                   return statusToJson(DataModification.register(),MessageProtocol.MODIFY.REGISTER); 
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

    public boolean hasNotifications() {
        return hasnotifications;
    }

    public void setHasnotifications(boolean hasnotifications) {
        this.hasnotifications = hasnotifications;
    }
    
    
}
