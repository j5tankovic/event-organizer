package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

public class Location implements Serializable {

    private double lat;
    private double lng;
    private String address;
    private String name;

    public Location() {
    }

    public Location(double lat, double lng, String address, String name) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
