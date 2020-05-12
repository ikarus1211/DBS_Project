package inventory;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import menu.MenuController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {

    @FXML
    TableView<ModelTab> tabView;
    @FXML
    TableColumn<ModelTab, String> name;
    @FXML
    TableColumn<ModelTab, String> level;
    @FXML
    TableColumn<ModelTab, String> type;
    @FXML
    TableColumn<ModelTab, String> rarity;

    private int char_id;
    private int personId;
    private int itemId;
    private int itemLvl;
    private ResultSet resultSet;
    private DatabaseConnector databaseConnector = new DatabaseConnector();
    private ObservableList<ModelTab> oblist = FXCollections.observableArrayList();
    private Alert a = new Alert(Alert.AlertType.NONE);
    private List<Integer> lst = null;

    public void initId(int c_id, int p_id) {
        itemId = 0;
        this.personId = p_id;
        this.char_id = c_id;
        getItems(c_id);
    }

    public void getItems(int id) {
        try {
            databaseConnector.DatabseInit();
            lst = databaseConnector.getItems(id);
        } catch (SQLException e)
        {
            System.out.println("? Items not found ?");
        }

        fillTable(lst);
    }

    private String getType(int t) {
        String type;
        if (t == 1)
            type = "Helmet";
        else if (t == 2)
            type = "Shirt";
        else if (t == 3)
            type = "Boots";
        else type = "Gloves";
        return type;
    }

    private void fillTable(List<Integer> lst) {

        oblist.clear();

        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        level.setCellValueFactory(new PropertyValueFactory<>("Level"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        rarity.setCellValueFactory(new PropertyValueFactory<>("Rarity"));
        try {
            for (Integer i: lst) {
                ArrayList<ArrayList<String>> aList = databaseConnector.getItemInfo(i);
                for (int k = 0; k < aList.size(); k++) {
                    oblist.add(new ModelTab(aList.get(k).get(1),
                            aList.get(k).get(2),
                            getType(Integer.parseInt(aList.get(k).get(3))),
                            aList.get(k).get(4)));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Cannot find items");
        }
        tabView.setItems(oblist);
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

    public void chooseItem() {
        ModelTab item = tabView.getSelectionModel().getSelectedItem();
        if (item != null) {
            try {
                itemId = databaseConnector.getItemId(item.name, Integer.valueOf(item.level));
                itemLvl = Integer.valueOf(item.level);
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public void buffItem() throws SQLException {

        if (personId != databaseConnector.checkCharacterOwner(char_id)) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You cannot modify someone else's character!");
            a.show();
            return;
        }

        if (itemId == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Choose an item!");
            a.show();
            return;
        }

        try {
            if (itemLvl > 4) {
                a.setAlertType(Alert.AlertType.INFORMATION);
                a.setHeaderText("Item is fully upgraded!");
                a.show();
                return;
            }
            else {
                if (itemLvl * 5000 <= databaseConnector.getMoney(char_id)) {
                    databaseConnector.buffItem(itemId, char_id, itemLvl);
                } else {
                    a.setAlertType(Alert.AlertType.INFORMATION);
                    a.setHeaderText("You don't have enough money!");
                    a.show();
                    return;
                }
            }
        } catch(SQLException e){
            System.out.println(e);
        }
        itemId = 0;
        getItems(char_id);
    }


    public void destroyItem() throws SQLException {

        if (personId != databaseConnector.checkCharacterOwner(char_id)) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("You cannot modify someone else's character!");
            a.show();
            return;
        }

        if (itemId == 0) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Choose an item!");
            a.show();
            return;
        }
        try {
            databaseConnector.destroyItem(itemId, char_id, itemLvl);
        } catch (SQLException e) {
            System.out.println(e);
        }
        itemId = 0;
        getItems(char_id);
    }
}