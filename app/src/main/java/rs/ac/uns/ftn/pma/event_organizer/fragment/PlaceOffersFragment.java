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

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.activity.EditPlaceOfferActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.EventsActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.NewPlaceOfferActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.PlaceOfferOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PlaceOffersAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.ClickListener;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Location;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOffersFragment extends Fragment {

    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    public static final String PLACE_OFFER = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER";

    private List<PlaceOffer> testData = new ArrayList<PlaceOffer>();
    private RecyclerView.Adapter adapter;

    Event selectedEvent;

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

        selectedEvent = (Event) getActivity().getIntent().getExtras().get(EventsActivity.SELECTED_EVENT);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        //ZA PRIKAZ JEDNOG PLACE OFFER-A, U NJEMU JE EDIT
        adapter = new PlaceOffersAdapter(testData, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                Intent intent = new Intent(getContext(), PlaceOfferOverviewActivity.class);
                intent.putExtra(SELECTED_EVENT, selectedEvent);
                intent.putExtra(PLACE_OFFER, testData.get(position));
                intent.putExtra("PLACE_OFFER_POS", position);
                startActivityForResult(intent, 998);
            }

            @Override
            public void onLongClicked(int position) {

            }
        }, selectedEvent);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        //ZA DODAVANJE NOVOG
        addPlaceOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewPlaceOfferActivity.class);
                intent.putExtra(SELECTED_EVENT, selectedEvent);
                startActivityForResult(intent, 995);
            }
        });


        DatabaseReference dbReferencePlaces = FirebaseDatabase.getInstance().getReference("events")
                .child(selectedEvent.getId()).child("potentialPlaces");
        dbReferencePlaces.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PlaceOffer offer = dataSnapshot.getValue(PlaceOffer.class);
                testData.add(offer);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                PlaceOffer offer = dataSnapshot.getValue(PlaceOffer.class);
                for (PlaceOffer po: testData) {
                    if (offer.getId().equals(po.getId())) {
                        adapter.notifyDataSetChanged();
                    }
                }
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
        //TODO: remove this when refactoring
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
