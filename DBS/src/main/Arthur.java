package main;

import database.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;


public class Arthur extends Application{

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {


        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("../main/sample.fxml"));
        primaryStage.setTitle("Arthur");
        primaryStage.setScene(new Scene(root, 635, 400));
        primaryStage.show();

    }

    public Pair<Integer, Integer> loginUser(String username, String password) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.DatabseInit();
        Pair<Integer,Integer> pair = databaseConnector.checkUser(username, password);
        databaseConnector.connectionClose();
        return pair;
    }


    public static void main(String[] args) {
        launch(args);
    }
}