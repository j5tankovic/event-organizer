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

import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

public class EditShoppingItemActivity extends AppCompatActivity {

    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";
    public static final String EDITED_ITEM = "rs.ac.uns.ftn.pma.event_organizer.EDITED_ITEM";

    private DatabaseReference databaseReference;

    private Event selectedEvent;
    private ShoppingItem shoppingItem;
    private ShoppingItem shoppingItem2Remove;


    private TextView name;
    private TextView description;
    private TextView quantity;
    private TextView price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shopping_item);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.edit_shoppingitem_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        Intent intent = getIntent();
        selectedEvent = (Event) intent.getExtras().get(ShoppingItemOverviewActivity.SELECTED_EVENT);
        shoppingItem = (ShoppingItem) intent.getExtras().get(ShoppingItemOverviewActivity.SHOPPING_ITEM);
        shoppingItem2Remove = (ShoppingItem) intent.getExtras().get(ShoppingItemOverviewActivity.SHOPPING_ITEM);

        name = findViewById(R.id.edit_shoppingitem_name);
        name.setText(shoppingItem.getName());

        description = findViewById(R.id.edit_shoppingitem_description);
        description.setText(shoppingItem.getDescription());

        price = findViewById(R.id.edit_shoppingitem_price);
        price.setText(String.valueOf(shoppingItem.getPrice()));

        quantity = findViewById(R.id.edit_shoppingitem_quantity);
        quantity.setText(String.valueOf(shoppingItem.getQuantity()));

        Button edit = findViewById(R.id.edit_shoppingitem);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingItem = formData();
                Event event = edit(selectedEvent);
                save(event);
                formResult(event);
            }
        });

        Spinner categories = findViewById(R.id.edit_shoppingitem_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shoppingitem_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
    }

    private ShoppingItem formData() {
        shoppingItem.setName(name.getText().toString());
        shoppingItem.setQuantity(Integer.parseInt(quantity.getText().toString()));
        shoppingItem.setDescription(description.getText().toString());
        shoppingItem.setPrice(Long.parseLong(price.getText().toString()));

        return  shoppingItem;
    }

    private Event edit(Event selectedEvent) {
        List<ShoppingItem> shoppingItems = selectedEvent.getShoppingItemList();

        for (int i = 0; i < shoppingItems.size(); i++) {
            if(shoppingItems.get(i).getId().equals(shoppingItem2Remove.getId())) {

                //shoppingItems.remove(i);
                ShoppingItem item = shoppingItems.get(i);
                shoppingItems.get(i).setName(shoppingItem.getName());
                shoppingItems.get(i).setDescription(shoppingItem.getDescription());
                shoppingItems.get(i).setQuantity(shoppingItem.getQuantity());
                shoppingItems.get(i).setPrice(shoppingItem.getPrice());
                shoppingItems.get(i).setStatus(shoppingItem.isStatus());
                shoppingItems.get(i).setCategory(ShoppingItemCategory.FOOD);
            }
        }
        //shoppingItems.add(shoppingItem2Remove);
        selectedEvent.setShoppingItemList(shoppingItems);
        return selectedEvent;
    }

    private void formResult(Event event) {
        Intent i = new Intent();
        i.putExtra(SELECTED_EVENT, event);
        i.putExtra(EDITED_ITEM, shoppingItem);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(Event event) {
        databaseReference.child(event.getId()).setValue(event);
    }

}
