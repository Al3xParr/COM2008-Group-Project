package teamProject.Classes;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.ArrayList;
import java.util.Date;

/**
 * StudyPeriod class definition
 */
public class StudyPeriod {
    private char label;
    private Date startDate, endDate; 
    private StudyLevel degreeLvl;
    private ArrayList<Grade> gradesList;
    

    StudyPeriod(char label, Date startDate, Date endDate, StudyLevel degreeLvl,
                ArrayList<Grade> gradesList) {
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.degreeLvl = degreeLvl;
        this.gradesList = null;
    }
    

    public char getLabel() {
        return this.label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public StudyLevel getDegreeLvl() {
        return this.degreeLvl;
    }

    public void setDegreeLvl(StudyLevel degreeLvl) {
        this.degreeLvl = degreeLvl;
    }

    public ArrayList<Grade> getGradesList() {
        return this.gradesList;
    }

    public void setGradesList(ArrayList<Grade> gradesList) {
        this.gradesList = gradesList;
    }

}
