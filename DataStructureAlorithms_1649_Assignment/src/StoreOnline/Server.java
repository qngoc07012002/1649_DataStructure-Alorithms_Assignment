package StoreOnline;

import model.Order;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 1111;
    private static Queue<Order> orderQueue = new ConcurrentLinkedQueue<Order>();

    private static ServerSocket serverSocket;

    private static ObjectInputStream inStream;
    private static ObjectOutputStream clientWriter;

    public static void main(String[] args) {
        try {
            Initialization();
            Thread receiveThread = new Thread(new ReceiveOrder());
            Thread processThread = new Thread(new ProcessOrder());

            receiveThread.start();
            processThread.start();

            receiveThread.join();
            processThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void Initialization(){
        try {
            System.out.println("Server is listening on port " + PORT);
            serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            clientWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            inStream = new ObjectInputStream(clientSocket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static class ReceiveOrder implements Runnable {
        public ReceiveOrder() {
        }

        @Override
        public void run() {
            try {
                while (true) {

                    try {
                        Order inputLine = (Order) inStream.readObject();
                        if (inputLine != null) {
                            System.out.println("Received order: " + inputLine);
                            orderQueue.offer(inputLine);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static class ProcessOrder implements Runnable {

        public ProcessOrder() {
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
                clientWriter.flush();
            }
        }
    }
}
