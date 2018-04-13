package rs.ac.uns.ftn.pma.event_organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class MyEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_events_toolbar);
        myToolbar.showOverflowMenu();
        setSupportActionBar(myToolbar);

        ImageView profileImage = (ImageView) findViewById(R.id.my_events_profile_image);
        profileImage.setImageResource(R.drawable.profile);

        profileImage.setClickable(true);
    }
}
