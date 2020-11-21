package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;

class ClientHandler extends Thread {
    private final DataInputStream inputFromClient;
    private final DataOutputStream outputToClient;
    private final Socket s;

    private String handlerOutputString(String input) {
        return ("Handler-1 @ " + new Date() + ":" + input + "\n");
    }

    // Constructor
    public ClientHandler(Socket s, DataInputStream inputFromClient, DataOutputStream outputToClient) {
        this.s = s;
        this.inputFromClient = inputFromClient;
        this.outputToClient = outputToClient;
    }

    @Override
    public void run()
    {
        String received;
        String toreturn;
        while (true) {
            try {
                received = inputFromClient.readUTF();
                if (received.equals("") || received.length() <= 0) {
                    outputToClient.writeUTF(handlerOutputString("student ID cannot be empty"));
                }
                if (ServerHelper.authenticate(inputFromClient.readUTF())) {
                    outputToClient.writeUTF(handlerOutputString("student authenticated"));
//                    // Receive radius from the client
//                    double radius = inputFromClient.readDouble();
//
//                    // Compute area
//                    double area = radius * radius * Math.PI;
//
//                    // Send area back to the client
//                    outputToClient.writeDouble(area);
//
//                    outputToClient.writeUTF(handlerOutputString("Radius received from client: " + radius + '\n'));
//                    outputToClient.writeUTF(handlerOutputString("Area found: " + area + '\n'));
                } else {
                    outputToClient.writeUTF(handlerOutputString("cannot authenticate student"));
                }

                if (inputFromClient.readUTF().equals("Exit")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            this.inputFromClient.close();
            this.outputToClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
