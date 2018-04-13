package rs.ac.uns.ftn.pma.event_organizer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MyEventsListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] event_name;
    private final String[] event_date;

    public MyEventsListAdapter(Activity context, String[] event_name, String[] event_date) {
        super(context, R.layout.activity_my_events_list_view, event_name);

        this.context = context;
        this.event_name = event_name;
        this.event_date = event_date;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_my_events_list_view, null,true);

        TextView name = (TextView) rowView.findViewById(R.id.event_name);
        TextView date = (TextView) rowView.findViewById(R.id.event_date);


        name.setText(event_name[position]);
        date.setText(event_date[position]);
        return rowView;

    };
}
