package teamProject.GUI;

import javax.swing.*;
import teamProject.Classes.*;
import teamProject.SystemSecurity;
import java.util.*;

public class AccountsPanel extends JPanel{
    
    private static final long serialVersionUID = 1L;
    
    MainFrame parent;
    HashMap<Integer, Student> students;
    HashMap<String, Registrar> registrars;
    HashMap<String, Teacher> teachers;
    HashMap<String, Administrator> admins;

    public AccountsPanel(MainFrame parent) {

        this.parent = parent;
        students = Student.instances;
        registrars = Registrar.instances;
        teachers = Teacher.instances;
        admins = Administrator.instances;

        String[] colNames = {"Username", "Access Level", "Delete Account"};
        int totalEntries = students.size() + registrars.size() + admins.size() + teachers.size();
        Object[][] accounts = new Object[totalEntries][3];

        int counter =  0;

        for (Administrator admin : admins.values()){
            accounts[counter][0] = admin.getUsername();
            accounts[counter][1]= "Admin";
            accounts[counter][2] = "Remove Account";
            counter ++;
        }

        for (Registrar reg : registrars.values()){
            accounts[counter][0] = reg.getUsername();
            accounts[counter][1] = "Registrar";
            accounts[counter][2] = "Remove Account";
            counter ++;
        }

        for (Teacher teacher : teachers.values()){
            accounts[counter][0] = teacher.getUsername();
            accounts[counter][1] = "Teacher";
            accounts[counter][2] = "Remove Account";
            counter ++;
        }

        for (Student student : students.values()){
            accounts[counter][0] = student.getUsername();
            accounts[counter][1] = "Student";
            accounts[counter][2] = "Remove Account";
            counter ++;
        }
        JTable table = new JTable(accounts, colNames);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                
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
                        } else{ 
                            JOptionPane.showMessageDialog(null, "User deletion failed");
                        }
                    }
                }
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        table.setEnabled(false);
        this.add(new JScrollPane(table));

    }
    
    

}