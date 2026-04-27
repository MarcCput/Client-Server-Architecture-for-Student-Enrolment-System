package za.ac.cput.serverside.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
     public static Connection derbyConnection() throws SQLException{
        String DATABASE_URL = "jdbc:derby://localhost:1527/StudentEnrolmentDB";
        String username = "administrator";
        String password = "admin";
        //creating a connection
        Connection con = DriverManager.getConnection(DATABASE_URL, username, password);
        return con;
}
}