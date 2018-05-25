package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.EventsAdapter;
import rs.ac.uns.ftn.pma.event_organizer.adapter.InvitationsAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;

public class MyEventsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;

    private List<Event> allEvents = new ArrayList<>();
    private EventsAdapter adapter;

    public static final String EVENT = "rs.ac.uns.ftn.pma.event_organizer.EVENT";
    private ListView listView;

    private ArrayList<Event> testDataInvitations=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("events");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allEvents = getAllEvents((Map<String,Object>)dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        adapter = new EventsAdapter(this,R.layout.activity_my_events_list, allEvents);
        listView = (ListView) findViewById(R.id.my_events_list);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(),
//                        InvitationActivity.class);
//
//                intent.putExtra(EVENT, testDataInvitations.get(position).getEvent());
//
//                startActivity(intent);
//            }
//        });
    }

    public List<Event> getAllEvents(Map<String,Object> events){
        for (Map.Entry<String, Object> entry : events.entrySet()){
            Map singleInvitation = (Map) entry.getValue();

            Event event = new Event();
            event.setId((String) singleInvitation.get("id"));
            event.setName((String) singleInvitation.get("name"));

            allEvents.add(event);
        }
        return allEvents;
    }
}
