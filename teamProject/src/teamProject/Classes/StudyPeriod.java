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
import java.util.HashMap;

import teamProject.db.Database;
import teamProject.*;

/**
 * StudyPeriod class definition
 */
public class StudyPeriod {
    private String label;
    private Date startDate, endDate;
    private StudyLevel degreeLvl;
    private ArrayList<Grade> gradesList;
    private int regNum;

    public static HashMap<String, StudyPeriod> instances = new HashMap<>();

    public StudyPeriod(int regNum, String label, Date startDate, Date endDate, StudyLevel degreeLvl,
            ArrayList<Grade> gradesList) {
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.degreeLvl = degreeLvl;
        this.gradesList = gradesList;
        this.regNum = regNum;
        instances.put(regNum + label, this);

    }

    public static StudyPeriod createNew(int regNum, String label, Date startDate, Date endDate, StudyLevel lvl) {
        StudyPeriod news = new StudyPeriod(regNum, label, startDate, endDate, lvl, new ArrayList<Grade>());
        try (Database db = StudentSystem.connect()) {
            db.addStudyPeriod(news);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Module m : lvl.getCoreModules()){
            news.registerModule(m);
        }

        return news;
    }

    /**
     * 
     * @param key is regNum + Label
     * @return
     */
    public static StudyPeriod getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public void delete() {
      try (Database db = StudentSystem.connect()) {
        db.deleteStudyPeriod(this);
      }catch(Exception e){
          e.printStackTrace();
      }
    }

    public void unregisterModule(Module m) {
        try (Database db = StudentSystem.connect()) {
            db.deleteGrade(this, m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Grade registerModule(Module m){
        Grade g = new Grade(m, null, null);
        gradesList.add(g);
        try (Database db = StudentSystem.connect()) {
            db.registerModule(this, g);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return g;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
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

    public int getRegNum() {
        return this.regNum;
    }

    public void setregNum(int regNum) {
        this.regNum = regNum;
    }

}
