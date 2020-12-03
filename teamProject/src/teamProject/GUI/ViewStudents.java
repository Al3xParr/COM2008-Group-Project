package teamProject.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import teamProject.Classes.*;
import teamProject.*;
import java.util.*;

public class ViewStudents extends RefreshablePanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    Collection<Student> students;
    JTable table;
    String[] columnNames = { "RegNum", "Username", "Title", "First names", "Surname", "Email", "Tutor", "Course",
            "View" };
    public ViewStudents(MainFrame parent, Collection<Student> students) {
        this.parent = parent;
        this.students = students;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Object[][] allStudents = getData();
        

        JLabel header = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>View all students:</h2><br><h3>To view a particular student, press the view button</h3></div>");
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header);
        if (SystemSecurity.getPrivilages() == 2) {
            JButton addStudentButton = new JButton("Add student");
            addStudentButton.addActionListener(this);
            addStudentButton.setMaximumSize(new Dimension(140, 0));
            addStudentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(addStudentButton);
        }

        table = setColumnWidth(new JTable(allStudents, columnNames));
        table.setPreferredScrollableViewportSize(new Dimension(700, 200));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 8 && row != -1) {
                    new SubFrame("Student: " + allStudents[row][1], parent,
                            new IndividualStudent(parent, Student.getByUsername((String) allStudents[row][1])));
                }
            }
        });
    }
    
    private Object[][] getData() {
        Object[][] allStudents = new Object[students.size()][columnNames.length];

        int count = 0;

        for (Student student : students) {

            allStudents[count][0] = student.getRegNum();
            allStudents[count][1] = student.getUsername();
            allStudents[count][2] = student.getTitle();
            allStudents[count][3] = student.getForenames();
            allStudents[count][4] = student.getSurname();
            allStudents[count][5] = student.getEmail();
            allStudents[count][6] = student.getTutor();
            allStudents[count][7] = student.getCourse().getFullName();
            allStudents[count][8] = "<html><B>View Student</B></html>";
            count++;
        }

        return allStudents;
    }
    
    public void refresh() {
        table.setModel(new DefaultTableModel(getData(), columnNames));
        revalidate();
        repaint();
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

    @Override
    public void actionPerformed(ActionEvent e) {
        
        new NewStudentForm(parent);

    }
}
