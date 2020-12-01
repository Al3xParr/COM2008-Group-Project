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
    private int regNum, creditsPerModule, creditsNeeded;
    private Double passMark;

    public static HashMap<String, StudyPeriod> instances = new HashMap<>();

    public StudyPeriod(int regNum, String label, Date startDate, Date endDate, StudyLevel degreeLvl,
            ArrayList<Grade> gradesList) {
        this.label = label;
        this.startDate = startDate;
        this.endDate = endDate;
        this.degreeLvl = degreeLvl;
        this.gradesList = gradesList;
        this.regNum = regNum;
        switch (degreeLvl.getDegreeLvl()) {
            case "1":
            case "2":
                creditsNeeded = 120;
                creditsPerModule = 20;
                passMark=40.0;
                break;
            case "3":
                creditsNeeded = 80;
                creditsPerModule = 20;
                passMark = 40.0;
                break;
            case "4":
                creditsNeeded = 60;
                creditsPerModule = 15;
                passMark=50.0;
                break;
        }
        instances.put(regNum + label, this);

    }

    public static StudyPeriod createNew(int regNum, String label, Date startDate, Date endDate, StudyLevel lvl) {
        StudyPeriod news = new StudyPeriod(regNum, label, startDate, endDate, lvl, new ArrayList<Grade>());
        try (Database db = StudentSystem.connect()) {
            db.addStudyPeriod(news);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Module m : lvl.getCoreModules()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean isRegistrationComplete() {

        int has = gradesList.size() * creditsPerModule;
        return has == creditsNeeded;
    }

    private Double judgeGrade(Grade g) {
        if (g.getMark() == null || (g.getMark() < passMark && g.getResitMark() == null)) {
            return -1.0;
        }
        if (g.getMark() >= passMark) {
            return g.getMark();
        }
        if (g.getResitMark() >= passMark) {
            return passMark;
        }
        if (g.getMark() > g.getResitMark()) {
            return g.getMark();
        }

        return g.getResitMark();
    }

    /**
     * Returns results for this study period
     * @return possibleValues:
     *         - "Registration not complete"
     *         - "Marking not complete"
     *         - "Pass - Average mark {average}"
     *         - "Conceded pass - Average mark {average}"
     *         - "Fail - Average mark {average}"
     *          where {average} is the average obrained
     */
    public String studyPeriodResults() {
        if (isRegistrationComplete()) {
            if (areMarksAssigned()) {
                Double avg = getAverageMark();
                if (avg >= passMark) {
                    int failed = 0;
                    for (Grade g : gradesList) {
                        Double mark = judgeGrade(g);
                        if (mark < passMark - 10.0) {
                            failed += 2;
                        } else if (mark < passMark) {
                            failed++;
                        }
                    }
                    if(failed == 0){
                        return "Pass - Average mark " + avg.toString();
                    } else if (failed == 1) {
                        return "Conceded pass - Average mark " + avg.toString();
                    }
                }
                return "Fail - Average mark " + avg.toString();
            }
            return "Marking not complete";
        }
        return "Registration not complete";
    }

    public Boolean areMarksAssigned() {
        for (Grade g : gradesList) {
            if(judgeGrade(g)<0)
                return false;
        }
        return true;
    }

    public Double getAverageMark() {
        
        if(gradesList.isEmpty() && areMarksAssigned()){
            Double sum = 0.0;
            for (Grade g : gradesList) {

                sum += judgeGrade(g);
            }
            return sum / gradesList.size();
        }
        return 0.0;
    }

    public void unregisterModule(Module m) {
        try (Database db = StudentSystem.connect()) {
            db.deleteGrade(this, m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Grade registerModule(Module m) {
        Grade g = new Grade(m, null, null);
        gradesList.add(g);
        try (Database db = StudentSystem.connect()) {
            db.registerModule(this, g);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return g;
    }

    public Boolean isFail() {
        return studyPeriodResults().indexOf("Fail") != -1;
    }

    public void awardMark(Module m, Double grade, Boolean isResit) {
        try (Database db = StudentSystem.connect()) {
            db.changeGrade(this, m, isResit, grade);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
