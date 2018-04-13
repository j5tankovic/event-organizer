package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

public class ShoppingItem implements Serializable {
    private String name;
    private String description;
    private int quantity;
    private double price;
    private boolean status;

    public ShoppingItem() {
    }

    public ShoppingItem(String name, String description, int quantity, double price, boolean status) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
