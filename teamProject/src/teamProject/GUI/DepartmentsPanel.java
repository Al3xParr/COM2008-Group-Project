package teamProject.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import teamProject.Classes.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import teamProject.SystemSecurity;

public class DepartmentsPanel extends RefreshablePanel implements ActionListener {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent;
    HashMap<String, Department> depts;
    
    JButton addBtn;
    JTable table;
    DefaultTableModel model;
    Object[][] data;
    String[] colNames = new String[3];

    JLabel header;

    JPanel headerPanel;
    JScrollPane scrollPane;

    BoxLayout mainForm = new BoxLayout(this,BoxLayout.PAGE_AXIS);

    public DepartmentsPanel(MainFrame parent){

        this.parent = parent;
        depts = Department.getAllInstances();

        colNames[0] = "Department Code";
        colNames[1] = "Department Name";
        colNames[2] = "Remove Department";

        data = fillData();
        model = new DefaultTableModel(data, colNames);
        table = new JTable(model);

        table.setEnabled(false);

        header = new JLabel("<html><div style = 'text-align : center;'><<h2>View All Departments:</h2><br><h4></h4></div>");
        header.setAlignmentY(Component.CENTER_ALIGNMENT);
        header.setVerticalAlignment(SwingConstants.CENTER);
        header.setOpaque(true);

        addBtn = new JButton("Add New Department");
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
                    String confirmStr = "Are you sure you want to delete " + data[row][0] + "?";
                    int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr,"Warning", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        
                        if (Department.getInstance(String.valueOf(data[row][0])).delete()){
                            if (SystemSecurity.getPrivilages() == 3){
                                JOptionPane.showMessageDialog(null, "Department Deleted");
                                parent.refreshAll();
                            }else{
                                JOptionPane.showMessageDialog(null, "You do not have the privileges required to do this");
                            }
                        }else{ 
                            JOptionPane.showMessageDialog(null, "Department deletion failed");
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

    public void refresh() {
        data = fillData();
        model = new DefaultTableModel(data, colNames);
        table.setModel(model);
        updateScreen();
    }

    public Object[][] fillData(){
        String[][] data = new String[depts.size()][3];
        
        int counter = 0;
        for (Department dept : depts.values()){
            data[counter][0] = dept.getDeptCode();
            data[counter][1] = dept.getFullName();
            data[counter][2] = "<html><B>DELETE</B><html>";
            counter++;
        }

        return data;
    }

    public void actionPerformed(ActionEvent event) {
        if (SystemSecurity.getPrivilages() == 3){
        
            JTextField code = new JTextField();
            JTextField name = new JTextField();
    
            Object[] msg = {"Department Code: ", code, "Department Name", name};
    
            int option = JOptionPane.showConfirmDialog(null, msg, "Add Department", JOptionPane.OK_CANCEL_OPTION);
            
    
            if (option == JOptionPane.OK_OPTION){
                if (code.getText().length() != 3){
                    JOptionPane.showMessageDialog(null, "Please enter a valid department code");
                }else if (name.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter a valid department name");
                }else{
                    Department.createNew(code.getText(), name.getText());
                    JOptionPane.showMessageDialog(null, "Department Added");
                    parent.refreshAll();
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "You do not have the privileges required to do this");
        }


    }
}
