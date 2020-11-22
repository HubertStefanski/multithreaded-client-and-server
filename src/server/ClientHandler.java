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
    private final String url;


    // Constructor
    public ClientHandler(Socket s, DataInputStream inputFromClient, DataOutputStream outputToClient,String url) {
        this.s = s;
        this.inputFromClient = inputFromClient;
        this.outputToClient = outputToClient;
        this.url = url;

    }
    private String handlerOutputString(String input) {
        return ("Handler@ " + url + ":" + input + "\n");
    }
    @Override
    public void run() {


        while (true) {
            try {
                String received = inputFromClient.readUTF();
                if (received.equals("") || received.length() <= 0) {
                    outputToClient.writeUTF(handlerOutputString("student ID cannot be empty"));
                }
                if (ServerHelper.authenticate(received)) {
                    Map map = ServerHelper.getUser(received);
                    outputToClient.writeUTF(handlerOutputString("Welcome " + map.get("FNAME") + " " + map.get("SNAME")));
                } else {
                    outputToClient.writeUTF(handlerOutputString("cannot authenticate student"));
                }

                if (inputFromClient.readUTF().equals("EXIT")) {
                    outputToClient.writeUTF(handlerOutputString("Client " + this.s + " sends exit..."));
                    outputToClient.writeUTF(handlerOutputString("Closing this connection."));
                    Thread.sleep(5000);
                    this.s.close();
                    break;
                }

            } catch (IOException | InterruptedException e) {
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
