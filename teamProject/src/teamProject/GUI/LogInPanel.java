package teamProject.GUI;

/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */

import javax.swing.*;
import java.awt.*;

public class LogInPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        MainFrame parent = null;
        public JTextField usernameField = null;
        public JPasswordField passwordField = null;
        JButton confirmButton = null;

        public LogInPanel(MainFrame parent) {
                this.parent = parent;
                setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                add(Box.createVerticalGlue());
                JLabel header = new JLabel(
                                "<html><div style = 'text-align : center;'><h1>Welcome to <br> the University System</h1><br><h3>Please Log in :</h3></div>");
                header.setAlignmentX(Component.CENTER_ALIGNMENT);
                header.setHorizontalAlignment(SwingConstants.CENTER);
                //header.setMaximumSize(new Dimension(500,500));
                add(header);

                usernameField = new JTextField(25);
                passwordField = new JPasswordField(25);
                JLabel usernameLabel = new JLabel("Username : ");
                usernameLabel.setLabelFor(usernameField);
                JLabel passwordLabel = new JLabel("Password : ");
                passwordLabel.setLabelFor(passwordField);

                JPanel loginForm = new JPanel();
                GroupLayout form = new GroupLayout(loginForm);
                loginForm.setLayout(form);
                form.setAutoCreateGaps(true);
                form.setAutoCreateContainerGaps(true);
                form.setHorizontalGroup(form.createSequentialGroup()
                                .addGroup(form.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(usernameLabel).addComponent(passwordLabel))
                                .addGroup(form.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(usernameField).addComponent(passwordField)));
                form.setVerticalGroup(form.createSequentialGroup()
                                .addGroup(form.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(usernameLabel).addComponent(usernameField))
                                .addGroup(form.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(passwordLabel).addComponent(passwordField)));
                loginForm.setAlignmentX(Component.CENTER_ALIGNMENT);
                loginForm.setMaximumSize(new Dimension(300, 70));
                add(loginForm);

                confirmButton = new JButton("<html><b>Log In</b>");
                confirmButton.setActionCommand("Log in");
                confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                confirmButton.setMaximumSize(new Dimension(75, 70));
                confirmButton.addActionListener(parent);
                confirmButton.setFocusable(false);
                add(confirmButton);

                add(Box.createVerticalGlue());

        }

}
