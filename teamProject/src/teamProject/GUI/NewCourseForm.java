package teamProject.GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.awt.event.*;

import teamProject.Classes.Course;
import teamProject.Classes.Department;

public class NewCourseForm extends SubFrame implements ActionListener, ItemListener {

    private static final long serialVersionUID = 1L;

    String[] courseTypes;
    JPanel cards;

    public ArrayList<String> selectedDepartments;

    Dimension maxSize = new Dimension(200, 20);

    JFormattedTextField courseCodeMSc;
    JFormattedTextField courseCodeBSc;
    JFormattedTextField courseCodeMEng;

    JTextField courseNameMSc;
    JTextField courseNameBSc;
    JTextField courseNameMEng;

    JComboBox<String> courseMainMSc;
    JComboBox<String> courseMainBSc;
    JComboBox<String> courseMainMEng;

    JCheckBox courseYIIMSc;
    JCheckBox courseYIIBSc;
    JCheckBox courseYIIMEng;

    JComboBox<String> courseEquivMSc;

    MainFrame parent;

    public NewCourseForm(MainFrame main) throws HeadlessException {
        super("Add new Course", main, new JPanel());
        parent = main;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setSize(screenSize.width / 3, screenSize.height / 3);
        setLocation(screenSize.width / 4, screenSize.height / 4);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalGlue());
        courseTypes = new String[] { "1 year MSc", "BSc/BEng", "MComp/MEng" };
        cards = new JPanel(new CardLayout());

        JPanel card1 = getMScForm();
        JPanel card2 = getBScForm();
        JPanel card3 = getMCompForm();

        cards.add(card1, courseTypes[0]);
        cards.add(card2, courseTypes[1]);
        cards.add(card3, courseTypes[2]);

        JPanel comboBoxPane = new JPanel();

        JLabel cTLabel = new JLabel("I want to create:");
        comboBoxPane.add(cTLabel);

