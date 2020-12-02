package teamProject.Classes;



import java.util.ArrayList;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.HashMap;

import teamProject.*;
import teamProject.db.*;

/** 
 * User class extention, Registrar class definition
*/
public class Registrar extends User {

    public static HashMap<String, Registrar> instances = new HashMap<>();

    public Registrar(String username, String passwordHash, String salt) {
        super(username, passwordHash, salt);
        instances.put(username, this);

    }

    public static Registrar createNew(String username, String password) {
        ArrayList<String> temp = SystemSecurity.getHashAndSalt(password);
        String hash = temp.get(0);
        String salt = temp.get(1);
        Registrar news = new Registrar(username, hash, salt);
        try (Database db = StudentSystem.connect()) {
            db.addUser(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public static Registrar getByUsername(String username) {
        Registrar res = null;
        for (Registrar x : instances.values()) {
            if (x.getUsername().equals(username)) {
                res = x;
            }
        }

        return res;
    }

    public static Registrar getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public static HashMap<String, Registrar> getAllInstances(){
        return instances;
    }
}
