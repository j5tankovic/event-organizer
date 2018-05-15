package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.content.Intent;
import android.os.Bundle;
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

import rs.ac.uns.ftn.pma.event_organizer.activity.NewPlaceOfferActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.PlaceOfferOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.adapter.PlaceOffersAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.RecyclerTouchListener;
import rs.ac.uns.ftn.pma.event_organizer.model.Location;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceOffersFragment extends Fragment {
    public static final String PLACE_OFFER = "rs.ac.uns.ftn.pma.event_organizer.PLACE_OFFER";

    private View view;

    private List<PlaceOffer> testData = new ArrayList<PlaceOffer>();
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
        addPlaceOffer = view.findViewById(R.id.add_place_offer);

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
                startActivityForResult(intent, 998);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        addPlaceOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewPlaceOfferActivity.class);
                startActivityForResult(intent, 995);
            }
        });

        prepareTestData();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 998 && resultCode == RESULT_OK) {
            long id = data.getLongExtra(PlaceOfferOverviewActivity.PLACE_OFFER_ID, -1);
            PlaceOffer offer = findById(id);
            if (offer != null) {
                removeFromList(offer);
                adapter.notifyDataSetChanged();
            }
        } else if (requestCode == 995 && resultCode == RESULT_OK) {
            PlaceOffer offer = (PlaceOffer) data.getExtras().get(NewPlaceOfferActivity.ADDED_OFFER);
            addToList(offer);
            adapter.notifyDataSetChanged();
        }
    }

    private void prepareTestData() {
        Location loc1 = new Location(45.2505725, 19.845315, "Maksima Gorkog 17/a");
        Location loc2 = new Location(45.2544913, 19.839304, "Pap Pavla 6");
        Location loc3 = new Location(45.2491729, 19.8411698, "Sremska 9");

        PlaceOffer po1 = new PlaceOffer(1, 30, loc1, 1000);
        PlaceOffer po2 = new PlaceOffer(2, 10, loc2, 2000);
        PlaceOffer po3 = new PlaceOffer(3, 40, loc3, 3000);

        testData.add(po1);
        testData.add(po2);
        testData.add(po3);

        adapter.notifyDataSetChanged();
    }

    private PlaceOffer findById(long id) {
        for (PlaceOffer offer : testData) {
            if (offer.getId() == id) {
                return offer;
            }
        }
        return null;
    }

    private void removeFromList(PlaceOffer offer) {
        testData.remove(offer);
    }

    private void addToList(PlaceOffer offer) {
        testData.add(offer);
    }

}
