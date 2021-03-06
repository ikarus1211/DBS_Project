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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import menu.MenuController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @FXML
    RadioButton radioSer1;
    @FXML
    RadioButton radioSer2;
    @FXML
    Button searchButton;


    private int personId;
    private database.DatabaseConnector connector;
    private int offset;
    private int server = 1;
    private int serverID;
    ObservableList<LbModelTable> oblist = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbChoiceBox.getItems().addAll("Players", "Guilds","Characters");
        connector = new DatabaseConnector();
        connector.DatabseInit();
        offset = 0;
    }
    public void filter()
    {
        offset = 0;
        oblist.clear();
        if (radioSer2.isSelected()) {
            server = 2;
        }
        else server = 1;
        String selected = selectFilter();

        if (selected.equals("Players")) {
            runPlayerFilter();
        }
        else if (selected.equals("Guilds")) {
            runGuildsFilter();
        } else if (selected.equals("Characters"))
        {
            runBestPlayerFilter();
        }

    }
    public void setPersonId(int id, int serverID)
    {
        this.personId = id;
        this.serverID = serverID;
    }

    public String selectFilter()
    {

        String selected = lbChoiceBox.getValue().toString();
        if (selected == null)
            return "Players";
        return selected;
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
            ArrayList<ArrayList<String>> aList = connector.bestPlayers(offset, server);
            if (aList != null) {

                for (int i = 0; i < aList.size(); i++) {
                    oblist.add(new LbModelTable(aList.get(i).get(0),
                            aList.get(i).get(1),
                            Float.valueOf(aList.get(i).get(2)),
                            Integer.parseInt(aList.get(i).get(3)),
                            Integer.parseInt(aList.get(i).get(4))));

                }
            }
        }catch (SQLException e)
        {
            System.out.println(e);
        }
        lbTable.setItems(oblist);
    }


    public void runGuildsFilter(){
        lbFirstCol.setText("Guild's name");
        lbSecondCol.setText("Number of players");
        lbThirdCol.setText("");
        lbFourthCol.setText("Total amount of money");
        lbFifthCol.setText("");

        lbFirstCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        lbSecondCol.setCellValueFactory(new PropertyValueFactory<>("avgExperience"));
        lbThirdCol.setCellValueFactory(new PropertyValueFactory<>("bestCharacter"));
        lbFourthCol.setCellValueFactory(new PropertyValueFactory<>("numberCharacter"));
        lbFifthCol.setCellValueFactory(new PropertyValueFactory<>("bestExperience"));

        try {
            ArrayList<ArrayList<String>> aList = connector.bestGuild(offset, server);

            if (aList != null) {
                for (int i = 0; i < aList.size(); i++) {
                    oblist.add(new LbModelTable(aList.get(i).get(0),
                            "",
                            Float.valueOf(aList.get(i).get(1)),
                            Integer.parseInt(aList.get(i).get(2)),
                            0));
                }
            }
        }catch(SQLException e)
        {
            System.out.println(e);
        }
        lbTable.setItems(oblist);
    }


    public void runBestPlayerFilter(){
        lbFirstCol.setText("Player name");
        lbSecondCol.setText("Hours played");
        lbThirdCol.setText("Character name");
        lbFourthCol.setText("Character money");
        lbFifthCol.setText("Total amount");

        lbFirstCol.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        lbSecondCol.setCellValueFactory(new PropertyValueFactory<>("avgExperience"));
        lbThirdCol.setCellValueFactory(new PropertyValueFactory<>("bestCharacter"));
        lbFourthCol.setCellValueFactory(new PropertyValueFactory<>("numberCharacter"));
        lbFifthCol.setCellValueFactory(new PropertyValueFactory<>("bestExperience"));

        try {
            ArrayList<ArrayList<String>> aList = connector.getBestPlayers(offset, server);

            if (aList != null) {
                for (int i = 0; i < aList.size(); i++) {
                    oblist.add(new LbModelTable(aList.get(i).get(0),
                            aList.get(i).get(1),
                            Float.valueOf(aList.get(i).get(6)),
                            Integer.parseInt(aList.get(i).get(4)),
                            Integer.parseInt(aList.get(i).get(5))));
                }
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
            controller.initData(personId, serverID);

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
        String selected = lbChoiceBox.getValue().toString();
        offset += 100;
        oblist.clear();
        if (selected.equals("Players")) {
            runPlayerFilter();
        } else if (selected.equals("Characters"))
        {
            runBestPlayerFilter();
        }
        else runGuildsFilter();
    }

    public void decrementOffset()
    {
        String selected = lbChoiceBox.getValue().toString();
        offset -= 100;
        oblist.clear();
        if (selected.equals("Players")) {
            runPlayerFilter();
            }
        else if (selected.equals("Characters"))
        {
            runBestPlayerFilter();
        }
        else runGuildsFilter();
        }

}
