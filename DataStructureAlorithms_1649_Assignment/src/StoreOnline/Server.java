package StoreOnline;

import model.Order;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1111;
    private static Queue<Order> orderQueue = new ConcurrentLinkedQueue<Order>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(clientSocket.getInputStream());

            Thread receiveThread = new Thread(new ReceiveOrder(clientSocket, inStream));
            Thread processThread = new Thread(new ProcessOrder(clientWriter));

            receiveThread.start();
            processThread.start();

            receiveThread.join();
            processThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class ReceiveOrder implements Runnable {
        private Socket clientSocket;
        private ObjectInputStream inStream;

        public ReceiveOrder(Socket socket, ObjectInputStream inStream) {
            this.clientSocket = socket;
            this.inStream = inStream;
        }

        @Override
        public void run() {
            try {
                while (!clientSocket.isClosed()) {
                    try {
                        Order inputLine = (Order) inStream.readObject();
                        if (inputLine == null) {
                            break;
                        }

                        System.out.println("Received order: " + inputLine);
                        orderQueue.offer(inputLine);
                    } catch (EOFException e) {
                        System.out.println("Client disconnected.");
                        break;
                    }
                }

                inStream.close();
                clientSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ProcessOrder implements Runnable {
        private ObjectOutputStream clientWriter;

        public ProcessOrder(ObjectOutputStream clientWriter) {
            this.clientWriter = clientWriter;
        }

        @Override
        public void run() {
            while (true) {
                Order order = orderQueue.poll();
                if (order != null) {
                    System.out.println("Processing order: " + order);
                    try {
                        sendProcessedOrder(order);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private void sendProcessedOrder(Order order) throws IOException {
            synchronized (clientWriter) {
                order.status = "Delivered";
                clientWriter.writeObject(order);
            }
        }
    }
}
