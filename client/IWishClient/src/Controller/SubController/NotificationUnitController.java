/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.SubController;

import Connection.MessageProtocol;
import Connection.MyConnection;
import Controller.NotificationController;
import Model.Notification;
import Model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class NotificationUnitController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Notification unit;
    private VBox parentVBox;
    private Node myself;
    @FXML
    private Button acceptbtn;
    @FXML
    private Label message;
    @FXML
    private Button denybtn;
    @FXML
    private ImageView showimg;
    @FXML
    private AnchorPane parentnode;
    Gson gson = new Gson();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            message.setText(unit.getNotification());
            if (!unit.getNotificationtype().equals("REQUEST_SENT")) {
                parentnode.getChildren().remove(denybtn);
                parentnode.getChildren().remove(acceptbtn);
                message.setPrefWidth(850);
            }
        });
    }

    @FXML
    public void onDenyClick() {
        try {
            JsonObject request = new JsonObject();
            request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.DENY_REQUEST));
            request.addProperty("data", gson.toJson(new User(unit.getFriendid())));
            NotificationController.currnode = myself;
            MyConnection.getInstance().getOutputStream().println(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(NotificationUnitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void onAcceptClick() {
        try {
            JsonObject request = new JsonObject();
            request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.ACCEPT_FRIEND));
            request.addProperty("data", gson.toJson(new User(unit.getFriendid())));
            NotificationController.currnode = myself;
            MyConnection.getInstance().getOutputStream().println(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(NotificationUnitController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptAndDenyHandler(String msg) {
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        if (reply.get("status").getAsBoolean()) {
            Platform.runLater(() -> {
                parentVBox.getChildren().remove(myself);
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error: Unable to process your request");
                ButtonType retrybtn = new ButtonType("Retry");
                alert.getButtonTypes().add(retrybtn);
                alert.showAndWait().ifPresent(response -> {
                    if (response == retrybtn) {
                        onAcceptClick();
                    }
                });
            });
        }
    }

    public void setIdentity(VBox parentVBox, Node myself) {
        this.parentVBox = parentVBox;
        this.myself = myself;
    }

    // Styling
    @FXML
    public void mouseHover(MouseEvent event) {
        Button firedbtn = (Button) event.getSource();
        setStyleRegex(firedbtn, "-10");
    }

    @FXML
    public void mouseRelease(MouseEvent event) {
        Button firedbtn = (Button) event.getSource();
        setStyleRegex(firedbtn, "0");
    }

    @FXML
    public void mousePress(MouseEvent event) {
        Button firedbtn = (Button) event.getSource();
        setStyleRegex(firedbtn, "-30");
    }

    private void setStyleRegex(Button btn, String degree) {
        if (btn.getText().equals("Accept")) {
            String style = btn.getStyle().replaceAll("-fx-background-color[^;]*", "-fx-background-color: derive(#00c732," + degree + "%)");
            btn.setStyle(style);
        } else {
            String style = btn.getStyle().replaceAll("-fx-background-color[^;]*", "-fx-background-color: derive(#c41202," + degree + "%)");
            btn.setStyle(style);
        }
    }

    public void setUnit(Notification unit) {
        this.unit = unit;
    }
}
