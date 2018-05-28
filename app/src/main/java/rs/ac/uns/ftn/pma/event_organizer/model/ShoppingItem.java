package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

import rs.ac.uns.ftn.pma.event_organizer.model.enums.Currency;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

public class ShoppingItem implements Serializable {
    private String id;
    private String name;
    private String description;
    private long quantity;
    private long price;
    private Currency currency;
    private boolean status;
    private ShoppingItemCategory category;

    public ShoppingItem() {
    }

    public ShoppingItem(String id, String name, String description, long quantity, long price, boolean status, ShoppingItemCategory category) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
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

    @Override
    public String toString() {
        return "ShoppingItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", currency=" + currency +
                ", status=" + status +
                ", category=" + category +
                '}';
    }
}
