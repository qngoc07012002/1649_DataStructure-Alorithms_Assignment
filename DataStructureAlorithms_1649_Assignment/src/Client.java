import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class Client {
    public static void main(String[] args) {
        Thread[] clientThreads = new Thread[5];

        for (int i = 0; i < 5; i++) {
            final int clientId = i + 1;
            clientThreads[i] = new Thread(() -> sendTasks(clientId));
            clientThreads[i].start();
        }

        for (Thread thread : clientThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendTasks(int clientId) {
        try (Socket socket = new Socket("localhost", 12345)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            int startTask = (clientId - 1) * 10 + 1;
            int endTask = clientId * 10;

            for (int i = startTask; i <= endTask; i++) {
                System.out.println("Client " + clientId + " send Task " + i);
                out.println("Task " + i);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}