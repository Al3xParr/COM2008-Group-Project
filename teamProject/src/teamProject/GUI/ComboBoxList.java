package teamProject.GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class ComboBoxList extends SubFrame implements ActionListener{
    
    private static final long serialVersionUID = 1L;

    ArrayList<JCheckBox> itemList;
    NewCourseForm parent;

    public ComboBoxList(String title, MainFrame main, NewCourseForm parent, ArrayList<String> items, ArrayList<String> selected) {
        super(title, main, new JPanel());
        setSize(100, 300);

        this.parent = parent;
        
        JPanel scrollIn = new JPanel();
        scrollIn.setLayout(new BoxLayout(scrollIn, BoxLayout.PAGE_AXIS));
        

        itemList = new ArrayList<JCheckBox>();
        for (String i : items) {
            JCheckBox item = new JCheckBox(i, selected.contains(i));
            itemList.add(item);
            scrollIn.add(item);
        }
        JButton done = new JButton("Done");
        done.setMaximumSize(new Dimension(80, 20));
        done.setActionCommand("CheckBoxSelectionFinished");
        done.addActionListener(this);
        scrollIn.add(done);
        JScrollPane scroll = new JScrollPane(scrollIn);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setContentPane(scroll);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        ArrayList<String> selected = new ArrayList<>();
        for (JCheckBox X : itemList) {
            if (X.isSelected()) {
                selected.add(X.getText());
            }
        }

        parent.selectedDepartments = selected;
        dispose();

    }

    




}
