package Controller;

import Connection.MessageProtocol;
import Connection.MyConnection;
import Connection.ReceiverHandler;
import Main.IWishClient;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date; // Import java.sql.Date for Date class
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 */
public class SignupController implements Initializable {

    private Label label;

    @FXML
    private Button RegisterButton;

     @FXML
    private Button BackToLoginButton;
     
      @FXML
    private Button ChoosePhotoButton;
      
    private boolean start;

    @FXML
    private TextField NameField;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField PasswordField;

    @FXML
    private DatePicker DateOfBirthField;
    @FXML
    private AnchorPane mainnode;
    @FXML
    private Label RegisterLabel;

    private String selectedPhotoPath;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ReceiverHandler.setSignupcontroller(this);
        RegisterButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            if (!start) {
          
            
                Thread connect = new Thread(() -> {
                    try {
                        MyConnection con = MyConnection.getInstance();
                        start = true;
                                String email = EmailField.getText();
        
        // Check if the email contains "@" sign
        if (!email.contains("@") || !email.contains(".com")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid email format. Please enter a valid email address.");
                alert.showAndWait();
            });
        }else{
             String pass = PasswordField.getText();
                        String name = NameField.getText();
                        String PhotoPath = getSelectedFilePath();
                        java.sql.Date sqlDate = null;
                        LocalDate localDate = DateOfBirthField.getValue();

                        if (localDate != null) {
                            // Convert JavaFX LocalDate to java.sql.Date
                            sqlDate = Date.valueOf(localDate);
                        }

                        IWishClient.user = new User(email,name, pass,PhotoPath , sqlDate);
                        Register(IWishClient.user);}
                   
                    } catch (IOException ex) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Failed to connect to the server!!");
                            alert.showAndWait();
                        });
                    }
                });
                connect.start();
            }
        });
         BackToLoginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    Parent signupParent = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
                    Scene signupScene = new Scene(signupParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(signupScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
          ChoosePhotoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose an Image File");
               
                // Now 'substring' contains the path starting from "resources"
           
                // Set the extension filter for image files
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
                fileChooser.getExtensionFilters().add(extFilter);
                // Show open dialog
                File selectedFile = fileChooser.showOpenDialog(new Stage());
                if (selectedFile != null) {
                    selectedPhotoPath = selectedFile.getAbsolutePath();
                    int indexOfResourcesString = selectedPhotoPath.indexOf("resources");
                    selectedPhotoPath = selectedPhotoPath.substring(indexOfResourcesString).replace("\\", "/");
                    System.out.println("Selected Image File: " + selectedPhotoPath);
                    // You can now use the 'selectedFilePath' variable as needed
                }
            }
        });
         
    }

     public String getSelectedFilePath() {
        return selectedPhotoPath;
    }
    public void Register(User user) throws IOException {
        // Prepare Request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.REGISTER));
        request.addProperty("data", gson.toJson(user));
        // Send
        MyConnection.getInstance().getOutputStream().println(request.toString());
    }

    public void waitForHandler(String msg) {
        // Process reply
        Gson gson = new Gson();
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        boolean status = reply.get("status").getAsBoolean();
        if (status) {
            Platform.runLater(() -> {
                try {
                    Parent homeParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                    Scene homeScene = new Scene(homeParent);
                    Stage window = (Stage) (mainnode.getScene().getWindow());
                    window.setScene(homeScene);
                    window.show();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Your email already exists, login please.");
                alert.setHeaderText("Invalid email!");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/resources/genie-lamp-icon.png").toString()));
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK){
                        try {
                            Parent homeParent = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
                            Scene homeScene = new Scene(homeParent);
                            Stage window = (Stage) (mainnode.getScene().getWindow());
                            window.setScene(homeScene);
                            window.show();
                        } catch (IOException ex) {
                            Logger.getLogger(FriendsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                start = false;
            });
       }
    }
}