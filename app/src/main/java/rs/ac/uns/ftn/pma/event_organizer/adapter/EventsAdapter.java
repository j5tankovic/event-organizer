package rs.ac.uns.ftn.pma.event_organizer.adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;

public class EventsAdapter extends ArrayAdapter<Event> {

    private final Activity context;
    private List<Event> events;

    public EventsAdapter(Activity context, int resource, List<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.events = events;
    }


    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_my_events_list_item, null,true);

        TextView name = (TextView) rowView.findViewById(R.id.event_name);
        TextView date = (TextView) rowView.findViewById(R.id.event_date);

        name.setText(events.get(position).getName());

        String date1str = "";
        String date2str = "";
        if(events.get(position).getStartDateTime() != null)
            date1str = new SimpleDateFormat("dd.MM.yyyy").format(events.get(position).getStartDateTime());
        if(events.get(position).getEndDateTime() != null)
            date2str = new SimpleDateFormat("dd.MM.yyyy").format(events.get(position).getEndDateTime());

        date.setText(date1str + " - " + date2str);

        return rowView;

    };



}
