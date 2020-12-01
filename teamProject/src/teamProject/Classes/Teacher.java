package teamProject.Classes;

import java.util.ArrayList;

import teamProject.*;
import teamProject.db.Database;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.HashMap;

/** 
 * User class extention, Teacher class definition
*/
public class Teacher extends User {

    private String fullName;
    public static HashMap<String, Teacher> instances = new HashMap<>();

    public Teacher(String username, String passwordHash, String salt, String fullName) {
        super(username, passwordHash, salt);
        this.fullName = fullName;
        instances.put(username, this);

    }

    public static Teacher createNew(String username, String password, String fullName) {
        ArrayList<String> temp = SystemSecurity.getHashAndSalt(password);
        String hash = temp.get(0);
        String salt = temp.get(1);
        Teacher news = new Teacher(username, hash, salt, fullName);
        try (Database db = StudentSystem.connect()) {
            db.addUser(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public static Teacher getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public static HashMap<String, Teacher> getAllInstances(){
        return instances;
    }

}