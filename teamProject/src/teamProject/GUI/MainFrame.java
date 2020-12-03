package teamProject.GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import teamProject.SystemSecurity;
import teamProject.Classes.Course;

/** 
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

public class MainFrame extends JFrame implements ActionListener {

    public static final long serialVersionUID = 1L;
    LogInPanel logInPanel = null;
    ArrayList<SubFrame> subFrames;

    public MainFrame() {
        super("University of COM2008");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        subFrames = new ArrayList<SubFrame>();
        setSize(screenSize.width / 4, screenSize.height / 2);
        setLocation(screenSize.width / 4, screenSize.height / 4);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        logInPanel = new LogInPanel(this);

        setContentPane(logInPanel);
        setVisible(true);

    }

    public void showMenu() {
        setContentPane(new MenuPanel(this));
        revalidate();
        repaint();
    }

    public void changeLogIn() {
        logInPanel = new LogInPanel(this);
        setContentPane(logInPanel);
        revalidate();
        repaint();
    }

    public void newFrame(SubFrame s) {
        subFrames.add(s);
    }

    public void disposeOfSubFrames() {
        for (SubFrame s : subFrames) {
            s.dispose();
        }
        subFrames.clear();
    }

    public void actionPerformed(ActionEvent event) {

        if (event.getActionCommand().equals("Log in")) {
            String username = logInPanel.usernameField.getText();
            String pass = new String(logInPanel.passwordField.getPassword());
            if (SystemSecurity.login(username, pass)) {
                logInPanel.passwordField.setText(null);
                showMenu();
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect username or password", "Invalid login",
                        JOptionPane.WARNING_MESSAGE);

            }
        }
    }

}
