package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.fragment.ShoppingListFragment;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;

public class ShoppingItemOverviewActivity extends AppCompatActivity {

    private ShoppingItem shoppingItem;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static final String ITEM_ID = "rs.ac.uns.ftn.pma.event_organizer.ITEM_ID";
    public static final String SHOPPING_ITEM = "rs.ac.uns.ftn.pma.event_organizer.SHOPPING_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_item_overview);

        Toolbar placeOfferToolbar = findViewById(R.id.shoppingitem_toolbar);
        setSupportActionBar(placeOfferToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        shoppingItem = (ShoppingItem) getIntent().getExtras().get(ShoppingListFragment.SHOPPING_ITEM);

        fillUi();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("shopping_items");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.shoppingitem_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shoppingitem_edit:
                openEditActivity();
                return true;
            case R.id.action_shoppingitem_delete:
                openConfirmationDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openEditActivity() {
        Intent i = new Intent(this, EditShoppingItemActivity.class);
        i.putExtra(SHOPPING_ITEM, shoppingItem);
        startActivityForResult(i, 996);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 996 && resultCode == RESULT_OK) {
            shoppingItem = (ShoppingItem) data.getExtras().get(EditShoppingItemActivity.EDITED_ITEM);
            fillUi();
        }
    }

    private void openConfirmationDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingItemOverviewActivity.this);

        builder
                .setMessage("Delete this item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                        formResult();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void fillUi() {
        TextView name = findViewById(R.id.shoppingitem_name);
        name.setText(shoppingItem.getName());

        TextView quantity = findViewById(R.id.shoppingitem_quantity);
        quantity.setText(String.valueOf(shoppingItem.getQuantity()));

        TextView price = findViewById(R.id.shoppingitem_price);
        price.setText(String.valueOf(shoppingItem.getPrice()));

        TextView description = findViewById(R.id.shoppingitem_description);
        description.setText(shoppingItem.getDescription());

        getSupportActionBar().setTitle(shoppingItem.getName());
    }

    private void formResult() {
        Intent i = new Intent();
        i.putExtra(ITEM_ID, shoppingItem.getId());
        setResult(RESULT_OK, i);
        finish();
    }

    private void delete() {
        databaseReference.child(shoppingItem.getId()).setValue(null);
    }
}
