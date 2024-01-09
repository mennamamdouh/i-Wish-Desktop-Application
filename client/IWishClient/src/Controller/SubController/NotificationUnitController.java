/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.SubController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private VBox parentVBox;
    private Node myself;
    @FXML
    private Button acceptbtn;
    @FXML
    private Label friendname;
    @FXML
    private Label message;
    @FXML
    private Button denybtn;
    @FXML
    private ImageView showimg;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    public void onDenyClick(){
        parentVBox.getChildren().remove(myself);
    }

    public void setIdentity(VBox parentVBox , Node myself) {
        this.parentVBox = parentVBox;
        this.myself = myself;
    }
    // Styling
    @FXML
    public void mouseHover(MouseEvent event){
        Button firedbtn = (Button)event.getSource();
        setStyleRegex(firedbtn , "-10");
    }    
    @FXML
    public void mouseRelease(MouseEvent event){
        Button firedbtn = (Button)event.getSource();
        setStyleRegex(firedbtn , "0");
    }  
    @FXML
    public void mousePress(MouseEvent event){
        Button firedbtn = (Button)event.getSource();
        setStyleRegex(firedbtn , "-30");
    } 
    private void setStyleRegex(Button btn , String degree){
        if(btn.getText().equals("Accept")){
            String style = btn.getStyle().replaceAll("-fx-background-color[^;]*", "-fx-background-color: derive(#00c732,"+degree+"%)");
            btn.setStyle(style);  
        }else{
            String style = btn.getStyle().replaceAll("-fx-background-color[^;]*", "-fx-background-color: derive(#c41202,"+degree+"%)");
            btn.setStyle(style);  
        }
    }
    
}
