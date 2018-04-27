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


}
