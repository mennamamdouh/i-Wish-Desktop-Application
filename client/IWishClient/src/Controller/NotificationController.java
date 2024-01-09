/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.SubController.NotificationUnitController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private boolean hasnotification = true;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(hasnotification)
        {
            vcontainer.getChildren().clear();
            for(int i = 0 ; i<10 ; i++){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/SubView/NotificationUnit.fxml"));
                    Node temp = loader.load();
                    NotificationUnitController controller = loader.getController();
                    controller.setIdentity(vcontainer , temp);
                    //controller.loadData(Notification unit); // Later to Display Data for each Unit
                    vcontainer.getChildren().add(temp);   
                } catch (IOException ex) {
                    Logger.getLogger(NotificationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }    
    
}
