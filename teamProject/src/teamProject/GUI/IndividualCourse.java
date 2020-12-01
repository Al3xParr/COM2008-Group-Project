package teamProject.GUI;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import teamProject.Classes.*;

public class IndividualCourse extends JPanel {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    String[] otherDep;
    Course course;
    String otherDepString;

    public IndividualCourse(MainFrame parent, Course course) {

        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String courseCode = course.getCourseCode();
        String fullName = course.getFullName();
        Boolean yearII = course.getYearInIndustry();
        String mainDep = course.getMainDep().getFullName();
        ArrayList<Department> otherDep = course.getDepartmentList();

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
                "<html><div style = 'text-align : center;'><<h2>Course: " + courseCode + "</h2><br>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header);
        
        JLabel fullNameLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Full name: " + fullName + "</h3><br>");
        add(fullNameLabel);
        JLabel yIILabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Year in Industry: " + yearII + "</h3><br>");
        add(yIILabel);
        JLabel mainDepLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Main Department: " + mainDep + "</h3><br>");
        add(mainDepLabel);

        JButton depButton = new JButton("<html>View Main Department");
        depButton.setMaximumSize(new Dimension(200, 40));
        depButton.setActionCommand("View Department");
        depButton.addActionListener(parent);
        add(depButton);

        /*
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
        */
    }
}
