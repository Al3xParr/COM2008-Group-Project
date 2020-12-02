package teamProject.GUI;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import teamProject.Classes.*;
import teamProject.Classes.Module;

public class StudyLevelPanel extends JPanel {
    
    private static final long serialVersionUID = 1L;
    MainFrame parent = null;

    public StudyLevelPanel(MainFrame parent, StudyLevel studyLevel) {
        this.parent = parent;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        String courseCode = studyLevel.getCourseCode();
        String degreeLvl = studyLevel.getDegreeLvl();
        ArrayList<Module> coreModules = studyLevel.getCoreModules();
        ArrayList<Module> optionalModules = studyLevel.getOptionalModules();

        JLabel courseCodeLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>Course: " + courseCode + "</h2><br>");
        courseCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(courseCodeLabel);
        JLabel degreeLvlLabel = new JLabel(
                "<html><div style = 'text-align : center;'><<h2>Degree Level: " + degreeLvl + "</h2><br>");
        degreeLvlLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(degreeLvlLabel);
    }
}
