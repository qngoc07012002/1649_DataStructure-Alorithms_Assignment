package StoreOnline;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1111;
    private static Queue<String> orderQueue = new ConcurrentLinkedQueue<>();
    private static Queue<PrintWriter> clientWriters = new ConcurrentLinkedQueue<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            executorService.submit(new ProcessOrders());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(clientWriter);

                Thread clientThread = new Thread(new ClientHandler(clientSocket, clientWriter));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter clientWriter;

        public ClientHandler(Socket socket, PrintWriter writer) {
            this.clientSocket = socket;
            this.clientWriter = writer;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received order: " + inputLine);
                    orderQueue.add(inputLine);
                }

                in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ProcessOrders implements Runnable {
        @Override
        public void run() {
            while (true) {
                String order = orderQueue.poll();
                if (order != null) {
                    System.out.println("Processing order: " + order);
                    sendProcessedOrder(order);
                }
            }
        }
    }

    private static void sendProcessedOrder(String order) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println("Processed order: " + order);
            }
        }
    }
}
