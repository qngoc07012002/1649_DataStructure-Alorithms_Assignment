package model;

import java.io.Serializable;

public class Order implements Serializable {
    public String name;
    public double price;
    public String status;

    public Order(String name, double price, String status) {
        this.name = name;
        this.price = price;
        this.status = status;
    }

    @Override
    public String toString() {
        return name+"|"+price+"|"+status;
    }
}
