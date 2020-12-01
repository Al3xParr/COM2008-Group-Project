package teamProject.GUI;

import java.awt.*;
import javax.swing.*;
import teamProject.Classes.*;
import java.util.*;

public class ViewStudents extends JPanel {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;

    public ViewStudents(MainFrame parent, Collection<Student> students) {
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        int numStudents = students.size();
        String[] colNames = {"RegNum", "Username", "Title", "First names", "Surname", "Email", "Tutor", "Course", "View"};
        Object[][] allStudents = new Object[numStudents][9];

        int count = 0;

        
        for (Student student: students) {
            
            allStudents[count][0] = student.getRegNum();
            allStudents[count][1] = student.getUsername();
            allStudents[count][2] = student.getTitle();
            allStudents[count][3] = student.getForenames();
            allStudents[count][4] = student.getSurname();
            allStudents[count][5] = student.getEmail();
            allStudents[count][6] = student.getTutor();
            allStudents[count][7] = student.getCourse().getFullName();
            allStudents[count][8] = "View";
            count ++;
        }

        //creating a header menu bar
        JMenu viewMenu = new JMenu("View");
        viewMenu.add(new JMenuItem("Departments"));
        viewMenu.add(new JMenuItem("Courses"));
        viewMenu.add(new JMenuItem("Modules"));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(viewMenu);
        parent.setJMenuBar(menuBar);

        JLabel header = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>View all students:</h2><br><h3>To view a particular student, press the view button</h3></div>");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header);

        final JTable table = setColumnWidth(new JTable(allStudents, colNames));
        table.setPreferredScrollableViewportSize(new Dimension(700, 200));
        table.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane);
        

    }

    public JTable setColumnWidth(JTable table) {
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(75);
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(110);
        table.getColumnModel().getColumn(8).setPreferredWidth(60);
        return table;
    }
}
