package teamProject.Classes;

import java.util.*;

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

    public static Administrator getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public static HashMap<String, Administrator> getAllInstances(){
        return instances;
    }
}