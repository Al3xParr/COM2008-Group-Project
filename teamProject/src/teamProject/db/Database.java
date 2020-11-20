package teamProject.db;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.sql.*;

import java.nio.file.*;
import java.io.IOException;

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
        try (Statement statement = con.createStatement()){
            String query = "TRUNCATE TABLE 'Teachers'; TRUNCATE TABLE 'Students'; 
                            TRUNCATE TABLE 'StudentsToModules'; TRUNCATE TABLE 'Modules'; 
                            TRUNCATE TABLE 'CourseToDepartment'; TRUNCATE TABLE 'Departments'; 
                            TRUNCATE TABLE 'Course'; TRUNCATE TABLE 'BachEquiv'; 
                            TRUNCATE TABLE 'ModulesToCourse';"
            statement.executeUpdate(query);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
    }

    public void resetTable(String tblName){
        String query = "TRUNCATE TABLE ?;"
        try (PreparedStatement prepState = con.PreparedStatement(query)){
            prepState.clearParameters();
            prepState.setString(1, tblName)
            prepState.executeUpdate();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void createDB() {
        String createQuery = new String();

        try {
        createQuery = new String(Files.readAllBytes(Paths.get("dbSchema.txt")));

        } catch (IOException ex){
            ex.printStackTrace();}

        try (Statement statement = con.createStatement()){
            statement.executeUpdate(createQuery);
            System.out.println("Database Created");
            
        } catch (Exception ex) {
            System.out.println("Database creation failed");
            ex.printStackTrace();
        }
    }
    //QUERIES AND DATA PROCESSING 

    @Override
    public void close() throws Exception {
        con.close();
    }

}
