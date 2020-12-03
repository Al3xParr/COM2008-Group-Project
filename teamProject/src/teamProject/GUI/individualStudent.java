package teamProject.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

import teamProject.SystemSecurity;
import teamProject.Classes.*;

public class IndividualStudent extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    int numGrades = 0;
    Student student = null;
    String courseCode;
    Course course = null;

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
        ArrayList<StudyPeriod> studyPeriods = student.getStudyPeriodList();
        courseCode = student.getCourse().getCourseCode();
        course = Course.getInstance(courseCode);

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
                "<html><div style = 'text-align : center;'><<h3>Course: " + courseName + "</h3>");
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

        if (SystemSecurity.getPrivilages() == 1) {
            JButton gradeButton = new JButton("View Current Progress");
            gradeButton.addActionListener(this);
            add(gradeButton);

            JButton progressButton = new JButton("Progress Student");
            progressButton.addActionListener(this);
            add(progressButton);
        }

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
                    String ref = regNum + (String)allStudyPeriod[row][0];
                    StudyPeriod period = StudyPeriod.getInstance(ref);
                    new SubFrame("Study Period: "+ allStudyPeriod[row][0], parent, 
                                new StudyPeriodPanel(parent, period));
                }
            }
        });
    }

    public String getCourseCode() {
        return this.courseCode;
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
            String level;
            /*if (course.getMasters()) {
                if (student.getDegreeLvl() == "3" && course.getYearInIndustry()) {
                    level = "P";
                } else if (student.getDegreeLvl() == "3" && !course.getYearInIndustry()) {
                    level = "4";
                } else if (student.getDegreeLvl() == "4") {
                    level = "G";
                } else if (student.getDegreeLvl().equals("2")) {
                    level = "3";
                } else {
                    level = "2";
                }
            //else {*/
            if (student.getDegreeLvl().equals("2")) {
                if (course.getYearInIndustry()) {
                    level = "P";
                } else {
                    level = "3";
                }
            } else if (student.getDegreeLvl().equals("3"))  {
                level = "G";
            } else {
                level = "2";
            }
            //}
            System.out.println(level);
            if (!level.equals("G")) {
                //working out the start and end date
                long milliStart =System.currentTimeMillis();  
                java.sql.Date startDate =new java.sql.Date(milliStart);  
                System.out.println(startDate);
                Calendar c = Calendar.getInstance(); 
                c.setTime(startDate); 
                c.add(Calendar.YEAR, 1);
                long milliEnd = c.getTimeInMillis();
                java.sql.Date endDate = new java.sql.Date(milliEnd);
                System.out.println(endDate);

                //retrieving the study level
                StudyLevel studyLevel = StudyLevel.getInstance(level + courseCode);

                StudyPeriod.createNew(student.getRegNum(), "Z", startDate, endDate, studyLevel);
                System.out.println("Update success");
            }
        }
    }
}
