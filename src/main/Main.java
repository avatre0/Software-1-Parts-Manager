package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App Entry Class to start the Inventory Application.
 */
public class Main extends Application {
    /**
     * Creates the stage and loads the initial scene.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        primaryStage.setTitle("Inventory");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * App entry.
     * JavaDoc is located at $programPath/JavaDco
     * @param args the command line arguments.
     */
    public static void main(String[] args){
        launch(args);
    }
}
