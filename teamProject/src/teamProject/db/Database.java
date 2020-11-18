package teamProject.db;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.sql.*;
import java.util.*;

import teamProject.Classes.*;
import teamProject.Classes.Module;

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

    @Override
    public void close() throws Exception {
        con.close();
    }

    //QUERIES AND PROCESSING

    private Set<String> getTableNames() {
        Set<String> tables = null;
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT TABLE_NAME, TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES "
                    + "WHERE table_schema = (SELECT DATABASE());");
            tables = new HashSet<String>();
            while (results.next()) {
                tables.add(results.getString("TABLE_NAME"));
            }
            results.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tables;

    }

    private Set<String> getColumnNames(String tableName) {
        Set<String> columns = null;
        if (getTableNames().contains(tableName)) {
            try (Statement stsm = con.createStatement()) {
                ResultSet results = stsm.executeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS "
                        + "WHERE table_name = '" + tableName + "';");
                columns = new HashSet<String>();
                while (results.next()) {
                    columns.add(results.getString("COLUMN_NAME"));
                }
                results.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return columns;
    }

    /**
     * Checks if given set of values exists within the same row of a table
     * @param tableName table to check
     * @param columnNames Array of column names to check
     * @param columnValues Array of values of primary key associated with column names
     * @return true if given set of values exists in table false otherwise
     */
    public boolean ValueSetCheck(String tableName, String[] columnNames, String[] columnValues) {
        boolean succes = false;
        if (columnNames.length == columnValues.length && columnNames.length != 0) {
            for (String name : columnNames)
                if (!getColumnNames(tableName).contains(name))
                    return false;

            String query = "SELECT * FROM " + tableName + "WHERE";
            int i = 0;
            query += " " + columnNames[i++] + " = ?";
            for (; i < columnNames.length; i++) {
                query += " AND " + columnNames[i] + " = ?";
            }
            query += ";";
            try (PreparedStatement check = con.prepareStatement(query)) {
                check.clearParameters();
                int index = 1;
                for (String s : columnValues) {
                    check.setString(index++, s);
                }
                ResultSet res = check.executeQuery();
                if (res.next()) {
                    res.close();
                    succes = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return succes;

    }

    //INSERTS - PUBLIC FUNCTIONS

    /**
    * Inserts information about new study period into database
    * @param newPeriod new StudyPeriod object to be assigned to student
    * @param to Student the study period belongs to
    * @return true if operation was compleated false if not
    */
    public boolean addStudyPeriod(StudyPeriod newPeriod, Student to) {
        boolean succes = false;
        try {
            con.setAutoCommit(false);
            try {
            insertStudyPeriod(newPeriod, to);
                con.commit();
                succes = true;

        } catch (Exception e) {
            e.printStackTrace();
                con.rollback();
        }
            con.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return succes;

    }

    /**
     * Inserting newly created user into database (inserts all relative information)
     * Students shouldnt have study periods yet
     * @param newUser user to be inserted
     * @return true if user inserted succesfully false if not
     */
    public boolean addUser(User newUser) {
        
        boolean succes = false;
        String[] names = { "username" };
        String[] values = { newUser.getUsername() };
        
        if (!ValueSetCheck("Accounts", names, values)) {
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
                        insertStudent((Student) newUser);
                        break;
                    case "Teacher":
                        insertTeacher((Teacher) newUser);
                        break;
                }
                succes = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return succes;

    }

    /**
     * Inserts information about a course into database
     * @param newCourse
     * @return true if operation was succesfull
     */
    public boolean addCourse(Course newCourse) {
        boolean succes = false;
        try {
            insertCourse(newCourse);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return succes;
    }

    /**
     * Inserts information about new Department into database (the module list should be)
     * @param newDepartment new Department to be inserted
     * @return true if the opperation was succesful
     */
    public boolean addDepartment(Department newDepartment) {
        boolean succes = false;
        String[] names = { "deptCode" };
        String[] values = { newDepartment.getDeptCode() };
        if (newDepartment.getModuleList().length == 0 && !ValueSetCheck("Departments", names, values)) {

            String insertDep = "INSERT INTO Departments VALUES (?,?);";
            
            try (PreparedStatement insert = con.prepareStatement(insertDep)) {
                insert.clearParameters();
                insert.setString(1, newDepartment.getDeptCode());
                insert.setString(2, newDepartment.getFullName());
                insert.executeUpdate();
                for (Course c : newDepartment.getCourseList()) {
                    insertCourseDepartLink(c, newDepartment);
                }
                succes = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return succes;

    }

    /**
     * Inserts information about new Module into Department
     * @param newModule Module to be added
     * @return true if operation was succesfull
     */
    public boolean addModule(Module newModule) {
        boolean succes = false;
        try {
            insertModule(newModule);
            succes = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return succes;
    }

    /**
     * Inserts information about new Study Level into Department
     * @param newStudyLevel Level to be added
     * @param owner Course the study Level belongs to
     * @return true if operation was succesfull
     */
    public boolean addStudyLevel(StudyLevel newStudyLevel, Course owner) {
        boolean succes = false;
        try {
            con.setAutoCommit(false);
            try {
                for (Module m : newStudyLevel.getCoreModules()) {
                    insertModuleCourseLink(m, owner, true, newStudyLevel.getDegreeLvl());
                }
                for (Module m : newStudyLevel.getOptionalModules()) {
                    insertModuleCourseLink(m, owner, false, newStudyLevel.getDegreeLvl());
                }
                con.commit();
                succes = true;
            } catch (Exception e) {
                e.printStackTrace();
                con.rollback();
            }
            con.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return succes;

    }

    /**
     * Adds course supervision
     * @param c course to be supervised
     * @param d supervising department
     * @return true if operation was succesfull false otherwise
     */
    public boolean connectCourseToDepart(Course c, Department d) {
        boolean succes = false;
        try {
            insertCourseDepartLink(c, d);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return succes;
    }

    //INSERTS - PRIVATE FUNCTIONS

    private void insertStudyPeriod(StudyPeriod newPeriod, Student student) throws SQLException {
        
        String insertPeriod = "INSERT INTO StudentsToModules VALUES(?,?,?,?,?,?,?);";
        
        try (PreparedStatement insert = con.prepareStatement(insertPeriod)) {
            for (Grade g : newPeriod.getGradesList()) {
                String[] names = { "regNum", "moduleCode", "label" };
                String[] values = { Integer.toString(student.getRegNum()), g.getModule().getModuleCode(),
                        "" + newPeriod.getLabel() };
                if (!ValueSetCheck("StudentsToModules", names, values)) {
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
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private void insertStudent(Student newStudent) throws SQLException {
        
        String[] names = { "regNum" };
        String[] values = { Integer.toString(newStudent.getRegNum()) };
        
        if (!ValueSetCheck("Students", names, values)) {
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
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    private void insertTeacher(Teacher newTeacher) throws SQLException {
        
        String[] names = { "username" };
        String[] values = { newTeacher.getUsername() };
        
        if (!ValueSetCheck("Students", names, values)) {
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
    }

    private void insertCourse(Course c) throws SQLException {

        String[] names = { "courseCode" };
        String[] values = { c.getCourseCode() };

        if (!ValueSetCheck("Course", names, values)) {

            String insertCourse = "INSERT INTO Course VALUES(?,?,?)";
            try (PreparedStatement insert = con.prepareStatement(insertCourse)) {
                insert.clearParameters();
                insert.setString(1, c.getCourseCode());
                insert.setString(2, c.getFullName());
                insert.setBoolean(3, c.getYearInIndustry());
                insert.executeUpdate();

                if (c.getBachEquiv() != null) {
                    insertBachEquiv(c, c.getBachEquiv());
                }
            } catch (Exception e) {
                throw e;
            }

        }

    }

    private void insertCourseDepartLink(Course c, Department d) throws SQLException {
        String insertLink = "INSERT INTO CourseToDepartment VALUES(?,?,?);";
        try (PreparedStatement insert = con.prepareStatement(insertLink)) {
            insert.clearParameters();
            insert.setString(1, c.getCourseCode());
            insert.setString(2, d.getDeptCode());
            insert.setBoolean(3, c.getMainDep() == d);
            insert.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    private void insertBachEquiv(Course master, Course equiv) throws SQLException {

        String[] names = { "curseCode" };
        String[] values = { master.getCourseCode() };
        if (!ValueSetCheck("BachEquiv", names, values)) {
            String insertEquiv = "INSERT INTO BachEqiv VALUES(?,?);";
            try (PreparedStatement insert = con.prepareStatement(insertEquiv)) {
                insert.clearParameters();
                insert.setString(1, master.getCourseCode());
                insert.setString(2, equiv.getCourseCode());
                insert.executeUpdate();
                
            } catch (SQLException e) {
                throw e;
            }

        }
        
    }

    private void insertModule(Module m) throws SQLException {
        String[] names = { "moduleCode" };
        String[] values = { m.getModuleCode() };
        if (!ValueSetCheck("Modules", names, values)) {
            String insertModule = "INSERT INTO Modules VALUES(?,?,?,?);";
            try (PreparedStatement insert = con.prepareStatement(insertModule)) {
                insert.clearParameters();
                insert.setString(1, m.getModuleCode());
                insert.setString(2, m.getDepartment().getDeptCode());
                insert.setString(3, m.getFullName());
                insert.setString(4, m.getTimeTaught());
                insert.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }

    }

    private void insertModuleCourseLink(Module m, Course c, Boolean core, int lvl) throws SQLException {

        String[] names = { "moduleCode", "courseCode" };
        String[] values = { m.getModuleCode(), c.getCourseCode() };
        if (!ValueSetCheck("ModulesToCourses", names, values)) {
            String insertLink = "INSERT INTO ModulesToCourse VALUES(?,?,?,?);";
            try (PreparedStatement insert = con.prepareStatement(insertLink)) {
                insert.clearParameters();
                insert.setString(1, m.getModuleCode());
                insert.setString(2, c.getCourseCode());
                insert.setBoolean(3, core);
                insert.setInt(4, lvl);
                insert.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }

    }

    /**
     * Updates Supervision of the module
     * @param m module in question
     * @param d new supervising department
     * @return true if operation was succesfull false otherwise
     */
    public boolean changeModuleSupervison(Module m, Department d) {
        boolean succes = false;
        try {
            updateModuleSupervision(m, d);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return succes;
    }

    //UPDATES - PRIVATE FUNCTIONS

    private void updateModuleSupervision(Module m, Department d) throws SQLException {
        String updateModul = "UPDATE Modules SET departmentCode = ? WHERE moduleCode = ?;";
        try (PreparedStatement update = con.prepareStatement(updateModul)) {
            update.clearParameters();
            update.setString(1, d.getDeptCode());
            update.setString(2, m.getModuleCode());
            update.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

}
