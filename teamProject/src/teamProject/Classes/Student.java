package teamProject.Classes;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * User class extention, Student class definition
*/
public class Student extends User {

    private int regNum;
    private String title, surname, forenames, email, tutor;
    private Course course;
    private ArrayList<StudyPeriod> studyPeriodList;

    public static HashMap<Integer, Student> instances = new HashMap<>();

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
        instances.put(regNum, this);

    }

    public static Student getInstance(Integer key) {
        return instances.get(key);
    }

    public static Collection<Student> allInstances() {
        return instances.values();
    }
    
    public static Student getByUsername(String username) {
        Student res = null;
        for (Student x : instances.values()) {
            if (x.getUsername().equals(username)) {
                res = x;
            }
        }

        return res;
    }

    public static void clearInstances() {
        instances.clear();
    }

    public ArrayList<Module> getLatestModules() {
        ArrayList<Module> ans = new ArrayList<>();
        if (!studyPeriodList.isEmpty()) {
            StudyPeriod latest = studyPeriodList.get(0);
            for (StudyPeriod sP : studyPeriodList) {
                if (latest.getLabel().compareTo(sP.getLabel()) < 0) {
                    latest = sP;
                }
            }

            for (Grade g : latest.getGradesList()) {
                ans.add(g.getModule());
            }
        }
        return ans;
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
