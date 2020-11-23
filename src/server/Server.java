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


        while (true) {
            Socket s = null;
            try {
                //Accept connection
                s = serverSocket.accept();
                //Get the url for the server
                String url = String.format("%s:%s", s.getLocalAddress(), s.getLocalPort());
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(s.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(s.getOutputStream());

                //Log to the user about new connection start
                outputToClient.writeUTF(serverOutputString("Server connection started",url));
                serverView.logArea.append(serverOutputString("Server connection started",url));

                //Create a new clientHandler thread for new client
                Thread t = new ClientHandler(s, inputFromClient, outputToClient,url,serverView.logArea);
                //Log to the server about new client handler thread being started
                serverView.logArea.append(serverOutputString("New handler started for client",url));

                //Start the thread
                t.start();

            } catch (IOException e) {
                try {
                    //If an exception is found close the socket
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