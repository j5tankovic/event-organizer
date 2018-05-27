package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.fragment.PlaceOffersFragment;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Location;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class NewPlaceOfferActivity extends AppCompatActivity {

    public static final String TAG = NewPlaceOfferActivity.class.getSimpleName();
    public static final String ADDED_OFFER = "rs.ac.uns.ftn.pma.event_organizer.ADDED_OFFER";

    private DatabaseReference databaseReference;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place_offer);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.new_placeoffer_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Button add = findViewById(R.id.add_placeoffer);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOffer offer = formPlaceOffer();
                Event selectedEvent = (Event) getIntent().getExtras().get(PlaceOffersFragment.SELECTED_EVENT);

                List<PlaceOffer> placeOffers = new ArrayList<>();
                if(selectedEvent.getPotentialPlaces() != null) {
                    placeOffers = selectedEvent.getPotentialPlaces();
                }
                placeOffers.add(offer);
                selectedEvent.setPotentialPlaces(placeOffers);
                save(selectedEvent);
                formResult(selectedEvent);
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Location");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                location = new Location();
                location.setLat(place.getLatLng().latitude);
                location.setLng(place.getLatLng().longitude);
                location.setAddress(place.getAddress().toString());
                location.setName(place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

    }

    private PlaceOffer formPlaceOffer() {
        //TextView location = findViewById(R.id.new_placeoffer_location);
        TextView capacity = findViewById(R.id.new_placeoffer_capacity);
        TextView notes = findViewById(R.id.new_placeoffer_notes);
        TextView price = findViewById(R.id.new_placeoffer_price);

        PlaceOffer offer = new PlaceOffer();
        String key = databaseReference.push().getKey();
        offer.setId(key);
        offer.setLocationName(location.getName());//PROBAJ SA OVIM
        offer.setCapacity(Integer.valueOf(capacity.getText().toString()));
        offer.setNotes(notes.getText().toString());
        offer.setPrice(Long.parseLong(price.getText().toString()));
        offer.setLocation(location);

        return offer;
    }

    private void formResult(Event event) {
        Intent i = new Intent();
        PlaceOffer lastAdded = event.getPotentialPlaces().get(event.getPotentialPlaces().size()-1);
        i.putExtra(ADDED_OFFER, lastAdded);
        setResult(RESULT_OK, i);

        finish();
    }

    private void save(Event event) {
        databaseReference.child(event.getId()).setValue(event);
    }
}
