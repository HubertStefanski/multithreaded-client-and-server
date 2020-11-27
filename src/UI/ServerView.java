package UI;

import javax.swing.*;
import java.awt.*;

public class ServerView {

    public JPanel rootPanel;

    public JTextArea getLogArea() {
        return logArea;
    }

    public void setLogArea(JTextArea logArea) {
        this.logArea = logArea;
    }

    public JTextArea logArea;

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setRootPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
    }

    public void createNewServerUI() {

        JFrame frame = new JFrame("Server");
        setLogArea(new JTextArea());
        setRootPanel(new JPanel(new BorderLayout(20, 10)));
        getRootPanel().add(getLogArea());
        frame.setContentPane(getRootPanel());
        frame.setSize(750, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }


}
