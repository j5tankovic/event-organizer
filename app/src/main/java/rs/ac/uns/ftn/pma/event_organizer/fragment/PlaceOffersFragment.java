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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOffersFragment extends Fragment {

    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    public static final String PLACE_OFFER = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER";

    private List<PlaceOffer> testData = new ArrayList<PlaceOffer>();
    private RecyclerView.Adapter adapter;

    private DatabaseReference dbReference;

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
                startActivityForResult(intent, 998);
            }

            @Override
            public void onLongClicked(int position) {

            }
        });
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

        dbReference = FirebaseDatabase.getInstance().getReference("events");
        dbReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.child("potentialPlaces").getValue() != null && dataSnapshot.child("id").getValue().equals(selectedEvent.getId())) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) dataSnapshot.child("potentialPlaces").getValue();
                    for (Map<String, Object> map : list) {
                        PlaceOffer placeOffer = getFromMap(map);
                        testData.add(placeOffer);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String key = dataSnapshot.getKey();
//                PlaceOffer offer = findById(key);
//                testData.remove(offer);
//                adapter.notifyDataSetChanged();
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
            PlaceOffer placeOffer = (PlaceOffer) data.getExtras().get(EditPlaceOfferActivity.EDITED_OFFER);
            testData.add(placeOffer);
            adapter.notifyDataSetChanged();
        } else if (requestCode == 995 && resultCode == RESULT_OK) { //DODAVANJE
            PlaceOffer placeOffer = (PlaceOffer) data.getExtras().get(NewPlaceOfferActivity.ADDED_OFFER);
            testData.add(placeOffer);
            adapter.notifyDataSetChanged();
        }
    }

    private PlaceOffer getFromMap(Map<String, Object> map) {
        PlaceOffer placeOffer = new PlaceOffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getKey().equals("id")) {
                placeOffer.setId((String) entry.getValue());
            } else if(entry.getKey().equals("capacity")) {
                placeOffer.setCapacity((Long) entry.getValue());
            } else if(entry.getKey().equals("notes")) {
                placeOffer.setNotes((String) entry.getValue());
            } else if(entry.getKey().equals("price")) {
                placeOffer.setPrice((Long) entry.getValue());
            } else if(entry.getKey().equals("locationName")) {
                placeOffer.setLocationName((String) entry.getValue());
            }

            else if(entry.getKey().equals("location")) {
                Location location = new Location();
                Map<String, Object> mapLocation = (Map<String, Object>) entry.getValue();
                for(Map.Entry<String, Object> entryValue : mapLocation.entrySet()) {
                    if(entryValue.getKey().equals("lat")) {
                        location.setLat((Double) entryValue.getValue());
                    } else if(entryValue.getKey().equals("lng")) {
                        location.setLng((Double) entryValue.getValue());
                    } else if(entryValue.getKey().equals("address")) {
                        location.setAddress((String) entryValue.getValue());
                    } else if(entryValue.getKey().equals("name")) {
                        location.setName((String) entryValue.getValue());
                    }
                }
                placeOffer.setLocation(location);
            }
        }

        return placeOffer;
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
