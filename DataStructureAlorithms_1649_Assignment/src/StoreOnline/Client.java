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

    private static Queue<String> orderQueue = new Queue<String>();

    private static Stack<String> orderStack = new Stack<String>();

    public static void main(String[] args) {
        mainMenu();
    }

    public static void sendOrdertoServer(){
        long totalTime = 0;
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int currentSize = orderQueue.size();

            System.out.println("Send order to server ");

            long startTime = System.currentTimeMillis();

            while (!orderQueue.isEmpty()) {
                out.println(orderQueue.poll());
                System.out.print(".");
                orderStack.push(in.readLine());
            }
            long endTime = System.currentTimeMillis();

            totalTime = endTime - startTime;
            System.out.println();




        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("All orders delivered successfully: " + totalTime + "ms");
        }

    }


    public static void readOrderListFromFile() {

        String filename = "D:\\GitHubClone\\1649_DataStructure-Alorithms_Assignment\\DataStructureAlorithms_1649_Assignment\\src\\data\\order_list.txt";

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                orderQueue.offer(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            System.out.println("All orders read from file order_list.txt have been added to Queue!");
        }
    }

    public static void exportOrder(){
        String filename = "D:\\GitHubClone\\1649_DataStructure-Alorithms_Assignment\\DataStructureAlorithms_1649_Assignment\\src\\data\\order_unbox.txt";

        try {
            FileWriter writer = new FileWriter(filename);

            while (!orderStack.isEmpty()) {
                String item = orderStack.pop();
            }

            writer.close();
            System.out.println("All orders from stack have been exported to order_unbox.txt!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void showOrderHasDelivered(){
        System.out.println(orderStack);
    }

    public static void addOrdertoQueue(){
        System.out.print("Enter your order: ");
        orderQueue.offer(scn.nextLine());
        System.out.println("Add to Queue successful");
    }

    public static void mainMenu(){
        int choice = 0;
        while (choice != 6){
            showMenu();
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scn.nextLine());
            System.out.println();
            switch (choice){
                case 1:
                    readOrderListFromFile();
                    break;
                case 2:
                    addOrdertoQueue();
                    break;
                case 3:
                    sendOrdertoServer();
                    break;
                case 4:
                    showOrderHasDelivered();
                    break;
                case 5:

                    break;
                case 6:

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
        System.out.println("5. Unbox and Export all Orders");
        System.out.println("6. Quit");
        System.out.println("_______________________________________");
    }

}


