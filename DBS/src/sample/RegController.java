package sample;

import Database.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegController {

    @FXML
    public TextField username;

    @FXML
    private PasswordField pass;

    @FXML
    private PasswordField passCheck;

    @FXML
    private TextField email;

    @FXML
    public void register(ActionEvent event) throws IOException {

        DatabaseConnector dataCon = new DatabaseConnector();
        dataCon.DatabseInit();
        Alert a = new Alert(AlertType.NONE);

        String name = username.getText();
        String passw = pass.getText();
        String passwC = passCheck.getText();
        String mail = email.getText();

        if (passw.equals(passwC) && (!name.equals(""))) {
            dataCon.addUser(passw, name, mail);
            a.setAlertType(AlertType.INFORMATION);
            a.setHeaderText("Account has been created successfully!");
            a.show();
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root, 635, 400));
            window.show();
        }
        else {
            a.setAlertType(AlertType.WARNING);
            a.setHeaderText("Passwords don't match!");
            a.show();
        }
    }

    @FXML
    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 635, 400));
        window.show();
    }
}
