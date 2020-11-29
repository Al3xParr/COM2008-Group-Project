package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.ArrayList;

/**
 * User class extention, Student class definition
*/
public class Student extends User {

    private int regNum;
    private String title, surname, forenames, email, tutor;
    private Course course;
    private ArrayList<StudyPeriod> studyPeriodList;

    public Student(String username, String passwordHash, String salt, int regNum, String title, String surname,
            String forenames, String email, String tutor, Course course, ArrayList<StudyPeriod> studyPeriodList) {

        super(username, passwordHash, salt);

        this.title = title;
        this.surname = surname;
        this.forenames = forenames;
        this.email = email;
        this.tutor = tutor;
        this.course = course;
        this.studyPeriodList = studyPeriodList;
    }

    public int getRegNum() {
        return this.regNum;
    }

    public void setRegNum(int regNum) {
        this.regNum = regNum;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getForenames() {
        return this.forenames;
    }

    public void setForenames(String forenames) {
        this.forenames = forenames;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTutor() {
        return this.tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ArrayList<StudyPeriod> getStudyPeriodList() {
        return this.studyPeriodList;
    }

    public void setStudyPeriodList(ArrayList<StudyPeriod> studyPeriodList) {
        this.studyPeriodList = studyPeriodList;
    }
    

}