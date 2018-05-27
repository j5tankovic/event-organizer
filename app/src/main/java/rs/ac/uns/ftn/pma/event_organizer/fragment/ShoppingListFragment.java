package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rs.ac.uns.ftn.pma.event_organizer.activity.EventsActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.NewShoppingItemActivity;
import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.ShoppingItemOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.adapter.ShoppingListAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.ClickListener;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Location;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.Currency;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    public static final String TAG = ShoppingListFragment.class.getSimpleName();
    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    public static final String SHOPPING_ITEM = "rs.ac.uns.ftn.pma.event_organizer.SHOPPING_ITEM";

    private List<ShoppingItem> testData = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private DatabaseReference dbReference;

    Event selectedEvent;

    public ShoppingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.shopping_list_rv);
        FloatingActionButton addShoppingItem = view.findViewById(R.id.add_shopping_item);

        selectedEvent = (Event) getActivity().getIntent().getExtras().get(EventsActivity.SELECTED_EVENT);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ShoppingListAdapter(testData, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                Log.d(TAG, "onPositionClicked");
                Intent intent = new Intent(getContext(), ShoppingItemOverviewActivity.class);
                intent.putExtra(SHOPPING_ITEM, testData.get(position));
                startActivityForResult(intent, 998);
            }

            @Override
            public void onLongClicked(int position) {
                Log.d(TAG, "onLongClicked");
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        addShoppingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewShoppingItemActivity.class);
                intent.putExtra(SELECTED_EVENT, selectedEvent);
                startActivityForResult(intent, 995);
            }
        });

        dbReference = FirebaseDatabase.getInstance().getReference("events");
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("shoppingItemList").getValue() != null && dataSnapshot.child("id").getValue().equals(selectedEvent.getId())) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) dataSnapshot.child("shoppingItemList").getValue();
                    for (Map<String, Object> map : list) {
                        ShoppingItem shoppingItem = new ShoppingItem();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if(entry.getKey().equals("name")) {
                                shoppingItem.setName((String) entry.getValue());
                            } else if(entry.getKey().equals("description")) {
                                shoppingItem.setDescription((String) entry.getValue());
                            } else if(entry.getKey().equals("quantity")) {
                                shoppingItem.setQuantity((Long) entry.getValue());
                            } else if(entry.getKey().equals("price")) {
                                shoppingItem.setPrice((Long) entry.getValue());
                            } else if(entry.getKey().equals("status")) {
                                shoppingItem.setStatus((Boolean) entry.getValue());
                            }
                        }
                        testData.add(shoppingItem);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ShoppingItem item = dataSnapshot.getValue(ShoppingItem.class);
                for (ShoppingItem si: testData) {
                    if (item.getId().equals(si.getId())) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                ShoppingItem item = findById(key);
                testData.remove(item);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 998 && resultCode == RESULT_OK) {
//            String id = data.getStringExtra(ShoppingItemOverviewActivity.ITEM_ID);
//            ShoppingItem item = findById(id);
//            if (item != null) {
//                //removeFromList(item);
//                adapter.notifyDataSetChanged();
//            }
        } else if (requestCode == 995 && resultCode == RESULT_OK) {
            ShoppingItem item = (ShoppingItem) data.getExtras().get(NewShoppingItemActivity.ADDED_ITEM);
            adapter.notifyDataSetChanged();
        }
    }

    private ShoppingItem findById(String id) {
        for (ShoppingItem item : testData) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

}
