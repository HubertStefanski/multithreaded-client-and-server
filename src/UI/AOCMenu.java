package UI;

import javax.swing.*;
import java.awt.*;

public class AOCMenu {


    public JPanel rootPanel;
    public JTextField radiusField;
    public JButton sendButton;
    public JButton exitButton;
    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setRootPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
    }

    public JTextField getRadiusField() {
        return radiusField;
    }

    public void setRadiusField(JTextField radiusField) {
        this.radiusField = radiusField;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    public JPanel getAOCComponent() {
        setExitButton(new JButton("Exit"));
        setSendButton(new JButton("Send"));
        setRadiusField(new JTextField());

        setRootPanel(new JPanel(new GridLayout(20,60)));
        getRootPanel().add(new JLabel("Enter radius"));

        getRootPanel().add(getRadiusField());
        getRootPanel().add(getExitButton());
        getRootPanel().add(getSendButton());



        return getRootPanel();
    }



}
