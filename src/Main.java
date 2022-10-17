package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {


        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("src/views/Maininterface.fxml")));
        stage.setTitle("Data Mining Project Part 1");
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);

    }
}


//todo refaire le rapport
//todo fix interface
//todo affichree la varance
//todo afficher les valeurs vides
//todo afficher les valeurs avc 1470 modes
