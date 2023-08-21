package StoreOnline;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import implementations.*;
import model.Order;


public class Client {
    private static Scanner scn = new Scanner(System.in);

    private static Queue<Order> orderQueue = new Queue<Order>();

    private static Stack<Order> orderStack = new Stack<Order>();

    private static Socket socket;
    private static ObjectOutputStream outStream;
    private static ObjectInputStream inStream;

    public static void main(String[] args) {
        Initialization();
        mainMenu();
    }

    public static void Initialization(){
        try {
            socket = new Socket("localhost", 1111);
            outStream =  new ObjectOutputStream(socket.getOutputStream());
            inStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readOrderListFromFile() {

        String filename = "D:\\GitHubClone\\1649_DataStructure-Alorithms_Assignment\\DataStructureAlorithms_1649_Assignment\\src\\data\\order_list.txt";

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    String status = "Not Yet";
                    orderQueue.offer(new Order(name, price, status));
                } else throw new Exception("Invalid Format");
            }
            if (orderQueue.isEmpty()) throw new Exception("Queue is Empty"); else {
                System.out.println("All orders read from file order_list.txt have been added to Queue!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addOrdertoQueue(){
        try {
            System.out.println("Enter your order");
            String name;
            do {
                System.out.print("Name: ");
                name = scn.nextLine();
            } while (name == null || name.isEmpty());
            System.out.print("Price: ");
            double price = Double.parseDouble(scn.nextLine());
            orderQueue.offer(new Order(name, price, "Not Yet"));
            System.out.println("Add to Queue successful");
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format.");
        }
    }

    public static void sendOrdertoServer(){
        try {
//            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
//            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

            if (orderQueue.isEmpty()) throw new Exception("Queue is Empty"); else {
                System.out.println("Send order to server ");
                while (!orderQueue.isEmpty()) {

                    outStream.writeObject(orderQueue.poll());
                    outStream.flush();
                    System.out.print(".");

                    orderStack.push((Order) inStream.readObject());
                }
                outStream.writeObject(null);
                outStream.flush();
                System.out.println();
                System.out.println("All orders delivered successfully: ");
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }


    public static void exportOrderFromStack(){
        String filename = "D:\\GitHubClone\\1649_DataStructure-Alorithms_Assignment\\DataStructureAlorithms_1649_Assignment\\src\\data\\order_unbox.txt";

        try {
            if (orderStack.isEmpty()) throw new Exception("Stack is Empty");
            else {
                FileWriter writer = new FileWriter(filename);

                while (!orderStack.isEmpty()) {
                    Order item = orderStack.pop();
                    item.status = "Opened";
                    writer.write(item.name + "|" + item.price + "|" + item.status+"\n");
                }

                writer.close();
                System.out.println("All orders from stack have been exported to order_unbox.txt!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void showOrderHasDelivered(){
        try{
            if (orderStack.isEmpty()) throw new Exception("Stack is Empty");
            else System.out.println(orderStack);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void quickSort(Stack<Order> stack) {
        if (stack.isEmpty()) {
            return;
        }

        Stack<Order> left = new Stack<>();
        Stack<Order> right = new Stack<>();
        Stack<Order> tempStack = new Stack<>();

        Order pivot = stack.pop();

        while (!stack.isEmpty()) {
            Order order = stack.pop();
            if (order.price < pivot.price) {
                left.push(order);
            } else {
                right.push(order);
            }
        }

        quickSort(left);
        quickSort(right);

        while (!right.isEmpty()) {
            tempStack.push(right.pop());
        }
        tempStack.push(pivot);
        while (!left.isEmpty()) {
            tempStack.push(left.pop());
        }

        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }

    }

    public static void mainMenu(){
        long startTime;
        long endTime;
        long totalTime;
        int choice = 0;
        while (choice != 7) {
            showMenu();
            try {
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(scn.nextLine());
                System.out.println();

                switch (choice) {
                    case 1:
                        startTime = System.currentTimeMillis();
                        readOrderListFromFile();
                        endTime = System.currentTimeMillis();
                        totalTime = endTime - startTime;
                        System.out.println("Time Complexity: " + totalTime + "ms");
                        break;
                    case 2:
                        startTime = System.currentTimeMillis();
                        addOrdertoQueue();
                        endTime = System.currentTimeMillis();
                        totalTime = endTime - startTime;
                        System.out.println("Time Complexity: " + totalTime + "ms");
                        break;
                    case 3:
                        startTime = System.currentTimeMillis();
                        sendOrdertoServer();
                        endTime = System.currentTimeMillis();
                        totalTime = endTime - startTime;
                        System.out.println("Time Complexity: " + totalTime + "ms");
                        break;
                    case 4:
                        startTime = System.currentTimeMillis();
                        showOrderHasDelivered();
                        endTime = System.currentTimeMillis();
                        totalTime = endTime - startTime;
                        System.out.println("Time Complexity: " + totalTime + "ms");
                        break;
                    case 5:
                        if (orderStack.isEmpty()) System.out.println("Stack is Empty");
                         else {
                            startTime = System.currentTimeMillis();
                            quickSort(orderStack);
                            System.out.println("Sort Successful");
                            endTime = System.currentTimeMillis();
                            totalTime = endTime - startTime;
                            System.out.println("Time Complexity: " + totalTime + "ms");
                            break;
                        }
                    case 6:
                        startTime = System.currentTimeMillis();
                        exportOrderFromStack();
                        endTime = System.currentTimeMillis();
                        totalTime = endTime - startTime;
                        System.out.println("Time Complexity: " + totalTime + "ms");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
            }
        }
    }

    public static void showMenu(){
        System.out.println("_______________________________________");
        System.out.println("1. Read Order form File");
        System.out.println("2. Add Order to Queue");
        System.out.println("3. Send Order to Server");
        System.out.println("4. Show delivered orders");
        System.out.println("5. Sort Orders by price");
        System.out.println("6. Unbox and Export all Orders");
        System.out.println("7. Quit");
        System.out.println("_______________________________________");
    }

}


