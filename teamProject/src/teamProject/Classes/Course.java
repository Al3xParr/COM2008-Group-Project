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
 * Course class definition
*/
public class Course {

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
