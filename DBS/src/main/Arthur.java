package main;

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

    @Override
    public void start(Stage primaryStage) throws Exception{

        /*
         * This displays Login GUI which is designed in sample.fxml
         */
        this.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Arthur");
        primaryStage.setScene(new Scene(root, 635, 400));
        primaryStage.show();

    }
    /*
     *  This function opens connection into database. Then checks the user credentials
     * and returns value which indicates if login was successful
     */
    public int loginUser(String username, String password) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.DatabseInit();
        int flag = databaseConnector.checkUser(username, password);
        databaseConnector.connectionClose();
        return flag;
    }


    public void sceneChange(String fxml) throws Exception, NullPointerException{

    }


    public static void main(String[] args) {
        launch(args);
    }
}