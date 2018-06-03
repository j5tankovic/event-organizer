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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.PlaceOfferOverviewActivity;
import rs.ac.uns.ftn.pma.event_organizer.listener.ClickListener;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;

public class PlaceOffersAdapter extends RecyclerView.Adapter<PlaceOffersAdapter.ViewHolder> {
    private ClickListener clickListener;
    private List<PlaceOffer> testSet;
    private Event event;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView;
        public TextView finalPlaceTextView;
        public ImageButton acceptButton;
        public ImageButton rejectButton;
        public WeakReference<ClickListener> listenerRef;
        public boolean isFinalPlace;

        public ViewHolder(View v, ClickListener clickListener) {
            super(v);

            listenerRef = new WeakReference<>(clickListener);
            textView = v.findViewById(R.id.placeoffer_tv);
            finalPlaceTextView = v.findViewById(R.id.placeoffer_final);
            acceptButton = v.findViewById(R.id.placeoffer_accept);
            rejectButton = v.findViewById(R.id.placeoffer_reject);

            v.setOnClickListener(this);
            acceptButton.setOnClickListener(this);
            rejectButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.placeoffer_accept) {
                openConfirmationAboutAcceptance(v, getAdapterPosition());
            } else if (v.getId() == R.id.placeoffer_reject) {
                openConfirmationAboutRejecting(v, getAdapterPosition());
            } else {
                listenerRef.get().onPositionClicked(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }

    public PlaceOffersAdapter(List<PlaceOffer> testSet, ClickListener clickListener, Event event) {
        this.testSet = testSet;
        this.clickListener = clickListener;
        this.event = event;
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
          holder.textView.setText(testSet.get(position).getLocationName());
          if (oneIsAcceptedAsFinal()) {
              if (testSet.get(position).isAccepted()) {
                  holder.acceptButton.setVisibility(View.GONE);
                  holder.rejectButton.setVisibility(View.GONE);
                  holder.finalPlaceTextView.setVisibility(View.VISIBLE);
                  holder.finalPlaceTextView.setText(R.string.event_final_place);
              } else {
                  holder.acceptButton.setVisibility(View.GONE);
                  holder.rejectButton.setVisibility(View.GONE);
                  holder.finalPlaceTextView.setVisibility(View.GONE);
              }
          }
    }

    @Override
    public int getItemCount() {
        return testSet.size();
    }

    private void openConfirmationAboutAcceptance(View v, final int position) {
        final Context context = v.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Accept this place offer as final?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateFinalOffer(testSet.get(position));
                        Toast.makeText(context, "Accepted place offer", Toast.LENGTH_LONG).show();
                        long budget = updateBudget(testSet.get(position), event);
                        Toast.makeText(context, "Your budget is now:" + budget, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void openConfirmationAboutRejecting(View v, final int position) {
        final Context context = v.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setMessage("Remove this place offer as potential event place?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rejectPlaceOffer(testSet.get(position));
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

    private void updateFinalOffer(PlaceOffer offer) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("events").child(event.getId())
                .child("finalPlace");
        String key = dbReference.push().getKey();
        dbReference.child(key).setValue(offer);

        FirebaseDatabase.getInstance().getReference("events").child(event.getId())
                .child("potentialPlaces").child(offer.getId()).child("accepted").setValue(true);
    }

    private long updateBudget(PlaceOffer offer, Event event) {
        long offerPrice = offer.getPrice();
        long newEventBudget = event.getBudget() - offerPrice;
        event.setBudget(newEventBudget);
        FirebaseDatabase.getInstance().getReference("events").child(event.getId())
                .child("budget").setValue(newEventBudget);
        return newEventBudget;
    }

    private void rejectPlaceOffer(PlaceOffer offer) {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("events").child(event.getId())
                .child("potentialPlaces");
        dbReference.child(offer.getId()).setValue(null);
    }

    private boolean oneIsAcceptedAsFinal() {
        for (PlaceOffer po: testSet) {
            if (po.isAccepted()) {
                return true;
            }
        }
        return false;
    }
}
