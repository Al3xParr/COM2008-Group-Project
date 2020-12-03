package teamProject.GUI;

import java.awt.*;
import java.util.Collection;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.event.*;
import java.text.ParseException;

import teamProject.Classes.Department;
import teamProject.Classes.Module;

public class NewModuleForm extends SubFrame implements ActionListener{

    private static final long serialVersionUID = 1L;

    JFormattedTextField modCodeField  = null;
    JTextField modNameField = null;
    JComboBox<String> deptBox = null;
    JComboBox<String> timeBox = null;
    JButton okButton = null;

    public NewModuleForm(MainFrame main) throws HeadlessException {
        super("Add new Module", main, new RefreshablePanel());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setSize(650,115);
        setLocation(screenSize.width/4, screenSize.height/4);

        RefreshablePanel panel = new RefreshablePanel();
        setContentPane(panel);

        JLabel codeNumLabel = new JLabel("Enter Number value for Module Code, ");
        JLabel fullNameLabel = new JLabel("Full Module Name, ");
        JLabel timeLabel = new JLabel("Choose the Time Period, ");
        JLabel depLabel = new JLabel("and the Department.");

        add(codeNumLabel);
        add(fullNameLabel);
        add(timeLabel);
        add(depLabel);

        okButton = new JButton("ok");

        MaskFormatter formatter  = null; 
         
        try  {
            formatter = new MaskFormatter("####");
            modCodeField = new JFormattedTextField(formatter);
        }
            catch (ParseException e)  {
                e.printStackTrace();
            } 

        modCodeField.setColumns(4);

        add(modCodeField);

        modNameField = new JTextField(15);
        add(modNameField);

        timeBox = new JComboBox<String>();
        timeBox.addItem("AUTUMN");
        timeBox.addItem("SPRING");
        timeBox.addItem("ALL YEAR");
        timeBox.addItem("SUMMER");

        add(timeBox);

        Collection<Department> departments = Department.allInstances();
        int deptNumber = departments.size();
        String[] allDeptCodes = new String[deptNumber];

        int i = 0;
        for (Department department: departments) {
            allDeptCodes[i] = department.getDeptCode();
            i++;
        }
        deptBox = new JComboBox<String>(allDeptCodes);
        add(deptBox);
        add(okButton);

        okButton.addActionListener(this);
    }      

    public void actionPerformed(ActionEvent e) {
        int moduleNum =  Integer.valueOf((String) modCodeField.getValue());
        String deptCode = deptBox.getItemAt(deptBox.getSelectedIndex());
        String fullName = modNameField.getText();
        String time = timeBox.getItemAt(timeBox.getSelectedIndex());
        String modCode = deptCode+String.valueOf(moduleNum);

        if (String.valueOf(moduleNum).length() == 0) {
            JOptionPane.showMessageDialog(null, 
                "Module Name cannot be empty");
        }
        else if (fullName.length() == 0) {
            JOptionPane.showMessageDialog(null, 
                "Module Name cannot be empty");
        } else if (Module.checkPrimaryKeyExists(modCode)){
            JOptionPane.showMessageDialog(null, 
                "Module code taken, please choose different number");
        } else {
            Module.createNew(moduleNum, deptCode, fullName, time);
            JOptionPane.showMessageDialog(null, 
                    "A Module added sucessfuly");
            main.refreshAll();
            dispose();
        }
    }
}
