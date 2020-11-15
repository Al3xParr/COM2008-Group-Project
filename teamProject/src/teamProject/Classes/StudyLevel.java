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

    StudyLevel(char degreeLvl, Module[] coreModules, Module[] optionalModules){
        this.degreeLvl = degreeLvl;
        this.coreModules = coreModules;
        this.optionalModules = optionalModules;
    }
}
