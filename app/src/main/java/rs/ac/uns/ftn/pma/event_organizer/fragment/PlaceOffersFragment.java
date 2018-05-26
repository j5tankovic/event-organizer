package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.activity.NewPlaceOfferActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.PlaceOfferOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PlaceOffersAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.ClickListener;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOffersFragment extends Fragment {
    public static final String PLACE_OFFER = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER";

    private List<PlaceOffer> testData = new ArrayList<PlaceOffer>();
    private RecyclerView.Adapter adapter;

    private DatabaseReference dbReference;

    public PlaceOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place_offers, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.place_offers_rv);
        FloatingActionButton addPlaceOffer = view.findViewById(R.id.add_place_offer);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new PlaceOffersAdapter(testData, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                Intent intent = new Intent(getContext(), PlaceOfferOverviewActivity.class);
                intent.putExtra(PLACE_OFFER, testData.get(position));
                startActivityForResult(intent, 998);
            }

            @Override
            public void onLongClicked(int position) {

            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        addPlaceOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewPlaceOfferActivity.class);
                startActivityForResult(intent, 995);
            }
        });

        dbReference = FirebaseDatabase.getInstance().getReference("place_offers");
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PlaceOffer offer = dataSnapshot.getValue(PlaceOffer.class);
                testData.add(offer);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                PlaceOffer offer = dataSnapshot.getValue(PlaceOffer.class);
                testData.add(offer);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                PlaceOffer offer = findById(key);
                testData.remove(offer);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 998 && resultCode == RESULT_OK) {
//            long id = data.getLongExtra(PlaceOfferOverviewActivity.PLACE_OFFER_ID, -1);
//            PlaceOffer offer = findById(id);
//            if (offer != null) {
//                removeFromList(offer);
//                adapter.notifyDataSetChanged();
//            }
        } else if (requestCode == 995 && resultCode == RESULT_OK) {
//            PlaceOffer offer = (PlaceOffer) data.getExtras().get(NewPlaceOfferActivity.ADDED_OFFER);
//            addToList(offer);
//            adapter.notifyDataSetChanged();
        }
    }

    private PlaceOffer findById(String id) {
        for (PlaceOffer offer : testData) {
            if (offer.getId().equals(id)) {
                return offer;
            }
        }
        return null;
    }
}
