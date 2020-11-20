package server;

import UI.LoginMenu;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    // Text area for displaying contents

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
                // Receive radius from the client
                double radius = inputFromClient.readDouble();

                // Compute area
                double area = radius * radius * Math.PI;

                // Send area back to the client
                outputToClient.writeDouble(area);

                outputToClient.writeUTF("Radius received from client: " + radius + '\n');
                outputToClient.writeUTF("Area found: " + area + '\n');
            }
        }
        catch(IOException ex) {
            System.err.println(ex);
        }
    }
}