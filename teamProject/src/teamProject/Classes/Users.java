package teamProject.Classes;

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

public abstract class Users {

    protected String username, passwordHash, salt;
    
    public Users(String username, String passwordHash, String salt) {
		this.username = username;
        this.passwordHash = passwordHash;
        this.salt = salt;
	}
}


