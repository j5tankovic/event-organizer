package rs.ac.uns.ftn.pma.event_organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.adapter.PlaceOffersAdapter;

public class PlaceOffersActivity extends AppCompatActivity {
    private List<String> testData = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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

        prepareTestData();
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
