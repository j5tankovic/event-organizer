package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rs.ac.uns.ftn.pma.event_organizer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleInvitationFragment extends Fragment {
    View view;

    public PeopleInvitationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_people_invitation, container, false);
        return view;
    }

}
