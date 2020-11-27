package UI;

import javax.swing.*;
import java.awt.*;

public class LoginMenu {
    public JTextField loginField;
    public JButton loginButton;
    public JPanel rootPanel;


    public JTextField getLoginField() {
        return loginField;
    }

    public void setLoginField(JTextField loginField) {
        this.loginField = loginField;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setRootPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(JButton loginButton) {
        this.loginButton = loginButton;
    }

    public JPanel getLoginUIComponent() {
        setLoginButton(new JButton("Login"));
        setLoginField(new JTextField());

        setRootPanel(new JPanel(new GridLayout(20, 60)));
        getRootPanel().add(new JLabel("Enter student ID"));

        getRootPanel().add(getLoginField());
        getLoginField().setHorizontalAlignment(JTextField.CENTER);
        getRootPanel().add(getLoginButton());
        return getRootPanel();
    }
}
