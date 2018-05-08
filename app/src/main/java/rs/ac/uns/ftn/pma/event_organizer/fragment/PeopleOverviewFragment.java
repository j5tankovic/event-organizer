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

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PeopleOverviewAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.RecyclerTouchListener;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

import static android.app.Activity.RESULT_OK;

public class PeopleOverviewFragment extends Fragment {
    public static final String USER = "rs.ac.uns.ftn.pma.event_organizer.USER";

    private View view;

    private List<User> testData = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareTestData();
        return view;
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
