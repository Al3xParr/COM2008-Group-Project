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
import java.sql.PreparedStatement;
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

    //INSERT FUNCTIONS

    private void newStudent(Student newStudent) throws SQLException{
        
        String insertStudent = "INSERT INTO Students VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement insert = con.prepareStatement(insertStudent)) {
            insert.clearParameters();
            insert.setInt(1, newStudent.regNum);
            insert.setString(2, newStudent.title);
            insert.setString(3, newStudent.surname);
            insert.setString(4, newStudent.fornames);
            insert.setString(5, newStudent.email);
            insert.setString(6, newStudent.username);
            insert.setString(7, newStudent.username);
            insert.setString(8, newStudent.degree.courseCode);
            insert.executeUpdate();
            for(StudyPeriod x : newStudent.studyPeriodList){
                newStudyPeriod(x);
            }
        }catch(SQLException e){
            throw e;
        }
    }

    /**
     * Inserting newly created user into database (inserts all relative information)
     * @param newUser user to be inserted
     * @return true if user inserted succesfully false if not
     */
    public boolean newUser(User newUser) {
        
        String type = "Basic";
        if(newUser instanceof Student){
            type = "Student";
        }else if(newUser instanceof Teacher){
            type = "Teacher";   
        }else if (newUser instanceof Administrator) {
            type = "Administrator";
        } else if (newUser instanceof Registrar) {
            type = "Registrar";
        }
        boolean succes = true;
        String insertUser = "INSERT INTO Account VALUES(?,?,?,?);";
        try (PreparedStatement insert = con.prepareStatement(insertUser)) {
            insert.clearParameters();
            insert.setString(1, newUser.username);
            insert.setString(2, newUser.passwordHash);
            insert.setString(3, newUser.passwordSalt);
            insert.setString(4, type);
            insert.executeUpdate();
            switch(type){
                case "Student":
                    newStudent(newUser);
                    break;
                case "Teacher":
                    newTeacher(newUser);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            succes = false;
        }
        
        
        return succes;
        
    }

}

