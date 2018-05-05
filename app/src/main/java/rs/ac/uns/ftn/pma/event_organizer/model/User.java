package rs.ac.uns.ftn.pma.event_organizer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sandra on 4/27/2018.
 */

public class User {
    private long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String lastName;
    private List<Event> createdEvents;
    private List<Invitation> invitations;

    public User(long id, String username, String password, String email, String name, String lastName, List<Event> createdEvents, List<Invitation> invitations) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.createdEvents = createdEvents;
        this.invitations = invitations;
    }

    public User(long id, String username, String password, String email, String name, String lastName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.createdEvents = new ArrayList<>();
        this.invitations = new ArrayList<>();
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(List<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }
}
