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

import teamProject.db.Database;
import teamProject.*;

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

    public static Department createNew(String deptCode, String fullName){
        Department news = new Department(deptCode, fullName, new ArrayList<Module>(), new ArrayList<Course>());
                
        try (Database db = StudentSystem.connect()) {
            db.addDepartment(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public static Department getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public Boolean delete() {
        try (Database db = StudentSystem.connect()) {
            for (Course c : getCourseList()) {
                if (c.getMainDep() == this) {
                    c.delete();
                }
            }
            return db.deleteDepartment(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    public static Collection<Department> allInstances() {
        return instances.values();
    }

}
