package teamProject.GUI;

import java.awt.*;
import javax.swing.*;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

public class MainFrame extends JFrame{

    public static final long serialVersionUID = 1L;
    
    public MainFrame() {
        super("University of COM2008");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setSize(screenSize.width / 4, screenSize.height / 2);
        setLocation(screenSize.width / 4, screenSize.height / 4);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new LogInPanel(this));

        setVisible(true);

    }
    
    public void showMenu() {
        System.out.println("Yey we did it");
        setContentPane(new MenuPanel(this));
    }
    
}
