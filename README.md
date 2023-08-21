# 1649_DataStructure-Alorithms_Assignment
**Read Order from File:**
Users can choose this option to read orders from a file.
The system reads order details (name, price, status) from a specified file and adds them to the order queue.


**Add Order to Queue:**
This option enables users to manually input an order's details, including the product name and price.
The newly created order is added to the order queue.
The user receives a confirmation message indicating the successful addition to the queue.


**Send Order to Server:**
Upon selecting this option, the system establishes a connection to the server.
Orders in the queue are sent to the server for processing.
The processed orders received from the server are pushed onto the order stack.
The user receives feedback about the successful delivery of all orders.


**Show Delivered Orders:**
Choosing this option allows users to view orders that have been successfully processed and marked as "Delivered."
The orders are displayed, revealing their details including name, price, and status.
The time complexity of displaying the delivered orders is presented to the user upon completion.


**Sort Orders by Price:**
This option triggers the sorting of orders in the order stack based on their price values.
The quicksort algorithm is employed to achieve the sorting process.
A confirmation message confirms the successful sorting of the orders.


**Unbox and Export all Orders:**
Users can select this option to export orders from the order stack.
Exported orders are written to a designated file with their status marked as "Opened."
