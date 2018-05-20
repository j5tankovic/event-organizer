package rs.ac.uns.ftn.pma.event_organizer.model;

import java.io.Serializable;

/**
 * Created by Sandra on 4/27/2018.
 */

public class EventCategory implements Serializable{
    private long id;
    private String name;

    public EventCategory(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public EventCategory() {
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
}
