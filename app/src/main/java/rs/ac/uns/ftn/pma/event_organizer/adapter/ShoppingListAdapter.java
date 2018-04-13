package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<String> testData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.shoppingitem_tv);
        }

    }

    public ShoppingListAdapter(List<String> testData) {
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
        holder.textView.setText(testData.get(position));
    }

    @Override
    public int getItemCount() {
        return testData.size();
    }
}
