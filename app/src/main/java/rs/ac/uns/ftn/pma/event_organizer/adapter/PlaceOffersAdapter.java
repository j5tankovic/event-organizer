package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.PlaceOfferOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.listener.ClickListener;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class PlaceOffersAdapter extends RecyclerView.Adapter<PlaceOffersAdapter.ViewHolder> {
    private ClickListener clickListener;
    private List<PlaceOffer> testSet;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView;
        public ImageButton acceptButton;
        public ImageButton rejectButton;
        public WeakReference<ClickListener> listenerRef;

        public ViewHolder(View v, ClickListener clickListener) {
            super(v);

            listenerRef = new WeakReference<>(clickListener);
            textView = v.findViewById(R.id.placeoffer_tv);
            acceptButton = v.findViewById(R.id.placeoffer_accept);
            rejectButton = v.findViewById(R.id.placeoffer_reject);

            v.setOnClickListener(this);
            acceptButton.setOnClickListener(this);
            rejectButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.placeoffer_accept) {
                openConfirmationAboutAcceptance(v);
            } else if (v.getId() == R.id.placeoffer_reject) {
                openConfirmationAboutRejecting(v);
            } else {
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
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
//          holder.textView.setText(testSet.get(position).getLocationName());
    //    holder.textView.setText(testSet.get(position).getLocation().getName()); //PUCA
    }

    @Override
    public int getItemCount() {
        return testSet.size();
    }

    private void openConfirmationAboutAcceptance(View v) {
        final Context context = v.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Accept this place offer as final?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Accepted place offer", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void openConfirmationAboutRejecting(View v) {
        final Context context = v.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Remove this place offer as potential event place?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Rejected place offer", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
}
