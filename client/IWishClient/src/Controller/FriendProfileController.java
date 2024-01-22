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
import Model.WishList;
import Model.WishListItem;
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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class FriendProfileController implements Initializable {

    private User friend;
    @FXML
    private ImageView imageView;
    @FXML
    private Button RemoveFriendButton;
    @FXML
    private Button backhome;
    @FXML
    private TableView<WishList> wishlistTable;
    @FXML
    private TableColumn<WishList, String> imageColumn;
    @FXML
    private TableColumn<WishList, String> nameColumn;
    @FXML
    private TableColumn<WishList, Double> priceColumn;
    @FXML
    private TableColumn<WishList, Double> progressColumn;
    @FXML
    private TableColumn<WishList, Void> contributeColumn;
    @FXML
    private Text friendname;
    @FXML
    private Text friendbd;
    @FXML
    private Circle friendimg;
    private ArrayList<WishList> receivedlist;

    private ObservableList<WishList> hiswishlist;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ReceiverHandler.setFriendprofilecontroller(this);
        FriendsController friendsController = ReceiverHandler.getFriendscontroller();
        
        backhome.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Parent signupParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                    Scene signupScene = new Scene(signupParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(signupScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        RemoveFriendButton.setOnAction(event -> {
            Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("You're about to remove " + friend.getFullname());
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/resources/genie-lamp-icon.png").toString()));
                    stage.setTitle("Remove Friend");
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK){
                            try {
                                // Perform the remove action for the person
                                friendsController.removeFriend(friend);
                            } catch (IOException ex) {
                                Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    Parent friendsParent;
                try {
                    friendsParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                    Scene friendsScene = new Scene(friendsParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(friendsScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });

        imageColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getItem().getItemphoto()));

        // Set the cell factory for the imageColumn
        imageColumn.setCellFactory(param -> new TableCell<WishList, String>() {
                private final ImageView imageView = new ImageView();
                
                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);   
                    if (empty || imagePath == null) {
                        setGraphic(null);
                    } else {
                        try {
                            // Load the image using the correct class loader
                            Image image = new Image(getClass().getClassLoader().getResourceAsStream(imagePath));
                            // Set properties for the ImageView
                            imageView.setImage(image);
                            imageView.setFitWidth(50); // Adjust the width as needed
                            imageView.setFitHeight(50); // Adjust the height as needed
                            
                            // Set the ImageView as the graphic for the cell
                            setGraphic(imageView);
                            
                        } catch (Exception e) {
                           // System.err.println("Error loading image: " + e.getMessage());
                            setGraphic(null);
                        }
                    }
                }
            });
    
        // Set the cell value factory for the Columns
        nameColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getItem().getItemname()));
        
        priceColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getItem().getPrice()).asObject());
        
        progressColumn.setCellValueFactory(
                new PropertyValueFactory<>("progress")
        );
            // Set the cell value factory for the progressColumn
            progressColumn.setCellFactory(param -> new TableCell<WishList, Double>() {
                private final ProgressBar progressBar = new ProgressBar();

                {
                    // Set up the ProgressBar in the cell
                    progressBar.setMaxWidth(Double.MAX_VALUE);
                    setGraphic(progressBar);
                }
    
            @Override
            protected void updateItem(Double progress, boolean empty) {
                super.updateItem(progress, empty);

                if (empty || progress == null) {
                    setGraphic(null);
                } else {
                    // Update the ProgressBar with the current progress
                    progressBar.setProgress(progress / 100.0);
                    setText(String.format("%.1f%%", progress)); // Display the percentage
                }
            }
        });
            
        contributeColumn.setCellFactory(new Callback<TableColumn<WishList, Void>, TableCell<WishList, Void>>() {
            @Override
            public TableCell<WishList, Void> call(TableColumn<WishList, Void> param) {
                return new TableCell<WishList, Void>() {
                    private final Button payButton = new Button("Pay");

                    {
                        payButton.setOnAction(event -> {
                            try {
                                // Get the row item associated with the clicked button
                                WishList selected = getTableView().getItems().get(getIndex());
                                
                                Stage popup = new Stage();
                                popup.initModality(Modality.APPLICATION_MODAL);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Payment.fxml"));
                                Parent temp = loader.load();
                                PaymentController controller = loader.getController();
                                controller.setFriend(friend);
                                controller.setItem(selected);
                                Scene scene = new Scene(temp);
                                popup.setScene(scene);
                                popup.setResizable(false);
                                popup.showAndWait();

                            } catch (IOException ex) {
                                Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            WishList selected = getTableView().getItems().get(getIndex());
                            if(selected.getProgress()!= 100.00)
                                setGraphic(payButton);
                        }
                    }
                };
            }

        });
        
        Platform.runLater(() -> {
            requestWishlist();
            friendname.setText(friend.getFullname());
            String imagePath = friend.getUserphoto();
            Image userImage = new Image(imagePath);
            imageView.setImage(userImage);
            Circle defaultClip = createCircularClip();
            imageView.setClip(defaultClip);
        });

    }

    public void requestWishlist() {

        try {
            Gson gson = new Gson();
            JsonObject request = new JsonObject();
            request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.GET_FRIEND_WISHLIST));
            request.addProperty("data", gson.toJson(friend));
            MyConnection.getInstance().getOutputStream().println(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void waitForHandler(String msg) {
        Gson gson = new Gson();
        // Process the reply
        JsonObject reply = gson.fromJson(msg, JsonObject.class);

        if (reply.has("data")) {
            String arr = reply.get("data").getAsString();
            Type dataType = new TypeToken<ArrayList<WishList>>() {
            }.getType();
            receivedlist = gson.fromJson(arr, dataType);
            hiswishlist = FXCollections.observableArrayList(receivedlist);
        } else {
            System.out.println("No data found");
        }
        Platform.runLater(() -> {
            wishlistTable.setItems(hiswishlist);
        });
    }
    private Circle createCircularClip() {
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) * 0.5;
        Circle clip = new Circle();
        clip.setCenterX(imageView.getFitWidth() / 2);
        clip.setCenterY(imageView.getFitHeight() / 2);
        clip.setRadius(radius);
        return clip;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

}
