package teamProject.db;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class representing and operating database
 */
public class Database implements AutoCloseable {

    Connection con = null;

    public Database(String url, String user, String password) throws SQLException {
        String str = "jdbc:mysql:" + url + "?user=" + user + "&password=" + password;
        con = DriverManager.getConnection(str);
    }

    public void resetDB() {
        //reset and setup DB
        //dummy code
        System.out.println("Hey code works!");

    }

    //QUERIES AND DATA PROCESSING 

    @Override
    public void close() throws Exception {
        con.close();
    }

}
