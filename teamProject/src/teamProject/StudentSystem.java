package teamProject;

import java.util.*;
import java.sql.*;
import teamProject.db.*;

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
    public static Database db = null;
    public static void main(String[] args) {

        try{

            //DO STUFF HERE
            db = new Database(url, user, pass);
            db.resetDB();
            db.populateDB();
            

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(db != null){
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
