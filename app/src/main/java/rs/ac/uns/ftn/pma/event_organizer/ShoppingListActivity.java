package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.adapter.ShoppingListAdapter;
import rs.ac.uns.ftn.pma.event_organizer.listener.RecyclerTouchListener;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;

public class ShoppingListActivity extends AppCompatActivity {
    public static final String SHOPPING_ITEM = "rs.ac.uns.ftn.pma.event_organizer.SHOPPING_ITEM";

    private List<ShoppingItem> testData = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton addShoppingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        recyclerView = (RecyclerView) findViewById(R.id.shopping_list_rv);

        layoutManager = new LinearLayoutManager(this);
        adapter = new ShoppingListAdapter(testData);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ShoppingListActivity.this, ShoppingItemOverviewActivity.class);
                intent.putExtra(SHOPPING_ITEM, testData.get(position));
                startActivity(intent);
            }


            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareTestData();
    }

    public void openNewShoppingItemForm(View view) {
        Intent intent = new Intent(this, NewShoppingItemActivity.class);
        startActivity(intent);
    }

    private void prepareTestData() {
        ShoppingItem item1 = new ShoppingItem("Pileca krilca", "Fina ukusna pileca krilca", 1, 1000, false);
        ShoppingItem item2 = new ShoppingItem("Gurmanska pljeskavica", "Fina ukusna gurmanska pljeskavica", 2, 1000, false);
        ShoppingItem item3 = new ShoppingItem("Cevapcici", "Fina ukusni cevapcici", 10, 2000, false);
        ShoppingItem item4 = new ShoppingItem("Pivce za zivce", "Fina pitko pivce", 3, 300, false);

        testData.add(item1);
        testData.add(item2);
        testData.add(item3);
        testData.add(item4);

        adapter.notifyDataSetChanged();
    }
}
