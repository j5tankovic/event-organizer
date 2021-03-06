package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sandra on 4/27/2018.
 */

public class Event implements Serializable{

    private String id;
    private String name;
    private Date startDateTime;
    private Date endDateTime;
    private String description;
    private long budget;
    private String image;
    private HashMap<String, ShoppingItem> shoppingItemList;
    private HashMap<String, PlaceOffer> potentialPlaces;
    private HashMap<String, Invitation> invitations;
    private HashMap<String, Invitation> invitationsByMail;
    private PlaceOffer finalPlace;
    private EventCategory eventCategory;
    private User creator;

    public Event(String id, String name, Date startDateTime, Date endDateTime, String description, long budget, String image) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
        this.budget = budget;
        this.image = image;
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

    public Map<String, ShoppingItem> getShoppingItemList() {
        return shoppingItemList;
    }

    public void setShoppingItemList(HashMap<String, ShoppingItem> shoppingItemList) {
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

    public Map<String, PlaceOffer> getPotentialPlaces() {
        return potentialPlaces;
    }

    public void setPotentialPlaces(HashMap<String, PlaceOffer> potentialPlaces) {
        this.potentialPlaces = potentialPlaces;
    }

    public PlaceOffer getFinalPlace() {
        return finalPlace;
    }

    public void setFinalPlace(PlaceOffer finalPlace) {
        this.finalPlace = finalPlace;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public HashMap<String, Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(HashMap<String, Invitation> invitations) {
        this.invitations = invitations;
    }

    public HashMap<String, Invitation> getInvitationsByMail() {
        return invitationsByMail;
    }

    public void setInvitationsByMail(HashMap<String, Invitation> invitationsByMail) {
        this.invitationsByMail = invitationsByMail;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                ", image='" + image + '\'' +
                ", shoppingItemList=" + shoppingItemList +
                ", potentialPlaces=" + potentialPlaces +
                ", finalPlace=" + finalPlace +
                ", eventCategory=" + eventCategory +
                ", creator=" + creator +
                '}';
    }
}
