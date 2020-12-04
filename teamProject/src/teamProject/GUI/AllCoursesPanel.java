package teamProject.GUI;

import teamProject.SystemSecurity;
import teamProject.Classes.Course;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

/** GUI for all Courses
*/

public class AllCoursesPanel extends RefreshablePanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    String[] columnNames;
    JTable table;
    Collection<Course> courses;

    public AllCoursesPanel(MainFrame parent, Collection<Course> courses) {
        this.parent = parent;
        this.courses = courses;
        JButton addCButton = new JButton("Add Course");
        addCButton.addActionListener(this);

        columnNames = getColumnNames();
        Object[][] allCourses = getData();

        BoxLayout form = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(form);

        add(Box.createVerticalGlue());

        JLabel header = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>View All Courses:</h2><br><h4></h4></div>");
        header.setAlignmentY(Component.CENTER_ALIGNMENT);
        header.setVerticalAlignment(SwingConstants.CENTER);
        header.setOpaque(true);

        JPanel headerPanel = new JPanel();
        BoxLayout form1 = new BoxLayout(headerPanel, BoxLayout.LINE_AXIS);
        headerPanel.setLayout(form1);
        add(headerPanel);

        headerPanel.add(Box.createHorizontalGlue());
        header.setMaximumSize(new Dimension(500, 100));
        headerPanel.add(header);

        Dimension minSize = new Dimension(25, 20);
        Dimension prefSize = new Dimension(400, 20);
        headerPanel.add(new Box.Filler(minSize, prefSize, prefSize));

        if (SystemSecurity.getPrivilages() == 3) {
            headerPanel.add(addCButton);
        }

        headerPanel.add(Box.createHorizontalGlue());

        //Overides default data model behind JTable making it unedible
        table = new JTable();
        table.setModel(new DefaultTableModel(allCourses, columnNames));
        setColumnWidth(table);

        table.setEnabled(false);
        table.setPreferredSize(new Dimension(700, 200));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setMaximumSize(new Dimension(703, 200));
        add(scrollpane);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 4 && row != (-1)) {
                    Course c = Course.getInstance((String) (allCourses[row][0]));
                    new SubFrame("Course: " + c.getFullName(), parent, new IndividualCourse(parent, c));
                }
                if (col == 5 && row != (-1)) {
                    String confirmStr = "Are you sure you want to delete " + allCourses[row][1] + "?";
                    int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr, "Warning",
                            JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        if (Course.getInstance((String) (allCourses[row][0])).delete()) {
                            JOptionPane.showMessageDialog(null,
                                    "Course Deleted");
                            parent.refreshAll();
                        } else {
                            JOptionPane.showMessageDialog(null, "Course deletion failed");
                        }
                    }
                }

            }
        });
    }

    private Object[][] getData() {
        Object[][] allCourses = new Object[courses.size()][columnNames.length];
        int row = 0;

        for (Course course : courses) {

            allCourses[row][0] = course.getCourseCode();
            allCourses[row][1] = course.getFullName();
            allCourses[row][2] = (course.getBachEquiv() == null) ? "NONE" : course.getBachEquiv().getFullName();
            allCourses[row][3] = course.isYearInIndustry();
            allCourses[row][4] = "<html><B>VIEW</B></html>";
            if (SystemSecurity.getPrivilages() == 3) {
                allCourses[row][5] = "<html><B>DELETE</B></html>";
            }
            row++;
        }
        return allCourses;
    }

    public void setColumnWidth(JTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
    }

    public void refresh() {
        Collection<Course> updated = new ArrayList<Course>();
        for (Course c : courses) {
            updated.add(Course.getInstance(c.getCourseCode()));
        }
        courses= updated;
        table.setModel(new DefaultTableModel(getData(), columnNames));
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent event) {
        
        new NewCourseForm(parent);

    }

    private String[] getColumnNames() {
        if (SystemSecurity.getPrivilages() == 3) {
            return new String[] { "Course Code", "Full Name", "Equivalent Bachelor", "With Year in Industry ?", "",
                    "" };
        }

        return new String[] { "Course Code", "Full Name", "Equivalent Bachelor", "With Year in Industry ?", "" };
    }
}
