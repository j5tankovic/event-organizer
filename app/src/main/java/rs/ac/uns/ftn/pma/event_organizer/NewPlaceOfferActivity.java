package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import rs.ac.uns.ftn.pma.event_organizer.model.Location;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class NewPlaceOfferActivity extends AppCompatActivity {

    public static final String ADDED_OFFER = "rs.ac.uns.ftn.pma.event_organizer.ADDED_OFFER";

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
                formResult(offer);
            }
        });
    }

    private PlaceOffer formPlaceOffer() {
        TextView location = findViewById(R.id.new_placeoffer_location);
        TextView capacity = findViewById(R.id.new_placeoffer_capacity);
        TextView notes = findViewById(R.id.new_placeoffer_notes);
        TextView price = findViewById(R.id.new_placeoffer_price);

        PlaceOffer offer = new PlaceOffer();
        offer.setCapacity(Integer.valueOf(capacity.getText().toString()));
        offer.setNotes(notes.getText().toString());
        offer.setPrice(Double.parseDouble(price.getText().toString()));
        offer.setLocation(new Location(0, 0, location.getText().toString()));

        return offer;
    }

    private void formResult(PlaceOffer offer) {
        Intent i = new Intent();
        i.putExtra(ADDED_OFFER, offer);
        setResult(RESULT_OK, i);
        finish();
    }
}
