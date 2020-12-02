package teamProject.GUI;

import java.awt.*;
import java.text.NumberFormat;

import javax.swing.*;
import java.util.ArrayList;

import teamProject.SystemSecurity;
import teamProject.Classes.*;
import teamProject.Classes.Module;

public class StudyPeriodPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;

    public StudyPeriodPanel(MainFrame parent, StudyPeriod studyPeriod) {
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String label = studyPeriod.getLabel();
        ArrayList<Grade> grades = studyPeriod.getGradesList();

        JLabel header = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Period: " + label + " </h3><br>");
        header.setHorizontalAlignment(SwingConstants.LEFT);
        add(header);

        JLabel gradeLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h3>Grades: </h3><br>");
        gradeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(gradeLabel);
        
        if (SystemSecurity.getPrivilages() == 1) {
            String[] colNames = {"Module", "Mark", "Resit Mark", "Edit Grades"};
            Object[][] gradesTable = new Object[grades.size()][4];

            int count = 0;
            for (Grade grade: grades) {
                gradesTable[count][0] = grade.getModule().getModuleCode();
                gradesTable[count][1] = grade.getMark();
                gradesTable[count][2] = grade.getResitMark();
                gradesTable[count][3] = "<html><B>Edit Grades</B></html>";
                count ++;
            }
            
            final JTable table = new JTable(gradesTable, colNames);
            table.setPreferredScrollableViewportSize(new Dimension(300, 150));
            table.setFillsViewportHeight(true);
            JScrollPane scrollpane = new JScrollPane(table);
            add(scrollpane);

            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table.rowAtPoint(evt.getPoint());
                    int col = table.columnAtPoint(evt.getPoint());
                    if (col == 3){
                        String[] markTypes = {"Mark", "Resit Mark"};
                        JFormattedTextField gradeInp = new JFormattedTextField(new Double(100.00));

                        JComboBox <String> markType = new JComboBox<String>(markTypes);
    
                        Object[] msg = {"Grade: ", gradeInp, "Mark Type: ", markType};
    
                        int option = JOptionPane.showConfirmDialog(null, msg, "Add Grade", JOptionPane.OK_CANCEL_OPTION);

                        if (option == JOptionPane.OK_OPTION){
                            Module module = Module.getInstance((String) gradesTable[row][0]);
                            if ((Double) gradeInp.getValue() > 100){
                                JOptionPane.showMessageDialog(null, "Input grade is too large (0-100)");
                            }else if ((Double) gradeInp.getValue() < 0){
                                JOptionPane.showMessageDialog(null, "Input grade cannot be negative");
                            }else {
                                if (markType.getItemAt(markType.getSelectedIndex()).equals("Mark")) {
                                    studyPeriod.awardMark(module, (Double) gradeInp.getValue(), false);
                                    JOptionPane.showMessageDialog(null, "Mark Updated");
                                } else {
                                    studyPeriod.awardMark(module, (Double) gradeInp.getValue(), true);
                                    JOptionPane.showMessageDialog(null, "Resit Mark Updated");
                                }
                            }
                        }
            
                    }
                }
            });

        } else {
            String[] colNames = {"Module", "Mark", "Resit Mark"};
            Object[][] gradesTable = new Object[grades.size()][3];

            int count = 0;
            for (Grade grade: grades) {
                gradesTable[count][0] = grade.getModule().getFullName();
                gradesTable[count][1] = grade.getMark();
                gradesTable[count][2] = grade.getResitMark();
                count ++;
            }
            
            final JTable table = new JTable(gradesTable, colNames);
            table.setPreferredScrollableViewportSize(new Dimension(200, 70));
            table.setFillsViewportHeight(true);
            JScrollPane scrollpane = new JScrollPane(table);
            add(scrollpane);
        }

    }
}
