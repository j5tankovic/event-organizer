package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import rs.ac.uns.ftn.pma.event_organizer.fragment.PlaceOffersFragment;

public class PlaceOfferOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_offer_overview);

        Intent intent = getIntent();
        String placeOffer = intent.getStringExtra(PlaceOffersFragment.PLACE_OFFER);

        TextView tv = findViewById(R.id.placeoffer_location);
        tv.setText(placeOffer);
    }
}
