package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
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
    private DatabaseReference databaseReferenceEvents;
    private FirebaseDatabase firebaseDatabase;
    private InvitationsAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private User loggedUser = new User();
    private List<Event> events = new ArrayList<Event>();


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
                    addEventsListener();
                 }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new InvitationsAdapter(this,R.layout.activity_invitation_list_view, testDataInvitations);
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

    public void addEventsListener() {
        databaseReferenceEvents = FirebaseDatabase.getInstance().getReference("events");

        databaseReferenceEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                testDataInvitations.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Event e = snapshot.getValue(Event.class);
                    if (e.getInvitations() != null) {
                        for (Invitation invitation: e.getInvitations().values()) {
                            if (invitation.getInvitedUser().getEmail().equals(loggedUser.getEmail())) {
                                testDataInvitations.add(invitation);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
