package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class Controller{

    Alert a = new Alert(Alert.AlertType.NONE);
    public Controller() {

    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField pass;


    @FXML
    private void Register(ActionEvent event) throws SQLException, IOException, Exception {
        Parent root = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 635, 400));
        window.show();
    }

    @FXML
    private void Login(ActionEvent event) throws SQLException, IOException, Exception {

        Arthur arthur = new Arthur();
        if ( arthur.loginUser(username.getText(), pass.getText()) == 1)
        {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You are logged in");
            a.show();
        } else {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Wrong name or password!");
            a.show();
        }



    }

}
