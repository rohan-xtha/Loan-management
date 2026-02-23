package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Loading FXML from the resources folder
        URL url = new File("resources/main.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        
        // Load CSS
        File cssFile = new File("resources/application.css");
        if (cssFile.exists()) {
            root.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());
        }

        primaryStage.setTitle("Digital Borrow and Lend Tracker");
        primaryStage.setScene(new Scene(root, 1000, 700)); // Increased height slightly
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
