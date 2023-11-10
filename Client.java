import java.io.*;
import java.net.*;

public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader stdIn;
    private String clientName;

    public Client(String serverAddress) throws IOException {
        Socket socket = new Socket(serverAddress, 5050);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        stdIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your name: ");
        clientName = stdIn.readLine();
        out.println(clientName); 
    }

    public void run() {
        new Thread(() -> {
            try {
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    System.out.println(serverResponse);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();

        try {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost");
        client.run();
    }
}
