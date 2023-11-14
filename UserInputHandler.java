import java.util.ArrayList;
import java.util.Map;

public class UserInputHandler {
    public static boolean handleUserInput(String input,String clientName,Map<String,ClientHandler> clientNameToClientHandlerMap, 
    ArrayList<ClientHandler> clients) {
        if(isPrivateMsg(input)) {
            String recipient = getRecipientFromPrivateMsg(input);
            String msg = getMsg(input);
            MessageService.privateMessage(msg,clientName, recipient,clientNameToClientHandlerMap);
            return true;
        }
        else if(!isExitMsg(input)) {
          if(isListUserMsg(input)) {
            MessageService.listAllUsersMessage(clientName, clientNameToClientHandlerMap);
          }
          else {
            MessageService.broadcastMessage(clientName + ": " + input,clients);
          }
          return true;
        }
        else {
            return false;
        }
    }
    public static boolean isListUserMsg(String msg) {
        return msg.toLowerCase().equals("list");
    }
    public static boolean isPrivateMsg(String msg) {
        return msg.toLowerCase().startsWith("to");
    }
    public static boolean isExitMsg(String msg) {
        return msg.toLowerCase().equals("exit");
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
