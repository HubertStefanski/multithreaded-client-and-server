package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

    private String serverOutputString(String input) {
        return ("Server-1 @ " + new Date() + ":" + input + "\n");
    }

    public static void main(String[] args) {

        new Server();
    }

    public Server() {

        // Create a server socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create data input and output streams
        // Listen for a connection request

        while (true) {
            Socket s = null;
            try {
                s = serverSocket.accept();

                DataInputStream inputFromClient = new DataInputStream(s.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(s.getOutputStream());

                outputToClient.writeUTF(serverOutputString("Server started at " + new Date() + '\n'));

                Thread t = new ClientHandler(s, inputFromClient, outputToClient);
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