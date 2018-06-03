package rs.ac.uns.ftn.pma.event_organizer.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.EventsActivity;
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
    private DatabaseReference databaseReferenceUser;
    private DatabaseReference databaseReferenceInvitations;
    private FirebaseDatabase firebaseDatabase;
    private List<User> allUsers = new ArrayList<>();
    private List<Invitation> allInvitations = new ArrayList<>();
    private List<Invitation> eventInvitations = new ArrayList<>();
    private Event selectedEvent;
    private FirebaseAuth mAuth;
    private User loggedUser = new User();
    private DatabaseReference databaseReferenceLogged;

    public PeopleOverviewFragment() {
    }

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

        selectedEvent = (Event) getActivity().getIntent().getExtras().get(EventsActivity.SELECTED_EVENT);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("invitations");

        databaseReferenceUser = firebaseDatabase.getReference().child("users");
        databaseReferenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getAllUsers((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("events")
                .child(selectedEvent.getId()).child("invitations");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventInvitations.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    eventInvitations.add(snapshot.getValue(Invitation.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();

        databaseReferenceLogged = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReferenceLogged.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    loggedUser = user.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ImageButton button = (ImageButton) view.findViewById(R.id.new_invitation_email_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView textViewEmail = (AutoCompleteTextView) view.findViewById(R.id.new_invitation_email_edittext);
                String email = (textViewEmail).getText().toString();

                if (TextUtils.isEmpty(email)) {
                    textViewEmail.setError("Required");
                } else {
                    textViewEmail.setText("");

                    User foundedUser = findUserByEmail(email);

                    if (foundedUser == null) {
                        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", email, null));
                        i.putExtra(Intent.EXTRA_SUBJECT, "Invitation");

                        StringBuilder sb = new StringBuilder();
                        sb.append("Dear Friend, ");
                        sb.append('\n');
                        sb.append("I would like to invite you to attend to my event: ");
                        sb.append(selectedEvent.getName());

                        if (selectedEvent.getStartDateTime() != null && selectedEvent.getEndDateTime() != null) {
                            sb.append(". The event will start ");
                            sb.append(new SimpleDateFormat("dd/MM/yyyy").format(selectedEvent.getStartDateTime()));
                            sb.append(" and end ");
                            sb.append(new SimpleDateFormat("dd/MM/yyyy").format(selectedEvent.getEndDateTime()));
                        }
                        if (selectedEvent.getFinalPlace() != null) {
                            sb.append(" at ");
                            sb.append(selectedEvent.getFinalPlace().getLocationName());
                        }
                        sb.append('\n');
                        sb.append('\n');
                        sb.append("Regards,");
                        sb.append('\n');
                        sb.append(selectedEvent.getCreator().getName() + " " + selectedEvent.getCreator().getLastName() + "!");
                        i.putExtra(Intent.EXTRA_TEXT, sb.toString());
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        boolean invited = false;
                        if (foundedUser.getEmail().equals(loggedUser.getEmail())) {
                            Toast.makeText(getContext(), "You're creator of event.", Toast.LENGTH_LONG).show();
                            invited = true;
                        } else for (Invitation inv : eventInvitations) {
                            if (inv.getInvitedUser().getEmail().equals(foundedUser.getEmail())) {
                                Toast.makeText(getContext(), "User " + foundedUser.getUsername() + " is already invited.", Toast.LENGTH_LONG).show();
                                invited = true;
                            }
                        }
                        if (!invited) {
                            Toast.makeText(getContext(), "Inviting " + email, Toast.LENGTH_LONG).show();
                            createNewInvitation(foundedUser, selectedEvent);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        return view;
    }

    public void createNewInvitation(User foundedUser, Event event) {
        String invitationId = databaseReference.push().getKey();
        Invitation newInvitation = new Invitation();
        newInvitation.setInvitedUser(foundedUser);
        newInvitation.setId(invitationId);
        newInvitation.setEvent(event);
        newInvitation.setStatus(InvitationStatus.PENDING);

//        databaseReference.child(invitationId).setValue(newInvitation);
        DatabaseReference invitations = FirebaseDatabase.getInstance().getReference("events").child(selectedEvent.getId())
                .child("invitations");
        String key = invitations.push().getKey();
        newInvitation.setId(key);
        invitations.child(key).setValue(newInvitation);
        eventInvitations.add(newInvitation);
    }

    public User findUserByEmail(String email) {
        for (User user : allUsers) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public void getAllUsers(Map<String, Object> users) {
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
        setDropBox();
    }

    private void setDropBox() {
        String[] users = new String[allUsers.size()];
        for (int i = 0; i < allUsers.size(); i++) {
            users[i] = allUsers.get(i).getEmail();
        }
        if (users.length != 0) {
            ArrayAdapter<String> adapterTextView = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, users);
            AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.new_invitation_email_edittext);
            textView.setAdapter(adapterTextView);
            textView.setThreshold(1);
        }

    }
}
