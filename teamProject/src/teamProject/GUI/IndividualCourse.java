package teamProject.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;

import teamProject.Classes.*;

public class IndividualCourse extends RefreshablePanel {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    String[] otherDep;
    Course course;
    String otherDepString;

    JTable table;
    DefaultTableModel model;
    String[] columnNames = { "Degree Level", "View Modules" };
    ArrayList<StudyLevel> degreeLvls;

    Object[][] allDegreeLvl;

    public IndividualCourse(MainFrame parent, Course course) {

        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String courseCode = course.getCourseCode();
        String fullName = course.getFullName();
        Boolean yearII = course.getYearInIndustry();
        String mainDep = course.getMainDep().getFullName();
        degreeLvls = course.getDegreeLvlList();

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

        JLabel degreeLvlLabel = new JLabel("<html><div style = 'text-align : center;'><<h3>Study Levels: </h3>");
        add(degreeLvlLabel);

        allDegreeLvl = getData();
        model = new DefaultTableModel(allDegreeLvl, columnNames);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(200, 100));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 1 && row != -1) {
                    new SubFrame("Study Level: " + allDegreeLvl[row][0], parent,
                            new StudyLevelPanel(parent, StudyLevel.getInstance(allDegreeLvl[row][0] + courseCode)));
                    System.out.println(allDegreeLvl[row][0]);
                }
            }
        });
    }

    private Object[][] getData() {
        Object[][] allDegreeLvl = new Object[degreeLvls.size()][columnNames.length];

        //will use as a reference to use for the position in the table for each grade
        int count = 0;
        //populating the table of grades
        for (StudyLevel degreeLvl : degreeLvls) {
            allDegreeLvl[count][0] = degreeLvl.getDegreeLvl();
            allDegreeLvl[count][1] = "<html><B>View Degree Level</B></html>";
            count++;
        }

        return allDegreeLvl;
    }

    public void refresh() {
        course = Course.getInstance(course.getCourseCode());
        allDegreeLvl = getData();
        model.setDataVector(allDegreeLvl, columnNames);
        revalidate();
        repaint();
    }
}
