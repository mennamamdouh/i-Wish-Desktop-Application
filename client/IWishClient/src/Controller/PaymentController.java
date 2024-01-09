/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class PaymentController implements Initializable {

    /**
     * Initializes the controller class.
     */
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cardNumberDynamic();
        cardNameDynamic(); 
        cardCvvDynamic();
        datepicker.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                carddate.setText(datepicker.getValue().format(DateTimeFormatter.ofPattern("MM/yy")));
            }
        
        });
    }
    private void cardCvvDynamic(){
          cvvtextfield.setOnKeyTyped(new EventHandler<KeyEvent>(){
                @Override
                public void handle(KeyEvent event) {
                    if(cvvtextfield.getText().length() >=3 || !Character.isDigit(event.getCharacter().charAt(0)))
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
    
}
