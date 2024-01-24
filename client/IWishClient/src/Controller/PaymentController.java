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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class PaymentController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private User friend;
    private WishList item;
    private boolean enough ;
    @FXML
    private Label hintlabel;
    @FXML
    private Tooltip tooltip ;
    @FXML
    private TextField nametxtfield;
    @FXML
    private TextField cardtextfield;
    @FXML
    private TextField cvvtextfield;
    @FXML
    private Button submitbtn;
    @FXML
    private AnchorPane cvvanchor;
    @FXML
    private Label cardnumber;
    private String defaultnumber = "0000   0000   0000   0000";
    @FXML
    private Label cardname;
    private String defaultname ="CARD HOLDER NAME";
    @FXML
    private Label carddate;
    private String defaultdate ="00/00";
    @FXML
    private DatePicker datepicker;
    @FXML
    private TextField amounttf;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ReceiverHandler.setPaymentcontroller(this);
        
        cardNumberDynamic();
        cardNameDynamic(); 
        cardCvvDynamic();
        amountCheck();
        datepicker.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                carddate.setText(datepicker.getValue().format(DateTimeFormatter.ofPattern("MM/yy")));
            }
        
        });
        submitbtn.setOnAction(event -> {
            if(cardnumber.getText().length() == 25 && cardname.getText().length() >= 10 && cvvtextfield.getText().length() == 3 && Integer.parseInt(amounttf.getText()) > 0 && !datepicker.getValue().toString().equals(null) )
                contribute();
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(cardnumber.getText().length() < 25)
                  alert.setContentText("Please Enter Your Card Number Corrrect !!");
                if(cardname.getText().length() <= 10)
                  alert.setContentText("Please Enter Your Name on Card Corrrect\n Ex : Diaa Ahmed");
                if(cvvtextfield.getText().length() == 3 )
                  alert.setContentText("CVV isn't correct ..");              
                alert.showAndWait();
            }
        });
        Platform.runLater(() -> {
            Stage stage = (Stage) (submitbtn.getScene().getWindow());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ReceiverHandler.getFriendprofilecontroller().requestWishlist();
                }
            });
        });
    }
    public void contribute() {
        try {
            Gson gson = new Gson();
            JsonObject request = new JsonObject();
            request.addProperty("request", gson.toJson(MessageProtocol.MODIFY.CONTRIBUTE));
            request.addProperty("friend_data", gson.toJson(friend));
            request.addProperty("item_data", gson.toJson(item.getItem()));
            if(Double.parseDouble(amounttf.getText()) > item.getRemaining()){
                 request.addProperty("amount", item.getRemaining());
                 enough = true;
            }
            else
                request.addProperty("amount", amounttf.getText());
            MyConnection.getInstance().getOutputStream().println(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(FriendProfileController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void waitForHandler(String msg ){
        Gson gson = new Gson();
        JsonObject reply = gson.fromJson(msg, JsonObject.class);
        if (reply.get("status").getAsBoolean()){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if(enough)
                    alert.setContentText("Thanks For Your Contribution !" + "\n Only " + item.getRemaining() + " was Enough <3" );
                else
                    alert.setContentText("Thanks For Your Contribution !");
                alert.showAndWait();
                Stage stage = (Stage) (submitbtn.getScene().getWindow());
                ReceiverHandler.getFriendprofilecontroller().requestWishlist();
                stage.close();
                // On close call friendsController function which request data to update view 
            });
        }else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error: Unable to process your request");
                ButtonType retrybtn = new ButtonType("Retry");
                alert.getButtonTypes().add(retrybtn);
                alert.showAndWait().ifPresent(response -> {
                    if (response == retrybtn) {
                        contribute();
                    }
                });
            });
        }
    
    }
    private void cardCvvDynamic(){
          cvvtextfield.setOnKeyTyped(new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent event) {
                    if(cvvtextfield.getText().length() >=3 || !Character.isDigit(event.getCharacter().charAt(0)))
                        event.consume();
                }});
    }
    private void amountCheck(){
          amounttf.setOnKeyTyped(new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent event) {
                    String total = amounttf.getText();
                    if(!Character.isDigit(event.getCharacter().charAt(0)) || total.length()+1 >6)
                        event.consume();
                }});
    }
    private void cardNumberDynamic(){
          cardtextfield.setOnKeyTyped(new EventHandler<KeyEvent>(){
                int num = 0;
                String space ="   ";
                @Override
                public void handle(KeyEvent event) {
                    char input = event.getCharacter().charAt(0);
                    if(input == '\b' && num !=0)
                    {   
                        if(cardtextfield.getText().endsWith(space)){
                            cardtextfield.deletePreviousChar();
                            cardtextfield.deletePreviousChar();
                            cardtextfield.deletePreviousChar();
                        }
                        num--;
                        return;
                    }
                    if( num <16)
                    {
                        if(Character.isDigit(input)){
                            if(num%4 == 0 & num !=0)
                                cardtextfield.appendText(space);
                            num++;
                            return;
                        }
                        event.consume();
                    }
                       else 
                           event.consume();
                }});
            
            cardnumber.textProperty().bind(Bindings.when(cardtextfield.textProperty().isEmpty())
                .then(defaultnumber)
                .otherwise(cardtextfield.textProperty()));
    
    }
    private void cardNameDynamic(){
           cardname.textProperty().bind(Bindings.when(nametxtfield.textProperty().isEmpty())
                .then(defaultname)
                .otherwise(nametxtfield.textProperty()));
            
            
            nametxtfield.textProperty().addListener(new ChangeListener(){
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    String temp =(String)newValue;
                    nametxtfield.setText(temp.toUpperCase());
                }
               });
            
            nametxtfield.setOnKeyTyped(new EventHandler<KeyEvent>(){
                private char previous = ' ';
                
                @Override
                public void handle(KeyEvent event) {
                    if(nametxtfield.getText().length() <18 ){
                        char typed = event.getCharacter().charAt(0);
                        int parts = nametxtfield.getText().split("\\s").length;
                        if(parts == 3 & Character.isWhitespace(typed))
                        {
                           event.consume();
                           return;
                        }
                        if( (Character.isWhitespace(typed) & Character.isWhitespace(previous) )||Character.isDigit(typed))
                        {
                            event.consume();
                            return;
                        }    
                        if(typed == '\b'&& nametxtfield.getText().endsWith(" "))
                            previous = ' ';
                        else
                            previous= typed;
                     
                }
                 else
                   event.consume();
                        }
            });
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public WishList getItem() {
        return item;
    }

    public void setItem(WishList item) {
        this.item = item;
    }    
    
}
