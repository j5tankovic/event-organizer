package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Sandra on 4/27/2018.
 */

public class Event implements Serializable{

    private long id;
    private String name;
    private Date startDateTime;
    private Date endDateTime;
    private String description;
    private double budget;
    private String image;
    private List<ShoppingItem> shoppingItemList;
    private EventCategory eventCategory;

    public Event(long id, String name, Date startDateTime, Date endDateTime, String description, double budget, String image, List<ShoppingItem> shoppingItemList) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
        this.budget = budget;
        this.image = image;
        this.shoppingItemList = shoppingItemList;
    }

    public Event() {
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public List<ShoppingItem> getShoppingItemList() {
        return shoppingItemList;
    }

    public void setShoppingItemList(List<ShoppingItem> shoppingItemList) {
        this.shoppingItemList = shoppingItemList;
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

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(float budget) {
        this.budget = budget;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
