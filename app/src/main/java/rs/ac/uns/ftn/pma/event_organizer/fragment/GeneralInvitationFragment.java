package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationsActivity;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralInvitationFragment extends Fragment {
    private View view;
    private Event event;

    public GeneralInvitationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_general_invitation, container, false);

        event=InvitationActivity.event;

        ((TextView)view.findViewById(R.id.invitation_info_name)).setText(event.getName());
        String date1str="";
        if(event.getStartDateTime()!=null)
           date1str= new SimpleDateFormat("dd/MM/yyyy").format(event.getStartDateTime());
        String date2str ="";
        if(event.getEndDateTime()!=null)
            date2str=new SimpleDateFormat("dd/MM/yyyy").format(event.getEndDateTime());
        ((TextView)view.findViewById(R.id.invitation_info_date)).setText(date1str+" - "+date2str);
        ((TextView)view.findViewById(R.id.invitation_info_description)).setText(event.getDescription());
        if(event.getEventCategory()!=null)
            ((TextView)view.findViewById(R.id.invitation_info_category)).setText(event.getEventCategory().getName());
        //location?

        return view;
    }


}
