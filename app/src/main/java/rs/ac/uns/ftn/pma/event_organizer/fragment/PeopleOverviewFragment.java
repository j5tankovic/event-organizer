package rs.ac.uns.ftn.pma.event_organizer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PeopleOverviewAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.EventCategory;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;

public class PeopleOverviewFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private  DatabaseReference databaseReferenceUser;
    private  DatabaseReference databaseReferenceInvitations;
    private FirebaseDatabase firebaseDatabase;
    private List<User> allUsers=new ArrayList<>();
    private List<Invitation> allInvitations=new ArrayList<>();
    private List<Invitation> eventInvitations=new ArrayList<>();
    private int eventId=1; //npr.=1 rodjendan   -   FIND THIS EVENT; FROM GENERAL FRAGMENT?

    public PeopleOverviewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_people_overview, container, false);
        recyclerView = view.findViewById(R.id.people_rv);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PeopleOverviewAdapter(eventInvitations);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("invitations");

        databaseReferenceUser=firebaseDatabase.getReference().child("users");
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getAllUsers((Map<String,Object>)dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

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

        ImageButton button=(ImageButton)view.findViewById(R.id.new_invitation_email_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewEmail=(TextView)view.findViewById(R.id.new_invitation_email_edittext);
                String email=(textViewEmail).getText().toString();
                Toast.makeText(getContext(), "Inviting "+email, Toast.LENGTH_LONG).show();
                textViewEmail.setText("");

                User foundedUser=findUserByEmail(email);
                //YOU CAN'T INVITE SOMEONE WHO IS ALREADY INVITED
                //CAN YOU INVITE YOURSELF?
                if(foundedUser!=null) {
                    createNewInvitation(foundedUser, null);//THIS EVENT
                    adapter.notifyDataSetChanged();
                } else{
                    Toast.makeText(getContext(), email+" doesn't have a profile", Toast.LENGTH_LONG).show();
                    //IF THIS USER DOESN'T HAVE PROFILE
                }
            }
        });
        return view;
    }
    public void createNewInvitation(User foundedUser, Event event){
        String invitationId = databaseReference.push().getKey();
        Invitation newInvitation=new Invitation();
        newInvitation.setInvitedUser(foundedUser);
        newInvitation.setId(invitationId);//FIX
        event=new Event();
        event.setId(1L);
        event.setStartDateTime(new Date());
        event.setEndDateTime(new Date());
        event.setName("Rodjendan");
        event.setBudget(50.0);
        event.setDescription("opis rodjendana: prvi rodjendan male tare");
        event.setEventCategory(new EventCategory(1,"Rodjendan"));
        newInvitation.setEvent(event);
        newInvitation.setStatus(InvitationStatus.PENDING);

        databaseReference.child(invitationId).setValue(newInvitation);
        eventInvitations.add(newInvitation);
      }

    public User findUserByEmail( String email){
          for(User user:allUsers){
              if(user.getEmail().equals(email)){
                  return user;
              }
        }
        return null;
      }

    public void getAllUsers(Map<String,Object> users){
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            Map singleUser = (Map) entry.getValue();

            User newUser = new User();
            newUser.setEmail((String) singleUser.get("email"));
            newUser.setUsername((String) singleUser.get("username"));
            newUser.setName((String) singleUser.get("name"));
            newUser.setLastName((String) singleUser.get("lastName"));
            newUser.setPassword((String) singleUser.get("password"));
            newUser.setId((long) singleUser.get("id"));
            allUsers.add(newUser);
        }
    }

    public void getAllInvitations(Map<String,Object> invitations){
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
            newEvent.setId((long)eventMap.get("id"));
            newInvitation.setEvent(newEvent);

            Map userMap=(Map)singleInvitation.get("invitedUser");
            User newUser=new User();
            newUser.setEmail((String)userMap.get("email"));
            newInvitation.setInvitedUser(newUser);

            allInvitations.add(newInvitation);
        }
        prepareTestData();
       }

    private void prepareTestData() {
        for(Invitation inv:allInvitations){
            if(inv.getEvent().getId()==eventId){
                eventInvitations.add(inv);
            }
        }
       adapter.notifyDataSetChanged();
    }
}
