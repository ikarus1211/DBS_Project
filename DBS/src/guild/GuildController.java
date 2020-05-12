package guild;

import database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import menu.MenuController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuildController {

    @FXML
    private Label name;
    @FXML
    private Label leader;
    @FXML
    private Label rank;
    @FXML
    private Label no_players;

    private int char_id;
    private int guild_id;
    private int personId;
    private int serverID;

    private DatabaseConnector databaseConnector = new DatabaseConnector();

    public void initId(int c_id, int g_id, int p_id, int serverID)
    {
        this.char_id = c_id;
        this.guild_id = g_id;
        this.personId = p_id;
        this.serverID = serverID;
        databaseConnector.DatabseInit();
        showGuild();
    }

    public void showGuild() {
        try {
            ArrayList<ArrayList<String>> aList = databaseConnector.getGuildInfo(guild_id);
            String lead =  databaseConnector.getLeader(guild_id);

            name.setText("Name: " + aList.get(0).get(1));
            leader.setText("Leader: " + lead);
            no_players.setText("Number of members: " + aList.get(0).get(2));
            rank.setText("Rank: " + aList.get(0).get(3));

        } catch (SQLException e) {
            System.out.println(e);
        }
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
}
