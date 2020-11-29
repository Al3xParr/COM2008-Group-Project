package teamProject.Classes;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.HashMap;

/** 
 * User class extention, Registrar class definition
*/
public class Registrar extends User {

    public static HashMap<String, Registrar> instances = new HashMap<>();

    public Registrar(String username, String passwordHash, String salt) {
        super(username, passwordHash, salt);
        instances.put(username, this);

    }

    public static Registrar getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }
}
