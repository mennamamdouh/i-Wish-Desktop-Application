/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author attia
 */
public class AboutController implements Initializable {

    @FXML
    private ImageView presentImage;
    @FXML
    private ImageView diaaImage;
    @FXML
    private ImageView mennaImage;
    @FXML
    private ImageView hatemImage;
    @FXML
    private ImageView omarImage;
    @FXML
    private ImageView abdullahImage;
    
    private static final double CIRCLE_CENTER = 0.5; // Center ratio for circular clipping
    @FXML
    private Circle diaaCircle;
    @FXML
    private Circle mennaCircle;
    @FXML
    private Circle hatemCircle;
    @FXML
    private Circle omarCircle;
    @FXML
    private Circle abdullahCircle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Circle diaaCircle = createCircularClip();
        Image diaa = new Image("resources/diaa.jpg");
        diaaImage.setImage(diaa);
        diaaImage.setClip(diaaCircle);
        
        Circle mennaCircle = createCircularClip();
        Image menna = new Image("resources/menna.jpeg");
        mennaImage.setImage(menna);
        mennaImage.setClip(mennaCircle);
        
        Circle hatemCircle = createCircularClip();
        Image hatem = new Image("resources/hatem.jpg");
        hatemImage.setImage(hatem);
        hatemImage.setClip(hatemCircle);
        
        Circle omarCircle = createCircularClip();
        Image omar = new Image("resources/omar.jpg");
        omarImage.setImage(omar);
        omarImage.setClip(omarCircle);
        
        Circle abdullahCircle = createCircularClip();
        Image abdullah = new Image("resources/male-avatar.png");
        abdullahImage.setImage(abdullah);
        abdullahImage.setClip(abdullahCircle);
    }
    
    private Circle createCircularClip() {
        double radius = Math.min(150, 130) * CIRCLE_CENTER;
        Circle clip = new Circle();
        clip.setCenterX(150 / 2);
        clip.setCenterY(130 / 2);
        clip.setRadius(radius);
        return clip;
    }
    
}
