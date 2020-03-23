package Database;

import java.sql.*;

public class DatabaseConnector {

    private Connection connection = null;
    private String url = "jdbc:mysql://localhost:3306/dbs_projekt?characterEncoding=latin1";
    private String name = "root";
    private String passw = "1999";
    private Statement statement = null;

    public void DatabseInit()
    {
        System.out.print("here");

        // Driver initialization for connecting to database
        // Maros ak ti to nahodou nepojde v lib je jeden subor a ten si musis nastavit do PATH tohto projektu
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
            System.out.println("Connection established");
        } else {
            System.out.println("Problem with connection");
        }

    }

    public int checkUser(String userName, String password)
    {
        ResultSet returnedValue = null;
        try
        {
            PreparedStatement prepstatement =  connection.prepareStatement("SELECT * FROM player" +
                    "WHERE USERNAME = ?");
            prepstatement.setString(1, userName);
            returnedValue = prepstatement.executeQuery();

            while(true)
            {
                String uName = null;
                String pWord = null;

                if (returnedValue.next())
                {
                    uName = returnedValue.getString("player_name");
                    pWord = returnedValue.getString("player_password");
                } else break;
                if ( uName.equals(userName) && pWord.equals(password)) {
                    System.out.print("Login Successful");
                    return 1;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    // This function adds a new user into database
    public void addUser(String userPassw, String userName, String userEmail)
    {
        PreparedStatement prepstatement = null;
        try {
            prepstatement = connection.prepareStatement("INSERT INTO player " +
                    "(player_name,player_password,email,no_players,premium_points) VALUES (?,?,?,0,0");
            prepstatement.setString(1, userName);
            prepstatement.setString(2, userPassw);
            prepstatement.setString(3, userEmail);
            prepstatement.executeUpdate();

        } catch (SQLException e)
        {
            System.out.print("Failed to add user");
            e.printStackTrace();
        }

    }


}
