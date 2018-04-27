package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

import rs.ac.uns.ftn.pma.event_organizer.model.enums.Currency;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

public class ShoppingItem implements Serializable {
    private long id;
    private String name;
    private String description;
    private int quantity;
    private double price;
    private Currency currency;
    private boolean status;
    private ShoppingItemCategory category;

    public ShoppingItem() {
    }

    public ShoppingItem(long id, String name, String description, int quantity, double price, boolean status, ShoppingItemCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.category = category;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public ShoppingItemCategory getCategory() {
        return category;
    }

    public void setCategory(ShoppingItemCategory category) {
        this.category = category;
    }
}
