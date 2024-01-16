package Controller;

import Connection.MessageProtocol;
import Connection.MyConnection;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.sql.Date; // Import java.sql.Date for Date class
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 */
public class SignupController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Button RegisterButton;

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
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        RegisterButton.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            if (!start) {
                Thread connect = new Thread(() -> {
                    try {
                        MyConnection con = MyConnection.getInstance();
                        start = true;
                        String email = EmailField.getText();
                        String pass = PasswordField.getText();
                        String name = NameField.getText();

                        java.sql.Date sqlDate = null;
                        LocalDate localDate = DateOfBirthField.getValue();

                        if (localDate != null) {
                            // Convert JavaFX LocalDate to java.sql.Date
                            sqlDate = Date.valueOf(localDate);
                        }
                        
                        User user = new User(name, email, pass, sqlDate);
                        if (Register(user)) {
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
                        } else {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Invalid date, try again");
                                alert.showAndWait();
                                start = false;
                                NameField.setText("");
                                EmailField.setText("");
                                PasswordField.setText("");
                            });
                        }
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
    }

    public boolean Register(User user) throws IOException {
        System.out.println("goooo");
        // Prepare Request
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.REGISTER));
        request.addProperty("data", gson.toJson(user));
        // Send
        MyConnection.getInstance().getOutputStream().println(request.toString());
        // Waiting for reply
        String msg = MyConnection.getInstance().getInputStream().readLine();
        // Process reply
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        return reply.get("status").getAsBoolean();
    }
}
