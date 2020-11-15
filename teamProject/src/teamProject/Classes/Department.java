package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * Department class definition
*/
public class Department {
    private String deptCode, fullName;
    private Module[] moduleList;
    private Course[] courseList;

    public Department(String deptCode, String fullName, Module[] moduleList,
    Course[] courseList){
        this.deptCode = deptCode;
        this.fullName = fullName;
        this.moduleList = moduleList;
        this.courseList = courseList;
    }
}
