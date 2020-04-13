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
import sample.Character;
import sample.menu.MenuController;

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

    private int playerId;
    @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            race_id.getItems().addAll("Human","Maros","Goblin","Shemale");
            class_id.getItems().addAll("David","Warrior","Porn star");


    }

    public void initId(int id)
    {
        this.playerId = id;
    }
    public void characterCreation(javafx.event.ActionEvent event) {
        DatabaseConnector connector = new DatabaseConnector();
        connector.DatabseInit();

        Character character = new Character(character_name_creation.getText(),0,0,class_id.getValue(),
                race_id.getValue(),0,false, 0, playerId);

        connector.addCharacter(character.getChar_name(),character.getChar_class(),
                character.getRace(),playerId);
        try {
            connector.connectionClose();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../menu/main_screen.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);

            MenuController controller = loader.getController();
            controller.initData(playerId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println("IO error");
        }



    }
}
