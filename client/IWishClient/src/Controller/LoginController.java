package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class LoginController implements Initializable {

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSignup.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            try {
                Parent signupParent = FXMLLoader.load(getClass().getResource("/View/Signup.fxml"));
                Scene signupScene = new Scene(signupParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setScene(signupScene);
                window.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        btnSignin.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            try {
                Parent homeParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                Scene homeScene = new Scene(homeParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setScene(homeScene);
                window.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
