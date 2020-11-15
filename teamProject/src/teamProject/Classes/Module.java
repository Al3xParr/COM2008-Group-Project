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
    private Department departmentCode;
    private int moduleCredits;

    public Module(String moduleCode, Department departmentCode, String fullName, int moduleCredits, String timeTaught) {
        this.moduleCode = moduleCode;
        this.departmentCode = departmentCode;
        this.fullName = fullName;
        this.moduleCredits = moduleCredits;
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

    public Department getDepartmentCode() {
        return this.departmentCode;
    }

    public void setDepartmentCode(Department departmentCode) {
        this.departmentCode = departmentCode;
    }

    public int getModuleCredits() {
        return this.moduleCredits;
    }

    public void setModuleCredits(int moduleCredits) {
        this.moduleCredits = moduleCredits;
    }
    
    
        
}