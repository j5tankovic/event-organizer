package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.listener.ClickListener;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private ClickListener clickListener;
    private List<ShoppingItem> testData;
    private Event event;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView shoppingItemName;
        public TextView shoppingItemQuantity;
        public CheckBox shoppingItemStatus;
        public WeakReference<ClickListener> listenerRef;

        public ViewHolder(final View v, ClickListener clickListener) {
            super(v);

            listenerRef = new WeakReference<>(clickListener);
            shoppingItemName = (TextView) v.findViewById(R.id.shoppinglist_item_name);
            shoppingItemQuantity = (TextView) v.findViewById(R.id.shoppinglist_item_quantity);
            shoppingItemStatus = (CheckBox) v.findViewById(R.id.shoppinglist_item_status);

            v.setOnClickListener(this);
            shoppingItemStatus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == shoppingItemStatus.getId()) {
                ShoppingItem item = testData.get(getAdapterPosition());
                item.setStatus(!item.isStatus());
                update(item);
            } else {
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }

    public ShoppingListAdapter(List<ShoppingItem> testData, ClickListener clickListener, Event event) {
        this.testData = testData;
        this.clickListener = clickListener;
        this.event = event;
    }

    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item_view, parent, false);

        ShoppingListAdapter.ViewHolder vh = new ShoppingListAdapter.ViewHolder(v, clickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoppingListAdapter.ViewHolder holder, int position) {

        holder.shoppingItemName.setText(testData.get(position).getName());
        holder.shoppingItemQuantity.setText(String.valueOf(testData.get(position).getQuantity()));
        holder.shoppingItemStatus.setChecked(testData.get(position).isStatus());
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }

    private void update(ShoppingItem item) {
        FirebaseDatabase.getInstance().getReference("events").child(event.getId())
                .child("shoppingItemList").child(item.getId()).setValue(item);
    }
}
