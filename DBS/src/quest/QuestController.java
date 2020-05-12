package quest;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import menu.MenuController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QuestController {


    @FXML
    private ListView<String> list;

    @FXML
    private Label head_lbl = null;

    @FXML
    private Button bckBtn;

    @FXML
    private Label description_lbl = null;

    @FXML
    private Button newQuest;

    @FXML
    private Button showActive;

    @FXML
    private Button getNew_or_deleteOld;

    @FXML
    private Label rewards_lbl = null;

    @FXML
    private Label location_lbl = null;


    private Alert a = new Alert(Alert.AlertType.NONE);
    private ObservableList<String> quests = FXCollections.observableArrayList();
    private ObservableList<String> newQuests = FXCollections.observableArrayList();
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private int iter = 0;
    private int personId;
    private int char_id;
    private List<Integer> rs_id = null;
    private String rs_name = null;
    private String new_quest;
    private int selected_quest_id;


    public void initId(int c_id, int p_id)
    {
        this.char_id = c_id;
        this.personId = p_id;
        getQuests(c_id);
    }

    public void getQuests(int id) {
        iter = 0;
        quests.clear();
        try {
            databaseConnector.DatabseInit();
            rs_id = databaseConnector.getQuests(id);

            for (Integer i: rs_id) {
                iter++;
                rs_name = databaseConnector.getNameOfQuest(i);
                quests.add(rs_name);
            }
            if (iter == 0) {
                quests.add("There are no active quests.");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        list.setItems(quests);
    }

    public void showQuestDetail() {
        try {
            ArrayList<ArrayList<String>> aList = databaseConnector.getQuestInfo(list.getSelectionModel().getSelectedItem());
            selected_quest_id = Integer.parseInt(aList.get(0).get(0));
            String desc = aList.get(0).get(5);
            String loc = databaseConnector.getLocation(Integer.parseInt(aList.get(0).get(6)));
            String rew = aList.get(0).get(3) + " gold        " + aList.get(0).get(4) + " XP";

            if (desc != null) {
                description_lbl.setText("Description: \n" + desc);
            }
            if (location_lbl != null) {
                location_lbl.setText("Location: " + loc);
            }
            rewards_lbl.setText("Rewards: " + rew);
        } catch (IndexOutOfBoundsException | SQLException e) {

        }
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

    public void getNewQuest(javafx.event.ActionEvent event) throws SQLException{

        if (personId != databaseConnector.checkCharacterOwner(char_id)) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You cannot modify someone else's character!");
            a.show();
            return;
        }

        if (iter > 3) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You have maximum number of quest!");
            a.show();
            return;
        }
        getNew_or_deleteOld.setText("Take that!");
        head_lbl.setText("Choose a new quest");
        if (newQuests.size() < 2) {
            for (int i = 0; i < 3; i++) {
                Random r = new Random();
                int randomQ = r.nextInt(7500);

                try {
                    new_quest = databaseConnector.getNameOfQuest(randomQ);
                    newQuests.add(new_quest);
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
        list.setItems(newQuests);
    }

    public void showActive() {
        description_lbl.setText("");
        rewards_lbl.setText("");
        location_lbl.setText("");
        getNew_or_deleteOld.setText("Get rid of this!");
        getQuests(char_id);
    }

    public void getNew_or_deleteOld() throws SQLException{
        if (selected_quest_id == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Choose a quest!");
            a.show();
            return;
        }

        if (personId != databaseConnector.checkCharacterOwner(char_id)) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You cannot modify someone else's character!");
            a.show();
            return;
        }

        // ziskanie noveho
        if (getNew_or_deleteOld.getText().equals("Take that!")) {
            try {
                if(databaseConnector.addQuest(selected_quest_id, char_id) == -1) {
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setHeaderText("You already have this quest!");
                    a.show();
                    return;
                }
                description_lbl.setText("");
                rewards_lbl.setText("");
                location_lbl.setText("");
                showActive();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

        // zbavenie sa stareho, strata 10% aktualnych XP, aktualizacia databazy
        else {
            try {
                databaseConnector.deleteQuest(selected_quest_id, char_id);
            } catch (SQLException e) {
                System.out.println(e);
            }
            try {
                databaseConnector.decreaseXP(char_id);
                description_lbl.setText("");
                rewards_lbl.setText("");
                location_lbl.setText("");
                showActive();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
}