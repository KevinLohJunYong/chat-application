import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 5050;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static Set<String> userNames = Collections.synchronizedSet(new HashSet<>());
    private static Map<String,ClientHandler> clientNameToClientHandlerMap = new ConcurrentHashMap<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port: " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket,userNames,clients,clientNameToClientHandlerMap);
                UserManager.registerUser(clientThread.getClientName(),clientThread,
                userNames,clientNameToClientHandlerMap);
                clients.add(clientThread);
                clientThread.start();
            }
        } finally {
            serverSocket.close();
        }
    }
}
