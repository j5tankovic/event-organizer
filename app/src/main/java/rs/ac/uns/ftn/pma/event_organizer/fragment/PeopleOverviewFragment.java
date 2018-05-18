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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private List<Invitation> testDataInvitation=new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;


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

        ImageButton button=(ImageButton)view.findViewById(R.id.new_invitation_email_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=((TextView)view.findViewById(R.id.new_invitation_email_edittext)).getText().toString();
                //go through all users.emails find user with this email
                //findUserByEmail()
                List<User> allUsers=testData;
                User foundedUser=null;
                for(User user:allUsers){
                    if(user.getEmail().equals(email))
                        foundedUser=user;
                        break;
                }
                if(foundedUser!=null){
                    List<Invitation> invitations= InvitationActivity.testDataInvitations;
                    //if doesnt exist invitation with this event and this user -> create new
                     firebaseDatabase = FirebaseDatabase.getInstance();
                    // get reference to 'users' node
                    databaseReference = firebaseDatabase.getReference("invitations");
                    createNewInvitation(foundedUser,null);


                }
                //add it to user.listInvitation
                //add to database
            }
        });

        return view;
    }
    public void createNewInvitation(User foundedUser, Event event){

        String invitationId = databaseReference.push().getKey();
        Invitation newInvitation=new Invitation();
        newInvitation.setInvitedUser(foundedUser);
        newInvitation.setId(2L);
        newInvitation.setStatus(InvitationStatus.PENDING);
        databaseReference.child(invitationId).setValue(newInvitation);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK) {

        } else if (requestCode == 994 && resultCode == RESULT_OK) {

        }
    }
    private void prepareTestData() {
       User user1=new User(0,"user1","user1","user1@gmail.com","user1","user1",null, null);
       User user2=new User(1,"user2","user2","user2@gmail.com","user2","user2",null, null);
       User user3=new User(2,"user3","user3","user3@gmail.com","user3","user3",null, null);

       testData.add(user1);
       testData.add(user2);
       testData.add(user3);

       adapter.notifyDataSetChanged();
    }



}
