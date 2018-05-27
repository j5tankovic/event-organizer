package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.EventsAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.EventCategory;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;


public class EventsActivity extends AppCompatActivity {

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    public static final String EVENT = "rs.ac.uns.ftn.pma.event_organizer.EVENT";
    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    private static final String TAG_HOME = "home";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_LOGOUT = "logout";
    private static final String TAG_PROFILE = "profile";

    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private ListView listView;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName;
    private Toolbar myToolbar;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

    private List<Event> allEvents = new ArrayList<>();
    private List<Event> testData = new ArrayList<>();
    private EventsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        myToolbar = (Toolbar) findViewById(R.id.my_events_toolbar);
        myToolbar.showOverflowMenu();

        ImageView profileImage = (ImageView) findViewById(R.id.my_events_profile_image);
        profileImage.setImageResource(R.drawable.profile);
        profileImage.setClickable(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("events");

        mAuth = FirebaseAuth.getInstance();

//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                getAllEvents((Map<String,Object>)dataSnapshot.getValue());
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//            }
//        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event event = dataSnapshot.getValue(Event.class);
                testData.add(event);
                adapter.notifyDataSetChanged();
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

        adapter = new EventsAdapter(this,R.layout.activity_events_list, testData);
        listView = (ListView) findViewById(R.id.my_events_list);
        listView.setAdapter(adapter);

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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        txtName.setText("Jovan Jovanovic"); //UPISATI IME I PREZIME ULOGOVANOG

        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.);

        // initializing navigation menu
        setUpNavigationView();

    }

    public void prepareTest(){
        for(Event event : allEvents){
            testData.add(event);
            adapter.notifyDataSetChanged();
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

            allEvents.add(event);
        }
        prepareTest();

    }

    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.settings:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.logout:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_LOGOUT;
                        logout();
                        Toast.makeText(EventsActivity.this,"LOGOUT", Toast.LENGTH_LONG);
                        break;
                    case R.id.profile:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_PROFILE;
                        openUserProfileActivity();
                        break;
                    case R.id.my_invitations:
                        Intent intent = new Intent(EventsActivity.this, InvitationsActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        navItemIndex = 0;
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
