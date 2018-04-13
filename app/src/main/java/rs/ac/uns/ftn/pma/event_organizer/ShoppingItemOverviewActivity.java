package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShoppingItemOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_item_overview);

        Intent intent = getIntent();
        String placeOffer = intent.getStringExtra(ShoppingListActivity.SHOPPING_ITEM);

        TextView tv = findViewById(R.id.shoppingitem_name);
        tv.setText(placeOffer);
    }
}
