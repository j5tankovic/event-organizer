package rs.ac.uns.ftn.pma.event_organizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddNewEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);
    }

    public void onClick(View view) {

        Intent intent = new Intent(AddNewEventActivity.this, EventOverviewActivity.class);
        startActivity(intent);
    }
}
