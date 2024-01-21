package Controller;

import Connection.MyConnection;
import Connection.MessageProtocol;
import Connection.ReceiverHandler;
import Main.IWishClient;
import Model.User;
import com.google.gson.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

public class LoginController implements Initializable {

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    private boolean start;
    @FXML
    private TextField emailtf;
    @FXML
    private PasswordField passtf;
    @FXML
    private Pane mainnode;
    private Gson gson = new Gson();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ReceiverHandler.setLogincontroller(this);
        Platform.runLater(() -> {
            Stage primaryStage = (Stage) (mainnode.getScene().getWindow());
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (MyConnection.getStatus()) {
                        try {
                            MyConnection.getInstance().closeConnection();
                        } catch (IOException ex) {
                            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
        });

        btnSignup.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            try {
                Parent signupParent = FXMLLoader.load(getClass().getResource("/View/Signup.fxml"));
                Scene signupScene = new Scene(signupParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setTitle("iWish Sign-up");
                window.getIcons().add(new Image("/resources/genie-lamp-icon.png"));
                window.setScene(signupScene);
                window.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnSignin.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            if (!start) {
                Thread connect = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            start = true;
                            String email = emailtf.getText();
                            String pass = passtf.getText();
                            User log = new User(email, pass);
                            login(log);

                        } catch (IOException ex) {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setContentText("Failed to connect to the server !!");
                                alert.showAndWait();
                            });
                        }
                    }
                });
                connect.start();
            }
        });
    }

    public void login(User user) throws IOException {
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.LOGIN));
        request.addProperty("data", gson.toJson(user));
        MyConnection.getInstance().getOutputStream().println(request.toString());
    }

    public void waitForHandler(String msg) {
        boolean status = false;
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        if (reply.get("status").getAsBoolean())
            status= true;
        if (status) {
            String loggedInUser = reply.get("data").getAsString();
            IWishClient.user = gson.fromJson(loggedInUser, User.class);
            Platform.runLater(() -> {
                try {
                    Parent homeParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                    Scene homeScene = new Scene(homeParent);
                    Stage window = (Stage) (mainnode.getScene().getWindow());
                    window.setScene(homeScene);
                    window.show();
                }catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Invalid Email or Password");
                alert.showAndWait();
                start = false;
                emailtf.setText("");
                passtf.setText("");
            });
        }
    }
}
