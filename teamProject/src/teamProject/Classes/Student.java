package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * User class extention, Student class definition
*/
public class Student extends Users {

    private int regNum;
    private String title, surname, forenames, email, tutor;
    private Course course;
    private StudyPeriod[] studyPeriodList;

    public Student(String username, String passwordHash, String salt,
    int regNum, String title, String surname, String forenames, String email,
    String tutor, Course course, StudyPeriod[] studyPeriodList){

        super(username, passwordHash, salt);
        
        this.title = title;
        this.surname = surname;
        this.forenames = forenames;
        this.email = email;
        this.tutor = tutor;
        this.course = course;
        this.studyPeriodList = studyPeriodList;
    }

}