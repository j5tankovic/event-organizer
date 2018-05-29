package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.InvitationsAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.EventCategory;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;
import rs.ac.uns.ftn.pma.event_organizer.services.AuthentificationService;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class InvitationsActivity extends AppCompatActivity {
    public static final String EVENT = "rs.ac.uns.ftn.pma.event_organizer.EVENT";
    private ListView listView;
    private ArrayList<Invitation> testDataInvitations=new ArrayList<>();
    private DatabaseReference databaseReferenceInvitations;
    private FirebaseDatabase firebaseDatabase;
    private InvitationsAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private User loggedUser = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    loggedUser = user.getValue(User.class);
                 }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        adapter = new InvitationsAdapter(this,R.layout.activity_invitation_list_view, testDataInvitations );
        listView = (ListView) findViewById(R.id.invitations_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(),
                        InvitationActivity.class);

                intent.putExtra(EVENT, testDataInvitations.get(position).getEvent());

                startActivity(intent);
            }
        });
    }

    public void getInvitations(List<Invitation> allInvitations){

        for(Invitation inv:allInvitations){
            if(inv.getInvitedUser()!=null) {
                if (inv.getInvitedUser().getEmail().equals(loggedUser.getEmail())) {
                    testDataInvitations.add(inv);
                    adapter.notifyDataSetChanged();
                }
            }
        }
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
            newEvent.setDescription((String)eventMap.get("description"));

            /*
            EventCategory newEventCat=new EventCategory();
            Map eventCat=(Map) singleInvitation.get("eventCategory");
            newEventCat.setName((String)eventCat.get("name"));
            newEvent.setEventCategory(newEventCat);
*/
            Map date=(Map)eventMap.get("startDateTime");
            Date startTime=new Date((long)date.get("time"));
            newEvent.setStartDateTime(startTime);
            Map dateEnd=(Map)eventMap.get("endDateTime");
            Date endTime=new Date((long)dateEnd.get("time"));
            newEvent.setEndDateTime(endTime);

            newEvent.setName((String)eventMap.get("name"));
            newInvitation.setEvent(newEvent);

            Map userMap=(Map)singleInvitation.get("invitedUser");
            User newUser=new User();
            newUser.setEmail((String)userMap.get("email"));
            newInvitation.setInvitedUser(newUser);

            allInvitations.add(newInvitation);
        }
        getInvitations(allInvitations);
    }

}
