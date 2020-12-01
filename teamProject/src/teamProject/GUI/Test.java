package teamProject.GUI;

import teamProject.StudentSystem;
import teamProject.Classes.Module;
import teamProject.db.Database;

import java.awt.*;
import javax.swing.*;

public class Test extends JFrame {

  // Needed for serialisation
  private static final long serialVersionUID = 1L;

  // Constructor with frame title
  public Test(String title) throws HeadlessException {
        super(title);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
    
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        try(Database db = StudentSystem.connect()){
          db.instantiateUsers();
        }catch (Exception ex) {
          ex.printStackTrace();
        }
          
         AllModulesPanel aMP = new AllModulesPanel(Module.allInstances());
         setContentPane(aMP);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    
        setVisible(true);
    
    } 
    public static void main(String[] args) {
        
        Test test = new Test("All Modules Panel");
        
      }
    }


