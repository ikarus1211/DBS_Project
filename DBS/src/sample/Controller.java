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
import sample.menu.MenuController;

import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.sql.SQLException;


public class Controller{

    /*
     * Pop up window for displaying messages
     */
    Alert a = new Alert(Alert.AlertType.NONE);
    public Controller() {

    }

    @FXML
    private TextField username;

    @FXML
    private PasswordField pass;

    /*
     * Function changes the scene for registration
     */

    @FXML
    private void Register(ActionEvent event) throws SQLException, IOException, Exception {
        Parent root = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 635, 400));
        window.show();
    }

    /*
     * Functions which gets string that user puts in and then calls function for login.
     * After login is done it displays alert based on value that was returned
     */
    @FXML
    private void Login(ActionEvent event) throws IOException {

        Arthur arthur = new Arthur();
        int id = arthur.loginUser(username.getText(), pass.getText());
        if (id > 0)
        {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu/main_screen.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);

            MenuController controller = loader.getController();
            controller.initData(id);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } else {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Wrong name or password!");
            a.show();
        }


    }

}
