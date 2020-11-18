package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * Module class definition
*/
public class Module {
    private String moduleCode, fullName, timeTaught;
    private Department department;

    public Module(String moduleCode, Department department, String fullName, String timeTaught) {
        this.moduleCode = moduleCode;
        this.department = department;
        this.fullName = fullName;

        this.timeTaught = timeTaught;
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

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department departmentCode) {
        this.department = departmentCode;
    }
        
}