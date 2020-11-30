package teamProject.GUI;

import javax.swing.*;
import teamProject.Classes.*;
import java.util.*;

public class AllAccounts extends JPanel{
    
    private static final long serialVersionUID = 1L;
    
    public AllAccounts(HashMap<String, Student> students, HashMap<String, Registrar> registrars, 
                        HashMap<String, Administrator> admins, HashMap<String, Teacher> teachers) {
        
        String[] colNames = {"Username", "Access Level", ""};
        int totalEntries = students.size() + registrars.size() + admins.size() + teachers.size();
        Object[][] accounts = new Object[3][totalEntries];

        int counter =  0;

        for (Administrator admin : admins.values()){
            accounts[0][counter] = admin.getUsername();
            accounts[1][counter] = "Admin";
            accounts[2][counter] = "Remove Account";
            counter ++;
        }

        for (Registrar reg : registrars.values()){
            accounts[0][counter] = reg.getUsername();
            accounts[1][counter] = "Registrar";
            accounts[2][counter] = "Remove Account";
            counter ++;
        }

        for (Teacher teacher : teachers.values()){
            accounts[0][counter] = teacher.getUsername();
            accounts[1][counter] = "Teacher";
            accounts[2][counter] = "Remove Account";
            counter ++;
        }

        for (Student student : students.values()){
            accounts[0][counter] = student.getUsername();
            accounts[1][counter] = "Student";
            accounts[2][counter] = "Remove Account";
            counter ++;
        }
        JTable table = new JTable(accounts, colNames);


        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            
            int row = table.rowAtPoint(evt.getPoint());
            int col = table.columnAtPoint(evt.getPoint());
            
            if (col == 4){
                String confirmStr = "Are you sure you want to delete " + accounts[0][row] + "?";
                int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr,"Warning", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    System.out.println("Delete user " + accounts[0][row]);
                    //Delete user accounts[0][row]
                }
            

        }}
    });
    }

}