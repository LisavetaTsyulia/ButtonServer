package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Server {
    private volatile int theNumber;
    private boolean isAlive;

    public static void main(String[] args) {
        new Server();
    }

    public Server(){
        theNumber = 0;
        isAlive = true;
        setConnection();
    }

    private void setConnection() {
        try (
                ServerSocket serverSocket = new ServerSocket(4444)
            ){
            while (isAlive) {
                try {
                    Socket socket = serverSocket.accept();
                    new OneClientThread(socket, this).start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    isAlive = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void changeTheNumber() {
        theNumber ++;
    }

    public int getTheNumber() {
        return theNumber;
    }
}
