import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

class Task {
    private final String content;

    public Task(String content) {
        this.content = content;
    }

    public void process() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return content;
    }
}

class Server {
    private Queue<Task> taskQueue = new LinkedBlockingQueue<>();
    private Stack<Task> completedTasks = new Stack<>();
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String taskContent;
        while ((taskContent = in.readLine()) != null) {
            Task task = new Task(taskContent);
            taskQueue.add(task);
            System.out.println("Received task: " + task);
            executor.submit(new Worker(task));
            try {
                Thread.sleep(100); // Simulate the receive delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Worker implements Runnable {
        private final Task task;

        public Worker(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            System.out.println("Worker is processing task: " + task);
            task.process();
            completedTasks.push(task);
            System.out.println("Completed task: " + task);
        }
    }


    public void awaitCompletion() {
        try {
            executor.shutdown(); // Đóng executor
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Đợi executor kết thúc
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void shutdown() {
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void displayCompletedTasks() {
        System.out.println("Completed tasks:");
        while (!completedTasks.isEmpty()) {
            System.out.println(completedTasks.pop());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server::start).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Before check");

        server.awaitCompletion();

        System.out.println("ACBCBCSDSĐS");
        server.displayCompletedTasks();
    }
}

