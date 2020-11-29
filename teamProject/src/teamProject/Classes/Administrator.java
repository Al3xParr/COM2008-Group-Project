package teamProject.Classes;

import java.util.*;

import teamProject.StudentSystem;
import teamProject.SystemSecurity;
import teamProject.db.Database;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * User class extention, Administrator class definition
*/
public class Administrator extends User {

    public static HashMap<String, Administrator> instances = new HashMap<>();

    public Administrator(String username, String passwordHash, String salt) {
        super(username, passwordHash, salt);
        instances.put(username, this);
    }

    public static Administrator createNew(String username, String password) {
        ArrayList<String> temp = SystemSecurity.getHashAndSalt(password);
        String hash = temp.get(0);
        String salt = temp.get(1);
        Administrator news = new Administrator(username, hash, salt);
        try(Database db = StudentSystem.connect()){
            db.addUser(news);
        }catch(Exception e){
            e.printStackTrace();
        }
        return news;
    }

    public static Administrator getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }
}