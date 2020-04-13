package Database;

import java.sql.*;

public class DatabaseConnector {
        // TODO prerobit dokumnetaciu a diagra
    private Connection connection = null;
    private String url = "jdbc:mysql://localhost:3306/dbs_projekt?characterEncoding=latin1";
    private String name = "root";
    private String passw = "1999";
    private Statement statement = null;
    private ResultSet returnedValue = null;
    private PreparedStatement prepstatement = null;

    /*
     * Initializing connection into database
     */
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
        if (connection != null) {
            System.out.println("Connection established\n");
        } else {
            System.out.println("Problem with connection");
        }

    }

    /*
     * Function finds user in database and then evaluate if password is matching.
     * If password are matching, it return value corresponding to it.
     */
    public int checkUser(String userName, String password)
    {
        // In case connection is not established
        if(connection == null) {
            System.out.println("Problem");
        }

        // Try to get user from table
        try {
            prepstatement = connection
                    .prepareStatement("SELECT * FROM player WHERE player_name = ?");
            prepstatement.setString(1, userName);
            returnedValue = prepstatement.executeQuery();

            // If something was found it evaluate the password.
            // If not there is not a player with given name
            if (returnedValue.next()) {
                if (password.equals(returnedValue.getString("player_password"))) {
                    System.out.print("Login Successful");
                    return returnedValue.getInt("player_id");
                } else {
                    System.out.print("Login failed!");
                    return 0;
                }
            } else
                return 0;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // This function adds a new user into database
    public int addUser(String userPassw, String userName, String userEmail)
    {

        PreparedStatement prepstatement = null;
        try {
            prepstatement = connection.prepareStatement("INSERT INTO player " +
                    "(player_name,player_password,email,no_characters,premium_points) VALUES (?,?,?,0,0)");
            prepstatement.setString(1, userName);
            prepstatement.setString(2, userPassw);
            prepstatement.setString(3, userEmail);
            prepstatement.executeUpdate();

        } catch (SQLException e)
        {
            System.out.print("Failed to add user");
            e.printStackTrace();
            return -1;
        }
        return 1;
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
            System.out.print("Connection closed\n");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print("Closeing connection failed!");
        }
    }

    public ResultSet getFromQuery(String quer)
    {
        try {
            prepstatement = connection
                    .prepareStatement(quer);
            returnedValue = prepstatement.executeQuery();
            return returnedValue;
        } catch (SQLException e)
        {
            System.out.println("Problem with player table");
        }
        return null;
    }

    public int addCharacter(String charName, String charClass, String charRace, int id)
    {
        {

            PreparedStatement prepstatement = null;
            try {
                prepstatement = connection.prepareStatement("INSERT INTO game_character " +
                        "(character_name,character_xp,class,race,game_money,player_owner) VALUES (?,0,?,?,0,?)");
                prepstatement.setString(1, charName);
                prepstatement.setString(2, charClass);
                prepstatement.setString(3, charRace);
                prepstatement.setInt(4, id);
                prepstatement.executeUpdate();

            } catch (SQLException e)
            {
                System.out.print("Failed to add user");
                e.printStackTrace();
                return -1;
            }
            return 1;
        }
    }

    public ResultSet getCharacters(int id ,int offset) throws SQLException {
        prepstatement = connection
                .prepareStatement("SELECT * FROM game_character WHERE player_owner = ? LIMIT ?,12");
        prepstatement.setInt(1, id);
        prepstatement.setInt(2, offset);
        returnedValue = prepstatement.executeQuery();
        return  returnedValue;
    }

    public ResultSet searchInTable(String searchValue) throws SQLException {
        prepstatement = connection
                .prepareStatement("SELECT * FROM game_character WHERE character_name = ?");
        prepstatement.setString(1, searchValue);
        returnedValue = prepstatement.executeQuery();
        return  returnedValue;
    }
}
