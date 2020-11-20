package client;

import UI.LoginMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import UI.MainView;


public class Client {

    private DataOutputStream toServer;
    private DataInputStream fromServer;

    public static void main(String[] args) {
        new Client();
    }
    JFrame frame = new JFrame("Area Of Circle");
    MainView mainView = new MainView();
    LoginMenu loginMenu = new LoginMenu();



    public Client() {
        mainView.rootPanel.add(loginMenu.rootPanel,BorderLayout.WEST);
        mainView.rootPanel.add(mainView.logArea,BorderLayout.EAST);
        frame.setContentPane(mainView.rootPanel);
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("130.254.204.36", 8000);
            // Socket socket = new Socket("drake.Armstrong.edu", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            mainView.logArea.append(ex.toString() + '\n');
        }
    }

    private class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Get the radius from the text field
//                double radius = Double.parseDouble(.getText().trim());

                // Send the radius to the server
                toServer.writeDouble(10); //TODO refactor this back to radius
                toServer.flush();

                // Get area from the server
                double area = fromServer.readDouble();

                // Display to the text area
                mainView.logArea.append("Radius is " + 10 + "\n");
                mainView.logArea.append("Area received from the server is "
                        + area + '\n');
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
}