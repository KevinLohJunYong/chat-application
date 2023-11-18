import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int START_PORT = 5050;
    private static final int NUM_SERVER_INSTANCES = 3;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static Set<String> userNames = Collections.synchronizedSet(new HashSet<>());
    private static ConcurrentHashMap<String, ClientHandler> clientNameToClientHandlerMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_SERVER_INSTANCES);

        for (int i = 0; i < NUM_SERVER_INSTANCES; i++) {
            int port = START_PORT + i;
            executor.execute(() -> startServerInstance(port));
        }

        executor.shutdown();
    }

    private static void startServerInstance(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket, userNames, clients, clientNameToClientHandlerMap);
                UserManager.registerUser(clientThread.getClientName(), clientThread, userNames, clientNameToClientHandlerMap);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Exception in server on port " + port + ": " + e.getMessage());
        }
    }
}
