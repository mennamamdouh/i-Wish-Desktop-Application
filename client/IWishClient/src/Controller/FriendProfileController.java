/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Connection.ReceiverHandler;
import Model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class FriendProfileController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private Button RemoveFriendButton;
    @FXML
    private Button backhome;
    @FXML
    private TableView<?> wishlistTable;
    @FXML
    private TableColumn<?, ?> imageColumn;
    @FXML
    private TableColumn<?, ?> nameColumn;
    @FXML
    private TableColumn<?, ?> priceColumn;
    @FXML
    private TableColumn<?, ?> progressColumn;
    @FXML
    private TableColumn<?, ?> contributeColumn;
    
    private User friend;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        FriendsController friendsController = ReceiverHandler.getFriendscontroller();
        
        backhome.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Parent signupParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                    Scene signupScene = new Scene(signupParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(signupScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        RemoveFriendButton.setOnAction(event -> {
            Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("You're about to remove " + friend.getFullname());
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK){
                            try {
                                // Perform the remove action for the person
                                friendsController.removeFriend(friend);
                            } catch (IOException ex) {
                                Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    Parent friendsParent;
                try {
                    friendsParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                    Scene friendsScene = new Scene(friendsParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(friendsScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}