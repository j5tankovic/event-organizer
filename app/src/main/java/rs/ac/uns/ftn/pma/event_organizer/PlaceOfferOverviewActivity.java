package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import rs.ac.uns.ftn.pma.event_organizer.fragment.PlaceOffersFragment;

public class PlaceOfferOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_offer_overview);

        Toolbar placeOfferToolbar = findViewById(R.id.placeoffer_toolbar);
        setSupportActionBar(placeOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String placeOffer = intent.getStringExtra(PlaceOffersFragment.PLACE_OFFER);

        TextView tv = findViewById(R.id.placeoffer_location);
        tv.setText(placeOffer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.placeoffer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
