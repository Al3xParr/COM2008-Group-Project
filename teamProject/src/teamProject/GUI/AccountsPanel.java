package teamProject.GUI;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import teamProject.Classes.*;
import teamProject.SystemSecurity;
import java.util.*;
import java.awt.event.*;
import java.awt.*;

public class AccountsPanel extends RefreshablePanel implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    MainFrame parent;
    HashMap<Integer, Student> students;
    HashMap<String, Registrar> registrars;
    HashMap<String, Teacher> teachers;
    HashMap<String, Administrator> admins;

    Object[][] accounts;
    String[] colNames = new String[3];
    DefaultTableModel model;
    JTable table;
    JButton addBtn;
    
    JLabel header;

    JPanel headerPanel;
    JScrollPane scrollPane;

    BoxLayout mainForm = new BoxLayout(this,BoxLayout.PAGE_AXIS);
    
    public AccountsPanel(MainFrame parent) {

        this.parent = parent;
        students = Student.instances;
        registrars = Registrar.instances;
        teachers = Teacher.instances;
        admins = Administrator.instances;

        colNames[0] = "Username";
        colNames[1] = "Access Level";
        colNames[2] = "Delete Account";
        accounts = fillData();
        model = new DefaultTableModel(accounts, colNames);
        table = new JTable(model);
        table.setEnabled(false);


        header = new JLabel("<html><div style = 'text-align : center;'><<h2>View All Accounts:</h2><br><h4></h4></div>");
        header.setAlignmentY(Component.CENTER_ALIGNMENT);
        header.setVerticalAlignment(SwingConstants.CENTER);
        header.setOpaque(true);

        addBtn = new JButton("Add New Account");
        addBtn.addActionListener(this);

        headerPanel = new JPanel();

        BoxLayout headerForm = new BoxLayout(headerPanel, BoxLayout.LINE_AXIS);
        headerPanel.setLayout(headerForm);

        headerPanel.add(Box.createHorizontalGlue());
        header.setMaximumSize(new Dimension(500,100));
        headerPanel.add(header);
        
        Dimension minSize = new Dimension (25,20);
        Dimension prefSize = new Dimension (400,20);
        headerPanel.add(new Box.Filler(minSize, prefSize, prefSize));

        headerPanel.add(addBtn);
        headerPanel.add(Box.createHorizontalGlue());

        table.setPreferredSize(new Dimension(700, 200));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMaximumSize(new Dimension(703,200));
           

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 2 && row != -1){
                    String confirmStr = "Are you sure you want to delete " + accounts[row][0] + "?";
                    int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr,"Warning", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        boolean success = false;
                        switch (String.valueOf(accounts[row][1])){
                            case "Student":
                                success = Student.getByUsername(String.valueOf(accounts[row][0])).delete();
                                break;
                            case "Teacher":
                                success = Teacher.getByUsername(String.valueOf(accounts[row][0])).delete();
                                break;
                            case "Registrar":
                                success = Registrar.getByUsername(String.valueOf(accounts[row][0])).delete();
                                break;
                            case "Admin":
                                success = Administrator.getByUsername(String.valueOf(accounts[row][0])).delete();
                                break;
                        }
                    
                        if (success){
                            if (SystemSecurity.getPrivilages() == 3){
                                JOptionPane.showMessageDialog(null, "User Deleted");
                                parent.refreshAll();
                            }else{
                                JOptionPane.showMessageDialog(null, "You do not have the privileges required to do this");
                            }
                        } else{ 
                            JOptionPane.showMessageDialog(null, "User deletion failed");
                        }
                    }
                }
            }
        });
        updateScreen();
    }



    public void updateScreen() {
        removeAll();
        setLayout(mainForm);
        add(Box.createVerticalGlue());
        add(headerPanel);
        add(scrollPane);
        revalidate();
        repaint();
    }
    
    public void refresh(){
        accounts = fillData();
        model = new DefaultTableModel(accounts, colNames);
        table.setModel(model);
        updateScreen();
    }

    public Object[][] fillData(){
        int totalEntries = students.size() + registrars.size() + admins.size() + teachers.size();
        Object[][] data = new Object[totalEntries][3];
        int counter =  0;
    
        for (Administrator admin : admins.values()){
            data[counter][0] = admin.getUsername();
            data[counter][1]= "Admin";
            data[counter][2] = "<html><B>DELETE</B><html>";
            counter ++;
        }

        for (Registrar reg : registrars.values()){
            data[counter][0] = reg.getUsername();
            data[counter][1] = "Registrar";
            data[counter][2] = "<html><B>DELETE</B><html>";
            counter ++;
        }

        for (Teacher teacher : teachers.values()){
            data[counter][0] = teacher.getUsername();
            data[counter][1] = "Teacher";
            data[counter][2] = "<html><B>DELETE</B><html>";
            counter ++;
        }

        for (Student student : students.values()){
            data[counter][0] = student.getUsername();
            data[counter][1] = "Student";
            data[counter][2] = "<html><B>DELETE</B><html>";
            counter ++;
        }

        return data;
    }

    public void actionPerformed(ActionEvent event) {

        String[] types = { "Teacher", "Registrar", "Admin"};
        JComboBox<String> type = new JComboBox<String>(types);
        JTextField username = new JTextField();
        JTextField pass = new JTextField();
        JTextField confirmpass = new JTextField();
        JTextField name = new JTextField();

        Object[] msg = {"Account Type: ", type, "Username: ", username, 
                            "Password: ", pass, "Confirm Password: ", confirmpass,
                            "Name:", name};
        
        int option = JOptionPane.showConfirmDialog(null, msg, "Add Account", JOptionPane.OK_CANCEL_OPTION);    
        

        if (option == JOptionPane.OK_OPTION){
            if (SystemSecurity.getPrivilages() == 3){
            
                if (pass.getText().equals(confirmpass.getText())){
                    
                    switch (String.valueOf(type.getSelectedItem())){
                        case "Admin":
                            Administrator.createNew(username.getText(), pass.getText());
                            break;
                        case "Registrar":
                            Registrar.createNew(username.getText(), pass.getText());
                            break;
                        case "Teacher":
                            Teacher.createNew(username.getText(), pass.getText(), name.getText());
                            break;
                    }

                    JOptionPane.showMessageDialog(null, "New User Added");
                    parent.refreshAll();
                }else{
                    JOptionPane.showMessageDialog(null, "Please enter the same password");
                }
            } else{
                JOptionPane.showMessageDialog(null, "You do not have the privileges required to do this");
            }
        }
    }

}