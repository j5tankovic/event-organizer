package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.NewPlaceOfferActivity;
import rs.ac.uns.ftn.pma.event_organizer.PlaceOfferOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.PlaceOffersActivity;
import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PlaceOffersAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.RecyclerTouchListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOffersFragment extends Fragment {
    public static final String PLACE_OFFER = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER";

    private View view;

    private List<String> testData = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton addPlaceOffer;

    public PlaceOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_place_offers, container, false);
        recyclerView = view.findViewById(R.id.place_offers_rv);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new PlaceOffersAdapter(testData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), PlaceOfferOverviewActivity.class);
                intent.putExtra(PLACE_OFFER, testData.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareTestData();
        return view;
    }

    public void openNewPlaceOfferForm(View view) {
        Intent intent = new Intent(getContext(), NewPlaceOfferActivity.class);
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
