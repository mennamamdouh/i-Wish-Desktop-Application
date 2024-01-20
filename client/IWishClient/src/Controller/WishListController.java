/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Connection.MyConnection;
import Connection.ReceiverHandler;
import Main.IWishClient;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author attia
 */
public class WishListController implements Initializable {

    //defining the wishlist 
    ArrayList<WishListItems> myWishList = new ArrayList<>();
    WishListItems item1 = new WishListItems("file:src/Resources/blank.png", "Headphones", 99.99, 50.0);
    WishListItems item2 = new WishListItems("file:src/Resources/cool.jpeg", "Smartwatch", 149.99, 25.0);
    WishListItems item3 = new WishListItems("file:src/Resources/gift1.png", "Backpack", 39.99, 75.0);

    @FXML
    private Button ProfilePictureButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button ClearMyWishlistButton;
    @FXML
    private ListView wishlistList;
    @FXML
    private Button SignOutButton;
    @FXML
    private Text txtFullName;
    @FXML
    private Text txtBirthDate;
    @FXML
    private Button searchItemsButton;
    
    private static final String DEFAULT_IMAGE_PATH = "file:src/Resources/blank.png";
    private static final double CIRCLE_CENTER = 0.5; // Center ratio for circular clipping


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ReceiverHandler.setWishListcontroller(this);
        
        Platform.runLater(() -> {
               txtFullName.setText(IWishClient.user.getFullname());
            });
        
        Platform.runLater(() -> {
               txtBirthDate.setText(IWishClient.user.getDateOfBirth().toString());
            });
        
        setupDefaultImage();
        addProfilePictureButtonHandler();
        signOutButtonHandler();
        ClearMyWishlistButtonHandler();
        // adding few items in the list
        myWishList.add(item1);
        myWishList.add(item2);
        myWishList.add(item3);

        wishlistList.setItems(FXCollections.observableArrayList(myWishList));
        wishlistList.setCellFactory(param -> new ListCell<WishListItems>() {
            private final HBox hbox = new HBox();
            private final Label label = new Label();
            private final ProgressBar progressBar = new ProgressBar();

            {
                hbox.getChildren().addAll(label, progressBar);
                HBox.setHgrow(progressBar, Priority.ALWAYS);
            }

            @Override
            protected void updateItem(WishListItems item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.getName() + ", Price: $" + item.getPrice());
                    progressBar.setProgress(item.getProgress() / 100.0);

                    Image image = new Image(item.getPhoto());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(50); // Adjust the height as needed
                    imageView.setPreserveRatio(true);

                    hbox.getChildren().setAll(imageView, label, progressBar);
                    HBox.setHgrow(progressBar, Priority.ALWAYS);

                    setGraphic(hbox);
                }
            }
        });
        
        // Configure the search for items button
        searchItemsButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("/View/Items.fxml"));
                Scene scene = new Scene(root);
                popup.setResizable(false);
                popup.setScene(scene);
                popup.setTitle("Items");
                popup.show();
            } catch (IOException ex) {
                Logger.getLogger(WishListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void setupDefaultImage() {
        Image defaultImage = new Image("file:src/Resources/gift.jpg");
        imageView.setImage(defaultImage);

        Circle defaultClip = createCircularClip();
        imageView.setClip(defaultClip);
    }

    private void signOutButtonHandler(){
    SignOutButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            try {
                Parent signinParent = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
                Scene signinScene = new Scene(signinParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setScene(signinScene);
                window.show();
                MyConnection.getInstance().closeConnection();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void addProfilePictureButtonHandler() {
        ProfilePictureButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Photo");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);

                Circle clip = createCircularClip();
                imageView.setClip(clip);
            }
        });
    }

    private void ClearMyWishlistButtonHandler() {
        ClearMyWishlistButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            myWishList.clear();
            wishlistList.getItems().clear();
        });
    }

    private Circle createCircularClip() {
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) * CIRCLE_CENTER;
        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(radius);
        return clip;
    }
}
// items class to add few items to the wishlist

class WishListItems {

    private String photo;
    private String name;
    private double price;
    private double progress; // Represents progress as a percentage (0.0 to 100.0)

    // Constructor
    public WishListItems(String photo, String name, double price, double progress) {
        this.photo = photo;
        this.name = name;
        this.price = price;
        this.progress = progress;
    }

    // Getters and Setters (optional)
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        // Ensuring progress stays within the valid range of 0.0 to 100.0
        if (progress < 0.0 || progress > 100.0) {
            throw new IllegalArgumentException("Progress should be between 0.0 and 100.0");
        }
        this.progress = progress;
    }
}
