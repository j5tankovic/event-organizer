package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.listener.ClickListener;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class PlaceOffersAdapter extends RecyclerView.Adapter<PlaceOffersAdapter.ViewHolder> {
    private ClickListener clickListener;
    private List<PlaceOffer> testSet;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView;
        public WeakReference<ClickListener> listenerRef;

        public ViewHolder(View v, ClickListener clickListener) {
            super(v);

            listenerRef = new WeakReference<>(clickListener);
            textView = (TextView) v.findViewById(R.id.placeoffer_tv);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }

    public PlaceOffersAdapter(List<PlaceOffer> testSet, ClickListener clickListener) {
        this.testSet = testSet;
        this.clickListener = clickListener;
    }

    @Override
    public PlaceOffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_offers_item_view, parent, false);

        ViewHolder vh = new ViewHolder(v, clickListener);
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
