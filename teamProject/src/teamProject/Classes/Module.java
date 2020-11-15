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
    
    public Module(String moduleCode, Department departmentCode, String fullName,
    int moduleCredits, String timeTaught){
        this.moduleCode = moduleCode;
        this.departmentCode = departmentCode;
        this.fullName = fullName;
        this.moduleCredits = moduleCredits;
        this.timeTaught = timeTaught;
    }
        
}