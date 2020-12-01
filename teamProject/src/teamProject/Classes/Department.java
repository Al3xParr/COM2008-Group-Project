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

/**
 * Department class definition
*/
public class Department {
    private String deptCode, fullName;
    private ArrayList<Module> moduleList;
    private ArrayList<Course> courseList;

    public static HashMap<String, Department> instances = new HashMap<>();

    public Department(String deptCode, String fullName, ArrayList<Module> moduleList, ArrayList<Course> courseList) {
        this.deptCode = deptCode;
        this.fullName = fullName;
        this.moduleList = moduleList;
        this.courseList = courseList;
        instances.put(deptCode, this);
    }

    public static Department getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public String getDeptCode() {
        return this.deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ArrayList<Module> getModuleList() {
        return this.moduleList;
    }

    public void setModuleList(ArrayList<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public ArrayList<Course> getCourseList() {
        return this.courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public static HashMap<String, Department> getAllInstances(){
        return instances;
    }

}
