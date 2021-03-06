package teamProject.GUI;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.table.*;

import teamProject.*;
import teamProject.Classes.*;

public class IndividualStudent extends RefreshablePanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    int numGrades = 0;
    Student student = null;
    String courseCode;
    Course course = null;
    ArrayList<StudyPeriod> studyPeriods;
    String[] columnNames = { "Label", "Start Date", "End Date", "Degree Level", "View Grades" };
    JTable table;
    Object[][] allStudyPeriod;

    public IndividualStudent(MainFrame parent, Student student) {
        this.parent = parent;
        this.student = student;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        int regNum = student.getRegNum();
        String username = student.getUsername();
        String firstNames = student.getForenames();
        String surname = student.getSurname();
        String email = student.getEmail();
        String tutor = student.getTutor();
        String courseName = student.getCourse().getFullName();
        studyPeriods = student.getStudyPeriodList();
        courseCode = student.getCourse().getCourseCode();
        course = Course.getInstance(courseCode);

        JLabel header = new JLabel("<html><div style = 'text-align : center;'><<h2>Student: " + username + "</h2><br>");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header);

        JLabel firstNameLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>First Name(s): " + firstNames + "</h3><br>");
        add(firstNameLabel);
        JLabel surNameLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Surname: " + surname + "</h3><br>");
        add(surNameLabel);
        JLabel emailLabel = new JLabel("<html><div style = 'text-align : center;'><<h3>Email: " + email + "</h3><br>");
        add(emailLabel);
        JLabel tutorLabel = new JLabel("<html><div style = 'text-align : center;'><<h3>Tutor: " + tutor + "</h3><br>");
        add(tutorLabel);
        JLabel courseLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Course: " + courseName + "</h3>");
        add(courseLabel);

        JButton courseButton = new JButton("View Course");
        courseButton.addActionListener(this);
        add(courseButton);

        JLabel studyLevelLabel = new JLabel("<html><div style = 'text-align : center;'><<h3>Study Periods: </h3>");
        add(studyLevelLabel);

        allStudyPeriod = getData();

        if (SystemSecurity.getPrivilages() == 1) {
            JButton gradeButton = new JButton("View Current Progress");
            gradeButton.addActionListener(this);
            add(gradeButton);

            JButton progressButton = new JButton("Progress Student");
            progressButton.addActionListener(this);
            add(progressButton);
        }

        //will use as a reference to use for the position in the table for each grade

        table = new JTable(allStudyPeriod, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 4 && row != -1) {
                    String ref = regNum + (String) allStudyPeriod[row][0];
                    StudyPeriod period = StudyPeriod.getInstance(ref);
                    new SubFrame("Study Period: " + allStudyPeriod[row][0], parent,
                            new StudyPeriodPanel(parent, period));
                }
            }
        });
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public Object[][] getData() {
        int count = 0;
        Object[][] allStudyPeriod = new Object[studyPeriods.size()][columnNames.length];
        //populating the table of study periods
        for (StudyPeriod studyPeriod : studyPeriods) {
            allStudyPeriod[count][0] = studyPeriod.getLabel();
            allStudyPeriod[count][1] = studyPeriod.getStartDate();
            allStudyPeriod[count][2] = studyPeriod.getEndDate();
            allStudyPeriod[count][3] = studyPeriod.getDegreeLvl().getDegreeLvl();
            allStudyPeriod[count][4] = "<html><B>View Grades</B></html>";
            count++;
        }

        return allStudyPeriod;
    }

    public void refresh() {
        student = Student.getInstance(student.getRegNum());
        studyPeriods = student.getStudyPeriodList();
        allStudyPeriod = getData();
        table.setModel(new DefaultTableModel(allStudyPeriod, columnNames));
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("View Course")) {
            String courseCode = getCourseCode();
            Course course = Course.getInstance(courseCode);
            new SubFrame("Course: " + courseCode, parent, new IndividualCourse(parent, course));
        }
        if (event.getActionCommand().equals("View Current Progress")) {
            JOptionPane.showMessageDialog(null, student.getStudentResults());
        }
        if (event.getActionCommand().equals("Progress Student")) {
            if (student.nextYear()) {
                JOptionPane.showMessageDialog(null, "Student Progression completed succesfully");
                parent.refreshAll();
            } else {
                JOptionPane.showMessageDialog(null, "Error, Registration not complete or marks not assigned");
            }

        }
    }
}
