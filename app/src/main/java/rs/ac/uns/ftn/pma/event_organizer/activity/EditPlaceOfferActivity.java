package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.fragment.PlaceOffersFragment;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class EditPlaceOfferActivity extends AppCompatActivity {

    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    public static final String EDITED_OFFER = "rs.ac.uns.ftn.pma.event_organizer.EDITED_OFFER";

    private DatabaseReference databaseReference;

    private Event selectedEvent;
    private PlaceOffer placeOffer;
    private PlaceOffer placeOffer2Remove;

    private TextView location;
    private TextView notes;
    private TextView capacity;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place_offer);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.edit_placeoffer_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        Intent intent = getIntent();
        selectedEvent = (Event) intent.getExtras().get(PlaceOfferOverviewActivity.SELECTED_EVENT);
        placeOffer = (PlaceOffer) intent.getExtras().get(PlaceOfferOverviewActivity.PLACE_OFFER);
        placeOffer2Remove = (PlaceOffer) intent.getExtras().get(PlaceOfferOverviewActivity.PLACE_OFFER);


        location = findViewById(R.id.edit_placeoffer_location);
        location.setText(placeOffer.getLocation().getAddress());

        notes = findViewById(R.id.edit_placeoffer_notes);
        notes.setText(placeOffer.getNotes());

        price = findViewById(R.id.edit_placeoffer_price);
        price.setText(String.valueOf(placeOffer.getPrice()));

        capacity = findViewById(R.id.edit_placeoffer_capacity);
        capacity.setText(String.valueOf(placeOffer.getCapacity()));

        Button edit = findViewById(R.id.edit_placeoffer);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOffer = formData();
                Event event = edit(selectedEvent);
                save(event);
                formResult(event);
            }
        });

    }

    private PlaceOffer formData() {
        placeOffer.setCapacity(Integer.parseInt(capacity.getText().toString()));
        placeOffer.setNotes(notes.getText().toString());
        placeOffer.setPrice(Long.parseLong(price.getText().toString()));

        return  placeOffer;
    }

    private Event edit(Event selectedEvent) {
        List<PlaceOffer> placeOffers = selectedEvent.getPotentialPlaces();

        for (int i = 0; i < placeOffers.size(); i++) {
            if(placeOffers.get(i).getId().equals(placeOffer2Remove.getId())) {
                PlaceOffer offer = placeOffers.get(i);
                placeOffers.get(i).setId(placeOffer.getId());
                placeOffers.get(i).setLocationName(placeOffer.getLocationName());
                placeOffers.get(i).setNotes(placeOffer.getNotes());
                placeOffers.get(i).setCapacity(placeOffer.getCapacity());
                placeOffers.get(i).setPrice(placeOffer.getPrice());
            }
        }
        selectedEvent.setPotentialPlaces(placeOffers);
        return selectedEvent;
    }

    private void formResult(Event event) {
        Intent i = new Intent();
        i.putExtra(SELECTED_EVENT, event);
        i.putExtra(EDITED_OFFER, placeOffer);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(Event event) {
        databaseReference.child(event.getId()).setValue(event);
    }
}
