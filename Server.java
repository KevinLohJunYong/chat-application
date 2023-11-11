import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5050;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port: " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket);
                clients.add(clientThread);
                clientThread.start();
            }
        } finally {
            serverSocket.close();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private String clientName;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = in.readLine();
            broadcastMessage(clientName + " has joined");
        }

        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    broadcastMessage(clientName + ": " + input);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void broadcastMessage(String message) {
            System.out.println(message);
        }
    }
}
