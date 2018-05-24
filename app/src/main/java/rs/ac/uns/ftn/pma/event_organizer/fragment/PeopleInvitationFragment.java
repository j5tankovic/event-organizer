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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationActivity;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PeopleInvitationAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleInvitationFragment extends Fragment {
    View view;
    private List<User> testData = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public PeopleInvitationFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_people_invitation, container, false);
        recyclerView = view.findViewById(R.id.invitation_people_rv);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PeopleInvitationAdapter(testData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        prepareTestData();

        Event event= InvitationActivity.event;
        //all users ->list of Events.id ==  event.id
        //status? from invitation

        return view;

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
