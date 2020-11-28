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
import java.util.*;
import java.sql.Date;
import teamProject.Classes.*;
import teamProject.Classes.Module;

/**
 * Class representing and operating database
 */
public class Database implements AutoCloseable {

    Connection con = null;

    public Database(String url, String user, String password) throws SQLException {
        String str = "jdbc:mysql:" + url + "?user=" + user + "&password=" + password + "&allowMultiQueries=true";
        con = DriverManager.getConnection(str);
    }

    /*
    public void resetDB() {
        try (Statement statement = con.createStatement()){
            String query = "TRUNCATE TABLE Teacher; TRUNCATE TABLE Students;" +
                           "TRUNCATE TABLE StudentsToModules; TRUNCATE TABLE Modules;" +  
                           "TRUNCATE TABLE CourseToDepartment; TRUNCATE TABLE Departments;" + 
                           "TRUNCATE TABLE Course; TRUNCATE TABLE BachEquiv;" + 
                           "TRUNCATE TABLE ModulesToCourse;";
            statement.executeUpdate(query);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
    }
    */

    /*public void resetTable(String tblName){
        String query = "TRUNCATE TABLE ?;";
        try (PreparedStatement prepState = con.prepareStatement(query)){
            prepState.clearParameters();
            prepState.setString(1, tblName);
            prepState.executeUpdate();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }*/

    public void resetDB() {
        String createQuery = new String();

        try {
            createQuery = new String(Files.readAllBytes(Paths.get("teamProject/db/dbSchema.sql")));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(createQuery);
            System.out.println("Database Created");

        } catch (Exception ex) {
            System.out.println("Database creation failed");
            ex.printStackTrace();
        }
    }

