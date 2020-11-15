package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * User class extention, Registrar class definition
*/
public class Registrar extends User {

    public Registrar(String username, String passwordHash, String salt){
        super(username, passwordHash, salt);
    }
}

