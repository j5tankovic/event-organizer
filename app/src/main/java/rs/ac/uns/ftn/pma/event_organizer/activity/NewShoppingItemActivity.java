package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

    private TextView txtName;
    private TextView txtDescription;
    private TextView txtQuantity;
    private TextView txtPrice;

    private Event selectedEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shopping_item);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.new_shoppingitem_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.new_shoppingitem_name);
        txtDescription = findViewById(R.id.new_shoppingitem_description);
        txtQuantity = findViewById(R.id.new_shoppingitem_quantity);
        txtPrice = findViewById(R.id.new_shoppingitem_price);

        Button add = findViewById(R.id.add_shoppingitem);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()) {
                    return;
                }
                ShoppingItem item = formShoppingItem();
                selectedEvent = (Event) getIntent().getExtras().get(ShoppingListFragment.SELECTED_EVENT);

               // Event event = add(selectedEvent, item);
                save(item);
                formResult(item);
            }
        });

        Spinner categories = findViewById(R.id.new_shoppingitem_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shoppingitem_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

    }

    private boolean validateForm() {
        boolean valid = true;

        String name = txtName.getText().toString();
        if(TextUtils.isEmpty(name)) {
            txtName.setError("Required");
            valid = false;
        }
        String description = txtDescription.getText().toString();
        if(TextUtils.isEmpty(description)) {
            txtDescription.setError("Required");
            valid = false;
        }
        String price = txtPrice.getText().toString();
        if(TextUtils.isEmpty(price)) {
            txtPrice.setError("Required");
            valid = false;
        } else {
            if(!TextUtils.isDigitsOnly(price)) {
                txtPrice.setError("Only digits allowed");
                valid = false;
            }
        }
        String quantity = txtQuantity.getText().toString();
        if(TextUtils.isEmpty(quantity)) {
            txtQuantity.setError("Required");
            valid = false;
        } else {
            if(!TextUtils.isDigitsOnly(quantity)) {
                txtQuantity.setError("Only digits allowed");
                valid = false;
            }
        }

        return valid;
    }

    private ShoppingItem formShoppingItem() {
        ShoppingItem item = new ShoppingItem();
        item.setName(txtName.getText().toString());
        item.setDescription(txtDescription.getText().toString());
        item.setQuantity(Integer.valueOf(txtQuantity.getText().toString()));
        item.setPrice(Long.valueOf(txtPrice.getText().toString()));
        item.setCategory(ShoppingItemCategory.FOOD);

        return item;
    }


    private void formResult(ShoppingItem item) {
        Intent i = new Intent();
        i.putExtra(ADDED_ITEM, item);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(ShoppingItem shoppingItem) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("events");
        String key = dbReference.child(selectedEvent.getId()).child("shoppingItemList").push().getKey();
        shoppingItem.setId(key);
        dbReference.child(selectedEvent.getId()).child("shoppingItemList").child(key).setValue(shoppingItem);
    }
}
