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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        // TODO
    }

}
