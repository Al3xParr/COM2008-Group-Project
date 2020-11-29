import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MyFrame(String title) {
        super(title);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        setSize(screenSize.width/2, screenSize.height/2);
        setLocation(screenSize.width/4, screenSize.height/4);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setVisible(true);

    }

    public static void main(String[] args) {
        new MyFrame("Test");
        
    }

}