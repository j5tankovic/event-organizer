package rs.ac.uns.ftn.pma.event_organizer.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationActivity;
import rs.ac.uns.ftn.pma.event_organizer.activity.InvitationsActivity;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;
import rs.ac.uns.ftn.pma.event_organizer.model.PlaceOffer;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralInvitationFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private Event event;
    private MapView mapView;
    private GoogleMap gMap;
    private PlaceOffer placeOffer;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private StorageReference storageReference;
    private ImageView imageView;

    public GeneralInvitationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_general_invitation, container, false);

        event=InvitationActivity.event;

        ((TextView)view.findViewById(R.id.invitation_info_name)).setText(event.getName());
        String date1str="";
        if(event.getStartDateTime()!=null)
           date1str= new SimpleDateFormat("dd/MM/yyyy").format(event.getStartDateTime());
        String date2str ="";
        if(event.getEndDateTime()!=null)
            date2str=new SimpleDateFormat("dd/MM/yyyy").format(event.getEndDateTime());
        ((TextView)view.findViewById(R.id.invitation_info_date)).setText(date1str+" - "+date2str);
        ((TextView)view.findViewById(R.id.invitation_info_description)).setText(event.getDescription());
        if(event.getEventCategory()!=null) {
            ((TextView) view.findViewById(R.id.invitation_info_category)).setText(event.getEventCategory().getName());
        }
        ((TextView)view.findViewById(R.id.invitation_info_creator)).setText(event.getCreator().getName()+" "+event.getCreator().getLastName());

        if(event.getFinalPlace()!=null) {
            placeOffer = (PlaceOffer) event.getFinalPlace();
            Bundle mapViewBundle = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
            }

       //     mapView = view.findViewById(R.id.invitation_info_mapview);
            mapView.onCreate(mapViewBundle);
            mapView.getMapAsync(this);
       //     ((TextView)view.findViewById(R.id.invitation_info_location)).setText(event.getFinalPlace().getLocationName());
        }

        imageView = view.findViewById(R.id.invitation_image);

        if (event.getImage() != null){
            storageReference = FirebaseStorage.getInstance().getReference().child(event.getImage());

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
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMinZoomPreference(12);

        LatLng coords = new LatLng(placeOffer.getLocation().getLat(), placeOffer.getLocation().getLng());
        gMap.addMarker(new MarkerOptions().position(coords).title(placeOffer.getLocation().getAddress()));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(coords));
    }

}
