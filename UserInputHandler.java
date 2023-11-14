import java.util.ArrayList;
import java.util.Map;

public class UserInputHandler {
    public static void handleUserInput(String input,String clientName,Map<String,ClientHandler> clientNameToClientHandlerMap, 
    ArrayList<ClientHandler> clients) {
        if(isPrivateMsg(input)) {
            String recipient = getRecipientFromPrivateMsg(input);
            String msg = getMsg(input);
            MessageService.privateMessage(msg,clientName, recipient,clientNameToClientHandlerMap);
        }
        else {
          MessageService.broadcastMessage(clientName + ": " + input,clients);
        }
    }
    public static boolean isPrivateMsg(String msg) {
        return msg.toLowerCase().startsWith("to");
    }
    public static String getRecipientFromPrivateMsg(String input) {
        int idx = input.indexOf(":");
        return input.substring("to".length(),idx).trim();
    }
    public static String getMsg(String input) {
        int idx = input.indexOf(":");
        return input.substring(idx+1).trim();
    }
}
