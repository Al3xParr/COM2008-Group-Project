package teamProject.GUI;

import teamProject.SystemSecurity;
import teamProject.Classes.Module;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.*;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */
/** GUI for all Modules
*/

public class AllModulesPanel extends RefreshablePanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    MainFrame parent = null;

    Object[][] allModules;
    Collection<Module> modules;
    String[] columnNames;
    JTable table;

    public AllModulesPanel(MainFrame parent, Collection<Module> modules) {
        this.parent = parent;
        JButton addMButton = new JButton("Add Module");
        addMButton.addActionListener(this);

        this.modules = modules;
        columnNames = getColumnNames();
        allModules = getData();

        BoxLayout form = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(form);

        add(Box.createVerticalGlue());

        JLabel header = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>View All Modules:</h2><br><h4></h4></div>");
        header.setAlignmentY(Component.CENTER_ALIGNMENT);
        header.setVerticalAlignment(SwingConstants.CENTER);
        header.setOpaque(true);

        JPanel headerPanel = new JPanel();
        BoxLayout form1 = new BoxLayout(headerPanel, BoxLayout.LINE_AXIS);
        headerPanel.setLayout(form1);
        add(headerPanel);

        headerPanel.add(Box.createHorizontalGlue());
        header.setMaximumSize(new Dimension(500, 100));
        headerPanel.add(header);

        Dimension minSize = new Dimension(25, 20);
        Dimension prefSize = new Dimension(400, 20);
        headerPanel.add(new Box.Filler(minSize, prefSize, prefSize));

        if (SystemSecurity.getPrivilages() == 3) {
            headerPanel.add(addMButton);
        }
        headerPanel.add(Box.createHorizontalGlue());

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
        scrollpane.setMaximumSize(new Dimension(703, 200));
        add(scrollpane);

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (col == 4 && row != (-1)) {
                    String confirmStr = "Are you sure you want to delete " + allModules[row][1] + "?";
                    int dialogResult = JOptionPane.showConfirmDialog(null, confirmStr, "Warning",
                            JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        if (Module.getInstance((String) (allModules[row][0])).delete()) {
                            JOptionPane.showMessageDialog(null,
                                    "Module Deleted");
                            parent.refreshAll();
                        } else {
                            JOptionPane.showMessageDialog(null, "Module deletion failed");
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
        Object[][] data = new Object[modules.size()][columnNames.length];
        int row = 0;

        for (Module module : modules) {

            data[row][0] = module.getModuleCode();
            data[row][1] = module.getFullName();
            data[row][2] = module.getTimeTaught();
            data[row][3] = module.getDepartmentCode();
            if (SystemSecurity.getPrivilages() == 3) {
                data[row][4] = "<html><B>DELETE</B></html>";
            }
            row++;
        }

        return data;
    }
    
    @Override
    public void refresh() {
        Collection<Module> updated = new ArrayList<Module>();
        for (Module m : modules) {
            updated.add(Module.getInstance(m.getModuleCode()));
        }
        modules = updated;
        table.setModel(new DefaultTableModel(getData(), columnNames));
        revalidate();
        repaint();
    }
    

    public void actionPerformed(ActionEvent event) {
        new NewModuleForm(parent);
    }

    private String[] getColumnNames() {
        if (SystemSecurity.getPrivilages() == 3) {
            return new String[] { "Modul Code", "Full Name", "Semester", "Department Code", "" };
        }

        return new String[] { "Modul Code", "Full Name", "Semester", "Department Code" };
    }
}
