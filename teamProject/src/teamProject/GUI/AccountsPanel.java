package teamProject.GUI;

import javax.swing.*;
import teamProject.Classes.*;
import teamProject.SystemSecurity;
import teamProject.StudentSystem;
import java.util.*;

public class AccountsPanel extends JPanel{
    
    private static final long serialVersionUID = 1L;
    
    MainFrame parent;
    HashMap<Integer, Student> students;
    HashMap<String, Registrar> registrars;
    HashMap<String, Teacher> teachers;
    HashMap<String, Administrator> admins;

    Object[][] accounts;
    JTable table;

    public AccountsPanel(MainFrame parent) {

        this.parent = parent;
        students = Student.instances;
        registrars = Registrar.instances;
        teachers = Teacher.instances;
        admins = Administrator.instances;

        String[] colNames = {"Username", "Access Level", "Delete Account"};
        accounts = fillData();

        table = new JTable(accounts, colNames);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                System.out.println(teachers.size());
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 2){
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

                        System.out.println("Delete user " + accounts[row][0]);
                    
                        if (success){
                            JOptionPane.showMessageDialog(null, "User Deleted");
                            StudentSystem.reinstance();
                            accounts = fillData();
                            table = new JTable(accounts, colNames);
                            updateScreen();
                        } else{ 
                            JOptionPane.showMessageDialog(null, "User deletion failed");
                        }
                        
                    }
                }
            }
        });

        updateScreen();
    }

    public void updateScreen(){
        this.removeAll();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        table.setEnabled(false);
        this.add(new JScrollPane(table));
        revalidate();
        repaint();
    }

    public Object[][] fillData(){
        int totalEntries = students.size() + registrars.size() + admins.size() + teachers.size();
        Object[][] data = new Object[totalEntries][3];
        int counter =  0;
        System.out.println("Number: " + Integer.toString(totalEntries));
        for (Administrator admin : admins.values()){
            data[counter][0] = admin.getUsername();
            data[counter][1]= "Admin";
            data[counter][2] = "Remove Account";
            counter ++;
        }

        for (Registrar reg : registrars.values()){
            data[counter][0] = reg.getUsername();
            data[counter][1] = "Registrar";
            data[counter][2] = "Remove Account";
            counter ++;
        }

        for (Teacher teacher : teachers.values()){
            data[counter][0] = teacher.getUsername();
            data[counter][1] = "Teacher";
            data[counter][2] = "Remove Account";
            counter ++;
        }

        for (Student student : students.values()){
            data[counter][0] = student.getUsername();
            data[counter][1] = "Student";
            data[counter][2] = "Remove Account";
            counter ++;
        }

        return data;
    }

    
    
    

}