package teamProject.Classes;
/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** 
 * Grade class definition
*/
public class Grade {
    private Module module;
    private Double mark, resitMark;
    
    public Grade(Module module, Double mark, Double resitMark){
        this.module = module;
        this.mark = mark;
        this.resitMark = resitMark;
    }
            
}
