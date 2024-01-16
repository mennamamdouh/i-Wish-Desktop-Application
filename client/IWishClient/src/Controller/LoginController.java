package Controller;

import Connection.MyConnection;
import Connection.MessageProtocol;
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
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

public class LoginController implements Initializable {

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;
    
    private boolean start ;
    @FXML
    private TextField emailtf;
    @FXML
    private PasswordField passtf;
    @FXML
    private Pane mainnode;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Platform.runLater(() -> {
            Stage primaryStage = (Stage) (mainnode.getScene().getWindow());
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>(){
                @Override
                public void handle(WindowEvent event) {
                    if(MyConnection.getStatus())
                        try {
                            MyConnection.getInstance().closeConnection();
                    } catch (IOException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });}
        );
        
        btnSignup.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            try {
                Parent signupParent = FXMLLoader.load(getClass().getResource("/View/Signup.fxml"));
                Scene signupScene = new Scene(signupParent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setScene(signupScene);
                window.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        btnSignin.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            if(!start){
                Thread connect = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {            
                            MyConnection con = MyConnection.getInstance();
                            start= true;
                            String email =emailtf.getText() ;
                            String pass = passtf.getText() ;
                            User log = new User(email,pass);
                            if(login(log)){
                                Platform.runLater(() -> {
                                    try {           
                                        Parent homeParent = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
                                        Scene homeScene = new Scene(homeParent);
                                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                        window.setScene(homeScene);
                                        window.show();
                                    } catch (IOException ex) {
                                         Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                });
                            }
                            else{
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(AlertType.ERROR);
                                    alert.setContentText("Invalid Email or Password");
                                    alert.showAndWait();
                                    start =false;
                                    emailtf.setText("");
                                    passtf.setText("");
                                });
                            }
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
    public boolean login(User user) throws IOException{
        // Prepare Request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.RETRIEVAL.LOGIN));
        request.addProperty("data", gson.toJson(user));
        // Send
        MyConnection.getInstance().getOutputStream().println(request.toString());
        // Waiting for reply
        String msg = MyConnection.getInstance().getInputStream().readLine();
        // Process reply
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        if(reply.get("status").getAsBoolean())
        {
            //user = gson.fromJson(reply.get("data").getAsString(), User.class);
            return true;
        }
        else
          return false;
    }
}
