package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.EventsAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.EventCategory;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;


public class EventsActivity extends AppCompatActivity {

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    public static final String EVENT = "rs.ac.uns.ftn.pma.event_organizer.EVENT";
    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    private static final String TAG_HOME = "My events";
    private static final String TAG_INVITATIONS = "My invitations";
    private static final String TAG_LOGOUT = "logout";
    private static final String TAG_PROFILE = "profile";

    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private ListView listView;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imageView;
    private TextView txtName;
    private Toolbar myToolbar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUsers;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private List<Event> allEvents = new ArrayList<>();
    private List<Event> testData = new ArrayList<>();
    private EventsAdapter adapter;

    User loggedUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        myToolbar = (Toolbar) findViewById(R.id.my_events_toolbar);
        myToolbar.showOverflowMenu();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        imageView = navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // initializing navigation menu
        setUpNavigationView();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("events");
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        adapter = new EventsAdapter(this,R.layout.activity_events_list, testData);
        listView = (ListView) findViewById(R.id.my_events_list);
        listView.setAdapter(adapter);

        //--------------------------------------------------
        Query query = databaseReferenceUsers.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    loggedUser = user.getValue(User.class);
                }

                txtName.setText(loggedUser.getName() + " " + loggedUser.getLastName());

                if (loggedUser.getProfilePicture() != null){
                    storageReference = FirebaseStorage.getInstance().getReference().child(loggedUser.getProfilePicture());

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            GlideApp.with(getApplicationContext())
                                    .load(imageURL)
                                    .placeholder(R.drawable.user_picture)
                                    .into(imageView)
                            ;
                            System.out.println("User picture loaded!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            System.out.println("Failed to load a picture!");
                        }
                    });
                }

                databaseReference.addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Event event = dataSnapshot.getValue(Event.class);
                        if(event.getCreator().getUsername().equals(loggedUser.getUsername())) {
                            testData.add(event);
                            adapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //--------------------------------------------------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),
                        EventActivity.class);

                intent.putExtra(SELECTED_EVENT, testData.get(position));

                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.open_add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here
                startActivity(new Intent(EventsActivity.this, AddNewEventActivity.class));
            }
        });

    }

    public void prepareTest(){
        for(Event event : allEvents){
            if(event.getCreator().getUsername().equals(loggedUser.getUsername())) {
                testData.add(event);
                adapter.notifyDataSetChanged();
            }
        }

    }

    public void getAllEvents(Map<String,Object> events){
        for (Map.Entry<String, Object> entry : events.entrySet()){
            Map value = (Map) entry.getValue();

            Event event = new Event();

            event.setId((String) value.get("id"));
            event.setName((String) value.get("name"));
            event.setDescription((String) value.get("description"));
            event.setBudget((Long) value.get("budget"));
            Map startDateTime = (Map) value.get("startDateTime");
            Date startTime = new Date((long) startDateTime.get("time"));
            event.setStartDateTime(startTime);
            Map endDateTime = (Map) value.get("endDateTime");
            Date endTime = new Date((long) endDateTime.get("time"));
            event.setEndDateTime(endTime);
            Map eventCategoryMap = (Map) value.get("eventCategory");
            EventCategory eventCategory = new EventCategory((String) eventCategoryMap.get("name"));
            event.setEventCategory(eventCategory);

            Map creatorMap = (Map) value.get("creator");
            User creator = new User((String) creatorMap.get("username"));
            event.setCreator(creator);


            allEvents.add(event);
        }
        prepareTest();

    }

    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.my_invitations:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_INVITATIONS;
                        Intent intent = new Intent(EventsActivity.this, InvitationsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.my_events:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_HOME;
                        Intent intent1 = new Intent(EventsActivity.this, EventsActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.profile:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PROFILE;
                        openUserProfileActivity();
                        break;
                    case R.id.logout:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_LOGOUT;
                        logout();
                        Toast.makeText(EventsActivity.this,"LOGOUT", Toast.LENGTH_LONG);
                        break;

                    default:
                        navItemIndex = 0;
                        Intent intent2 = new Intent(EventsActivity.this, EventsActivity.class);
                        startActivity(intent2);
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        super.onBackPressed();
    }

    private void logout(){
        mAuth.signOut();
        openLoginActivity();
    }

    private void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void openUserProfileActivity(){
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}
