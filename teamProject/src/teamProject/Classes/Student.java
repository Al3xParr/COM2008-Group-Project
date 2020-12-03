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
import java.util.Date;
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
            String forenames, String email, String tutor, Course course, ArrayList<StudyPeriod> studyPeriodList,
            String degreeLvl) {

        super(username, passwordHash, salt);

        this.regNum = regNum;
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
            String tutor, Course course, String degreeLvl, Date startDate, Date endDate) {
        ArrayList<String> temp = SystemSecurity.getHashAndSalt(password);
        String hash = temp.get(0);
        String salt = temp.get(1);
        int regNum = -1;
        String email = getNewEmail(forenames, surname);
        Student news = new Student(username, hash, salt, regNum, title, surname, forenames, email, tutor, course,
                new ArrayList<StudyPeriod>(), degreeLvl);
        try (Database db = StudentSystem.connect()) {
            regNum = db.addUser(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        news.setRegNum(regNum);
        StudyPeriod.createNew(regNum, "A", startDate, endDate,
                StudyLevel.getInstance(degreeLvl + course.getCourseCode()));
        
        return news;
    }

    private static String getNewEmail(String f, String s) {
        String prefix = "";
        for (String x : f.split(" ")) {
            if (x.length() > 0) {
                prefix = prefix + x.charAt(0);
            }
        }
        prefix += s;
        try (Database db = StudentSystem.connect()) {
            prefix += Integer.toString(db.countEmails(prefix) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prefix + "@university.com";

    }

    public void reAddToInstances() {
        instances.put(getRegNum(), this);
        for (StudyPeriod sp : getStudyPeriodList()) {
            sp.reAddToInstances();
        }
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

    /**
     * Attempts to progress student to next period of study or graduate them
     * @return false if the registration or mark assignment is not complete
     */
    public boolean nextYear() {
        if (degreeLvl.equals("P")) {
            progress();
        }
        if (isRegistrationComplete() && areMarksAssigned()) {
            if (getCourse().getDegreeLvlList().size() == 1) {
                StudyPeriod last = getCurrentStudyPeriod();
                if (last.isFail()) {
                    if (getStudyPeriodList().size() == 2) {
                        changeLvl("G");
                        return true;
                    }
                    repeatStudyPeriod();
                    return true;
                } else {
                    changeLvl("G");
                    return true;
                }
            }

            StudyPeriod last = getCurrentStudyPeriod();
            StudyPeriod second = getPreviousStudyPeriod();

            if (isInFinalYear()) {
                if (second.getDegreeLvl().getDegreeLvl().equals(last.getDegreeLvl().getDegreeLvl())) {
                    changeLvl("G");
                    return true;
                }
                if (last.isFail()) {
                    repeatStudyPeriod();
                    return true;
                }
                changeLvl("G");
                return true;
            }
            if (second == null) {
                if (last.isFail()) {
                    repeatStudyPeriod();
                    return true;
                }
                progress();
                return true;
            }
            if (second.getDegreeLvl().getDegreeLvl().equals(last.getDegreeLvl().getDegreeLvl())) {
                if (last.isFail()) {
                    changeLvl("G");
                    return true;
                }
                progress();
                return true;
            }
            if (last.isFail()) {
                repeatStudyPeriod();
                return true;
            } else {
                progress();
                return true;
            }

        }
        return false;
    }

    private void progress() {

        if (degreeLvl.equals("P")) {
            StudyPeriod last = getCurrentStudyPeriod();
            long milisecInYear = 31557600000L;
            Date start = new Date(last.getStartDate().getTime() + 2 * milisecInYear);
            Date end = new Date(last.getEndDate().getTime() + 2 * milisecInYear);
            String label = "" + (char) ((int) last.getLabel().charAt(0) + 1);
            StudyLevel lvl = last.getDegreeLvl().nextLvl();
            StudyPeriod.createNew(regNum, label, start, end, lvl);
            changeLvl(lvl.getDegreeLvl());
            return;
        }

        if (isInPenUltimateYear() && course.isYearInIndustry()) {
            changeLvl("P");
            return;
        }
        StudyPeriod last = getCurrentStudyPeriod();
        long milisecInYear = 31557600000L;
        Date start = new Date(last.getStartDate().getTime() + milisecInYear);
        Date end = new Date(last.getEndDate().getTime() + milisecInYear);
        String label = "" + (char) ((int) last.getLabel().charAt(0) + 1);
        StudyLevel lvl = last.getDegreeLvl().nextLvl();
        StudyPeriod.createNew(regNum, label, start, end, lvl);
        changeLvl(lvl.getDegreeLvl());
    }

    private void repeatStudyPeriod() {
        StudyPeriod last = getCurrentStudyPeriod();
        long milisecInYear = 31557600000L;
        Date start = new Date(last.getStartDate().getTime() + milisecInYear);
        Date end = new Date(last.getEndDate().getTime() + milisecInYear);
        String label = "" + (char) ((int) last.getLabel().charAt(0) + 1);
        StudyPeriod news = StudyPeriod.createNew(regNum, label, start, end, last.getDegreeLvl());

        for (Grade g : last.getGradesList()) {
            news.registerModule(g.getModule());
            if (last.isPass(g)) {
                news.awardMark(g.getModule(), g.getMark(), false);
                news.awardMark(g.getModule(), g.getResitMark(), true);
            }
        }
    }

    private boolean isInFinalYear() {
        StudyPeriod last = getCurrentStudyPeriod();
        return Integer.parseInt(last.getDegreeLvl().getDegreeLvl()) == course.getDegreeLvlList().size();
    }

    private boolean isInPenUltimateYear() {
        StudyPeriod last = getCurrentStudyPeriod();
        return Integer.parseInt(last.getDegreeLvl().getDegreeLvl()) == course.getDegreeLvlList().size() - 1;
    }

    private void changeLvl(String lvl) {
        try (Database db = StudentSystem.connect()) {
            db.changeStudentProgress(lvl, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double getOverallAverage() {
        return getCurrentStudyPeriod().getAverageMark();
    }

    public String getStudentResults() {
        if (degreeLvl.equals("G")) {
            return getCareerResults();
        }
        return getCurrentResults();
    }

    private String getCareerResults() {

        if (getStudyPeriodList().size() > 1 && getCurrentStudyPeriod().isFail() && getPreviousStudyPeriod().isFail()
                && !getCurrentStudyPeriod().getDegreeLvl().getDegreeLvl().equals("4")) {
            return "Failed lvl " + getCurrentStudyPeriod().getDegreeLvl().getDegreeLvl() + " twice";
        }

        int courseType = getCourse().getDegreeLvlList().size();
        if (courseType == 1) {
            return judgeOneYearMs();
        }
        if (courseType == 3) {
            return judgeBachelors(getCourse());
        }
        if (courseType == 4) {
            return judgeMasters();
        }

        return "Something is wrong";

    }

    private String judgeMasters() {
        StudyPeriod last = getCurrentStudyPeriod();
        StudyPeriod second = getPreviousStudyPeriod();
        if (last.isFail() && second.isFail()) {
            return judgeBachelors(getCourse().getBachEquiv());
        }
        Double avg = (getStudyPeriodByLvl(2).getAverageMark() + 2 * getStudyPeriodByLvl(3).getAverageMark()
                + 2 * getStudyPeriodByLvl(4).getAverageMark()) / 5;

        if (avg < 49.5) {
            return "Failed the BSc/BEng degree (" + getCourse().getFullName() + " average grade " + avg;
        }
        if (avg < 59.5) {
            return "Achieved a 'lower second' mark in " + getCourse().getFullName();
        }
        if (avg < 69.5) {
            return "Achieved a 'upper second' mark in " + getCourse().getFullName();
        }
        return "Achieved a 'first class' mark in " + getCourse().getFullName();

    }

    private String judgeBachelors(Course c) {
        StudyPeriod second = getPreviousStudyPeriod();
        Double avg = (getStudyPeriodByLvl(2).getAverageMark() + 2 * getStudyPeriodByLvl(3).getAverageMark()) / 3;
        if (second.isFail()) {
            if (avg >= 39.5) {
                return "Achieved 'pass (nonhonours)' mark in " + c.getFullName();
            }
            return "Failed the BSc/BEng degree (" + c.getFullName() + " average grade " + avg;
        }
        if (avg < 39.5) {
            return "Failed the BSc/BEng degree (" + c.getFullName() + " average grade " + avg;
        }
        if (avg < 49.5) {
            return "Achieved a 'third class' mark in " + c.getFullName();
        }
        if (avg < 59.5) {
            return "Achieved a 'lower second' mark in " + c.getFullName();
        }
        if (avg < 69.5) {
            return "Achieved a 'upper second' mark in " + c.getFullName();
        }
        return "Achieved a 'first class' mark in " + c.getFullName();
    }

    private String judgeOneYearMs() {
        StudyPeriod sp = getCurrentStudyPeriod();
        Double average = sp.getAverageMark();
        if (average < 49.5) {
            return "Failed the 1 year MSc degree (" + getCourse().getFullName() + " average grade " + average;
        }
        if (average < 59.5) {
            return "Achieved a 'pass' mark in " + getCourse().getFullName();
        }
        if (average < 69.5) {
            return "Achieved a 'merit' mark in " + getCourse().getFullName();
        }

        return "Achieved a 'distinction' mark in " + getCourse().getFullName();

    }

    private String getCurrentResults() {
        if (degreeLvl.equals("P")) {
            return "Currently on placement";
        }
        String res = "Currently Studying \n Last period of study status: \n";
        res += getCurrentStudyPeriod().studyPeriodResults();
        return res;
    }

    public Boolean isRegistrationComplete() {
        return getCurrentStudyPeriod().isRegistrationComplete();
    }

    public Boolean areMarksAssigned() {
        return getCurrentStudyPeriod().areMarksAssigned();
    }

    public StudyPeriod getCurrentStudyPeriod() {
        StudyPeriod latest = studyPeriodList.get(0);
        for (StudyPeriod sP : studyPeriodList) {
            if (latest.getLabel().compareTo(sP.getLabel()) < 0) {
                latest = sP;
            }
        }
        return latest;
    }

    public StudyPeriod getPreviousStudyPeriod() {
        StudyPeriod second = null;
        StudyPeriod latest = studyPeriodList.get(0);
        for (StudyPeriod sP : studyPeriodList) {
            if (latest.getLabel().compareTo(sP.getLabel()) < 0) {
                second = latest;
                latest = sP;
            }
        }
        return second;
    }

    public StudyPeriod getStudyPeriodByLvl(int lvl) {
        StudyPeriod first = null;
        StudyPeriod second = null;
        for (StudyPeriod s : getStudyPeriodList()) {
            if (Integer.parseInt(s.getDegreeLvl().getDegreeLvl()) == lvl) {
                if (first == null) {
                    first = s;
                } else {
                    second = s;
                }
            }
        }

        if (second != null && first.getLabel().compareTo(second.getLabel()) < 0)
            return second;
        return first;
    }

    public ArrayList<Module> getLatestModules() {
        ArrayList<Module> ans = new ArrayList<>();

        StudyPeriod latest = getCurrentStudyPeriod();
        for (Grade g : latest.getGradesList()) {
            ans.add(g.getModule());
        }
        return ans;
    }

    public int getRegNum() {
        return this.regNum;
    }

    public void setRegNum(int regNum) {
        instances.remove(this.regNum);
        instances.put(regNum,this);
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

    public String getDegreeLvl() {
        return this.degreeLvl;
    }

    public void setDegreeLvl(String lvl) {
        this.degreeLvl = lvl;
    }

}
