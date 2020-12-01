package teamProject.GUI;

import teamProject.StudentSystem;
import teamProject.Classes.*;
import teamProject.db.Database;

import java.awt.*;
import javax.swing.*;

public class SubFrame extends JFrame {

  // Needed for serialisation
  private static final long serialVersionUID = 1L;
  // Constructor with frame title
  public SubFrame(String title, MainFrame main, JPanel panel) throws HeadlessException {
        super(title);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        main.newFrame(this);
        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        setContentPane(panel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
        setVisible(true);
    
  } 
  public static void main(String[] args) {
        
    try (Database db = StudentSystem.connect()) {
      db.resetDB();
      db.populateDB();
      db.instantiateUsers();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    MainFrame main = new MainFrame();
    new SubFrame("Test", main, new ViewStudents(main, Student.instances.values()));
        
  }
}


