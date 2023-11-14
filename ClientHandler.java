import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;
        private Set<String> userNames;
        private ArrayList<ClientHandler> clients;
        private Map<String,ClientHandler> clientNameToClientHandlerMap;

        public ClientHandler(Socket socket,Set<String> userNames,
        ArrayList<ClientHandler> clients,
        Map<String,ClientHandler> clientNameToClientHandlerMap
        ) throws IOException {
            this.socket = socket;
            this.userNames = userNames;
            this.clients = clients;
            this.clientNameToClientHandlerMap = clientNameToClientHandlerMap;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            while (true) {
                this.clientName = in.readLine();
                synchronized (userNames) {
                    if (UserManager.isUserNameAvailable(clientName,userNames)) {
                        printMessage("Welcome " + this.clientName + "!");
                        MessageService.broadcastMessage(clientName + " has joined",clients);
                        userNames.add(clientName);
                        break;
                    } else {
                        printMessage("Username already taken, please choose another name:");
                    }
                }
            }
        }
        
        public String getClientName() {
            return this.clientName;
        }
        public void printMessage(String msg) {
            this.out.println(msg);
        }
        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    UserInputHandler.handleUserInput(input, clientName,
                    clientNameToClientHandlerMap,clients);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    UserManager.removeUser(clientName,userNames,clientNameToClientHandlerMap);
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    
        @Override
        public String toString() {
            return "Client Handler with clientName: " + clientName;
        }
    }