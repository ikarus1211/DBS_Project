package database;

import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    private Connection connection = null;
    private String url = "jdbc:mysql://localhost:3306/dbs_projekt?characterEncoding=latin1";
    private String name = "root";
    private String passw = "1999";
    private Statement statement = null;
    private ResultSet returnedValue = null;
    private PreparedStatement prepstatement = null;
    public void DatabseInit()
    {

        // Checks if driver is available
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            e.printStackTrace();
            return;
        }

        // Connecting into database
        try {
            connection = DriverManager.getConnection(url, name, passw);
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        //Testing connection
/*        if (connection != null) {
            System.out.println("Connection established\n");
        } else {
            System.out.println("Problem with connection");
        }
*/
    }

    /*
     * Function finds user in database and then evaluate if password is matching.
     * If password are matching, it return value corresponding to it.
     */
    public Pair<Integer,Integer> checkUser(String userName, String password)
    {
        // In case connection is not established
        if(connection == null) {
            System.out.println("Problem");
        }

        // Try to get user from table
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM player INNER JOIN player_server ON pid = player_id\n" +
                            "INNER JOIN game_server ON server_id = sid WHERE player_name = ?");
            prepstatement.setString(1, userName);
            returnedValue = prepstatement.executeQuery();

            // If something was found it evaluate the password.
            // If not there is not a player with given name
            if (returnedValue.next()) {
                if (password.equals(returnedValue.getString("player_password"))) {
      //              System.out.print("login Successful");

                    return new Pair<>(returnedValue.getInt("player_id"), returnedValue.getInt("server_id"));
                } else {
                    System.out.print("login failed!");
                    return new Pair<>(0,0);
                }
            } else
                return new Pair<>(0,0);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new Pair<>(-1,-1);
        }
    }

    public void connectUserServer(int id, int server) throws SQLException {
        connection.setAutoCommit(false);
        try {
            System.out.println("shir");
            prepstatement = connection.prepareStatement("INSERT INTO player_server (pid, sid) VALUES (?, ?)");
            prepstatement.setInt(1, id);
            prepstatement.setInt(2, server);
            prepstatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
    }


    // This function adds a new user into database
    public int addUser(String userPassw, String userName, String userEmail) throws SQLException
    {
        connection.setAutoCommit(false);
        int id = 0;
        PreparedStatement prepstatement = null;
        try {
            prepstatement = connection.prepareStatement("INSERT INTO player " +
                    "(player_name,player_password,email,no_characters,premium_points) VALUES (?,?,?,0,0)");
            prepstatement.setString(1, userName);
            prepstatement.setString(2, userPassw);
            prepstatement.setString(3, userEmail);
            prepstatement.executeUpdate();

            prepstatement = connection.prepareStatement("select last_insert_id()");
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                id = returnedValue.getInt(1);
            }

            connection.commit();
        } catch (SQLException e)
        {
            connection.rollback();
            System.out.print("Failed to add user");
            e.printStackTrace();
            return -1;
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return id;
    }

    /*
     * Closing connection
     */
    public void connectionClose()
    {
        try {
            if (returnedValue != null)
                returnedValue.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
    //        System.out.print("Connection closed\n");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Closing connection failed!");
        }
    }



    public int updateChar(int id) throws SQLException
    {
        PreparedStatement prepstatement;
        connection.setAutoCommit(false);
        try {

            prepstatement = connection.prepareStatement("UPDATE player SET no_characters=(no_characters+1)" +
                    "WHERE player_id = ?");
            prepstatement.setInt(1, id);
            prepstatement.execute();

            connection.commit();
        } catch (SQLException e)
        {
            connection.rollback();
            System.out.print("Failed to update user");
            e.printStackTrace();
            connection.setAutoCommit(true);
            return -1;
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return 1;
    }




    public ArrayList<ArrayList<String>> getCharacters(int id, int serverID, int offset) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM game_character INNER JOIN player_server ON pid = player_id\n" +
                            "INNER JOIN game_server ON server_id = sid WHERE player_owner = ? AND server_id = ? LIMIT ?,12");
            prepstatement.setInt(1, id);
            prepstatement.setInt(2, serverID);
            prepstatement.setInt(3, offset);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return  a;
    }

    public ArrayList<ArrayList<String>> searchInTable(String searchValue, int serverID) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM game_character INNER JOIN player ON player_owner = player_id \n" +
                            "INNER JOIN player_server ON pid = player_id\n" +
                            "INNER JOIN game_server ON server_id = sid\n" +
                            "WHERE character_name = ? AND server_id = ?");
            prepstatement.setString(1, searchValue);
            prepstatement.setInt(2, serverID);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }


    public ArrayList<ArrayList<String>> getOneCharacter(int id) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM game_character WHERE character_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }


    public int deleteCharacter(int c_id, int p_id) throws SQLException {
        connection.setAutoCommit(false);
        PreparedStatement prepstatement;
        try {
            prepstatement = connection.prepareStatement("DELETE FROM character_item WHERE cid = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.execute();

            prepstatement = connection.prepareStatement("DELETE FROM character_quest WHERE char_id = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.execute();

            prepstatement = connection.prepareStatement("DELETE FROM game_character WHERE character_id = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.execute();

            prepstatement = connection.prepareStatement("UPDATE player SET no_characters=(no_characters-1)" +
                    "WHERE player_id = ?");
            prepstatement.setInt(1, p_id);
            prepstatement.execute();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.print("Failed to delete character");
            e.printStackTrace();
            return -1;
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return 1;
    }


    public int checkCharacterOwner(int id) throws SQLException {
        int owner = 0;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT player_owner FROM game_character WHERE character_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                owner = returnedValue.getInt(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        return owner;
    }


    public ArrayList<ArrayList<String>> bestPlayers(int offset, int serverID) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT player_name,character_name,AVG(character_xp) as AvgExperience, no_characters as NoCharacters, character_xp, player_id, server_id\n" +
                            "FROM game_character \n" +
                            "INNER JOIN player ON player_owner = player_id INNER JOIN player_server ON pid = player_id\n" +
                            "INNER JOIN game_server ON server_id = sid \n" +
                            "GROUP BY player_name\n" +
                            "HAVING server_id = ? AND character_xp = (SELECT MAX(character_xp) FROM game_character WHERE player_owner = player_id)\n" +
                            "ORDER BY 3 DESC\n"+
                            "LIMIT ?,100");
            prepstatement.setInt(1,serverID);
            prepstatement.setInt(2,offset);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }


    public ArrayList<ArrayList<String>> bestGuild(int offset, int serverID) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT guild_name as Guild_Name, number_of_players as no_players, SUM(game_money) as AMOUNT_OF_GOLD, server_id\n" +
                            "FROM guild g\n" +
                            "INNER JOIN game_character c ON c.guild_id = g.guild_id  INNER JOIN player ON player_owner = player_id\n" +
                            "INNER JOIN player_server ON pid = player_id\n" +
                            "INNER JOIN game_server ON server_id = sid \n" +
                            "group by guild_name HAVING server_id = ?\n" +
                            "order by 3 DESC\n" +
                            "LIMIT ?,100");
            prepstatement.setInt(1,serverID);
            prepstatement.setInt(2,offset);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }



    public ArrayList<ArrayList<String>> getBestPlayers(int offset, int serverID) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.
                    prepareStatement(" SELECT temp.player_name, temp.character_name, temp.race, temp.class, temp.game_money, temp.total_money, temp.hours_played, server_id\n" +
                            "FROM( SELECT p.player_name, c.hours_played, c.race, c.class, c.character_name, server_id, c.game_money,\n" +
                            "sum(game_money) OVER (PARTITION BY player_id) total_money\n" +
                            "FROM game_character c\n" +
                            "INNER JOIN player p ON p.player_id = c.player_owner INNER JOIN player_server ON pid = player_id\n" +
                            "INNER JOIN game_server ON server_id = sid \n" +
                            ") temp\n" +
                            "WHERE server_id = ?\n" +
                            "ORDER BY 6 DESC, 1 DESC, 5 DESC\n" +
                            "LIMIT ?,100");
            prepstatement.setInt(1,serverID);
            prepstatement.setInt(2,offset);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }


    public String getGuildName(int id) throws SQLException {
        String s = "";
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT guild_name FROM guild WHERE guild_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                s = returnedValue.getString(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return s;
    }


    public ArrayList<ArrayList<String>> getGuildInfo(int id) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT * FROM guild WHERE guild_id = ?");
            prepstatement.setInt(1, id);
            returnedValue= prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
        return a;
    }


    public String getLeader(int id) throws SQLException {
        String s = "";
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT character_name FROM game_character WHERE guild_leader = 1 AND guild_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                s = returnedValue.getString(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return s;
    }


    public List<Integer> getItems(int id) throws SQLException {
        List<Integer> l = new ArrayList<>();
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.
                    prepareStatement("SELECT itemid FROM character_item WHERE cid = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                l.add(returnedValue.getInt(1));
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return l;
    }


    public ArrayList<ArrayList<String>> getItemInfo(int id) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT * FROM item WHERE item_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return a;
    }

    public int getItemId(String name, int level) throws SQLException {
        connection.setAutoCommit(false);
        int i_id = 0;
        try {
            prepstatement = connection.prepareStatement("SELECT item_id FROM item WHERE item_name = ? AND item_level = ?");
            prepstatement.setString(1, name);
            prepstatement.setInt(2, level);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                i_id = returnedValue.getInt(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return i_id;
    }

    public void destroyItem(int i_id, int c_id, int lvl) throws SQLException {
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("DELETE FROM character_item WHERE itemid = ? AND cid = ? ");
            prepstatement.setInt(1, i_id);
            prepstatement.setInt(2, c_id);
            prepstatement.execute();
            addMoney(lvl, c_id);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }


    private void addMoney(int amount, int id) throws SQLException {
        prepstatement = connection.prepareStatement("UPDATE game_character SET game_money=(game_money + ?*1000)" +
                "WHERE character_id = ?");
        prepstatement.setInt(1, amount);
        prepstatement.setInt(2, id);
        prepstatement.execute();
    }


    public void buffItem(int i_id, int c_id, int lvl) throws SQLException {
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("UPDATE character_item SET itemid=(itemid+1000)" +
                    "WHERE cid = ? AND itemid = ?");
            prepstatement.setInt(1, c_id);
            prepstatement.setInt(2, i_id);
            prepstatement.execute();
            // strhne 5000 korun za kazdy level itemu
            addMoney(-(5*lvl), c_id);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }

    public int addQuest(int id_q, int c_id) throws SQLException {
        int cnt = 0;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT COUNT(*) FROM character_quest WHERE questid = ? AND char_id = ?");
            prepstatement.setInt(1, id_q);
            prepstatement.setInt(2, c_id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                cnt = returnedValue.getInt(1);
            }

            if (cnt != 0) {
                return -1;
            }

            try {
                prepstatement = connection.prepareStatement("INSERT INTO character_quest" +
                        "(questid, char_id) VALUES (?, ?)");
                prepstatement.setInt(1, id_q);
                prepstatement.setInt(2, c_id);
                prepstatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Cannot add quest");
            }
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
        return 1;
    }


    public void deleteQuest(int id_q, int c_id) throws SQLException{
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("DELETE FROM character_quest WHERE questid = ? AND char_id = ? ");
            prepstatement.setInt(1, id_q);
            prepstatement.setInt(2, c_id);
            prepstatement.execute();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }

    public void decreaseXP(int id) throws SQLException {
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("UPDATE game_character SET character_xp=(character_xp/100*90) " +
                    "WHERE character_id = ?");
            prepstatement.setInt(1, id);
            prepstatement.execute();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
    }


    public List<Integer> getQuests(int id) throws SQLException {
        List<Integer> l = new ArrayList<>();
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.
                    prepareStatement("SELECT questid FROM character_quest WHERE char_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                l.add(returnedValue.getInt(1));
            }
            connection.commit();
        }  catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return l;
    }


    public String getNameOfQuest(int id) throws SQLException {
        String lst = "";
        connection.setAutoCommit(false);
        try {
            prepstatement = connection
                    .prepareStatement("SELECT quest_name FROM quest WHERE quest_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                lst = returnedValue.getString(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return lst;
    }


    public String getLocation(int id) throws SQLException {
        String s = "";
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT location_name FROM location WHERE location_id = ?");
            prepstatement.setInt(1, id);
            returnedValue = prepstatement.executeQuery();
            while (returnedValue.next()) {
                s = returnedValue.getString(1);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        connection.setAutoCommit(true);
        prepstatement.close();
        return s;
    }


    public ArrayList<ArrayList<String>> getQuestInfo(String name) throws SQLException {
        ArrayList<ArrayList<String>> a = null;
        connection.setAutoCommit(false);
        try {
            prepstatement = connection.prepareStatement("SELECT * FROM quest WHERE quest_name = ?");
            prepstatement.setString(1, name);
            returnedValue= prepstatement.executeQuery();
            a = rsToString(returnedValue);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        prepstatement.close();
        connection.setAutoCommit(true);
        return a;
    }


    private static ArrayList<ArrayList<String>> rsToString(ResultSet resultSet) throws SQLException {
        ArrayList<ArrayList<String>> stringResult = new ArrayList<>();
        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<>();
            int i = 1;
            do {
                String column = resultSet.getString(i);
                i++;
                row.add(column);
            } while (i <= resultSet.getMetaData().getColumnCount());

            stringResult.add(row);
        }
        return stringResult;
    }
}