package teamProject.GUI;

import teamProject.SystemSecurity;
import teamProject.Classes.Module;
import teamProject.Classes.StudyLevel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */
 /** GUI for all Modules
*/

public class StudyLevelPanel extends RefreshablePanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;
    StudyLevel SL;
    JTable table;
    String[] columnNames;

    public StudyLevelPanel(MainFrame parent, StudyLevel SL) {
        this.parent = parent;
        this.SL = SL;
        JButton addMButton = new JButton("Add Module");
        addMButton.addActionListener(this);
        
        columnNames = getColumnNames();
        
        Object[][] allModules = getData();

        BoxLayout form = new BoxLayout(this,BoxLayout.PAGE_AXIS);
        setLayout(form);

        add(Box.createVerticalGlue());

        JLabel header = new JLabel(
            "<html><div style = 'text-align : center;'><<h2>Study Level no."+SL.getDegreeLvl()+" for "+
                                SL.getCourseCode()+"</h2><br><h4></h4></div>");
        header.setAlignmentY(Component.CENTER_ALIGNMENT);
        header.setVerticalAlignment(SwingConstants.CENTER);
        header.setOpaque(true);

        JPanel headerPanel = new JPanel();
        BoxLayout form1 = new BoxLayout(headerPanel, BoxLayout.LINE_AXIS);
        headerPanel.setLayout(form1);
        add(headerPanel);

        headerPanel.add(Box.createHorizontalGlue());
        header.setMaximumSize(new Dimension(500,100));
        headerPanel.add(header);
        
        Dimension minSize = new Dimension (25,20);
        Dimension prefSize = new Dimension (400,20);
        headerPanel.add(new Box.Filler(minSize, prefSize, prefSize));

        if (SystemSecurity.getPrivilages()==3) {
            headerPanel.add(addMButton);
        }
        headerPanel.add(Box.createHorizontalGlue());
    

        //Overides default data model behind JTable making it unedible
        table = new JTable();
        table.setModel(new DefaultTableModel(allModules, columnNames));
        table.setEnabled(false);
        setColumnWidth(table);


        table.setPreferredSize(new Dimension(700, 200));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setMaximumSize(new Dimension(703,200));
        add(scrollpane);   
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 5 && row != (-1)){
                    String confirmStr = "Are you sure you want to remove " + allModules[row][1] + " from this level?";
                    int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr,"Warning", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        if (SL.removeModule(Module.getInstance((String)(allModules[row][0])))){
                            JOptionPane.showMessageDialog(null,
                                    "Module removed");
                            parent.refreshAll();
                        } else{ 
                            JOptionPane.showMessageDialog(null, "Module removal failed");
                        }
                    }
                }
            }
        });
    }

    public void setColumnWidth(JTable table) {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
    }
    
    private Object[][] getData() {
        int moduleNumber = SL.getCoreModules().size() + SL.getOptionalModules().size();
        Object[][] allModules = new Object[moduleNumber][columnNames.length];
        int row = 0;

        for (Module module : SL.getCoreModules()) {

            allModules[row][0] = module.getModuleCode();
            allModules[row][1] = module.getFullName();
            allModules[row][2] = module.getTimeTaught();
            allModules[row][3] = module.getDepartmentCode();
            allModules[row][4] = "YES";
            if (SystemSecurity.getPrivilages() == 3) {
                allModules[row][5] = "<html><B>DELETE</B></html>";
            }
            row++;
        }

        for (Module module : SL.getOptionalModules()) {

            allModules[row][0] = module.getModuleCode();
            allModules[row][1] = module.getFullName();
            allModules[row][2] = module.getTimeTaught();
            allModules[row][3] = module.getDepartmentCode();
            allModules[row][4] = "NO";
            if (SystemSecurity.getPrivilages() == 3) {
                allModules[row][5] = "<html><B>DELETE</B></html>";
            }
            row++;
        }

        return allModules;
    }

    public void refresh() {
        SL = StudyLevel.getInstance(SL.getDegreeLvl() + SL.getCourseCode());
        table.setModel(new DefaultTableModel(getData(), columnNames));
        revalidate();
        repaint();
    }

    public void actionPerformed(ActionEvent event) {
        new StudyLevelChooseModule(parent,SL);
    }
    private String[] getColumnNames() {
        if (SystemSecurity.getPrivilages() == 3) {
            return new String[] { "Modul Code", "Full Name", "Semester", "Department Code","Is Core?", "" };
        }

        return new String[] { "Modul Code", "Full Name", "Semester", "Department Code", "Is Core?" };
    }
}
