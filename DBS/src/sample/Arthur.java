package sample;

import Database.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;



public class Arthur extends Application{

    private Stage primaryStage;

    private DatabaseConnector dataConnector = new DatabaseConnector();
    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Arthur");
        primaryStage.setScene(new Scene(root, 635, 400));
        primaryStage.show();
        dataConnector.DatabseInit();

    }

    public void registration() throws Exception{
        sceneChange("registration.fxml");
    }


    public void sceneChange(String fxml) throws Exception, NullPointerException{

    }


    public static void main(String[] args) {
        launch(args);
    }
}