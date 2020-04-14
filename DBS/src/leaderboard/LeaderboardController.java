package leaderboard;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import menu.MenuController;


import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {
    @FXML
    ChoiceBox lbChoiceBox;
    @FXML
    Button lbBtnBack;
    @FXML
    TableView<LbModelTable> lbTable;
    @FXML
    TableColumn<LbModelTable,String> lbFirstCol;
    @FXML
    TableColumn<LbModelTable,String> lbSecondCol;
    @FXML
    TableColumn<LbModelTable,String> lbThirdCol;
    @FXML
    TableColumn<LbModelTable,String> lbFourthCol;
    @FXML
    TableColumn<LbModelTable,String> lbFifthCol;
    @FXML
    Button lbNext;
    @FXML
    Button lbPrev;

    private int personId;
    private database.DatabaseConnector connector;
    private int offset;
    ObservableList<LbModelTable> oblist = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbChoiceBox.getItems().addAll("Players");
        connector = new DatabaseConnector();
        connector.DatabseInit();
        offset = 0;
    }

    public void setPersonId(int id)
    {
        this.personId = id;
    }

    public void selectFilter()
    {
        String selected = lbChoiceBox.getValue().toString();
        System.out.println(selected);
        if (selected.equals("Players"))
            runPlayerFilter();
    }
    public void runPlayerFilter()
    {
        lbFirstCol.setText("Player");
        lbSecondCol.setText("AVG Experience");
        lbThirdCol.setText("Best character");
        lbFourthCol.setText("NoCharacters");
        lbFifthCol.setText("Character XP");
        lbFirstCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        lbSecondCol.setCellValueFactory(new PropertyValueFactory<>("avgExperience"));
        lbThirdCol.setCellValueFactory(new PropertyValueFactory<>("bestCharacter"));
        lbFourthCol.setCellValueFactory(new PropertyValueFactory<>("numberCharacter"));
        lbFifthCol.setCellValueFactory(new PropertyValueFactory<>("bestExperience"));
        try {
            ResultSet resultSet = connector.bestPlayers(offset);
            while (resultSet.next())
            {
                oblist.add(new LbModelTable(resultSet.getString("player_name"),
                        resultSet.getString("character_name"),
                        resultSet.getFloat("AvgExperience"),
                        resultSet.getInt("NoCharacters"),
                        resultSet.getInt("character_xp")));
            }
        }catch (SQLException e)
        {
            System.out.println(e);
        }
        lbTable.setItems(oblist);
    }

    public void goBack(javafx.event.ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../menu/main_screen.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);


            MenuController controller = loader.getController();
            controller.initData(personId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e)
        {
            System.out.println(e);
        }
    }
    public void incrementOffset()
    {
        offset += 100;
        oblist.clear();
        runPlayerFilter();
    }

    public void decrementOffset()
    {
        offset -= 100;
        oblist.clear();
        runPlayerFilter();
    }

}
