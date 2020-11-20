package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
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
            Socket socket = serverSocket.accept();

            DataInputStream inputFromClient = new DataInputStream(
                    socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(
                    socket.getOutputStream());
            outputToClient.writeUTF("Server started at " + new Date() + '\n');


            while (true) {
                if (inputFromClient.readUTF() == "" || inputFromClient.readUTF().length() <= 0) {
                    outputToClient.writeUTF(serverOutputString("student ID cannot be empty"));
                }
                if (ServerHelper.authenticate(inputFromClient.readUTF())) {
                    outputToClient.writeUTF(serverOutputString("student authenticated"));
                    // Receive radius from the client
                    double radius = inputFromClient.readDouble();

                    // Compute area
                    double area = radius * radius * Math.PI;

                    // Send area back to the client
                    outputToClient.writeDouble(area);

                    outputToClient.writeUTF(serverOutputString("Radius received from client: " + radius + '\n'));
                    outputToClient.writeUTF(serverOutputString("Area found: " + area + '\n'));
                }
                else{
                    outputToClient.writeUTF(serverOutputString("cannot authenticate student"));
                }
            }
        } catch (IOException | SQLException e) {

            e.printStackTrace();
        }
    }
}