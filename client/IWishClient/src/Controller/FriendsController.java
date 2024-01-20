/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Connection.MyConnection;
import Connection.MessageProtocol;
import Connection.ReceiverHandler;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author mennatallah
 */
public class FriendsController implements Initializable {
    
    @FXML private TableView<User> listOfFriends;
    @FXML private TableColumn<User, String> imageColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, Void> buttonColumn;
    @FXML private TextField searchTextField;
    boolean searching = false;
    
    // Define the observable list that holds the friends data
    private ObservableList<User> friends;
    
    // Define a collection to hold the targeted people
    private ObservableList<User> filteredPeopleCollection;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ReceiverHandler.setFriendscontroller(this);
        // Implement new thread to request a connection to the server
        Thread connect = new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    MyConnection con = MyConnection.getInstance();
                    // Getting the friend list of the current use
                    getFriendList();
                    // View the friends list into the table view through the Application Thread
                } catch (IOException ex) {
                    Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        connect.start();
        
        // Set the cell value factory for the imageColumn
        imageColumn.setCellValueFactory(
                new PropertyValueFactory<>("userphoto")
        );

        // Set the cell factory for the imageColumn
        imageColumn.setCellFactory(new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell<User, String> call(TableColumn<User, String> param) {
                 return new TableCell<User, String>(){
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
                new PropertyValueFactory<>("fullname")
        );
        
        // Configure the search field listener
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchForPeople(newValue);
            } catch (IOException ex) {
                Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        // Make the table's rows clickable to be able to visit the friend's profile
        listOfFriends.setRowFactory(new Callback<TableView<User>, TableRow<User>>() {
            @Override
            public TableRow<User> call(TableView<User> tv) {
                TableRow<User> userRow = new TableRow<>();
                userRow.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                          public void handle(MouseEvent event) {
                        if (event.getClickCount() == 1 && (!userRow.isEmpty())) {
                            try {
                                // User rowData = userRow.getItem();
                                // System.out.println("You clicked on: " + rowData.getFullname());
                                // Here should be the calling method of the FXML file of the Friend's Profile Scene
                                Parent signupParent = FXMLLoader.load(getClass().getResource("/View/FriendProfile.fxml"));
                                Scene signupScene = new Scene(signupParent);
                                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                window.setScene(signupScene);
                                window.show();
                            } catch (IOException ex) {
                                Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });                
                
                return userRow ;
            }
        });
    }
    
    private void getFriendList() throws IOException{
        resetButton(false);
        // Prepare the request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.GET_FRIENDS));
        
        // Send the request
        MyConnection.getInstance().getOutputStream().println(request.toString());
        
    }
    public void getFriendListHandler(String msg ){
        Gson gson = new Gson();
         // Process the reply
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        
        if(reply.has("data")){
            String arr = reply.get("data").getAsString();
            Type dataType = new TypeToken<ArrayList<User>>(){}.getType();
            ArrayList<User> friendsList = gson.fromJson(arr, dataType);
            friends = FXCollections.observableArrayList(friendsList);
        }
        else{
            System.out.println("No data found");
        }
        Platform.runLater(() -> {
           listOfFriends.setItems(friends);
        });
    }
    
    // A method to remove friends from the friend list
    private void removeFriend(User friend) throws IOException {
        // Prepare the request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.REMOVE_FRIEND));
        request.addProperty("data", gson.toJson(friend));
                
        // Send the request
        MyConnection.getInstance().getOutputStream().println(request.toString());

    }
    private void addFriend(User friend) throws IOException {
        // Prepare the request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.ADD_FRIEND));
        request.addProperty("data", gson.toJson(friend));
                
        // Send the request
        MyConnection.getInstance().getOutputStream().println(request.toString());
        
        // Wait for the reply
        String msg = MyConnection.getInstance().getInputStream().readLine();
        
        // Update the viewed list after removing the friend
        getFriendList();
        Platform.runLater(() -> {
            resetButton(false);
            listOfFriends.setItems(friends);
        });
    }
     public void addAndRemoveFriendHandler(){
        try {
            // Update the viewed list after removing the friend
            getFriendList();
            Platform.runLater(() -> {
                resetButton(false);
            });
        } catch (IOException ex) {
            Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // A method to search for a specific friends
    private void searchForPeople(String searchText) throws IOException {
        
        if(searchText.isEmpty()){
            resetButton(false);
            listOfFriends.setItems(friends);
        }
        else{
            resetButton(true);
            // Prepare the request
            Gson gson = new Gson();
            JsonObject request = new JsonObject();
            request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.GET_USERS));
            request.addProperty("data", gson.toJson(searchText));

            // Send the request
            MyConnection.getInstance().getOutputStream().println(request.toString());
        }
    }
    public void searchHandler(String msg){
           Gson gson = new Gson();
        // Process the reply
            JsonObject reply = gson.fromJson(msg, JsonObject.class);

            if(reply.has("data")){
                String arr = reply.get("data").getAsString();
                Type dataType = new TypeToken<ArrayList<User>>(){}.getType();
                ArrayList<User> filteredPeople = gson.fromJson(arr, dataType);
                filteredPeopleCollection = FXCollections.observableArrayList(filteredPeople);
                Platform.runLater(() -> {
                    listOfFriends.setItems(filteredPeopleCollection);
                });
            }
            else{
                System.out.println("No data found");
            }
    }
    private void resetButton(Boolean searching){
        // Set the cell factory for the buttonColumn
        buttonColumn.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(TableColumn<User, Void> param) {
                if(!searching){
                    return new TableCell<User, Void>() {
                        private final Button removeButton = new Button("Remove");
                        {
                            removeButton.setOnAction(event -> {
                                // Get the row item associated with the clicked button
                                User friend = getTableView().getItems().get(getIndex());

                                Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setContentText("You're about to remove " + friend.getFullname());
                                        alert.showAndWait().ifPresent(response -> {
                                            if (response == ButtonType.OK){
                                                try {
                                                    // Perform the remove action for the person
                                                    removeFriend(friend);
                                                } catch (IOException ex) {
                                                    Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
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
                else{
                    return new TableCell<User, Void>() {
                        private final Button addButton = new Button("Add Friend");
                        {
                            addButton.setOnAction(event -> {
                                // Get the row item associated with the clicked button
                                User friend = getTableView().getItems().get(getIndex());

                                Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setContentText("You're about to add " + friend.getFullname());
                                        alert.showAndWait().ifPresent(response -> {
                                            if (response == ButtonType.OK){
                                                try {
                                                    // Perform the remove action for the person
                                                    addFriend(friend);
                                                } catch (IOException ex) {
                                                    Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
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
                                setGraphic(addButton);
                            }
                        }
                    };
                }
            }            
        });
    }
}