package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.adapter.PlaceOffersAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.RecyclerTouchListener;

public class PlaceOffersActivity extends AppCompatActivity {
    public static final String PLACE_OFFER = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER";

    private List<String> testData = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton addPlaceOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_offers);
        recyclerView = (RecyclerView) findViewById(R.id.place_offers_rv);

        layoutManager = new LinearLayoutManager(this);
        adapter = new PlaceOffersAdapter(testData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               Intent intent = new Intent(PlaceOffersActivity.this, PlaceOfferOverviewActivity.class);
               intent.putExtra(PLACE_OFFER, testData.get(position));
               startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareTestData();
    }

    public void openNewPlaceOfferForm(View view) {
        Intent intent = new Intent(this, NewPlaceOfferActivity.class);
        startActivity(intent);
    }

    private void prepareTestData() {
        String placeOffer1 = "New place offer";
        String placeOffer2 = "Again new placeOffer";
        String placeOffer3 = "How you doin";
        String placeOffer4 = "Fine day my friend";

        this.testData.add(placeOffer1);
        this.testData.add(placeOffer2);
        this.testData.add(placeOffer3);
        this.testData.add(placeOffer4);

        adapter.notifyDataSetChanged();
    }
}
