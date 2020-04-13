package registration;

import database.DatabaseConnector;

public class Registration {

    public int registerUser(String passw,String name,String mail)
    {
        DatabaseConnector dataCon = new DatabaseConnector();
        dataCon.DatabseInit();
        int flag = dataCon.addUser(passw, name, mail);
        dataCon.connectionClose();
        return flag;
    }
}
