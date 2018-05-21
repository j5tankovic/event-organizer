package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import rs.ac.uns.ftn.pma.event_organizer.R;

public class InvitationsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] invitation_name;
    private final String[] invitation_date;

    public InvitationsAdapter(Activity context, String[] invitation_name, String[] invitation_date) {
        super(context, R.layout.activity_invitation_list_view, invitation_name);

        this.context = context;
        this.invitation_name = invitation_name;
        this.invitation_date = invitation_date;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_invitation_list_view, null,true);

        TextView name = (TextView) rowView.findViewById(R.id.invitation_name);
        TextView date = (TextView) rowView.findViewById(R.id.invitation_date);


        name.setText(invitation_name[position]);
        date.setText(invitation_date[position]);
        return rowView;

    };

}
