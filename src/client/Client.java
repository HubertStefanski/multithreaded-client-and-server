package client;

import UI.AOCMenu;
import UI.LoginMenu;
import UI.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


public class Client {

    private String clientOutputString(String input) {
        return ("Client@ " + new Date() + ":" + input + "\n");
    }

    private CountDownLatch latch = new CountDownLatch(1);
    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private final MainView mainView = new MainView();
    private final LoginMenu loginMenu = new LoginMenu();
    private final AOCMenu aocMenu = new AOCMenu();


    public static void main(String[] args) {
        new Client();
    }

    public void panelSwap(JFrame frame, JPanel oldPanel, JPanel newPanel) {
        frame.setVisible(false);
        frame.remove(oldPanel);
        frame.add(newPanel);
        frame.setVisible(true);
    }


    public Client() {
        JFrame frame = new JFrame("Area Of Circle");

        mainView.rootPanel.add(loginMenu.rootPanel, BorderLayout.WEST);
        mainView.rootPanel.add(mainView.logArea, BorderLayout.EAST);
        frame.setContentPane(mainView.rootPanel);
        frame.setSize(750, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        loginMenu.loginButton.addActionListener(new LoginListener());
        aocMenu.sendButton.addActionListener(new AocListener());
        aocMenu.exitButton.addActionListener(new exitListener());


        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Socket socket = new Socket("130.254.204.36", 8000);
            // Socket socket = new Socket("drake.Armstrong.edu", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
//            getLogs(fromServer);
            while (true) {

                String read = fromServer.readUTF();
                mainView.logArea.append(read);
                if (read.contains("Welcome")) {
//                    panelSwap(frame, loginMenu.rootPanel, aocMenu.rootPanel);
                    frame.remove(loginMenu.rootPanel);
                    frame.add(aocMenu.rootPanel);
                    mainView.logArea.append(read);
                }
            }

        } catch (IOException ex) {
            mainView.logArea.append(ex.toString() + '\n');
        }
    }


    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loginMenu.loginField.getText() != null || loginMenu.loginField.getText().length() > 0) {
                mainView.logArea.append(clientOutputString("processing login request for " + loginMenu.loginField.getText()));
                try {
                    toServer.writeUTF("LOGIN");
                    toServer.writeUTF(loginMenu.loginField.getText());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }


    private class AocListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (aocMenu.radiusField.getText() != null && aocMenu.radiusField.getText().length() > 0) {
                try {
                    toServer.writeUTF("RADIUS");
                    // Get the radius from the text field
                    // Send the radius to the server
                    toServer.writeUTF(String.format("%s",Double.parseDouble(aocMenu.radiusField.getText().trim())));
                    // Display to the text area
                    mainView.logArea.append(clientOutputString("Radius is " + Double.parseDouble(aocMenu.radiusField.getText().trim()) + "\n"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }else{
                mainView.logArea.append(clientOutputString("cannot process a non-numeric value"));
            }
        }
    }

    private class exitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeUTF("EXIT");
                while (true) {
                    mainView.logArea.append(fromServer.readUTF());
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.exit(0);
        }
    }
}
