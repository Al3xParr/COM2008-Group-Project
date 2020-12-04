package teamProject.GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;

import teamProject.SystemSecurity;
import teamProject.Classes.*;
import teamProject.Classes.Module;

public class StudyPeriodPanel extends RefreshablePanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    StudyPeriod studyPeriod = null;
    StudyLevel studyLevel = null;
    ArrayList<Grade> grades = null;
    String[] columnNames;
    JTable table;

    public StudyPeriodPanel(MainFrame parent, StudyPeriod studyPeriod) {
        this.parent = parent;
        this.studyPeriod = studyPeriod;
        this.studyLevel = studyPeriod.getDegreeLvl();
        this.grades = studyPeriod.getGradesList();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String label = studyPeriod.getLabel();

        JLabel header = new JLabel("<html><div style = 'text-align : center;'><<h3>Period: " + label + " </h3><br>");
        header.setHorizontalAlignment(SwingConstants.LEFT);
        add(header);

        JLabel gradeLabel = new JLabel("<html><div style = 'text-align : center;'><<h3>Grades: </h3>");
        gradeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(gradeLabel);

        if (SystemSecurity.getPrivilages() == 2) {
            JButton addModuleButton = new JButton("Add Module");
            addModuleButton.addActionListener(this);
            add(addModuleButton);

            JButton removeButton = new JButton("Remove Module");
            removeButton.addActionListener(this);
            add(removeButton);
        }

        JButton resultButton = new JButton("See results");
        resultButton.addActionListener(this);
        add(resultButton);

        columnNames = getColumnNames();
        Object[][] gradesTable = getData();

        table = new JTable(gradesTable, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(300, 150));
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
                    String[] markTypes = { "Mark", "Resit Mark" };
                    JFormattedTextField gradeInp = new JFormattedTextField(Double.valueOf(100.00));

                    JComboBox<String> markType = new JComboBox<String>(markTypes);

                    Object[] msg = { "Grade: ", gradeInp, "Mark Type: ", markType };

                    int option = JOptionPane.showConfirmDialog(null, msg, "Add Grade", JOptionPane.OK_CANCEL_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        Module module = Module.getInstance((String) gradesTable[row][0]);
                        if ((Double) gradeInp.getValue() > 100) {
                            JOptionPane.showMessageDialog(null, "Input grade is too large (0-100)");
                        } else if ((Double) gradeInp.getValue() < 0) {
                            JOptionPane.showMessageDialog(null, "Input grade cannot be negative");
                        } else {
                            if (markType.getItemAt(markType.getSelectedIndex()).equals("Mark")) {
                                studyPeriod.awardMark(module, (Double) gradeInp.getValue(), false);
                                JOptionPane.showMessageDialog(null, "Mark Updated");
                                parent.refreshAll();

                            } else {
                                studyPeriod.awardMark(module, (Double) gradeInp.getValue(), true);
                                JOptionPane.showMessageDialog(null, "Resit Mark Updated");
                                parent.refreshAll();
                            }
                            parent.refreshAll();
                        }
                    }

                }
            }
        });
    }

    public String[] getRegisteredModules() {
        ArrayList<Module> optionaModules = studyLevel.getOptionalModules();
        String modulesString = "";
        for (Module module : optionaModules) {
            Boolean flag = false;
            //checking the module is already selected
            for (Grade grade : grades) {
                if (grade.getModule().equals(module)) {
                    flag = true;
                }
            }
            if (flag) {
                modulesString += module.getModuleCode() + " ";
            }
        }
        String[] modulesOptions = modulesString.split("\\W+");
        ;
        return modulesOptions;
    }

    private String[] getColumnNames() {
        if (SystemSecurity.getPrivilages() == 1) {
            return new String[] { "Module Code", "Module Name", "Mark", "Resit Mark", "Edit Grades" };
        }
        return new String[] { "Module", "Module Name", "Mark", "Resit Mark" };
    }

    private Object[][] getData() {
        Object[][] gradesTable = new Object[grades.size()][columnNames.length];
        int count = 0;
        for (Grade grade : grades) {
            gradesTable[count][0] = grade.getModule().getModuleCode();
            gradesTable[count][1] = grade.getModule().getFullName();
            gradesTable[count][2] = grade.getMark();
            gradesTable[count][3] = grade.getResitMark();
            if (SystemSecurity.getPrivilages() == 1) {
                gradesTable[count][4] = "<html><B>Edit Grades</B></html>";
            }
            count++;
        }

        return gradesTable;

    }

    public void refresh() {
        studyPeriod = StudyPeriod.getInstance(studyPeriod.getRegNum() + studyPeriod.getLabel());
        grades = studyPeriod.getGradesList();
        table.setModel(new DefaultTableModel(getData(),columnNames));
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("Add Module")) {
            ArrayList<Module> optionaModules = studyLevel.getOptionalModules();
            String modulesString = "";
            for (Module module : optionaModules) {
                Boolean flag = true;
                //checking the module is already selected
                for (Grade grade : grades) {
                    if (grade.getModule().equals(module)) {
                        flag = false;
                    }
                }
                if (flag) {
                    modulesString += module.getModuleCode() + " ";
                }
            }
            String[] modulesOptions = modulesString.split("\\W+");
            ;
            JComboBox<String> modulesCombo = new JComboBox<String>(modulesOptions);

            Object[] msg = { "Optional modules available: ", modulesCombo };
            int option = JOptionPane.showConfirmDialog(null, msg, "Add Module", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                Module selectedModule = Module.getInstance(modulesCombo.getItemAt(modulesCombo.getSelectedIndex()));
                studyPeriod.registerModule(selectedModule);
                parent.refreshAll();
            }
        }
        if (event.getActionCommand().equals("Remove Module")) {
            String[] modulesTaken = getRegisteredModules();
            JComboBox<String> deletionCombo = new JComboBox<String>(modulesTaken);

            Object[] msg = { "Optional modules for Deletion: ", deletionCombo };
            int option = JOptionPane.showConfirmDialog(null, msg, "Delete Module", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                Module selectedModule = Module.getInstance(deletionCombo.getItemAt(deletionCombo.getSelectedIndex()));
                studyPeriod.unregisterModule(selectedModule);
                System.out.println("Successfully deleted");
                parent.refreshAll();
            }
        }
        if (event.getActionCommand().equals("See results")) {
            JOptionPane.showMessageDialog(null, studyPeriod.studyPeriodResults());
        }
    }
}
