package main;

import database.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



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

    public int loginUser(String username, String password) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.DatabseInit();
        int flag = databaseConnector.checkUser(username, password);
        databaseConnector.connectionClose();
        return flag;
    }


    public static void main(String[] args) {
        launch(args);
    }
}