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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;


public class Client {



    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private final MainView mainView = new MainView();
    private final LoginMenu loginMenu = new LoginMenu();
    private final AOCMenu aocMenu = new AOCMenu();


    public static void main(String[] args) throws UnknownHostException {
        new Client();
    }

    String clientUrl = InetAddress.getLocalHost().toString();
    public Client() throws UnknownHostException {
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
            while (true) {

                String read = fromServer.readUTF();
                mainView.logArea.append(read);
                if (read.contains("Welcome")) {
                    frame.remove(loginMenu.rootPanel);
                    frame.add(aocMenu.rootPanel);
                }
            }

        } catch (IOException ex) {
            mainView.logArea.append(ex.toString() + '\n');
        }
    }

    private String clientOutputString(String input, String url) {
        return ("Client@ " + url + " " + new Date() + ": " + input + "\n");
    }
    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loginMenu.loginField.getText() != null || loginMenu.loginField.getText().length() > 0) {
                mainView.logArea.append(clientOutputString("processing login request for " + loginMenu.loginField.getText(),clientUrl));
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
                    toServer.writeUTF(String.format("%s", Double.parseDouble(aocMenu.radiusField.getText().trim())));
                    // Display to the text area
                    mainView.logArea.append(clientOutputString("Radius is " + Double.parseDouble(aocMenu.radiusField.getText().trim()) + "\n",clientUrl));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                mainView.logArea.append(clientOutputString("cannot process a non-numeric value",clientUrl));
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
