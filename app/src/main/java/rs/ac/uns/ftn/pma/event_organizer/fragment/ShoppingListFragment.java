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

import rs.ac.uns.ftn.pma.event_organizer.NewShoppingItemActivity;
import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.ShoppingItemOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.adapter.ShoppingListAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.RecyclerTouchListener;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends Fragment {
    public static final String SHOPPING_ITEM = "rs.ac.uns.ftn.pma.event_organizer.SHOPPING_ITEM";

    private View view;

    private List<ShoppingItem> testData = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton addShoppingItem;


    public ShoppingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        recyclerView = view.findViewById(R.id.shopping_list_rv);
        addShoppingItem = view.findViewById(R.id.add_shopping_item);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ShoppingListAdapter(testData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), ShoppingItemOverviewActivity.class);
                intent.putExtra(SHOPPING_ITEM, testData.get(position));
                startActivityForResult(intent, 999);
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        addShoppingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewShoppingItemActivity.class);
                startActivity(intent);
            }
        });

        prepareTestData();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK) {
            long id = data.getLongExtra(ShoppingItemOverviewActivity.ITEM_ID, -1);
            ShoppingItem item = findById(id);
            if (item != null) {
                removeFromList(item);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void prepareTestData() {
        ShoppingItemCategory food = ShoppingItemCategory.FOOD;
        ShoppingItemCategory drink = ShoppingItemCategory.DRINK;

        ShoppingItem item1 = new ShoppingItem(1, "Pileca krilca",
                "Fina ukusna pileca krilca", 1, 1000, false, food);
        ShoppingItem item2 = new ShoppingItem(2, "Gurmanska pljeskavica",
                "Fina ukusna gurmanska pljeskavica", 2, 1000, false, food);
        ShoppingItem item3 = new ShoppingItem(3, "Cevapcici",
                "Fina ukusni cevapcici", 10, 2000, false, food);
        ShoppingItem item4 = new ShoppingItem(4, "Pivce za zivce",
                "Fina pitko pivce", 3, 300, false, drink);

        testData.add(item1);
        testData.add(item2);
        testData.add(item3);
        testData.add(item4);

        adapter.notifyDataSetChanged();
    }

    private ShoppingItem findById(long id) {
        for (ShoppingItem item : testData) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private void removeFromList(ShoppingItem item) {
        testData.remove(item);
    }

}
