package teamProject;

import teamProject.db.*;
import teamProject.GUI.*;
import teamProject.Classes.*;
import teamProject.Classes.Module;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * Main class and starting point of the application
*/
public class StudentSystem {

    final static String url = "//stusql.dcs.shef.ac.uk/team044";
    final static String user = "team044";
    final static String pass = "872345c0";

    public static void reinstance(){
        clearHashMaps();

        try (Database db = connect()) {
            db.instantiateUsers();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void clearHashMaps() {
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

    public static Database connect() {
        Database db = null;
        try {
            db = new Database(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }

    public static void main(String[] args) {

        try (Database db = connect()) {
            //DO STUFF HERE

            db.resetDB();
            db.populateDB();
            db.instantiateUsers();
            new MainFrame();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
