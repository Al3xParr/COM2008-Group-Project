package teamProject.Classes;
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

    private String courseCode, fullName;
    private Course bachEquiv;
    private Boolean yearInIndustry;
    private Department mainDep;
    private Department[] departmentList;
    private StudyLevel[] degreeLvlList;

    public Course(String courseCode, String fullName, Boolean yearInIndustry,
     Course bachEquiv, Department mainDep, Department[] departmentList, StudyLevel[] degreeLvlList){
        this.courseCode = courseCode;
        this.fullName = fullName;
        this.yearInIndustry = yearInIndustry;
        this.bachEquiv = bachEquiv;
        this.mainDep = mainDep;
        this.departmentList = departmentList;
        this.degreeLvlList = degreeLvlList;
    }
}
