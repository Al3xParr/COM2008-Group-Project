package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.util.ArrayList;

/**
 * StudyLevel class definition
*/
public class StudyLevel {
    private char degreeLvl;  
    private ArrayList<Module> coreModules, optionalModules;

    StudyLevel(char degreeLvl, ArrayList<Module> coreModules, ArrayList<Module> optionalModules) {
        this.degreeLvl = degreeLvl;
        this.coreModules = coreModules;
        this.optionalModules = optionalModules;
    }


    public char getDegreeLvl() {
        return this.degreeLvl;
    }

    public void setDegreeLvl(char degreeLvl) {
        this.degreeLvl = degreeLvl;
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
