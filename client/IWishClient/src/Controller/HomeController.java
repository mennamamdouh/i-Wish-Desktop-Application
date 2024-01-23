/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Connection.ReceiverHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class HomeController implements Initializable {
    @FXML
    private Tab wishList;
    @FXML
    private Tab friends;
    @FXML
    private Tab notification;
    @FXML
    private Tab about;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ReceiverHandler.setHomecontroller(this);
            wishList.setContent(FXMLLoader.load(getClass().getResource("/View/WishList.fxml"))); 
            friends.setContent(FXMLLoader.load(getClass().getResource("/View/Friends.fxml")));
            about.setContent(FXMLLoader.load(getClass().getResource("/View/About.fxml")));
            notification.setContent(FXMLLoader.load(getClass().getResource("/View/Notification.fxml")));   
            notification.setOnSelectionChanged((event) -> {
               notification.setText("Notifications");
                notification.setGraphic(null);
            });
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void createTabGraphic(int oldsize , int newsize) {
        if(newsize > oldsize)
            Platform.runLater(() -> {
                notification.setText("");
                Image image = new Image(getClass().getClassLoader().getResourceAsStream("resources/red-notification.png"));

                ImageView imageView = new ImageView(image);

                imageView.setFitWidth(20); 
                imageView.setFitHeight(20); 

                Label label = new Label("Notifications  ");

                HBox graphic = new HBox( label , imageView);
                graphic.setAlignment(Pos.CENTER_LEFT); 

                notification.setGraphic(graphic);
            });
    }
}