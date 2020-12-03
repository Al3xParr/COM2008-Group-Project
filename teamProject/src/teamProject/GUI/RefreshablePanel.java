package teamProject.GUI;

import javax.swing.*;

public class RefreshablePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public RefreshablePanel() {
        super();
    }

    public void refresh() {
        System.out.println("Sth is wrong refresh of RefreshablePanel called");
    }
}