        JComboBox<String> cb = new JComboBox<String>(courseTypes);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);

        panel.add(comboBoxPane);
        panel.add(cards);
        panel.add(Box.createVerticalGlue());
        selectedDepartments = new ArrayList<String>();

        setContentPane(panel);

    }

    private JPanel getMScForm() {
        JPanel res = new JPanel();
        res.setLayout(new BoxLayout(res, BoxLayout.PAGE_AXIS));

        MaskFormatter format;
        try {
            format = new MaskFormatter("###");
            courseCodeMSc = new JFormattedTextField(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.add(getFormField("Course code number:", courseCodeMSc));
        courseCodeMSc.setMaximumSize(maxSize);
        courseCodeMSc.setColumns(3);

        courseNameMSc = new JTextField(20);
        res.add(getFormField("Full name: ", courseNameMSc));
        courseNameMSc.setMaximumSize(maxSize);

        JButton b = new JButton("Choose");
        b.setMaximumSize(new Dimension(40, 20));
        b.setActionCommand("DeptChoose");
        b.addActionListener(this);
        res.add(getFormField("Choose Departments:", b));

        Vector<String> departments = new Vector<>();
        for (Department d : Department.instances.values()) {
            departments.add(d.getDeptCode());
        }
        courseMainMSc = new JComboBox<>(departments);
        courseMainMSc.setMaximumSize(maxSize);
        res.add(getFormField("Main Department: ", courseMainMSc));

        courseYIIMSc = new JCheckBox();
        res.add(getFormField("With year in Industry ", courseYIIMSc));

        JButton create = new JButton("Create Course");
        create.setMaximumSize(new Dimension(60, 30));
        create.setActionCommand("MSc");
        create.addActionListener(this);
        res.add(getFormField("", create));

        return res;
    }

    private JPanel getBScForm() {
        JPanel res = new JPanel();
        res.setLayout(new BoxLayout(res, BoxLayout.PAGE_AXIS));

        MaskFormatter format;
        try {
            format = new MaskFormatter("###");
            courseCodeBSc = new JFormattedTextField(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.add(getFormField("Course code number:", courseCodeBSc));
        courseCodeBSc.setMaximumSize(maxSize);
        courseCodeBSc.setColumns(3);

        courseNameBSc = new JTextField(20);
        res.add(getFormField("Full name: ", courseNameBSc));
        courseNameBSc.setMaximumSize(maxSize);

        JButton b = new JButton("Choose");
        b.setMaximumSize(new Dimension(40, 20));
        b.setActionCommand("DeptChoose");
        b.addActionListener(this);
        res.add(getFormField("Choose Departments:", b));

        Vector<String> departments = new Vector<>();
        for (Department d : Department.instances.values()) {
            departments.add(d.getDeptCode());
        }
        courseMainBSc = new JComboBox<>(departments);
        courseMainBSc.setMaximumSize(maxSize);
        res.add(getFormField("Main Department: ", courseMainBSc));

        courseYIIBSc = new JCheckBox();
        res.add(getFormField("With year in Industry ", courseYIIBSc));

        JButton create = new JButton("Create Course");
        create.setMaximumSize(new Dimension(60, 30));
        create.setActionCommand("BSc");
        create.addActionListener(this);
        res.add(getFormField("", create));

        return res;
    }

    private JPanel getMCompForm() {
        JPanel res = new JPanel();
        res.setLayout(new BoxLayout(res, BoxLayout.PAGE_AXIS));

        MaskFormatter format;
        try {
            format = new MaskFormatter("###");
            courseCodeMEng = new JFormattedTextField(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.add(getFormField("Course code number:", courseCodeMEng));
        courseCodeMEng.setMaximumSize(maxSize);
        courseCodeMEng.setColumns(3);

        courseNameMEng = new JTextField(20);
        res.add(getFormField("Full name: ", courseNameMEng));
        courseNameMEng.setMaximumSize(maxSize);

        JButton b = new JButton("Choose");
        b.setMaximumSize(new Dimension(40, 20));
        b.setActionCommand("DeptChoose");
        b.addActionListener(this);
        res.add(getFormField("Choose Departments:", b));

        Vector<String> departments = new Vector<>();
        for (Department d : Department.instances.values()) {
            departments.add(d.getDeptCode());
        }
        courseMainMEng = new JComboBox<>(departments);
        courseMainMEng.setMaximumSize(maxSize);
        res.add(getFormField("Main Department: ", courseMainMEng));

        courseYIIMEng = new JCheckBox();
        res.add(getFormField("With year in Industry ", courseYIIMEng));

        Vector<String> courses = new Vector<>();
        for (Course c : Course.instances.values()) {
            courses.add(c.getCourseCode());
        }
        courseEquivMSc = new JComboBox<>(courses);
        courseEquivMSc.setMaximumSize(maxSize);
        res.add(getFormField("Bachelor Equivalent: ", courseEquivMSc));

        JButton create = new JButton("Create Course");
        create.setMaximumSize(new Dimension(60, 30));
        create.setActionCommand("MEng");
        create.addActionListener(this);
        res.add(getFormField("", create));

        return res;
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
        if (e.getActionCommand().equals("DeptChoose")) {
            ArrayList<String> departments = new ArrayList<>();
            for (Department d : Department.instances.values()) {
                departments.add(d.getDeptCode());
            }
            new ComboBoxList("Choose Supervising Departments", parent, this, departments, selectedDepartments);
        }
        if (e.getActionCommand().equals("MSc")) {
            int code = Integer.parseInt((String) courseCodeMSc.getValue());
            String name = courseNameMSc.getText();
            Department mainDept = Department.getInstance((String) courseMainMSc.getSelectedItem());
            ArrayList<Department> departments = new ArrayList<>();
            for (String depCode : selectedDepartments) {
                departments.add(Department.getInstance(depCode));
            }
            if (!departments.contains(mainDept)) {
                departments.add(mainDept);
            }
            Boolean industry = courseYIIMSc.isSelected();

            if (name.length() < 1) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty");
                return;
            }
            if(!Course.checkPrimaryKeyExists(code + mainDept.getDeptCode())){
                JOptionPane.showMessageDialog(null, "Course code already taken");
                return;
            }

            Course.createNew(code, name, industry, null, mainDept, departments, 1);

            JOptionPane.showMessageDialog(null,
                    "A Course added sucessfuly. Please refresh the application to see the result.");
            dispose();
        }
        if (e.getActionCommand().equals("BSc")) {
            int code = Integer.parseInt((String) courseCodeBSc.getValue());
            String name = courseNameBSc.getText();
            Department mainDept = Department.getInstance((String) courseMainBSc.getSelectedItem());
            ArrayList<Department> departments = new ArrayList<>();
            for(String depCode : selectedDepartments){
                departments.add(Department.getInstance(depCode));
            }
            if (!departments.contains(mainDept)) {
                departments.add(mainDept);
            }
            
            Boolean industry = courseYIIBSc.isSelected();

            if (name.length() < 1) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty");
                return;
            }
            if(!Course.checkPrimaryKeyExists(code + mainDept.getDeptCode())){
                JOptionPane.showMessageDialog(null, "Course code already taken");
                return;
            }

            Course.createNew(code, name, industry, null, mainDept, departments, 3);

            JOptionPane.showMessageDialog(null,
                    "A Course added sucessfuly. Please refresh the application to see the result.");
            dispose();
        }
        if (e.getActionCommand().equals("MEng")) {
            int code = Integer.parseInt((String) courseCodeMEng.getValue());
            String name = courseNameMEng.getText();
            Department mainDept = Department.getInstance((String) courseMainMEng.getSelectedItem());
            ArrayList<Department> departments = new ArrayList<>();
            for (String depCode : selectedDepartments) {
                departments.add(Department.getInstance(depCode));
            }
            if(!departments.contains(mainDept)){
                departments.add(mainDept);
            }
            Boolean industry = courseYIIMEng.isSelected();
            Course equiv = Course.getInstance((String)courseEquivMSc.getSelectedItem());

            if (name.length() < 1) {
                JOptionPane.showMessageDialog(null, "Name cannot be empty");
                return;
            }
            if(!Course.checkPrimaryKeyExists(code + mainDept.getDeptCode())){
                JOptionPane.showMessageDialog(null, "Course code already taken");
                return;
            }

            Course.createNew(code, name, industry,equiv, mainDept, departments, 4);

            JOptionPane.showMessageDialog(null,
                    "A Course added sucessfuly. Please refresh the application to see the result.");
            dispose();
        }
    }

    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, (String) evt.getItem());
    }
}
