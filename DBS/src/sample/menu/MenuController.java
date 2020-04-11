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


    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    DatabaseConnector databaseConnector = new DatabaseConnector();

    private ResultSet resultSet;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseConnector.DatabseInit();
        resultSet = databaseConnector.getFromQuery("SELECT * FROM game_character");


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

    /*@FXML
    public void switchToCharCreation(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample/characterCreation/character_creation.fxml"));
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 635, 400));
        window.show();
    }*/

    public void switchToCharCreation(javafx.event.ActionEvent event) {
        databaseConnector.connectionClose();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../characterCreation/character_creation.fxml"));
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root, 635, 400));
            window.show();
        } catch (IOException e)
        {
            System.out.println("IO error");
        }

    }
}
