package teamProject.GUI;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import java.awt.*;
import java.util.Collection;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.event.*;
import java.text.ParseException;

import teamProject.Classes.Department;
import teamProject.Classes.Module;

public class NewModuleForm extends SubFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    JFormattedTextField modCodeField = null;
    JTextField modNameField = null;
    JComboBox<String> deptBox = null;
    JComboBox<String> timeBox = null;

    public NewModuleForm(MainFrame main) throws HeadlessException {
        super("Add new Module", main, new RefreshablePanel());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setLocation(screenSize.width / 4, screenSize.height / 4);

        RefreshablePanel panel = new RefreshablePanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalGlue());
        setSize(400, 215);

        modNameField = new JTextField(15);

        MaskFormatter formatter = null;

        try {
            formatter = new MaskFormatter("####");
            modCodeField = new JFormattedTextField(formatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        modCodeField.setColumns(4);

        timeBox = new JComboBox<String>();
        timeBox.addItem("AUTUMN");
        timeBox.addItem("SPRING");
        timeBox.addItem("ALL YEAR");
        timeBox.addItem("SUMMER");

        Collection<Department> departments = Department.allInstances();
        int deptNumber = departments.size();
        String[] allDeptCodes = new String[deptNumber];

        int i = 0;
        for (Department department : departments) {
            allDeptCodes[i] = department.getDeptCode();
            i++;
        }
        deptBox = new JComboBox<String>(allDeptCodes);

        Dimension maxSize = new Dimension(90, 70);
        modNameField.setMaximumSize(maxSize);
        modCodeField.setMaximumSize(maxSize);
        timeBox.setMaximumSize(maxSize);
        deptBox.setMaximumSize(maxSize);

        panel.add(getFormField("Module Code Number:                              ",modCodeField));
        panel.add(getFormField("Full Module Name:   ",modNameField));
        panel.add(getFormField("Time of Study:                                 ",timeBox));
        panel.add(getFormField("Supervising Department:                      ",deptBox));

        JButton button = new JButton("Confirm");
        button.addActionListener(this);
        panel.add(getFormField("", button));
        panel.add(Box.createVerticalGlue());
        setContentPane(panel);
    }

    private JPanel getFormField(String label, JComponent field) {
        JPanel res = new JPanel();
        res.setLayout(new BoxLayout(res, BoxLayout.LINE_AXIS));
        res.add(Box.createHorizontalGlue());
        res.add(new JLabel(label));
        res.add(Box.createRigidArea(new Dimension(10, 0)));
        res.add(field);
        res.add(Box.createHorizontalGlue());
        return res;
    }

    public void actionPerformed(ActionEvent e) {
        int moduleNum = Integer.valueOf((String) modCodeField.getValue());
        String deptCode = deptBox.getItemAt(deptBox.getSelectedIndex());
        String fullName = modNameField.getText();
        String time = timeBox.getItemAt(timeBox.getSelectedIndex());
        String modCode = deptCode + String.valueOf(moduleNum);

        if (String.valueOf(moduleNum).length() == 0) {
            JOptionPane.showMessageDialog(null, "Module Name cannot be empty");
        } else if (fullName.length() == 0) {
            JOptionPane.showMessageDialog(null, "Module Name cannot be empty");
        } else if (Module.checkPrimaryKeyExists(modCode)) {
            JOptionPane.showMessageDialog(null, "Module code taken, please choose different number");
        } else {
            Module.createNew(moduleNum, deptCode, fullName, time);
            JOptionPane.showMessageDialog(null, "A Module added sucessfuly");
            main.refreshAll();
            dispose();
        }
    }
}
