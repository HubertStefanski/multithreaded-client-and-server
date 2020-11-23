package server;


import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

class ClientHandler extends Thread {
    private final DataInputStream inputFromClient;
    private final DataOutputStream outputToClient;
    private final Socket s;
    private final String url;
    private final JTextArea serverTextArea;


    // Constructor
    public ClientHandler(Socket s, DataInputStream inputFromClient, DataOutputStream outputToClient, String url, JTextArea serverTextArea) {
        this.s = s;
        this.inputFromClient = inputFromClient;
        this.outputToClient = outputToClient;
        this.url = url;
        this.serverTextArea = serverTextArea;
    }

    private String handlerOutputString(String input) {
        return ("Handler@ " + url + ":" + input + "\n");
    }

    @Override
    public void run() {


        while (true) {
            try {
                String received = inputFromClient.readUTF();
                if (received.equals("LOGIN")) {
                    received = inputFromClient.readUTF();
                    if (received.equals("") || received.length() <= 0) {
                        outputToClient.writeUTF(handlerOutputString("student ID cannot be empty"));
                        serverTextArea.append(handlerOutputString("client attempted login with empty text area"));

                    }
                    if (ServerHelper.authenticate(received)) {
                        Map map = ServerHelper.getUser(received);
                        outputToClient.writeUTF(handlerOutputString("Welcome " + map.get("FNAME") + " " + map.get("SNAME")));
                        serverTextArea.append(handlerOutputString("authenticated student : " + map.get("FNAME") + " " + map.get("SNAME") + " ID : " + received  ));
                    } else {
                        outputToClient.writeUTF(handlerOutputString("cannot authenticate student"));
                        serverTextArea.append(handlerOutputString("failed to authenticate student " + received));
                    }
                    continue;
                }
                if (received.equals("EXIT")) {
                    outputToClient.writeUTF(handlerOutputString("Client " + this.s + " sends exit..."));
                    outputToClient.writeUTF(handlerOutputString("Closing this connection."));
                    serverTextArea.append(handlerOutputString("Closing this connection."));
                    this.s.close();
                    break;
                }
                if (received.equals("RADIUS")) {
                    received = inputFromClient.readUTF();
                    serverTextArea.append(handlerOutputString("server got radius : " + received));

                    Double radius = Double.parseDouble(received);
                    // Compute area
                    double area = radius * radius * Math.PI;;
                    outputToClient.writeUTF(handlerOutputString("Area received from the server is :" + area + '\n'));
                    serverTextArea.append(handlerOutputString("returning area of circle to client :" + area));
                }


            } catch (IOException e) {
                try {
                    this.inputFromClient.close();
                    this.outputToClient.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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
