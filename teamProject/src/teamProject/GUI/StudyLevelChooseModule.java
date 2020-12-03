package teamProject.GUI;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;

import java.awt.event.*;

import teamProject.Classes.Module;
import teamProject.Classes.*;

public class StudyLevelChooseModule extends SubFrame implements ActionListener{

    private static final long serialVersionUID = 1L;

    MainFrame parent;

    JComboBox<String> modules;
    JCheckBox core;
    StudyLevel SL;
    public StudyLevelChooseModule(MainFrame main, StudyLevel SL) throws HeadlessException {
        super("Add Module", main, new RefreshablePanel());
        parent = main;
        this.SL = SL;

        setSize(220,120);
        

        RefreshablePanel panel = new RefreshablePanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(Box.createVerticalGlue());
        Vector<String> notYetChoosen = new Vector<String>();
        for (Module m : Module.instances.values()) {
            boolean add = true;
            for (StudyLevel SLS : Course.getInstance(SL.getCourseCode()).getDegreeLvlList()) {
                if(SLS.getCoreModules().contains(m) || SLS.getOptionalModules().contains(m)){
                    add = false;
                }
            }

            if (add) {
                notYetChoosen.add(m.getModuleCode());
            }

        }
        modules = new JComboBox<>(notYetChoosen);
        modules.setMaximumSize(new Dimension(0,30));
        panel.add(getFormField("Choose module: ",modules));

        core = new JCheckBox();
        panel.add(getFormField("Is core? ",core));

        JButton button = new JButton("Confirm");
        button.addActionListener(this);
        panel.add(getFormField("", button));
        panel.add(Box.createVerticalGlue());
        setContentPane(panel);

    }

    private JPanel getFormField(String label, JComponent field) {
        JPanel res = new JPanel();
        res.setLayout(new BoxLayout(res, BoxLayout.LINE_AXIS));
        res.add(Box.createHorizontalGlue());
        res.add(new JLabel(label));
        res.add(Box.createRigidArea(new Dimension(10, 0)));
        res.add(field);
        res.add(Box.createHorizontalGlue());
        return res;
    }

    public void actionPerformed(ActionEvent e) {
        
        Module m = Module.getInstance((String)modules.getSelectedItem());

        SL.addModule(m, core.isSelected());
        JOptionPane.showMessageDialog(null, "Module added successfully");
        main.refreshAll();
        dispose();
        
    }

    
}
