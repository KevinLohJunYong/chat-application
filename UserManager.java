import java.util.*;


public class UserManager {

    public static synchronized boolean isUserNameAvailable(String userName,
    Set<String> userNames) {
        return !userNames.contains(userName);
    }

    public static synchronized void registerUser(String userName,
     ClientHandler clientHandler,Set<String> userNames, 
     Map<String,ClientHandler> clientNameToClientHandlerMap) {
        userNames.add(userName);
        clientNameToClientHandlerMap.put(userName, clientHandler);
    }

    public static synchronized void removeUser(String userName, 
    Set<String> userNames, Map<String,ClientHandler> clientNameToClientHandlerMap) {
        userNames.remove(userName);
        clientNameToClientHandlerMap.remove(userName);
    }

    public static ClientHandler getClientHandler(String userName, 
    Map<String,ClientHandler> clientNameToClientHandlerMap) {
        return clientNameToClientHandlerMap.get(userName);
    }
}
