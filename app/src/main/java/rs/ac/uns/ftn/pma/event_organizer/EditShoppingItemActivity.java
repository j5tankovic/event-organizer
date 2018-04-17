package rs.ac.uns.ftn.pma.event_organizer;

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

import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;

public class EditShoppingItemActivity extends AppCompatActivity {

    private ShoppingItem shoppingItem;

    private TextView name;
    private TextView description;
    private TextView quantity;
    private TextView price;

    public static final String EDITED_ITEM = "rs.ac.uns.ftn.pma.event_organizer.EDITED_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shopping_item);

        Toolbar newPlaceOfferToolbar = findViewById(R.id.edit_shoppingitem_toolbar);
        setSupportActionBar(newPlaceOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        shoppingItem = (ShoppingItem) intent.getExtras().get(ShoppingItemOverviewActivity.SHOPPING_ITEM);

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
                edit();
                formResult();
            }
        });

        Spinner categories = findViewById(R.id.edit_shoppingitem_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shoppingitem_categories, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
    }

    private void edit() {
        shoppingItem.setName(name.getText().toString());
        shoppingItem.setQuantity(Integer.parseInt(quantity.getText().toString()));
        shoppingItem.setDescription(description.getText().toString());
        shoppingItem.setPrice(Double.parseDouble(price.getText().toString()));
    }

    private void formResult() {
        Intent i = new Intent();
        i.putExtra(EDITED_ITEM, shoppingItem);
        setResult(RESULT_OK, i);
        finish();
    }
}
