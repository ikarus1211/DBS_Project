package menu;


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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import character.Character;
import characterCreation.CharacterCreationController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    @FXML
    Label char_name;
    @FXML
    Button btn_inventory;
    @FXML
    Button btn_quest;
    @FXML
    Label money_value;
    @FXML
    Label level_number;
    @FXML
    Label guild_name;
    @FXML
    Label guild_member;
    @FXML
    Label race_name;
    @FXML
    ProgressBar progBarLevel;
    @FXML
    ImageView image;


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

    public void detailedView()
    {
        ResultSet newResultSet;
        ModelTable returned = tabView.getSelectionModel().getSelectedItem();
        try {
            newResultSet =  databaseConnector.getOneCharacter(Integer.parseInt(returned.id));
            if (newResultSet.next())
            {
                char_name.setText(newResultSet.getString("character_name"));
                money_value.setText(Integer.toString(newResultSet.getInt("game_money")));
                guild_name.setText(Integer.toString(newResultSet.getInt("guild_id")));
                race_name.setText(newResultSet.getString("race"));
                Character calc = new Character();
                MyResult retValue = calc.calculateLevel(newResultSet.getInt("character_xp"));
                level_number.setText(Integer.toString(retValue.getFinalLevel()));
                progBarLevel.setProgress(retValue.getExp());
                FileInputStream imageStream = new FileInputStream(choseImage(newResultSet.getString("class")));
                Image im = new Image (imageStream );
                image.setImage(im);

            }


        }
        catch (SQLException | FileNotFoundException e)
        {
            System.out.println("SQL error");
        }

    }
    private String choseImage(String charClass)
    {
        if (charClass.equals("Warrior"))
            return "DBS/src/img/warior.png";
        if (charClass.equals("Paladin"))
            return "DBS/src/img/paladin.png";
        if (charClass.equals("Rogue"))
            return "DBS/src/img/rogue.png";
        if (charClass.equals("Mage"))
            return "DBS/src/img/mage.png";
        if (charClass.equals("Hunter"))
            return "DBS/src/img/hunter.png";
        return null;
    }

}
