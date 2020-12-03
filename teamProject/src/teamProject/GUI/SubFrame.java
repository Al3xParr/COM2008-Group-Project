package teamProject.GUI;

import teamProject.StudentSystem;
import teamProject.SystemSecurity;
import teamProject.Classes.Module;
import teamProject.db.Database;

import java.awt.*;
import javax.swing.*;

public class SubFrame extends JFrame {

  // Needed for serialisation
  private static final long serialVersionUID = 1L;
  public RefreshablePanel panel;
  MainFrame main;
  // Constructor with frame title
  public SubFrame(String title, MainFrame main, RefreshablePanel panel) throws HeadlessException {
    super(title);
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    this.main = main;
    main.newFrame(this);
    this.panel = panel;
    addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosed(java.awt.event.WindowEvent windowEvent) {
        main.removeFrame((SubFrame) windowEvent.getSource());
      }
    });
    setSize(screenSize.width / 2, screenSize.height / 2);
    setLocation(screenSize.width / 4, screenSize.height / 4);
    
    setContentPane(panel);

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    setVisible(true);

  }

  public void setContentPane(RefreshablePanel cont) {
    this.panel = cont;
    JScrollPane scroll = new JScrollPane(panel);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    super.setContentPane(scroll);
  }

  public static void main(String[] args) {

    try (Database db = StudentSystem.connect()) {
      db.resetDB();
      db.populateDB();
      db.instantiateUsers();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    SystemSecurity.login("user5", "passwordHash");
    MainFrame main = new MainFrame();
    new SubFrame("Test", main, new AllModulesPanel(main, Module.allInstances()));

  }
}
