package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.ShoppingItemCategory;

public class NewShoppingItemActivity extends AppCompatActivity {

    public static final String ADDED_ITEM = "rs.ac.uns.ftn.pma.event_organizer.ADDED_ITEM";

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
                formResult(item);
            }
        });
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
}
