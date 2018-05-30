package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.enums.InvitationStatus;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InvitationsAdapter extends ArrayAdapter<Invitation> {
    private Context context;
    private List<Invitation> invitations;
    private StorageReference storageReference;
    private ImageView imageView;

    public InvitationsAdapter(Context context, int resource, List<Invitation> invitations) {
        super(context, resource, invitations);
        this.context=context;
        this.invitations=invitations;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if(view==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.activity_invitation_list_view, null, true);
        }
        final Invitation inv=getItem(position);
        final int pos=position;
        if(inv!=null){
            TextView name = (TextView) view.findViewById(R.id.invitation_name);
            TextView date = (TextView) view.findViewById(R.id.invitation_date);
            final ImageButton button_acc=(ImageButton)view.findViewById(R.id.invitation_status_acc);
            final ImageButton button_rej=(ImageButton)view.findViewById(R.id.invitation_status_rej);
            button_acc.setFocusable(false);
            button_rej.setFocusable(false);

            if(inv.getStatus().equals(InvitationStatus.ACCEPTED)){
                button_acc.setImageResource(R.drawable.ic_check_green_24dp);
                button_rej.setImageResource(R.drawable.ic_close_gray_24dp);
           }else if (inv.getStatus().equals(InvitationStatus.REJECTED)){
                button_acc.setImageResource(R.drawable.ic_check_gray_24dp);
                button_rej.setImageResource(R.drawable.ic_close_red_24dp);
            }

            button_acc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button_acc.setImageResource(R.drawable.ic_check_green_24dp);
                    button_rej.setImageResource(R.drawable.ic_close_gray_24dp);
                    FirebaseDatabase.getInstance().getReference("invitations")
                            .child(String.valueOf(inv.getId())).child("status").setValue("ACCEPTED");
                }});

            button_rej.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button_rej.setImageResource(R.drawable.ic_close_red_24dp);
                    button_acc.setImageResource(R.drawable.ic_check_gray_24dp);
                    FirebaseDatabase.getInstance().getReference("invitations")
                            .child(String.valueOf(inv.getId())).child("status").setValue("REJECTED");
                }
            });


            name.setText(inv.getEvent().getName());
            String date1str="";
            String date2str="";
            if(inv.getEvent().getStartDateTime()!=null)
                date1str = new SimpleDateFormat("dd/MM/yyyy").format(inv.getEvent().getStartDateTime());
            if(inv.getEvent().getEndDateTime()!=null)
                date2str = new SimpleDateFormat("dd/MM/yyyy").format(inv.getEvent().getEndDateTime());

            date.setText(date1str+" - "+date2str);

            imageView = view.findViewById(R.id.invitation_image);

            if (inv.getEvent().getImage() != null){
                storageReference = FirebaseStorage.getInstance().getReference().child(inv.getEvent().getImage());

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
        }
        return view;
    };

}
