package teamProject.Classes;
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
public class Administrator extends Users {

    public Administrator(String username, String passwordHash, String salt){
        super(username, passwordHash, salt);
    }
}