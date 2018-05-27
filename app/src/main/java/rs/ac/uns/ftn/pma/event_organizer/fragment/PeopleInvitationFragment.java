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
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Event event;
    private DatabaseReference databaseReferenceInvitations;
    private FirebaseDatabase firebaseDatabase;

    public PeopleInvitationFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_people_invitation, container, false);
        recyclerView = view.findViewById(R.id.invitation_people_rv);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PeopleInvitationAdapter(testData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        event= InvitationActivity.event;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceInvitations=firebaseDatabase.getReference().child("invitations");
        databaseReferenceInvitations.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getAllInvitations((Map<String,Object>)dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return view;

    }
    public void getAllInvitations(Map<String,Object> invitations){
        List<Invitation> allInvitations=new ArrayList<>();
        for (Map.Entry<String, Object> entry : invitations.entrySet()){
            Map singleInvitation = (Map) entry.getValue();

            Invitation newInvitation=new Invitation();
            newInvitation.setId((String)singleInvitation.get("id"));

            if(singleInvitation.get("status").equals("ACCEPTED"))
                newInvitation.setStatus(InvitationStatus.ACCEPTED);
            else if(singleInvitation.get("status").equals("REJECTED"))
                newInvitation.setStatus(InvitationStatus.REJECTED);
            else
                newInvitation.setStatus(InvitationStatus.PENDING);

            Map eventMap= (Map) singleInvitation.get("event");
            Event newEvent=new Event();
            newEvent.setId((String)eventMap.get("id"));
            newInvitation.setEvent(newEvent);

            Map userMap=(Map)singleInvitation.get("invitedUser");
            User newUser=new User();
            newUser.setEmail((String) userMap.get("email"));
            newInvitation.setInvitedUser(newUser);

            allInvitations.add(newInvitation);
        }
        findInvitedUsers(allInvitations);
    }
    private void findInvitedUsers(List<Invitation> invitations){
        for(Invitation inv:invitations){
            if(inv.getEvent().getId().equals(event.getId())){
                testData.add(inv);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
