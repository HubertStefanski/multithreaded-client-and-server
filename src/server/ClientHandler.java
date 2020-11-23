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

    //Convenient string for returning information to the user with all variables
    private String handlerOutputString(String input) {
        return ("Handler@ " + url + ":" + input + "\n");
    }

    @Override
    public void run() {


        while (true) {
            try {
                //Continously read the data from the stream
                String received = inputFromClient.readUTF();
                //Check the received data
                if (received.equals("LOGIN")) {
                    //Read the data again to get the doouble
                    received = inputFromClient.readUTF();
                    //Verify the data once more, in case UI changes
                    if (received.equals("") || received.length() <= 0) {
                        //Log appropriate messages for sitautions where data isnt valid
                        outputToClient.writeUTF(handlerOutputString("student ID cannot be empty"));
                        serverTextArea.append(handlerOutputString("client attempted login with empty text area"));

                    }
                    //Authenticate the user using the helper method, if true continue
                    if (ServerHelper.authenticate(received)) {
                        //Get the user info from a separate function
                        Map map = ServerHelper.getUser(received);
                        // Log information to the server and user, provide a nice welcome message
                        outputToClient.writeUTF(handlerOutputString("Welcome " + map.get("FNAME") + " " + map.get("SNAME")));
                        serverTextArea.append(handlerOutputString("authenticated student : " + map.get("FNAME") + " " + map.get("SNAME") + " ID : " + received  ));
                    } else {
                        //Log information where authentication is not successful
                        outputToClient.writeUTF(handlerOutputString("cannot authenticate student"));
                        serverTextArea.append(handlerOutputString("failed to authenticate student " + received));
                    }
                    continue;
                }
                //Check if the User sent an EXIT request
                if (received.equals("EXIT")) {
                    //Close off all connections and let the user know that connections are being closed
                    outputToClient.writeUTF(handlerOutputString("Client " + this.s + " sends exit..."));
                    outputToClient.writeUTF(handlerOutputString("Closing this connection."));
                    serverTextArea.append(handlerOutputString("Closing this connection."));
                    this.s.close();
                    //break out of the for loop for this clientHandler
                    break;
                }
                //Check what type of data to expect
                if (received.equals("RADIUS")) {
                    //Read the new data from the client
                    received = inputFromClient.readUTF();
                    //Tell the server what radius was received
                    serverTextArea.append(handlerOutputString("server got radius : " + received));

                    //Parse the data from the user
                    Double radius = Double.parseDouble(received);
                    // Compute area
                    double area = radius * radius * Math.PI;

                    //Return the area of the circle back to the user and the server
                    outputToClient.writeUTF(handlerOutputString("Area received from the server is :" + area + '\n'));
                    serverTextArea.append(handlerOutputString("returning area of circle to client :" + area));
                }


            } catch (IOException e) {
                try {
                    //In case the connection closes unexpectedly close off all datastreams to prevent unnecessary resource consumption
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
