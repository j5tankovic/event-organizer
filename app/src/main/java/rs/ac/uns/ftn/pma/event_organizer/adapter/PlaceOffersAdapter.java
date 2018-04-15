package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class PlaceOffersAdapter extends RecyclerView.Adapter<PlaceOffersAdapter.ViewHolder> {
    private List<PlaceOffer> testSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.placeoffer_tv);
        }
    }

    public PlaceOffersAdapter(List<PlaceOffer> testSet) {
        this.testSet = testSet;
    }

    @Override
    public PlaceOffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_offers_item_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(testSet.get(position).getLocation().getAddress());
    }

    @Override
    public int getItemCount() {
        return testSet.size();
    }
}
