package teamProject.GUI;

import java.awt.*;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;

import java.awt.event.*;
import java.text.DateFormat;

import teamProject.Classes.*;


public class NewStudentForm extends SubFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    MainFrame parent;

    JTextField usernameField;
    JTextField passField;
    JComboBox<String> titleField;
    JTextField surnameField;
    JTextField foreNamesField;
    JTextField tutorField;
    JComboBox<String> courseField;
    JComboBox<String> lvlField;
    JFormattedTextField startDateField;
    DateFormat df;

    public NewStudentForm(MainFrame main) throws HeadlessException {
        super("Add Student", main, new RefreshablePanel());
        parent = main;

        setSize(300, 300);

        RefreshablePanel panel = new RefreshablePanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalGlue());

        Dimension maxSize = new Dimension(130, 30);

        usernameField = new JTextField(15);
        usernameField.setMaximumSize(maxSize);
        panel.add(getFormField("Username:", usernameField));

        passField = new JTextField(15);
        passField.setMaximumSize(maxSize);
        panel.add(getFormField("Password:", passField));

        String[] titles = { "Mr", "Ms", "Mx" };
        titleField = new JComboBox<String>(titles);
        titleField.setMaximumSize(maxSize);
        panel.add(getFormField("Title:", titleField));

        surnameField = new JTextField(15);
        surnameField.setMaximumSize(maxSize);
        panel.add(getFormField("Surname:    ", surnameField));

        foreNamesField = new JTextField(15);
        foreNamesField.setMaximumSize(maxSize);
        panel.add(getFormField("Forenames:", foreNamesField));

        tutorField = new JTextField(15);
        tutorField.setMaximumSize(maxSize);
        panel.add(getFormField("Tutor:           ", tutorField));

        Vector<String> courses = new Vector<String>();
        for (Course c : Course.instances.values()) {
            courses.add(c.getCourseCode());
        }
        courseField = new JComboBox<String>(courses);
        courseField.setMaximumSize(maxSize);
        panel.add(getFormField("Course:", courseField));

        String[] lvls = { "1", "2", "3", "4" };
        lvlField = new JComboBox<String>(lvls);
        lvlField.setMaximumSize(maxSize);
        panel.add(getFormField("Starting Level of Study:", lvlField));

        df = DateFormat.getDateInstance(DateFormat.SHORT);
        startDateField = new JFormattedTextField(df);
        startDateField.setColumns(12);
        startDateField.setMaximumSize(maxSize);
        panel.add(getFormField("Start Date (dd/mm/yyyy):", startDateField));

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
        String error = "";
        String username = usernameField.getText();
        if(username.length()==0|username.length()>50){
            error+="Incorrect number of characters in username\n";
        }
        if(User.checkPrimaryKeyExists(username)){
            error+="Username Taken\n";
        }
        String password = passField.getText();
        if(password.length()<8){
            error+="Password should have at least 8 characters\n";
        }
        String title = (String)titleField.getSelectedItem();
        String surname = surnameField.getText();
        String forenames = foreNamesField.getText();
        if((surname.length() + forenames.split(" ").length)>30){
            error+="Please enter less forenames\n";
        }
        String tutor = tutorField.getText();
        if(tutor.length()>50){
            error+="Tutor name too long\n";
        }
        Course course = Course.getInstance((String)courseField.getSelectedItem());
        String degreeLvl = (String)lvlField.getSelectedItem();
        if (course.getStudyLvl(Integer.parseInt(degreeLvl)) == null) {
            error += "This course doesn't have this study lvl";
        }
        Date startDate = (Date)startDateField.getValue();
        long milisecInYear = 31557600000L;
        long milisecInDay = 86400000L;
        Date endDate = new Date(startDate.getTime() + milisecInYear - milisecInDay);

        if(error.equals("")){
            Student.createNew(username, password, title, surname, forenames, tutor, course, degreeLvl, startDate,
                    endDate);
            JOptionPane.showMessageDialog(null, "Student added successfully");
            main.refreshAll();
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, error);
        }

    }

}
