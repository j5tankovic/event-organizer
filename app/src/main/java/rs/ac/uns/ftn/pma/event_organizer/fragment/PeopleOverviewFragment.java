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
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationActivity;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PeopleOverviewAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.RecyclerTouchListener;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;

import static android.app.Activity.RESULT_OK;

public class PeopleOverviewFragment extends Fragment {
    public static final String USER = "rs.ac.uns.ftn.pma.event_organizer.USER";

    private View view;

    private List<User> testData = new ArrayList<>();
    private List<User> usersDatabase=new ArrayList<>();
    private List<Invitation> testDataInvitation=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private  DatabaseReference databaseReferenceUser;
    private FirebaseDatabase firebaseDatabase;
    List<User> allUsers;

    public PeopleOverviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_people_overview, container, false);

        recyclerView = view.findViewById(R.id.people_rv);


        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PeopleOverviewAdapter(testData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        prepareTestData();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("invitations");

        databaseReferenceUser=firebaseDatabase.getReference().child("users");
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsers((Map<String,Object>)dataSnapshot.getValue());
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

                //go through all users.emails find user with this email
                //findUserByEmail()
                //if doesn't exist invitation with this event and this user -> create new
                //add it to user.listInvitation
                //add to database ->invitations, users

                User foundedUser=findUserByEmail(email);
                if(foundedUser!=null) {
                    createNewInvitation(foundedUser, null);
                }

                //add on list?
            }
        });

        return view;
    }
    public void createNewInvitation(User foundedUser, Event event){
        String invitationId = databaseReference.push().getKey();
        Invitation newInvitation=new Invitation();
        newInvitation.setInvitedUser(foundedUser);
        newInvitation.setId(2L);
        newInvitation.setEvent(event);
        newInvitation.setStatus(InvitationStatus.PENDING);

        databaseReference.child(invitationId).setValue(newInvitation);
      }

      public User findUserByEmail( String email){
         for(User user:allUsers){
              if(user.getEmail().equals(email)){
                  return user;
              }
        }
        return null;
      }

      public List<User> allUsers(Map<String,Object> users){
        allUsers=new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();

            User newUser=new User();
            newUser.setEmail((String) singleUser.get("email"));
            newUser.setUsername((String) singleUser.get("username"));
            newUser.setName((String) singleUser.get("name"));
            newUser.setLastName((String) singleUser.get("lastName"));
            newUser.setPassword((String) singleUser.get("password"));
            newUser.setId((long) singleUser.get("id"));
            allUsers.add(newUser);
        }
          return allUsers;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK) {

        } else if (requestCode == 994 && resultCode == RESULT_OK) {

        }
    }
    private void prepareTestData() {
       User user1=new User(1,"user1","user1","user1@gmail.com","user1","user1",null, null);
       User user2=new User(2,"user2","user2","user2@gmail.com","user2","user2",null, null);
       User user3=new User(3,"user3","user3","user3@gmail.com","user3","user3",null, null);
       User user4=new User(4,"user4","user4","user4@gmail.com","user4","user4",null, null);
       User user5=new User(5,"user5","user5","user5@gmail.com","user5","user5",null, null);

       testData.add(user1);
       testData.add(user2);
       testData.add(user3);
       testData.add(user4);
       testData.add(user5);

       adapter.notifyDataSetChanged();
    }



}
