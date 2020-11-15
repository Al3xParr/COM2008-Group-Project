package teamProject.db;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.sql.*;
import teamProject.Classes.*;

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

    private void newStudyPeriod(StudyPeriod newPeriod, Student student) throws SQLException {
        String insertPeriod = "INSERT INTO StudentsToModules VALUES(?,?,?,?,?,?,?);";
        try (PreparedStatement insert = con.prepareStatement(insertPeriod)) {
            for (Grade g : newPeriod.getGradesList()) {
                insert.clearParameters();
                insert.setInt(1, student.getRegNum());
                insert.setString(2, g.getModule().getModuleCode());
                insert.setDouble(3, g.getMark());
                insert.setDouble(4, g.getResitMark());
                insert.setString(5, "" + newPeriod.getLabel());
                insert.setString(6, student.getCourse().getCourseCode());
                insert.setString(7, "" + newPeriod.getDegreeLvl().getDegreeLvl());
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private void newStudent(Student newStudent) throws SQLException {

        String insertStudent = "INSERT INTO Students VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement insert = con.prepareStatement(insertStudent)) {
            insert.clearParameters();
            insert.setInt(1, newStudent.getRegNum());
            insert.setString(2, newStudent.getTitle());
            insert.setString(3, newStudent.getSurname());
            insert.setString(4, newStudent.getForenames());
            insert.setString(5, newStudent.getEmail());
            insert.setString(6, newStudent.getUsername());
            insert.setString(7, newStudent.getTutor());
            insert.setString(8, newStudent.getCourse().getCourseCode());
            insert.executeUpdate();
            for (StudyPeriod x : newStudent.getStudyPeriodList()) {
                newStudyPeriod(x, newStudent);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private void newTeacher(Teacher newTeacher) throws SQLException {

        String insertTeacher = "INSERT INTO Teachers VALUES(?,?)";
        try (PreparedStatement insert = con.prepareStatement(insertTeacher)) {
            insert.clearParameters();
            insert.setString(1, newTeacher.getUsername());
            insert.setString(2, newTeacher.getFullName());
            insert.executeUpdate();

        } catch (SQLException e) {
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
        if (newUser instanceof Student) {
            type = "Student";
        } else if (newUser instanceof Teacher) {
            type = "Teacher";
        } else if (newUser instanceof Administrator) {
            type = "Administrator";
        } else if (newUser instanceof Registrar) {
            type = "Registrar";
        }
        boolean succes = true;
        String insertUser = "INSERT INTO Account VALUES(?,?,?,?);";
        try (PreparedStatement insert = con.prepareStatement(insertUser)) {
            insert.clearParameters();
            insert.setString(1, newUser.getUsername());
            insert.setString(2, newUser.getPasswordHash());
            insert.setString(3, newUser.getSalt());
            insert.setString(4, type);
            insert.executeUpdate();
            switch (type) {
                case "Student":
                    newStudent((Student) newUser);
                    break;
                case "Teacher":
                    newTeacher((Teacher) newUser);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            succes = false;
        }

        return succes;

    }

}
