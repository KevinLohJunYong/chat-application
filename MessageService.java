import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MessageService {
    public static void broadcastMessage(String message,ArrayList<ClientHandler> clients) {
        for (ClientHandler client : clients) {
            client.printMessage(message);
        }
    }
    public static void privateMessage(String msg, String sender,String receipient, Map<String,ClientHandler> clientNameToClientHandlerMap) {
        ClientHandler receipientClientHandler = UserManager.getClientHandler(receipient, clientNameToClientHandlerMap);
        if(receipientClientHandler == null) {
            ClientHandler senderClientHandler = UserManager.getClientHandler(sender, clientNameToClientHandlerMap);
            senderClientHandler.printMessage("The receipient you are trying to reach is not found");
        }
        else {
            receipientClientHandler.printMessage(sender+": "+msg);
        }
    }   
    public static void listAllUsersMessage(String requester, Map<String,ClientHandler> clientNameToClientHandlerMap) {
        ClientHandler requesterClientHandler = UserManager.getClientHandler(requester, clientNameToClientHandlerMap);
        Set<String> userNames = clientNameToClientHandlerMap.keySet();
        String responseMsg = "Users currently in the chat : " + userNames.toString();
        requesterClientHandler.printMessage(responseMsg);
    } 
}
