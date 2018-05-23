package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.EventCategory;

public class AddNewEventActivity extends AppCompatActivity {

    public static final String ADDED_ITEM = "rs.ac.uns.ftn.pma.event_organizer.ADDED_ITEM";

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);


        Button add = findViewById(R.id.add_new_event);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = formEvent();
                save(event);
                formResult(event);
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("events");
    }

    private Event formEvent() {
        TextView name = findViewById(R.id.new_event_name);
        TextView description = findViewById(R.id.new_event_description);
        TextView start_date = findViewById(R.id.new_event_start_date);
        TextView end_date = findViewById(R.id.new_event_end_date);
        TextView budget = findViewById(R.id.new_event_budget);

        System.out.println(name.getText().toString());
        System.out.println(description.getText().toString());
        System.out.println(start_date.getText().toString());
        System.out.println(end_date.getText().toString());

        Event event = new Event();
        event.setName(name.getText().toString());
        event.setDescription(description.getText().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String start_date_string = start_date.getText().toString();
        Date start_date_date = null;
        String end_date_string = end_date.getText().toString();
        Date end_date_date = null;
        try {
            start_date_date = formatter.parse(start_date_string);
            end_date_date = formatter.parse(end_date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        event.setStartDateTime(start_date_date);
        event.setEndDateTime(end_date_date);
        event.setBudget(Double.valueOf(budget.getText().toString()));

        return event;
    }

    private void formResult(Event event) {
        Intent i = new Intent();
        i.putExtra(ADDED_ITEM, event);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(Event event) {
        String key = databaseReference.push().getKey();

        event.setId(key);
        databaseReference.child(key).setValue(event);
    }

}
