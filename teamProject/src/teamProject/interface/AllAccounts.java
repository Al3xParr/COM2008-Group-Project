import javax.swing.*;
import teamProject.db.Database;
import teamProject.Classes.*;

public class AllAccounts extends JPanel{
    
    
    public AllAccounts(HashMap<String, Student> students, HashMap<String, Registrar> registrars, 
                        HashMap<String, Administrator> admins, HashMap<String, Teacher> teachers) {
        
        String[] colNames = {"Username", "Access Level", "", ""};
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

        table.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = jTable1.rowAtPoint(evt.getPoint());
            int col = jTable1.columnAtPoint(evt.getPoint());
            
            if (col == 4){
                String confirmStr = "Are you sure you want to delete " + accounts[4][row] + "?"
                int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr,"Warning", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    //Delete user accounts[4][row]
                }
                
            }
            else if (col == 3){
                String[] possibleValues = { "Student", "Teacher", "Registrar", "Admin" };
                String selectedValue = JOptionPane.showInputDialog(null,
                    "Choose access level", "Access Level Change",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    possibleValues, possibleValues[0]);
                
                int levelNum;
                switch(selectedValue){
                    case "Student":
                        levelNum = 1;
                        break;
                    case "Teacher":
                        levelNum = 2;
                        break;
                    case "Registrar":
                        levelNum = 3;
                        break;
                    case "Admin":
                        levelNum = 4;
                        break;
                }
                //changePriv of user accounts[4][row] to levelNum
            }

        }
    });
    }

}