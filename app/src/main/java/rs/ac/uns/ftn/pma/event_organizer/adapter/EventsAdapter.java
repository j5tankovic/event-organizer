package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;

public class EventsAdapter extends ArrayAdapter<Event> {

    private final Activity context;
    private List<Event> events;

    private StorageReference storageReference;

    TextView name;
    TextView date;
    ImageView imageView;

    public EventsAdapter(Activity context, int resource, List<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.events = events;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_events_list_item, null,true);

        name = rowView.findViewById(R.id.event_name);
        date = rowView.findViewById(R.id.event_date);
        imageView = rowView.findViewById(R.id.event_image);

        name.setText(events.get(position).getName());

        String date1str = "";
        String date2str = "";
        if(events.get(position).getStartDateTime() != null)
            date1str = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(events.get(position).getStartDateTime());
        if(events.get(position).getEndDateTime() != null)
            date2str = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(events.get(position).getEndDateTime());

        date.setText(date1str + " - " + date2str);

        if (events.get(position).getImage() != null){
            storageReference = FirebaseStorage.getInstance().getReference().child(events.get(position).getImage());

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String imageURL = uri.toString();
                    GlideApp.with(context)
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
        return rowView;

    };



}
