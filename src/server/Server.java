package server;

import UI.ServerView;
import client.Client;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

    private ServerView serverView = new ServerView();

    private String serverOutputString(String input, String url) {
        return ("Server@ " + url + " " + new Date() + ":" + input + "\n");
    }



    public static void main(String[] args) {

        new Server();
    }

    public Server() {
        JFrame frame = new JFrame("Server");

        frame.setContentPane(serverView.rootPanel);
        frame.setSize(750, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);



        // Create a server socket
        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(8000);
            serverView.logArea.append("Server Starting" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create data input and output streams
        // Listen for a connection request

        while (true) {
            Socket s = null;
            try {
                s = serverSocket.accept();
                String url = String.format("%s:%s", s.getLocalAddress(), s.getLocalPort());
                DataInputStream inputFromClient = new DataInputStream(s.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(s.getOutputStream());

                outputToClient.writeUTF(serverOutputString("Server connection started",url));
                serverView.logArea.append(serverOutputString("Server connection started",url));

                Thread t = new ClientHandler(s, inputFromClient, outputToClient,url,serverView.logArea);
                serverView.logArea.append(serverOutputString("New handler started for client",url));


                t.start();

            } catch (IOException e) {
                try {
                    s.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    System.exit(0);
                }
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}