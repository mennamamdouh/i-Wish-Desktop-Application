/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author mennatallah
 */
public class FriendsController implements Initializable {
    
    @FXML private TableView<Friend> listOfFriends;
    @FXML private TableColumn<Friend, String> imageColumn;
    @FXML private TableColumn<Friend, String> nameColumn;
    @FXML private TableColumn<Friend, Void> removeButtonColumn;
    @FXML private TextField searchTextField;
    
    private ObservableList<Friend> friends =
        FXCollections.observableArrayList(
            new Friend("resources/male-avatar.png", "Jacob Smith"),
            new Friend("resources/female-avatar.png", "Isabella Johnson"),
            new Friend("resources/male-avatar.png", "Ethan Williams"),
            new Friend("resources/female-avatar.png", "Emma Jones"),
            new Friend("resources/male-avatar.png", "Michael Brown")
    );
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Set the cell value factory for the imageColumn
        imageColumn.setCellValueFactory(
                new PropertyValueFactory<>("profilePicture")
        );

        // Set the cell factory for the imageColumn
        imageColumn.setCellFactory(new Callback<TableColumn<Friend, String>, TableCell<Friend, String>>() {
            @Override
            public TableCell<Friend, String> call(TableColumn<Friend, String> param) {
                return new TableCell<Friend, String>(){
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
                new PropertyValueFactory<>("fullName")
        );
        
        // Set the cell factory for the removeButtonColumn
        removeButtonColumn.setCellFactory(new Callback<TableColumn<Friend, Void>, TableCell<Friend, Void>>() {
            @Override
            public TableCell<Friend, Void> call(TableColumn<Friend, Void> param) {
                return new TableCell<Friend, Void>() {
                    private final Button removeButton = new Button("Remove");

                    {
                        removeButton.setOnAction(event -> {
                            // Get the row item associated with the clicked button
                            Friend friend = getTableView().getItems().get(getIndex());
                            // Perform the remove action for the person
                            removeFriend(friend);
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
        
        // Set the friends data into the table
        listOfFriends.setItems(friends);
        
        // Configure the search field listener
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> searchFriends(newValue));
    }
    
    private void removeFriend(Friend friend) {
        // Perform the remove action for the person
        friends.remove(friend);
        
        // Update the viewed list after removing
        String searchText = searchTextField.getText().toLowerCase();
        searchFriends(searchText);
    }
    
    private void searchFriends(String searchText) {
        
        ObservableList<Friend> filteredFriends = FXCollections.observableArrayList();
        
        for (Friend friend : friends) {
            if (friend.getFullName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredFriends.add(friend);
            }
        }

        // Set the filteredList as the items of the listOfFriends TableView
        listOfFriends.setItems(filteredFriends);
    }
    
}