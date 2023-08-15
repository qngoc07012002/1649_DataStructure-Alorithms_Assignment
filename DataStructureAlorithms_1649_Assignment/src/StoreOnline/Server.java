package StoreOnline;

import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 2222;
    private static Queue<String> orderQueue = new ConcurrentLinkedQueue<>();
    private static Stack<String> orderHistory = new Stack<>();
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
                mainMenu();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received order: " + inputLine);
                    orderQueue.add(inputLine);

                    out.println("Order received");
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class OrderProcessing implements Runnable {
        private String order;

        public OrderProcessing(String order) {
            this.order = order;
        }

        @Override
        public void run() {
            orderHistory.push(order);
            System.out.println("Processing order: " + order);

            // Đợi một chút trước khi kiểm tra lại
            try {
                Thread.sleep(1000); // Đợi 1 giây
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Phần MainMenu và các phần liên quan
    public static void showMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Process Orders");
        System.out.println("2. Print Order History");
        System.out.println("3. Disconnect");
        System.out.print("Enter your choice: ");
    }

    public static void mainMenu() {
        boolean exit = false;
        while (!exit) {
            showMenu();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        processOrders();
                        break;
                    case 2:
                        printOrderHistory();
                        break;
                    case 3:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processOrders() {
        while (!orderQueue.isEmpty()) {
            String order = orderQueue.poll();
            executorService.submit(new OrderProcessing(order));
        }
    }


    private static void printOrderHistory() {
        System.out.println("Order History:");
        for (String order : orderHistory) {
            System.out.println(order);
        }
    }

    // Phần còn lại của mã bạn đã đưa ra
}
