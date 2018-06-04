package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationActivity;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PeopleInvitationAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleInvitationFragment extends Fragment {
    View view;
    private List<Invitation> testData = new ArrayList<>();
    private List<Invitation> testDataMail = new ArrayList<>();
    private List<Invitation> allInvitations = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Event event;

    public PeopleInvitationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_people_invitation, container, false);
        recyclerView = view.findViewById(R.id.invitation_people_rv);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PeopleInvitationAdapter(allInvitations);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        event = InvitationActivity.event;

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("events")
                .child(event.getId()).child("invitations");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                testData.clear();
                allInvitations.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                   Invitation invitation = snapshot.getValue(Invitation.class);
                   testData.add(invitation);
                   allInvitations.add(invitation);
                   adapter.notifyDataSetChanged();
                }
                for(Invitation inv:testDataMail) {
                    allInvitations.add(inv);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference dbReferenceMail = FirebaseDatabase.getInstance().getReference("events")
                .child(event.getId()).child("invitationsByMail");

        dbReferenceMail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                testDataMail.clear();
                allInvitations.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Invitation invitation = snapshot.getValue(Invitation.class);
                    testDataMail.add(invitation);
                    allInvitations.add(snapshot.getValue(Invitation.class));
                    adapter.notifyDataSetChanged();
                }
                for(Invitation inv:testData) {
                    allInvitations.add(inv);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }
}
