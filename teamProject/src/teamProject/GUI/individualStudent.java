package teamProject.GUI;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

import teamProject.Classes.*;

public class IndividualStudent extends JPanel {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    int numGrades = 0;

    public IndividualStudent(MainFrame parent, Student student) {
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String username = student.getUsername();
        String firstNames = student.getForenames();
        String surname = student.getSurname();
        String email = student.getEmail();
        String tutor = student.getTutor();
        String course = student.getCourse().getFullName();
        ArrayList<StudyPeriod> studyPeriods = student.getStudyPeriodList();

        //creating a header menu bar
        JMenu viewMenu = new JMenu("View");
        viewMenu.add(new JMenuItem("Departments"));
        viewMenu.add(new JMenuItem("Courses"));
        viewMenu.add(new JMenuItem("Modules"));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(viewMenu);
        parent.setJMenuBar(menuBar);

        setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 3)); 
        JLabel header = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>Student: " + username + "</h2><br>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header);
        
        JLabel firstNameLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>First Name(s): " + firstNames + "</h3><br>");
        add(firstNameLabel);
        JLabel surNameLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Surname: " + surname + "</h3><br>");
        add(surNameLabel);
        JLabel emailLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Email: " + email + "</h3><br>");
        add(emailLabel);
        JLabel tutorLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Tutor: " + tutor + "</h3><br>");
        add(tutorLabel);
        JLabel courseLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Course: " + course + "</h3>");
        add(courseLabel);
        JButton confirmButton = new JButton("<html><b>View Course</b>");
        confirmButton.setMaximumSize(new Dimension(130, 40));
        add(confirmButton);

        String [] colNames = {"Label", "Module Code", "Mark", "Resit mark"};
        int numGrades = 0;
        for (StudyPeriod studyPeriod: studyPeriods) {
            numGrades += studyPeriod.getGradesList().size();
        }
        Object[][] allGrades = new Object[numGrades][4];

        //populating the table of grades
        for (StudyPeriod studyPeriod: studyPeriods) {
            
        }

        final JTable gradesTable = new JTable(allGrades, colNames);
        gradesTable.setPreferredScrollableViewportSize(new Dimension(700, 200));
        gradesTable.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(gradesTable);
        add(scrollpane);

    }
}
