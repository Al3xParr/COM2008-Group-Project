package teamProject.GUI;

import teamProject.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

public class MenuPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;

    public MenuPanel(MainFrame parent) {

        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(Box.createVerticalGlue());
        String welcomeText = "<html><div style = 'text-align: center;'><h2>Welcome"
                + SystemSecurity.getCurrentUser().getUsername() + "</h2></div>";
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

}
