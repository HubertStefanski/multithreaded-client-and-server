package server;

import UI.ServerView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

    private ServerView serverView = new ServerView();

    private int count = 0;

    private String serverOutputString(String input, String url, int count) {
        return ("Server- " + count + " @ " + url + " " + new Date() + ":" + input + "\n");
    }


    public static void main(String[] args) {

        new Server();
    }

    public Server() {
        serverView.createNewServerUI();


        // Create a server socket
        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(8000);
            serverView.getLogArea().append("Server Started at socket :  " + serverSocket + " "  + "\n");
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
                outputToClient.writeUTF(serverOutputString("Server connection started", url, 1));

                serverView.logArea.append(serverOutputString("Server connection started", url, 1));

                //Create a new clientHandler thread for new client
                Thread t = new ClientHandler(s, inputFromClient, outputToClient, url, serverView.logArea, count);
                //Log to the server about new client handler thread being started
                count += 1;
                serverView.logArea.append(serverOutputString(String.format("New handler %s started for client",count), url, 1));

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