/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Server.DAO.DBConnection;
import Server.DTO.Item;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mennatallah
 */
public class AddItemController implements Initializable {

    @FXML
    private TextField itemField;
    @FXML
    private TextField priceField;
    @FXML
    private Button addButton;
    @FXML
    private AnchorPane cvvanchor;
    @FXML
    private Button browseButton;
    @FXML
    private ImageView itemPhoto;
    
    String imageURI;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addItemPhoto();
        addButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            String itemName = itemField.getText();
            int itemPrice = Integer.valueOf(priceField.getText());
            System.out.println("Item: " + itemName + " - Price: " + itemPrice);
            Item item = new Item(itemName, itemPrice, imageURI);
            try {
                if(addNewItem(item))
                {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("An item was added successfuly.");
                    alert.showAndWait();
                    Stage stage = (Stage) (addButton.getScene().getWindow());
                    stage.close();
                }else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Item wasn't added.");
                    alert.showAndWait();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddItemController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private void addItemPhoto(){
        browseButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Photo");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                imageURI = selectedFile.toURI().toString();
                System.out.println("Before: " + imageURI);
                int indexOfResourcesString = imageURI.indexOf("resources");
                imageURI = imageURI.substring(indexOfResourcesString);
                System.out.println("After: " + imageURI);
                Image image = new Image(imageURI);
                itemPhoto.setImage(image);
            }
        });
    }
    
    public boolean addNewItem(Item item) throws SQLException{
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("INSERT INTO Items(ItemName, ItemPhoto, Price) VALUES(?, ?, ?)");
        statement.setString(1, item.getItemname());
        statement.setString(2, item.getItemphoto());
        statement.setDouble(3, item.getPrice());
        int result = statement.executeUpdate();
        if(result == 1){
            statement.close();
            System.out.println("An item was added successfuly.");
            return true;
        }
        else{
            System.out.println("Item wasn't added.");
            return false;
        }
    }
}
