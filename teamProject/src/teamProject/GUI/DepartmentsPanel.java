package teamProject.GUI;

import javax.swing.*;
import teamProject.Classes.*;
import java.util.*;
import java.awt.event.*;
import teamProject.StudentSystem;

public class DepartmentsPanel extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent;
    HashMap<String, Department> depts;
    
    JButton addBtn;
    JTable table;
    Object[][] data;
    String[] colNames = new String[3];


    public DepartmentsPanel(MainFrame parent){

        this.parent = parent;
        depts = Department.getAllInstances();

        colNames[0] = "Department Code";
        colNames[1] = "Department Name";
        colNames[2] = "Remove Department";

        data = fillData();
        table = new JTable(data, colNames);
        addBtn = new JButton("Add Department");
        addBtn.addActionListener(this);
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
        this.add(addBtn);
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
            data[counter][2] = "DELETE";
            counter++;
        }

        return data;
    }

    public void actionPerformed(ActionEvent event) {
        JTextField code = new JTextField();
        JTextField name = new JTextField();

        Object[] msg = {"Course Code: ", code, "Course Name", name};

        int option = JOptionPane.showConfirmDialog(null, msg, "Add Department", JOptionPane.OK_CANCEL_OPTION);
        

        if (option == JOptionPane.OK_OPTION){
            Department.createNew(code.getText(), name.getText());
            JOptionPane.showMessageDialog(null, "Department Added");
            StudentSystem.reinstance();
            data = fillData();
            table = new JTable(data, colNames);
            updateScreen();
        }

    }
}
