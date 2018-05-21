package rs.ac.uns.ftn.pma.event_organizer.model;

import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;

/**
 * Created by Sandra on 4/27/2018.
 */

public class Invitation {

    private long id;
    private Event event;
    private User invitedUser;
    private InvitationStatus status;

    public Invitation(long id, Event event, User invitedUser, InvitationStatus status) {
        this.id = id;
        this.event = event;
        this.invitedUser = invitedUser;
        this.status = status;
    }
    public Invitation(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
}
