/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Server.DTO.Item;
import Server.IWishServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    @FXML
    private Button loaddata;

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
        loaddata.setOnAction(new EventHandler<ActionEvent>() {
            int i = 0;
            @Override
            public void handle(ActionEvent value) {
                if(i==0)
                 new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            i++;
                            AddItemController itemcon = new AddItemController();
                            ArrayList<Item> data = loadData();
                            System.out.println(data.size());
                            for(Item i : data)
                                itemcon.addNewItem(i);
                                } catch (IOException ex) {
                            Logger.getLogger(ServerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerGUIController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            
                    }
                    
                 }).start();
            }
        });
    }
    public ArrayList<Item> loadData() throws ProtocolException, IOException{
        ArrayList<Item> itemlist = new ArrayList<Item>();
        try {
            URL url = new URL("https://api.escuelajs.co/api/v1/products");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // If the response code is HTTP_OK (200), read the response data
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                line = reader.readLine();
                reader.close();
                connection.disconnect();

                JsonParser jp = new JsonParser();
                JsonArray arr = jp.parse(line).getAsJsonArray();
                
                for (int i = 1; i < arr.size() - 15; i++) {
                    JsonObject item = arr.get(i).getAsJsonObject();
                    String name = item.get("title").getAsString();
                    String image = "/resources/genie-lamp-icon.png";
                    if (image.startsWith("[\"") || image.endsWith("\"]")) {
                        image = image.replace("[\"", "");
                        image = image.replace("\"]", "");
                        image = image.replace("\"", "");
                    }
                    double price = item.get("price").getAsDouble() * 40 /* Dollar :( */;

                    itemlist.add(new Item(name, price,image));                
                }
                 return itemlist;
                
            }   } catch (MalformedURLException ex) {
            Logger.getLogger(ServerGUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemlist;
    }
}