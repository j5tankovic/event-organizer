package rs.ac.uns.ftn.pma.event_organizer.model;

/**
 * Created by Sandra on 4/27/2018.
 */

public class EventCategory {
    private String id;
    private String name;

    public EventCategory(String id, String name) {
        this.id = id;
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
}
