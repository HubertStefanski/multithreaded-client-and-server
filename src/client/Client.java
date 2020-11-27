package client;

import UI.AOCMenu;
import UI.LoginMenu;
import UI.MainView;

import javax.swing.*;
import javax.swing.border.Border;
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

public class Client {


    private DataOutputStream toServer;
    private DataInputStream fromServer;
    private final MainView mainView = new MainView();
    private final LoginMenu loginMenu = new LoginMenu();
    private final AOCMenu aocMenu = new AOCMenu();
    private final JPanel loginComp = loginMenu.getLoginUIComponent();

    public static void main(String[] args) throws UnknownHostException {
        new Client();
    }

    String clientUrl = InetAddress.getLocalHost().toString();

    public Client() throws UnknownHostException {
        JFrame frame = new JFrame("Area Of Circle");

        //Create new login menu for the user, assign to the left of contentPane
        mainView.setNestPanel(loginComp);
        //Create new logging area for the mainview, assign to the right
        //Set main content pane
        frame.setContentPane(mainView.getMainUI());
        frame.setSize(750, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Display the window to the user
        frame.setVisible(true);
        //Add listeners for buttons
        loginMenu.loginButton.addActionListener(new LoginListener());


        try {


            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);


            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
            while (true) {
                //Read data for the server in an infinite loop
                String read = fromServer.readUTF();
                mainView.logArea.append(read);
                //Check the response for authorised message
                if (read.contains("Welcome")) {
                    //remove the login menu from the left side and add area of circle controls
                    frame.setVisible(false);
                    frame.remove(loginComp);
                    frame.add(aocMenu.getAOCComponent());
                    aocMenu.getSendButton().addActionListener(new AocListener());
                    aocMenu.getExitButton().addActionListener(new exitListener());
                    frame.setVisible(true);
                }
            }

        } catch (IOException ex) {
            // Log some errors
            mainView.logArea.append(ex.toString() + '\n');
        }
    }

    //Convenient string for logging with client variables
    private String clientOutputString(String input, String url) {
        return ("Client@ " + url + " " + new Date() + ": " + input + "\n");
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Verify the data, ensure not null and not empty
            if (loginMenu.loginField.getText() != null || loginMenu.loginField.getText().length() > 0) {
                //Tell the user what's going on
                mainView.logArea.append(clientOutputString("processing login request for " + loginMenu.loginField.getText(), clientUrl));
                try {
                    //Tell the server what data to expect
                    toServer.writeUTF("LOGIN");
                    //Send the student ID
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
            //Verify the data, ensure not null and not empty
            if (aocMenu.radiusField.getText() != null && aocMenu.radiusField.getText().length() > 0) {
                try {
                    //Tell the server what data to expect
                    toServer.writeUTF("RADIUS");
                    // Get the radius from the text field
                    // Send the radius to the server
                    toServer.writeUTF(String.format("%s", Double.parseDouble(aocMenu.radiusField.getText().trim())));
                    // Display the choice back to the user
                    mainView.logArea.append(clientOutputString("Radius is " + Double.parseDouble(aocMenu.radiusField.getText().trim()) + "\n", clientUrl));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                //log this message if data is not valid
                mainView.logArea.append(clientOutputString("cannot process a non-numeric value", clientUrl));
            }
        }
    }

    private class exitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //Tell the server what to do
                toServer.writeUTF("EXIT");
                while (true) {
                    //Read any message from the server regarding exit info
                    mainView.logArea.append(fromServer.readUTF());
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            //Exit the client
            System.exit(0);
        }
    }
}
