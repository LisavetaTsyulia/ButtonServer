package server;

import java.io.*;
import java.net.Socket;

public class OneClientThread extends Thread {
    private Socket clientSocket;
    private OutputStream os;
    private BufferedReader br;
    private Server server;
    private boolean isAlive;

    public OneClientThread (Socket socket, Server server){
        this.server = server;
        clientSocket = socket;
        isAlive = true;
        try {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendNewNumber(os);
    }

    @Override
    public void run() {
        super.run();
        while (isAlive) {
            try {
                String line = br.readLine();
                if (line != null) {
                    switch (line) {
                        case "+" :
                            server.changeTheNumber();
                            sendNewNumber(os);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                isAlive = false;
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNewNumber(OutputStream os) {
        try (PrintWriter writer = new PrintWriter(os)) {
            writer.write(String.valueOf(server.getTheNumber()));
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
