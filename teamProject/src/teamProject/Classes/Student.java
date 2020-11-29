package teamProject.Classes;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.ArrayList;
import java.util.HashMap;

import teamProject.*;
import teamProject.db.Database;

/**
 * User class extention, Student class definition
*/
public class Student extends User {

    private int regNum;
    private String title, surname, forenames, email, tutor;
    private Course course;
    private ArrayList<StudyPeriod> studyPeriodList;
    private String degreeLvl;

    public static HashMap<Integer, Student> instances = new HashMap<>();

    public Student(String username, String passwordHash, String salt, int regNum, String title, String surname,
            String forenames, String email, String tutor, Course course, ArrayList<StudyPeriod> studyPeriodList,String degreeLvl) {

        super(username, passwordHash, salt);

        this.title = title;
        this.surname = surname;
        this.forenames = forenames;
        this.email = email;
        this.tutor = tutor;
        this.course = course;
        this.studyPeriodList = studyPeriodList;
        this.degreeLvl = degreeLvl;
        instances.put(regNum, this);

    }

    public static Student createNew(String username, String password, String title, String surname, String forenames,
            String tutor, Course course, StudyPeriod firstPeriod) {
        ArrayList<String> temp = SystemSecurity.getHashAndSalt(password);
        String hash = temp.get(0);
        String salt = temp.get(1);
        int regNum = -1;
        String email = getNewEmail(forenames, surname);
        ArrayList<StudyPeriod> periods = new ArrayList<>();
        periods.add(firstPeriod);
        Student news = new Student(username, hash, salt, regNum, title, surname, forenames, email, tutor, course,
                periods, firstPeriod.getDegreeLvl().getDegreeLvl());
        try (Database db = StudentSystem.connect()) {
            regNum = db.addUser(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        news.setRegNum(regNum);
        return news;
    }

    private static String getNewEmail(String f,String s){
        String prefix = "";
        for (String x : f.split(" ")) {
            if (x.length() > 0) {
                prefix = prefix + x.charAt(0);
            }
        }
        prefix += s;
        try (Database db = StudentSystem.connect()) {
            prefix += Integer.toString(db.countEmails(prefix)+1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prefix + "@university.com";
    

    }

    public static Student getInstance(Integer key) {
        return instances.get(key);
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

    public String getDegreeLvl(){
        return this.degreeLvl;
    }

    public void setDegreeLvl(String lvl) {
        this.degreeLvl = lvl;
    }

}
