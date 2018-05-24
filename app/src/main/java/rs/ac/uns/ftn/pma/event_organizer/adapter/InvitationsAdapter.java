package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;

public class InvitationsAdapter extends ArrayAdapter<Invitation> {
    private Context context;
    private List<Invitation> invitations;

    public InvitationsAdapter(Context context, int resource, List<Invitation> invitations) {
        super(context, resource, invitations);
        this.context=context;
        this.invitations=invitations;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.activity_invitation_list_view, null, true);
        }
        Invitation inv=getItem(position);
        if(inv!=null){
            TextView name = (TextView) view.findViewById(R.id.invitation_name);
            TextView date = (TextView) view.findViewById(R.id.invitation_date);

            name.setText(inv.getEvent().getName());
            String date1str="";
            String date2str="";
            if(inv.getEvent().getStartDateTime()!=null)
                date1str = new SimpleDateFormat("dd/MM/yyyy").format(inv.getEvent().getStartDateTime());
            if(inv.getEvent().getEndDateTime()!=null)
                date2str = new SimpleDateFormat("dd/MM/yyyy").format(inv.getEvent().getEndDateTime());

            date.setText(date1str+" - "+date2str);
        }
        return view;
    };

}
