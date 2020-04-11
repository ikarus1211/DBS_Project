package sample.characterCreation;

import Database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CharacterCreationController implements Initializable {

    @FXML
    ChoiceBox<String> race_id;
    @FXML
    ChoiceBox<String> class_id;
    @FXML
    Button createBtn;
    @FXML
    TextField character_name_creation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        race_id.getItems().addAll("Human","Maros","Goblin","Shemale");
        class_id.getItems().addAll("David","Warrior","Porn star");


    }
    public void characterCreation(javafx.event.ActionEvent event) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        databaseConnector.DatabseInit();

        databaseConnector.addCharacter(character_name_creation.getText(), race_id.getValue(), class_id.getValue());
        databaseConnector.connectionClose();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../menu/main_screen.fxml"));
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root, 635, 400));
            window.show();
        } catch (IOException e)
        {
            System.out.println("IO error");
        }
    }
}
