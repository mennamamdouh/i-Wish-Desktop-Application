/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Connection.MessageProtocol;
import Connection.MyConnection;
import Connection.ReceiverHandler;
import Model.Item;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author mennatallah
 */

public class ItemsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private TableView<Item> listOfItems;
    @FXML private TableColumn<Item, String> imageColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, Void> addItemButton;
    
    // Define the observable list that holds the friends data
    private ObservableList<Item> items;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ReceiverHandler.setItemscontroller(this);
        
        Thread connect = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    MyConnection con = MyConnection.getInstance();
                    // Getting the friend list of the current use
                    getItemsList();
                    // View the friends list into the table view through the Application Thread
                } catch (IOException ex) {
                    Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        connect.start();
        
        // Set the cell value factory for the imageColumn
        imageColumn.setCellValueFactory(
                new PropertyValueFactory<>("itemphoto")
        );
        
        // Set the cell factory for the imageColumn
        imageColumn.setCellFactory(new Callback<TableColumn<Item, String>, TableCell<Item, String>>() {
            @Override
            public TableCell<Item, String> call(TableColumn<Item, String> param) {
                return new TableCell<Item, String>(){
                    private final ImageView imageView = new ImageView();
                    
                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);

                        if (empty || imagePath == null) {
                            setGraphic(null);
                        } else {
                            Image image = new Image(imagePath);
                            imageView.setImage(image);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        
        // Set the cell value factory for the nameColumn
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("itemname")
        );
        
        addItemButton.setCellFactory(new Callback<TableColumn<Item, Void>, TableCell<Item, Void>>() {
            @Override
            public TableCell<Item, Void> call(TableColumn<Item, Void> param) {
                return new TableCell<Item, Void>() {
                    private final Button removeButton = new Button("Add");
                    {
                        removeButton.setOnAction(event -> {
                            // Get the row item associated with the clicked button
                            Item item = getTableView().getItems().get(getIndex());

                            Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setContentText("You're about to add " + item.getItemname());
                                    alert.showAndWait().ifPresent(response -> {
                                        if (response == ButtonType.OK){
                                            try {
                                                // Perform the remove action for the person
                                                addItem(item);
                                            } catch (IOException ex) {
                                                Logger.getLogger(ItemsController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    });
                                });
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(removeButton);
                        }
                    }
                };
            }
            
        });
    }
    
    private void getItemsList() throws IOException{
        // Prepare the request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.GET_ITEMS));
        
        // Send the request
        MyConnection.getInstance().getOutputStream().println(request.toString());
        
    }
    
    public void getItemsListHandler(String msg ){
        // Prepare the request
        Gson gson = new Gson();
        
        // Process the reply
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        if(reply.has("data")){
            String arr = reply.get("data").getAsString();
            Type dataType = new TypeToken<ArrayList<Item>>(){}.getType();
            ArrayList<Item> itemsList = gson.fromJson(arr, dataType);
            items = FXCollections.observableArrayList(itemsList);
        }
        else{
            System.out.println("No data found");
        }
        Platform.runLater(() -> {
           listOfItems.setItems(items);
        });
    }
    
    private void addItem(Item item) throws IOException{
        // Prepare the request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.ADD_TO_WISHLIST));
        request.addProperty("data", gson.toJson(item));
                
        // Send the request
        MyConnection.getInstance().getOutputStream().println(request.toString());
        
        // Wait for the reply
        String msg = MyConnection.getInstance().getInputStream().readLine();
        
        // Update the viewed list after removing the friend
        getItemsList();
        Platform.runLater(() -> {
            listOfItems.setItems(items);
        });
    }
}