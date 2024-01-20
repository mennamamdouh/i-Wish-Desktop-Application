package Main;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage Stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ServerGUI.fxml")); 
        Scene scene = new Scene(root);
        Stage.setScene(scene);
        Stage.show();
        
       
    }
}
