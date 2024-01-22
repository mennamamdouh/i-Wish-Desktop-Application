/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Connection.MessageProtocol;
import Connection.ReceiverHandler;
import Model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.cell.PropertyValueFactory;
import Connection.MyConnection;
import Main.IWishClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author attia
 */
public class WishListController implements Initializable {

    //defining the wishlist 
    ArrayList<WishList> myWishList = new ArrayList<>();
    @FXML
    private Button ProfilePictureButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Button btnClearMyWishlist;
    private ListView wishlistList;
    @FXML
    private Button SignOutButton;
    @FXML
    private Text txtFullName;
    @FXML
    private Text txtBirthDate;
    @FXML
    private Button searchItemsButton;
    @FXML
    private TableView<WishListItem> wishlistTable;
    @FXML
    private TableColumn<WishListItem, String> imageColumn;
    @FXML
    private TableColumn<WishListItem, String> nameColumn;
    @FXML
    private TableColumn<WishListItem, Double> priceColumn;
    @FXML
    private TableColumn<WishListItem, Double> progressColumn;

    private ObservableList<WishListItem> wishlist;

    private static final String DEFAULT_IMAGE_PATH = "file:src/Resources/blank.png";
    private static final double CIRCLE_CENTER = 0.5; // Center ratio for circular clipping
    @FXML
    private TableView<Contribution> contributionsTable;
    @FXML
    private TableColumn<Contribution, String> contributorColumn;
    @FXML
    private TableColumn<Contribution, Double> amountColumn;
    private ObservableList<Contribution> contlist;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ReceiverHandler.setWishListcontroller(this);
            getWishlist();
            Platform.runLater(() -> {
                txtFullName.setText(IWishClient.user.getFullname());
                txtBirthDate.setText(IWishClient.user.getDateOfBirth().toString());
            });
            
            signOutButtonHandler();
            addProfilePictureButtonHandler();
            
            Platform.runLater(() -> {                                   //New method <-----------------------------------
            String imagePath = IWishClient.user.getUserphoto();
            Image userImage = new Image(imagePath);
            //System.out.println(imagePath);
            imageView.setImage(userImage);
            Circle defaultClip = createCircularClip();
            imageView.setClip(defaultClip);

            });        

        btnClearMyWishlist.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {     //New <--------------------
            try {
                clearWishlist(IWishClient.user);
            } catch (IOException ex) {
                Logger.getLogger(WishListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
              
            // adding few items in the list
            Platform.runLater(() -> {
            initContribution();
            imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            
            imageColumn.setCellFactory(param -> new TableCell<WishListItem, String>() {
                private final ImageView imageView = new ImageView();
                
                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);
                    
            //        System.out.println("Image path: " + imagePath);
                    
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
            
            // Set the cell value factory for the nameColumn
            nameColumn.setCellValueFactory(
                    new PropertyValueFactory<>("name")
            );
            // Set the cell value factory for the priceColumn
            priceColumn.setCellValueFactory(
                    new PropertyValueFactory<>("price")
            );

            // Set the cell value factory for the progressColumn
            progressColumn.setCellValueFactory(
                    new PropertyValueFactory<>("progress")
            );

            // Set the cell value factory for the progressColumn
            progressColumn.setCellFactory(param -> new TableCell<WishListItem, Double>() {
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
                    setText(null);
                    setGraphic(null);
                } else {
                    // Update the ProgressBar with the current progress
                    progressBar.setProgress(progress / 100.0);
                    setText(String.format("%.1f%%", progress)); // Display the percentage
                }
            }
        });
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
        popup.getIcons().add(new Image("/resources/genie-lamp-icon.png"));
        popup.setTitle("iWish Items");
        popup.setScene(scene);
        popup.setTitle("Items");
        popup.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        getWishlist();
                    } catch (IOException ex) {
                        Logger.getLogger(WishListController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        popup.show();
        
    } catch (IOException ex) {
        Logger.getLogger(WishListController.class.getName()).log(Level.SEVERE, null, ex);
    }
});
        } catch (IOException ex) {
            Logger.getLogger(WishListController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     public void getWishlist() throws IOException {
        // Populate wishlist and contributions with data
        // Prepare the request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.GET_WISHLIST));

        // Send the request
        MyConnection.getInstance().getOutputStream().println(request.toString());
    }

    public void getWishListHandler(String msg){
        Gson gson = new Gson();
         // Process the reply
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        if(reply.has("data")){
            String arr = reply.get("data").getAsString();
            Type dataType = new TypeToken<ArrayList<WishList>>(){}.getType();
            ArrayList<WishList> itemList = gson.fromJson(arr, dataType);
            ArrayList<WishListItem> wList = new ArrayList<WishListItem>();
            for(WishList wl : itemList)
                wList.add(new WishListItem(wl.getItem().getItemid(),wl.getItem().getItemphoto(),wl.getItem().getItemname(),wl.getItem().getPrice(),wl.getProgress()));
            
            wishlist = FXCollections.observableArrayList(wList);
            Platform.runLater(() -> {
               wishlistTable.setItems(wishlist);  
            });
        }
        else{
            System.out.println("No data found");
        }
    }
    private void setupDefaultImage() {
        Image defaultImage = new Image("file:src/Resources/gift.jpg");
        imageView.setImage(defaultImage);

        Circle defaultClip = createCircularClip();
        imageView.setClip(defaultClip);
    }

    private void signOutButtonHandler() {
        SignOutButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            try {
                Parent signinParent = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
                Scene signinScene = new Scene(signinParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(signinScene);
                window.show();
                MyConnection.getInstance().closeConnection();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private void clearWishlist(User user) throws IOException {
        // Prepare the request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.CLEAR_WISHLIST));
        request.addProperty("data", gson.toJson(user));
                
        // Send the request
        MyConnection.getInstance().getOutputStream().println(request.toString());
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
        btnClearMyWishlist.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
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

    private void initContribution(){
      contributorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFriend().getFullname()));
      amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );  
      wishlistTable.setRowFactory(new Callback<TableView<WishListItem>, TableRow<WishListItem>>() {
            @Override
            public TableRow<WishListItem> call(TableView<WishListItem> tv) {
                TableRow<WishListItem> itemRow = new TableRow<>();
                itemRow.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                          public void handle(MouseEvent event) {
                         if (!itemRow.isEmpty()) {
                             WishListItem item = itemRow.getItem();
                             getContribution(new Item(item.getItemid()));
                        }
                    }
                });                
                
                return itemRow ;
            }
        });
    }
     public void getContribution(Item choosen){
        try {
            Gson gson = new Gson();
            JsonObject request = new JsonObject();
            request.addProperty("request",gson.toJson(MessageProtocol.RETRIEVAL.GET_CONTRIBUTION));
            request.addProperty("data",gson.toJson(choosen));
            MyConnection.getInstance().getOutputStream().println(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(WishListController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    public void getContributionHandler(String msg){
        Gson gson = new Gson();
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        if(reply.has("data")){
            String arr = reply.get("data").getAsString();
            Type dataType = new TypeToken<ArrayList<Contribution>>(){}.getType();
            ArrayList<Contribution> contributionList = gson.fromJson(arr, dataType);
            contlist = FXCollections.observableArrayList(contributionList);
            Platform.runLater(() -> {     
               contributionsTable.setItems(contlist);  
            });
        }
    }
}
// items class to add few items to the wishlist

