import javax.swing.*;
import teamProject.db.Database;
import teamProject.Classes.*;

public class AllAccounts extends JPanel{
    
    
    public AllAccounts(HashMap<String, Student> students, HashMap<String, Registrar> registrars, 
                        HashMap<String, Administrator> admins, HashMap<String, Teacher> teachers) {
        
        String[] colNames = {"Username", "Access Level", " ", " "};
        int totalEntries = students.size() + registrars.size() + admins.size() + teachers.size();
        Object[][] accounts = new Object[4][totalEntries];

        int counter =  0;

        for (Administrator admin : admins.getvalues()){
            accounts[0][counter] = admin.getUsername();
            accounts[1][counter] = "Admin";
            accounts[2][counter] = "Edit Access Level";
            accounts[3][counter] = "Remove Account";
            counter ++;
        }

        for (Registrar reg : registrars.getvalues()){
            accounts[0][counter] = registrars.getUsername();
            accounts[1][counter] = "Registrar";
            accounts[2][counter] = "Edit Access Level";
            accounts[3][counter] = "Remove Account";
            counter ++;
        }

        for (Teacher teacher : teachers.getvalues()){
            accounts[0][counter] = teachers.getUsername();
            accounts[1][counter] = "Teacher";
            accounts[2][counter] = "Edit Access Level";
            accounts[3][counter] = "Remove Account";
            counter ++;
        }

        for (Student student : students.getvalues()){
            accounts[0][counter] = students.getUsername();
            accounts[1][counter] = "Student";
            accounts[2][counter] = "Edit Access Level";
            accounts[3][counter] = "Remove Account";
            counter ++;
        }
        JTable table = new JTable(accounts, colNames);
    }

}