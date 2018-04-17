package rs.ac.uns.ftn.pma.event_organizer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity  {

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
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_events_toolbar);
        myToolbar.showOverflowMenu();

        ImageView profileImage = (ImageView) findViewById(R.id.my_events_profile_image);
        profileImage.setImageResource(R.drawable.profile);

        profileImage.setClickable(true);

        MyEventsListAdapter adapter = new MyEventsListAdapter(this, event_name, event_date);
        list = (ListView) findViewById(R.id.list);
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
