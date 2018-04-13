package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<ShoppingItem> testData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView shoppingItemName;
        public TextView shoppingItemQuantity;
        public CheckBox shoppingItemStatus;

        public ViewHolder(View v) {
            super(v);
            shoppingItemName = (TextView) v.findViewById(R.id.shoppinglist_item_name);
            shoppingItemQuantity = (TextView) v.findViewById(R.id.shoppinglist_item_quantity);
            shoppingItemStatus = (CheckBox) v.findViewById(R.id.shoppinglist_item_status);
        }

    }

    public ShoppingListAdapter(List<ShoppingItem> testData) {
        this.testData = testData;
    }

    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item_view, parent, false);

        ShoppingListAdapter.ViewHolder vh = new ShoppingListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        holder.shoppingItemName.setText(testData.get(position).getName());
        holder.shoppingItemQuantity.setText(String.valueOf(testData.get(position).getQuantity()));
        holder.shoppingItemStatus.setChecked(testData.get(position).isStatus());
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }
}
