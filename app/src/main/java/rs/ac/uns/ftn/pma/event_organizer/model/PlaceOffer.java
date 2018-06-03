package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

public class PlaceOffer implements Serializable {

    private String id;
    private long capacity;
    private String notes;
    private Location location;
    private long price;
    private String locationName;
    private boolean isAccepted;

    public PlaceOffer() {
    }

    public PlaceOffer(String id, long capacity, Location location, long price) {
        this.id = id;
        this.capacity = capacity;
        this.location = location;
        this.price = price;
    }

    public PlaceOffer(String id, long capacity, String notes, Location location, long price, String locationName, boolean isAccepted) {
        this.id = id;
        this.capacity = capacity;
        this.notes = notes;
        this.location = location;
        this.price = price;
        this.locationName = locationName;
        this.isAccepted = isAccepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getLocationName() { return locationName; }

    public void setLocationName(String locationName) { this.locationName = locationName; }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    @Override
    public String toString() {
        return "PlaceOffer{" +
                "id='" + id + '\'' +
                ", capacity=" + capacity +
                ", notes='" + notes + '\'' +
                ", location=" + location +
                ", price=" + price +
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
