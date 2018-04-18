package rs.ac.uns.ftn.pma.event_organizer.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.ShoppingItem;

public class GeneralFragment extends Fragment {
    public static final String GENERAL = "rs.ac.uns.ftn.pma.event_organizer.GENERAL";

    private View view;

    public GeneralFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.event_fragment_general, container, false);

        TextView name = view.findViewById(R.id.event_name);
        name.setText("Pidzama party");
        return view;
    }



}
