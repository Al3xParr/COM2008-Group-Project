package teamProject.Classes;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.HashMap;
import java.util.Collection;

import teamProject.db.Database;
import teamProject.*;

/** 
 * Module class definition
*/
public class Module {
    private String moduleCode, fullName, timeTaught, departmentCode;

    public static HashMap<String, Module> instances = new HashMap<>();

    public Module(String moduleCode, String departmentCode, String fullName, String timeTaught) {
        this.moduleCode = moduleCode;
        this.departmentCode = departmentCode;
        this.fullName = fullName;
        this.timeTaught = timeTaught;
        instances.put(moduleCode, this);

    }

    public static Module createNew(int moduleNum, String deptCode, String fullName, String time){
        String moduleCode = Integer.toString(moduleNum);
        while (moduleCode.length() < 4) {
            moduleCode = '0' + moduleCode;
        }
        moduleCode = deptCode + moduleCode;
        Module news = new Module(moduleCode, deptCode, fullName, time);
        try (Database db = StudentSystem.connect()) {
            db.addModule(news);
            Department.getInstance(deptCode).getModuleList().add(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public void reAddToInstances(){
        instances.put(getModuleCode(), this);
    }

    public static Module getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public static Collection<Module> allInstances() {
        return instances.values();
    }

    public static Module getFantomModule() {
        Module m = new Module("", "", "", "");
        instances.remove("");
        return m;
    }

    public Boolean delete() {
        try (Database db = StudentSystem.connect()) {
            return db.deleteModule(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getModuleCode() {
        return this.moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTimeTaught() {
        return this.timeTaught;
    }

    public void setTimeTaught(String timeTaught) {
        this.timeTaught = timeTaught;
    }

    public String getDepartmentCode() {
        return this.departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

}