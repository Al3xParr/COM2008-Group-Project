package teamProject.GUI;

import java.awt.*;
import java.util.Collection;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.event.*;
import java.text.ParseException;

import teamProject.StudentSystem;
import teamProject.Classes.Department;
import teamProject.Classes.Module;
import teamProject.db.Database;

public class NewModuleForm extends SubFrame implements ActionListener{

    private static final long serialVersionUID = 1L;

    public JFormattedTextField modCodeField  = null;
    public JTextField modNameField = null;
    JComboBox<String> deptBox = null;
    JComboBox<String> timeBox = null;
    JButton okButton = null;

    public NewModuleForm(MainFrame main) throws HeadlessException {
        super("Add new Module", main, new JPanel());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setSize(screenSize.width/3, screenSize.height/3);
        setLocation(screenSize.width/4, screenSize.height/4);

        JPanel panel = new JPanel();
        setContentPane(panel);
        
        okButton = new JButton();

        MaskFormatter formatter  = null; 
         
        try  {
            formatter = new MaskFormatter("####");
            modCodeField = new JFormattedTextField(formatter);
        }
            catch (ParseException e)  {
                e.printStackTrace();
            } 
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
        int moduleNum = (int) modCodeField.getValue();
        String deptCode = deptBox.getItemAt(deptBox.getSelectedIndex());
        String fullName = modNameField.getText();
        String time = timeBox.getItemAt(timeBox.getSelectedIndex());
        Module.createNew(moduleNum, deptCode, fullName, time);
        JOptionPane.showMessageDialog(null, 
         "A Module added sucessfuly. Please refresh the application to see the result.");
    }
}
