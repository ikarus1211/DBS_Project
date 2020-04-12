package sample.menu;


import Database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Character;
import sample.characterCreation.CharacterCreationController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    TableView<ModelTable> tableView;
    TableColumn<ModelTable, String> name_id;
    TableColumn<ModelTable, String> level_id;

    @FXML
    Button createNewCharacter;
    @FXML
    TextField searchBar;

    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    DatabaseConnector databaseConnector = new DatabaseConnector();

    private int personID;
    private ResultSet resultSet;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseConnector.DatabseInit();

    }
    public void initData(int id)
    {
        searchBar.setText(Integer.toString(id));
        this.personID = id;


    }
    private void getCharacters(int id)
    {
        
    }

    private void fillTable(ResultSet resSet)
    {
        try
        {
            while (resSet.next())
            {
                oblist.add(new ModelTable(resSet.getString("character_name"), resSet.getInt("character_name")));
            }
        }
        catch (SQLException e)
        {

        }
        tableView.setItems(oblist);
    }

    public void switchToCharCreation(javafx.event.ActionEvent event) {

        databaseConnector.connectionClose();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../characterCreation/character_creation.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);

            CharacterCreationController controller = loader.getController();
            controller.initId(personID);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println("IO error");
        }

    }

}
