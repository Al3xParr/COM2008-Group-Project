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

/** 
 * Module class definition
*/
public class Module {
    private String moduleCode, fullName, timeTaught, departmentCode;

    static HashMap<String, Module> instances = new HashMap<>();
    
    public Module(String moduleCode, String departmentCode, String fullName, String timeTaught) {
        this.moduleCode = moduleCode;
        this.departmentCode = departmentCode;
        this.fullName = fullName;
        this.timeTaught = timeTaught;
        instances.put(moduleCode, this);

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

    public String getDepartmentCode() { return this.departmentCode; }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
    

}