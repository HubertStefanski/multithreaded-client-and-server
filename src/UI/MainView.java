package UI;

import javax.swing.*;
import java.awt.*;

public class MainView {


    public JPanel rootPanel;
    public JTextArea logArea;
    public JPanel nestPanel;


    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setRootPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
    }

    public JTextArea getLogArea() {
        return logArea;
    }

    public void setLogArea(JTextArea logArea) {
        this.logArea = logArea;
    }

    public JPanel getNestPanel() {
        return nestPanel;
    }

    public void setNestPanel(JPanel nestPanel) {
        this.nestPanel = nestPanel;
    }

    public JPanel getMainUI() {
        setRootPanel(new JPanel(new BorderLayout()));
        setLogArea(new JTextArea("------------------------------------ L - O - G - S ------------------------------------------------- \n"));
        getRootPanel().add(getLogArea(), BorderLayout.EAST);
        getRootPanel().add(getNestPanel(), BorderLayout.WEST);

        return getRootPanel();
    }

}