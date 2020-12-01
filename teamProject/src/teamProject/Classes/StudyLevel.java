package teamProject.Classes;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import teamProject.db.Database;
import teamProject.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * StudyLevel class definition
*/
public class StudyLevel {
    private String degreeLvl, courseCode;
    private ArrayList<Module> coreModules, optionalModules;

    public static HashMap<String, StudyLevel> instances = new HashMap<>();

    public StudyLevel(String degreeLvl, String courseCode, ArrayList<Module> coreModules,
            ArrayList<Module> optionalModules) {
        this.degreeLvl = degreeLvl;
        this.courseCode = courseCode;
        this.coreModules = coreModules;
        this.optionalModules = optionalModules;
        instances.put(degreeLvl + courseCode, this);

    }

    public static StudyLevel createNew(String lvl, String courseCode, ArrayList<Module> core, ArrayList<Module> optional){
        StudyLevel news = new StudyLevel(lvl, courseCode, core, optional);
        try (Database db = StudentSystem.connect()) {
            db.addStudyLevel(news);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;
    }

    public void reAddToInstances() {
        instances.put(getDegreeLvl()+ getCourseCode(), this);
        
        for (Module m : getOptionalModules()) {
            m.reAddToInstances();
        }

        for (Module m : getCoreModules()) {
            m.reAddToInstances();
        }

    }

    /**
     * 
     * @param key is degreeLvl + courseCode
     * @return
     */
    public static StudyLevel getInstance(String key) {
        return instances.get(key);
    }

    public static void clearInstances() {
        instances.clear();
    }

    public Boolean delete() {
        try (Database db = StudentSystem.connect()) {
            return db.deleteStudyLevel(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getDegreeLvl() {
        return this.degreeLvl;
    }

    public void setDegreeLvl(String degreeLvl) {
        this.degreeLvl = degreeLvl;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public ArrayList<Module> getCoreModules() {
        return this.coreModules;
    }

    public void setCoreModules(ArrayList<Module> coreModules) {
        this.coreModules = coreModules;
    }

    public ArrayList<Module> getOptionalModules() {
        return this.optionalModules;
    }

    public void setOptionalModules(ArrayList<Module> optionalModules) {
        this.optionalModules = optionalModules;
    }

}
