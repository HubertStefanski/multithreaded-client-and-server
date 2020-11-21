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

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8000);
            // Create data input and output streams
            // Listen for a connection request

            while (true) {
                Socket s = null;
                try {
                    s = serverSocket.accept();

                    DataInputStream inputFromClient = new DataInputStream(s.getInputStream());
                    DataOutputStream outputToClient = new DataOutputStream(s.getOutputStream());

                    outputToClient.writeUTF("Server started at " + new Date() + '\n');

                    Thread t = new ClientHandler(s, inputFromClient, outputToClient);

                } catch (IOException e) {
                    s.close();
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
    }
    }
}