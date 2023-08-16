package StoreOnline;

import java.io.*;
import java.net.*;


public class Client {
    private static final String SERVER_IP = "127.0.0.1"; // Địa chỉ IP của Server
    private static final int SERVER_PORT = 1111; // Cổng của Server

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while ((userInput = consoleReader.readLine()) != null) {
                out.println(userInput); // Gửi đơn hàng lên Server
                String response = in.readLine(); // Nhận phản hồi từ Server
                System.out.println("Server response: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


