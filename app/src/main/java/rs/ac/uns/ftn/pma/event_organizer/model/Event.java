package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Sandra on 4/27/2018.
 */

public class Event implements Serializable {

    private String id;
    private String name;
    private Date startDateTime;
    private Date endDateTime;
    private String description;
    private long budget;
    private String image;
    private List<ShoppingItem> shoppingItemList;
    private List<PlaceOffer> potentialPlaces;
    private PlaceOffer finalPlace;
    private EventCategory eventCategory;

    public Event(String id, String name, Date startDateTime, Date endDateTime, String description, long budget, String image, List<ShoppingItem> shoppingItemList) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
        this.budget = budget;
        this.image = image;
        this.shoppingItemList = shoppingItemList;
    }

    public Event(String name, Date startDateTime, Date endDateTime) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Event() {
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public List<ShoppingItem> getShoppingItemList() {
        return shoppingItemList;
    }

    public void setShoppingItemList(List<ShoppingItem> shoppingItemList) {
        this.shoppingItemList = shoppingItemList;
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

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PlaceOffer> getPotentialPlaces() {
        return potentialPlaces;
    }

    public void setPotentialPlaces(List<PlaceOffer> potentialPlaces) {
        this.potentialPlaces = potentialPlaces;
    }

    public PlaceOffer getFinalPlace() {
        return finalPlace;
    }

    public void setFinalPlace(PlaceOffer finalPlace) {
        this.finalPlace = finalPlace;
    }
}
