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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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
        
        additembtn.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent event) {
                try {           
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Main/AddItem.fxml"));
                    AnchorPane addItemScene = loader.load();

                    Stage blockingWindow = new Stage();
                    blockingWindow.initModality(Modality.APPLICATION_MODAL);
                    blockingWindow.getIcons().add(new Image("/resources/genie-lamp-icon.png"));
                    blockingWindow.setTitle("Add Items");
                    blockingWindow.setScene(new Scene(addItemScene));
                blockingWindow.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
             } 
        });
    } 
}