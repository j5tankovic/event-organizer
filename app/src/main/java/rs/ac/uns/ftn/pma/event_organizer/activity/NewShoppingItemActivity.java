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

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

public class NewShoppingItemActivity extends AppCompatActivity {

    public static final String ADDED_ITEM = "rs.ac.uns.ftn.pma.event_organizer.ADDED_ITEM";

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shopping_item);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.new_shoppingitem_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Button add = findViewById(R.id.add_shoppingitem);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingItem item = formShoppingItem();
                save(item);
                formResult(item);
            }
        });

        Spinner categories = findViewById(R.id.new_shoppingitem_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shoppingitem_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("shopping_items");
    }

    private ShoppingItem formShoppingItem() {
        TextView name = findViewById(R.id.new_shoppingitem_name);
        TextView description = findViewById(R.id.new_shoppingitem_description);
        TextView quantity = findViewById(R.id.new_shoppingitem_quantity);
        TextView price = findViewById(R.id.new_shoppingitem_price);

        ShoppingItem item = new ShoppingItem();
        item.setName(name.getText().toString());
        item.setDescription(description.getText().toString());
        item.setQuantity(Integer.valueOf(quantity.getText().toString()));
        item.setPrice(Double.valueOf(price.getText().toString()));
        item.setCategory(ShoppingItemCategory.FOOD);

        return item;
    }

    private void formResult(ShoppingItem item) {
        Intent i = new Intent();
        i.putExtra(ADDED_ITEM, item);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(ShoppingItem item) {
        String key = databaseReference.push().getKey();

        item.setId(key);
        databaseReference.child(key).setValue(item);
    }
}
