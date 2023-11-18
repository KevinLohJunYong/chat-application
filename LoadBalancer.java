import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LoadBalancer {
    private static final int LB_PORT = 5000;
    private static final int START_PORT = 5050; 
    private static final int NUM_SERVER_INSTANCES = 3; 
    private static int currentPort = START_PORT;

    public static void main(String[] args) throws IOException {
        ServerSocket loadBalancerSocket = new ServerSocket(LB_PORT);
        System.out.println("LoadBalancer started on port: " + LB_PORT);

        try {
            while (true) {
                Socket clientSocket = loadBalancerSocket.accept();
                System.out.println("new client joined");
                int serverPort = chooseServerPort();
                System.out.println("Client is assigned to port: " + serverPort);
                redirectRequest(clientSocket, serverPort);
            }
        }
        catch(Exception e) {
            System.out.println("exception: " + e.getMessage());
        }
        finally {
            System.out.println("Load balancer has closed");
            loadBalancerSocket.close();
        }
    }

    private static int chooseServerPort() {
        int port = currentPort;
        currentPort++;
        // round robin load balancing strategy
        if (currentPort >= START_PORT + NUM_SERVER_INSTANCES) { 
            currentPort = START_PORT;
        }
        return port;
    }

    private static void redirectRequest(Socket clientSocket, int serverPort) {
        try {
            System.out.println("Redirected to server port: " + serverPort);
            Socket serverSocket = new Socket("localhost", serverPort);
            new Thread(new SocketPipe(clientSocket.getInputStream(), serverSocket.getOutputStream())).start();
            new Thread(new SocketPipe(serverSocket.getInputStream(), clientSocket.getOutputStream())).start();
        } catch (IOException e) {
            System.out.println("Error redirecting request to server: " + e.getMessage());
        }
    }

    private static class SocketPipe implements Runnable {
        private InputStream input;
        private OutputStream output;

        public SocketPipe(InputStream input, OutputStream output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int length;
            try {
                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }
            } catch (IOException e) {
            } finally {
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                }
            }
        }
}
}
