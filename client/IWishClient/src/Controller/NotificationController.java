/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Connection.MessageProtocol;
import Connection.MyConnection;
import Connection.ReceiverHandler;
import Controller.SubController.NotificationUnitController;
import Model.Notification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class NotificationController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private VBox vcontainer;
    private ArrayList<Notification> notifications = new ArrayList<Notification>();
    private Gson gson = new Gson();
    public static Node currnode ;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ReceiverHandler.setNotificationcontroller(this);
        requestNotifications();
    }

    public void viewNotifications() {
        if (notifications.size() > 0) {
            vcontainer.getChildren().clear();
        }
        for (int i = 0; i < notifications.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SubView/NotificationUnit.fxml"));
                Node temp = loader.load();
                NotificationUnitController controller = loader.getController();
                temp.setUserData(controller);
                controller.setIdentity(vcontainer, temp);
                controller.setUnit(notifications.get(i)); // data for each item
                vcontainer.getChildren().add(temp);
            } catch (IOException ex) {
                Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void requestNotifications() {
        try {
            JsonObject request = new JsonObject();
            request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.GET_NOTIFICATIONS));
            MyConnection.getInstance().getOutputStream().println(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void waitForHandler(String msg) {
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        String data = reply.get("data").getAsString();
        Type dataType = new TypeToken<ArrayList<Notification>>() {}.getType();
        notifications = gson.fromJson(data, dataType);
        Platform.runLater(() -> {
           viewNotifications();
        });
    }
    public void nodeHandler(String msg ){
        NotificationUnitController curr = (NotificationUnitController) currnode.getUserData();
        curr.acceptAndDenyHandler(msg);
    }
    public int numOfNotification(){
        return notifications.size();
    }
}
