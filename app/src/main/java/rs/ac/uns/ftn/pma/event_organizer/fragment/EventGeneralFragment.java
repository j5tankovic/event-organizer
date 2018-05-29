package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.EventsActivity;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventGeneralFragment extends Fragment {

    public static final String SELECTED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.SELECTED_EVENT";

    private StorageReference storageReference;

    private View view;

    private TextView event_name;
    private TextView event_description;
    private TextView event_start_date;
    private TextView event_end_date;
    private TextView event_budget;
    private TextView event_category;
    private ImageView imageView;

    public EventGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_event_general, container, false);

        Event selectedEvent = (Event) getActivity().getIntent().getExtras().get(EventsActivity.SELECTED_EVENT);
        System.out.println(selectedEvent);

        event_name = view.findViewById(R.id.event_name);
        event_name.setText(selectedEvent.getName());

        event_description = view.findViewById(R.id.event_description);
        event_description.setText(selectedEvent.getDescription());

        event_start_date = view.findViewById(R.id.event_start_date);
        String startDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date((long) selectedEvent.getStartDateTime().getTime()));
        event_start_date.setText(startDate);

        event_end_date = view.findViewById(R.id.event_end_date);
        String endDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date((long) selectedEvent.getEndDateTime().getTime()));
        event_end_date.setText(endDate);

        event_budget = view.findViewById(R.id.event_budget);
        event_budget.setText(Objects.toString(selectedEvent.getBudget()));

        event_category =  view.findViewById(R.id.event_category);
        event_category.setText(selectedEvent.getEventCategory().getName());

        imageView = view.findViewById(R.id.event_image);

        if (selectedEvent.getImage() != null){
            storageReference = FirebaseStorage.getInstance().getReference().child(selectedEvent.getImage());

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageURL = uri.toString();
                    GlideApp.with(getContext())
                            .load(imageURL)
                            .placeholder(R.drawable.background)
                            .into(imageView)
                    ;
                    System.out.println("Event picture loaded!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("Failed to load a picture!");
                }
            });
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

}
