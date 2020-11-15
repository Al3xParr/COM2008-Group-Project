package teamProject.Classes;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.Date;

/**
 * StudyPeriod class definition
 */
public class StudyPeriod {
    private char label;
    private Date startDate, endDate; 
    private StudyLevel degreeLvl;
    private Grade[] gradesList;
    

    StudyPeriod(char label, Date startDate, Date endDate, StudyLevel degreeLvl,
         Grade[] gradesList){
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.degreeLvl = degreeLvl;
        this.gradesList = gradesList;

    }
}
