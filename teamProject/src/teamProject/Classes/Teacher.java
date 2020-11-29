package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * User class extention, Teacher class definition
*/
public class Teacher extends User {

    private String fullName;

    public Teacher(String username, String passwordHash, String salt, String fullName) {
        super(username, passwordHash, salt);
        this.fullName = fullName;
    }


    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    

}