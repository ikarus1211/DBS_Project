package menu;

import database.DatabaseConnector;
import guild.GuildController;
import inventory.InventoryController;
import inventory.ModelTab;
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
import leaderboard.LeaderboardController;
import quest.QuestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @FXML
    Button menuLeader;

    Alert a = new Alert(Alert.AlertType.NONE);
    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    DatabaseConnector databaseConnector = new DatabaseConnector();

    private int offset;
    private int personID;
    private int char_id;
    private int guild_id;
    private int serverID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseConnector.DatabseInit();
        offset = 0;

    }
    public void initData(int id, int serverID)
    {
        this.personID = id;
        this.serverID = serverID;
        getCharacters(id);

    }
    private void getCharacters(int id)
    {
        ArrayList<ArrayList<String>> aList = null;
        try {

            aList = databaseConnector.getCharacters(id,serverID,offset);

        } catch (SQLException e)
        {
            System.out.println("Error while searching for characters");
        }

        fillTable(aList);
    }

    private void fillTable(ArrayList<ArrayList<String>> aList)
    {
        name_id.setCellValueFactory(new PropertyValueFactory<>("name"));
        level_id.setCellValueFactory(new PropertyValueFactory<>("level"));
        id_table.setCellValueFactory(new PropertyValueFactory<>("id"));

        if (aList != null) {
            for (int k = 0; k < aList.size(); k++) {
                oblist.add(new ModelTable(aList.get(k).get(1),
                        aList.get(k).get(3),
                        aList.get(k).get(0)));
            }
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
            controller.initId(personID, serverID);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println("IO error");
        }

    }

    public void switchToQuests(javafx.event.ActionEvent event) {

        if (char_id == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Choose a character!");
            a.show();
            return;
        }

        databaseConnector.connectionClose();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../quest/quest.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            QuestController controller = loader.getController();
            controller.initId(char_id, personID, serverID);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public void switchToInventory(javafx.event.ActionEvent event) {
        if (char_id == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Choose a character!");
            a.show();
            return;
        }

        databaseConnector.connectionClose();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../inventory/inventory.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            InventoryController controller = loader.getController();
            controller.initId(char_id, personID, serverID);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println("IO error");
        }
    }


    public void switchToGuild(javafx.event.ActionEvent event) {
        if (char_id == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Choose a character!");
            a.show();
            return;
        }

        if (guild_id == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Character doesn't have a guild!");
            a.show();
            return;
        }

        databaseConnector.connectionClose();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../guild/guild.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            GuildController controller = loader.getController();
            controller.initId(char_id, guild_id, personID, serverID);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println(e);
        }
    }

    public void deleteCharacter() throws SQLException {

        if (personID == databaseConnector.checkCharacterOwner(char_id)) {
            databaseConnector.deleteCharacter(char_id, personID);
            oblist.clear();
            getCharacters(personID);
        } else {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You cannot delete someone else's character!");
            a.show();
        }

    }

    public void searchInDatabase()
    {

        try {
            ArrayList<ArrayList<String>> aList = databaseConnector.searchInTable(searchBar.getText(),serverID);
            oblist.clear();

            for (int k = 0; k < aList.size(); k++) {
                oblist.add(new ModelTable(aList.get(k).get(1),
                        aList.get(k).get(3),
                        aList.get(k).get(0)));
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
        ModelTable returned = tabView.getSelectionModel().getSelectedItem();

        if (returned != null) {
            try {
                ArrayList<ArrayList<String>> aList = databaseConnector.getOneCharacter(Integer.parseInt(returned.id));

                char_id = Integer.parseInt(aList.get(0).get(0));
                char_name.setText(aList.get(0).get(1));
                money_value.setText(aList.get(0).get(6));
                if (aList.get(0).get(8) != null) {
                    guild_id = Integer.parseInt(aList.get(0).get(8));
                    guild_name.setText(databaseConnector.getGuildName(Integer.parseInt(aList.get(0).get(8))));
                } else guild_name.setText("None");
                race_name.setText(aList.get(0).get(5));

                Character calc = new Character();
                MyResult retValue = calc.calculateLevel(Integer.parseInt(aList.get(0).get(3)));
                level_number.setText(Integer.toString(retValue.getFinalLevel()));
                progBarLevel.setProgress(retValue.getExp());

                FileInputStream imageStream = new FileInputStream(choseImage(aList.get(0).get(4)));
                Image im = new Image(imageStream);
                image.setImage(im);

            } catch (SQLException | FileNotFoundException | NullPointerException e) {
                System.out.println(e);
            }
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

    public void changeToLeaderboard(javafx.event.ActionEvent event)
    {
        databaseConnector.connectionClose();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../leaderboard/leaderboard.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);


            LeaderboardController controller = loader.getController();
            controller.setPersonId(personID, serverID);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println(e);
        }
    }

}