package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class EditPlaceOfferActivity extends AppCompatActivity {

    private PlaceOffer placeOffer;

    private TextView location;
    private TextView notes;
    private TextView capacity;
    private TextView price;

    public static final String EDITED_OFFER = "rs.ac.uns.ftn.pma.event_organizer.EDITED_OFFER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place_offer);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.edit_placeoffer_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        placeOffer = (PlaceOffer) intent.getExtras().get(PlaceOfferOverviewActivity.PLACE_OFFER);

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
                edit();
                formResult();
            }
        });
    }

    private void edit() {
        placeOffer.setCapacity(Integer.parseInt(capacity.getText().toString()));
        placeOffer.setNotes(notes.getText().toString());
        placeOffer.setPrice(Double.parseDouble(price.getText().toString()));
    }

    private void formResult() {
        Intent i = new Intent();
        i.putExtra(EDITED_OFFER, placeOffer);
        setResult(RESULT_OK, i);
        finish();
    }
}