    public void populateDB() {
        String populateQuery = new String();

        try {
            populateQuery = new String(Files.readAllBytes(Paths.get("teamProject/db/dbTestData.sql")));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (Statement statement = con.createStatement()) {
            statement.executeUpdate(populateQuery);
            System.out.println("Database Populated");

        } catch (Exception ex) {
            System.out.println("Database population failed");
            ex.printStackTrace();
        }
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
            int type = -1;
            if (newUser instanceof Student) {
                type = 0;
            } else if (newUser instanceof Teacher) {
                type = 1;
            } else if (newUser instanceof Registrar) {
                type = 2;
            } else if (newUser instanceof Administrator) {
                type = 3;
            }

            String insertUser = "INSERT INTO Account VALUES(?,?,?,?);";
            try (PreparedStatement insert = con.prepareStatement(insertUser)) {
                insert.clearParameters();
                insert.setString(1, newUser.getUsername());
                insert.setString(2, newUser.getPasswordHash());
                insert.setString(3, newUser.getSalt());
                insert.setInt(4, type);
                insert.executeUpdate();
                switch (type) {
                    case 0:
                        insertStudent((Student) newUser);
                        break;
                    case 1:
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
        if (newDepartment.getModuleList().isEmpty() && !ValueSetCheck("Departments", names, values)) {

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

        String[] names = { "regNum", "label" };
        String[] values = { Integer.toString(student.getRegNum()), "" + newPeriod.getLabel() };
        if (!ValueSetCheck("StudyPeriods", names, values)) {
            String insertPeriod = "INSERT INTO StudyPeriod VALUES(?,?,?,?,?,?);";

            try (PreparedStatement insert = con.prepareStatement(insertPeriod)) {
                insert.clearParameters();
                insert.setInt(1, student.getRegNum());
                insert.setString(2, "" + newPeriod.getLabel());
                insert.setString(3, student.getCourse().getCourseCode());
                insert.setInt(4, Integer.parseInt(newPeriod.getDegreeLvl().getDegreeLvl()));
                insert.setDate(5, new Date(newPeriod.getStartDate().getTime()));
                insert.setDate(6, new Date(newPeriod.getEndDate().getTime()));
                insert.executeUpdate();

                for (Grade g : newPeriod.getGradesList()) {
                    String[] namess = { "regNum", "moduleCode", "label" };
                    String[] valuess = { Integer.toString(student.getRegNum()), g.getModule().getModuleCode(),
                            "" + newPeriod.getLabel() };
                    if (!ValueSetCheck("Grades", namess, valuess)) {
                        String insertGrades = "INSERT INTO Grades VALUES(?,?,?,?,?);";
                        try (PreparedStatement insertGrade = con.prepareStatement(insertGrades)) {
                            insert.clearParameters();
                            insert.setInt(1, student.getRegNum());
                            insert.setString(2, "" + newPeriod.getLabel());
                            insert.setString(3, g.getModule().getModuleCode());
                            insert.setDouble(4, g.getMark());
                            insert.setDouble(5, g.getResitMark());
                            insert.executeUpdate();
                        } catch (SQLException e) {
                            throw e;
                        }
                    }
                }
            } catch (SQLException e) {
                throw e;
            }
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

            String insertCourse = "INSERT INTO Course VALUES(?,?,?);";
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
                insert.setString(2, m.getDepartmentCode());
                insert.setString(3, m.getFullName());
                insert.setString(4, m.getTimeTaught());
                insert.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }

    }

    private void insertModuleCourseLink(Module m, Course c, Boolean core, String lvl) throws SQLException {

        String[] names = { "moduleCode", "courseCode" };
        String[] values = { m.getModuleCode(), c.getCourseCode() };
        if (!ValueSetCheck("ModulesToCourses", names, values)) {
            String insertLink = "INSERT INTO ModulesToCourse VALUES(?,?,?,?);";
            try (PreparedStatement insert = con.prepareStatement(insertLink)) {
                insert.clearParameters();
                insert.setString(1, m.getModuleCode());
                insert.setString(2, c.getCourseCode());
                insert.setBoolean(3, core);
                insert.setString(4, lvl);
                insert.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }

    }

    //UPDATES
    /**
     * Changes the grade for given student
     * @param s student the grade belongs to
     * @param p study period in wchich the grade was awarded
     * @param m module the grade is for
     * @param resit is it resit
     * @param newGrade the new grade
     * @return true if operation was succesfull
     */
    public boolean changeGrade(Student s, StudyPeriod p, Module m, boolean resit, double newGrade) {

        boolean succes = false;
        String updateGrade = "UPDATE Grades SET " + (resit ? "resitMark" : "mark") + " = ?"
                + "WHERE regNum = ? AND label = ? AND moduleCode = ?;";
        try (PreparedStatement update = con.prepareStatement(updateGrade)) {

            update.clearParameters();
            update.setDouble(1, newGrade);
            update.setInt(2, s.getRegNum());
            update.setString(3, "" + p.getLabel());
            update.setString(4, m.getModuleCode());
            update.executeUpdate();
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return succes;
    }

    //DELETIONS

    /**
     * Delete module and related records from database
     * @param m module to be deleted
     * @return true if operation was succesfull
     */
    public boolean deleteModule(Module m) {
        boolean succes = false;
        String deleteModule = "DELETE FROM Modules WHERE moduleCode = ?;";
        try (PreparedStatement delete = con.prepareStatement(deleteModule)) {
            con.setAutoCommit(false);
            delete.clearParameters();
            delete.setString(1, m.getModuleCode());
            delete.executeUpdate();
            con.setAutoCommit(true);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception en) {
                en.printStackTrace();
            }
        }
        return succes;
    }

    /**
     * Delete course and related records from database
     * @param c course to be deleted
     * @return true if operation was succesfull
     */
    public boolean deleteCourse(Course c) {
        boolean succes = false;
        String deleteCourse = "DELETE FROM Courses WHERE courseCode = ?;";
        try (PreparedStatement delete = con.prepareStatement(deleteCourse)) {
            con.setAutoCommit(false);
            delete.clearParameters();
            delete.setString(1, c.getCourseCode());
            delete.executeUpdate();
            con.setAutoCommit(true);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception en) {
                en.printStackTrace();
            }
        }
        return succes;
    }

    /**
     * Delete department and related records from database
     * @param d department to be deleted
     * @return true if operation was succesfull
     */
    public boolean deleteDepartment(Department d) {
        boolean succes = false;
        String deleteDepartment = "DELETE FROM Departments WHERE deptCode = ?;";
        try (PreparedStatement delete = con.prepareStatement(deleteDepartment)) {
            con.setAutoCommit(false);
            delete.clearParameters();
            delete.setString(1, d.getDeptCode());
            delete.executeUpdate();
            con.setAutoCommit(true);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception en) {
                en.printStackTrace();
            }
        }
        return succes;
    }

    /**
     * Delete study period and related records from database
     * @param p study period to be deleted
     * @param s owner of thestudyperiod
     * @return true if operation was succesfull
     */
    public boolean deleteStudyPeriod(StudyPeriod p, Student s) {
        boolean succes = false;
        String deleteStudyPeriod = "DELETE FROM StudyPeriods WHERE regNum = ? AND label = ?;";
        try (PreparedStatement delete = con.prepareStatement(deleteStudyPeriod)) {
            con.setAutoCommit(false);
            delete.clearParameters();
            delete.setInt(1, s.getRegNum());
            delete.setString(2, "" + p.getLabel());
            delete.executeUpdate();
            con.setAutoCommit(true);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception en) {
                en.printStackTrace();
            }
        }
        return succes;
    }

    /**
     * Delete study level and related records from database
     * @param l study level to be deleted
     * @param c owner of the study level
     * @return true if operation was succesfull
     */
    public boolean deleteStudyLevel(StudyLevel l, Course c) {
        boolean succes = false;
        String deleteStudyLevel = "DELETE FROM ModulesToCourses WHERE courseCode = ? AND degreeLvl = ?;";
        try (PreparedStatement delete = con.prepareStatement(deleteStudyLevel)) {
            con.setAutoCommit(false);
            delete.clearParameters();
            delete.setString(1, c.getCourseCode());
            delete.setString(2, l.getDegreeLvl());
            delete.executeUpdate();
            con.setAutoCommit(true);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception en) {
                en.printStackTrace();
            }
        }
        return succes;
    }

    /**
     * Delete user and related records from database
     * @param u user to be deleted
     * @return true if operation was succesfull
     */
    public boolean deleteUser(User u) {
        boolean succes = false;
        String deleteUser = "DELETE FROM Accounts WHERE username = ?;";
        try (PreparedStatement delete = con.prepareStatement(deleteUser)) {
            con.setAutoCommit(false);
            delete.clearParameters();
            delete.setString(1, u.getUsername());
            delete.executeUpdate();
            con.setAutoCommit(true);
            succes = true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception en) {
                en.printStackTrace();
            }
        }
        return succes;
    }

