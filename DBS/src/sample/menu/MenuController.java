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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Character;
import sample.characterCreation.CharacterCreationController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    TableView<ModelTable> tabView;
    @FXML
    TableColumn<ModelTable, String> name_id;
    @FXML
    TableColumn<ModelTable, String> level_id;
    @FXML
    TableColumn<ModelTable, String> id_table;
    @FXML
    Button createNewCharacter;
    @FXML
    TextField searchBar;
    @FXML
    Button btnSearch;
    @FXML
    Button btn_set_next_offset;
    @FXML
    Button btn_set_prev_offset;

    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    DatabaseConnector databaseConnector = new DatabaseConnector();

    private int offset;
    private int personID;
    private ResultSet resultSet;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseConnector.DatabseInit();
        offset = 0;

    }
    public void initData(int id)
    {
        this.personID = id;
        getCharacters(id);

    }
    private void getCharacters(int id)
    {
        try {
            resultSet = databaseConnector.getCharacters(id,offset);
        } catch (SQLException e)
        {
            System.out.println("Error while searching for characters");
        }

        fillTable(resultSet);
    }

    private void fillTable(ResultSet resSet)
    {

        name_id.setCellValueFactory(new PropertyValueFactory<>("name"));
        level_id.setCellValueFactory(new PropertyValueFactory<>("level"));
        id_table.setCellValueFactory(new PropertyValueFactory<>("id"));
        try
        {
            while (resSet.next())
            {
                oblist.add(new ModelTable(resSet.getString("character_name"), Integer.toString(resSet.getInt("character_xp")),
                        Integer.toString(resSet.getInt("character_id"))));
            }
        }
        catch (SQLException e)
        {

        }
        if (oblist == null)
            System.out.println("Prazdna tabulka");
        else
            tabView.setItems(oblist);
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
    public void searchInDatabase()
    {

        try {
            resultSet = databaseConnector.searchInTable(searchBar.getText());
            oblist.clear();
            while (resultSet.next() )
            {
                oblist.add(new ModelTable(resultSet.getString("character_name"), Integer.toString(resultSet.getInt("character_xp")),
                        Integer.toString(resultSet.getInt("character_id"))));
            }
        } catch (SQLException e)
        {
            System.out.println("SQL error");
        }

    }
    public void incrementOffset()
    {
        offset += 12;
        oblist.clear();
        getCharacters(personID);
    }

    public void decrementOffset()
    {
        offset -= 12;
        oblist.clear();
        getCharacters(personID);
    }

}
