package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationsActivity;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralInvitationFragment extends Fragment {
    View view;

    public GeneralInvitationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_general_invitation, container, false);

        Invitation invitation=InvitationActivity.invitation;

        ((TextView)view.findViewById(R.id.invitation_info_name)).setText(invitation.getEvent().getName());
        ((TextView)view.findViewById(R.id.invitation_info_date)).setText(invitation.getEvent().getStartDateTime().toString()+" - "+invitation.getEvent().getEndDateTime().toString());
        ((TextView)view.findViewById(R.id.invitation_info_description)).setText(invitation.getEvent().getDescription());
      //  ((TextView)view.findViewById(R.id.positionOfNew)).setText(String.valueOf(InvitationActivity.invitationId));

        return view;
    }


}
