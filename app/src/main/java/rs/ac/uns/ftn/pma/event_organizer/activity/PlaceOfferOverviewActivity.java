package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.fragment.PlaceOffersFragment;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class PlaceOfferOverviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    public static final String PLACE_OFFER = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER";
    public static final String PLACE_OFFER_ID = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER_ID";

    private MapView mapView;
    private GoogleMap gMap;

    private Event selectedEvent;
    private PlaceOffer placeOffer;
    private String pos;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_offer_overview);

        Toolbar placeOfferToolbar = findViewById(R.id.placeoffer_toolbar);
        setSupportActionBar(placeOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        selectedEvent = (Event) intent.getExtras().get(PlaceOffersFragment.SELECTED_EVENT);
        placeOffer = (PlaceOffer) intent.getExtras().get(PlaceOffersFragment.PLACE_OFFER);
        pos = String.valueOf(intent.getIntExtra("PLACE_OFFER_POS", -1));

        fillUi();

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.placeoffer_mapview);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.placeoffer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMinZoomPreference(12);

        LatLng coords = new LatLng(placeOffer.getLocation().getLat(), placeOffer.getLocation().getLng());
        gMap.addMarker(new MarkerOptions().position(coords).title(placeOffer.getLocation().getAddress()));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_placeoffer_edit:
                openEditActivity();
                return true;
            case R.id.action_placeoffer_delete:
                openConfirmationDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openEditActivity() {
        Intent intent = new Intent(this, EditPlaceOfferActivity.class);
        intent.putExtra(PLACE_OFFER, placeOffer);
        intent.putExtra(SELECTED_EVENT, selectedEvent);
        startActivityForResult(intent, 997);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 997 && resultCode == RESULT_OK) {
            placeOffer = (PlaceOffer) data.getExtras().get(EditPlaceOfferActivity.EDITED_OFFER);
            fillUi();
        }
    }

    private void openConfirmationDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOfferOverviewActivity.this);

        builder
                .setMessage("Delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(placeOffer);
                        formResult();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void fillUi() {
        TextView location = findViewById(R.id.placeoffer_location);
        //location.setText(placeOffer.getLocation().getAddress());
        location.setText(placeOffer.getLocationName());

        TextView notes = findViewById(R.id.placeoffer_notes);
        notes.setText(placeOffer.getNotes());

        TextView capacity = findViewById(R.id.placeoffer_capacity);
        capacity.setText(String.valueOf(placeOffer.getCapacity()));

        TextView price = findViewById(R.id.placeoffer_price);
        price.setText(String.valueOf(placeOffer.getPrice()));

        //getSupportActionBar().setTitle(placeOffer.getLocation().getAddress());
        getSupportActionBar().setTitle(placeOffer.getLocationName());

    }

    private void formResult() {
        Intent i = new Intent();
        i.putExtra(PLACE_OFFER_ID, placeOffer.getId());
        i.putExtra(PLACE_OFFER, placeOffer);
        setResult(RESULT_OK, i);
        finish();
    }

    private void delete(PlaceOffer placeOffer) {
        databaseReference.child("potentialPlaces").child(pos).setValue(null);
    }
}
