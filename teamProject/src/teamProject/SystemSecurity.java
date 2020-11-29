package teamProject;

import java.nio.charset.Charset;
import java.security.*;
import java.util.*;

import teamProject.Classes.*;
import teamProject.db.Database;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/**
 * Class responsible for authentication and
 * authorization of the system access
 */
public class SystemSecurity {

    private static User currentUser = null;

    private final static Charset charset = Charset.forName("UTF-8");

    private static String getSalt() {
        byte[] salt = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            salt = new byte[16];
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteAToString(salt);
    }

    private static String hashString(String s, String salt) {
        String result = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(charset));
            byte[] hash = md.digest(s.getBytes(charset));
            result = byteAToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    private static String byteAToString(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            sb.append(Integer.toString((a[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Generates new hash and salt from password
     * @param password to hash
     * @return List where list[0] is hash generated and list[1] is salt
     */
    public static ArrayList<String> getHashAndSalt(String password) {
        String salt = getSalt();
        String hash = hashString(password, salt);
        ArrayList<String> ans = new ArrayList<String>();
        ans.add(hash);
        ans.add(salt);
        return ans;
    }

    /**
     * User login procedure checks credential and posiibly sets current user
     * @param username username provided
     * @param password password entered
     * @return true if log in succesfull false otherwise
     */
    public static boolean login(String username, String password) {
        try(Database db = StudentSystem.connect()){
            ArrayList<String> temp = db.getLoginData(username);
            if (!temp.isEmpty()) {
                String trueHash = temp.get(0);
                String salt = temp.get(1);
                String calcHash = hashString(password, salt);
                if (calcHash.equals(trueHash)) {
                    getAccessibleData(username, Integer.parseInt(temp.get(2)));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void getAccessibleData(String username, int lvl) {
        try (Database db = StudentSystem.connect()) {
            if (lvl > 0) {
                db.instantiateUsers();
                switch (lvl) {
                    case 1:
                        currentUser = Teacher.getInstance(username);
                        break;
                    case 2:
                        currentUser = Registrar.getInstance(username);
                        break;
                    case 3:
                        currentUser = Administrator.getInstance(username);
                        break;
                }
            } else {
                //TODO initialisation for students
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User getCurrentUser(){
        return currentUser;
    }

}
