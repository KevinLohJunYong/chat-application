import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 5050;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static Set<String> userNames = Collections.synchronizedSet(new HashSet<>());
    private static Map<String,ClientHandler> clientNameToClientHandlerMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port: " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket);
                clients.add(clientThread);
                clientNameToClientHandlerMap.put(clientThread.clientName,clientThread);

                clientThread.start();
            }
        } finally {
            serverSocket.close();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while (true) {
                this.clientName = in.readLine();
                synchronized (userNames) {
                    if (!userNames.contains(clientName)) {
                        out.println("Welcome " + this.clientName + "!");
                        userNames.add(clientName);
                        break;
                    } else {
                        out.println("Username already taken, please choose another name:");
                    }
                }
            }
        
            broadcastMessage(clientName + " has joined");
        }
        

        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    if(input.toLowerCase().startsWith("to")) {
                        int idx = input.indexOf(":");
                        String recipient = input.substring("to".length(),idx).trim();
                        String msg = input.substring(idx+1).trim();
                        privateMessage(msg,clientName, recipient);
                    }
                    else {
                      broadcastMessage(clientName + ": " + input);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    userNames.remove(clientName);
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }

        private void privateMessage(String msg, String sender,String receipient) {
            ClientHandler receipientClientHandler = clientNameToClientHandlerMap.get(receipient);
            if(receipientClientHandler == null) {
                ClientHandler senderClientHandler = clientNameToClientHandlerMap.get(sender);
                senderClientHandler.out.println("The receipient you are trying to reach is not found");
            }
            else {
                receipientClientHandler.out.println(sender+": "+msg);
            }
        }
        @Override
        public String toString() {
            return "Client Handler with clientName: " + clientName;
        }
    }
}
