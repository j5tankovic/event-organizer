package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

/**
 * Created by Sandra on 4/27/2018.
 */


public class EventCategory implements Serializable{
    private String id;
    private String name;

       public EventCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public EventCategory(String name) {
        this.name = name;
    }

    public EventCategory() {
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

    @Override
    public String toString() {
        return "[EventCategory] name = " + name;
    }
}
