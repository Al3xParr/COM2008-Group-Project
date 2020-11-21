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
    HashMap<String, Module> modules = new HashMap<>();
    HashMap<String, Course> courses = new HashMap<>();
    HashMap<String, Department> departments = new HashMap<>();
    HashMap<Integer, Student> students = new HashMap<>();
    HashMap<String, Administrator> administrators = new HashMap<>();
    HashMap<String, Registrar> registrars = new HashMap<>();
    HashMap<String, Teacher> teachers = new HashMap<>();


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

    private Set<String> getTableNames() {
        Set<String> tables = null;
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT TABLE_NAME, TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES "
                    + "WHERE table_schema = (SELECT DATABASE());");
            tables = new HashSet<String>();
            while (results.next()) {
                tables.add(results.getString("TABLE_NAME"));
            }

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return columns;
    }

    /**
     * Checks if given value is already used in a table
     * @param tableName table to check
     * @param columnName column to check
     * @param value value to took for
     * @return true if value exists in given coulmn or there was an error false otherwise
     */
    public boolean valueExists(String tableName, String columnName, String value) {
        if (getTableNames().contains(tableName) && getColumnNames(tableName).contains(columnName)) {
            try (Statement stsm = con.createStatement()) {

                ResultSet results = stsm.executeQuery("SELECT DISTINCT " + columnName + " FROM " + tableName + ";");

                while (results.next()) {
                    if (results.getString(1) == value) {
                        return true;
                    }
                }
                return false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    //INSERT FUNCTIONS

    private void insertStudyPeriod(StudyPeriod newPeriod, Student student) throws SQLException {
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

    /**
     * Inserts information about new study period into database
     * @param newPeriod new StudyPeriod object to be assigned to student
     * @param to Student the study period belongs to
     * @return true if operation was compleated false if not
     */
    public boolean addStudyPeriod(StudyPeriod newPeriod, Student to) {
        boolean succes = true;
        try {
            insertStudyPeriod(newPeriod, to);
        } catch (Exception e) {
            e.printStackTrace();
            succes = false;
        }

        return succes;

    }

    private void insertStudent(Student newStudent) throws SQLException {

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
                insertStudyPeriod(x, newStudent);
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    private void insertTeacher(Teacher newTeacher) throws SQLException {

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
    public boolean addUser(User newUser) {

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
                    insertStudent((Student) newUser);
                    break;
                case "Teacher":
                    insertTeacher((Teacher) newUser);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            succes = false;
        }

        return succes;

    }

    /**
     * Inserts information about new Department into database
     * @param newDepartment new Department to be inserted
     * @return true if the opperation was succesful
     */
    public boolean addDepartment(Department newDepartment) {
        boolean succes = true;

        String insertDep = "INSERT INTO Departments VALUES (?,?);";
        try (PreparedStatement insert = con.prepareStatement(insertDep)) {
            insert.clearParameters();
            insert.setString(1, newDepartment.getDeptCode());
            insert.setString(2, newDepartment.getFullName());
            insert.executeUpdate();
            for (Course c : newDepartment.getCourseList()) {
                insertCourseDepartLink(c, newDepartment);
            }
            for (Module m : newDepartment.getModuleList()) {
                updateModuleSupervision(m, newDepartment);
            }

        } catch (Exception e) {
            e.printStackTrace();
            succes = false;
        }

        return succes;

    }

    private void insertCourseDepartLink(Course c, Department d) throws SQLException{
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

    //UPDATE QUERIES

    private void updateModuleSupervision(Module m, Department d) throws SQLException{
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

    /**
     * Updates Supervidion of the module
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

    public void instantiateUsers() {
        instantiateModule();
        instantiateCourse();
        instantiateAdministrator();
        instantiateRegistrar();
        instantiateTeachers();
        instantiateStudent();
    }


    public void instantiateModule() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM MODULE;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String moduleCode = results.getString(1);
                String departmentCode = results.getString(2);
                String fullName = results.getString(3);
                int moduleCredits = results.getInt(4);
                String timeTaught = results.getString(5);
                Module module = new Module(moduleCode, departmentCode, fullName, moduleCredits, timeTaught);
                modules.put(moduleCode, module);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateCourse() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM COURSE;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String courseCode = results.getString(1);
                String fullName = results.getString(2);
                Boolean yearInIndustry = results.getBoolean(3);
                Course bachEquiv = (Course) results.getObject(4);
                Department mainDep = (Department) results.getObject(5);
                Course course = new Course(courseCode, fullName, yearInIndustry, bachEquiv, mainDep);
                courses.put(courseCode, course);
            }
            ResultSet results2 = stsm.executeQuery("SELECT * FROM BACHEQUIV WHERE ;");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateDepartment() {
        try (Statement stsm = con.createStatement()) {
            ArrayList<Course> coursesList = new ArrayList<Course>();
            ResultSet results = stsm.executeQuery("SELECT * FROM Departments;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String deptCode = results.getString(1);
                String fullName = results.getString(2);
                Department department = new Department(deptCode, fullName, null, null);
                ResultSet coursesResults = stsm.executeQuery("SELECT * FROM CourseToDepartment WHERE deptCode = "
                        + deptCode +";");
                coursesList.clear();
                while (coursesResults.next()) {
                    coursesList.add(courses.get(results.getString(1)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //instantiating the users from the databases
    public void instantiateTeachers() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM TEACHERS;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String username = results.getString(1);
                String passwordHash = results.getString(2);
                String salt = results.getString(3);
                String fullName = results.getString(4);
                Teacher teacher = new Teacher(username, passwordHash, salt, fullName);
                teachers.put(username, teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateRegistrar() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Accounts;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String username = results.getString(1);
                String passwordHash = results.getString(2);
                String salt = results.getString(3);
                Registrar registrar = new Registrar(username, passwordHash, salt);
                registrars.put(username, registrar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateAdministrator() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Accounts;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String username = results.getString(1);
                String passwordHash = results.getString(2);
                String salt = results.getString(3);
                Administrator administrator = new Administrator(username, passwordHash, salt);
                administrators.put(username, administrator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instantiateStudent() {
        try (Statement stsm = con.createStatement()) {

            ResultSet results = stsm.executeQuery("SELECT * FROM Students;");

            while (results.next()) {
                //not sure where the attributes are stored yet, can change later
                String username = results.getString(1);
                String passwordHash = results.getString(2);
                String salt = results.getString(3);
                int regNumber = results.getInt(4);
                String title = results.getString(5);
                String surname = results.getString(6);
                String fornames = results.getString(7);
                String email = results.getString(8);
                String tutor = results.getString(9);
                Course course = (Course) results.getObject(10);
                StudyPeriod[] studyPeriodList = results.getObject(11);

                Student student = new Student(username, passwordHash, salt, regNumber, title, surname, fornames, email,
                        tutor, course, studyPeriodList);
                students.put(regNumber, student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
