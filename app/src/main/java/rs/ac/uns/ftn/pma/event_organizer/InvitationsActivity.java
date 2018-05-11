package rs.ac.uns.ftn.pma.event_organizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import rs.ac.uns.ftn.pma.event_organizer.adapter.InvitationsAdapter;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.Invitation;

public class InvitationsActivity extends AppCompatActivity {
    ListView list;
    ArrayList<Invitation> testData;
    String[] invitation_name ={
            "",
            "Moj rodjendan",
            "Pidzama party",
            "Movie night",
            "Rostilj"
    };
    String[] invitation_date ={
            "",
            "15.04.2018",
            "20.04.2018",
            "20.04.2018",
            "15.04.2018"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        InvitationsAdapter adapter = new InvitationsAdapter(this, invitation_name, invitation_date);
        list = (ListView) findViewById(R.id.invitations_list);
        list.setAdapter(adapter);
    }

}
