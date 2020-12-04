package teamProject.GUI;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

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
    setSize((int)(screenSize.width / 1.75), (int)(screenSize.height / 1.75));
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

}
