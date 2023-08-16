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
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int currentSize = orderQueue.size();

            System.out.println("Send order to server ");

            while (!orderQueue.isEmpty()){
                out.println(orderQueue.poll());
                System.out.print(".");
                orderStack.push(in.readLine());
            }
            System.out.println();
            if (currentSize == orderStack.size()){
                System.out.println("All orders delivered successfully");
            }

//            for (String order : orderList) {
//                out.println(order); // Gửi đơn hàng lên Server
//                String response = in.readLine(); // Nhận phản hồi từ Server
//                System.out.println("Server response: " + response);
//            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
            System.out.println("Read Order From File Successful!");
        }
    }

    public static void mainMenu(){
        int choice = 0;
        while (choice != 5){
            showMenu();
            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(scn.nextLine());

            switch (choice){
                case 1:
                    readOrderListFromFile();

                    break;
                case 3:
                    sendOrdertoServer();
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
            }
        }
    }

    public static void showMenu(){
        System.out.println("1. Read Order form File");
        System.out.println("2. Add Order to Queue");
        System.out.println("3. Send Order to Server");
        System.out.println("4. Show orders have been delivered");
        System.out.println("5. Unbox the latest delivered order");
        System.out.println("6. Quit");

    }

}


