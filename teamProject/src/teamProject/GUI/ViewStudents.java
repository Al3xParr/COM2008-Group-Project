package teamProject.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import teamProject.Classes.*;
import java.util.*;

public class ViewStudents extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;

    public ViewStudents(MainFrame parent, Collection<Student> students) {
        this.parent = parent;
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

        JMenu accountMenu = new JMenu("Accounts");
        accountMenu.add(new JMenuItem("Login"));
        accountMenu.addSeparator();
        accountMenu.add(new JMenuItem("Register"));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(viewMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(accountMenu);
        parent.setJMenuBar(menuBar);

        JLabel header = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>View all students:</h2><br><h4>To view a particular student, press the view button</h4></div>");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header);

        final JTable table = setColumnWidth(new JTable(allStudents, colNames));
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane);
        
    }

    public JTable setColumnWidth(JTable table) {
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(40);
        table.getColumnModel().getColumn(5).setPreferredWidth(110);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(8).setPreferredWidth(45);
        return table;
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        System.out.println(command);
    }
}
