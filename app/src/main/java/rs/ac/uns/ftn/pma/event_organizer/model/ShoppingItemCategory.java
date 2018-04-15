package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

public class ShoppingItemCategory implements Serializable {

    private String name;

    public ShoppingItemCategory() {
    }

    public ShoppingItemCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
