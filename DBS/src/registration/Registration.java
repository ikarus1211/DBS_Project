package registration;

import database.DatabaseConnector;

import java.sql.SQLException;

public class Registration {

    public int registerUser(String passw,String name,String mail) throws SQLException
    {
        DatabaseConnector dataCon = new DatabaseConnector();
        dataCon.DatabseInit();
        int flag = dataCon.addUser(passw, name, mail);
        dataCon.connectionClose();
        return flag;
    }
    public void connectUserWithServer(int id, int server)  throws SQLException {
        DatabaseConnector dataCon = new DatabaseConnector();
        dataCon.DatabseInit();
        dataCon.connectUserServer(id, server);
        dataCon.connectionClose();
    }
}
