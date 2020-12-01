package teamProject.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import teamProject.Classes.*;

public class IndividualStudent extends JPanel {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    int numGrades = 0;
    Student student;
    String courseCode = null;

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
        courseCode = student.getCourse().getCourseCode();

        //creating a header menu bar
        JMenu viewMenu = new JMenu("View");
        viewMenu.add(new JMenuItem("Departments"));
        viewMenu.add(new JMenuItem("Courses"));
        viewMenu.add(new JMenuItem("Modules"));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(viewMenu);
        parent.setJMenuBar(menuBar);

        setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6)); 
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

        JButton courseButton = new JButton("<html>View Course");
        courseButton.setMaximumSize(new Dimension(130, 40));
        courseButton.setActionCommand("View Course");
        courseButton.addActionListener(parent);
        add(courseButton);

        JLabel gradesLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Grades: </h3>");
        add(gradesLabel);

        String [] colNames = {"Label", "Module Code", "Mark", "Resit mark"};
        int numGrades = 0;
        for (StudyPeriod studyPeriod: studyPeriods) {
            numGrades += studyPeriod.getGradesList().size();
        }
        Object[][] allGrades = new Object[numGrades][4];

        //will use as a reference to use for the position in the table for each grade
        int count = numGrades;
        //populating the table of grades
        for (StudyPeriod studyPeriod: studyPeriods) {
            ArrayList<Grade> grades = studyPeriod.getGradesList();
            String label = studyPeriod.getLabel();
            for (Grade grade: grades) {
                allGrades[numGrades-count][0] = label;
                allGrades[numGrades-count][1] = grade.getModule().getFullName();
                allGrades[numGrades-count][2] = grade.getMark();
                allGrades[numGrades-count][3] = grade.getResitMark();
                count --;
            }
        }

        final JTable gradesTable = new JTable(allGrades, colNames);
        gradesTable.setPreferredScrollableViewportSize(new Dimension(700, 200));
        gradesTable.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(gradesTable);
        add(scrollpane);

    }
}
