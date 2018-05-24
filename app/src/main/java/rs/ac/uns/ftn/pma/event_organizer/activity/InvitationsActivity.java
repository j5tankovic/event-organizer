package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InvitationsActivity extends AppCompatActivity {
    public static final String EVENT = "rs.ac.uns.ftn.pma.event_organizer.EVENT";
    private ListView listView;
    private ArrayList<Invitation> testDataInvitations=new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceInvitations;
    private FirebaseDatabase firebaseDatabase;
    private List<Invitation> allInvitations=new ArrayList<>();
    private InvitationsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

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

    public void prepareTest(){
        // ulogovan npr:
        User user1=new User(1,"ja","ja","ja@gmail.com","ja","ja",null, null);
        for(Invitation inv:allInvitations){
            if(inv.getInvitedUser()!=null) {
                if (inv.getInvitedUser().getEmail().equals(user1.getEmail())) {

                    testDataInvitations.add(inv);
                    adapter.notifyDataSetChanged();
                }
            }
        }

    }
    public void getAllInvitations(Map<String,Object> invitations){
        for (Map.Entry<String, Object> entry : invitations.entrySet()){
            Map singleInvitation = (Map) entry.getValue();

            Invitation newInvitation=new Invitation();
            newInvitation.setId((long)singleInvitation.get("id"));

            if(singleInvitation.get("status").equals("ACCEPTED"))
                newInvitation.setStatus(InvitationStatus.ACCEPTED);
            else if(singleInvitation.get("status").equals("REJECTED"))
                newInvitation.setStatus(InvitationStatus.REJECTED);
            else
                newInvitation.setStatus(InvitationStatus.PENDING);

            Map eventMap= (Map) singleInvitation.get("event");
            Event newEvent=new Event();
            newEvent.setId((long)eventMap.get("id"));
            newEvent.setDescription((String)eventMap.get("description"));

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
            newUser.setId((long)eventMap.get("id"));
            newUser.setLastName((String)eventMap.get("lastname"));
            newUser.setUsername((String)eventMap.get("username"));
            newInvitation.setInvitedUser(newUser);

            allInvitations.add(newInvitation);
        }
        prepareTest();
    }

}
