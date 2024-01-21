/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

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
            wishList.setContent(FXMLLoader.load(getClass().getResource("/View/WishList.fxml"))); 
            friends.setContent(FXMLLoader.load(getClass().getResource("/View/Friends.fxml")));
            about.setContent(FXMLLoader.load(getClass().getResource("/View/About.fxml")));
            notification.setContent(FXMLLoader.load(getClass().getResource("/View/Notification.fxml")));          
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}