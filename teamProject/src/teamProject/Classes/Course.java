package teamProject.Classes;

import java.util.ArrayList;
import java.util.HashMap;

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
 * Course class definition
*/
public class Course {

    public static HashMap<String, Course> instances = new HashMap<>();

    private String courseCode, fullName;
    private Course bachEquiv;
    private Boolean yearInIndustry;
    private Department mainDep;
    private ArrayList<Department> departmentList;
    private ArrayList<StudyLevel> degreeLvlList;

    public Course(String courseCode, String fullName, Boolean yearInIndustry, Course bachEquiv, Department mainDep,
            ArrayList<Department> departmentList, ArrayList<StudyLevel> degreeLvlList) {
        this.courseCode = courseCode;
        this.fullName = fullName;
        this.yearInIndustry = yearInIndustry;
        this.bachEquiv = bachEquiv;
        this.mainDep = mainDep;
        this.departmentList = departmentList;
        this.degreeLvlList = degreeLvlList;
        instances.put(courseCode, this);
    }

    public static Course createNew(int courseNum, String fullName, Boolean YII, Course bachEquiv, Department mainDept,
            ArrayList<Department> allDepartments) {
        String courseCode = Integer.toString(courseNum);
        while(courseCode.length()<3){
            courseCode = '0' + courseCode;
        }
        courseCode = mainDept.getDeptCode() + courseCode;
        Course news = new Course(courseCode, fullName, YII, bachEquiv, mainDept, allDepartments,
                new ArrayList<StudyLevel>());
        for (Department d : allDepartments) {
            d.getCourseList().add(news);
        }
        try (Database db = StudentSystem.connect()) {
            db.addCourse(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public static Course getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public Boolean delete() {
        try (Database db = StudentSystem.connect()) {
            
            return db.deleteCourse(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addNewStudyLvl(String lvl, ArrayList<Module> core, ArrayList<Module> optional) {
        this.degreeLvlList.add(StudyLevel.createNew(lvl, getCourseCode(), core, optional));
    }

    public StudyLevel getStudyLvl(int x) {
        StudyLevel res = null;
        for (StudyLevel s : getDegreeLvlList()) {
            if (Integer.parseInt(s.getDegreeLvl()) == x) {
                res = s;
                break;
            }
        }

        return res;
        
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Course getBachEquiv() {
        return this.bachEquiv;
    }

    public void setBachEquiv(Course bachEquiv) {
        this.bachEquiv = bachEquiv;
    }

    public Boolean isYearInIndustry() {
        return this.yearInIndustry;
    }

    public Boolean getYearInIndustry() {
        return this.yearInIndustry;
    }

    public void setYearInIndustry(Boolean yearInIndustry) {
        this.yearInIndustry = yearInIndustry;
    }

    public Department getMainDep() {
        return this.mainDep;
    }

    public void setMainDep(Department mainDep) {
        this.mainDep = mainDep;
    }

    public ArrayList<Department> getDepartmentList() {
        return this.departmentList;
    }

    public void setDepartmentList(ArrayList<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public ArrayList<StudyLevel> getDegreeLvlList() {
        return this.degreeLvlList;
    }

    public void setDegreeLvlList(ArrayList<StudyLevel> degreeLvlList) {
        this.degreeLvlList = degreeLvlList;
    }

}
