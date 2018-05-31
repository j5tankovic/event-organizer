package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

    public static final String ADDED_OFFER = "rs.ac.uns.ftn.pma.event_organizer.ADDED_OFFER";

    private DatabaseReference databaseReference;

    private Event selectedEvent;

    private Location location;
    private TextView txtCapacity;
    private TextView txtNotes;
    private TextView txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place_offer);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.new_placeoffer_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        txtCapacity = findViewById(R.id.new_placeoffer_capacity);
        txtNotes = findViewById(R.id.new_placeoffer_notes);
        txtPrice = findViewById(R.id.new_placeoffer_price);

        Button add = findViewById(R.id.add_placeoffer);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()) {
                    return;
                }
                PlaceOffer placeOffer = formData();
                selectedEvent = (Event) getIntent().getExtras().get(PlaceOffersFragment.SELECTED_EVENT);
               // Event event = add(selectedEvent, placeOffer);
                save(placeOffer);
                formResult(placeOffer);
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

            }
        });

    }

    private boolean validateForm() {
        boolean valid = true;

        String notes = txtNotes.getText().toString();
        if(TextUtils.isEmpty(notes)) {
            txtNotes.setError("Required");
            valid = false;
        }
        String capacity = txtCapacity.getText().toString();
        if(TextUtils.isEmpty(capacity)) {
            txtCapacity.setError("Required");
            valid = false;
        } else {
            if(!TextUtils.isDigitsOnly(capacity)) {
                txtCapacity.setError("Only digits allowed");
                valid = false;
            }
        }
        String price = txtPrice.getText().toString();
        if(TextUtils.isEmpty(price)) {
            txtPrice.setError("Required");
            valid = false;
        } else {
            if(!TextUtils.isDigitsOnly(price)) {
                txtPrice.setError("Only digits allowed");
                valid = false;
            }
        }

        return valid;
    }

    private PlaceOffer formData() {
        PlaceOffer offer = new PlaceOffer();
        String key = databaseReference.push().getKey();
        offer.setId(key);
        offer.setLocationName(location.getName());//PROBAJ SA OVIM
        offer.setCapacity(Integer.valueOf(txtCapacity.getText().toString()));
        offer.setNotes(txtNotes.getText().toString());
        offer.setPrice(Long.parseLong(txtPrice.getText().toString()));
        offer.setLocation(location);

        return offer;
    }

//    private Event add(Event selectedEvent, PlaceOffer placeOffer) {
//        List<PlaceOffer> placeOffers = new ArrayList<>();
//        if(selectedEvent.getPotentialPlaces() != null) {
//            placeOffers = selectedEvent.getPotentialPlaces();
//        }
//        placeOffers.add(placeOffer);
//        selectedEvent.setPotentialPlaces(placeOffers);
//
//        return selectedEvent;
//    }

    private void formResult(PlaceOffer placeOffer) {
        Intent i = new Intent();
       // PlaceOffer lastAdded = event.getPotentialPlaces().get(event.getPotentialPlaces().size()-1);
        i.putExtra(ADDED_OFFER, placeOffer);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(PlaceOffer placeOffer) {
        String key = databaseReference.child(selectedEvent.getId()).child("potentialPlaces").push().getKey();
        placeOffer.setId(key);
        databaseReference.child(selectedEvent.getId()).child("potentialPlaces").child(key).setValue(placeOffer);
    }
}
