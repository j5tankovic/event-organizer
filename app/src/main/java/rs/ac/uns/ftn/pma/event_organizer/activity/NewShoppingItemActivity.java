package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.fragment.PlaceOffersFragment;
import rs.ac.uns.ftn.pma.event_organizer.fragment.ShoppingListFragment;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

public class NewShoppingItemActivity extends AppCompatActivity {

    public static final String ADDED_ITEM = "rs.ac.uns.ftn.pma.event_organizer.ADDED_ITEM";

    private DatabaseReference databaseReference;

    private Event selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shopping_item);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.new_shoppingitem_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        Button add = findViewById(R.id.add_shoppingitem);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingItem item = formShoppingItem();
                selectedEvent = (Event) getIntent().getExtras().get(ShoppingListFragment.SELECTED_EVENT);

                Event event = add(selectedEvent, item);
                save(event);
                formResult(event);
            }
        });

        Spinner categories = findViewById(R.id.new_shoppingitem_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shoppingitem_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

    }

    private ShoppingItem formShoppingItem() {
        TextView name = findViewById(R.id.new_shoppingitem_name);
        TextView description = findViewById(R.id.new_shoppingitem_description);
        TextView quantity = findViewById(R.id.new_shoppingitem_quantity);
        TextView price = findViewById(R.id.new_shoppingitem_price);

        ShoppingItem item = new ShoppingItem();
        //String key = databaseReference.push().getKey();
        //item.setId(key);
        item.setName(name.getText().toString());
        item.setDescription(description.getText().toString());
        item.setQuantity(Integer.valueOf(quantity.getText().toString()));
        item.setPrice(Long.valueOf(price.getText().toString()));
        item.setCategory(ShoppingItemCategory.FOOD);

        return item;
    }

    private Event add(Event selectedEvent, ShoppingItem shoppingItem) {
        List<ShoppingItem> shoppingItems = new ArrayList<>();
        if(selectedEvent.getShoppingItemList() != null) {
            shoppingItems = selectedEvent.getShoppingItemList();
        }
        shoppingItems.add(shoppingItem);
        selectedEvent.setShoppingItemList(shoppingItems);

        return selectedEvent;
    }

    private void formResult(Event event) {
        Intent i = new Intent();
        ShoppingItem lastAdded = event.getShoppingItemList().get(event.getShoppingItemList().size()-1);
        i.putExtra(ADDED_ITEM, lastAdded);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(Event event) {
        databaseReference.child(event.getId()).setValue(event);
    }
}
