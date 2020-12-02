package teamProject.GUI;

import javax.swing.*;
import teamProject.Classes.*;
import java.util.*;
import teamProject.StudentSystem;

public class DepartmentsPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent;
    HashMap<String, Department> depts;
    
    JTable table;
    Object[][] data;


    public DepartmentsPanel(MainFrame parent){

        this.parent = parent;
        depts = Department.getAllInstances();

        String[] colNames = {"Department Code", "Department Name", "Remove Department"};

        data = fillData();
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
                        System.out.println("Delete dept " + data[row][0]);

                        System.out.println(Department.getInstance(String.valueOf(data[row][0])).getFullName());
                        if (Department.getInstance(String.valueOf(data[row][0])).delete()){
                            JOptionPane.showMessageDialog(null, "Department Deleted");
                            StudentSystem.reinstance();
                            data = fillData();
                            table = new JTable(data, colNames);
                            updateScreen();
                        }else{ 
                            JOptionPane.showMessageDialog(null, "Department deletion failed");
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
        String[][] data = new String[depts.size()][3];
        System.out.println(depts.size());

        int counter = 0;
        for (Department dept : depts.values()){
            data[counter][0] = dept.getDeptCode();
            data[counter][1] = dept.getFullName();
            data[counter][2] = "Remove Department";
            counter++;
        }

        return data;
    }
}
