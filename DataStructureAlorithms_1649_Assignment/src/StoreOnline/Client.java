package StoreOnline;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import implementations.*;



public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1111;
    private static Scanner scn = new Scanner(System.in);

    private static Queue<Order> orderQueue = new Queue<Order>();

    private static Stack<Order> orderStack = new Stack<Order>();

    public static void main(String[] args) {
        mainMenu();
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


    public static void sendOrdertoServer(){
        long totalTime = 0;
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
//            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int currentSize = orderQueue.size();

            System.out.println("Send order to server ");

            while (!orderQueue.isEmpty()) {

                outStream.writeObject(orderQueue.poll());
                System.out.print(".");

                orderStack.push((Order) inStream.readObject());
            }
            System.out.println();




        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("All orders delivered successfully: " + totalTime + "ms");
        }

    }


    public static void readOrderListFromFile() {

        String filename = "D:\\GitHubClone\\1649_DataStructure-Alorithms_Assignment\\DataStructureAlorithms_1649_Assignment\\src\\data\\order_list.txt";

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    String status = parts[2];
                    orderQueue.offer(new Order(name, price, status));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("All orders read from file order_list.txt have been added to Queue!");
        }
    }

    public static void exportOrderFromStack(){
        String filename = "D:\\GitHubClone\\1649_DataStructure-Alorithms_Assignment\\DataStructureAlorithms_1649_Assignment\\src\\data\\order_unbox.txt";

        try {
            FileWriter writer = new FileWriter(filename);

            while (!orderStack.isEmpty()) {
                Order item = orderStack.pop();
                item.status = "Opened";
                writer.write(item.name + "|" + item.price + "|" + item.status+"\n");
            }

            writer.close();
            System.out.println("All orders from stack have been exported to order_unbox.txt!");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void showOrderHasDelivered(){
        System.out.println(orderStack);
    }

    public static void addOrdertoQueue(){
        System.out.println("Enter your order");
        System.out.print("Name: ");
        String name = scn.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scn.nextLine());
        orderQueue.offer(new Order(name,price,"Not Yet"));
        System.out.println("Add to Queue successful");
    }



    public static void mainMenu(){
        long startTime;
        long endTime;
        long totalTime;
        int choice = 0;
        while (choice != 7){
            showMenu();
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scn.nextLine());
            System.out.println();
            switch (choice){
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
                    startTime = System.currentTimeMillis();
                    quickSort(orderStack);
                    System.out.println("Sort Successful");
                    endTime = System.currentTimeMillis();
                    totalTime = endTime - startTime;
                    System.out.println("Time Complexity: " + totalTime + "ms");
                    break;
                case 6:
                    startTime = System.currentTimeMillis();
                    exportOrderFromStack();
                    endTime = System.currentTimeMillis();
                    totalTime = endTime - startTime;
                    System.out.println("Time Complexity: " + totalTime + "ms");
                    break;
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


