/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Server.IWishServer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class ServerGUIController implements Initializable {

    @FXML
    private Button startbtn;
    @FXML
    private Button stopbtn;
    @FXML
    private Button additembtn;
    private boolean running;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         stopbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent value) {
                if(running){
                    IWishServer.kill();
                    System.out.println("Server Stopped ..");
                }
            }
        });
         
        Platform.runLater(() -> {
            Stage primaryStage = (Stage) (stopbtn.getScene().getWindow());
            primaryStage.setOnCloseRequest(value -> stopbtn.fire()); 
        });

        startbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent value) {
                 Thread init = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        running = true;
                        System.out.println("Server Start Working !!");
                        new IWishServer();
                    }
                });
                init.start();
            }
        });
    }    
    
}