    public void instantiateUsers() {
        clearHashMaps();
        instantiateModule();
        instantiateCourse();
        addBachEquiv();
        instantiateDepartment();
        instantiateStudyLevel();
        instantiateStudyPeriod();
        addCourseInformation();
        instantiateAdministrator();
        instantiateRegistrar();
        instantiateTeachers();
        instantiateStudent();
    }

    private void clearHashMaps() {
        Administrator.clearInstances();
        Course.clearInstances();
        Department.clearInstances();
        Module.clearInstances();
        Registrar.clearInstances();
        Student.clearInstances();
        StudyLevel.clearInstances();
        StudyPeriod.clearInstances();
        Teacher.clearInstances();
    }

    public void instantiateModule() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Modules;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String moduleCode = results.getString(1);
                String departmentCode = results.getString(2);
                String fullName = results.getString(3);
                String timeTaught = results.getString(4);
                new Module(moduleCode, departmentCode, fullName, timeTaught);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateCourse() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Course;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String courseCode = results.getString(1);
                String fullName = results.getString(2);
                Boolean yearInIndustry = results.getBoolean(3);
                new Course(courseCode, fullName, yearInIndustry, null, null, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //adding the bachEquiv for all the relevant courses for which it applies
    public void addBachEquiv() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Course;");

            while (results.next()) {
                String courseCode = results.getString(1);
                try (Statement stsm2 = con.createStatement()) {
                    ResultSet bachResult = stsm2
                            .executeQuery("SELECT * FROM BachEquiv WHERE courseCode = '" + courseCode + "';");
                    if (bachResult.next()) {
                        Course bachEquiv = Course.getInstance(bachResult.getString(2));
                        Course course = Course.getInstance(courseCode);
                        course.setBachEquiv(bachEquiv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //instantiating the departments
    public void instantiateDepartment() {
        try (Statement stsm = con.createStatement()) {
            ArrayList<Course> coursesList = new ArrayList<Course>();
            ArrayList<Module> modulesList = new ArrayList<Module>();
            ResultSet results = stsm.executeQuery("SELECT * FROM Departments;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String deptCode = results.getString(1);
                String fullName = results.getString(2);
                try (Statement stsm2 = con.createStatement()) {
                    //finding the courses from that department
                    ResultSet coursesResults = stsm2
                            .executeQuery("SELECT * FROM CourseToDepartment WHERE deptCode = '" + deptCode + "';");
                    coursesList.clear();
                    while (coursesResults.next()) {
                        coursesList.add(Course.getInstance(results.getString(1)));
                    }
                    //finding the modules within that department
                    ResultSet modulesResults = stsm2
                            .executeQuery("SELECT * FROM Modules WHERE deptCode = '" + deptCode + "';");
                    modulesList.clear();
                    while (modulesResults.next()) {
                        modulesList.add(Module.getInstance(modulesResults.getString(3)));
                    }
                    new Department(deptCode, fullName, modulesList, coursesList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //instantiating each of the study levels
    public void instantiateStudyLevel() {
        try (Statement stsm = con.createStatement()) {

            //result sets for finding the courses and the degree levels
            ResultSet courseResults = stsm.executeQuery("SELECT DISTINCT courseCode FROM ModulesToCourse;");

            while (courseResults.next()) {
                try (Statement stsm2 = con.createStatement()) {
                    String courseCode = courseResults.getString(1);
                    ResultSet lvlResults = stsm2.executeQuery(
                            "SELECT DISTINCT degreeLvl FROM ModulesToCourse WHERE courseCode = '" + courseCode + "';");
                    while (lvlResults.next()) {
                        try (Statement stsm3 = con.createStatement()) {
                            String degreeLvl = lvlResults.getString(1);
                            ArrayList<Module> coreModules = new ArrayList<Module>();
                            ArrayList<Module> optionalModules = new ArrayList<Module>();

                            ResultSet coreModuleList = stsm3
                                    .executeQuery("SELECT moduleCode FROM ModulesToCourse WHERE degreeLvl = '"
                                            + degreeLvl + "' AND courseCode = '" + courseCode + "' AND core = True;");
                            while (coreModuleList.next()) {
                                String moduleCode = coreModuleList.getString(1);
                                coreModules.add(Module.getInstance(moduleCode));
                            }

                            ResultSet optionalModuleList = stsm3
                                    .executeQuery("SELECT moduleCode FROM ModulesToCourse WHERE degreeLvl = '"
                                            + degreeLvl + "' AND courseCode = '" + courseCode + "' AND core = False;");
                            while (optionalModuleList.next()) {
                                String moduleCode = optionalModuleList.getString(1);
                                optionalModules.add(Module.getInstance(moduleCode));
                            }

                            new StudyLevel(degreeLvl, courseCode, coreModules, optionalModules);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //instantiating each of the study periods
    public void instantiateStudyPeriod() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM StudyPeriods;");

            while (results.next()) {
                int regNum = results.getInt(1);
                String label = results.getString(2);
                String courseCode = results.getString(3);
                String degreeLvl = results.getString(4);
                Date startDate = results.getDate(5);
                Date endDate = results.getDate(6);
                String gradeQuery = "SELECT moduleCode, mark, resitMark FROM Grades WHERE regNum = ? AND label = ?;";
                try (PreparedStatement stsm2 = con.prepareStatement(gradeQuery)) {
                    stsm2.clearParameters();
                    stsm2.setInt(1, regNum);
                    stsm2.setString(2, label);
                    ResultSet gradesResult = stsm2.executeQuery();
                    ArrayList<Grade> gradesList = new ArrayList<>();
                    while (gradesResult.next()) {
                        Grade g = new Grade(Module.getInstance(gradesResult.getString(1)), gradesResult.getDouble(2),
                                gradesResult.getDouble(3));
                        gradesList.add(g);
                    }
                    new StudyPeriod(regNum, label, startDate, endDate, StudyLevel.getInstance(degreeLvl + courseCode),
                            gradesList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //adding the departmentList, the degreeLvlList, mainDept
    public void addCourseInformation() {
        try (Statement stsm = con.createStatement()) {

            ArrayList<Department> otherDepartments = new ArrayList<Department>();
            ResultSet results = stsm.executeQuery("SELECT courseCode FROM Course;");

            while (results.next()) {
                String courseCode = results.getString(1);
                Course course = Course.getInstance(courseCode);
                try (Statement stsm2 = con.createStatement()) {
                    //setting the main department
                    ResultSet mainDeptResult = stsm2
                            .executeQuery("SELECT deptCode FROM CourseToDepartment WHERE courseCode = '" + courseCode
                                    + "' AND mainDept = True;");
                    Department mainDept = null;
                    if (mainDeptResult.next()) {
                        mainDept = Department.getInstance(mainDeptResult.getString(1));
                    }
                    course.setMainDep(mainDept);
                    //setting the other departments
                    ResultSet deptResult = stsm2.executeQuery(
                            "SELECT deptCode FROM CourseToDepartment WHERE courseCode = '" + courseCode + "';");
                    otherDepartments.clear();
                    while (deptResult.next()) {
                        otherDepartments.add(Department.getInstance(deptResult.getString(1)));
                    }
                    course.setDepartmentList(otherDepartments);
                    //TO-DO: setting the degree level list
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //instantiating the users from the databases
    public void instantiateTeachers() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT Accounts.username,passwordHash,salt,fullName"
                    + " FROM Accounts JOIN Teacher ON Accounts.username = Teacher.username WHERE accessLvl = 1;");

            while (results.next()) {
                String username = results.getString(1);
                String passwordHash = results.getString(2);
                String salt = results.getString(3);
                String fullName = results.getString(4);
                new Teacher(username, passwordHash, salt, fullName);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateRegistrar() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Accounts WHERE accessLvl = 2;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String username = results.getString(1);
                String passwordHash = results.getString(2);
                String salt = results.getString(3);
                new Registrar(username, passwordHash, salt);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateAdministrator() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Accounts WHERE accessLvl = 3;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String username = results.getString(1);
                String passwordHash = results.getString(2);
                String salt = results.getString(3);
                new Administrator(username, passwordHash, salt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateStudent() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT regNum, title, surname, forenames, email, Students.username, "
                    + "tutor, courseCode, degreeLvl, passwordHash, salt FROM "
                    + "Students INNER JOIN Accounts ON Students.username = Accounts.username;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String username = results.getString(6);
                String passwordHash = results.getString(9);
                String salt = results.getString(10);
                int regNumber = results.getInt(1);
                String title = results.getString(2);
                String surname = results.getString(3);
                String forenames = results.getString(4);
                String email = results.getString(5);
                String tutor = results.getString(7);
                Course course = Course.getInstance(results.getString(8));
                ArrayList<StudyPeriod> studyPeriodList = new ArrayList<StudyPeriod>();
                try (Statement stsm2 = con.createStatement()) {
                    ResultSet resultsStudyPeriods = stsm2
                            .executeQuery("SELECT label FROM StudyPeriods WHERE regNum = '" + regNumber + "';");

                    while (resultsStudyPeriods.next()) {
                        String label = resultsStudyPeriods.getString(1);
                        studyPeriodList.add(StudyPeriod.getInstance(regNumber + label));
                    }
                    new Student(username, passwordHash, salt, regNumber, title, surname, forenames, email, tutor,
                            course, studyPeriodList);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
