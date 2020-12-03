package teamProject.Classes;

import teamProject.db.Database;
import teamProject.*;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * User class definition
*/

public abstract class User {

    protected String username, passwordHash, salt;
    
    public User(String username, String passwordHash, String salt) {
      this.username = username;
      this.passwordHash = passwordHash;
      this.salt = salt;
    }

    public Boolean delete() {
      try (Database db = StudentSystem.connect()) {
        return db.deleteUser(this);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return false;
    }

    public static Boolean checkPrimaryKeyExists(String key) {
      try (Database db = StudentSystem.connect()) {
        String[] names = { "username" };
        String[] values = { key };
        return db.ValueSetCheck("Accounts", names, values);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return true;
    }
    
  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswordHash() {
    return this.passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getSalt() {
    return this.salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

}


