package teamProject.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import teamProject.Classes.*;

public class IndividualStudent extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    int numGrades = 0;
    Student student;
    String courseCode;

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

        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); 
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
        courseButton.addActionListener(this);
        add(courseButton);

        JLabel studyLevelLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Study Levels: </h3>");
        add(studyLevelLabel);

        String [] colNames = {"Label", "Start Date", "End Date", "Degree Level", "View Grades"};
        Object[][] allStudyPeriod = new Object[studyPeriods.size()][5];

        //will use as a reference to use for the position in the table for each grade
        int count = 0;
        //populating the table of study periods
        for (StudyPeriod studyPeriod: studyPeriods) {
            allStudyPeriod[count][0] = studyPeriod.getLabel();
            allStudyPeriod[count][1] = studyPeriod.getStartDate();
            allStudyPeriod[count][2] = studyPeriod.getEndDate();
            allStudyPeriod[count][3] = studyPeriod.getDegreeLvl().getDegreeLvl();
            allStudyPeriod[count][4] = "<html><B>View Grades</B></html>";
            count ++;
        }

        final JTable table = new JTable(allStudyPeriod, colNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 4){
                    //TODO Create new study period panel - as an instance, use this students regID + the label
                    //Will probably have to cast to a String in order to work (look at the ViewStudents.java)
                    //new SubFrame("Study Period: "+ allStudyPeriod[row][0], parent, ...);
                    System.out.println(allStudyPeriod[row][0]);
                }
            }
        });
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public void actionPerformed(ActionEvent event) {
        String courseCode = getCourseCode();
        Course course = Course.getInstance(courseCode);
        if (event.getActionCommand().equals("View Course")) {
            new SubFrame("Course: " + courseCode, parent, new IndividualCourse(parent, course));
        }
    }
}
