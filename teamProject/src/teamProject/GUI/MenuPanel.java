package teamProject.GUI;

import teamProject.*;
import teamProject.Classes.*;
import teamProject.Classes.Module;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
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

        String welcomeText = "<html><div style = 'text-align: center;'><h2>Welcome "
                + SystemSecurity.getCurrentUser().getUsername() + "</h2></div>";
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel);

        addMenuButtons();
        
        JButton LogOut = new JButton("Log Out");
        LogOut.setActionCommand("LogOut");
        LogOut.setAlignmentX(Component.CENTER_ALIGNMENT);
        LogOut.setMaximumSize(new Dimension(100, 70));
        LogOut.addActionListener(this);
        LogOut.setFocusable(false);
        add(LogOut);

        add(Box.createVerticalGlue());

    }

    private void addMenuButtons() {
        int priv = SystemSecurity.getPrivilages();
        if (priv == 0) {
            JButton browsePeriods = new JButton("Browse StudyPeriods");
            browsePeriods.setActionCommand("StudyPeriods");
            browsePeriods.setAlignmentX(Component.CENTER_ALIGNMENT);
            browsePeriods.setMaximumSize(new Dimension(200, 70));
            browsePeriods.addActionListener(this);
            browsePeriods.setFocusable(false);
            add(browsePeriods);
            add(Box.createRigidArea(new Dimension(0, 8)));
            JButton browseModules = new JButton("Browse CurrentModules");
            browseModules.setActionCommand("CurrentModules");
            browseModules.setAlignmentX(Component.CENTER_ALIGNMENT);
            browseModules.setMaximumSize(new Dimension(200, 70));
            browseModules.addActionListener(this);
            browseModules.setFocusable(false);
            add(browseModules);
            add(Box.createRigidArea(new Dimension(0, 8)));
        } else if (priv == 1) {

            JButton browseStudents = new JButton("Browse Students");
            browseStudents.setActionCommand("TeacherStudents");
            browseStudents.setAlignmentX(Component.CENTER_ALIGNMENT);
            browseStudents.setMaximumSize(new Dimension(200, 70));
            browseStudents.addActionListener(this);
            browseStudents.setFocusable(false);
            add(browseStudents);
            add(Box.createRigidArea(new Dimension(0, 8)));

        } else if (priv == 2) {

            JButton browseStudents = new JButton("Browse Students");
            browseStudents.setActionCommand("RegistrarStudents");
            browseStudents.setAlignmentX(Component.CENTER_ALIGNMENT);
            browseStudents.setMaximumSize(new Dimension(200, 70));
            browseStudents.addActionListener(this);
            browseStudents.setFocusable(false);
            add(browseStudents);
            add(Box.createRigidArea(new Dimension(0, 8)));

        } else if (priv == 3) {

            JButton browseUsers = new JButton("Browse Users");
            browseUsers.setActionCommand("Users");
            browseUsers.setAlignmentX(Component.CENTER_ALIGNMENT);
            browseUsers.setMaximumSize(new Dimension(200, 70));
            browseUsers.addActionListener(this);
            browseUsers.setFocusable(false);
            add(browseUsers);
            add(Box.createRigidArea(new Dimension(0, 8)));

            JButton browseDepartments = new JButton("Browse Departments");
            browseDepartments.setActionCommand("Departments");
            browseDepartments.setAlignmentX(Component.CENTER_ALIGNMENT);
            browseDepartments.setMaximumSize(new Dimension(200, 70));
            browseDepartments.addActionListener(this);
            browseDepartments.setFocusable(false);
            add(browseDepartments);
            add(Box.createRigidArea(new Dimension(0, 8)));

            JButton browseCourses = new JButton("Browse Courses");
            browseCourses.setActionCommand("Courses");
            browseCourses.setAlignmentX(Component.CENTER_ALIGNMENT);
            browseCourses.setMaximumSize(new Dimension(200, 70));
            browseCourses.addActionListener(this);
            browseCourses.setFocusable(false);
            add(browseCourses);
            add(Box.createRigidArea(new Dimension(0, 8)));

            JButton browseModules = new JButton("Browse Modules");
            browseModules.setActionCommand("Modules");
            browseModules.setAlignmentX(Component.CENTER_ALIGNMENT);
            browseModules.setMaximumSize(new Dimension(200, 70));
            browseModules.addActionListener(this);
            browseModules.setFocusable(false);
            add(browseModules);
            add(Box.createRigidArea(new Dimension(0, 8)));

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println(command);
        if (command.equals("StudyPeriods")) {
            Collection<StudyPeriod> list = ((Student) (SystemSecurity.getCurrentUser())).getStudyPeriodList();
            //TODO Open new study period browser
        }
        if (command.equals("CurrentModules")) {
            Collection<Module> modules = ((Student) (SystemSecurity.getCurrentUser())).getLatestModules();
            //TODO Open new Module browser
        }
        if (command.equals("TeacherStudents")){
            Collection<Student> students = Student.instances.values();
            //TODO Open new Student Browser for Teachers
        }
        if (command.equals("RegistrarStudents")){
            Collection<Student> students = Student.instances.values();
            //TODO Open new Student Browser for Registrar
        }
        if (command.equals("Users")){
            Collection<User> users = new ArrayList<>();
            users.addAll(Student.instances.values());
            users.addAll(Teacher.instances.values());
            users.addAll(Registrar.instances.values());
            users.addAll(Administrator.instances.values());
            //TODO Open new Student Browser for Teachers
        }
        

    }

}
