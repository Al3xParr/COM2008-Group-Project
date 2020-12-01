package teamProject.GUI;

import javax.swing.*;
import teamProject.Classes.*;
import java.util.*;

public class DepartmentsPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent;
    HashMap<String, Department> depts;
    JTable table;

    public DepartmentsPanel(MainFrame parent){

        this.parent = parent;
        depts = Department.getAllInstances();

        String[] colNames = {"Department Code", "Department Name", "Remove Department"};
        String[][] data = new String[depts.size()][3];
        System.out.println(depts.size());

        int counter = 0;
        for (Department dept : depts.values()){
            data[counter][0] = dept.getDeptCode();
            data[counter][1] = dept.getFullName();
            data[counter][2] = "Remove Department";
            counter++;
        }

        table = new JTable(data, colNames);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 2){
                    String confirmStr = "Are you sure you want to delete " + data[row][0] + "?";
                    int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr,"Warning", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        System.out.println("Delete user " + data[row][0]);
                        //if (SystemSecurity.removeUser(String.valueOf(data[row][0]))){
                        //    JOptionPane.showMessageDialog(null, "User Deleted");
                        //} else{ 
                        //    JOptionPane.showMessageDialog(null, "User deletion failed");
                        //}
                    }
                }
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        table.setEnabled(false);
        this.add(new JScrollPane(table));

    }
}
