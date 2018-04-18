package rs.ac.uns.ftn.pma.event_organizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import rs.ac.uns.ftn.pma.event_organizer.adapter.MyEventsListAdapter;

public class MyEventsActivity extends Activity {

    ListView list;

    String[] event_name ={
            "",
            "Moj rodjendan",
            "Pidzama party",
            "Movie night",
            "Rostilj"
    };
    String[] event_date ={
            "",
            "15.04.2018",
            "20.04.2018",
            "20.04.2018",
            "15.04.2018"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events_app_bar_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_events_toolbar);
        myToolbar.showOverflowMenu();

        ImageView profileImage = (ImageView) findViewById(R.id.my_events_profile_image);
        profileImage.setImageResource(R.drawable.profile);

        profileImage.setClickable(true);

        MyEventsListAdapter adapter = new MyEventsListAdapter(this, event_name, event_date);
        list = (ListView) findViewById(R.id.my_events_list);
        list.setAdapter(adapter);

        /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String selectedEvent = event_name[+position];
                Toast.makeText(getApplicationContext(), selectedEvent, Toast.LENGTH_SHORT).show();

            }
        });*/

    }

}
