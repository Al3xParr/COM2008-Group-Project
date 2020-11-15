package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * StudyLevel class definition
*/
public class StudyLevel {
    private char degreeLvl;  
    private Module[] coreModules, optionalModules;

    StudyLevel(char degreeLvl, Module[] coreModules, Module[] optionalModules) {
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

    public Module[] getCoreModules() {
        return this.coreModules;
    }

    public void setCoreModules(Module[] coreModules) {
        this.coreModules = coreModules;
    }

    public Module[] getOptionalModules() {
        return this.optionalModules;
    }

    public void setOptionalModules(Module[] optionalModules) {
        this.optionalModules = optionalModules;
    }
    
}
