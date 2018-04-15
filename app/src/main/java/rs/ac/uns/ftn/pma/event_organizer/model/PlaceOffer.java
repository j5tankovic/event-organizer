package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

public class PlaceOffer implements Serializable {

    private int capacity;
    private String notes;
    private Location location;
    private double price;

    public PlaceOffer() {
    }

    public PlaceOffer(int capacity, Location location, double price) {
        this.capacity = capacity;
        this.location = location;
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
