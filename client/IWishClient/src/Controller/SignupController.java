/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author Omar
 */
public class SignupController implements Initializable {
    
    @FXML
    private Label label;
    
     @FXML
       private Button RegisterButton;
          
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        RegisterButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("hello");
          
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                    Scene HomeScene = new Scene(root);
                     Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                   window.setScene(HomeScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                }

    }    
        }
         );
         
                 }
}



//import org.apache.derby.jdbc.ClientDriver;
/**
 *
 * @author Omar
 */
 class user {
 
 private String name ; 
 private String email ; 
  private String passowrd ; 

    public user( String name, String email, String passowrd) {
       
        this.name = name;
        this.email = email;
        this.passowrd = passowrd;
    }

    public static int adduser(user firstUser) throws SQLException {
        int result;
        
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/second", "root", "root");
        PreparedStatement statement = con.prepareCall("INSERT INTO iti (ID, Name, Email, Password, DateOfBirth) VALUES (1, ?, ?, ?, '2023-07-01')");
        statement.setString(1, firstUser.getName());
        statement.setString(2, firstUser.getEmail());
        statement.setString(3, firstUser.getPassowrd());

        result = statement.executeUpdate();
        return result;
    }
  

   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
    }
 
}



