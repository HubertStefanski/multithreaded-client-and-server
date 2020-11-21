package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

class ClientHandler extends Thread {
    private final DataInputStream inputFromClient;
    private final DataOutputStream outputToClient;
    private final Socket s;

    private String handlerOutputString(String input) {
        return ("Handler-1 @ " + ":" + input + "\n");
    }

    // Constructor
    public ClientHandler(Socket s, DataInputStream inputFromClient, DataOutputStream outputToClient) {
        this.s = s;
        this.inputFromClient = inputFromClient;
        this.outputToClient = outputToClient;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (inputFromClient.readUTF().equals("") || inputFromClient.readUTF().length() <= 0) {
                    outputToClient.writeUTF(handlerOutputString("student ID cannot be empty"));
                }
                String received = inputFromClient.readUTF();
                if (ServerHelper.authenticate(received)) {
                    Map map = ServerHelper.getUser(received);
                    outputToClient.writeUTF(handlerOutputString("Welcome " + map.get("FNAME") + " " + map.get("SNAME")));
                } else {
                    outputToClient.writeUTF(handlerOutputString("cannot authenticate student"));
                }

                if (inputFromClient.readUTF().equals("EXIT")) {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.s.close();
                    System.out.println("Connection closed");
                    break;
                }

            } catch (IOException e) {
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
